package com.winguo.mine.collect;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseFragment2;
import com.winguo.base.BaseTitleFragmentActivity;
import com.winguo.mine.collect.goods.GoodsCollectAllBean;
import com.winguo.mine.collect.goods.GoodsCollectFragment;
import com.winguo.mine.collect.shop.ShopCollectFragment;
import com.winguo.utils.RequestCodeConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hcpai
 * @desc 商品和店铺收藏
 */
public class MyCollectActivity extends BaseTitleFragmentActivity implements View.OnClickListener {
//    private RelativeLayout hybrid4_account_register_back2;
    /**
     * 商品关注
     */
    private TextView mycollect_attention_goods_tv;
    /**
     * 店铺收藏
     */
    private TextView mycollect_attention_shop_tv;
    private List<BaseFragment2> fragments = new ArrayList<>();
    private GoodsCollectAllBean mGoodsCollectAllBean;
    /**
     * 是否加载商品收藏
     */
    private boolean isLoadingGoodsCollect = true;
    private GoodsCollectFragment mGoodsCollectFragment;
    private ShopCollectFragment mShopCollectFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        setBackBtn();
        initViews();
        initDatas();
        setListener();
    }

    /**
     * 获取布局id
     *
     * @return
     */
    private int getViewId() {
        return R.layout.activity_mycollect;
    }

    /**
     * 初始化视图
     */
    private void initViews() {
//        CommonUtil.stateSetting(this, R.color.white_top_color);
//        hybrid4_account_register_back2 = (RelativeLayout) findViewById(R.id.hybrid4_account_register_back2);
        mycollect_attention_goods_tv = (TextView) findViewById(R.id.mycollect_attention_goods_tv);
        mycollect_attention_shop_tv = (TextView) findViewById(R.id.mycollect_attention_shop_tv);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        int type = (int) getIntent().getExtras().get("collect");
        //获取fragment对象
        mGoodsCollectFragment = new GoodsCollectFragment();
        fragments.add(mGoodsCollectFragment);
        mShopCollectFragment = new ShopCollectFragment();
        fragments.add(mShopCollectFragment);
        //商品收藏
        if (type == 1) {
            isLoadingGoodsCollect = true;
            changeStatus(1);
            getSupportFragmentManager().beginTransaction().replace(R.id.mycollect_container, fragments.get(0)).commit();
        } else {
            //TODO 店铺收藏
            isLoadingGoodsCollect = false;
            changeStatus(2);
            getSupportFragmentManager().beginTransaction().replace(R.id.mycollect_container, fragments.get(1)).commit();
        }
    }

    /**
     * 设置监听器
     */
    private void setListener() {
//        hybrid4_account_register_back2.setOnClickListener(this);
        mycollect_attention_goods_tv.setOnClickListener(this);
        mycollect_attention_shop_tv.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_GOODS_COLLECT:
                this.mGoodsCollectAllBean = (GoodsCollectAllBean) msg.obj;
                //显示商品收藏
                getSupportFragmentManager().beginTransaction().replace(R.id.mycollect_container, fragments.get(0)).commit();
                break;
        }
    }

    public GoodsCollectAllBean getData() {
        return mGoodsCollectAllBean;
    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.hybrid4_account_register_back2:
//                finish();
//                break;
            case R.id.mycollect_attention_goods_tv:
                if (isLoadingGoodsCollect) {
                    return;
                }
                mGoodsCollectFragment.page = 1;
                GoodsCollectFragment.isFirst = true;
                isLoadingGoodsCollect = true;
                changeStatus(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.mycollect_container, fragments.get(0)).commit();
                break;
            case R.id.mycollect_attention_shop_tv:
                if (!isLoadingGoodsCollect) {
                    return;
                }
                isLoadingGoodsCollect = false;
                changeStatus(2);
                getSupportFragmentManager().beginTransaction().replace(R.id.mycollect_container, fragments.get(1)).commit();
                //TODO 店铺关注
                break;
        }
    }

    /**
     * 根据status变换Fragment的标题
     *
     * @param status
     */
    private void changeStatus(int status) {
        //商品收藏被选中
        if (status == 1) {
            mycollect_attention_goods_tv.setTextColor(getResources().getColor(R.color.mycollect_attention_select));
            mycollect_attention_shop_tv.setTextColor(getResources().getColor(R.color.mycollect_attention_normal));
            //店铺收藏被选中
        } else {
            mycollect_attention_shop_tv.setTextColor(getResources().getColor(R.color.mycollect_attention_select));
            mycollect_attention_goods_tv.setTextColor(getResources().getColor(R.color.mycollect_attention_normal));
        }
    }
}
