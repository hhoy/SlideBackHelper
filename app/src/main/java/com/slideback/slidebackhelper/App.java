package com.slideback.slidebackhelper;

import android.app.Application;

import com.slideback.helper.manager.ActivityLifeManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifeManager.registerListener(this);
    }
}
