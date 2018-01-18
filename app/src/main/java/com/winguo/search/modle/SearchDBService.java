package com.winguo.search.modle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 * 查询历史记录的数据库管理类
 */
public class SearchDBService {
    private static final String TABLE_NAME="search";
    private static SearchDBService dbService;
    private final SearchOpenHelper openHelper;

    private SearchDBService(Context context) {
        openHelper = new SearchOpenHelper(context);
    }
    //使用单例
    public static SearchDBService getInstance(Context context){
        if (dbService==null){
            synchronized (SearchDBService.class){
                if (dbService==null){
                    dbService=new SearchDBService(context);
                }
            }
        }
        return dbService;
    }

    /**
     * 向数据库中插入数据
     * @param field 插入的数据
     */
    public void insert(String field){
        List<String> query = query();
        //判断数据库中是否有了这个数据
        if(query.contains(field)){
            //包含就先删除
           delete(field);
        }
            //不包含插入
            SQLiteDatabase db = openHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("field", field);
            db.insert(TABLE_NAME, null, values);
            db.close();

    }

    /**
     * 删除数据库中数据
     * @param field 删除条件
     */
    public void delete(String field){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete(TABLE_NAME,"field=?",new String[]{field});
        db.close();
    }
    /**
     * 删除数据库中数据
     */
    public void deleteAll(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }


    /**
     * 修改数据
     * @param field
     * @param time
     */
    public void update(String field,String time){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("time",time);
        db.update(TABLE_NAME,values,"field=?",new String[]{field});
        db.close();
    }

    /**
     * 查询数据库
     * @return
     */
    public List<String> query(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        List<String> fields=new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor!=null&&cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String field1 = cursor.getString(cursor.getColumnIndex("field"));
                fields.add( field1);
            }
            cursor.close();
        }
        db.close();

        return fields;
    }

    /**
     * 倒序查询
     * @return
     */
    public synchronized List<String> queryDesc(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        List<String> fields=new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "_id desc", "10");
        if (cursor!=null&&cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String field1 = cursor.getString(cursor.getColumnIndex("field"));
                fields.add( field1);
            }
            cursor.close();
        }
        db.close();
        return fields;
    }


}
