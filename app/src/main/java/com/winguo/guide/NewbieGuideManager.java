package com.winguo.guide;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.winguo.R;
import com.winguo.utils.ScreenUtil;

/**
 * Created by xufangzhen on 2016/6/15.
 */
public class NewbieGuideManager {

    private static final String TAG = "newbie_guide";

    public static final int TYPE_LIST = 0;//收藏
    public static final int TYPE_COLLECT = 1;//list

    private Activity mActivity;
    private SharedPreferences sp;
    private NewbieGuide mNewbieGuide;
    private int mType;

    public NewbieGuideManager(Activity activity, int type) {
        mNewbieGuide = new NewbieGuide(activity);
        sp = activity.getSharedPreferences(TAG, Activity.MODE_PRIVATE);
        mActivity = activity;
        mType = type;
    }

    public NewbieGuideManager addView(View view, int shape) {
        mNewbieGuide.addHighLightView(view, shape);
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(int delayTime) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(TAG + mType, false);
        editor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_LIST:
                        mNewbieGuide.setEveryWhereTouchable(false).addIndicateImg(R.drawable.guide_left_arrow, ScreenUtils.dpToPx(mActivity,
                                60), ScreenUtils.dpToPx(mActivity, 110),0,0).addMsgAndKnowTv("这个listview滚动到item6后出现新手引导浮层，\n只有点击我知道啦才会想消失",
                                -ScreenUtils.dpToPx(mActivity, 250)).show();
                        break;
                    case TYPE_COLLECT:
                        //mNewbieGuide.addIndicateImg(R.drawable.winguo_jump_first_guide, ScreenUtils.dpToPx(mActivity, -20), ScreenUtils.dpToPx(mActivity, 30)).addIndicateImg2(R.drawable.guide_right,50, ScreenUtils.getScreenHeight(mActivity)-200).show();
                        int screenWidth = ScreenUtil.getScreenWidth(mActivity);
                        if (screenWidth == 720) {

                            mNewbieGuide.addLayout(R.layout.guide_voice_lt,0,100).addTesxtView(ScreenUtils.dpToPx(mActivity, -20), ScreenUtils.dpToPx(mActivity, 30)).show();

                        } else {

                            mNewbieGuide.addLayout(R.layout.guide_voice_lt,0,200).addTesxtView(ScreenUtils.dpToPx(mActivity, -20), ScreenUtils.dpToPx(mActivity, 30)).show();
                        }
                        break;
                }
            }
        }, delayTime);
    }

    public void showWithListener(int delayTime, NewbieGuide.OnGuideChangedListener onGuideChangedListener) {
        mNewbieGuide.setOnGuideChangedListener(onGuideChangedListener);
        show(delayTime);
    }

    /**
     * 判断新手引导也是否已经显示了
     */
    public static boolean isNeverShowed(Activity activity, int type) {
        return activity.getSharedPreferences(TAG, Activity.MODE_PRIVATE).getBoolean(TAG + type, true);
    }


}
