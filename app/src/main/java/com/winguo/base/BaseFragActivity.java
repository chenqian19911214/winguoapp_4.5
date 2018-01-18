package com.winguo.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.winguo.app.StartApp;
import com.winguo.utils.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * FragmentActivity 基类抽象
 * Created by admin on 2016/12/5.
 */

public abstract class BaseFragActivity extends FragmentActivity implements View.OnClickListener {

    protected LayoutInflater mInflater;
    private long exitTime = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStateColor();
        setContentView(getLayout());
        mInflater = LayoutInflater.from(this);
        initData();
        initViews();
        setListener();

    }
    public abstract void setStateColor();
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.showToast(this, "再点一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            /*Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
            mHomeIntent.addCategory(Intent.CATEGORY_HOME);
            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(mHomeIntent);*/
            finish();

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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

    /**
     * 设置字体不受 系统主题影响
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }
    /**
     * 设置字体不受 系统主题影响
     */
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
     * 初始化视图
     */
    protected abstract void initViews();

    /**
     * 加载布局
     */
    protected abstract int getLayout();

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
        WeakReference<BaseFragActivity> mWeakReference;

        public LeakHander(BaseFragActivity baseActivity) {
            mWeakReference = new WeakReference<BaseFragActivity>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().handleMsg(msg);
            }
        }
    }

}
