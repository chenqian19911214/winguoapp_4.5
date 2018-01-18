package com.guobi.account;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * Created by chenq on 2017/1/3.
 */
public final class GBAccountDataProvider extends ContentProvider {
    public static Uri CONTENT_URI = Uri.parse("content://" + "com.winguo." + GBAccountDataProvider.class.getSimpleName());

    /**
     * 数据库列名定义
     */
    public static final class Columns implements BaseColumns {
        public static final String URI = "uri";
        public static final String VALUE = "value";
        public static final String EXTRA_DATA = "extra_data";
    }

    private SQLiteDatabase sqlDB;
    private DatabaseHelper dbHelper;
    private static final String DATABASE_NAME = "gbaccount.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "data";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + "(" +
                            "_id INTEGER PRIMARY KEY" +
                            ", " + Columns.URI + " TEXT" +
                            ", " + Columns.VALUE + " TEXT" +
                            //From version 2
                            ", " + Columns.EXTRA_DATA + " BLOB" +
                            ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1) {
                switch (newVersion) {
                    case 2:
                        if (GBLogUtils.DEBUG) {
                            GBLogUtils.DEBUG_DISPLAY(this, "@@@updating database from:"
                                    + oldVersion + "to newVer:" + newVersion);
                        }

                        String sql = "ALTER TABLE " + TABLE_NAME +
                                " ADD COLUMN " + Columns.EXTRA_DATA + " BLOB";
                        db.execSQL(sql);
                        return;
                }
            }


            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    }

    @Override
    public int delete(Uri uri, String s, String[] as) {
        sqlDB = dbHelper.getWritableDatabase();
        int num = sqlDB.delete(TABLE_NAME, s, as);
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        sqlDB = dbHelper.getWritableDatabase();
        long rowId = sqlDB.insert(TABLE_NAME, null, contentvalues);
        Uri newUri = Uri.withAppendedPath(CONTENT_URI, "" + rowId);
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;
    }

    @Override
    public boolean onCreate() {
        //final String uri_str = GBManifestConfig.getMetaDataValue(getContext(),"GBAccountDataProviderURI");
       // final String uri_str = "content://" + getContext().getPackageName() + "." + GBAccountDataProvider.class.getSimpleName();
       // CONTENT_URI = Uri.parse(uri_str);
        dbHelper = new DatabaseHelper(getContext());
        return (dbHelper == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, projection, selection, null, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
        Cursor c = null;
        try {
            c = query(uri, null, null, null, null);
            if (c == null || c.getCount() == 0) {
                insert(uri, contentvalues);
                return 1;
            } else {
                sqlDB = dbHelper.getWritableDatabase();
                int result = sqlDB.update(TABLE_NAME, contentvalues, s, as);
                if (result > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return result;
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
    }

}
