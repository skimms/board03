package com.example.board03.app;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

/**
 * Created by gkimms on 2014-04-26.
 */
public class ListAdapterCust extends BaseAdapter{
    private Context context = null;
    private Cursor cursor = null;
    private CheckBox checkBox = null;

    public ListAdapterCust(Context context, Cursor cursor){
        super();
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
