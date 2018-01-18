package com.winguo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guobi.gblocation.GBDLocation;
import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.base.BaseFragActivity;
import com.winguo.utils.BitmapUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/2/9.
 */

public class StartActivity extends BaseFragActivity {

    private ViewPager start_vp;
    private List<Integer> images = new ArrayList<>();
    private ImageView start_next_btn;
    private LinearLayout ll_points;
    private Boolean isGuide;
    private TextView start_jump_btn;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    private int lastPosition = 0;


    @Override
    public void setStateColor() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // CommonUtil.stateSetting(this, R.color.start4_color);
    }

    @Override
    protected void initViews() {
        start_vp =  findViewById(R.id.start_vp);
        ll_points =  findViewById(R.id.ll_points);
        start_next_btn =  findViewById(R.id.start_next_btn);
        start_jump_btn =  findViewById(R.id.start_jump_btn);
        if (!isGuide) {
            start_jump_btn.setVisibility(View.GONE);
        } else {
            start_jump_btn.setVisibility(View.VISIBLE);
            start_next_btn.setVisibility(View.GONE);
            ll_points.setVisibility(View.GONE);
        }
        if (images.size() > 1) {
            for (int i = 0; i < images.size(); i++) {
                ImageView point = new ImageView(this);
                point.setImageResource(R.drawable.point_selecter);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 15;

                ll_points.addView(point, lp);
            }
            ll_points.getChildAt(0).setSelected(true);
        }
        StartPagerAdapter adapter = new StartPagerAdapter();
        start_vp.setAdapter(adapter);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_start;
    }


    private static final int ACCESS_COARSE_LOCATION = 0x852;

    @Override
    protected void initData() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GBDLocation.getInstance().startLocation(null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_COARSE_LOCATION);
            }
        }

        //启动页数据
        isGuide = (Boolean) SPUtils.get(this, Constants.isGuide, false);
        if (!isGuide) {
            images.clear();
            images.add(R.drawable.start1);
            images.add(R.drawable.start2);
            images.add(R.drawable.start3);
            images.add(R.drawable.start);

        } else {
            images.clear();
            images.add(R.drawable.start);

            //3s后跳转
            handler.postDelayed(runnable, 3000);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ACCESS_COARSE_LOCATION == requestCode) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GBDLocation.getInstance().startLocation(null);
            } else {
                //Intents.noPermissionStatus(this,"录音权限需要打开");
                ToastUtil.showToast(this, "定位权限未打开，请先开启此权限！");

            }
        }

    }

    @Override
    protected void setListener() {
        start_next_btn.setOnClickListener(this);
        start_jump_btn.setOnClickListener(this);
        if (images.size() > 1) {
            start_vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    ll_points.getChildAt(lastPosition).setSelected(false);
                    ll_points.getChildAt(position).setSelected(true);
                    lastPosition = position;
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    //滑动到最后一张时显示按钮
                    if (position == images.size() - 1) {
                        start_next_btn.setVisibility(View.VISIBLE);
                    } else {
                        start_next_btn.setVisibility(View.GONE);
                    }
                }


            });
        }

    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {

        }
    }

    @Override
    protected void doClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.start_next_btn:
                SPUtils.put(this, Constants.isGuide, true);
                it = new Intent(StartActivity.this, MainActivity.class);
                break;
            case R.id.start_jump_btn:
                SPUtils.put(this, Constants.isGuide, true);
                handler.removeCallbacks(runnable);
                it = new Intent(StartActivity.this, MainActivity.class);
                break;
        }
        startActivity(it);
        finish();

    }

    private class StartPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(StartActivity.this);
            int screenWidth = ScreenUtil.getScreenWidth(StartActivity.this);
            int screenHeight = ScreenUtil.getScreenHeight(StartActivity.this);
            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(getResources(), images.get(position), screenWidth, screenHeight);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
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


}
