package com.zhangcz.superrecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView v;
    RecyclerAdaper adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llM = new LinearLayoutManager(this);
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        v.setLayoutManager(llM);
        adapter = new RecyclerAdaper(this);
        List<String> list = new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add("this is the i="+i);
        }
        adapter.addList(list);
        v.setAdapter(adapter);
    }
}
