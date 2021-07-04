package com.moregood.kit.protocol;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.window.FloatWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 专门用于维护生命周期
 */

public class UserActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, ActivityState {
    private List<Activity> activityList = new ArrayList<>();
    private List<Activity> resumeActivity = new ArrayList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Logger.d("ActivityLifecycle > onActivityCreated " + activity.getLocalClassName());
        activityList.add(0, activity);
        BaseApplication.getInstance().onActivitysCreated(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.d("ActivityLifecycle > onActivityStarted " + activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.d("ActivityLifecycle > onActivityResumed " + activity.getLocalClassName());
        if (!resumeActivity.contains(activity)) {
            resumeActivity.add(activity);
            if (resumeActivity.size() == 1) {
                //do nothing
            }
            restartSingleInstanceActivity(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.d("ActivityLifecycle > onActivityPaused " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.d("ActivityLifecycle > onActivityStopped " + activity.getLocalClassName());
        resumeActivity.remove(activity);
        if (resumeActivity.isEmpty()) {
            Logger.d("ActivityLifecycle > 在后台了");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Logger.d("ActivityLifecycle > onActivitySaveInstanceState " + activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.d("ActivityLifecycle > onActivityDestroyed " + activity.getLocalClassName());
        activityList.remove(activity);
        BaseApplication.getInstance().onActivitysDestory(activity);
    }

    @Override
    public Activity current() {
        return activityList.size() > 0 ? activityList.get(0) : null;
    }

    @Override
    public Activity current(Class<? extends Activity> target) {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (target.isAssignableFrom(activity.getClass())) {
                    return activity;
                }
            }
        }
        return null;
    }

    @Override
    public List<Activity> getActivityList() {
        return activityList;
    }

    @Override
    public int count() {
        return activityList.size();
    }

    @Override
    public boolean isFront() {
        return resumeActivity.size() > 0;
    }

    /**
     * 跳转到目标activity
     *
     * @param cls
     */
    public void skipToTarget(Class<?> cls) {
        if (activityList != null && activityList.size() > 0) {
            current().startActivity(new Intent(current(), cls));
            for (Activity activity : activityList) {
                activity.finish();
            }
        }

    }

    /**
     * finish target activity
     *
     * @param cls
     */
    public void finishTarget(Class<?> cls) {
        if (activityList != null && !activityList.isEmpty()) {
            for (Activity activity : activityList) {
                if (activity.getClass() == cls) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 判断app是否在前台
     *
     * @return
     */
    public boolean isOnForeground() {
        return resumeActivity != null && !resumeActivity.isEmpty();
    }


    /**
     * 用于按下home键，点击图标，检查启动模式是singleInstance，且在activity列表中首位的Activity
     * 下面的方法，专用于解决启动模式是singleInstance, 为开启悬浮框的情况
     *
     * @param activity
     */
    private void restartSingleInstanceActivity(Activity activity) {
        boolean isClickByFloat = activity.getIntent().getBooleanExtra("isClickByFloat", false);
        if (isClickByFloat) {
            return;
        }
        //刚启动，或者从桌面返回app
        //至少需要activityList中至少两个activity
        if (resumeActivity.size() == 1 && activityList.size() > 1) {
            Activity topActivity = activityList.get(0);
            if (!topActivity.isFinishing() //没有正在finish
                    && topActivity != activity //当前activity和列表中首个activity不相同
                    && topActivity.getTaskId() != activity.getTaskId() //分属不同的任务栈 且 没有展示悬浮框
                    && !FloatWindow.getInstance(topActivity).isShowing()) {
                Logger.d("ActivityLifecycle > 启动了activity = " + topActivity.getClass().getName());
                activity.startActivity(new Intent(activity, topActivity.getClass()));
            }
        }
    }

    /**
     * 此方法用于设置启动模式为singleInstance的activity调用
     * 用于解决点击悬浮框后，然后finish当前的activity，app回到桌面的问题
     * 需要如下两个权限：
     * <uses-permission android:name="android.permission.GET_TASKS" />
     * <uses-permission android:name="android.permission.REORDER_TASKS"/>
     *
     * @param activity
     */
    public void makeMainTaskToFront(Activity activity) {
        //当前activity正在finish，且可见的activity列表中只有这个正在finish的activity,且没有销毁的activity个数大于等于2
        if (activity.isFinishing() && resumeActivity.size() == 1 && resumeActivity.get(0) == activity && activityList.size() > 1) {
            ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(20);
            for (int i = 0; i < runningTasks.size(); i++) {
                ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(i);
                ComponentName topActivity = taskInfo.topActivity;
                //判断是否是相同的包名
                if (topActivity != null && topActivity.getPackageName().equals(activity.getPackageName())) {
                    int taskId;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        taskId = taskInfo.taskId;
                    } else {
                        taskId = taskInfo.id;
                    }
                    //将任务栈置于前台
                    Logger.d("ActivityLifecycle > 执行moveTaskToFront，current activity:" + activity.getClass().getName());
                    manager.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME);
                }
            }
        }
    }

    public void finishAll() {
        if (activityList != null && !activityList.isEmpty()) {
            for (Activity activity : activityList) {
                activity.finish();
            }
        }
    }

    /**
     * 结束除当前Activity之外的所有栈底的activity
     *
     * @param activityClazz
     */
    public void popBeforeActivity(Class activityClazz) {
        if (activityList != null && !activityList.isEmpty()) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(activityClazz)) {
                    break;
                } else {
                    activity.finish();
                }
            }
        }
    }
}
