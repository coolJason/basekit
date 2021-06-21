package com.moregood.kit.auth;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.moregood.kit.bean.Account;
import com.moregood.kit.utils.Logger;

/**
 * @类名 FirebaseAuth
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/10 08:17
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class MGFirebaseAuth implements IAuth<FirebaseUser> {
    public static final String TAG = "MGAuth";

    protected com.google.firebase.auth.FirebaseAuth mAuth;
    protected AuthOperatorCallback<FirebaseUser> mLoginCallback;
    protected Activity mActivity;


    @Override
    public void init(Activity activity, Object... param) {
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    protected void handleAuthCredential(AuthCredential credential) {
        if (mAuth != null) {
            final OnCompleteListener onCompleteListener = (OnCompleteListener<AuthResult>) task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, "signInWithCredential:success uid=" + user.getUid());
                    mLoginCallback.onLoginSuccess(Account.parseType(user.getProviderId()),user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure->", task.getException());
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        sign(credential, (OnCompleteListener<AuthResult>) signTask -> {
                            if (signTask.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d(TAG, "signInWithCredential:success uid=" + user.getUid());
                                mLoginCallback.onLoginSuccess(Account.parseType(user.getProviderId()),user);
                            } else {
                                mLoginCallback.onLoginFail(0, signTask.getException().getMessage());
                            }

                            // [START_EXCLUDE]
                            mLoginCallback.hideLoading();
                            // [END_EXCLUDE]
                        });
                    } else {
                        mLoginCallback.onLoginFail(0, task.getException().getMessage());
                    }
                }

                // [START_EXCLUDE]
                mLoginCallback.hideLoading();
                // [END_EXCLUDE]
            };
            if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isAnonymous()) {
                Logger.e("Guest 升级 %s", getClass().getSimpleName());
                mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(onCompleteListener);
            } else {
                sign(credential, onCompleteListener);
            }
        }

    }

    private void sign(AuthCredential credential, OnCompleteListener<AuthResult> onCompleteListener) {
        Logger.e("%s 登录Firebase", getClass().getSimpleName());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, onCompleteListener);
    }

    @Override
    public void setCallback(AuthOperatorCallback<FirebaseUser> callback) {
        this.mLoginCallback = callback;
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    public void release() {
        mActivity = null;
        mLoginCallback = null;
        mAuth = null;
        Logger.d("release Auth");
    }
}
