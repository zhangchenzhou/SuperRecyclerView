package com.zhangcz.superrecycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhou on 2017/10/7 17:51 .
 *
 *
 *
 * 此类内部方法执行顺序
 *
 * 1.onStartNestedScroll   ---->    onNestedScrollAccepted     ---->    onNestedPreScroll     -------------->    onNestedScroll     -------->    onStopNestedScroll
 *
 *
 */

public class NestedParentView extends LinearLayout implements NestedScrollingParent {

    private View mTop;
    private int mTopHeight;

    String TAG = getClass().getName();

    public NestedParentView(Context context) {
        super(context);
    }

    public NestedParentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始嵌套滚动
     * @param child
     * @param target 发起这个滚动的目标view
     * @param nestedScrollAxes 滚动轴向
     * @return true 如果父类接收并处理这次嵌套滚动,如果不处理则返回false ,则后续嵌套滚动事件不向此类内部分发
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        showLog("onStartNestedScroll");
        if(nestedScrollAxes == SCROLL_AXIS_VERTICAL){
            showLog("onStartNestedScroll  --->true");
            return true;
        }else{
            showLog("onStartNestedScroll  --->false");
            return false;
        }
    }

    /**
     * 这个方法会在 onStartNestedScroll 之后调用
     * 父类实现嵌套滚动前可以在此方法中进行初始化操作,实现这个方法如果还有父类的话应该调用父类的此方法,让他的父类也进行初始化
     * @param child
     * @param target 发起这个滚动的目标view
     * @param nestedScrollAxes 滚动的轴向
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        showLog("onNestedScrollAccepted");
    }

    /**
     * 表示一次嵌套滚动的结束
     * @param target 发起这次滚动的view
     */
    @Override
    public void onStopNestedScroll(View target) {
        showLog("onStopNestedScroll");
    }

    /**
     *表示一个嵌套滚动进行
     * @param target 发起这次滚动的view
     * @param dxConsumed 已经被target消费的横向像素距离
     * @param dyConsumed 已经被target消费的纵向像素距离
     * @param dxUnconsumed 未被target消费的横向像素距离
     * @param dyUnconsumed 未被target消费的纵向像素距离
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        showLog("onNestedScroll");
    }

    /**
     *表示目标view滚动之前.
     * @param target 发起这次滚动的view
     * @param dx x方向滚动的像素距离
     * @param dy y方向滚动的像素距离
     * @param consumed 被父类横向和纵向消费的距离
     */

    int countDy=0;
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean isCanHiddenTop = dy>0 && getScrollY()<mTopHeight;
        boolean isCanShowTop = dy<0 && getScrollY()>0 && ViewCompat.canScrollVertically(target,-1);
        if(isCanHiddenTop||isCanShowTop){
            int consumedDy;
            if(dy>0){//向下滚动
                if(getScrollY()+dy>mTopHeight){
                    consumedDy = mTopHeight-getScrollY();
                }else{
                    consumedDy = dy;
                }
            }else{//向上滚动
                if(getScrollY()+dy<0){
                    consumedDy = -getScrollY();
                }else{
                    consumedDy = dy;
                }
            }
            countDy += consumedDy;
            Log.i(TAG,"---->dy"+dy);
            Log.i(TAG,"---->getScrollY()"+getScrollY());
            Log.i(TAG,"-------->countDy"+countDy);
            scrollBy(0,consumedDy);
            consumed[1] = consumedDy;
        }
        showLog("onNestedPreScroll");
    }

    /**
     *
     * @param target 发起这次滚动的view
     * @param velocityX x轴上的速度,像素每秒
     * @param velocityY y轴上的速度,像素每秒
     * @param consumed 如果children view消费了则是true,否则是false
     * @return
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        showLog("onNestedFling");
        return false;
    }

    /**
     * @return 如果这个父视图决定提前消耗这次fling,则返回true,否则返回false
     * @param target 发起这次滚动的view
     * @param velocityX x轴上的速度,像素每秒
     * @param velocityY y轴上的速度,像素每秒
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        showLog("onNestedPreFling");
        return false;
    }

    /**
     * @return 返回当前注册的嵌套滚动轴
     * 1.SCROLL_AXIS_NONE  当前没有注册的嵌套滚动轴
     * 2.SCROLL_AXIS_VERTICAL  当前注册为沿纵向嵌套滚动
     * 3.SCROLL_AXIS_HORIZONTAL  当前注册为沿横向滚动.
     */
    @Override
    public int getNestedScrollAxes() {
        showLog("getNestedScrollAxes");
        return 0;
    }

    private void showLog(String logStr){
        Log.i(TAG,logStr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mTop!=null){
            mTopHeight = mTop.getMeasuredHeight();
        }else{
            showLog("mTop cant be null");
        }
    }
}
