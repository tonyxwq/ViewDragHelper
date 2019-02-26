package com.rx.viewdraghelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Author:XWQ
 * Time   2019/2/25
 * Descrition: this is Adapter
 */
public class Adapter extends BaseAdapter
{

    private String[] data;
    private Context mContext;

    public Adapter(Context mContext, String[] data)
    {
        super();
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount()
    {
        return 10;
    }

    @Override
    public Object getItem(int i)
    {
        return 10;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TextView textView = new TextView(mContext);
        textView.setText("sdfsdfs");
        return textView;
    }
}
