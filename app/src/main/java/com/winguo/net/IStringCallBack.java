package com.winguo.net;

/**
 * 对网络请求返回结果的回调
 */
public interface IStringCallBack {
    int stringReturn(String result);
    void exceptionMessage(String message);
}
