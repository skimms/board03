package com.example.board03.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.SQLException;

import com.example.board03.app.BoardContact.BoardEntry;
/**
 * Created by gkimms on 2014-05-08.
 */
public class DBAdapter {
    private Context context;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        super();
        this.context = context;
        this.open();
    }

    private void open() throws SQLException{
        try{
            db = (new DBHelper(context).getWritableDatabase());
        }
        catch (SQLiteException e){
            db = (new DBHelper(context).getReadableDatabase());
        }
    }

    public void Close() {
        db.close();
    }

    /*
       전체 목록보기
     */
    public Cursor board_select(){
        Cursor c = db.query(BoardEntry.TABLE_NAME,
                new String[]{
                        BoardEntry.COL_NAME_IDX,
                        BoardEntry.COL_NAME_TITIL,
                        BoardEntry.COL_NAME_USERID,
                        BoardEntry.COL_NAME_CONTENT,
                        BoardEntry.COL_NAME_SINGDATE
                },
                null,
                null,
                null,
                null,
                BoardEntry.COL_NAME_IDX+" desc"
            );
        return c;
    }

    /*
        선택 삭제하기
     */
    public boolean deleteRow(long rowId) {
        boolean isSuccess;
        db.beginTransaction();
        isSuccess = db.delete(BoardEntry.TABLE_NAME,
                BoardEntry.COL_NAME_IDX + "=" + rowId, null) > 0;
        db.setTransactionSuccessful();
        db.endTransaction();
        return isSuccess;
    }

    /*
        입력하기
     */
    public Long createRow(String title, String userId, String content, int signdate){
        Long rowId;
        ContentValues initialValues = new ContentValues();
        initialValues.put(BoardEntry.COL_NAME_TITIL, title);
        initialValues.put(BoardEntry.COL_NAME_USERID, userId);
        initialValues.put(BoardEntry.COL_NAME_CONTENT, content);
        initialValues.put(BoardEntry.COL_NAME_SINGDATE, signdate);
        db.beginTransaction();
        rowId = db.insert(BoardEntry.TABLE_NAME, null, initialValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return rowId;
    }

    /*
        수정하기
     */
    public boolean updateRow(long rowId, String title, String userId, String content, int signdate){
        boolean isSuccess;
        ContentValues args = new ContentValues();
        args.put(BoardEntry.COL_NAME_TITIL, title);
        args.put(BoardEntry.COL_NAME_USERID, userId);
        args.put(BoardEntry.COL_NAME_CONTENT, content);
        args.put(BoardEntry.COL_NAME_SINGDATE, signdate);

        db.beginTransaction();
        isSuccess = db.update(BoardEntry.TABLE_NAME, args, BoardEntry.COL_NAME_IDX + "=?",
                new String[]{rowId+""}) > 0;
        db.setTransactionSuccessful();
        db.endTransaction();
        return isSuccess;
    }

    /*
        전체 목록 출력
     */
    public Cursor fetchAllRow() {
        return db.query(BoardEntry.TABLE_NAME,
                new String[] {BoardEntry.COL_NAME_IDX,
                        BoardEntry.COL_NAME_TITIL,BoardEntry.COL_NAME_USERID,
                        BoardEntry.COL_NAME_CONTENT,
                        BoardEntry.COL_NAME_SINGDATE},
                null, null, null, null, null);
    }

    /*
        선택 목록 출력
     */
    public Cursor fetchRow(long rowId){
        Cursor cursor = db.query(
                true,
                BoardEntry.TABLE_NAME,
                new String[]{BoardEntry.COL_NAME_TITIL, BoardEntry.COL_NAME_USERID, BoardEntry.COL_NAME_CONTENT, BoardEntry.COL_NAME_SINGDATE},
                BoardEntry.COL_NAME_IDX+"="+rowId,
                null,
                null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

}
