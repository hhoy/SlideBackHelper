package com.slideback.helper.view;

import android.view.MotionEvent;

public interface SlideBackScrollListener {
    void onScrollStart(MotionEvent event);
    void onScrolling(MotionEvent event);
    void onScrollRelease(boolean isBack);
    void onScrollToOrigin(boolean isBack);
}
