package com.winguo.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guobi.account.WinguoAccountConfig;
import com.winguo.R;
import com.winguo.adapter.RecyclerAdapter;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.adapter.UserTodayShopAdapter;
import com.winguo.base.BaseActivity;
import com.winguo.bean.UserTodayShop;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/26.
 * 商品推荐 详情
 */
@Deprecated
public class ShopRecommendActivity extends BaseActivity implements WinguoAcccountManagerUtils.IUserLabelShopData, WinguoAcccountManagerUtils.ITodayShopData, WinguoAcccountManagerUtils.IOpenLabel {

    private RecyclerView todayShop;
    private boolean isLogin;//是否登录
    private boolean isOpenLabel;//是否开启标签
    private String accountName;
    private List<UserTodayShop> shops = new ArrayList<>();
    private UserTodayShopAdapter adapter;
    private Button openLabel;
    private LinearLayout open;

    @Override
    protected int getLayout() {
        return R.layout.shop_recommend_activity;
    }

    @Override
    protected void initData() {
        CommonUtil.stateSetting(this, R.color.white_top_color);
        Intent intent = getIntent();
        String flag1 = intent.getStringExtra("isLogin");
        String flag2 = intent.getStringExtra("isOpenLabel");
        if (flag1 != null) {
            isLogin = true;
            accountName = (String) SPUtils.get(this, "accountName", "");
            WinguoAcccountManagerUtils.takeShopDataLogin(accountName, this);
            if (flag2.equals("1")) {
                isOpenLabel = true;
            }
        } else {
            WinguoAcccountManagerUtils.takeShopDataNoLogin(this);
        }


    }
    private ImageView back;
    private ImageView shopCart;
    private TextView topTitle;

    @Override
    protected void initViews() {
        back = (ImageView) findViewById(R.id.top_back);
        topTitle = (TextView) findViewById(R.id.layout_title);
        shopCart = (ImageView) findViewById(R.id.my_shopping_cart);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.today_recommend_sv);
        openLabel = (Button) findViewById(R.id.shop_recommend_open_my_label);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,0);
            }
        });
        open = (LinearLayout) findViewById(R.id.shop_recommend_open);

        todayShop = (RecyclerView) findViewById(R.id.today_shop_rv);
        if (isLogin) {
            //登录
            //设置layoutManager
            todayShop.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            if (isOpenLabel) {
                open.setVisibility(View.GONE);
            }

        } else {
            //未登录
            todayShop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        }
        adapter = new UserTodayShopAdapter(this, R.layout.today_shop_item,shops);
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        todayShop.addItemDecoration(decoration);
        todayShop.setItemAnimator(new DefaultItemAnimator());
        setData();

    }

    private void setData() {
        topTitle.setText(getString(R.string.day_shop_recommend_title));
        todayShop.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        shopCart.setOnClickListener(this);
        openLabel.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                UserTodayShop userTodayShop = shops.get(position);
                String details = WinguoAccountConfig.getDOMAIN()+userTodayShop.getUrl();
                Log.i("itemClickdetail : ",details);
                Intent it = new Intent(ShopRecommendActivity.this,ShopCenterActivity.class);
                it.putExtra("Target",details);
                startActivity(it);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.my_shopping_cart:

                break;
            case R.id.shop_recommend_open_my_label:
                if (isLogin) {
                    WinguoAcccountManagerUtils.userLabelOpen(accountName, this);
                } else {
                    ToastUtil.showToast(this,"请先登录");
                }
                break;
        }
    }

    @Override
    public void labelShopData(List<UserTodayShop> data) {
        if (data != null) {
            shops.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void labelShopDataErrorMsg(int message) {

    }

    /**
     * 未登录 获取数据
     * @param data
     */
    @Override
    public void shopData(List<UserTodayShop> data) {
        if (data != null) {
            shops.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void shopDataErrorMsg(int message) {

    }

    @Override
    public void openLabel(String result) {

        try {
            // {"msg":"\u6807\u7b7e\u5f00\u542f\u6210\u529f","code":0}
            JSONObject root = new JSONObject(result);
            String msg = root.getString("msg");
            String code = root.getString("code");
          //  1. code=0   标签开启成功    2.code =1   账号不存在    3.code =2   账号为空

            switch (code) {
                case "0":
                    startActivity(new Intent(ShopRecommendActivity.this,PersonLabelActivity.class));
                    open.setVisibility(View.GONE);
                    break;
                case "1":
                    ToastUtil.showToast(ShopRecommendActivity.this,getString(R.string.shop_comm_open_label_account_no));
                    break;
                case "2":
                    ToastUtil.showToast(ShopRecommendActivity.this,getString(R.string.shop_comm_open_label_account_null));
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openLabelErrorMsg(int message) {

    }



}
