package com.slideback.helper.view;

import android.view.MotionEvent;

public interface SlideProcessor {
    boolean onTouchIsIntercept(MotionEvent event);
    void onTouchScroll(MotionEvent event);
    void onScrollRecover();
}
