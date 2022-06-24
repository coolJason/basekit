package com.moregood.kit.base;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.moregood.kit.R;
import com.moregood.kit.language.AppLanguageUtils;
import com.moregood.kit.permission.PermissionChecker;
import com.moregood.kit.utils.ActivityCollector;
import com.moregood.kit.utils.AppUtils;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Devin.Ding
 * Date: 2020/11/16 15:48
 * Descripe:
 */
public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    protected VM mViewModel;
    protected Unbinder mUnbinder;
    List<ActivityLifecycleCallbacks> lifecycleCallbacks = new ArrayList<>();
    private PermissionChecker permissionChecker;
    private boolean dark = false;
    private boolean isSystemForTheme = false;//置顶到任务栏

    public boolean isSystemForTheme() {
        return isSystemForTheme;
    }

    public void setSystemForTheme(boolean systemForTheme) {
        isSystemForTheme = systemForTheme;
    }

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public void setmViewModel(VM mViewModel) {
        this.mViewModel = mViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityCollector.addActivity(this, getClass());
        initLanguage();
        if (isSystemForTheme) {
            if (AppUtils.isMall(this)) {
                //https://github.com/gyf-dev/ImmersionBar   属性参考链接
                ImmersionBar.with(this).autoDarkModeEnable(false)
                        .statusBarDarkFont(true)
                        .autoStatusBarDarkModeEnable(true, 0.2f)
                        .init();
            } else {
                //设置全屏
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                // 显示状态栏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //注意要清除 FLAG_TRANSLUCENT_STATUS flag
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR// 图标显示深色
                );
            }
        } else {
            updateFitSystemForTheme();
        }
        super.onCreate(savedInstanceState);
//        Crashlytics.log(Log.DEBUG, getClass().getName(), "Crash");
        if (!BaseApplication.getInstance().isFollowSystemLanguage()) {
            AppLanguageUtils.changeAppLanguage(this, BaseApplication.getInstance().getAppLanguage(this));
        }
        setContentView(getLayoutResID());

        if (getSupportActionBar() != null) {
            //左侧按钮：可见
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //左侧按钮：可点击
            getSupportActionBar().setHomeButtonEnabled(true);

        }
        if (!lifecycleCallbacks.isEmpty()) {
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityCreated(savedInstanceState);
            }
        }
        try {
            Class vmClass = ReflectionUtils.getDefinedTClass(this, 0);
            if (vmClass != null) {
                mViewModel = (VM) new ViewModelProvider(this).get(vmClass);
                Logger.e("Reflection VM=" + mViewModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bindViewInParent()) {
            mUnbinder = ButterKnife.bind(this);
        }
        initView();
        initData();
    }

    private void initLanguage() {
        String currentLanguage = AppLanguageUtils.getCurrentLanguage();
        AppLanguageUtils.changeAppLanguage(this, currentLanguage);
    }

    public void updateFitSystemForTheme() {
        setFitSystemForTheme(true, getStatusColor() != 0 ? getStatusColor() : R.color.white);

    }


    /**
     * 复写：左侧按钮点击动作
     * android.R.id.home
     * v7 actionbar back event
     * 注意：如果复写了onOptionsItemSelected方法，则onSupportNavigateUp无用
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public abstract int getLayoutResID();

    public abstract void initView();

    public abstract void initData();

    @ColorRes
    public int getStatusColor() {
        return 0;
    }

    public boolean isStatusDark() {
        return dark;
    }


    /**
     * 添加生命周期监听
     *
     * @param activityLifecycleCallbacks
     */
    public void addActivityLifecycleCallbacks(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (activityLifecycleCallbacks != null)
            lifecycleCallbacks.add(activityLifecycleCallbacks);
    }

    /**
     * 移除生命周期监听
     *
     * @param activityLifecycleCallbacks
     */
    public void removeActivityLifecycleCallbacks(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (!lifecycleCallbacks.isEmpty() && activityLifecycleCallbacks != null
                && lifecycleCallbacks.contains(activityLifecycleCallbacks))
            lifecycleCallbacks.remove(activityLifecycleCallbacks);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!lifecycleCallbacks.isEmpty())
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivitySaveInstanceState(outState);
            }
    }

    /**
     * 设置是否是沉浸式，并可设置状态栏颜色
     *
     * @param fitSystemForTheme
     * @param color             颜色值
     */
    public void setFitSystemForTheme(boolean fitSystemForTheme, String color) {
        setFitSystem(fitSystemForTheme);
        //初始设置
//        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
            if (isStatusDark())
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// 图标显示深色
            else
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            setDarkStatusIcon(isStatusDark());
        }
    }

    /**
     * 设置是否是沉浸式，并可设置状态栏颜色
     *
     * @param fitSystemForTheme
     * @param colorId           颜色资源路径
     */
    public void setFitSystemForTheme(boolean fitSystemForTheme, @ColorRes int colorId) {
        setFitSystem(fitSystemForTheme);
        //初始设置
//        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorId));
            if (isStatusDark())
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// 图标显示深色
            else
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            setDarkStatusIcon(isStatusDark());
        }
    }


    public void setDarkStatusIcon(boolean bDark) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View decorView = getWindow().getDecorView();
