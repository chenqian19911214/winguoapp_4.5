package com.winguo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseTitleActivity;
import com.winguo.cart.bean.AddCartCodeBean;
import com.winguo.cart.controller.store.CartController;
import com.winguo.cart.view.store.IAddCartView;
import com.winguo.login.LoginActivity;
import com.winguo.mine.collect.CollectHandler;
import com.winguo.mine.collect.bean.AddCollectBean;
import com.winguo.mine.collect.bean.DeleteCollectBean;
import com.winguo.mine.order.list.IPopWindowProtocal;
import com.winguo.net.GlideUtil;
import com.winguo.product.controller.ProductController;
import com.winguo.product.controller.ProductHandle;
import com.winguo.product.modle.Data;
import com.winguo.product.modle.EvaluateItemBean;
import com.winguo.product.modle.bean.GoodDetail;
import com.winguo.product.modle.Item;
import com.winguo.product.modle.bean.ItemSkuBean;
import com.winguo.product.modle.bean.ProductEntity;
import com.winguo.product.modle.ProductEvaluateAllBean;
import com.winguo.product.modle.bean.ShopSimpleBean;
import com.winguo.product.modle.TitleBean;
import com.winguo.product.modle.bean.CollectBean;
import com.winguo.product.view.CustomWebView;
import com.winguo.product.view.IProductView;
import com.winguo.product.view.PullUpToLoadMoreView;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/12/16.
 */

