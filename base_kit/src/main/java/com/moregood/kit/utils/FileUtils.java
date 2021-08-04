package com.moregood.kit.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @类名 FileUtils
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/17 08:47
 * @邮箱 ye_xi_feng@163.com
 */
public class FileUtils {
    public static String[] fileTypes = new String[]{"apk", "avi", "bmp", "chm", "dll", "doc", "docx", "dos", "gif",
            "html", "jpeg", "jpg", "movie", "mp3", "dat", "mp4", "mpe", "mpeg", "mpg", "pdf", "png", "ppt", "pptx", "rar",
            "txt", "wav", "wma", "wmv", "xls", "xlsx", "xml", "zip"};

    public static File[] loadFiles(File directory) {
        File[] listFiles = directory.listFiles();
        if (listFiles == null)
            listFiles = new File[]{};

        ArrayList<File> tempFolder = new ArrayList<File>();
        ArrayList<File> tempFile = new ArrayList<File>();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                tempFolder.add(file);
            } else if (file.isFile()) {
                tempFile.add(file);
            }
        }
        // sort list
        Comparator<File> comparator = new MyComparator();
        Collections.sort(tempFolder, comparator);
        Collections.sort(tempFile, comparator);

        File[] datas = new File[tempFolder.size() + tempFile.size()];
        System.arraycopy(tempFolder.toArray(new File[tempFolder.size()]), 0, datas, 0, tempFolder.size());
        System.arraycopy(tempFile.toArray(new File[tempFile.size()]), 0, datas, tempFolder.size(), tempFile.size());

        return datas;
    }

    /**
     * Determine the type of file
     *
     * @param f
     * @return
     */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();

        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
        return type;
    }

    public static String getMIMEType(String fileName) {
        String type = "";

        String end = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
        return type;
    }


    private static final String TAG = "FileUtils";

    /**
     * Please use easeui module/EaseCompat#openFile(File f, Activity context) for instead.
     *
     * @param f
     * @param context
     */
    @Deprecated
    public static void openFile(File f, Activity context) {
        /* get MimeType */
        String type = FileUtils.getMIMEType(f);
        /* get uri */
        Uri uri = getUriForFile(context, f);
        openFile(uri, type, context);
    }

    /**
     * Please use easeui module/EaseCompat#openFile(File f, String type, Activity context) for instead.
     *
     * @param uri
     * @param type
     * @param context
     */
    @Deprecated
    public static void openFile(Uri uri, String type, Activity context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        /* set intent's file and MimeType */
        intent.setDataAndType(uri, type);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Can't find proper app to open this file", Toast.LENGTH_LONG).show();
        }
    }

    // custom comparator
    public static class MyComparator implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }

    }

    public static Uri getUriForFile(Context context, File file) {
        // Build.VERSION_CODES.N = 24, 直接用会导致ant打包不成功
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }


    /**
     * 删除文件
     *
     * @param context
     * @param uri
     */
    public static void deleteFile(Context context, Uri uri) {
        if (UriUtils.isFileExistByUri(context, uri)) {
            String filePath = getFilePath(context, uri);
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
            } else {
                try {
                    context.getContentResolver().delete(uri, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        return deleteFile(new File(filePath));
    }

    /**
     * 删除多个文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFiles(List<File> filePath) {
        boolean deleteSuccess = false;
        if (filePath != null && filePath.size() > 0) {
            for (int i = 0; i < filePath.size(); i++) {
                deleteSuccess = deleteFile(filePath.get(i));
                if (!deleteSuccess) {
                    break;
                }
            }
        }
        return deleteSuccess;
    }

    /**
     * 删除单个文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        file.delete();
        return true;
    }

    /***
     * 清理所有缓存
     * @param context
     */
    public static boolean clearAllCache(Context context) {
        boolean success = deleteDir(context.getCacheDir());
        if (!success) {
            return false;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            success = deleteDir(context.getExternalCacheDir());
        }
        return success;
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取文件名
     *
     * @param context
     * @param fileUri
     * @return
     */
    public static String getFileNameByUri(Context context, Uri fileUri) {
        if (fileUri == null) {
            return "";
        }
        //target 小于Q
        if (!VersionUtils.isTargetQ(context)) {
            String filePath = getFilePath(context, fileUri);
            if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                return new File(filePath).getName();
            }
            return "";
        }
        //target 大于Q
        if (uriStartWithFile(fileUri)) {
            File file = new File(fileUri.getPath());
            return file.exists() ? file.getName() : "";
        }
        if (!uriStartWithContent(fileUri)) {
            if (fileUri.toString().startsWith("/") && new File(fileUri.toString()).exists()) {
                return new File(fileUri.toString()).getName();
            }
            return "";
        }

        return UriUtils.getFilenameByDocument(context, fileUri);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getFilePath(final Context context, final Uri uri) {
        if (uri == null) {
            return "";
        }
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        //sdk版本在29之前的
        if (!VersionUtils.isTargetQ(context)) {
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } else if (isFileProvider(context, uri)) {
                return getFPUriToPath(context, uri);
            } else if (isOtherFileProvider(context, uri)) {
                return copyFileProviderUri(context, uri);
            }
            // MediaStore (and general)
            else if (uriStartWithContent(uri)) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if (uriStartWithFile(uri)) {
                return uri.getPath();
            } else if (uri.toString().startsWith("/")) {//如果是路径的话，返回路径
                return uri.toString();
            }
        } else {
            //29之后，判断是否是file开头及是否是以"/"开头
            if (uriStartWithFile(uri)) {
                return uri.getPath();
            } else if (uri.toString().startsWith("/")) {
                return uri.toString();
            }
        }
        return "";
    }

    /**
     * 从FileProvider获取文件
     *
     * @param context
     * @param uri
     * @return
     */
    private static String copyFileProviderUri(Context context, Uri uri) {
        //如果是分享过来的文件，则将其写入到私有目录下
        String[] subs = uri.toString().split("/");
        String filename = null;
        if (subs.length > 0) {
            filename = subs[subs.length - 1];
        } else {
            return "";
        }
        String filePath = PathUtil.getInstance().getFilePath() + File.separator + filename;
        if (new File(filePath).exists()) {
            return filePath;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            out = new FileOutputStream(filePath);
            copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(filePath).exists() ? filePath : "";
    }

    public static long copy(@NonNull InputStream in, @NonNull OutputStream out) {
        long sum = 0;
        try {
            byte[] tmp = new byte[2048];
            int l;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
                sum += l;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * 判断uri是否以file开头
     *
     * @param fileUri
     * @return
     */
    public static boolean uriStartWithFile(Uri fileUri) {
        return "file".equalsIgnoreCase(fileUri.getScheme()) && fileUri.toString().length() > 7;
    }

    /**
     * 判断是否以content开头的Uri
     *
     * @param fileUri
     * @return
     */
    public static boolean uriStartWithContent(Uri fileUri) {
        return "content".equalsIgnoreCase(fileUri.getScheme());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 从FileProvider获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getFPUriToPath(Context context, Uri uri) {
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs != null) {
                String fileProviderClassName = FileProvider.class.getName();
                for (PackageInfo pack : packs) {
                    ProviderInfo[] providers = pack.providers;
                    if (providers != null) {
                        for (ProviderInfo provider : providers) {
                            if (uri.getAuthority().equals(provider.authority)) {
                                if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                    Class<FileProvider> fileProviderClass = FileProvider.class;
                                    try {
                                        Method getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context.class, String.class);
                                        getPathStrategy.setAccessible(true);
                                        Object invoke = getPathStrategy.invoke(null, context, uri.getAuthority());
                                        if (invoke != null) {
                                            String PathStrategyStringClass = FileProvider.class.getName() + "$PathStrategy";
                                            Class<?> PathStrategy = Class.forName(PathStrategyStringClass);
                                            Method getFileForUri = PathStrategy.getDeclaredMethod("getFileForUri", Uri.class);
                                            getFileForUri.setAccessible(true);
                                            Object invoke1 = getFileForUri.invoke(invoke, uri);
                                            if (invoke1 instanceof File) {
                                                String filePath = ((File) invoke1).getAbsolutePath();
                                                return filePath;
                                            }
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 是否是本app的FileProvider
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean isFileProvider(Context context, Uri uri) {
        return (context.getApplicationInfo().packageName + ".fileProvider").equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * 其他app分享过来的FileProvider
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean isOtherFileProvider(Context context, Uri uri) {
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(scheme) || TextUtils.isEmpty(authority)) {
            return false;
        }
        return !(context.getApplicationInfo().packageName + ".fileProvider").equalsIgnoreCase(uri.getAuthority())
                && "content".equalsIgnoreCase(uri.getScheme())
                && authority.contains(".fileProvider".toLowerCase());
    }


    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }
}

