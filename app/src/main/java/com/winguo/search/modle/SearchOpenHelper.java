package com.winguo.search.modle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/6.
 */

public class SearchOpenHelper extends SQLiteOpenHelper{

    private static final String SEARCH_DB_NAME = "search.db";
    private static final int SEARCH_DB_VERSION = 1;

    public SearchOpenHelper(Context context) {
        super(context, SEARCH_DB_NAME, null, SEARCH_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists search( _id integer primary key autoincrement,field text not null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