public class ProductActivity extends BaseTitleActivity implements IProductView, IAddCartView, View.OnClickListener {
    //商品属性页面返回数据的请求码
    public static final int PROPERTY_REQUEST_CODE = 88;
    private boolean IS_FROM_PROPERTY = true;
    private ProductEntity productEntity;
    private GoodDetail goodDetail;
    private ShopSimpleBean shopSimpleBean;
    private TextView product_detail_title;
    private FrameLayout iv_product_share;
    private ViewPager vp_product_detail;
    private TextView tv_product_name;
    private TextView tv_product_price;
    private TextView tv_product_original_price;
    private TextView tv_product_location;
    private RelativeLayout rl_price;
    private TextView tv_product_freight;
    private TextView tv_product_month_sales_volume;
    private TextView tv_product_grade;
    private TextView product_cash_coupon_tv;
    private TextView tv_product_sales;
    private LinearLayout ll_product_sales;
    private TextView tv_product_discount;
    private LinearLayout ll_product_discount;
    private TextView tv_product_free_postage;
    private LinearLayout ll_product_free_postage;
    private TextView tv_evaluate_count;
    private TextView tv_product_property;
    private ImageView iv_product_property;
    private ImageView iv_shop_logo;
    private TextView tv_shop_name;
    private TextView tv_shop_product_count;
    private TextView tv_shop_collect_count;
    private TextView join_shop_btn;
    private TextView buy_now_btn;
    private LinearLayout rl_product_evaluate_count;
    private RelativeLayout rl_product_property;
    private PullUpToLoadMoreView pull_up_to_load_more;
    private LinearLayout ll_request;
    private Button btn_request_net;
    private int gid;
    private LinearLayout ll_point_container;
    //放置图片URL的集合
    private ArrayList<String> pictureUrls = new ArrayList<>();
    //图片的位置
    private int lastPosition = 0;
    private CustomWebView product_web;
    private boolean isHavedProperty = false;
    private String sku_id;
    private String count;
    private int is_prompt = 0;//是否立即购买,0.否，1.是
    private LinearLayout ll_collect;
    private ImageView iv_collect;
    private int mIs_collect;
    private ProductController controller;
    private FrameLayout fl_webview_container;
    private ImageView iv_product_detail_shoppping_cart;
    private RelativeLayout rl_no_product_evaluate;
    private RelativeLayout ll_product_evaluate_user_name;
    private TextView tv_product_evaluate_user_name;
    private TextView tv_evaluate_time;
    private TextView tv_evaluate_content;
    private ProductEvaluateAllBean productEvaluateAllBean;
    private RelativeLayout shop_layout;
    private ImageView product_share_image;
    private boolean isFromCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_product);
        setBackBtn();
        initViews();
        initListener();
    }

    private void initViews() {
        Intent intent = getIntent();
        gid = intent.getIntExtra("gid", 0);
        isFromCart = intent.getBooleanExtra("isFromCart", false);
        //初始化控件
        initView();
        //判断是否有网络
        showNetState();
    }

    @Override
    protected void backEvent() {
        super.backEvent();
        //从商品返回的话就刷新数据
        if (isFromCart) {
            Intent receiverIntent = new Intent();
            receiverIntent.setAction("com.winguo.confirmpay.success");
            sendBroadcast(receiverIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFromCart) {
            Intent receiverIntent = new Intent();
            receiverIntent.setAction("com.winguo.confirmpay.success");
            sendBroadcast(receiverIntent);
        }
    }

    /**
     * 根据网络状态显示界面
     */
    private void showNetState() {
        //切换显示状态
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            ll_request.setVisibility(View.GONE);
            pull_up_to_load_more.setVisibility(View.VISIBLE);
            //网络请求拿数据
            requestNetData(gid);
            //恢复
            join_shop_btn.setEnabled(true);
            buy_now_btn.setEnabled(true);
            iv_collect.setEnabled(true);
            iv_product_detail_shoppping_cart.setEnabled(true);
        } else {
            //如果网络和WiFi都没有，就加载无网络
            ll_request.setVisibility(View.VISIBLE);
            pull_up_to_load_more.setVisibility(View.GONE);
            //设置一些按钮不可点击
            //加入购物车不可用
            join_shop_btn.setEnabled(false);
            //立即购买不可用
            buy_now_btn.setEnabled(false);
            //收藏不可用
            iv_collect.setEnabled(false);
            iv_product_detail_shoppping_cart.setEnabled(false);
        }
    }

    /**
     * 请求网络数据
     *
     * @param gid 请求商品信息的商品id
     */
    private void requestNetData(int gid) {
        LoadDialog.show(ProductActivity.this);
        controller = new ProductController(this);
        if (SPUtils.contains(ProductActivity.this, "accountName")) {
            //登录过
            controller.getCollectBeanData(ProductActivity.this, gid);
            //添加浏览记录
            controller.addHistoryLog(this, gid);
        } else {
           // controller.getGoodsData(ProductActivity.this, gid);
            controller.getGoodsDetail(ProductActivity.this, gid);
        }
    }

    //返回商品详情的信息
    @Override
    public void obtainProductData(ProductEntity productEntity) {
        if (productEntity == null) {
            LoadDialog.dismiss(ProductActivity.this);
            //如果网络和WiFi都没有，就加载无网络
            ll_request.setVisibility(View.VISIBLE);
            pull_up_to_load_more.setVisibility(View.GONE);
            return;
        }
        this.productEntity = productEntity;
        //展示数据到控件上
        //切换显示状态
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            ll_request.setVisibility(View.GONE);
            pull_up_to_load_more.setVisibility(View.VISIBLE);
            //网络请求拿数据
            if (productEntity.product != null && controller != null) {
                String shop_id = productEntity.product.shop_id;
                controller.getShopData(shop_id);
            }
            //恢复
            join_shop_btn.setEnabled(true);
            buy_now_btn.setEnabled(true);
            iv_collect.setEnabled(true);
            iv_product_detail_shoppping_cart.setEnabled(true);
        } else {
            //如果网络和WiFi都没有，就加载无网络
            ll_request.setVisibility(View.VISIBLE);
            pull_up_to_load_more.setVisibility(View.GONE);
            //设置一些按钮不可点击
            //加入购物车不可用
            join_shop_btn.setEnabled(false);
            //立即购买不可用
            buy_now_btn.setEnabled(false);
            //收藏不可用
            iv_collect.setEnabled(false);
            iv_product_detail_shoppping_cart.setEnabled(false);
        }
    }

    /**
     * 新商品详情数据
     * @param goodDetail
     */
    @Override
    public void obtainProductDetail(GoodDetail goodDetail) {
        Log.e("detail==","result:::::"+goodDetail);
        if (goodDetail == null) {
            LoadDialog.dismiss(ProductActivity.this);
            //如果网络和WiFi都没有，就加载无网络
            ll_request.setVisibility(View.VISIBLE);
            pull_up_to_load_more.setVisibility(View.GONE);
            return;
        }
        if (goodDetail.getCode() == 1) {
            ToastUtil.showToast(ProductActivity.this,goodDetail.getMsg());
            finish();
            return;
        }
        this.goodDetail = goodDetail;
        //展示数据到控件上
        //切换显示状态
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            ll_request.setVisibility(View.GONE);
            pull_up_to_load_more.setVisibility(View.VISIBLE);
            //网络请求拿数据
            GoodDetail.ProductBean product = goodDetail.getProduct();
            if (product != null && controller != null) {
                String shop_id = product.getShop_id();
                controller.getShopData(shop_id);
            }
            //恢复
            join_shop_btn.setEnabled(true);
            buy_now_btn.setEnabled(true);
            iv_collect.setEnabled(true);
            iv_product_detail_shoppping_cart.setEnabled(true);
        } else {
            //如果网络和WiFi都没有，就加载无网络
            ll_request.setVisibility(View.VISIBLE);
            pull_up_to_load_more.setVisibility(View.GONE);
            //设置一些按钮不可点击
            //加入购物车不可用
            join_shop_btn.setEnabled(false);
            //立即购买不可用
            buy_now_btn.setEnabled(false);
            //收藏不可用
            iv_collect.setEnabled(false);
            iv_product_detail_shoppping_cart.setEnabled(false);
        }
    }

    //返回店铺信息
    @Override
    public void obtainShopData(ShopSimpleBean shopSimpleBean) {
        Log.e("shopData::",""+shopSimpleBean);
        if (shopSimpleBean == null) {
            //如果网络和WiFi都没有，就加载无网络
            LoadDialog.dismiss(ProductActivity.this);
            ll_request.setVisibility(View.VISIBLE);
            pull_up_to_load_more.setVisibility(View.GONE);
            return;
        }
        this.shopSimpleBean = shopSimpleBean;
        if (NetWorkUtil.noHaveNet(ProductActivity.this)) {
            LoadDialog.dismiss(ProductActivity.this);
            return;
        }
        ProductHandle.getEvaluate(handler, gid, StartApp.limit, 1);
        //        LoadDialog.dismiss(ProductActivity.this);

        //展示数据到控件上
        showProductData();
        showShopData();
    }

    /**
     * 填充商品详情
     */
    @SuppressLint("StringFormatMatches")
    private void showProductData() {
        product_share_image.setImageResource(R.drawable.share_icon);
        //上面的商品图片
        GoodDetail.ProductBean product = goodDetail.getProduct();
        if (product.getItem_images() != null) {
            List<GoodDetail.ProductBean.ItemImagesBean> item_images = product.getItem_images();
            //获取图片的集合
            //添加之前先清除
            pictureUrls.clear();
            ll_point_container.removeAllViews();
            for (int i = 0; i < item_images.size(); i++) {
                pictureUrls.add(item_images.get(i).getData().getUrl());
                //添加图片指示器
                ImageView point = new ImageView(ProductActivity.this);
                point.setImageResource(R.drawable.product_point_bg);
                //设置point的间距
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
                params.leftMargin = 8;
                ll_point_container.addView(point, params);
            }
            ll_point_container.getChildAt(0).setSelected(true);

            ProductPagerAdapter adapter = new ProductPagerAdapter();
            vp_product_detail.setAdapter(adapter);
            vp_product_detail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                    ll_point_container.getChildAt(position).setSelected(true);
                    ll_point_container.getChildAt(lastPosition).setSelected(false);
                    lastPosition = position;
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
        //商品信息赋值
        //商品名称
        tv_product_name.setText(product.getName());
        //商品价格
        tv_product_price.setText(String.format(getString(R.string.product_price), String.valueOf(product.getOriginal_min_price())));
       /*
        if (product.getSpecial_price().length() != 0) {
            //有最高价格和最低价格
            tv_product_price.setText(String.format(getString(R.string.product_price), product.getSpecial_price() + "-" + product.getRegular_price()));
        } else {

        }*/
        //--------暂时没有设置原始价格的划去--------
        //发货地
        tv_product_location.setText(product.getProvincename() +product.getCityname());
        //快递费用
        if ("0".equals(product.getFare_type())) {
            tv_product_freight.setText(getString(R.string.product_freight_text));
        } else if ("1".equals(product.getFare_type())) {
            tv_product_freight.setText(String.format(getString(R.string.freight_text), getString(R.string.freight)));
        }else if ("".equals(product.getFare_type())){
            tv_product_freight.setText(getString(R.string.product_freight_text));
        }
        //月销量
        tv_product_month_sales_volume.setText(getString(R.string.tv_product_month_sales, product.getSale_qty()));
        //评分
        tv_product_grade.setText("评分: "+product.getRating_summary());
        //  TODO -------活动和评论暂时不设置--------
        //商品评价
        tv_evaluate_count.setText(getString(R.string.tv_evaluate_count, Integer.valueOf(product.getReviews_count())));
        if (!TextUtils.isEmpty(product.getItem_sku().get(1).getData().getM_sku_cash_coupon_used())) {
            //该商品 优惠券可使用数量（默认第一种规格的优惠使用数量）
            GoodDetail.ProductBean.ItemSkuBean.DataBean data = product.getItem_sku().get(1).getData();

            String colorStr = data.getM_sku_color_name();
            String sizeStr = data.getM_sku_size();
            String sku = "";
            if (!TextUtils.isEmpty(colorStr) && !TextUtils.isEmpty(sizeStr)) {
                sku = "\"" + colorStr + "\"," + "\"" + sizeStr + "\"," + "\"" + 1 + "件\"";
            }
            if (!TextUtils.isEmpty(colorStr) && TextUtils.isEmpty(sizeStr)) {
                sku = "\"" + colorStr + "\"," + "\"" + 1 + "件\"";
            }
            if (TextUtils.isEmpty(colorStr) && !TextUtils.isEmpty(sizeStr)) {
                sku = "\"" + sizeStr + "\"," + "\"" + 1 + "件\"";
            }
            if (TextUtils.isEmpty(colorStr) && TextUtils.isEmpty(sizeStr)) {
                sku = "\"" + 1 + "件\"";
            }
            isHavedProperty = true;

            count = "1";
            //显示返回的数据到控件上
            product_cash_coupon_tv.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), data.getM_sku_cash_coupon_used()));
            sku_id = data.getM_sku_id();
            tv_product_property.setText(getString(R.string.tv_product_property, sku));
            tv_product_property.setTextColor(getResources().getColor(R.color.property_default_color));

        } else {
            product_cash_coupon_tv.setText(String.format(getResources().getString(R.string.product_cash_coupon_tv), "0.00"));
        }
        //第二页WebView
        WebSettings settings = product_web.getSettings();
        settings.setSupportZoom(true);
        //        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        int screenWidth = ScreenUtil.getScreenWidth(ProductActivity.this);
        //有些webView不能自适应屏幕,进行按屏幕大小缩放
        if (screenWidth >= 1080) {
            product_web.setInitialScale(140);
        } else if (screenWidth >= 720) {
            product_web.setInitialScale(94);
        } else if (screenWidth >= 480) {
            product_web.setInitialScale(62);
        }
        String newContent = getNewContent(product.getDescription());
        Log.e("newContent product:",""+product.getDescription());
        Log.e("newContent:======",""+newContent);
        product_web.loadDataWithBaseURL(null, getNewContent(product.getDescription()),
                "text/html", "utf-8", null);
    }

    /**
     * 设置是否收藏的UI
     *
     * @param rid
     */
    private void setCollectUI(int rid) {
        iv_collect.setImageResource(rid);
    }

    /**
     * 设置收藏
     *
     * @param is_collect
     */
    private void setCollect(int is_collect) {
        if (is_collect == 0) {
            //没有收藏
            setCollectUI(R.drawable.is_no_collect);
        }
        if (is_collect == 1) {
            //已收藏
            setCollectUI(R.drawable.is_collect);
        }
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
    public void getCollectData(CollectBean collectBean) {
        //初始化是否有收藏
        if (collectBean != null) {
            if (collectBean.message.code == 0) {
                mIs_collect = collectBean.message.flag;
            }
        }
        setCollect(mIs_collect);
        if (NetWorkUtil.noHaveNet(ProductActivity.this)) {
            LoadDialog.dismiss(ProductActivity.this);
            return;
        }
        if (controller != null) {
          //  controller.getGoodsData(ProductActivity.this, gid);
            controller.getGoodsDetail(ProductActivity.this, gid);
        }
    }

    private void showShopData() {
        //店铺信息
        //店铺logo
        if (shopSimpleBean.message.logo.content.length() == 0) {
            //没有上传logo,使用默认的
            iv_shop_logo.setImageResource(R.drawable.shop_default_bg);
        } else {
            GlideUtil.getInstance().loadImage(ProductActivity.this, shopSimpleBean.message.logo.content, R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, iv_shop_logo);
        }
        //店铺名
        tv_shop_name.setText(shopSimpleBean.message.name);
        //店铺商品数
        tv_shop_product_count.setText(getString(R.string.tv_shop_product_count, shopSimpleBean.message.itemCount));
        //店铺收藏数
        tv_shop_collect_count.setText(String.valueOf(shopSimpleBean.message.brandCount));
    }

    private void initListener() {
        //跳转到购物车
        iv_product_detail_shoppping_cart.setOnClickListener(this);
        //顶部分享按钮
        iv_product_share.setOnClickListener(this);
        //加入购物车
        join_shop_btn.setOnClickListener(this);
        //立即购买
        buy_now_btn.setOnClickListener(this);
        rl_product_property.setOnClickListener(this);
        //收藏
        ll_collect.setOnClickListener(this);
        //评论
        rl_product_evaluate_count.setOnClickListener(this);
        //无网络点击重加载
        btn_request_net.setOnClickListener(this);
        //店铺
        shop_layout.setOnClickListener(this);
    }

 private Handler handler=new Handler(){
     @Override
     public void handleMessage(Message msg) {
         super.handleMessage(msg);
         LoadDialog.dismiss(ProductActivity.this);
         switch (msg.what) {
             //商品评论
             case RequestCodeConstant.REQUEST_PRODUCT_COMMENT:
                 productEvaluateAllBean = (ProductEvaluateAllBean) msg.obj;
                 if (productEvaluateAllBean == null) {
                     LoadDialog.dismiss(ProductActivity.this);
                     ll_request.setVisibility(View.VISIBLE);
                     pull_up_to_load_more.setVisibility(View.GONE);
                     return;
                 }
                 //没有数据
                 if (!productEvaluateAllBean.isHasData()) {
                     rl_no_product_evaluate.setVisibility(View.VISIBLE);
                     ll_product_evaluate_user_name.setVisibility(View.GONE);
                     tv_evaluate_content.setVisibility(View.GONE);
                 } else {
                     rl_no_product_evaluate.setVisibility(View.GONE);
                     ll_product_evaluate_user_name.setVisibility(View.VISIBLE);
                     tv_evaluate_content.setVisibility(View.VISIBLE);
                     EvaluateItemBean evaluateItemBean = null;
                     //有数据
                     if (productEvaluateAllBean.isObject()) {
                         evaluateItemBean = productEvaluateAllBean.getEvaluateObjectBean().getReviewList().getItems().getItem();
                     } else {
                         evaluateItemBean = productEvaluateAllBean.getEvaluateArrBean().getReviewList().getItems().getItem().get(0);
                     }
                     if (TextUtils.isEmpty(evaluateItemBean.getNickname())) {
                         tv_product_evaluate_user_name.setText("匿名");
                     } else {
                         tv_product_evaluate_user_name.setText(evaluateItemBean.getNickname());
                     }
                     tv_evaluate_time.setText(evaluateItemBean.getCreated_at());
                     if (TextUtils.isEmpty(evaluateItemBean.getDetail())) {
                         //买家评论内容为空时,后台默认默认为"很好";
                         tv_evaluate_content.setText("很好，很满意!");
                     } else {
                         tv_evaluate_content.setText(evaluateItemBean.getDetail());
                     }
                 }
                 break;
             //添加商品收藏
             case RequestCodeConstant.REQUEST_ADDPRODUCTCOLLECT:
                 AddCollectBean addCollectBean = (AddCollectBean) msg.obj;
                 if (addCollectBean == null) {
                     ToastUtil.show(ProductActivity.this, getString(R.string.timeout));
                     return;
                 }
                 if (addCollectBean.getMessage().getCode() == 1) {
                     ToastUtil.show(ProductActivity.this, getString(R.string.goods_collect_success_text));
                     mIs_collect = 1;
                     setCollect(mIs_collect);
                 } else {
                     ToastUtil.show(ProductActivity.this, addCollectBean.getMessage().getText());
                     mIs_collect = 0;
                 }
                 break;
             //删除商品收藏
             case RequestCodeConstant.REQUEST_DELETE_GOODS_COLLECT:
                 DeleteCollectBean deleteCollectBean = (DeleteCollectBean) msg.obj;
                 if (deleteCollectBean == null) {
                     ToastUtil.show(ProductActivity.this, getString(R.string.timeout));
                     return;
                 }
                 if (deleteCollectBean.getMessage().getCode() == 1) {
                     ToastUtil.show(ProductActivity.this, getString(R.string.goods_no_collect_success_text));
                     mIs_collect = 0;
                     setCollect(mIs_collect);
                 } else {
                     mIs_collect = 1;
                     ToastUtil.show(ProductActivity.this, deleteCollectBean.getMessage().getText());
                 }
                 break;
         }
         ll_collect.setClickable(true);
     }
 };

    //添加购物车返回的数据
    @Override
    public void showAddCartCode(AddCartCodeBean addCartCodeBean) {
        LoadDialog.dismiss(ProductActivity.this);
        //'status'=>'success','text'=>'放入购物车成功。' ,’code’ => 0
        //  'status'=>'error','text'=>'放入购物车失败。’,’code’=>-1
        //'status'=>'error','text'=>'该规格的商品已经下架。’,’code’=>-2
        int code = addCartCodeBean.message.code;
        if (code == RequestCodeConstant.CART_REQUEST_SUCCESS_CODE) {
            if (is_prompt == 0) {
                //加入购物车
                ToastUtil.showToast(ProductActivity.this, addCartCodeBean.message.text);
            } else {
                //立即购买,跳转到确定订单页面
                Intent payIntent = new Intent(ProductActivity.this, PayActivity.class);
                payIntent.putExtra("sku_ids", String.valueOf(sku_id));
                payIntent.putExtra("amount", count);
                payIntent.putExtra("is_prompt", is_prompt);//代表的是不是立即购买
                startActivity(payIntent);
            }
        } else if (code == RequestCodeConstant.CART_REQUEST_ERROR_SOLD_OUT_CODE) {
            ToastUtil.showToast(ProductActivity.this, addCartCodeBean.message.text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.join_shop_btn:
                addCart();
                break;
            case R.id.buy_now_btn:
                buyNowGoods();
                break;
            case R.id.btn_request_net:
                showNetState();
                break;
            case R.id.shop_layout:
                if (goodDetail.getProduct()!=null) {
                    Intent it = new Intent(ProductActivity.this, GoodsShopActivity.class);
                    it.putExtra("shopId",goodDetail.getProduct().getShop_id() );
                    it.putExtra("shopBean",shopSimpleBean);
                    startActivity(it);
                }
                break;
            case R.id.rl_product_property:
                IS_FROM_PROPERTY = true;
                //设置商品属性 ,开启一个商品属性的页面
                Intent propertyActivity = new Intent(ProductActivity.this, ProductPropertyActivity.class);
                //把要用的数据传递过去,(这里可能会出问题)
                CommonUtil.printI("传递的商品属性数据:=", goodDetail.getProduct().getItem_sku().toString());
                ItemSkuBean itemSkuBean = new ItemSkuBean();
                GoodDetail.ProductBean product = goodDetail.getProduct();
                cleanData(itemSkuBean, product);
                propertyActivity.putExtra("product",product);
                propertyActivity.putExtra("itemsku", itemSkuBean);
                propertyActivity.putExtra("isFromProperty", IS_FROM_PROPERTY);
                startActivityForResult(propertyActivity, PROPERTY_REQUEST_CODE);
                break;
            //点击分享
            case R.id.iv_product_share:
                showShare();
                //                new SharePopWindow().onShow(iv_product_share);
                break;
            case R.id.iv_product_detail_shoppping_cart:
                if (SPUtils.contains(ProductActivity.this, "accountName")) {
                    Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    jumpLogin();
                }
                break;
            //添加商品收藏
            case R.id.ll_collect:
                if (SPUtils.contains(this, "accountName")) {
                    ll_collect.setClickable(false);
                    LoadDialog.show(this);
                    //取消商品收藏
                    if (mIs_collect == 1) {
                        if (NetWorkUtil.isNetworkAvailable(this)) {
                            CollectHandler.deleteGoodsCollect(this, handler, gid);
                        } else {
                            LoadDialog.dismiss(ProductActivity.this);
                            ToastUtil.show(this, getString(R.string.timeout));
                        }
                    } else {
                        //添加商品收藏
                        if (NetWorkUtil.isNetworkAvailable(this)) {
                            CollectHandler.addProductCollect(this, handler, gid);
                        } else {
                            LoadDialog.dismiss(ProductActivity.this);
                            ToastUtil.show(this, getString(R.string.timeout));
                        }
                    }
                } else {
                    //没有登录
                    jumpLogin();
                }
                break;
            //点击跳转到评论界面
            case R.id.rl_product_evaluate_count:
                Intent evaluateIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("productEvaluateAllBean", productEvaluateAllBean);
                evaluateIntent.putExtras(bundle);
                evaluateIntent.putExtra(ActionUtil.ACTION_PRODUCT_EVALUATE, gid);
                evaluateIntent.setClass(ProductActivity.this, ProductEvaluateActivity.class);
                startActivity(evaluateIntent);
                break;
        }
    }

    /**
     * 整理商品规格数据
     * @param itemSkuBean
     * @param product
     */
    private void cleanData(ItemSkuBean itemSkuBean, GoodDetail.ProductBean product) {
        TitleBean titleBean = new TitleBean();
        GoodDetail.ProductBean.ItemSkuBean.TitleBean title = product.getItem_sku().get(0).getTitle();
        titleBean.id = title.getM_sku_id();
        titleBean.color_name =  title.getM_sku_color_name();
        List<String> names = new ArrayList<>();
        names.add(title.getM_sku_id());
        names.add(title.getM_sku_color());
        names.add(title.getM_color_picture());
        names.add(title.getM_sku_cash_coupon_used());
        names.add(title.getM_sku_price());
        names.add(title.getM_sku_size());
        names.add(title.getM_sku_stock());
        names.add(title.getM_sku_color_name());
        names.add(title.getM_sku_stock_last());
        names.add(title.getM_sku_weight());
        names.add(title.getOriginal_price());
        titleBean.name = names;
        Item item = new Item();
        List<Data> itemData = new ArrayList<>();
        for (int i = 1; i < product.getItem_sku().size(); i++) {
            GoodDetail.ProductBean.ItemSkuBean.DataBean dataBean = product.getItem_sku().get(i).getData();
            Data data = new Data();
            data.original_price = Double.valueOf(dataBean.getOriginal_price());
            List<String> values = new ArrayList<>();
            values.add(dataBean.getM_sku_id());
            values.add(dataBean.getM_sku_color());
            values.add(dataBean.getM_color_picture());
            values.add(dataBean.getM_sku_size());
            values.add(dataBean.getM_sku_price());
            values.add(dataBean.getM_sku_cash_coupon_used());
            values.add(dataBean.getM_sku_stock());
            values.add(dataBean.getM_sku_color_name());
            values.add(dataBean.getM_sku_stock_last());
            values.add(dataBean.getM_sku_weight());
            values.add(dataBean.getOriginal_price());
            data.value = values;
            itemData.add(data);
        }
        item.data = itemData;
        itemSkuBean.item = item;
        itemSkuBean.title = titleBean;
    }

    /**
     * 跳转到登录界面
     */
    private void jumpLogin() {
        ToastUtil.showToast(ProductActivity.this, getString(R.string.no_login_text));
        Intent loginActivity = new Intent(ProductActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }

    /**
     * 立即购买
     */
    private void buyNowGoods() {
        if (NetWorkUtil.noHaveNet(ProductActivity.this)) {
            return;
        }
        IS_FROM_PROPERTY = false;
        is_prompt = 1;
        if (SPUtils.contains(ProductActivity.this, "accountName")) {
            if (isHavedProperty) {
                Log.e("testdun:",""+sku_id+"::"+count+"::"+is_prompt);
                LoadDialog.show(ProductActivity.this);
                CartController addCartController = new CartController(ProductActivity.this);
                addCartController.getCartCodeData(ProductActivity.this, sku_id, count, is_prompt);

            } else {
                //没有设置属性,跳转加入
                Intent propertyActivity = new Intent(ProductActivity.this, ProductPropertyActivity.class);
                //把要用的数据传递过去,(这里可能会出问题)
                ItemSkuBean itemSkuBean = new ItemSkuBean();
                GoodDetail.ProductBean product = goodDetail.getProduct();
                cleanData(itemSkuBean, product);
                CommonUtil.printI("传递的商品属性数据:=",product.getItem_sku().toString());
                propertyActivity.putExtra("itemsku", itemSkuBean);
                propertyActivity.putExtra("product", product);
                propertyActivity.putExtra("mIs_prompt", is_prompt);
                startActivity(propertyActivity);
            }
        } else {
            jumpLogin();
        }
    }

    /**
     * 加入购物车
     */
    private void addCart() {
        if (NetWorkUtil.noHaveNet(ProductActivity.this)) {
            return;
        }
        IS_FROM_PROPERTY = false;
        is_prompt = 0;
        //判断是否登录
        if (SPUtils.contains(ProductActivity.this, "accountName")) {
            //是否有添加属性
            if (isHavedProperty) {
                CartController addCartController = new CartController(ProductActivity.this);
                addCartController.getCartCodeData(ProductActivity.this, sku_id, count, is_prompt);
            } else {
                ItemSkuBean itemSkuBean = new ItemSkuBean();
                GoodDetail.ProductBean product = goodDetail.getProduct();
                cleanData(itemSkuBean, product);
                //没有设置属性,跳转加入
                Intent propertyActivity = new Intent(ProductActivity.this, ProductPropertyActivity.class);
                //把要用的数据传递过去,(这里可能会出问题)
                propertyActivity.putExtra("itemsku",itemSkuBean);
                //把需要的商品名称传递过去
                propertyActivity.putExtra("product", product);
                propertyActivity.putExtra("mIs_prompt", is_prompt);
                startActivity(propertyActivity);
            }
        } else {
            jumpLogin();
        }
    }

    //商品属性页面返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            isHavedProperty = true;
            Bundle bundle = data.getExtras();
            String sku = bundle.getString("sku");
            sku_id = bundle.getString("sku_id");
            count = String.valueOf(bundle.getInt("count"));
            //显示返回的数据到控件上
            tv_product_property.setText(getString(R.string.tv_product_property, sku));
            tv_product_property.setTextColor(getResources().getColor(R.color.property_default_color));
        }
    }

    private void initView() {
        //顶部栏控件
        iv_product_share = (FrameLayout) findViewById(R.id.iv_product_share);

        pull_up_to_load_more = (PullUpToLoadMoreView) findViewById(R.id.pull_up_to_load_more);
        ll_request = (LinearLayout) findViewById(R.id.ll_request);
        btn_request_net = (Button) findViewById(R.id.btn_request_net);
        //商品图
        vp_product_detail = (ViewPager) findViewById(R.id.vp_product_detail);
        ll_point_container = (LinearLayout) findViewById(R.id.ll_point_product);
        product_share_image = (ImageView) findViewById(R.id.product_share_image);
        //商品信息
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_price = (TextView) findViewById(R.id.tv_product_price);
        tv_product_original_price = (TextView) findViewById(R.id.tv_product_original_price);
        tv_product_location = (TextView) findViewById(R.id.tv_product_location);
        rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        tv_product_freight = (TextView) findViewById(R.id.tv_product_freight);
        tv_product_month_sales_volume = (TextView) findViewById(R.id.tv_product_month_sales_volume);
        tv_product_grade = (TextView) findViewById(R.id.tv_product_grade);
        product_cash_coupon_tv = (TextView) findViewById(R.id.product_cash_coupon_tv);
        //商品活动
        tv_product_sales = (TextView) findViewById(R.id.tv_product_sales);
        ll_product_sales = (LinearLayout) findViewById(R.id.ll_product_sales);
        tv_product_discount = (TextView) findViewById(R.id.tv_product_discount);
        ll_product_discount = (LinearLayout) findViewById(R.id.ll_product_discount);
        tv_product_free_postage = (TextView) findViewById(R.id.tv_product_free_postage);
        ll_product_free_postage = (LinearLayout) findViewById(R.id.ll_product_free_postage);
        //评论数和跳转到评论页面
        rl_no_product_evaluate = (RelativeLayout) findViewById(R.id.rl_no_product_evaluate);
        ll_product_evaluate_user_name = (RelativeLayout) findViewById(R.id.ll_product_evaluate_user_name);
        tv_product_evaluate_user_name = (TextView) findViewById(R.id.tv_product_evaluate_user_name);
        tv_evaluate_time = (TextView) findViewById(R.id.tv_evaluate_time);
        tv_evaluate_content = (TextView) findViewById(R.id.tv_evaluate_content);
        tv_evaluate_count = (TextView) findViewById(R.id.tv_evaluate_count);
        rl_product_evaluate_count = (LinearLayout) findViewById(R.id.rl_product_evaluate_count);
        //设置商品属性
        tv_product_property = (TextView) findViewById(R.id.tv_product_property);
        rl_product_property = (RelativeLayout) findViewById(R.id.rl_product_property);
        iv_product_property = (ImageView) findViewById(R.id.iv_product_property);
        //店铺信息
        shop_layout = (RelativeLayout) findViewById(R.id.shop_layout);
        iv_shop_logo = (ImageView) findViewById(R.id.iv_shop_logo);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_shop_product_count = (TextView) findViewById(R.id.tv_shop_product_count);
        tv_shop_collect_count = (TextView) findViewById(R.id.tv_shop_collect_count);
        //第二页
        fl_webview_container = (FrameLayout) findViewById(R.id.fl_webview_container);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        product_web = new CustomWebView(this);
        product_web.setScrollbarFadingEnabled(true);
        fl_webview_container.addView(product_web, params);
        //底部栏控件
        ll_collect = (LinearLayout) findViewById(R.id.ll_collect);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        iv_product_detail_shoppping_cart = (ImageView) findViewById(R.id.iv_product_detail_shoppping_cart);
        join_shop_btn = (TextView) findViewById(R.id.join_shop_btn);
        buy_now_btn = (TextView) findViewById(R.id.buy_now_btn);
    }

    private class ProductPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pictureUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = new ImageView(ProductActivity.this);
            GlideUtil.getInstance().loadImage(ProductActivity.this, pictureUrls.get(position),R.drawable.big_banner_bg,R.drawable.big_banner_error_bg, iv);
            container.addView(iv);
            //点击图片是处理点击事件
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //开启大图模式
                    Intent pictureActivity = new Intent(ProductActivity.this, BigProductPictureActivity.class);
                    pictureActivity.putStringArrayListExtra("urls", pictureUrls);
                    pictureActivity.putExtra("position", position);
                    startActivity(pictureActivity);
                }
            });
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

        SharePopWindow() {
            initUI();
            initData();
        }

        @Override
        public void initUI() {
            View contentView = LayoutInflater.from(ProductActivity.this).inflate(R.layout.pop_share, null);
            mSharing_home_tv = contentView.findViewById(R.id.sharing_home_tv);
            mSharing_share_tv = contentView.findViewById(R.id.sharing_share_tv);
            mPopupWindow = new PopupWindow(contentView, 500, 200);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setFocusable(true);//PopWindow创建的时候默认内部控件无法被点击
            mPopupWindow.setOutsideTouchable(true);//设置了setFocusable后 外部控件无法点击
            //点击外部控件 让Pop消失
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            mPopupWindow.update();//刷新下页面  此时Pop还有消失出来
        }

        @Override
        public void initData() {
            mSharing_share_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShare();
                }
            });
        }

        @Override
        public void onShow(View anchor) {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                mPopupWindow.showAsDropDown(anchor);
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
        oks.setImageUrl(goodDetail.getProduct().getIcon());
        oks.setTitle(goodDetail.getProduct().getName());
        oks.setTitleUrl(goodDetail.getProduct().getLink());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(goodDetail.getProduct().getLink());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(goodDetail.getProduct().getLink());
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        fl_webview_container.removeView(product_web);
        product_web.removeAllViews();
        product_web.destroy();
        product_web = null;
        super.onDestroy();
    }
}
