package com.example.board03.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.board03.app.BoardContact.BoardEntry;

/**
 * Created by gkimms on 2014-04-26.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "board.db";
    private static final int DB_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String STR_CSV = " ,";

    private static final String SQL_CREATE_STR =
            "CREATE TABLE "+BoardEntry.TABLE_NAME+" (" +
                    BoardEntry.COL_NAME_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BoardEntry.COL_NAME_TITIL   + TEXT_TYPE + STR_CSV +
                    BoardEntry.COL_NAME_USERID  + TEXT_TYPE + STR_CSV +
                    BoardEntry.COL_NAME_CONTENT + TEXT_TYPE + STR_CSV +
                    BoardEntry.COL_NAME_SINGDATE + INT_TYPE +
            ")";

    private static final String SQL_DELETE_STR = "DROP TABLE IF EXISTS " + BoardEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_STR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(SQL_DELETE_STR);
        onCreate(sqLiteDatabase);
    }
}
