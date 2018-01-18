package com.winguo.net;

/**
 * 对网络请求返回结果的回调
 */
public interface IStringCallBack2 {
    //请求成功
    void stringReturn(String result);

    //请求失败
    void failReturn();
}
