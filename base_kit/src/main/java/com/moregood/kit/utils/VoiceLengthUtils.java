package com.moregood.kit.utils;

import android.content.Context;

/**
 * use to control voice view's length
 */
public class VoiceLengthUtils {
    /**
     * 获取语音的长度
     * @param context
     * @param voiceLength
     * @return
     */
    public static int getVoiceLength(Context context, int voiceLength) {
        // 先获取屏幕的宽度，取其一半作为最大长度
        // 语音超过20s后长度一致，小于20s的按照时长控制长度
        float maxLength = CommonUtils.getScreenInfo(context)[0] / 4 - CommonUtils.dip2px(context, 10);
        float paddingLeft;
        if(voiceLength <= 20) {
            paddingLeft = voiceLength / 20f * maxLength + CommonUtils.dip2px(context, 10);
        }else {
            paddingLeft = maxLength + CommonUtils.dip2px(context, 10);
        }
        return (int) paddingLeft;
    }
}
