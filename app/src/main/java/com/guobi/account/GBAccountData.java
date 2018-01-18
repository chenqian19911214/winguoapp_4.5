package com.guobi.account;

import android.content.Context;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * 原数据
 * Created by chenq on 2017/1/3.
 */
public class GBAccountData {

    /**
     * URI
     * 一经分配便不可改变
     */
    public String uri;

    /**
     * 数据的值
     */
    public String value;

    /**
     *
     */
    public byte[] extra_data;

    /**
     * ID，将在数据库操作时分配
     * 如果ID为-1、表示这是一条尚未放置（尚未写入数据库）的数据
     */
    public int id = -1;


    public GBAccountData() {

    }

    public GBAccountData(String uri) {
        this.uri = uri;
    }

    public GBAccountData(String uri, String value) {
        this.uri = uri;
        this.value = value;
    }

    public GBAccountData(String uri, byte[] data) {
        this.uri = uri;
        this.extra_data = data;
    }

    /**
     * 若是从未写入的数据，将会新分配一个ID
     *
     * @param context
     * @return
     */
    public boolean writeToDB(Context context) {
        if (id <= 0)
            return GBAccountDataProviderHelper.insert(context, this);
        else
            return GBAccountDataProviderHelper.update(context, this);
    }

    /**
     * 从数据库移除该数据，ID会置为-1
     *
     * @param context
     */
    public void removeFromDB(Context context) {
        GBAccountDataProviderHelper.remove(context, this);
    }

    public final boolean isWroteToDB() {
        return this.id > 0;
    }

    public void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "Dumpping Data: " + this);
            GBLogUtils.DEBUG_DISPLAY(this, " uri: " + uri);
            GBLogUtils.DEBUG_DISPLAY(this, " value: " + value);
        }
    }

}
