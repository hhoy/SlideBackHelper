package com.slideback.helper;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.slideback.helper.util.TransparentActivityUtil;
import com.slideback.helper.util.Util;
import com.slideback.helper.view.SlideBackLayout;
import com.slideback.helper.view.SlideBackScrollListener;

public class SlideBackHelper {

    /*
        将SlideBackLayout插入到DecorView顶级容器中，
        将背景移动到SlideBackLayout的子view中，
        设置decorView和window的背景透明
        添加默认的监听器，当开始滑动时设置activity透明，释放时根据距离选择滚回原点还是滑动退出
     */
    public static void init(final Activity activity){
        if(!TransparentActivityUtil.isSupportTransparent()){
            return;
        }
        FrameLayout decorView= (FrameLayout) activity.getWindow().getDecorView();
        SlideBackLayout slideBackLayout=new SlideBackLayout(activity);
        slideBackLayout.setCurrentActivity(activity);
        View v=decorView.getChildAt(0);
        v.setBackgroundDrawable(decorView.getBackground());
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        decorView.setBackgroundDrawable(new ColorDrawable(0));
        decorView.removeView(v);
        decorView.addView(slideBackLayout);
        slideBackLayout.addView(v);
        slideBackLayout.setScrollListener(new SlideBackScrollListener() {

            @Override
            public void onScrollStart(MotionEvent event) {
                TransparentActivityUtil.makeTransparent(activity);
            }

            @Override
            public void onScrolling(MotionEvent event) {

            }

            @Override
            public void onScrollRelease(boolean isBack) {

            }

            @Override
            public void onScrollToOrigin(boolean isBack) {
                if(isBack){
                    activity.finish();
                    activity.overridePendingTransition(0,0);
                }else {
                    TransparentActivityUtil.makeOpacity(activity);
                }

            }
        });
    }
}
