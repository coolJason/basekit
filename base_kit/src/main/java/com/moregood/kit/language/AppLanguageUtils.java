package com.moregood.kit.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author YanLu
 * @since 17/5/12
 */

public class AppLanguageUtils {

    private static HashMap<String, LangInfo> mAllLanguages = new HashMap<String, LangInfo>();

    public static void init(String[] languageTitle,String[] languageValue,Locale[] locales){
        for(int i=0;i<languageTitle.length;i++){
            String title = languageTitle[i];
            String value = languageValue[i];
            mAllLanguages.put(languageValue[i],new LangInfo(title,value,locales[i]));
        }
    }


    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    private static boolean isSupportLanguage(String language) {
        return mAllLanguages.containsKey(language);
    }

    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }

        return Locale.ENGLISH.getLanguage();
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #mAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #mAllLanguages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mAllLanguages.get(language).getLocale();
        } else {
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLocale().getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
            return locale;
        }
    }


    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = AppLanguageUtils.getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    public static Collection<LangInfo> getSupportLanguageList() {
        return mAllLanguages.values();
    }

    public static LangInfo getCurrentLanInfo(String lang){
        if(mAllLanguages.containsKey(lang)){
            return mAllLanguages.get(lang);
        }
        return new LangInfo("English","en",Locale.ENGLISH);
    }
}
