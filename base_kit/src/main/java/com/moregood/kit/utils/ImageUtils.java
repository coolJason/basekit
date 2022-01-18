/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.moregood.kit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.moregood.kit.R;
import com.moregood.kit.base.BaseActivity;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ImageUtils {

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		return getRoundedCornerBitmap(bitmap, 6);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 获取视频的缩略图
	 * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND,则类型要使用MICRO_KIND作为kind的值，这样会节省内存
	 * @param videoPath 视频的路径
	 * @param width 指定输出视频缩略图的宽度
	 * @param height 指定输出视频缩略图的高度
	 * @param kind 参照MediaStore。Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND.
	 * 			   其中，MINI_KIND:512*384,MICRO_KIND:96*96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath,int width,int height,int kind)
	{
		Bitmap bitmap=null;
		//获取视频的缩略图
		bitmap= ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		Log.d("getVideoThumbnail", "video thumb width:"+bitmap.getWidth());
		Log.d("getVideoThumbnail", "video thumb height:"+bitmap.getHeight());
		bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}


	/**
	 * 保存video的缩略图
	 * @param videoFile 视频文件
	 * @param width 指定输出视频缩略图的宽度
	 * @param height 指定输出视频缩略图的高度
	 * @param kind 参照MediaStore。Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND.
	 * 			   其中，MINI_KIND:512*384,MICRO_KIND:96*96
	 * @return 缩略图绝对路径
	 */
	public static String saveVideoThumb(File videoFile, int width, int height, int kind) {
		Bitmap bitmap = getVideoThumbnail(videoFile.getAbsolutePath(), width, height, kind);
		File file = new File(PathUtil.getInstance().getVideoPath(), "th" + videoFile.getName());
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			if (fOut != null) {
				fOut.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (fOut != null) {
				fOut.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}




	public static Bitmap decodeScaleImage(String imagePath, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		BitmapFactory.Options options = getBitmapOptions(imagePath);

		// Calculate inSampleSize
		int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.d("img", "original wid" + options.outWidth + " original height:" + options.outHeight + " sample:"
				+ sampleSize);
		options.inSampleSize = sampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
		//图片旋转角度
		int degree = readPictureDegree(imagePath);
		Bitmap rotateBm = null;
		if(bm != null && degree != 0){
			rotateBm = rotateImageView(degree, bm);
			bm.recycle();
			bm = null;
			return rotateBm;
		}else{
			return bm;
		}
		// return BitmapFactory.decodeFile(imagePath, options);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public static Bitmap decodeScaleImage(Context context, Uri imageUri, int reqWidth, int reqHeight) throws IOException{
		// First decode with inJustDecodeBounds=true to check dimensions
		BitmapFactory.Options options = getBitmapOptions(context, imageUri);

		// Calculate inSampleSize
		int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.d("img", "original wid" + options.outWidth + " original height:" + options.outHeight + " sample:"
				+ sampleSize);
		options.inSampleSize = sampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bm = getBitmapByUri(context, imageUri, options);
		//图片旋转角度
		int degree = readPictureDegree(context, imageUri);
		Bitmap rotateBm = null;
		if(bm != null && degree != 0){
			rotateBm = rotateImageView(degree, bm);
			bm.recycle();
			bm = null;
			return rotateBm;
		}else{
			return bm;
		}
		// return BitmapFactory.decodeFile(imagePath, options);
	}

	public static Bitmap decodeScaleImage(Context context, int drawableId, int reqWidth, int reqHeight) {
		Bitmap bitmap;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), drawableId, options);
		@SuppressWarnings("UnnecessaryLocalVariable")
		int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;

		bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, options);
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static String getThumbnailImage(String imagePath, int thumbnailSize) {

	    /*
	    BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageWidth <= thumbnailSize && imageHeight <= thumbnailSize) {
	        //it is already small image within thumbnail required size. directly using it
	        return imagePath;
	    }*/

		Bitmap image = decodeScaleImage(imagePath, thumbnailSize, thumbnailSize);

		try {
			File tempFile = File.createTempFile("image", ".jpg");
			FileOutputStream stream = new FileOutputStream(tempFile);

			if (image != null) {
				image.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			}
			stream.close();
			Log.d("img", "generate thumbnail image at:" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
			return tempFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			//if any error, return original file
			return imagePath;
		}

	}

	/**
	 * 获取文件名
	 * @param context
	 * @param fileUri
	 * @return
	 */
	public static String getFilename(Context context, String fileUri) {
		if(TextUtils.isEmpty(fileUri)) {
			return "";
		}
		if(!UriUtils.isFileExistByUri(context, Uri.parse(fileUri))) {
			return "";
		}
		return UriUtils.getFileNameByUri(context, Uri.parse(fileUri));
	}

	/**
	 * 获取文件长度
	 * @param context
	 * @param fileUri
	 * @return
	 */
	public static long getFileLength(Context context, String fileUri) {
		if(TextUtils.isEmpty(fileUri)) {
			return 0;
		}
		return UriUtils.getFileLength(context, Uri.parse(fileUri));
	}

	/**
	 * 获取图片缩略图
	 * @param context
	 * @param localPath
	 * @return
	 */
	public static String getScaledImageByUri(Context context, String localPath) {
		if(TextUtils.isEmpty(localPath)) {
			return localPath;
		}
		Log.d("img", "original localPath: "+localPath);
		Uri localUri = Uri.parse(localPath);
		if(!UriUtils.isFileExistByUri(context, localUri)) {
			return localPath;
		}
		String filePath = UriUtils.getFilePath(context, localUri);
		if(!TextUtils.isEmpty(filePath)) {
			return getScaledImage(context, filePath);
		}else {
			Uri scaledImage = null;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
				scaledImage = getScaledImage(context, localUri);
			}
			return scaledImage == null ? localPath : scaledImage.toString();
		}

	}

	/**
	 * deu to the network bandwidth limitation, we will scale image to smaller
	 * size before send out
	 *
	 * @param, appContext, the application context to get file dirs for creating temp image file
	 * @param imagePath
	 * @return
	 */
	public static String getScaledImage(Context appContext, String imagePath) {
		// if file size is less than 100k, no need to scale, directly return
		// that file path. imagePath
		File originalFile = new File(imagePath);
		if (!originalFile.exists()) {
			// wrong input
			return imagePath;
		}
		long fileSize = originalFile.length();
		Log.d("img", "original img size:" + fileSize);
		if (fileSize <= 100 * 1024) {
			Log.d("img", "use original small image");
			return imagePath;
		}

		// scale image to required size
		Bitmap image = decodeScaleImage(imagePath, SCALE_IMAGE_WIDTH, SCALE_IMAGE_HEIGHT);
		// save image to a temp file
		try {
			/*
			 * String extension =
			 * imagePath.substring(imagePath.lastIndexOf(".")+1); String imgExt
			 * = null; if (extension.equalsIgnoreCase("jpg")) { imgExt = "jpg";
			 * } else { imgExt = "png"; }
			 */
			File tempFile = File.createTempFile("image", ".jpg", appContext.getFilesDir());
			FileOutputStream stream = new FileOutputStream(tempFile);

			if (image != null) {
				image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
			}
			/*
			 * if (extension.equalsIgnoreCase("jpg") ||
			 * extension.equalsIgnoreCase("jpeg")) {
			 * image.compress(Bitmap.CompressFormat.JPEG, 60, stream); } else {
			 * image.compress(Bitmap.CompressFormat.PNG, 60, stream); }
			 */
			stream.close();
			Log.d("img", "compared to small fle" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
			return tempFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return imagePath;
	}

	/**
	 * deu to the network bandwidth limitation, we will scale image to smaller
	 * size before send out
	 *
	 * @param, appContext, the application context to get file dirs for creating temp image file
	 * @param imageUri
	 * @return
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static Uri getScaledImage(Context appContext, Uri imageUri) {
		// if file size is less than 100k, no need to scale, directly return
		// that file path. imagePath
		if (imageUri == null) {
			// wrong input
			return imageUri;
		}
		try {
			long fileSize = UriUtils.getFileLength(appContext, imageUri);
//			Log.d("img", "original img size:" + fileSize);
			if (fileSize <= 100 * 1024) {
				Log.d("img", "use original small image");
				return imageUri;
			}

			// scale image to required size
			Bitmap image = decodeScaleImage(appContext, imageUri, SCALE_IMAGE_WIDTH, SCALE_IMAGE_HEIGHT);
			// save image to a temp file

			/*
			 * String extension =
			 * imagePath.substring(imagePath.lastIndexOf(".")+1); String imgExt
			 * = null; if (extension.equalsIgnoreCase("jpg")) { imgExt = "jpg";
			 * } else { imgExt = "png"; }
			 */
			File tempFile = File.createTempFile("image", ".jpg", appContext.getFilesDir());
			FileOutputStream stream = new FileOutputStream(tempFile);

			if (image != null) {
				image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
			}
			/*
			 * if (extension.equalsIgnoreCase("jpg") ||
			 * extension.equalsIgnoreCase("jpeg")) {
			 * image.compress(Bitmap.CompressFormat.JPEG, 60, stream); } else {
			 * image.compress(Bitmap.CompressFormat.PNG, 60, stream); }
			 */
			stream.close();
			Log.d("img", "compared to small fle" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
			return Uri.fromFile(tempFile);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 得到"eaemobTemp"+i.jpg为文件名的临时图片
	 * @param imagePath
	 * @param i
	 * @return
	 */

	public static String getScaledImage(Context appContext, String imagePath,int i) {
//		List<String> temPaths = new ArrayList<String>();
//		for (int i = 0; i < imageLocalPaths.size(); i++) {
		File originalFile = new File(imagePath);
		if (originalFile.exists()) {
			long fileSize = originalFile.length();
			Log.d("img", "original img size:" + fileSize);
			if (fileSize > 100 * 1024) {
				// scale image to required size
				Bitmap image = decodeScaleImage(imagePath, SCALE_IMAGE_WIDTH, SCALE_IMAGE_HEIGHT);
				// save image to a temp file
				try {
					File tempFile = new File(appContext.getExternalCacheDir(), "eaemobTemp" + i + ".jpg");
					FileOutputStream stream = new FileOutputStream(tempFile);
					if (image != null) {
						image.compress(Bitmap.CompressFormat.JPEG, 60, stream);
					}
					stream.close();
					Log.d("img",
							"compared to small fle" + tempFile.getAbsolutePath() + " size:" + tempFile.length());
					return tempFile.getAbsolutePath();
				} catch (Exception e) {
					e.printStackTrace();

				}
			}

//			}

//			temPaths.add(imagePath);

		}
		return imagePath;

	}


	/**
	 * merge multiple images into one the result will be 2*2 images or 3*3
	 * images
	 *
	 * @param targetWidth
	 * @param targetHeight
	 * @param images
	 * @return
	 */
	public static Bitmap mergeImages(int targetWidth, int targetHeight, List<Bitmap> images) {
		Bitmap mergeBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mergeBitmap);
		canvas.drawColor(Color.LTGRAY);
		Log.d("img", "merge images to size:" + targetWidth + "*" + targetHeight + " with images:" + images.size());
		int size;
		if (images.size() <= 4) {
			size = 2; // 2*2 images
		} else {
			size = 3; // 3*3 images
		}
		// draw 2*2 images
		// expect targeWidth == targetHeight
		int imgIdx = 0;
		int smallImageSize = (targetWidth - 4) / size;
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				// load image into small size
				Bitmap originalImage = images.get(imgIdx);
				Bitmap smallImage = Bitmap.createScaledBitmap(originalImage, smallImageSize, smallImageSize, true);

				Bitmap smallRoundedImage = getRoundedCornerBitmap(smallImage, 2);
				smallImage.recycle();
				// draw on merged canvas
				canvas.drawBitmap(smallRoundedImage, column * smallImageSize + (column + 2), row * smallImageSize
						+ (row + 2), null);
				smallRoundedImage.recycle();

				imgIdx++;
				if (imgIdx == images.size()) {
					return mergeBitmap;
				}
			}
		}

		return mergeBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param imageUri
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static int readPictureDegree(Context context, Uri imageUri) {
		int degree = 0;
		try {
			ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(imageUri, "r");
			ExifInterface exifInterface = new ExifInterface(parcelFileDescriptor.getFileDescriptor());
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
			parcelFileDescriptor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 *
	 * @param angle
	 *
	 * @param bitmap
	 *
	 * @return Bitmap
	 */
	public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		@SuppressWarnings("UnnecessaryLocalVariable")
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 获取图片的Options
	 * @param context
	 * @param fileUri
	 * @return
	 */
	public static BitmapFactory.Options getBitmapOptions(Context context, String fileUri) {
		if(TextUtils.isEmpty(fileUri)) {
			return null;
		}
		if(!UriUtils.isFileExistByUri(context, Uri.parse(fileUri))) {
			return null;
		}
		String filePath = UriUtils.getFilePath(context, fileUri);
		if(!TextUtils.isEmpty(filePath)) {
			return getBitmapOptions(filePath);
		}else {
			try {
				return getBitmapOptions(context, Uri.parse(fileUri));
			} catch (IOException e) {
				//e.printStackTrace();
				Log.e("img", "get bitmap options fail by "+e.getMessage());
			}
		}
		return null;
	}

	/**
	 * get bitmap options
	 * @param imagePath
	 * @return
	 */
	public static BitmapFactory.Options getBitmapOptions(String imagePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		return options;
	}

	/**
	 * get bitmap options
	 * @param context
	 * @param uri
	 * @return
	 */
	public static BitmapFactory.Options getBitmapOptions(Context context, Uri uri) throws IOException{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
		FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		parcelFileDescriptor.close();
		return options;
	}

	/**
	 * get bitmap by uri
	 * @param context
	 * @param uri
	 * @param options
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmapByUri(Context context, Uri uri, BitmapFactory.Options options) throws IOException{
		ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
		Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor(), null, options);
		parcelFileDescriptor.close();
		return bitmap;
	}

	public static Bitmap getBitmap(String url) {
		Bitmap bm = null;
		try {
			URL iconUrl = new URL(url);
			URLConnection conn = iconUrl.openConnection();
			HttpURLConnection http = (HttpURLConnection) conn;

			int length = http.getContentLength();

			conn.connect();
			// 获得图像的字符流
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, length);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();// 关闭流
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static final int SCALE_IMAGE_WIDTH = 640;
	public static final int SCALE_IMAGE_HEIGHT = 960;
	
	public static String getImagePath(String remoteUrl)
	{
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path = PathUtil.getInstance().getImagePath()+"/"+ imageName;
        Log.d("msg", "image path:" + path);
        return path;
		
	}

	public static String getImagePathByFileName(String filename)
	{
		String path = PathUtil.getInstance().getImagePath()+"/"+ filename;
        Log.d("msg", "image path:" + path);
        return path;

	}

	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path = PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        Log.d("msg", "thum image path:" + path);
        return path;
    }

	public static String getThumbnailImagePathByName(String filename) {
		String path = PathUtil.getInstance().getImagePath()+"/"+ "th"+filename;
        Log.d("msg", "thum image dgdfg path:" + path);
        return path;
    }

	/**
	 * 获取图片最大的长和宽
	 * @param context
	 */
	public static int[] getImageMaxSize(Context context) {
		float[] screenInfo = CommonUtils.getScreenInfo(context);
		int[] maxSize = new int[2];
		if(screenInfo != null) {
			maxSize[0] = (int) (screenInfo[0] / 3);
			maxSize[1] = (int) (screenInfo[0] / 2);
		}
		return maxSize;
	}
	//分享保存图片
	public static Bitmap saveSharePic(Context context, RelativeLayout relativelayout) {
		if (relativelayout == null) {
			return null;
		}
		try {
			int h = 0;
			// 获取ScrollView实际高度
			for (int i = 0; i < relativelayout.getChildCount(); i++) {
				h += relativelayout.getChildAt(i).getHeight();
				relativelayout.getChildAt(i).setBackgroundResource(R.color.white);
			}
			// 创建对应大小的bitmap
			Bitmap bitmap = Bitmap.createBitmap(relativelayout.getWidth(), h, Bitmap.Config.ARGB_8888);
			final Canvas canvas = new Canvas(bitmap);
			relativelayout.draw(canvas);
			// 保存图片
//                savePicture(DoctorMainActivity.this, bitmap);
			 saveImg(context,bitmap, context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "", System.currentTimeMillis() + ".jpg");
			return bitmap;
		} catch (Exception oom) {
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show();
				}
			});
			return null;
		}
	}
	/**
	 * 保存图片到SD卡
	 *
	 * @param bm         图片bitmap对象
	 * @param floderPath 下载文件保存目录
	 * @param fileName   文件名称(不带后缀)
	 */
	public static void saveImg(Context context,Bitmap bm, String floderPath, String fileName) throws IOException {
		try{
			File folder = new File(floderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			String savePath = folder.getPath() + File.separator + fileName + ".jpg";
			File file = new File(savePath);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("ProfileDetailsActivity", savePath + " 保存成功");
			bos.flush();
			bos.close();
			//通知相册刷新
			// 其次把文件插入到系统图库
			try {
				MediaStore.Images.Media.insertImage(context.getContentResolver(),
						file.getAbsolutePath(), fileName, null);
			} catch (FileNotFoundException e) {
				Log.i("TAG", "saveImg: ===============eeeeeeeeee===="+e);
				e.printStackTrace();
			}
			// 最后通知图库更新
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
//			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
		}catch (Exception e){
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
