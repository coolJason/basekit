package com.moregood.kit.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gyf.immersionbar.ImmersionBar;
import com.moregood.kit.utils.ReflectionUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Devin.Ding
 * Date: 2020/11/16 14:42
 * Descripe:
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment {

    protected boolean isInit;
    protected boolean isSelected;
    protected View mView;
    protected Unbinder mUnbinder;
    protected VM mViewModel;
    protected int mCurrentStatusColor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (getStatusColor() != 0) setFitSystemForTheme(true, getStatusColor());
        int layoutResId = getLayoutResID();
        mView = layoutResId <= 0 ? getView() : inflater.inflate(getLayoutResID(), container, false);
        if (mView == null) mView = new FrameLayout(container.getContext());
        mUnbinder = initBinder(mView);
        try {
            Class vmClass = ReflectionUtils.getDefinedTClass(this, 0);
            if (vmClass != null) {
                mViewModel = (VM) new ViewModelProvider(this).get(vmClass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getArguments() != null)
            getInstanceBundle(getArguments());
        initView(mView);
        isInit = true;
        return mView;
    }

    public void getInstanceBundle(Bundle bundle) {
    }

    public View getLayout() {
        return null;
    }

    public abstract int getLayoutResID();

    protected void startOtherActivity(Class className) {
        startActivity(new Intent(getActivity(), className));
    }

    public abstract void initView(View view);

    @ColorRes
    public int getStatusColor() {
        return 0;
    }

    public boolean isStatusDark() {
        return false;
    }

    public void setCurrentStatusColor(int mCurrentStatusColor) {
        this.mCurrentStatusColor = mCurrentStatusColor;
    }

    /**
     * 设置是否是沉浸式，并可设置状态栏颜色
     *
     * @param fitSystemForTheme
     * @param colorId           颜色资源路径
     */
//    public void setFitSystemForTheme(boolean fitSystemForTheme, @ColorRes int colorId) {
//        mCurrentStatusColor = colorId;
//        setFitSystem(fitSystemForTheme);
//        //初始设置
////        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.white));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(colorId));
//            if (isStatusDark())
//                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// 图标显示深色
//            else
//                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        }
//    }

    /**
     * 设置是否是沉浸式
     *
     * @param fitSystemForTheme
     */
    public void setFitSystem(boolean fitSystemForTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (fitSystemForTheme) {
            ViewGroup contentFrameLayout = getActivity().findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }
    }

    Unbinder initBinder(View view) {
        return ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        mView = null;
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        super.onDestroyView();
        Runtime.getRuntime().gc();
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @CallSuper
    public void onFragmentSelected() {
        ImmersionBar.with(this).init();
//        if (getView() != null && isInit)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                if (getActivity().getWindow().getStatusBarColor() != mCurrentStatusColor && mCurrentStatusColor != 0)
//                    setFitSystemForTheme(true, mCurrentStatusColor);
    }

    @CallSuper
    public void onDismissSelected() {
    }

    public VM getViewModel() {
        return mViewModel;
    }
}