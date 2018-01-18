package com.winguo.search.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.winguo.bean.SearchHistory;
import com.winguo.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/20.
 */

public class SQLOperateImpl {

    private RecordSQLiteOpenHelper dbOpenHelper;

    public SQLOperateImpl(Context context) {
        dbOpenHelper = new RecordSQLiteOpenHelper(context);
    }


    public void add(SearchHistory p) {
        List<SearchHistory> searchHistories = queryDesc();
        //判断数据库中是否有了这个数据
        if (searchHistories.contains(p)) {
            //包含就先删除
            delete(p);
        }
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSQLiteOpenHelper.NAME, p.name);
        values.put(RecordSQLiteOpenHelper.TYPE, p.type);
        db.insert(RecordSQLiteOpenHelper.STUDENT_TABLE, null, values);
    }

    /**
     * 添加实体店搜索数据库的数据
     *
     * @param p
     */
    public void add2(SearchHistory p) {
        List<SearchHistory> searchHistories = queryDesc2();
        //判断数据库中是否有了这个数据
        if (searchHistories.contains(p)) {
            //包含就先删除
            delete2(p);
        }
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSQLiteOpenHelper.NAME, p.name);
        values.put(RecordSQLiteOpenHelper.TYPE, p.type);
        db.insert(RecordSQLiteOpenHelper.TABLE2, null, values);
    }

    /**
     * 删除数据库中数据
     *
     * @param field 删除条件
     */
    public void delete(SearchHistory field) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(RecordSQLiteOpenHelper.STUDENT_TABLE, RecordSQLiteOpenHelper.NAME + "=?", new String[]{field.name});
        db.close();
    }

    /**
     * 删除实体店搜索数据库的数据
     *
     * @param field
     */
    public void delete2(SearchHistory field) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(RecordSQLiteOpenHelper.TABLE2, RecordSQLiteOpenHelper.NAME + "=?", new String[]{field.name});
        db.close();
    }

    /**
     * 删除表
     */
    public void delete() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(RecordSQLiteOpenHelper.STUDENT_TABLE, null, null);
        db.close();
    }

    /**
     * 删除实体店搜索数据库的表
     */
    public void delete2() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(RecordSQLiteOpenHelper.TABLE2, null, null);
        db.close();
    }

    public void updata(SearchHistory p) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSQLiteOpenHelper.NAME, p.name);
        db.update(RecordSQLiteOpenHelper.STUDENT_TABLE, values, RecordSQLiteOpenHelper.NAME + "=?", new String[]{String.valueOf(p.name)});
    }

    public List<SearchHistory> find() {
        List<SearchHistory> historys = null;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(RecordSQLiteOpenHelper.STUDENT_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            historys = new ArrayList<>();
            while (cursor.moveToNext()) {
                int type = cursor.getInt(cursor.getColumnIndex(RecordSQLiteOpenHelper.TYPE));
                String name = cursor.getString(cursor.getColumnIndex(RecordSQLiteOpenHelper.NAME));
                SearchHistory person = new SearchHistory(name, type);
                historys.add(person);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return historys;
    }

    /**
     * @param label
     * @return
     */
    public SearchHistory findByName(String label) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(RecordSQLiteOpenHelper.STUDENT_TABLE, null, RecordSQLiteOpenHelper.NAME + "=?", new String[]{String.valueOf(label)}, null, null, null);
        SearchHistory history = null;
        if (cursor != null && cursor.moveToFirst()) {
            int type = cursor.getInt(cursor.getColumnIndex(RecordSQLiteOpenHelper.TYPE));
            String name = cursor.getString(cursor.getColumnIndex(RecordSQLiteOpenHelper.NAME));
            history = new SearchHistory(name, type);
        }
        return history;
    }

    /**
     * 倒序查询
     *
     * @return
     */
    public List<SearchHistory> queryDesc() {
        CommonUtil.printI("cacaacacacaacaccaa","----------------------");

        List<SearchHistory> historys = null;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(RecordSQLiteOpenHelper.STUDENT_TABLE, null,null, null, null, null, "_id desc", "10");

        if (cursor != null) {
            historys = new ArrayList<>();
            while (cursor.moveToFirst()) {
                int type = cursor.getInt(1);
                String name = cursor.getString(2);
                SearchHistory person = new SearchHistory(name, type);
                historys.add(person);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return historys;

    }

    /**
     * 查询实体店搜索数据库的数据(倒叙排序)
     *
     * @return
     */
    public List<SearchHistory> queryDesc2() {

        List<SearchHistory> histories = null;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(RecordSQLiteOpenHelper.TABLE2, null, null, null, null, null, "_id desc", "10");
        if (cursor != null) {
            histories = new ArrayList<>();
            while (cursor.moveToNext()) {
                int type = cursor.getInt(cursor.getColumnIndex(RecordSQLiteOpenHelper.TYPE));
                String name = cursor.getString(cursor.getColumnIndex(RecordSQLiteOpenHelper.NAME));
                SearchHistory person = new SearchHistory(name, type);
                histories.add(person);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return histories;

    }

}
