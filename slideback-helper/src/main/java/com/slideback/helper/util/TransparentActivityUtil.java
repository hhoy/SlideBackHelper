package com.slideback.helper.util;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransparentActivityUtil {

    /*
        Make an activity transparent
        When the method ends, it returns whether or not the transformation was successful
     */
    public static void makeTransparent(Activity activity) {

        //获取ActivityOption，android 5.0以后才有ActivityOption
        boolean hasOption = true;
        Object option = null;
        try {
            Method getOptionMethod = Activity.class.getDeclaredMethod("getActivityOptions");
            getOptionMethod.setAccessible(true);
            option = getOptionMethod.invoke(activity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            hasOption = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            hasOption = false;
            e.printStackTrace();
        }
        //获取监听器的Class
        Class allClass[] = Activity.class.getDeclaredClasses();
        Class listenerClass = null;
        for (Class c : allClass) {
            if (c.getSimpleName().equals("TranslucentConversionListener")) {
                listenerClass = c;
            }
        }
        Method convertToTranslucentMethod = null;

        try {
            if (hasOption) {
                convertToTranslucentMethod = Activity.class.getDeclaredMethod("convertToTranslucent", listenerClass, Class.forName("android.app.ActivityOptions"));
            } else {
                convertToTranslucentMethod = Activity.class.getDeclaredMethod("convertToTranslucent");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (convertToTranslucentMethod != null) {
            convertToTranslucentMethod.setAccessible(true);
            try {
                if (hasOption)
                    convertToTranslucentMethod.invoke(activity, null, option);
                else {
                    convertToTranslucentMethod.invoke(activity, new Object[]{null});
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void makeOpacity(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }
    public static boolean isSupportTransparent(){
        Method[] methods=Activity.class.getDeclaredMethods();
        for(Method m:methods){
            if(m.getName().equals("convertToTranslucent")){
                return true;
            }
        }
        return false;
    }
}
