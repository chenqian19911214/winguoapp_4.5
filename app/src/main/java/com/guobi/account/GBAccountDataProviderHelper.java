package com.guobi.account;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * 操作数据的接口类
 * Created by chenq on 2017/1/3.
 */
public class GBAccountDataProviderHelper {
    /**
     * 设置URI和对应的值
     *
     * @param context
     * @param uri
     * @param value
     */
    public static void setValueByUri(Context context, String uri, String value) {
        GBAccountData obj = GBAccountDataProviderHelper.getObj(context, uri);
        if (obj != null) {
            obj.value = value;
            GBAccountDataProviderHelper.update(context, obj);
        } else {
            obj = new GBAccountData(uri, value);
            GBAccountDataProviderHelper.insert(context, obj);
        }
    }

    public static void setDataByUri(Context context, String uri, byte[] data) {
        GBAccountData obj = GBAccountDataProviderHelper.getObj(context, uri);
        if (obj != null) {
            obj.extra_data = data;
            GBAccountDataProviderHelper.update(context, obj);
        } else {
            obj = new GBAccountData(uri, data);
            GBAccountDataProviderHelper.insert(context, obj);
        }
    }


    /**
     * 通过URI来获得值
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getValueByUri(Context context, String uri) {
        final GBAccountData obj = GBAccountDataProviderHelper.getObj(context, uri);
        if (obj != null) {
            return obj.value;
        }
        return null;
    }

    public static byte[] getDataByUri(Context context, String uri) {
        final GBAccountData obj = GBAccountDataProviderHelper.getObj(context, uri);
        if (obj != null) {
            return obj.extra_data;
        }
        return null;
    }


    /**
     * 通过URI来删除数据
     *
     * @param context
     * @param uri
     * @return
     */
    public static void removeByUri(Context context, String uri) {
        int id = GBAccountDataProviderHelper.find(context, uri);
        if (id != -1) {
            GBAccountDataProviderHelper.remove(context, id);
        }
    }

    public static final void clear(Context context) {

        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                contentResolver.delete(
                        GBAccountDataProvider.CONTENT_URI,
                        null,
                        null
                );
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        }

    }

    /**
     * 插入物件
     * 数据ID会被赋值
     * 同一个ID不能重复插入
     *
     * @param context
     * @param obj
     * @return
     */
    public static final boolean insert(Context context, GBAccountData obj) {

        if (GBLogUtils.DEBUG) {
            if (find(context, (int) obj.id) != -1) {
                throw new IllegalStateException("Dumplicate obj");
            }
        }

        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(GBAccountDataProvider.Columns.URI, obj.uri);
                contentValue.put(GBAccountDataProvider.Columns.VALUE, obj.value);
                if (obj.extra_data != null) {
                    contentValue.put(GBAccountDataProvider.Columns.EXTRA_DATA, obj.extra_data);
                }

                Uri result = contentResolver.insert(GBAccountDataProvider.CONTENT_URI, contentValue);
                if (result != null) {
                    obj.id = Integer.parseInt(result.getPathSegments().get(0));
                    return true;
                }
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新数据
     *
     * @param context
     * @param obj
     * @return
     */
    public static final boolean update(Context context, GBAccountData obj) {

        if (GBLogUtils.DEBUG) {
            if (find(context, (int) obj.id) == -1) {
                throw new IllegalStateException("Obj not found!");
            }
        }

        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                ContentValues contentValue = new ContentValues();
                contentValue.put(GBAccountDataProvider.Columns.URI, obj.uri);
                contentValue.put(GBAccountDataProvider.Columns.VALUE, obj.value);
                if (obj.extra_data != null) {
                    contentValue.put(GBAccountDataProvider.Columns.EXTRA_DATA, obj.extra_data);
                }

                final String whereClause = GBAccountDataProvider.Columns._ID + "=" + obj.id;
                int result = contentResolver.update(
                        GBAccountDataProvider.CONTENT_URI,
                        contentValue,
                        whereClause,
                        null
                );
                return result > 0;
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除数据
     * ID置为-1
     *
     * @param context
     * @param obj
     */
    public static final void remove(Context context, GBAccountData obj) {
        remove(context, obj.id);
        obj.id = -1;
    }

    public static final void remove(Context context, int id) {
        if (GBLogUtils.DEBUG) {
            if (find(context, id) == -1) {
                throw new IllegalStateException("Obj not found!");
            }
        }

        try {

            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                final String whereClause = GBAccountDataProvider.Columns._ID + "=" + id;
                contentResolver.delete(
                        GBAccountDataProvider.CONTENT_URI,
                        whereClause,
                        null
                );
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        }
    }

    public static final int find(Context context, int obj_id) {
        Cursor cursor = null;
        try {

            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                final String whereClause = GBAccountDataProvider.Columns._ID + "=" + obj_id;
                cursor = contentResolver.query(
                        GBAccountDataProvider.CONTENT_URI,
                        null,
                        whereClause,
                        null,
                        null
                );
                if (cursor != null && cursor.moveToFirst())
                    return obj_id;
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return -1;
    }

    public static final int find(Context context, String uri) {
        Cursor cursor = null;
        try {

            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                final String whereClause =
                        GBAccountDataProvider.Columns.URI + "=" + "'" + uri + "'";
                cursor = contentResolver.query(
                        GBAccountDataProvider.CONTENT_URI,
                        null,
                        whereClause,
                        null,
                        null
                );
                if (cursor != null && cursor.moveToFirst())
                    return cursor.getInt(cursor.getColumnIndexOrThrow(GBAccountDataProvider.Columns._ID));
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return -1;
    }
    public static final GBAccountData getObj(Context context, String uri) {
        Cursor cursor = null;
        try {

            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                final String whereClause = GBAccountDataProvider.Columns.URI + "=" + "'" + uri + "'";
                cursor = contentResolver.query(
                        GBAccountDataProvider.CONTENT_URI,
                        null,
                        whereClause,
                        null,
                        null
                );
                if (cursor != null && cursor.moveToFirst()) {
                    return createDataFromCursor(cursor);
                }
            }
        } catch (Exception e) {
            if (GBLogUtils.DEBUG)
                e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    private static GBAccountData createDataFromCursor(Cursor cursor) {
        GBAccountData info = new GBAccountData();
        info.id = cursor.getInt(cursor.getColumnIndex(GBAccountDataProvider.Columns._ID));
        info.uri = cursor.getString(cursor.getColumnIndex(GBAccountDataProvider.Columns.URI));
        info.value = cursor.getString(cursor.getColumnIndex(GBAccountDataProvider.Columns.VALUE));
        info.extra_data = cursor.getBlob(cursor.getColumnIndex(GBAccountDataProvider.Columns.EXTRA_DATA));
        return info;
    }

    private GBAccountDataProviderHelper() {
    }

}
