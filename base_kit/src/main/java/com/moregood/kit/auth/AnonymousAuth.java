package com.moregood.kit.auth;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moregood.kit.bean.Account;
import com.moregood.kit.bean.AccountType;


/**
 * @类名 GuestAuth
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/9 09:40
 * @邮箱 ye_xi_feng@163.com
 */
public class AnonymousAuth extends MGFirebaseAuth {

    @Override
    public void login(Object... option) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        mLoginCallback.onLoginSuccess(Account.parseType(user.getProviderId()),user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
//                        Toast.makeText(mActivity, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show();
                        mLoginCallback.onLoginFail(0, task.getException().getMessage());
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public AccountType getType() {
        return AccountType.Anonymous;
    }

}
