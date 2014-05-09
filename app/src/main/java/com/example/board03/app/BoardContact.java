package com.example.board03.app;

import android.provider.BaseColumns;

/**
 * Created by gkimms on 2014-04-26.
 */
public final class BoardContact {
    public BoardContact(){

    }

    public static abstract class BoardEntry implements BaseColumns{
        public static final String TABLE_NAME = "tb_board";
        public static final String COL_NAME_IDX = "_id";
        public static final String COL_NAME_TITIL = "title";
        public static final String COL_NAME_USERID = "userid";
        public static final String COL_NAME_CONTENT = "content";
        public static final String COL_NAME_SINGDATE = "signdate";
        public static final String CHECK_STATE = "checked";
    }
}
