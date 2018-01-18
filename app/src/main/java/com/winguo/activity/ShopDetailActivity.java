package com.winguo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountInfo;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.winguoapp.voice.VoiceFunActivity;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.CartShopList;
import com.winguo.bean.SectionType;
import com.winguo.bean.StoreDetail;
import com.winguo.bean.StoreShop;
import com.winguo.bean.StoreShopDetail;
import com.winguo.login.LoginActivity;
import com.winguo.net.GlideUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NearbyStoreUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.OpenMapUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.ShopSpecSelectDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 实体店 商品详情
 * Created by admin on 2017/6/14.
 */

public class ShopDetailActivity extends BaseTitleActivity implements View.OnClickListener, NearbyStoreUtil.IRequestStoreShopDetail, NearbyStoreUtil.IRequestStoreDetail {

    private BGABanner store_shop_detail_banner;
    //private RecyclerView store_shop_detail_rv;
    private TextView store_shop_detail_title;
    private View headView;
    private TextView shop_detail_name;
    private TextView shop_detail_price;
    private ImageView shop_detail_collect;
    private TextView shop_detail_store_describe;
    private LinearLayout store_star_grade_ll;
    private TextView shop_detail_store_type;
    private TextView shop_detail_store_per_capita;
    private TextView shop_detail_store_distance;
    private FrameLayout shop_detail_store_address_fl;
    private TextView shop_detail_store_address;
    private TextView shop_detail_store_time;
    private FrameLayout shop_detail_store_tel_fl;
    private TextView shop_detail_store_tel;
    private FrameLayout shop_detail_spec_fl;
    private TextView shop_detail_select_spec, shop_detail_consumer_tips, shop_detail_introduction;
    private ImageView store_shop_detail_bt_speak;
    private ImageView store_shop_detail_bt_panic_buying;
    private ImageView store_shop_detail_bt_tel;
    private SectionType sectionType;
    private Button btn_request_net;
    private View noNetView;
    private String shop_id, store_id, uid;
    private double log, lat;
    private WebView store_shop_detail_desc_wv;
    private LinearLayout store_shop_detail_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_detail);
        setBackBtn();
        initView();
        requestData();
        setListener();

    }

    private void requestData() {
        LoadDialog.show(this, true);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            NearbyStoreUtil.requestStoreShopDetail(this, shop_id, this);//shopid
        } else {
            LoadDialog.dismiss(this);
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    private String shopName;

    private void initView() {
        GBAccountInfo accountInfo = GBAccountMgr.getInstance().getAccountInfo();
        if (accountInfo != null) {
            WinguoAccountGeneral winguoGeneral = accountInfo.winguoGeneral;
            if (winguoGeneral != null) {
                uid = winguoGeneral.id;
            }
        }

        Intent intent = getIntent();
        if (sectionType == null)
            sectionType = new SectionType(new StoreShop.ResultBean());
        shop_id = intent.getStringExtra("shop_id");
        store_id = intent.getStringExtra("store_id");
        log = intent.getDoubleExtra("log", 0.00d);
        lat = intent.getDoubleExtra("lat", 0.00d);

        NestedScrollView store_shop_detail_scroll = (NestedScrollView) findViewById(R.id.store_shop_detail_scroll);
        store_shop_detail_content = (LinearLayout) findViewById(R.id.store_shop_detail_content);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.store_shop_detail_container);
        //无网络
        //(给容器添加布局)
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.alignWithParent = true;
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        noNetView.setVisibility(View.GONE);
        container.addView(noNetView, params);
        store_shop_detail_title = (TextView) findViewById(R.id.store_shop_detail_title);
        //店铺商品详情列表

        //headView = getLayoutInflater().inflate(R.layout.store_shop_detail_top, parent, false);
        store_shop_detail_banner = (BGABanner) findViewById(R.id.shop_detail_banner);
        shop_detail_name = (TextView) findViewById(R.id.shop_detail_describe);
        shop_detail_price = (TextView) findViewById(R.id.shop_detail_price);
        shop_detail_collect = (ImageView) findViewById(R.id.shop_detail_collect);
        shop_detail_store_describe = (TextView) findViewById(R.id.shop_detail_store_describe);
        store_star_grade_ll = (LinearLayout) findViewById(R.id.store_star_grade_ll);
        shop_detail_store_type = (TextView) findViewById(R.id.shop_detail_store_type);
        shop_detail_store_per_capita = (TextView) findViewById(R.id.shop_detail_store_per_capita);
        shop_detail_store_distance = (TextView) findViewById(R.id.shop_detail_store_distance);
        // --店家地址 营业时间 电话--
        shop_detail_store_address_fl = (FrameLayout) findViewById(R.id.shop_detail_store_address_fl);
        shop_detail_store_address = (TextView) findViewById(R.id.shop_detail_store_address);
        shop_detail_store_time = (TextView) findViewById(R.id.shop_detail_store_time);
        shop_detail_store_tel_fl = (FrameLayout) findViewById(R.id.shop_detail_store_tel_fl);
        shop_detail_store_tel = (TextView) findViewById(R.id.shop_detail_store_tel);
        shop_detail_spec_fl = (FrameLayout) findViewById(R.id.shop_detail_spec_fl);
        shop_detail_select_spec = (TextView) findViewById(R.id.shop_detail_select_spec);
        //  --商品介绍 消费提示--
        shop_detail_introduction = (TextView) findViewById(R.id.shop_detail_introduction);
        shop_detail_consumer_tips = (TextView) findViewById(R.id.shop_detail_consumer_tips);
        //商品描述
        store_shop_detail_desc_wv = new WebView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(ScreenUtil.dipToPx(this, 12), 0, ScreenUtil.dipToPx(this, 12), ScreenUtil.dipToPx(this, 12));
        store_shop_detail_desc_wv.setLayoutParams(lp);
        store_shop_detail_content.addView(store_shop_detail_desc_wv);
        initWeb();

        //底部按钮
        store_shop_detail_bt_speak = (ImageView) findViewById(R.id.store_shop_detail_bt_speak);
        store_shop_detail_bt_panic_buying = (ImageView) findViewById(R.id.store_shop_detail_bt_panic_buying);
        store_shop_detail_bt_tel = (ImageView) findViewById(R.id.store_shop_detail_bt_tel);


    }

    private void initWeb() {
        store_shop_detail_desc_wv.setScrollbarFadingEnabled(true);
        store_shop_detail_desc_wv.setClickable(true);
        store_shop_detail_desc_wv.setFocusable(true);
        store_shop_detail_desc_wv.setFocusableInTouchMode(true);
        store_shop_detail_desc_wv.setPersistentDrawingCache(0);
        WebSettings settings = store_shop_detail_desc_wv.getSettings();
        settings.setSupportZoom(true);
        //        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        int screenWidth = ScreenUtil.getScreenWidth(ShopDetailActivity.this);
        //有些webView不能自适应屏幕,进行按屏幕大小缩放
        if (screenWidth >= 1080) {
            store_shop_detail_desc_wv.setInitialScale(140);
        } else if (screenWidth >= 720) {
            store_shop_detail_desc_wv.setInitialScale(94);
        } else if (screenWidth >= 480) {
            store_shop_detail_desc_wv.setInitialScale(62);
        }
    }

    private void fillData(StoreDetail storeDetail) {
        StoreDetail.ResultBean resultBean = storeDetail.getResult().get(0);
        //设置星级
        // setStoreStar(Integer.valueOf("5"));
        setStoreDistance(resultBean.getDistance() + "");
        shop_detail_store_address.setText(resultBean.getM_maker_address_ch());
        shop_detail_store_time.setText(resultBean.getM_entity_maker_hour_begin() + "-" + resultBean.getM_entity_maker_hour_end());
        shop_detail_store_tel.setText(resultBean.getM_maker_mobile());
        shop_detail_store_describe.setText(resultBean.getM_maker_name_ch());
        shop_detail_store_type.setText(resultBean.getTradeName());
        shop_detail_store_per_capita.setText(resultBean.getM_entity_maker_average_consumption());

    }

    /**
     * 设置商品banner
     *
     * @param banners
     */
    private void setBanner(final List<String> banners) {
        store_shop_detail_banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, final int position) {
                GlideUtil.getInstance().loadImage(ShopDetailActivity.this, model, R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/5/18  轮播点击
                        //开启大图模式
                        Intent pictureActivity = new Intent(ShopDetailActivity.this, BigProductPictureActivity.class);
                        pictureActivity.putStringArrayListExtra("urls", (ArrayList<String>) banners);
                        pictureActivity.putExtra("position", position);
                        startActivity(pictureActivity);
                    }
                });
            }
        });
        store_shop_detail_banner.setData(banners, null);
        store_shop_detail_banner.setAutoPlayInterval(5000);
    }

    /**
     * 设置距离
     *
     * @param distance
     */
    private void setStoreDistance(String distance) {
        if (!TextUtils.isEmpty(distance) || distance != null) {
            double dis = Double.valueOf(distance);
            if (dis > 1000) {
                String format = new DecimalFormat("#.00").format(dis / 1000);
                shop_detail_store_distance.setText(format + "km");
            } else {
                shop_detail_store_distance.setText(dis + "m");
            }
        }

    }

    /**
     * 设置星级
     */
    private void setStoreStar(int starNumber) {
        int childCount = store_star_grade_ll.getChildCount();
        if (childCount > 5)
            return;
        for (int i = 0; i < starNumber; i++) {
            ImageView childAt = (ImageView) store_star_grade_ll.getChildAt(i);
            childAt.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        btn_request_net.setOnClickListener(this);
        store_shop_detail_bt_speak.setOnClickListener(this);
        store_shop_detail_bt_panic_buying.setOnClickListener(this);
        store_shop_detail_bt_tel.setOnClickListener(this);
        shop_detail_spec_fl.setOnClickListener(this);
        shop_detail_store_tel_fl.setOnClickListener(this);
        shop_detail_store_address_fl.setOnClickListener(this);
        shop_detail_collect.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_detail_collect:
                // TODO: 2017/6/16  商品关注or收藏
                ToastUtil.showToast(this, "商品关注or收藏");
                break;
            case R.id.shop_detail_store_address_fl:
                // TODO: 2017/6/16  跳转第三方地图（百度、高德）导航
                String address = shop_detail_store_address.getText().toString();
                OpenMapUtil.openMapByAddress(this, address);
                break;
            case R.id.shop_detail_store_tel_fl:
                callStoreTel();
                break;
            case R.id.shop_detail_spec_fl:
                // TODO: 2017/6/16  已选套餐
                if (uid != null) {

                    ShopSpecSelectDialog.Builder builder = new ShopSpecSelectDialog.Builder(this);
                    builder.setTitle(shopName).setSpecData(data)
                            .setSelectListener(new ShopSpecSelectDialog.OnSelectorSpecListener() {
                                @Override
                                public void selectPrice(final double price, final String spec, final String sku_id) {
                                    String m_maker_id = detail.getResult().get(0).getM_item_m_maker_id();
                                    String m_entity_item_m_item_id = detail.getResult().get(0).getM_entity_item_m_item_id();
                                    LoadDialog.show(ShopDetailActivity.this,true);
                                    NearbyStoreUtil.shopRequestAddCart(ShopDetailActivity.this, "1", m_entity_item_m_item_id, uid, sku_id, new NearbyStoreUtil.IShopRequestAddCart() {
                                        @Override
                                        public void addCart(String key, List<CartShopList.ContentBean.ItemBean> item) {
                                            LoadDialog.dismiss(ShopDetailActivity.this);
                                            ToastUtil.showToast(ShopDetailActivity.this, key);
                                            calcNumPricce(item);
                                            shop_detail_select_spec.setText(spec);
                                        }

                                        @Override
                                        public void addCartErrorMsg(String error) {
                                            LoadDialog.dismiss(ShopDetailActivity.this);
                                            ToastUtil.showToast(ShopDetailActivity.this, error);
                                        }
                                    }, m_maker_id);


                                }
                            });
                    builder.create().show();

                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.NEARBY_STORE_SHOP_SPEC);
                    it.putExtra("storeid", store_id);
                    it.putExtra("shopid", shop_id);
                    startActivity(it);
                }

                break;
            case R.id.store_shop_detail_bt_tel:
                callStoreTel();
                break;
            case R.id.store_shop_detail_bt_speak:
                launchSpeak();
                break;
            case R.id.store_shop_detail_bt_panic_buying:
                // TODO: 2017/6/16  抢购
                if (uid != null) {

                    ShopSpecSelectDialog.Builder builder1 = new ShopSpecSelectDialog.Builder(this);
                    builder1.setTitle(shopName).setSpecData(data)
                            .setSelectListener(new ShopSpecSelectDialog.OnSelectorSpecListener() {
                                @Override
                                public void selectPrice(final double price, final String spec, final String sku_id) {
                                    String m_maker_id = detail.getResult().get(0).getM_item_m_maker_id();
                                    String m_entity_item_m_item_id = detail.getResult().get(0).getM_entity_item_m_item_id();
                                    LoadDialog.show(ShopDetailActivity.this,true);
                                    NearbyStoreUtil.shopRequestAddCart(ShopDetailActivity.this, "1", m_entity_item_m_item_id, uid, sku_id, new NearbyStoreUtil.IShopRequestAddCart() {
                                        @Override
                                        public void addCart(String key, List<CartShopList.ContentBean.ItemBean> item) {
                                            LoadDialog.dismiss(ShopDetailActivity.this);
                                            ToastUtil.showToast(ShopDetailActivity.this, key);
                                            calcNumPricce(item);
                                            shop_detail_select_spec.setText(spec);
                                        }

                                        @Override
                                        public void addCartErrorMsg(String error) {
                                            LoadDialog.dismiss(ShopDetailActivity.this);
                                            ToastUtil.showToast(ShopDetailActivity.this, error);
                                        }
                                    }, m_maker_id);


                                }
                            });
                    builder1.create().show();

                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.NEARBY_STORE_SHOP_SPEC);
                    it.putExtra("storeid", store_id);
                    it.putExtra("shopid", shop_id);
                    startActivity(it);
                }

                break;
            case R.id.btn_request_net:
                // TODO: 2017/6/16  无网络
                noNetView.setVisibility(View.GONE);
                requestData();
                break;

        }
    }

    private int total_num;
    private double total_price;
    private String cartids = "";

    /**
     * 计算数量和总价
     */
    private void calcNumPricce(List<CartShopList.ContentBean.ItemBean> item) {
        total_price = 0.00;
        total_num = 0;
        cartids = "";
        if (!item.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < item.size(); i++) {
                CartShopList.ContentBean.ItemBean itemBean = item.get(i);
                if (i == (item.size() - 1)) {
                    sb.append(itemBean.getId());
                } else {
                    sb.append(itemBean.getId()).append(",");
                }
                cartids = sb.toString();
                total_num += Integer.valueOf(itemBean.getNum());
                total_price = new BigDecimal(total_price).add(new BigDecimal(itemBean.getNum_price())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (total_num != 0) {
            Intent it = new Intent();
            it.putExtra("total_num", total_num);
            it.putExtra("total_price", total_price);
            it.putExtra("cartids", cartids);
            setResult(RESULT_OK, it);
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        if (total_num != 0) {
            Intent it = new Intent();
            it.putExtra("total_num", total_num);
            it.putExtra("total_price", total_price);
            it.putExtra("cartids", cartids);
            setResult(RESULT_OK, it);
        }
        super.finish();
    }

    private void callStoreTel() {
        String tel = shop_detail_store_tel.getText().toString();
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Constants.REQUEST_CODE_PERMISSIONS_TEL == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String tel = shop_detail_store_tel.getText().toString();
                Intent it = new Intent();
                it.setAction(Intent.ACTION_CALL);
                it.setData(Uri.parse("tel:" + tel));
                startActivity(it);
            } else {
                Intents.noPermissionStatus(this, "电话权限需要打开");
            }
        }
        if (Constants.REQUEST_CODE_PERMISSIONS_VOICE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchSpeak();
            } else {
                Intents.noPermissionStatus(this, "录音权限需要打开");
            }
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

    private StoreShopDetail detail;

    @Override
    public void storeShopDetail(StoreShopDetail storeShopDetail) {
        // TODO: 2017/6/22 请求本商品的 实体店数据
        if (storeShopDetail != null) {
            detail = storeShopDetail;
            NearbyStoreUtil.requestStoreDetail(ShopDetailActivity.this, store_id, log, lat, this);
            initShopDetail(storeShopDetail);
        } else {
            LoadDialog.dismiss(this);
            ToastUtil.showToast(this, "找不到该商品");
            finish();
        }
    }

    private List<StoreShop.ResultBean.SkuBean> data = new ArrayList<>();

    private void initShopDetail(StoreShopDetail storeShopDetail) {
        StoreShopDetail.ResultBean resultBean = storeShopDetail.getResult().get(0);
        List<String> itemImage = resultBean.getItemImage();
        setBanner(itemImage);
        shopName = resultBean.getM_item_name();
        shop_detail_name.setText(shopName);
        if (resultBean.getM_item_max_price().equals(resultBean.getM_item_min_price())) {
            shop_detail_price.setText(resultBean.getM_item_min_price());
        } else {
            shop_detail_price.setText(resultBean.getM_item_min_price() + " - " + resultBean.getM_item_max_price());
        }
        data.addAll(resultBean.getItemSku());
        if (TextUtils.isEmpty(resultBean.getM_entity_item_note())) {
            shop_detail_introduction.setText("暂无\n");
        } else {
            shop_detail_introduction.setText(resultBean.getM_entity_item_note() + "\n");
        }
        //消费提示
        StringBuffer consumer = new StringBuffer();
        if (!TextUtils.isEmpty(resultBean.getM_entity_item_start_time())) {
            if (!"0000-00-00".equals(resultBean.getM_entity_item_start_time())) {
                if ("0".equals(resultBean.getM_entity_item_is_long())) {
                    consumer.append("有效期：").append(resultBean.getM_entity_item_start_time()).append("\t至\t").append(resultBean.getM_entity_item_end_time() + "\n");
                } else if ("1".equals(resultBean.getM_entity_item_is_long())) {
                    consumer.append("有效期：").append(resultBean.getM_entity_item_start_time()).append("\t至\t").append(" 长期\n");
                }
            }
        }

        if (!TextUtils.isEmpty(resultBean.getM_entity_item_no_use_date())) {
            consumer.append("不可使用时间：").append(resultBean.getM_entity_item_no_use_date() + "\n");
        }
        if (!TextUtils.isEmpty(resultBean.getM_entity_item_use_stime())) {
            if (!"00:00:00".equals(resultBean.getM_entity_item_use_stime())) {
                consumer.append("使用时间：").append(resultBean.getM_entity_item_use_stime() + "\t至\t" + resultBean.getM_entity_item_use_etime() + "\n");
            }
        }
        switch (resultBean.getM_entity_item_meet()) {
            case "1":
                consumer.append("预约提醒：").append("无需预约\n");
                break;
            case "2":
                consumer.append("预约提醒：").append("无需预约，消费高峰时可能需要等位\n");
                break;
            case "3":
                consumer.append("预约提醒：").append("需提前一小时预约\n");
                break;
            case "4":
                consumer.append("预约提醒：").append("需提前一天预约\n");
                break;
            case "5":
                consumer.append("预约提醒：").append("需提前3天预约\n");
                break;
            case "6":
                consumer.append("预约提醒：").append("需提前一周预约\n");
                break;
        }
        //店铺 服务
        StringBuffer shopServer = new StringBuffer();
        if (!TextUtils.isEmpty(resultBean.getM_entity_item_shop_server())) {
            String[] split = resultBean.getM_entity_item_shop_server().split(",");
            for (int i = 0; i < split.length; i++) {
                switch (split[i]) {
                    case "0":
                        shopServer.append("提供免费Wifi\t");
                        break;
                    case "1":
                        shopServer.append("停车场\t");
                        break;
                    case "2":
                        shopServer.append("空调\t");
                        break;
                    case "3":
                        shopServer.append("提供保险箱\t");
                        break;
                }
            }
        }
        if (!TextUtils.isEmpty(shopServer.toString()))
            consumer.append("店铺服务：").append(shopServer.toString() + "\n");

        if (TextUtils.isEmpty(consumer.toString()))
            shop_detail_consumer_tips.setText("暂无\n");
        else
            shop_detail_consumer_tips.setText(consumer.toString());

        if (store_shop_detail_desc_wv!=null)
            store_shop_detail_desc_wv.loadDataWithBaseURL(null, getNewContent(resultBean.getM_item_description()), "text/html", "utf-8", null);
        //底部商品详情
        // List<String> detail =   new ArrayList<String>();
        //detail.add(itemImage.get(0));
        //ShopDetailAdapter adapter = new ShopDetailAdapter(R.layout.store_shop_detail_item,detail);
        //store_shop_detail_rv.setAdapter(adapter);
        //adapter.addHeaderView(headView);


    }

    //设置HTML图片可以适应屏幕
    public String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();

    }

    @Override
    public void storeShopDetailErrorMsg(String error) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(this, getString(R.string.no_net_or_service_no));
    }

    @Override
    public void storeDetail(StoreDetail storeDetail) {
        LoadDialog.dismiss(this);
        if (storeDetail != null) {
            fillData(storeDetail);
            store_shop_detail_content.setVisibility(View.VISIBLE);
            //store_shop_detail_rv.setVisibility(View.VISIBLE);
        } else {
            ToastUtil.showToast(this, "没有找到该店铺");
            finish();
        }
    }

    @Override
    public void storeDetailErrorMsg(String error) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.VISIBLE);
        ToastUtil.showToast(this, getString(R.string.no_net_or_service_no));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        store_shop_detail_content.removeView(store_shop_detail_desc_wv);
        store_shop_detail_desc_wv.destroy();
        store_shop_detail_desc_wv = null;
    }
}
