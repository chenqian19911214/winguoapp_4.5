package com.winguo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.winguo.bean.PersonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/8.
 */

public class PersonInfoSQLOperateImpl {

    private PersonInfoSQLiteOpenHelper dbOpenHelper;

    public PersonInfoSQLOperateImpl(Context context) {
        dbOpenHelper = new PersonInfoSQLiteOpenHelper(context);
    }


    public void add(PersonInfo p) {

        List<PersonInfo> personHistories = queryDesc();
        //判断数据库中是否有了这个数据
        if(personHistories.contains(p)){
            //包含就先删除
           // delete(p);
            return;
        }
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (p.accountName != null)
            values.put(PersonInfoSQLiteOpenHelper.ACCOUNT_NAME, p.accountName);
        if (p.accountTel != null)
            values.put(PersonInfoSQLiteOpenHelper.USER_TEL, p.accountTel);
        if (p.accountSpaceName != null)
            values.put(PersonInfoSQLiteOpenHelper.USER_SPACE_NAME, p.accountSpaceName);
        if (p.accountAge != null)
            values.put(PersonInfoSQLiteOpenHelper.USER_AGE, p.accountAge);
        if (p.accountTel != null)
            values.put(PersonInfoSQLiteOpenHelper.USER_SEX, p.accountSex);
        if (p.accountAddressDef != null)
            values.put(PersonInfoSQLiteOpenHelper.USER_DEFALUT_ADDRESS, p.accountAddressDef);
        if (p.accountUUID != null)
            values.put(PersonInfoSQLiteOpenHelper.USER_UUID, p.accountUUID);

        db.insert(PersonInfoSQLiteOpenHelper.STUDENT_TABLE, null, values);
        db.close();
    }

    /**
     * 删除数据库中数据
     * @param accountName 删除条件
     */
    public void delete(String accountName){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(PersonInfoSQLiteOpenHelper.STUDENT_TABLE, PersonInfoSQLiteOpenHelper.ACCOUNT_NAME + "=?",new String[]{accountName});
        db.close();
    }

    public void delete() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(PersonInfoSQLiteOpenHelper.STUDENT_TABLE,null,null);
        db.close();
    }

    public void updata(String acccountName,String keyName,String valueName) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyName, valueName);
        db.update(PersonInfoSQLiteOpenHelper.STUDENT_TABLE, values,PersonInfoSQLiteOpenHelper.ACCOUNT_NAME+"=?", new String[]{acccountName});
        db.close();
    }

    public String findByName(String acountName,String keyName) {

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(PersonInfoSQLiteOpenHelper.STUDENT_TABLE, null, PersonInfoSQLiteOpenHelper.ACCOUNT_NAME + "=?", new String[]{String.valueOf(acountName)}, null, null, null);
        String value = null;
        if(cursor != null && cursor.moveToFirst()){
            value = cursor.getString(cursor.getColumnIndex(keyName));
        }
            if (cursor != null) {
                cursor.close();
            }
        db.close();
        return value;
    }


    /**
     * @param acountName
     * @return
     */
    public PersonInfo findByName(String acountName) {

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(PersonInfoSQLiteOpenHelper.STUDENT_TABLE, null, PersonInfoSQLiteOpenHelper.ACCOUNT_NAME + "=?", new String[]{String.valueOf(acountName)}, null, null, null);
        PersonInfo personInfo = null;
        if(cursor != null && cursor.moveToFirst()){
            String accountName = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.ACCOUNT_NAME));
            String age = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_AGE));
            String sex = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_SEX));
            String spaceName = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_SPACE_NAME));
            String uuid = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_UUID));
            String addressDel = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_DEFALUT_ADDRESS));
            String tel = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_TEL));
            personInfo = new PersonInfo(accountName,sex,age,tel,addressDel,uuid,spaceName);
        }
        return personInfo;
    }

    /**
     * 倒序查询
     * @return
     */
    public List<PersonInfo> queryDesc(){

        List<PersonInfo> historys = null;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(PersonInfoSQLiteOpenHelper.STUDENT_TABLE, null, null, null, null,null, "_id desc", "10");
        if(cursor != null){
            historys = new ArrayList<PersonInfo>();
            while(cursor.moveToNext()){
                String accountName = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.ACCOUNT_NAME));
                String age = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_AGE));
                String sex = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_SEX));
                String spaceName = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_SPACE_NAME));
                String uuid = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_UUID));
                String addressDel = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_DEFALUT_ADDRESS));
                String tel = cursor.getString(cursor.getColumnIndex(PersonInfoSQLiteOpenHelper.USER_TEL));
                PersonInfo person = new PersonInfo(accountName,sex,age,tel,addressDel,uuid,spaceName);
                historys.add(person);
            }
            cursor.close();
        }
        return historys;

    }


}
