package com.moregood.kit.auth;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseUser;
import com.moregood.kit.R;
import com.moregood.kit.bean.AccountType;
import com.moregood.kit.ui.dialog.BaseFullBottomSheetFragment;

/**
 * @类名 LoginDialog
 * @描述
 * @作者 xifengye
 * @创建时间 2021/2/20 08:36
 * @邮箱 ye_xi_feng@163.com
 */
public class LoginDialog extends BaseFullBottomSheetFragment {

    private IAuth auth;
    private AuthResultCallback<FirebaseUser> authResultCallback;
    private AuthOperatorCallback authOperatorCallback;

    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_login;
    }

    @Override
    protected void initViews(View view) {
        authOperatorCallback = new AuthOperatorCallback<FirebaseUser>() {
            @Override
            public void onLoginSelect(int index) {
            }

            @Override
            public void onLoginSuccess(AccountType type,FirebaseUser user) {
                if (authResultCallback != null) {
                    authResultCallback.onLoginSuccess(type,user);
                }
                if (user != null) {
                    dismiss();
                }
            }

            @Override
            public void onLogoutSuccess(String tag) {

            }

            @Override
            public void onLogoutFail(int code, String error) {

            }

            @Override
            public void onLoginFail(int code, String error) {
                Toast.makeText(getActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                if (authResultCallback != null) {
                    authResultCallback.onLoginFail(code, error);
                }
            }

            @Override
            public void onCancel(String reason) {
                if (authResultCallback != null) {
                    authResultCallback.onCancel(reason);
                }
            }

            @Override
            public void showLoading() {
                showProgressBar();
            }

            @Override
            public void hideLoading() {
                hideProgressBar();
            }
        };

        View facebookLoginView = view.findViewById(R.id.facebookLoginView);
        facebookLoginView.setOnClickListener(v -> {
            initFacebookAth();
            auth.login();
            if (authResultCallback != null)
                authResultCallback.onLoginSelect(0);
        });
        View googleLoginView = view.findViewById(R.id.googleLoginView);
        googleLoginView.setOnClickListener(v -> {
            initGoogleAth();
            auth.login();
            if (authResultCallback != null)
                authResultCallback.onLoginSelect(1);
        });

    }


    @Override
    protected void initData() {
    }


    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        super.showNow(manager, tag);
    }


    private void showProgressBar() {

    }

    private void hideProgressBar() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        auth.onActivityResult(requestCode, resultCode, data);

    }

    public void setAuthResultCallback(AuthResultCallback authResultCallback) {
        this.authResultCallback = authResultCallback;
    }

    private void initFacebookAth() {
        auth = new FacebookAuth();
        auth.init(getActivity());
        auth.setCallback(authOperatorCallback);
    }

    private void initGoogleAth() {
        auth = new GoogleAuth();
        auth.init(getActivity());
        auth.setCallback(authOperatorCallback);
    }


}
