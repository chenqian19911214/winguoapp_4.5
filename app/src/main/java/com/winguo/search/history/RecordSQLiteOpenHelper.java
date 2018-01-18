package com.winguo.search.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 历史记录数据库
 * Created by admin on 2017/4/19.
 */

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;//版本号改为2,新增一张表
    private static final String DB_NAME = "history.db";//数据库名
    public static final String STUDENT_TABLE = "student";//表名
    public static final String TABLE2 = "nearby_history";//表名
    public static final String _ID = "_id";//表中的列名
    public static final String NAME = "name";//表中的列名
    public static final String TYPE = "type";//表中的列名
    //创建数据库语句，STUDENT_TABLE，_ID ，NAME的前后都要加空格
    public static final String CREATE_TABLE_HISTORY = "create table " + STUDENT_TABLE + " ( " + _ID + " Integer primary key autoincrement," + TYPE + " integer," + NAME + " varchar(200))";
    public static final String CREATE_TABLE_NEARBY_HISTORY = "create table " + TABLE2 + " ( " + _ID + " Integer primary key autoincrement," + TYPE + " integer," + NAME + " varchar(200))";

    public RecordSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    //数据库第一次被创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_NEARBY_HISTORY);
    }

    //版本升级时被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TABLE_NEARBY_HISTORY);
    }
}
