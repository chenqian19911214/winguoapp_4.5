package com.winguo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * @author hcpai
 * @desc 基类Fragment
 */

public abstract class BaseFragment2 extends Fragment {
    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    public Activity mActivity;

    public Context mContext;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        mContext = getActivity();
        rootView = inflater.inflate(getLayout(), container, false);
        initView(rootView);
        setListener();
        if (!hasCreateView && getUserVisibleHint()) {
            loadData();
            //onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            loadData();
            //onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            //onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    /**
     * 留给子类自己选择实现
     */
    protected void loadData() {
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    /**
     * 布局资源
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化view
     *
     * @param view
     */
    protected abstract void initView(View view);

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
     * handler机制
     */
    public Handler handle = new LeakHander(this);

    //弱引用，防止内存泄漏
    private static class LeakHander extends Handler {
        WeakReference<BaseFragment2> mWeakReference;

        public LeakHander(BaseFragment2 baseFragment) {
            mWeakReference = new WeakReference<>(baseFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().handleMsg(msg);
            }
        }
    }
}
