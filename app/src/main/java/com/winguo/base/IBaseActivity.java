package com.winguo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.View;

/**
 * @author hcpai
 * @desc baseactivity的接口
 */

public interface IBaseActivity {

    /**
     * 初始化视图
     */
    void initViews();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 设置监听器
     */
    void setListener();

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    void handleMsg(Message msg);

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    void doClick(View v);

    /**
     * 处理广播接收者发来的信息
     *
     * @param context 上下文对象
     * @param intent  意图
     */
    void doReceived(Context context, Intent intent);
}
