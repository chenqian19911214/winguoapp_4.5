package com.winguo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.adapter.CommonAdapter;
import com.winguo.adapter.ViewHolder;
import com.winguo.bean.HelpVoice;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助说明 模块
 * 可扩展布局+动画
 * 针对隐藏和显示的部分为ListView
 */

public class ExpandCollapseLayoutListView extends FrameLayout implements View.OnClickListener {

    private IListView content;
    private ImageView expandView;
    private ImageView topHead;
    private TextView topTtle;
    private TextView topTitleTip;
    private RotateAnimation startAnim;
    private RotateAnimation endAnim;
    private boolean mIsExpand;
    private Context mContext;
    private CommonAdapter adapter;
    List<String> data = new ArrayList<>();
    private HelpVoice item ;
    private LinearLayout expand;

    public ExpandCollapseLayoutListView(Context context, HelpVoice item) {
        super(context);
        this.mContext = context;
        this.item = item;
        initalize();
    }

    public ExpandCollapseLayoutListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initalize();
    }

    public ExpandCollapseLayoutListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initalize();
    }

    /**
     * 初始化并添加到View
     */
    private void initalize() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.expand_collapse_top, this,true);
        expandView = (ImageView)findViewById(R.id.expand_top_right_imag);
        expand = (LinearLayout) findViewById(R.id.expand_click_item);
        topHead = (ImageView)findViewById(R.id.expand_top_img);
        topTtle = (TextView) findViewById(R.id.expand_top_title);
        topTitleTip = (TextView)findViewById(R.id.expand_top_title_tip);
        content = (IListView) findViewById(R.id.expand_content_lv);

        startAnim = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        endAnim = new RotateAnimation(180,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        startAnim.setDuration(350);
        endAnim.setDuration(350);
        startAnim.setFillAfter(true);
        endAnim.setFillAfter(true);

        //更改重要数据 抽取部分出来 只需提供数据就好了

        getResData();
        topHead.setImageResource(item.iconHead);
        topTtle.setText(item.topTitle);
        topTitleTip.setText(item.topTitleTip);

        adapter = new CommonAdapter<String>(mContext,data,R.layout.help_voice_item) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.help_voice_speek,item);
            }
        };

        setAnimListener();

    }

    /**
     * 从资源数组中获取数据 help_voice_res.xml
     */
    private void getResData() {
        data.addAll(item.resData);
    }

    private void setAnimListener() {
        content.setAdapter(adapter);
        expand.setOnClickListener(this);

    }

    public boolean isExpand() {
        return mIsExpand;
    }

    private void animateView(final View target, final int type) {
        final int duration = 350;
        Animation animation = new ExpandCollapseAnimation(target, type);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (type == ExpandCollapseAnimation.COLLAPSE) {
                    target.setVisibility(View.GONE);
                }
            }
        });
        animation.setDuration(duration);
        target.startAnimation(animation);
    }
    private View lastOpen;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expand_click_item:
                //折叠图片动画
                if (!isExpand()) {
                    expandView.clearAnimation();
                    expandView.startAnimation(startAnim);
                    mIsExpand = true;
                } else {
                    expandView.clearAnimation();
                    expandView.startAnimation(endAnim);
                    mIsExpand = false;
                }
                //折叠内容区 隐藏/显示
                if (content == lastOpen) {
                    lastOpen = null;
                    animateView(content, ExpandCollapseAnimation.COLLAPSE);
                } else {
                    if (lastOpen != null) {
                        animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
                    }

                    if (content.getMeasuredHeight() == 0) {

                        content.measure(getMeasuredWidth(), getMeasuredHeight());
                        content.requestLayout();
                    }
                    animateView(content, ExpandCollapseAnimation.EXPAND);
                    lastOpen = content;
                }

            break;
        }
    }
}
