package com.slideback.helper.manager;

import android.app.Activity;
import android.app.Application;

public interface BaseActivityLifeManager extends Application.ActivityLifecycleCallbacks {

    //获取当前activity
    Activity getCurrentActivity();

    //获取上一个activity
    Activity getPreActivity();

    //是否有上一个activity
    boolean hasPreActivity();

}
