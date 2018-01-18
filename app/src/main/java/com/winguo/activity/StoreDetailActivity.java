package com.winguo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.guobi.gblocation.GBDLocation;
import com.guobi.gblocation.ILocationCallBck;
import com.guobi.gblocation.ILocationInfoCallBck;
import com.guobi.winguoapp.voice.VoiceFunActivity;
import com.winguo.R;
import com.winguo.adapter.RecyclerAdapter;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.StoreDetail;
import com.winguo.net.GlideUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NearbyStoreUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.OpenMapUtil;
import com.winguo.utils.ToastUtil;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 实体店 店铺详情
 * Created by admin on 2017/6/12.
 */

public class StoreDetailActivity extends BaseTitleActivity implements View.OnClickListener, NearbyStoreUtil.IRequestStoreDetail {

    private RecyclerView store_detail_live_action_rv;
    private FrameLayout store_detail_store_address_fl;
    private FrameLayout store_detail_store_tel_fl;
    private TextView store_detail_store_address;
    private TextView store_detail_store_time;
    private TextView store_detail_store_tel;
    private ImageView store_detail_speak;
    private TextView store_detail_store_distance;
    private TextView store_detail_store_per_capita;
    private TextView store_detail_store_type;
    private TextView store_detail_store_grade;
    private TextView store_detail_store_describe;
    private BGABanner store_detail_banner;
    private String store_id;
    private FrameLayout store_detail_container,store_detail_content;
    private Button btn_request_net;
    private View noNetView;
    private View mainView;
    private double log,lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setContentView(R.layout.activity_store_detail);
        initView();
        requestData();
        setListener();

    }

    /**
     * 请求数据
     */
    private void requestData() {
        LoadDialog.show(this,true);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (store_id != null) {
                NearbyStoreUtil.requestStoreDetail(StoreDetailActivity.this, store_id, log, lat, this);
            }
        } else {
            LoadDialog.dismiss(this);
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id");
        log = intent.getDoubleExtra("log",0.00f);
        lat = intent.getDoubleExtra("lat",0.00f);
        store_detail_container = (FrameLayout) findViewById(R.id.store_detail_container);
        store_detail_content = (FrameLayout) findViewById(R.id.store_detail_content);
        //无网络
        //(给容器添加布局)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        noNetView.setVisibility(View.GONE);
        store_detail_container.addView(noNetView, params);
        //店铺View
        mainView = View.inflate(this, R.layout.store_detail_include_layout, null);
        store_detail_banner = (BGABanner) mainView.findViewById(R.id.store_detail_banner);
        store_detail_store_describe = (TextView) mainView.findViewById(R.id.store_detail_store_describe);
        store_detail_store_grade = (TextView) mainView.findViewById(R.id.store_detail_store_grade);
        store_detail_store_type = (TextView) mainView.findViewById(R.id.store_detail_store_type);
        store_detail_store_per_capita = (TextView) mainView. findViewById(R.id.store_detail_store_per_capita);
        store_detail_store_distance = (TextView) mainView. findViewById(R.id.store_detail_store_distance);

        store_detail_store_address_fl = (FrameLayout) mainView.findViewById(R.id.store_detail_store_address_fl);
        store_detail_store_tel_fl = (FrameLayout) mainView. findViewById(R.id.store_detail_store_tel_fl);
        store_detail_store_address = (TextView) mainView.findViewById(R.id.store_detail_store_address);
        store_detail_store_time = (TextView) mainView. findViewById(R.id.store_detail_store_time);
        store_detail_store_tel = (TextView) mainView.findViewById(R.id.store_detail_store_tel);


        store_detail_live_action_rv = (RecyclerView) mainView.findViewById(R.id.store_detail_live_action_rv);
        store_detail_speak = (ImageView) mainView.findViewById(R.id.store_detail_speak);
        store_detail_live_action_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        store_detail_live_action_rv.setItemAnimator(new DefaultItemAnimator());
        mainView.setVisibility(View.GONE);
        store_detail_content.addView(mainView);

    }

    /**
     * 填充实体店详情数据
     * @param storeDetail
     */
    private void fillData(StoreDetail storeDetail) {
        StoreDetail.ResultBean resultBean = storeDetail.getResult().get(0);
        List<String> banners = new ArrayList<>();
        banners.add(resultBean.getCanView());
        setBannerData(banners);
        store_detail_store_describe.setText(resultBean.getM_maker_name_ch());
        store_detail_store_type.setText(resultBean.getTradeName());
        store_detail_store_per_capita.setText(resultBean.getM_entity_maker_average_consumption());
        store_detail_store_address.setText(resultBean.getM_maker_address_ch());
        store_detail_store_time.setText(resultBean.getM_entity_maker_hour_begin()+"-"+resultBean.getM_entity_maker_hour_end());
        store_detail_store_tel.setText(resultBean.getM_maker_mobile());
        setStoreDistance(resultBean.getDistance()+"");
        List<String> shopView = resultBean.getShopView();
        setLiveAction(shopView);

    }
    /**
     * 设置距离
     * @param distance
     */
    private void setStoreDistance(String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            double dis = Double.valueOf(distance);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format( dis / 1000);
                store_detail_store_distance.setText(format + "km");
            } else {
                store_detail_store_distance.setText(dis + "m");
            }
        }

    }

    /**
     * 设置实景图
     * @param data
     */
    private void setLiveAction(final List<String> data) {
       RecyclerCommonAdapter adapter =  new RecyclerCommonAdapter<String>(this, R.layout.store_live_action_item,data) {
            @Override
            protected void convert(RecylcerViewHolder holder, String s, int position) {
                ImageView store_live_action_ic = holder.getView(R.id.store_live_action_ic);
                GlideUtil.getInstance().loadImage(StoreDetailActivity.this,s,R.drawable.little_theme_loading_bg, R.drawable.little_theme_error_bg, store_live_action_ic);

            }
        };
        store_detail_live_action_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //开启大图模式
                Intent pictureActivity = new Intent(StoreDetailActivity.this, BigProductPictureActivity.class);
                pictureActivity.putStringArrayListExtra("urls", (ArrayList<String>) data);
                pictureActivity.putExtra("position", position);
                startActivity(pictureActivity);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 设置顶部店招图
     * @param banners
     */
    private void setBannerData(final List<String> banners) {
        store_detail_banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, final int position) {
                GlideUtil.getInstance().loadImage(StoreDetailActivity.this,model, R.drawable.big_banner_bg,R.drawable.big_banner_error_bg , itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/5/18  轮播点击
                        //开启大图模式
                        Intent pictureActivity = new Intent(StoreDetailActivity.this, BigProductPictureActivity.class);
                        pictureActivity.putStringArrayListExtra("urls", (ArrayList<String>) banners);
                        pictureActivity.putExtra("position", position);
                        startActivity(pictureActivity);
                    }
                });
            }
        });
        store_detail_banner.setData(banners,null);
        store_detail_banner.setAutoPlayInterval(5000);
    }

    /**
     * s设置监听
     */
    private void setListener() {
        btn_request_net.setOnClickListener(this);
        store_detail_store_address_fl.setOnClickListener(this);
        store_detail_store_tel_fl.setOnClickListener(this);
        store_detail_speak.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_detail_store_address_fl:
                //去打开地图导航
                String address = store_detail_store_address.getText().toString();
                OpenMapUtil.openMapByAddress(this,address);
                break;

            case R.id.store_detail_store_tel_fl:
                //拨打电话
                String tel = store_detail_store_tel.getText().toString();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Constants.REQUEST_CODE_PERMISSIONS_TEL);
                    }
                } else {
                    Intent it = new Intent();
                    it.setAction(Intent.ACTION_CALL);
                    it.setData(Uri.parse("tel:" + tel));
                    startActivity(it);
                }
                break;
            case R.id.store_detail_speak:
                //语音
                launchSpeak();
                break;
            case R.id.btn_request_net:
                // TODO: 2017/6/23 网络罗刷新
                noNetView.setVisibility(View.GONE);
                requestData();
                break;

        }
    }

    /**
     * 启动语音
     */
    private void launchSpeak() {

        if (Intents.checkPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || Intents.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION},Constants.REQUEST_CODE_PERMISSIONS_VOICE);
            }
        } else {
            Intent voice = new Intent(this, VoiceFunActivity.class);
            //analysis这个字段 false：语音-->文字 直接跳转到SearchActivity ; true：加词句分析（打电话 发短息）
            voice.putExtra("analysis", "true");
            startActivity(voice);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Constants.REQUEST_CODE_PERMISSIONS_TEL == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String tel = store_detail_store_tel.getText().toString();
                Intent it = new Intent();
                it.setAction(Intent.ACTION_CALL);
                it.setData(Uri.parse("tel:" + tel));
                startActivity(it);
            } else {
                Intents.noPermissionStatus(this,"电话权限需要打开");
            }
        }
        if (Constants.REQUEST_CODE_PERMISSIONS_VOICE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               launchSpeak();
            } else {
                Intents.noPermissionStatus(this,"录音权限需要打开");
            }
        }

    }
    @Override
    public void storeDetail(StoreDetail storeDetail) {
        LoadDialog.dismiss(this);
        if (storeDetail != null) {
            fillData(storeDetail);
            mainView.setVisibility(View.VISIBLE);
        } else {
            ToastUtil.showToast(this,"没有找到该店铺");
            finish();
        }
    }

    @Override
    public void storeDetailErrorMsg(String error) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
    }

}
