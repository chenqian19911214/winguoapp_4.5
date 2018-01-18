package com.winguo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.winguo.R;
import com.winguo.app.StartApp;

import java.lang.ref.WeakReference;

/**
 * @author hcpai
 * @desc 基类activity
 */

public abstract class BaseActivity2 extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initViews();
        initData();
        setListener();
        // 将当前activity加入到全局控制的activity集合中
        StartApp.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除Activity
        StartApp.getInstance().removeActivity(this);
    }

    /**
     * view被点击
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        doClick(view);
    }

    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getViewId();

    /**
     * 初始化视图
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置监听器
     */
    protected abstract void setListener();

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    protected abstract void handleMsg(Message msg);
    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    protected abstract void doClick(View v);

    /**
     * handler机制
     */
    public Handler handler = new LeakHander(this);


    //弱引用，防止内存泄漏
    static class LeakHander extends Handler {
        WeakReference<BaseActivity2> mWeakReference;

        public LeakHander(BaseActivity2 baseActivity) {
            mWeakReference = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().handleMsg(msg);
            }
        }
    }

    /**
     * 带动画向上一页滑动
     */
    protected void toPre() {
        // 父类只知道子类要执行动画 但是跳到哪个界面父类是不知道的
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }

    /**
     * 带动画向下一页滑动
     */
    protected void toNext() {
        overridePendingTransition(R.anim.next_in, R.anim.next_out);
    }

    /**
     * 页面跳转
     *
     * @param cls
     */
    protected void jumpToNextActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        toNext();
        finish();
    }

    /**
     * 页面跳转
     *
     * @param cls
     */
    protected void jumpToPreActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        toPre();
        finish();
    }
}
