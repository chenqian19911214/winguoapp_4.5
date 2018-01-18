package com.winguo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.winguo.adapter.SpacesItemDecoration;
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
import com.winguo.utils.Constants;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 首页商品分类列表
 * 家电 年货 美妆
 */

public class HomeClassify2Activity extends BaseTitleActivity implements BaseQuickAdapter.RequestLoadMoreListener, IProductListView {


    private RecyclerView typeRecyclerView;
    private ImageView classify_theme_ic;
    private int cid;
    private ProductListController controller;
    private String type;
    private View headView;
    private FrameLayout classify2_content_container;
    private View noNetView;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        initHead();
        setContentView(R.layout.classify_activity);
        initView();
        takeData();
        setListener();
    }

    private void initHead() {
        //重设分类类型
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        List<Integer> cids= intent.getIntegerArrayListExtra("cids");
        tempCid.addAll(cids);
    }

    private void initView() {
        classify2_content_container = (FrameLayout) findViewById(R.id.classify2_content_container);
        //无网络
        //(给容器添加布局)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        noNetView = View.inflate(this, R.layout.no_net, null);
        Button btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        btn_request_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noNetView.setVisibility(View.GONE);
                classify2_content_container.setVisibility(View.VISIBLE);
                takeData();
            }
        });
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop2 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop2, null, null);
        noNetView.setVisibility(View.GONE);
        classify2_content_container.addView(noNetView, params);

        //1、返回数据为空
        emptyView = View.inflate(HomeClassify2Activity.this, R.layout.loading_empty, null);
        TextView textView = (TextView) emptyView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.loading_empty));
        emptyView.setVisibility(View.GONE);
        classify2_content_container.addView(emptyView, params);

        typeRecyclerView = (RecyclerView) findViewById(R.id.classify_rv);
        headView = getLayoutInflater().inflate(R.layout.classify_type_head, (ViewGroup) typeRecyclerView.getParent(), false);
        classify_theme_ic = (ImageView)headView.findViewById(R.id.classify_theme_ic);

        typeRecyclerView.setLayoutManager(new GridLayoutManager(HomeClassify2Activity.this, 2, GridLayoutManager.VERTICAL, false));
        typeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        typeRecyclerView.addItemDecoration(new SpacesItemDecoration(2));
        //初始化请求
        controller = new ProductListController(this);
        if (type != null) {
            switch (type) {
                case Constants.TYPE_HOUSE_ELE:
                    GlideUtil.getInstance().loadImage(this, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYE0SAUVu0AAKTtO1Yr9Q28..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, classify_theme_ic);
                    break;
                case Constants.TYPE_BEAUTY:
                    GlideUtil.getInstance().loadImage(this, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYE0SADbwJAAKYlN5rPF426..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, classify_theme_ic);
                    break;
                case Constants.TYPE_SNACK:
                    GlideUtil.getInstance().loadImage(this, "http://g1.img.winguo.com/group1/M00/06/78/wKgAUVpYE0WAYmTxAAOBHU4kWsg66..png", R.drawable.big_banner_bg, R.drawable.big_banner_error_bg, classify_theme_ic);
                    break;
            }
        }



    }

    private void takeData() {
        //请求分类数据
        toLoadNextPage(0);
    }

    private void toLoadNextPage(int tempPage) {
        cid = tempCid.get(tempPage); //临时写死
        LoadDialog.show(this, true);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            controller.getData(this,"",1,"","",cid,null,null,0,0,null);
        } else {
            LoadDialog.dismiss(this);
            ToastUtil.showToast(this,getString(R.string.no_net));
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    private void toLoadMorePage(int tempPage) {
        cid = tempCid.get(tempPage); //临时写死
        controller.getData(this,"",1,"","",cid,null,null,0,0,null);
    }

    private void setListener() {

    }


    private RecyclerClassifyAdapter classify2Adapter;
    private int TOTAL = 0;

    /**
     * 初始化适配器
     */
    private void initAdapter(final List<ItemEntitys> item) {
        TOTAL = item.size();
        if (type != null) {
            switch (type) {
                case Constants.TYPE_HOUSE_ELE:
                    //更换不同条目
                    classify2Adapter = new RecyclerClassifyAdapter(R.layout.classify1_item, item);
                    classify2Adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                    break;
                case Constants.TYPE_BEAUTY:
                    //更换不同条目
                    classify2Adapter = new RecyclerClassifyAdapter(R.layout.classify1_item, item);
                    classify2Adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                    break;
                case Constants.TYPE_SNACK:
                    //更换不同条目
                    classify2Adapter = new RecyclerClassifyAdapter(R.layout.classify1_item, item);
                    classify2Adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                    break;
            }
        }
        classify2Adapter.setOnLoadMoreListener(this, typeRecyclerView);
      //  classify2Adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);
        classify2Adapter.setLoadMoreView(new CustomLoadMoreView());
        typeRecyclerView.setAdapter(classify2Adapter);
        mCurrentCounter = classify2Adapter.getData().size();

        typeRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                ItemEntitys itemEntitys = item.get(position);
                int entity_id = itemEntitys.entity_id;
                Intent it = new Intent(HomeClassify2Activity.this,ProductActivity.class);
                it.putExtra("gid",entity_id);
                startActivity(it);
            }
        });
        classify2Adapter.addHeaderView(headView);

    }

    private List<Integer> tempCid = new ArrayList<>();
    private static final int PAGE_SIZE = 6;
    private int mCurrentCounter = 0;
    private boolean mLoadMoreEndGone = false;

    private int cidIndex = 0;
    @Override
    public void onLoadMoreRequested() {
        cidIndex++;
        Log.e("onloadMore",""+cidIndex+"::mCurent="+mCurrentCounter+":: TOTAL="+TOTAL);
        // TODO: 2017/5/15  加载更多
        if (classify2Adapter.getData().size() < PAGE_SIZE) {
            if (cidIndex < tempCid.size()) {
                toLoadMorePage(cidIndex);
            } else {
                //如果数据少于规定分页数据 显示没有更多
                classify2Adapter.loadMoreEnd(true);
            }
            Log.e("onloadMore PAGE_SIZE",""+cidIndex);

        } else {

            if (cidIndex < tempCid.size()) {
                toLoadMorePage(cidIndex);
            } else {
                if (mCurrentCounter >= TOTAL) {
                    //如果当前数据量展示完毕 显示没有更多
                    Log.e("onloadMore","mCurrentCounter >= TOTAL");
                    classify2Adapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visibl
                }
            }

        }
    }

    /**
     * handler机制
     */
    public Handler handler = new HomeClassify2Activity.LeakHander(this);


    /**
     * 获取商品数据
     * @param productListBeens
     */
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
                        if (classify2Adapter != null) {
                            TOTAL += item.size();
                            classify2Adapter.addData(item);
                            mCurrentCounter += classify2Adapter.getData().size();
                            Log.e("onloadMore size",""+item.size());
                        } else {
                            initAdapter(item);
                        }
                    } else {
                        if (classify2Adapter == null) {
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    // TODO: 2017/5/31 没有找到该分类的商品
                    if (classify2Adapter == null) {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            // TODO: 2017/5/31 网络请求超时或服务器异常
            classify2_content_container.setVisibility(View.GONE);
            noNetView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取更多数据
     * @param productListBeens
     */
    @Override
    public void getMoreProductList(ProductListBean productListBeens) {
        Log.e("getMoreProductList",":"+productListBeens.toString());
        LoadDialog.dismiss(this);

    }

    @Override
    public void getProvinceCity(ProvinceCityBean provinceCityBean) {

    }

    @Override
    public void getClassifyName(ClassifyName classifyName) {

    }


    //弱引用，防止内存泄漏
    static class LeakHander extends Handler {
        WeakReference<HomeClassify2Activity> mWeakReference;

        public LeakHander(HomeClassify2Activity baseActivity) {
            mWeakReference = new WeakReference<HomeClassify2Activity>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().handleMsg(msg);
            }
        }
    }

    /**
     * 处理hnandle消息
     * @param msg
     */
    public void handleMsg(Message msg) {

        switch (msg.what) {

            default:
                break;
        }

    }

}
