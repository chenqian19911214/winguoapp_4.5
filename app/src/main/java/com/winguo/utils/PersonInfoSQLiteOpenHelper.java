package com.winguo.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 个人资料数据库
 * Created by admin on 2017/5/8.
 */

public class PersonInfoSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;//版本
    private static final String DB_NAME = "personal.db";//数据库名
    public static final String STUDENT_TABLE = "user";//表名
    public static final String _ID = "_id";//表中的列名
    public static final String ACCOUNT_NAME = "account_name";//表中的列名 账户名
    public static final String USER_SEX = "sex";//表中的列名 性别
    public static final String USER_AGE = "age";//表中的列名 年龄
    public static final String USER_UUID = "uuid";//表中的列名 uuid
    public static final String USER_SPACE_NAME = "open_name";//表中的列名 空间站名字
    public static final String USER_DEFALUT_ADDRESS = "address_def";//表中的列名 默认收货地址
    public static final String USER_TEL = "tel";//表中的列名 手机号

    //创建数据库语句，STUDENT_TABLE，_ID ，NAME的前后都要加空格
    private static final String CREATE_TABLE = "create table " + STUDENT_TABLE + " ( " + _ID + " Integer primary key autoincrement," + ACCOUNT_NAME + " varchar(200)," + USER_TEL + " varchar(200),"+USER_AGE+" varchar(200),"+USER_UUID+" varchar(200),"+USER_SPACE_NAME+" varchar(200),"+USER_DEFALUT_ADDRESS+" varchar(200)," + USER_SEX + " varchar(200))";
    public PersonInfoSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
