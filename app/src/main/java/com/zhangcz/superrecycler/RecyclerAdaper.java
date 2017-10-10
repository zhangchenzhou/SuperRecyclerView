package com.zhangcz.superrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 2017/10/8 10:59 .
 */

public class RecyclerAdaper extends RecyclerView.Adapter<RecyclerAdaper.VH>{

    private LayoutInflater inflater;

    private List<String> mList = new ArrayList<>();

    public RecyclerAdaper(Context context){
        inflater = LayoutInflater.from(context);
    }

    public void addList(List<String> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.tv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.initData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private TextView tv;
        public VH(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.tv);
        }
        public void initData(String str){
            tv.setText(str);
        }
    }
}
