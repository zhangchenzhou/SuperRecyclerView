package com.zhangcz.superrecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    NestedParentView mParentView;
    RecyclerView v;
    RecyclerAdaper adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParentView = (NestedParentView)findViewById(R.id.parent_view);
        v = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llM = new LinearLayoutManager(this);
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        v.setLayoutManager(llM);
        adapter = new RecyclerAdaper(this);
        List<String> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add("this is the i="+i);
        }
        adapter.addList(list);
        v.setAdapter(adapter);
        initEvent();
    }

    private void initEvent() {
        mParentView.setLoadMoreListener(new NestedParentView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        final List<String> list = new ArrayList<>();
                        for(int i=adapter.getItemCount();i<adapter.getItemCount()+10;i++){
                            list.add("this is the i="+i);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addList(list);
                                mParentView.setLoadMoreFinish(NestedParentView.STATE_INIT);
                            }
                        });
                    }
                },2000);
            }
        });
    }
}
