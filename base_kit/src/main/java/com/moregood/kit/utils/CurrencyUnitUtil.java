package com.moregood.kit.utils;

import android.text.TextUtils;

/**
 * Author: Svtar
 * Date: 2022/7/28 14:51
 * E-mail: 707390415@qq.com
 * Description: 货币单位工具类
 */
public class CurrencyUnitUtil {

    private static final String KEY_CURRENCY_UNI = "KEY_CURRENCY_UNI";
    public static final String TYPE_UNI_IDR = "IDR";//印度尼西亚货币单位
    private static String unit = "";

    /**
     * 更新货币单位
     *
     * @param unit 需要更新的单位
     */
    public static void updateUnit(String unit) {
        if (TextUtils.isEmpty(unit)) {
            return;
        }
        MmkvUtil.put(KEY_CURRENCY_UNI, unit);
        CurrencyUnitUtil.unit = unit;
    }

    /**
     * 获取货币单位
     *
     * @return
     */
    public static String getUnit() {
        if (TextUtils.isEmpty(unit)) {
            unit = MmkvUtil.getString(KEY_CURRENCY_UNI, "AED");
        }
        return unit;
    }

    /**
     * 检查货币单位是否为空，如果是空返回默认的
     *
     * @return
     */
    public static String checkUnit(String unit) {
        if (TextUtils.isEmpty(unit)) {
            return getUnit();
        }
        return unit;
    }

}
