package com.zhangcz.superrecycler;

/**
 * Created by zhou on 2017/10/10 17:04 .
 */

public interface IFooterView {
    void onInit();//初始状态
    void onRelaseLoadMore();//释放加载更多
    void onLoading();//加载中
    void onDataNone();//数据为空
    void onNoMore();//没有更多数据
}
