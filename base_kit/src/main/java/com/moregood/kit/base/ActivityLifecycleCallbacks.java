package com.moregood.kit.base;

import android.os.Bundle;

/**
 * Created by Rico on 2019/10/14.
 */

public interface ActivityLifecycleCallbacks {
    default void onActivityCreated(Bundle savedInstanceState) {
    }

    default void onActivityStarted() {
    }

    default void onActivityResumed() {
    }

    default void onActivityPaused() {
    }

    default void onActivityStopped() {
    }

    default void onActivitySaveInstanceState(Bundle outState) {
    }

    default void onActivityDestroyed() {
    }

    default void onActivityRestarted() {
    }
}
