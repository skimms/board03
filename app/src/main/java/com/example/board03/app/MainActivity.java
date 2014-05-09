package com.example.board03.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.example.board03.app.BoardContact.BoardEntry;

public class MainActivity extends Activity {
    private static final int ACTIVITY_WRITE = 0;
    private static final int ACTIVITY_EDIT = 1;
    static Cursor c;
    static CustomCursorAdapter adapter;
    static ProgressBar progressBar;
    private ListView list;
    private Context context;
    static DBAdapter dbAdapter;

    //public static String authority="com.example.board03.app.provider";
    //public static Uri CONTENT_URI = Uri.parse("content://" + authority  + "/board");

//    static final String[] PROJECTION = new String[] {
//            BoardEntry.COL_NAME_IDX,
//            BoardEntry.COL_NAME_TITIL,
//            BoardEntry.COL_NAME_USERID,
//            BoardEntry.COL_NAME_CONTENT,
//            BoardEntry.COL_NAME_SINGDATE
//    };
//
//    static final String SELECTION = "((" +
//            BoardEntry.COL_NAME_IDX + " NOTNULL) AND (" +
//            BoardEntry.COL_NAME_IDX + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        /*
        try{
            db = new DBHelper(getApplicationContext()).getWritableDatabase();
        }
        catch (SQLException e){
        }
        finally {

        }
        */
        dbAdapter = new DBAdapter(context);

        //프로그레스바
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //글쓰기버튼
        Button btnWrite = (Button) findViewById(R.id.button);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });

        //갱신버튼
        Button btnRefresh = (Button) findViewById(R.id.button3);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList();
            }
        });


        //프로그레스바1
        //ProgressBar progressBar = new ProgressBar(this);
        //addContentView(progressBar,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );

        //프로그레스바2
        //ProgressBar progressBar = new ProgressBar(this);
        //RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
        //layout.addView(progressBar);

        //리스트
        c = dbAdapter.board_select();

        String[] fromColumns = {
                BoardEntry.COL_NAME_TITIL,
                BoardEntry.COL_NAME_USERID,
                BoardEntry.COL_NAME_SINGDATE
        };

        int[] toViews = {
                R.id.textView,
                R.id.textView2,
                R.id.textView3
        };

        adapter = new CustomCursorAdapter(this,
                R.layout.list_item,
                c,
                fromColumns,
                toViews
        );

        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("onItemClick","i:"+i+",l:"+l);
                Intent intent = new Intent(context, WriteActivity.class);
                intent.putExtra(BoardEntry.COL_NAME_IDX, l);
                startActivityForResult(intent, ACTIVITY_EDIT);
            }
        });

        list.setAdapter(adapter);

        //삭제버튼
        Button btnDelete = (Button) findViewById(R.id.button2);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick","adapter.getItemChecked():"+adapter.getItemChecked());

                if(!adapter.getItemChecked().contains(true)){
                    new AlertDialog.Builder(view.getContext())
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.board_noselect)
                            .setPositiveButton(R.string.board_confirm, null)
                            .show();
                    return;
                }

                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.board_delmsg)
                        .setPositiveButton(R.string.board_delete,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (Long aLong : adapter.getCheckedItemIdx()) {
                                    dbAdapter.deleteRow(aLong);
                                }
                                refreshList();
                            }
                        })
                        .setNegativeButton(R.string.board_cancel,null)
                        .show();

                //Log.d("adapter.getItemChecked","list.getCheckedItemPosition:"+adapter.getCheckedItemIdx());
                //Log.d("adapter.getItemChecked","list.getCheckedItemPosition:"+adapter.getCheckedItemPositions());
            }
        });
    }

    public static void refreshList(){
        progressBar.setVisibility(View.VISIBLE);
        c.requery();
        adapter.notifyDataSetChanged();
        adapter.refreshCheckBox();
        progressBar.setVisibility(View.GONE);
        Log.d("MainActivity", "refreshList");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult","requestCode:"+requestCode+
                ",resultCode:"+resultCode+",data:"+data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.Close();
    }
}
