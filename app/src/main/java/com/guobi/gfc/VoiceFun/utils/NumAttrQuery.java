package com.guobi.gfc.VoiceFun.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.utils.Constants;
import com.winguo.utils.FileUtil;
import com.winguo.utils.Intents;

public class NumAttrQuery {


    public static synchronized String query(Context context, String number) {

        String dede = "";
        if (Intents.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            SQLiteDatabase db = null;
            if (!FileUtil.isSdcardAvailable()) {
                db = SQLiteDatabase.openDatabase(context.getFilesDir() + "/telocation.db", null, SQLiteDatabase.OPEN_READONLY);
            } else {

                try {
                    db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/telocation.db", null, SQLiteDatabase.OPEN_READONLY);
                    StringBuilder address = new StringBuilder("");
                    String _id = number.replaceAll("\\s*", "").substring(0, 7);


                    Cursor cursor = db.rawQuery("select location from mob_location where _id=?", new String[]{_id});
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        GBLogUtils.DEBUG_DISPLAY("TEL", cursor.getString(0));
                        address.append(cursor.getString(0));
                        cursor.moveToNext();
                        dede = address.toString();
                    }
                    cursor.close();
                    db.close();
                } catch (Exception e) {

                    dede = "";
                    e.printStackTrace();
                }


            }

            if (number.length() < 7) {
                return "";
            }

            return dede;

        } else {
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity) context).requestPermissions(permission,Constants.REQUEST_CODE_PERMISSIONS_WRITE_SD);
            }
        }

        return "";
    }

}
