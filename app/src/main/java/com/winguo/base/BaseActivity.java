package com.winguo.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.utils.CommonUtil;

import java.lang.ref.WeakReference;

/**
 * @author hcpai
 * @desc 基类activity
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    protected LayoutInflater mInflater;
    protected Context mContext;
    protected Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtil.stateSetting(this,R.color.white_top_color);
        setContentView(getLayout());
//        CommonUtil.stateSetting(this);
        mInflater = LayoutInflater.from(this);
        mContext = getBaseContext();
        resources = getResources();
        initData();
        initViews();
        setListener();
        // 将当前activity加入到全局控制的activity集合中
        StartApp.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除Activity
        StartApp.getInstance().removeActivity(this);
    }


    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.activity_in_anim,R.anim.activity_out_anim);
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
     * 加载布局
     */
    protected  abstract int getLayout();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化视图
     */
    protected abstract void initViews();

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
        WeakReference<BaseActivity> mWeakReference;

        public LeakHander(BaseActivity baseActivity) {
            mWeakReference = new WeakReference<BaseActivity>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().handleMsg(msg);
            }
        }
    }
}
