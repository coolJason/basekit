package com.moregood.kit.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moregood.kit.bean.AccountType;
import com.moregood.kit.utils.Logger;

import java.util.Arrays;

/**
 * @类名 FacebookAuth
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/9 08:24
 * @邮箱 ye_xi_feng@163.com
 */
public class FacebookAuth extends MGFirebaseAuth {
    public static final String TAG = "Facebook";

    private CallbackManager mCallbackManager;


    @Override
    public void init(Activity activity, Object... param) {
        super.init(activity,param);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Logger.errorWithTag(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // [START_EXCLUDE]
                        mLoginCallback.onCancel(null);
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // [START_EXCLUDE]
                        mLoginCallback.onLoginFail(0, error.getMessage());
                        // [END_EXCLUDE]
                    }
                });

    }


    // [START auth_with_facebook]
    public void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        if (mLoginCallback != null) {
            mLoginCallback.showLoading();
        }
        // [END_EXCLUDE]
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        handleAuthCredential(credential);

    }

    @Override
    public void login(Object... param) {
        if (mActivity != null) {
            LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
            LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("email", "public_profile"));
        }
    }

    @Override
    public void logout() {
        try {
            super.logout();
            LoginManager.getInstance().logOut();
            if (mLoginCallback != null) {
                mLoginCallback.onLogoutSuccess("Facebook");
            }
        }catch (Exception e){
            e.printStackTrace();
            if (mLoginCallback != null) {
                mLoginCallback.onLogoutFail(0,e.getMessage());
            }
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void release() {
        super.release();
        mCallbackManager = null;
    }

    @Override
    public AccountType getType() {
        return AccountType.Facebook;
    }


}
