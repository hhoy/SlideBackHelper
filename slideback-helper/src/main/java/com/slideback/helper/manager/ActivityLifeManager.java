package com.slideback.helper.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;
/*
    这个类用于对activity的生命周期进行监听，保存activty栈的状态
 */
public class ActivityLifeManager implements BaseActivityLifeManager {
    private Stack<Activity> activityStack;//保存activity栈中的活动
    private static ActivityLifeManager activityLifeManager;
    private static boolean isInit;
    static {
        isInit=false;
    }
    private ActivityLifeManager() {
        activityStack=new Stack<>();
    }
    public static ActivityLifeManager getActivityLifeManager(){
        return activityLifeManager;
    }
    public static ActivityLifeManager registerListener(Application application){
        if(application==null){
            return null;
        }
        if(activityLifeManager==null){
            activityLifeManager=new ActivityLifeManager();
            application.registerActivityLifecycleCallbacks(activityLifeManager);
            isInit=true;
        }
        return activityLifeManager;
    }
    public static ActivityLifeManager registerListener(Activity activity){
        if(activity==null){
            return null;
        }
        return registerListener(activity.getApplication());
    }
    @Override
    public Activity getCurrentActivity() {
        if(activityStack.size()<=0){
            return null;
        }
        return activityStack.peek();
    }

    @Override
    public Activity getPreActivity() {
        if(hasPreActivity()){
            return activityStack.elementAt(activityStack.size()-2);
        }
        return null;
    }

    @Override
    public boolean hasPreActivity() {
        if(activityStack.size()>1)
            return true;
        else {
            return false;
        }
    }
    /*
        当前管理器是否注册了监听
     */
    public static boolean isInit() {
        return isInit;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activityStack.add(activity);
        Log.e("activity:",activity.getLocalClassName()+","+activity.getTaskId());
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityStack.remove(activity);
    }
}
