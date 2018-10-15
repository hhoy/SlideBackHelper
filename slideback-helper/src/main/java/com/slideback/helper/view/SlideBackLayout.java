package com.slideback.helper.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.slideback.helper.manager.ActivityLifeManager;

public class SlideBackLayout extends FrameLayout {
    private Activity currentActivity;
    private Scroller scroller;
    private float lastX;//记录按下后第一次的x位置
    private int lastScrollX;//记录按下后第一次的scrollX
    private boolean isScrollToOrigin;//是否开始滚动到原点
    private float preActivityOffsetScale;//上一个activity左边相对当前屏幕的偏移量0.0~1.0
    private boolean isBack;//activity返回条件是否成立
    private SlideBackScrollListener scrollListener;//滚动监听

    private boolean drawEdgeShadow;//是否开启绘制activity阴影
    private Paint paint;//这个画笔用来绘制activity旁边的阴影
    private int shadowWidth;//阴影宽度
    private int shadowDepth;//阴影深度0~255

    private boolean allowSlide;
    public SlideBackLayout(@NonNull Context context) {
        this(context,null);
    }

    public SlideBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*
        当被实例化时的一些初始化操作
     */
    private void init(){
        allowSlide=true;
        scroller=new Scroller(getContext(),new DecelerateInterpolator());
        isScrollToOrigin=false;
        drawEdgeShadow=true;//默认绘制阴影
        shadowWidth=50;
        shadowDepth=70;
        /*
            初始化绘制阴影的画笔
         */
        paint=new Paint();
        paint.setColor(Color.BLACK);
        LinearGradient linearGradient=new LinearGradient(-shadowWidth,0,0,0,Color.TRANSPARENT, Color.argb(shadowDepth,0,0,0),LinearGradient.TileMode.CLAMP);
        paint.setShader(linearGradient);
        preActivityOffsetScale =0.5f;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(needIntercept(ev)){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public float getPreActivityOffsetScale() {
        return preActivityOffsetScale;
    }

    public void setPreActivityOffsetScale(float preActivityOffsetScale) {
        this.preActivityOffsetScale = preActivityOffsetScale;
    }

    public boolean isDrawEdgeShadow() {
        return drawEdgeShadow;
    }

    public void setDrawEdgeShadow(boolean drawEdgeShadow) {
        this.drawEdgeShadow = drawEdgeShadow;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(drawEdgeShadow)
            canvas.drawRect(-shadowWidth,0,0,getHeight(),paint);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isNeedIntercept=needIntercept(event);
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            lastX=event.getRawX();
            lastScrollX=getScrollX();
            if(isNeedIntercept){
                scrollListener.onScrollStart(event);
            }
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            int x=(int) (lastScrollX+lastX-event.getRawX());
            scrollTo(x,getScrollY());
            scrollListener.onScrolling(event);
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            isScrollToOrigin=true;
            if(event.getRawX()<getWidth()/3){
                isBack=false;
                scroller.startScroll(getScrollX(),getScrollY(),-getScrollX(),-getScrollY(),600);
                invalidate();
            }else {
                isBack=true;
                scroller.startScroll(getScrollX(),getScrollY(),-getWidth()-getScrollX(),-getScrollY(),600);
                invalidate();
            }
            scrollListener.onScrollRelease(isBack);
        }
        return isNeedIntercept;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(ActivityLifeManager.isInit()&&ActivityLifeManager.getActivityLifeManager().hasPreActivity()){
            View decorView=ActivityLifeManager.getActivityLifeManager().getPreActivity().getWindow().getDecorView();
            decorView.setTranslationX(computerPreActivityTranalationX(decorView.getWidth(),getWidth(),l));
            if(currentActivity.isFinishing()){
                decorView.setTranslationX(0);
            }
        }
        LinearGradient linearGradient=new LinearGradient(-shadowWidth,0,0,0,Color.TRANSPARENT, Color.argb((int)(shadowDepth*(1+l*1.0f/getWidth())),0,0,0),LinearGradient.TileMode.CLAMP);
        paint.setShader(linearGradient);
    }
    private float computerPreActivityTranalationX(int preViewWidth,int currentWidth, int scrollX){
        float origin=-(1- preActivityOffsetScale)*preViewWidth;
        float scroll=origin*scrollX/currentWidth;
        return origin+scroll;
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(!isScrollToOrigin){
            return;
        }
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }else {
            if(ActivityLifeManager.isInit()&&ActivityLifeManager.getActivityLifeManager().hasPreActivity())
                ActivityLifeManager.getActivityLifeManager().getPreActivity().getWindow().getDecorView().setTranslationX(0);
            if(scrollListener!=null){
                scrollListener.onScrollToOrigin(isBack);
            }
            isScrollToOrigin=false;
        }

    }

    /*
        此方法用于判断是否需要进行activity滑动，默认是屏幕左边50px的位置
     */
    public boolean needIntercept(MotionEvent event){
        if(!allowSlide)
            return false;
        if(isScrollToOrigin)
            return false;
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(event.getRawX()<50){
                return true;
            }
        }
        return false;
    }

    public SlideBackScrollListener getScrollListener() {
        return scrollListener;
    }

    public void setScrollListener(SlideBackScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}