//            if (decorView != null) {
//                int vis = decorView.getSystemUiVisibility();
//                if (bDark) {
//                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                } else {
//                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                }
//                decorView.setSystemUiVisibility(vis);
//            }
//        }
        if (bDark)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// 图标显示深色
        else
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    /**
     * 设置是否是沉浸式
     *
     * @param fitSystemForTheme
     */
    public void setFitSystem(boolean fitSystemForTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (fitSystemForTheme) {
            ViewGroup contentFrameLayout = findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!lifecycleCallbacks.isEmpty())
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityStarted();
            }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!lifecycleCallbacks.isEmpty())
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityRestarted();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!lifecycleCallbacks.isEmpty())
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityResumed();
            }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!lifecycleCallbacks.isEmpty())
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityPaused();
            }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!lifecycleCallbacks.isEmpty())
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityStopped();
            }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        if (!lifecycleCallbacks.isEmpty()) {
            for (ActivityLifecycleCallbacks callbacks : lifecycleCallbacks) {
                callbacks.onActivityDestroyed();
            }
        }
        lifecycleCallbacks.clear();
        super.onDestroy();
        Glide.get(getApplicationContext()).onTrimMemory(Application.TRIM_MEMORY_BACKGROUND);
        Runtime.getRuntime().gc();
    }

    protected boolean bindViewInParent() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void attachBaseContext(Context newBase) {
        if (BaseApplication.getInstance().isFollowSystemLanguage()) {
            super.attachBaseContext(newBase);
        } else {
            String appLang = PreferenceManager.getDefaultSharedPreferences(newBase)
                    .getString(newBase.getResources().getString(R.string.app_language_pref_key), Locale.CHINESE.getLanguage());
            if (appLang.equals("system") ) {
                if (BaseApplication.getInstance().getAppLanguage(newBase).equals("zh")) {
                    super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, "zh"));
                }else {
                    super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, "en"));
                }
            } else {
                super.attachBaseContext(AppLanguageUtils.attachBaseContext(newBase, newBase.getString(R.string.app_language_pref_key)));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionChecker != null) {
//            MgDialog dialog = new MgDialog(this, R.string.key_256, R.string.key_970, R.string.key_966, R.string.key_66, R.color.accelarate_detail_high_end);
//            dialog.setOkClickListener(v -> {
//                AppUtil.gotoAppDetail(this, getPackageName());
//            });
//            dialog.show();
//            permissionChecker.onRequestPermissionsResult(requestCode, dialog);
        }
    }

    public VM getViewModel() {
        return mViewModel;
    }

    /**
     * 设置Activity的statusBar隐藏
     *
     * @param activity
     */
    public static void statusBarHide(Activity activity) {
        // 代表 5.0 及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            ActionBar actionBar = activity.getActionBar();
            actionBar.hide();
            return;
        }

        // versionCode > 4.4  and versionCode < 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    /**
     * 是否跟随系统语言
     * <p>
     * 某些应用内部有多语言选择，单纯只改变应用内部的配置，不随系统的语言
     *
     * @return
     */
//    protected boolean isFollowSystemLanguage() {
//        try {
//            return BaseApplication.getInstance().getResources().getBoolean(R.bool.follow_system_language);
//        } catch (Throwable e) {
//            return true;
//        }
//    }

    /**
     * hide keyboard
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}