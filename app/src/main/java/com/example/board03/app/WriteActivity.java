package com.example.board03.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.board03.app.BoardContact.BoardEntry;

/**
 * Created by gkimms on 2014-04-26.
 */
public class WriteActivity extends Activity {
    private SQLiteDatabase db;
    private Long mRowId;
    private EditText edtxtTitle;
    private EditText edtxtUserId;
    private EditText edtxtContent;
    private ProgressBar progressBar;
    private Button btnComplete;
    private Button btnCancel;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        edtxtTitle    = (EditText) findViewById(R.id.editText);
        edtxtUserId   = (EditText) findViewById(R.id.editText2);
        edtxtContent  = (EditText) findViewById(R.id.editText3);
        progressBar   = (ProgressBar) findViewById(R.id.progressBar);
        btnComplete   = (Button) findViewById(R.id.button);
        btnCancel     = (Button) findViewById(R.id.button2);
        btnDelete     = (Button) findViewById(R.id.button3);

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(BoardEntry.COL_NAME_IDX) : null;
        }

        //글 수정시
        if(mRowId != null && mRowId > 0){
            btnDelete.setVisibility(View.VISIBLE);
        }
        populateFields();

        //final EditText edtxtContent = (EditText) findViewById(R.id.editText3);
        //edtxtContent.setText("내용을 입력하세요.");

        edtxtContent.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                previousString = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edtxtContent.getLineCount() >= 6 ){
                    edtxtContent.setText(previousString);
                    edtxtContent.setSelection(edtxtContent.length());
                }
            }
        });

        //완료버튼
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                String title = edtxtTitle.getText().toString();
                String userId = edtxtUserId.getText().toString();
                String content = edtxtContent.getText().toString();

                int signdate = (int) Math.floor(System.currentTimeMillis()/1000);

                ContentValues values = new ContentValues();
                values.put(BoardEntry.COL_NAME_TITIL, title);
                values.put(BoardEntry.COL_NAME_USERID, userId);
                values.put(BoardEntry.COL_NAME_CONTENT, content);
                values.put(BoardEntry.COL_NAME_SINGDATE, signdate);

                long newRowId = 0;
                try{
                    progressBar.setVisibility(View.VISIBLE);
                    db.beginTransaction();
                    newRowId = db.insert(BoardEntry.TABLE_NAME, null, values);
                    db.setTransactionSuccessful();
                }
                catch (SQLException e){
                    Log.e("dberr", "e:" + e);
                }
                finally {
                    progressBar.setVisibility(View.GONE);
                    db.endTransaction();
                }

                Toast.makeText(getApplicationContext(), "newRowId:" + newRowId, Toast.LENGTH_LONG).show();
                */
                saveState();
                setResult(RESULT_OK);
                MainActivity.refreshList();

                finish();
            }
        });

        //취소버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //삭제버튼
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.board_delmsg)
                        .setPositiveButton(R.string.board_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.dbAdapter.deleteRow(mRowId);
                                setResult(RESULT_OK);
                                MainActivity.refreshList();
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.board_cancel, null)
                        .show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        outState.putSerializable(BoardEntry.COL_NAME_IDX, mRowId);
    }

    private void saveState() {
        String title = edtxtTitle.getText().toString();
        String userId = edtxtUserId.getText().toString();
        String content = edtxtContent.getText().toString();

        int signdate = (int) Math.floor(System.currentTimeMillis()/1000);

        if (mRowId == null) {
            long id = MainActivity.dbAdapter.createRow(title, userId, content, signdate);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            MainActivity.dbAdapter.updateRow(mRowId, title, userId, content, signdate);
        }
    }

    private void populateFields() {
        if(mRowId != null){
            Cursor cursor = MainActivity.dbAdapter.fetchRow(mRowId);
            startManagingCursor(cursor);
            edtxtTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(BoardEntry.COL_NAME_TITIL)));
            edtxtUserId.setText(cursor.getString(cursor.getColumnIndexOrThrow(BoardEntry.COL_NAME_USERID)));
            edtxtContent.setText(cursor.getString(cursor.getColumnIndexOrThrow(BoardEntry.COL_NAME_CONTENT)));
        }
    }








}
