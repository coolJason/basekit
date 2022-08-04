package com.moregood.kit.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Author: Svtar
 * Date: 2022/8/1 19:45
 * E-mail: 707390415@qq.com
 * Description: 价格统一格式工具类
 */
public class PriceFormatUtil {

    public static String format(float price) {
        Log.e("xxxxxxx", "float-price:" + price);
        try {
            return format(new BigDecimal("" + price), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(double price) {
        Log.e("xxxxxxx", "double-price:" + price);
        try {
            return format(new BigDecimal("" + price), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(int price) {
        Log.e("xxxxxxx", "int-price:" + price);
        try {
            return format(new BigDecimal("" + price), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(long price) {
        Log.e("xxxxxxx", "long-price:" + price);
        try {
            return format(new BigDecimal("" + price), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(String price) {
        Log.e("xxxxxxx", "String-price:" + price);
        try {
            return format(new BigDecimal(price), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(BigDecimal price) {
        Log.e("xxxxxxx", "BigDecimal-price:" + price.toPlainString());
        try {
            return format(price, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化金额-允许小数位末尾带0
     *
     * @param price 需要格式化的金额
     * @param unit  金额的单位，传空的话，默认取上次储存的单位
     * @return
     */
    public static String format(BigDecimal price, String unit) {
        Log.e("xxxxxxx", "price:" + price.toPlainString() + "|unit:" + unit + "|getUnit:" + CurrencyUnitUtil.getUnit());
        try {
            if (TextUtils.isEmpty(unit)) {
                unit = CurrencyUnitUtil.getUnit();
            }
            if (unit.equals(CurrencyUnitUtil.TYPE_UNI_IDR)) {
                //印尼货币，需要把金额转为整数,向下取整
                String priceIDR = price.setScale(0, RoundingMode.DOWN).toPlainString();
                //每隔三位加一个"."
                return strAddComma(priceIDR);
            } else {
                return compareNumber(price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化金额-不允许小数位末尾带0
     *
     * @param price 需要格式化的金额
     * @param unit  金额的单位，传空的话，默认取上次储存的单位
     * @return
     */
    public static String formatNoZeros(BigDecimal price, String unit) {
        Log.e("xxxxxxx", "price:" + price.toPlainString() + "|unit:" + unit + "|getUnit:" + CurrencyUnitUtil.getUnit());
        try {
            if (TextUtils.isEmpty(unit)) {
                unit = CurrencyUnitUtil.getUnit();
            }
            if (unit.equals(CurrencyUnitUtil.TYPE_UNI_IDR)) {
                //印尼货币，需要把金额转为整数,向下取整
                String priceIDR = price.setScale(0, RoundingMode.DOWN).toPlainString();
                //每隔三位加一个"."
                return strAddComma(priceIDR);
            } else {
                return compareNumberNoZeros(price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化数字-允许小数位末尾带0
     *
     * @param number 需要格式化的数字
     * @return
     */
    public static String compareNumber(BigDecimal number) {
        try {
            if (number != null) {
//                if (number.scale() > 2) {//超过2位小数，四舍五入只显示两位小数，并且去掉末尾的0
                    return number.setScale(2, RoundingMode.HALF_UP).toPlainString();
//                } else {//低于两位小数去掉末尾的0
//                    return number.toPlainString();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化数字-不允许小数位末尾带0
     *
     * @param number 需要格式化的数字
     * @return
     */
    public static String compareNumberNoZeros(BigDecimal number) {
        try {
            if (number != null) {
                if (number.scale() > 2) {//超过2位小数，四舍五入只显示两位小数，并且去掉末尾的0
                    return number.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
                } else {//低于两位小数去掉末尾的0
                    return number.stripTrailingZeros().toPlainString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将每三个数字（或字符）加上逗号处理（通常使用金额方面的编辑）
     * 5000000.00 --> 5,000,000.00
     * 20000000 --> 20,000,000
     *
     * @param str 无逗号的数字
     * @return 加上逗号的数字
     */
    public static String strAddComma(String str) {
        try {
            if (str == null) {
                str = "";
            }
//            String addCommaStr = ""; // 需要添加逗号的字符串（整数）
//            String tmpCommaStr = ""; // 小数，等逗号添加完后，最后在末尾补上
//            if (str.contains(".")) {
//                addCommaStr = str.substring(0,str.indexOf("."));
//                tmpCommaStr = str.substring(str.indexOf("."),str.length());
//            }else{
//                addCommaStr = str;
//            }
//            // 将传进数字反转
//            String reverseStr = new StringBuilder(addCommaStr).reverse().toString();
            // 将传进数字反转
            String reverseStr = new StringBuilder(str).reverse().toString();
            String strTemp = "";
            for (int i = 0; i < reverseStr.length(); i++) {
                if (i * 3 + 3 > reverseStr.length()) {
                    strTemp += reverseStr.substring(i * 3, reverseStr.length());
                    break;
                }
//                strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
                strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ".";
            }
            // 将 "5,000,000," 中最后一个","去除
//            if (strTemp.endsWith(",")) {
            if (strTemp.endsWith(".")) {
                strTemp = strTemp.substring(0, strTemp.length() - 1);
            }
            // 将数字重新反转,并将小数拼接到末尾
//            String resultStr = new StringBuilder(strTemp).reverse().toString() + tmpCommaStr;
            String resultStr = new StringBuilder(strTemp).reverse().toString();
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将加上逗号处理的数字（字符）的逗号去掉 （通常使用金额方面的编辑）
     * 5,000,000.00 --> 5000000.00
     * 20,000,000 --> 20000000
     *
     * @param str 加上逗号的数字（字符）
     * @return 无逗号的数字（字符）
     */
    public static String strRemoveComma(String str) {
        try {
            if (str == null) {
                str = "";
            }
//            String resultStr = str.replaceAll(",", ""); // 需要去除逗号的字符串（整数）
            String resultStr = str.replaceAll(".", "");
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
