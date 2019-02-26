package com.rx.viewdraghelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Author:XWQ
 * Time   2019/2/25
 * Descrition: this is MyRecyclerView
 */
public class MyRecyclerView extends RecyclerView
{

    public MyRecyclerView(@NonNull Context context)
    {
        this(context, null);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
}
