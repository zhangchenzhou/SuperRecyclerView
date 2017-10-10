package com.zhangcz.superrecycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
 * 1.目前支持单布局的嵌套滚动
 * 2.正在添加带上拉刷新的的RecyclerView
 *
 */

public class NestedParentView extends LinearLayout implements NestedScrollingParent {

    private View mTopScrollView;//顶部可嵌套滚动View ,默认是第一个
    private int mTopHeight;//顶部可嵌套滚动View的height
    private View mScrollView;//中部滚动的View

    private View mFooterView;//底部下拉刷新View
    private int mFooterHeight;//底部上啦加载footerview高度

    String TAG = getClass().getName();

    public NestedParentView(Context context) {
        this(context,null);
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
        if(dyUnconsumed>0){//向上滚动有剩余 显示底部
            showLog("向上滚动有剩余"+dyUnconsumed);
            if(getScrollY()+dyUnconsumed>mTopHeight+mFooterHeight){
                scrollBy(0,(mTopHeight+mFooterHeight)-getScrollY());
            }else{
                scrollBy(0,dyUnconsumed);
            }
//            scrollBy(0,dyUnconsumed);
        }else{//向下滚动有剩余  显示头部
            int scrollDist = 0;
            if(getScrollY()+dyUnconsumed<0){
                scrollDist = -getScrollY();
            }else{
                scrollDist = dyUnconsumed;
            }
            scrollBy(0,scrollDist);
            showLog("向下滚动有剩余"+dyUnconsumed);
        }
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
            int consumedDy=0;
            if(dy>0){//向上滚动 直接做隐藏头部操作  隐藏完毕就不消耗这次滚动了
                if(getScrollY()+dy>mTopHeight){
                    consumedDy = mTopHeight-getScrollY();
                }else{
                    consumedDy = dy;
                }
                countDy += consumedDy;
                Log.i(TAG,"---->dy"+dy);
                Log.i(TAG,"---->getScrollY()"+getScrollY());
                Log.i(TAG,"-------->countDy"+countDy);
//            target.getLayoutParams().height = target.getLayoutParams().height+consumedDy;
//            target.invalidate();
            }else{//向下滚动 先隐藏底部.完事则,由子布局自己处理,继而再交由本布局处理,确保列表滚到第一行才移出头部,代码移动到 onNestedScroll方法中
                if(getScrollY()>mTopHeight){
                    if(getScrollY()+dy>mTopHeight){
                        consumedDy = dy;
                    }else{
                        consumedDy = mTopHeight-getScrollY();
                    }
                }
            }
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
        mTopScrollView = getChildAt(0);
        mScrollView = getChildAt(getChildCount()-2);
        mFooterView = getChildAt(getChildCount()-1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mTopScrollView !=null){
            mTopHeight = mTopScrollView.getMeasuredHeight();
        }else{
            showLog("mTopScrollView cant be null");
        }
        if(mFooterView != null){
            mFooterHeight = mFooterView.getMeasuredHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mScrollView.getLayoutParams();
        params.height = getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTopScrollView.getMeasuredHeight() + mFooterView.getMeasuredHeight() + mScrollView.getMeasuredHeight());
    }
}
