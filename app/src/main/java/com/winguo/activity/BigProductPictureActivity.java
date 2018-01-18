package com.winguo.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.winguo.R;
import com.winguo.mine.order.list.IPopWindowProtocal;
import com.winguo.net.GlideUtil;
import com.winguo.utils.ScreenUtil;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/12/23.
 */

public class BigProductPictureActivity extends BaseActivity2 {
    private FrameLayout big_picture_back_btn;
    private TextView tv_current_position;
    private TextView tv_picture_count;
    private ImageView big_picture_top_more;
    private FrameLayout picture_container;
    private ArrayList<String> pictureUrls;
    private int mPosition;

    @Override
    protected int getViewId() {
        return R.layout.activity_big_picture;
    }

    @Override
    protected void initViews() {
        //获取传过来的数据
        Intent intent = getIntent();
        pictureUrls = intent.getStringArrayListExtra("urls");
        mPosition = intent.getIntExtra("position", 0);
        initView();
        tv_current_position.setText(String.valueOf(mPosition + 1));
        tv_picture_count.setText(String.valueOf(pictureUrls.size()));
        ViewPager viewPager = new ViewPager(BigProductPictureActivity.this);
        //获取屏幕的高度
        int screenHeight = ScreenUtil.getScreenHeight(BigProductPictureActivity.this);
        int hight = screenHeight - 560;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        picture_container.addView(viewPager, params);
        PictureAdapter adapter = new PictureAdapter();
        viewPager.setAdapter(adapter);
        //设置显示的是点击的图片位置
        viewPager.setCurrentItem(mPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                tv_current_position.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        big_picture_back_btn.setOnClickListener(this);
        big_picture_top_more.setOnClickListener(this);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.big_picture_back_btn:
                finish();
                break;
            case R.id.big_picture_top_more:
                new SharePopWindow().onShow(big_picture_top_more);
                break;
        }

    }

    private void initView() {
        big_picture_back_btn = (FrameLayout) findViewById(R.id.big_picture_back_btn);
        tv_current_position = (TextView) findViewById(R.id.tv_current_position);
        tv_picture_count = (TextView) findViewById(R.id.tv_picture_count);
        big_picture_top_more = (ImageView) findViewById(R.id.big_picture_top_more);
        picture_container = (FrameLayout) findViewById(R.id.picture_container);
    }

    private class PictureAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pictureUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(BigProductPictureActivity.this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideUtil.getInstance().loadImage(BigProductPictureActivity.this, pictureUrls.get(position),R.drawable.big_banner_bg,R.drawable.big_banner_error_bg, iv);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 分享的弹窗
     */
    public class SharePopWindow implements IPopWindowProtocal {

        private PopupWindow mPopupWindow;
        private TextView mSharing_home_tv;
        private TextView mSharing_share_tv;

        public SharePopWindow() {
            initUI();
            initData();
        }

        @Override
        public void initUI() {
            View contentView = LayoutInflater.from(BigProductPictureActivity.this).inflate(R.layout.pop_share, null);
            mSharing_home_tv = (TextView) contentView.findViewById(R.id.sharing_home_tv);
            mSharing_share_tv = (TextView) contentView.findViewById(R.id.sharing_share_tv);
            mPopupWindow = new PopupWindow(350, 250);
            mPopupWindow.setContentView(contentView);
            mPopupWindow.setFocusable(true);//PopWindow创建的时候默认内部控件无法被点击
            mPopupWindow.setOutsideTouchable(true);//设置了setFocusable后 外部控件无法点击
            //点击外部控件 让Pop消失
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            mPopupWindow.update();//刷新下页面  此时Pop还有消失出来
        }

        @Override
        public void initData() {
            mSharing_home_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startActivity(new Intent(BigProductPictureActivity.this, MainActivity.class));
                }
            });
            mSharing_share_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShare();
                }
            });
        }

        @Override
        public void onShow(View anchor) {
            if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                mPopupWindow.showAsDropDown(anchor, 0, 30);
            }
        }

        @Override
        public void onDismiss() {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        oks.setImageUrl(pictureUrls.get(mPosition));
        oks.show(this);
    }
}
