package com.moregood.kit.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.moregood.kit.R;
import com.moregood.kit.bean.AccountType;

/**
 * @类名 GoogleAuth
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/10 08:10
 * @邮箱 ye_xi_feng@163.com
 */
public class GoogleAuth extends MGFirebaseAuth {
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void init(Activity activity, Object... param) {
        super.init(activity,param);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    @Override
    public void login(Object... option) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void logout() {
        try {
            super.logout();
            mGoogleSignInClient.signOut().addOnCompleteListener(mActivity,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (mLoginCallback != null) {
                                mLoginCallback.onLogoutSuccess("google");
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            if (mLoginCallback != null) {
                mLoginCallback.onLogoutFail(0,e.getMessage());
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                if (mLoginCallback != null) {
                    mLoginCallback.onLoginFail(0,e.getMessage());
                }
                // [END_EXCLUDE]
            }
        }
    }

    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        if (mLoginCallback != null) {
            mLoginCallback.showLoading();
        }
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        handleAuthCredential(credential);
    }
    // [END auth_with_google]

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(mActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    @Override
    public void release() {
        mGoogleSignInClient = null;
    }

    @Override
    public AccountType getType() {
        return AccountType.Google;
    }


}
