package com.zhangcz.superrecycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhou on 2017/10/10 13:35 .
 */

public class FooterView extends LinearLayout implements IFooterView{
    private TextView footerInfo;
    public FooterView(Context context) {
        this(context,null);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_footer,this,true);
        footerInfo=(TextView) findViewById(R.id.footer_info);
    }

    @Override
    public void onInit() {
        footerInfo.setText("上拉加载更多");
    }

    @Override
    public void onRelaseLoadMore() {
        footerInfo.setText("释放加载更多");
    }

    @Override
    public void onLoading() {
        footerInfo.setText("加载中");
    }

    @Override
    public void onDataNone() {
        footerInfo.setText("暂无数据");
    }

    @Override
    public void onNoMore() {
        footerInfo.setText("没有更多数据了");
    }
}
