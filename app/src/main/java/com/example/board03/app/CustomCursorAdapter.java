package com.example.board03.app;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkimms on 2014-04-30.
 */
public class CustomCursorAdapter extends CursorAdapter{
    int layout;
    protected int[] mFrom;
    protected int[] mTo;
    String[] mOriginalFrom;
    private ViewBinder mViewBinder;
    private Cursor cursor;
    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
        super(context, c, false);
        this.layout = layout;
        mOriginalFrom = from;
        mTo = to;
        findColumns(c, from);
        itemChecked.clear();
        for (int i = 0; i < c.getCount(); i++) {
            itemChecked.add(i, false);
        }
        cursor = c;
    }

    //채크박스 채크해제 및 추가, 삭제로 인한 채크박스개수 변동문제 해결.
    public void refreshCheckBox() {
        super.onContentChanged();
        itemChecked.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            itemChecked.add(i, false);
        }
    }

    //채크된 채크박스의 포지션을 읽음.
    public List<Integer> getCheckedItemPositions(){
        List<Integer> checkedItemPositions = new ArrayList<Integer>();

        for(int i = 0; i < itemChecked.size(); i++){
            if (itemChecked.get(i)){
                (checkedItemPositions).add(i);
            }
        }
        return checkedItemPositions;
    }

    //채크된 채크박스의 COL_NAME_IDX 를 읽음.
    public List<Long> getCheckedItemIdx(){
        List<Long> checkedItemIdx = new ArrayList<Long>();

        for(int i = 0; i < itemChecked.size(); i++){
            if (itemChecked.get(i)){
                long l;
                l = getItemId(i);
                (checkedItemIdx).add(l);
            }
        }
        return checkedItemIdx;
    }

    public ArrayList<Boolean> getItemChecked() {
        return itemChecked;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor c) {
        final ViewBinder binder = mViewBinder;
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;
        final int position = cursor.getPosition();

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    itemChecked.set(position, true);
                    Log.d("bindView", "Onclick checkbox!: true:" + position);
                }
                else{
                    itemChecked.set(position, false);
                    Log.d("bindView", "Onclick checkbox!: false" + position);
                }
            }
        });

        //채크박스가 재사용되기 때문에 채크가 되었었는지 확인필요
        checkBox.setChecked(itemChecked.get(position));

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);

            if (v != null) {
                boolean bound = false;

                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {


                    String text = cursor.getString(from[i]);

                    if(mOriginalFrom[i] == "signdate" && text != null){
                        text = CustomDateUtil.unixtimeToDatetime(Integer.parseInt(text));
                    }

                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this CustomCursorAdapter");
                    }
                }
            }
        }
    }

    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public static interface ViewBinder {
        /**
         * Binds the Cursor column defined by the specified index to the specified view.
         *
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, SimpleCursorAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view the view to bind the data to
         * @param cursor the cursor to get the data from
         * @param columnIndex the column at which the data can be found in the cursor
         *
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(View view, Cursor cursor, int columnIndex);
    }

    /**
     * This class can be used by external clients of SimpleCursorAdapter
     * to define how the Cursor should be converted to a String.
     *
     * @see android.widget.CursorAdapter#convertToString(android.database.Cursor)
     */
    public static interface CursorToStringConverter {
        /**
         * Returns a CharSequence representing the specified Cursor.
         *
         * @param cursor the cursor for which a CharSequence representation
         *        is requested
         *
         * @return a non-null CharSequence representing the cursor
         */
        CharSequence convertToString(Cursor cursor);
    }
}
