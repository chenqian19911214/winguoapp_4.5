package com.guobi.account;

/**
 * 账号回调接口，各个方法保证运行在主线程
 * Created by chenq on 2017/1/3.
 */
public interface GBAccountCallback {
    void onTaskBegin();

    void onTaskEnd(int resultCode, String errMsg);

    void onTaskCanceled();
}
