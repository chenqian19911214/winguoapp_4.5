package com.winguo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.guobi.winguoapp.voice.VoiceFunActivity;
import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.lbs.order.list.StoreOrderListActivity;
import com.winguo.mine.order.MyOrderActivity;
import com.winguo.mine.order.list.CommonBean;
import com.winguo.net.GlideUtil;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.utils.ActionUtil;
import com.winguo.view.InterListView;

import java.util.ArrayList;

/**
 * Created by Admin on 2017/2/6.
 */

public class PaySuccessActivity extends BaseTitleActivity implements View.OnClickListener {
    //    private FrameLayout pay_success_back_btn;
    private TextView pay_success_consignee_text;
    private TextView pay_success_consignee_phone_text;
    private TextView pay_success_consignee_address_text;
    private InterListView lv_pay_sucess;
    private TextView shopping_again_btn;
    private TextView check_order_btn;
    private ArrayList<CommonBean> commonBeen;
    private ItemBean mItemBean;
    private TextView tv_pay_success_goods_price;
    private String price;
    private boolean isCheckOrder = false;
    private boolean isShoppingAgain = false;
    private ImageView iv_bottom_voice_btn;
    private int isphysical;
    private String name;
    private String phone;
    private TextView physical_pay_success_consignee_text;
    private TextView physical_pay_success_consignee_phone_text;
    private RelativeLayout rl_physical_address;
    private LinearLayout ll_store_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        setBackBtn();
        initViews();
        initDatas();
        initListener();
    }

    private void initListener() {
        //        pay_success_back_btn.setOnClickListener(this);
        shopping_again_btn.setOnClickListener(this);
        check_order_btn.setOnClickListener(this);
        iv_bottom_voice_btn.setOnClickListener(this);
    }

    private void initViews() {
        Intent intent = getIntent();
        isphysical = intent.getIntExtra("isphysical", 0);
        commonBeen = (ArrayList<CommonBean>) intent.getSerializableExtra("commonBeen");
        price = intent.getStringExtra("price");
        if (isphysical==0) {
            mItemBean = (ItemBean) intent.getSerializableExtra("mItemBean");
        }else if (isphysical==1){
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");

        }
        initView();
    }

    private void initView() {
//        pay_success_back_btn = (FrameLayout) findViewById(R.id.pay_success_back_btn);
        ll_store_address = (LinearLayout) findViewById(R.id.ll_store_address);
        pay_success_consignee_text = (TextView) findViewById(R.id.pay_success_consignee_text);
        pay_success_consignee_phone_text = (TextView) findViewById(R.id.pay_success_consignee_phone_text);
        pay_success_consignee_address_text = (TextView) findViewById(R.id.pay_success_consignee_address_text);

        rl_physical_address = (RelativeLayout) findViewById(R.id.rl_physical_address);
        physical_pay_success_consignee_text = (TextView) findViewById(R.id.physical_pay_success_consignee_text);
        physical_pay_success_consignee_phone_text = (TextView) findViewById(R.id.physical_pay_success_consignee_phone_text);

        lv_pay_sucess = (InterListView) findViewById(R.id.lv_pay_sucess);
        tv_pay_success_goods_price = (TextView) findViewById(R.id.tv_pay_success_goods_price);
        shopping_again_btn = (TextView) findViewById(R.id.shopping_again_btn);
        check_order_btn = (TextView) findViewById(R.id.check_order_btn);
        iv_bottom_voice_btn = (ImageView) findViewById(R.id.iv_bottom_voice_btn);
    }

    private void initDatas() {
        //给收货地址赋值
        if (isphysical==0) {
            rl_physical_address.setVisibility(View.GONE);
            ll_store_address.setVisibility(View.VISIBLE);
            if (mItemBean != null) {
                pay_success_consignee_text.setText(getResources().getString(R.string.pay_result_consignee_text) + mItemBean.dinfo_received_name);
                if (TextUtils.isEmpty(mItemBean.dinfo_mobile)) {
                    pay_success_consignee_phone_text.setText(mItemBean.dinfo_tel);
                }
                pay_success_consignee_phone_text.setText(mItemBean.dinfo_mobile);
                pay_success_consignee_address_text.setText(getResources().getString(R.string.pay_result_receiver_address_text) + mItemBean.provinceName + " " + mItemBean.cityName + " " + mItemBean.areaName + " " + mItemBean.townName + " " +
                        mItemBean.dinfo_address);
            }
        }else if (isphysical==1){
            rl_physical_address.setVisibility(View.VISIBLE);
            ll_store_address.setVisibility(View.GONE);
            physical_pay_success_consignee_text.setText(getResources().getString(R.string.pay_result_consignee_text) +name);
            physical_pay_success_consignee_phone_text.setText(phone);
        }
        tv_pay_success_goods_price.setText("¥ " + price);
        //给支付成功的商品展示
        if (commonBeen != null) {
            PaySuccessGoodsAdapter adapter = new PaySuccessGoodsAdapter();
            lv_pay_sucess.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.pay_success_back_btn:
//                finish();
//                break;
            case R.id.iv_bottom_voice_btn:
                Intent voiceIntent = new Intent(PaySuccessActivity.this, VoiceFunActivity.class);
                //analysis这个字段 false：语音-->文字 直接跳转到SearchActivity ; true：加词句分析（打电话 发短息）
                voiceIntent.putExtra("analysis", "true");
                startActivity(voiceIntent);

                break;
            case R.id.shopping_again_btn:
                //跳转到首页
                if (!isShoppingAgain) {
                    isShoppingAgain = true;
                    Intent intent = new Intent(PaySuccessActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                break;
            case R.id.check_order_btn:
                //跳转到待支付订单列表页面
                if (!isCheckOrder) {
                    isCheckOrder = true;
                    if (isphysical==0) {
                        Intent intentMyOrder = new Intent(PaySuccessActivity.this, MyOrderActivity.class);
                        intentMyOrder.putExtra(ActionUtil.ACTION_ORDER_STATUS, 0);
                        startActivity(intentMyOrder);
                    }else if (isphysical==1){
                        Intent intentMyOrder = new Intent(PaySuccessActivity.this, StoreOrderListActivity.class);
                        intentMyOrder.putExtra("store_order_state", 0);
                        startActivity(intentMyOrder);
                    }
                    finish();
                }
                break;
        }
    }


    private class PaySuccessGoodsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int count = 0;
            if (commonBeen != null) {
                count = commonBeen.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(PaySuccessActivity.this, R.layout.pay_success_goods_item, null);
                holder.iv_pay_success_icon = (ImageView) convertView.findViewById(R.id.iv_pay_success_icon);
                holder.tv_pay_success_goods_name = (TextView) convertView.findViewById(R.id.tv_pay_success_goods_name);
                holder.tv_pay_success_goods_count = (TextView) convertView.findViewById(R.id.tv_pay_success_goods_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CommonBean commonBean = commonBeen.get(position);
            GlideUtil.getInstance().loadImage(PaySuccessActivity.this, commonBean.content,
                    R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, holder.iv_pay_success_icon);
            holder.tv_pay_success_goods_name.setText(commonBean.name);
            holder.tv_pay_success_goods_count.setText("x " + commonBean.num);
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv_pay_success_icon;
        TextView tv_pay_success_goods_name, tv_pay_success_goods_count;
    }
}
