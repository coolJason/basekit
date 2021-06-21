package com.moregood.kit.utils;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Created by Devin.Ding on 2020/8/28 11:31.
 * Descripe:
 */
public class StringUtil {


    /**
     * 在字符串中每两个字符之间添加一个空格
     *
     * @param replace
     * @return
     */
    public static String getFileAddSpace(String replace) {
        String regex = "(.{2})";
        replace = replace.replaceAll(regex, "$1 ");
        return replace;
    }


    public static String getFileName(String file) {
        int start = file.lastIndexOf("/");
        int end = file.lastIndexOf(".");
        if (start < 0 && end < 0) {
            return file;
        }
        if (start < 0) {
            return file.substring(0, end);
        }
        if (end < 0) {
            return file.substring(start + 1);
        }
        return file.substring(start + 1, end);
    }

    public static String getFileNameAndExtension(String file) {
        int start = file.lastIndexOf("/");

        if (start < 0) {
            return file;
        }
        return file.substring(start + 1);

    }

    public static String getFileExtension(String file) {
        int start = file.lastIndexOf(".");
        if (start < 0) {
            return "";
        }
        return file.substring(start + 1);

    }

    public static String hashKeyForCache(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {
        return formatFileSize(fileS, 1024);
    }

    public static String formatFileSize(long fileS, int base) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        long base2 = base * base;
        long base3 = base * base * base;

        if (fileS < base) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < base2) {
            fileSizeString = df.format((double) fileS / base) + "KB";
        } else if (fileS < base3) {
            fileSizeString = df.format((double) fileS / base2) + "MB";
        } else {
            df = new DecimalFormat("#.00");
            fileSizeString = df.format((double) fileS / base3) + "GB";
        }
        return fileSizeString;
    }

    public static String formatFileGBSize(long fileS) {
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        int base = 1024;
        long base2 = base * base;
        long base3 = base * base * base;

//        if (fileS < base) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < base2) {
//            fileSizeString = df.format((double) fileS / base) + "KB";
//        } else if (fileS < base3) {
//            fileSizeString = df.format((double) fileS / base2) + "MB";
//        } else {
        fileSizeString = String.format("%.1fG", (double) fileS / base3);
//        }
        return fileSizeString;
    }

    /**
     * 转换文件大小，这里将数字和单位分开显示
     *
     * @param fileS
     * @return
     */
    public static String[] formatFileUnitSize(long fileS) {
        return formatFileUnitSize(fileS, 1024);
    }

    /**
     * 转换文件大小，这里将数字和单位分开显示
     *
     * @param fileS
     * @param base
     * @return
     */
    public static String[] formatFileUnitSize(long fileS, int base) {
        DecimalFormat df = new DecimalFormat("#.0");
        if (fileS == 0) {
            return new String[]{"0", "B"};
        }
        long base2 = base * base;
        long base3 = base * base * base;

        if (fileS < base) {
            return new String[]{df.format((double) fileS), "B"};
        } else if (fileS < base2) {
            return new String[]{df.format((double) fileS / base), "KB"};
        } else if (fileS < base3) {
            return new String[]{df.format((double) fileS / base2), "MB"};
        } else {
            df = new DecimalFormat("#.00");
            return new String[]{df.format((double) fileS / base3), "GB"};
        }
    }

    /**
     * 转换文件大小，这里将数字和单位分开显示
     *
     * @param fileS
     * @param floatFormat 保留几位小数点的格式 例: #.00
     * @return
     */
    public static String[] formatFileUnitSize(long fileS, String floatFormat) {
        DecimalFormat df = new DecimalFormat(floatFormat);
        if (fileS == 0) {
            return new String[]{"0", "B"};
        }
        long base2 = 1024 * 1024;
        long base3 = 1024 * 1024 * 1024;

        if (fileS < 1024) {
            return new String[]{df.format((double) fileS), "B"};
        } else if (fileS < base2) {
            return new String[]{df.format((double) fileS / 1024), "KB"};
        } else if (fileS < base3) {
            return new String[]{df.format((double) fileS / base2), "MB"};
        } else {
            return new String[]{df.format((double) fileS / base3), "GB"};
        }
    }


    public static CharSequence createMultiColorCharSequencePlaceholder(Resources resources, int allTextId, int allTextColor, TextColor... subTextColors) {
        StringBuilder allText = new StringBuilder(resources.getString(allTextId));
        if (allText.indexOf("%s") >= 0) {
            for (TextColor tc : subTextColors) {
                int placeholderIndex = allText.indexOf("%s");
                if (placeholderIndex >= 0) {
                    allText.replace(placeholderIndex, placeholderIndex + 2, tc.getText(resources));
                }
            }
            return createMultiColorCharSequence(resources, allText.toString(), allTextColor, subTextColors);
        } else {
            return createMultiColorCharSequence(resources, allText.toString(), allTextColor, subTextColors);
        }
    }

    public static CharSequence createMultiColorCharSequence(Resources resources, String allText, int allTextColor, TextColor... subTextColors) {
        SpannableStringBuilder builder = new SpannableStringBuilder(allText);
        try {
            ForegroundColorSpan allSpan = new ForegroundColorSpan(getColor(resources, allTextColor));
            builder.setSpan(allSpan, 0, allText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            if (subTextColors != null) {
                for (TextColor tc : subTextColors) {
                    String text = tc.getText(resources);
                    int startIndex = allText.indexOf(text);
                    if (startIndex >= 0) {
                        int endIndex = startIndex + text.length();
                        int color = getColor(resources, tc.getColor());
                        ForegroundColorSpan span = new ForegroundColorSpan(color);
                        builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
        } catch (Exception e) {
            Logger.d("createMultiColorCharSequence>>" + e.getMessage());
        }
        return builder;
    }

    private static int getColor(Resources resources, int color) {
        int c = -1;
        try {
            c = resources.getColor(color);
        } catch (Exception e) {
            c = color;
        }
        return c;
    }

}