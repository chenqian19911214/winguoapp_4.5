package com.winguo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.winguo.R;
import com.winguo.adapter.RecyclerClassifyAdapter;
import com.winguo.base.BaseTitleActivity;
import com.winguo.cad.loadmore.CustomLoadMoreView;
import com.winguo.net.GlideUtil;
import com.winguo.productList.controller.ProductListController;
import com.winguo.productList.modle.ItemEntitys;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.ProductsEntity;
import com.winguo.productList.bean.SearchEntity;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.view.IProductListView;
import com.winguo.utils.BitmapUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;

import java.util.List;

/**
 * 首页商品分类列表
 * 服饰鞋包 中外名酒 家具日用
 */
public class HomeClassify1Activity extends BaseTitleActivity implements BaseQuickAdapter.RequestLoadMoreListener, IProductListView {

    private RecyclerView home_classify1_recycler;
    private FrameLayout classify1_content_container;
    private CoordinatorLayout ll_clothes_container;
    private ImageView classify_theme_ic;
    private RecyclerClassifyAdapter adapter;
    private ProductListController controller;
    private int cid;
    private String type;
    private View headView;
    private Button btn_request_net;
    private View noNetView;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setStatuBackground();
        setContentView(R.layout.activity_home_classify1);
        initView();
        initHead();
        takeData();
    }

    private void setStatuBackground() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        cid = intent.getIntExtra("cid",0);
        CommonUtil.stateSetting(this, R.color.title_color);
       
    }

    private void initHead() {
        if (type != null) {
            switch (type) {
                case Constants.TYPE_CLOTHES:
                    GlideUtil.getInstance().loadImage(this, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYE0SAHRNsAALzZjQOePc02..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, classify_theme_ic);
                    break;
                case Constants.TYPE_DAY_USE:
                    GlideUtil.getInstance().loadImage(this, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYE0SAF-B0AAMUbIAszD860..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, classify_theme_ic);
                    break;
                case Constants.TYPE_WINE:
                    GlideUtil.getInstance().loadImage(this, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYE0WAI-TwAALxGz9Cuzo42..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, classify_theme_ic);
                    break;
            }
        }
    }

    private void initView() {
        classify1_content_container = (FrameLayout) findViewById(R.id.classify1_content_container);
        //无网络
        //(给容器添加布局)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        btn_request_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noNetView.setVisibility(View.GONE);
                classify1_content_container.setVisibility(View.VISIBLE);
                takeData();
            }
        });
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        noNetView.setVisibility(View.GONE);
        classify1_content_container.addView(noNetView, params);

        //1、返回数据为空
        emptyView = View.inflate(HomeClassify1Activity.this, R.layout.loading_empty, null);
        TextView textView = (TextView) emptyView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.loading_empty));
        emptyView.setVisibility(View.GONE);
        classify1_content_container.addView(emptyView, params);

        home_classify1_recycler = (RecyclerView) findViewById(R.id.home_classify1_recycler);
        ll_clothes_container = (CoordinatorLayout) findViewById(R.id.ll_clothes_container);
        headView = getLayoutInflater().inflate(R.layout.classify_type_head, (ViewGroup) home_classify1_recycler.getParent(), false);
        classify_theme_ic = (ImageView)headView.findViewById(R.id.classify_theme_ic);

        home_classify1_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        home_classify1_recycler.setItemAnimator(new DefaultItemAnimator());
        //home_classify1_recycler.addItemDecoration(new SpacesItemDecoration(5));

        controller = new ProductListController(this);
    }

    private void takeData() {
        //请求分类数据
        toLoadNextPage(1);

    }

    private int currPage = 1;
    private void toLoadNextPage(int page) {
        LoadDialog.show(this, true);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            controller.getData(this, "", page, "", "", cid, null, null, 0, 0, null);
        } else {
            LoadDialog.dismiss(this);
            ToastUtil.showToast(this,getString(R.string.no_net));
            noNetView.setVisibility(View.VISIBLE);
        }

    }
    private void toLoadMorePage(int page) {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            controller.getData(this, "", page, "", "", cid, null, null, 0, 0, null);
        } else {
            ToastUtil.showToast(this,getString(R.string.no_net));
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 修改跟布局背景
     *
     * @param layoutId
     */
    private void setBackground(int layoutId) {
        int screenWidth = ScreenUtil.getScreenWidth(this);
        int screenHeight = ScreenUtil.getScreenHeight(this);
        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(getResources(), layoutId, screenWidth, screenHeight);
        ll_clothes_container.setBackgroundDrawable(BitmapUtil.bitMapToDrawable(bitmap));
    }

    private static final int PAGE_SIZE = 6;
    private int TOTAL = 0;
    private int mCurrentCounter = 0;
    private boolean mLoadMoreEndGone = false;

    private void initAdapter(final List<ItemEntitys> item) {
        TOTAL = item.size();
        //更换不同条目
        adapter = new RecyclerClassifyAdapter(R.layout.classify1_item,item);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        adapter.setOnLoadMoreListener(this, home_classify1_recycler);
      //  adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        home_classify1_recycler.setAdapter(adapter);
        mCurrentCounter = adapter.getData().size();

        home_classify1_recycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                ItemEntitys itemEntitys = item.get(position);
                int entity_id = itemEntitys.entity_id;
                Intent it = new Intent(HomeClassify1Activity.this,ProductActivity.class);
                it.putExtra("gid",entity_id);
                startActivity(it);
            }
        });
        adapter.addHeaderView(headView);
    }

    @Override
    public void onLoadMoreRequested() {
        // TODO: 2017/5/15  加载更多
        currPage++;
        if (adapter.getData().size() < PAGE_SIZE) {
            //如果数据少于规定分页数据 显示没有更多
            adapter.loadMoreEnd(true);
        } else {
            adapter.loadMoreLoading();
            toLoadMorePage(currPage);
        }
    }


    @Override
    public void getProductList(ProductListBean productListBeens) {
        LoadDialog.dismiss(this);
        if (productListBeens != null) {
            SearchEntity search = productListBeens.search;
            if (search != null) {
                ProductsEntity products = search.products;
                if (products != null) {
                    List<ItemEntitys> item = products.item;
                    if (item != null) {
                        if (adapter != null) {
                            TOTAL += item.size();
                            adapter.addData(item);
                            mCurrentCounter += adapter.getData().size();
                            Log.e("onloadMore size",""+item.size());
                        } else {
                            initAdapter(item);
                        }
                    } else {
                        // TODO: 2017/5/31
                        if (adapter == null) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            //是否真的分页加载完
                            if (mCurrentCounter >= TOTAL) {
                                //如果当前数据量展示完毕 显示没有更多
                                adapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visibl
                            }
                        }
                    }
                } else {
                    // TODO: 2017/5/31 没有找到该分类的商品
                    if (adapter == null) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        if (mCurrentCounter >= TOTAL) {
                            //如果当前数据量展示完毕 显示没有更多
                            adapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visibl
                        }
                    }
                }
            }

        } else {
            // TODO: 2017/5/31 网络请求超时或服务器异常
            classify1_content_container.setVisibility(View.GONE);
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getMoreProductList(ProductListBean productListBeens) {

    }

    @Override
    public void getProvinceCity(ProvinceCityBean provinceCityBean) {

    }

    @Override
    public void getClassifyName(ClassifyName classifyName) {

    }
}
