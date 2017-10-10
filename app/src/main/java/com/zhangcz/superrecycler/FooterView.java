package com.zhangcz.superrecycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by zhou on 2017/10/10 13:35 .
 */

public class FooterView extends LinearLayout{
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
    }


}
