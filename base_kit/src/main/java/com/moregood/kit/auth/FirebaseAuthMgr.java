package com.moregood.kit.auth;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.Account;
import com.moregood.kit.bean.AccountType;
import com.moregood.kit.utils.Logger;

/**
 * @author Rico.lo
 * @date 2021/3/28
 * Description:
 */
public class FirebaseAuthMgr {

    private static class SingletonHolder {
        private static final FirebaseAuthMgr DEFAULT_MANAGER = new FirebaseAuthMgr();
    }

    public static FirebaseAuthMgr get() {
        return SingletonHolder.DEFAULT_MANAGER;
    }

    final int AUTH_TYPE_NOT_LOGGED_IN = -1;
    final int AUTH_TYPE_LOGGING_IN = 0;
    final int AUTH_TYPE_LOGGINED = 1;

    int currentLoginState = AUTH_TYPE_NOT_LOGGED_IN;

    public void authFirebase(Activity context) {
        currentLoginState = AUTH_TYPE_LOGGING_IN;
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            authAnonymous(context);
        } else {
            onFirebaseLogined();
        }

    }

    private void onFirebaseLogined() {
        currentLoginState = AUTH_TYPE_LOGGINED;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        BaseApplication.getInstance().getAccountConfig().getAccount().setType(user.getProviderId());
        BaseApplication.getInstance().getAccountConfig().getAccount().setAccountId(user.getUid());
        requestFCMToken();
        BaseApplication.getInstance().getAccountConfig().onAccountLogin();
    }

    private void authAnonymous(Activity activity) {
        final AnonymousAuth anonymousAuth = new AnonymousAuth();
        Logger.debugWithTag(Account.TAG, "guestAuth>" + anonymousAuth);
        anonymousAuth.init(activity);
        anonymousAuth.setCallback(new AuthOperatorCallback<FirebaseUser>() {
            @Override
            public void showLoading() {

            }

            @Override
            public void hideLoading() {

            }

            @Override
            public void onLoginSelect(int index) {

            }

            @Override
            public void onLoginSuccess(AccountType type, FirebaseUser user) {
                anonymousAuth.release();
                onFirebaseLogined();
            }

            @Override
            public void onLogoutSuccess(String tag) {

            }

            @Override
            public void onLogoutFail(int code, String error) {

            }

            @Override
            public void onLoginFail(int code, String error) {
                anonymousAuth.release();
                Logger.debugWithTag(Account.TAG, "Guest onLoginFail:%s", error);
                currentLoginState = AUTH_TYPE_NOT_LOGGED_IN;

            }

            @Override
            public void onCancel(String reason) {
                currentLoginState = AUTH_TYPE_NOT_LOGGED_IN;

            }
        });
        anonymousAuth.login();
        Logger.debugWithTag(Account.TAG, "Guest onLogin");
    }

    /**
     * 请求推送
     */
    public static void requestFCMToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("deepclean");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    // Log and toast
                    Logger.d("getFirebaseToken===" + token);
                    BaseApplication.getInstance().onPlatformPushToken(token);
                });
    }

}
