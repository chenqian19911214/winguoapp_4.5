package com.winguo.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author hcpai
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    protected Context mContext;
    protected View view;
    public BaseFragment() {
    }
    /**
     * 是否已经创建视图
     */
    private boolean isViewCreated = false;
    /**
     * 是否已加载完数据
     */
    public static int VOICE_RESULT = 1;
    public static int RESULT_OK = -1;
    protected LayoutInflater mInflater;
    protected Resources resources;
    protected Activity context;

    @Override
    public void onAttach(Activity activity) {
        context = getActivity();
        mContext = getContext();
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("dnw +++++++++++++++++++"+BaseFragment.class.getSimpleName(), "onCreateView");

        view = inflater.inflate(getLayout(), container, false);
        mInflater = inflater;
        resources = getContext().getResources();
        initData();
        initView(view);
        setListener();
        isViewCreated = true;
        return view;
    }


    /**
     * 布局资源
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化view
     * @param view
     */
    protected abstract void initView(View view);
    protected  abstract void initData();
    protected  abstract void setListener();
    protected  abstract void doClick(View v);

    @Override
    public void onClick(View v) {
        doClick(v);
    }


}
