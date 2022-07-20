package com.moregood.kit.utils;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.moregood.kit.dialog.LoadingDialog;

import static com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class GooglePlayApkUpdate {

    public static int UPDATE_APK_REQUEST_CODE = 20081;
    public static AppUpdateManager appUpdateManager;
    public static Activity activity;
    private static InstallStateUpdatedListener listener;
    private static int UPDATE_TYPE;

    public static void update(final Activity context) {
        update(context, IMMEDIATE);
    }
    public static void update(final Activity context, int updateType) {
        try {
            activity = context;
            if (updateType == 1) {
                UPDATE_TYPE = IMMEDIATE;
            } else {
                UPDATE_TYPE = FLEXIBLE;
            }
            appUpdateManager = AppUpdateManagerFactory.create(context);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    LoadingDialog.dismiss(context);
                    //是否允许更新
                    Log.e("GooglePlayApkUpdate", appUpdateInfo.toString());
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        //Google play商店通知用户更新以来已过去了多长时间
//                && appUpdateInfo.clientVersionStalenessDays() != null
//                && appUpdateInfo.clientVersionStalenessDays() >=7
                        //检查更新优先级
//                &&appUpdateInfo.updatePriority() >=5
                        //检查更新模式
//                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                    ) {
                        try {
                            Log.e("GooglePlayApkUpdate", appUpdateInfo.toString());
                            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, UPDATE_TYPE, (Activity) context, UPDATE_APK_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            //创建监听器跟踪请求状态更新
            listener = new InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(@NonNull InstallState state) {
                    //（可选）提供下载进度栏
                    if (state.installStatus() == InstallStatus.DOWNLOADING) {
                        //进度
                        long byteDownloaded = state.bytesDownloaded();
                        long totalBytesToDownload = state.totalBytesToDownload();
                    }
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        //下载完成，提示用户触发安装
                        appUpdateManager.completeUpdate();
//                    popupSnackbarForCompleteUpdate(context, appUpdateManager);
                    }
                    if (state.installStatus() == InstallStatus.CANCELED) {
                        Log.e("Octo","CANCELED");
                        appUpdateManager.unregisterListener(listener);
                    }
                    if (state.installStatus() == InstallStatus.FAILED) {
                        Log.e("Octo","FAILED");
                    }
                }
            };

            //在开始更新之前，注册一个监听器以进行更新
            appUpdateManager.registerListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void popupSnackbarForCompleteUpdate(Activity activity, AppUpdateManager appUpdateManager) {
        /*Snackbar snackbar =
                Snackbar.make(
                        activity.findViewById(R.id.activity_offline_push_settings),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(activity.getResources().getColor(R.color.blue));
        snackbar.show();*/
    }

    public static void onResume() {
        try {
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                        @Override
                        public void onSuccess(AppUpdateInfo appUpdateInfo) {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            UPDATE_TYPE,
                                            activity,
                                            UPDATE_APK_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onDestroy() {
        activity = null;
        appUpdateManager.unregisterListener(listener);
    }
}
