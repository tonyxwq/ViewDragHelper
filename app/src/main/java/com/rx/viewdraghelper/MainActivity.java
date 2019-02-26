package com.rx.viewdraghelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private MyRecyclerView mRecyclerView;
    private RecyclerView mTopRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.left_menu);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List list = new ArrayList();
        for (int i = 0; i < 30; i++)
        {
            list.add("我是商品" + i);
        }
        QuickAdapter<String> baseAdapter = new QuickAdapter<String>(list)
        {
            @Override
            public int getLayoutId(int viewType)
            {
                return R.layout.item;
            }

            @Override
            public void convert(VH holder, String data, int position)
            {
                holder.setText(R.id.tv_name,data);
                TextView textView=holder.getView(R.id.tv_name);
            }

            @Override
            public int clickItem(int position)
            {
                Toast.makeText(MainActivity.this,""+position,Toast.LENGTH_SHORT).show();
                return 0;
            }
        };
        mRecyclerView.setAdapter(baseAdapter);
    }
}
