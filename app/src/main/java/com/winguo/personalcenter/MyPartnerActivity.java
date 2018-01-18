package com.winguo.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.MyPartner;
import com.winguo.utils.GsonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/6.
 */

public class MyPartnerActivity extends BaseTitleActivity implements WinguoAcccountManagerUtils.IUserMyPartner,View.OnClickListener {

    private FrameLayout  my_partner_myqr, my_partner;
    private TextView  my_partner_name_right, my_partner_income_tip;
    private WinguoAccountGeneral info;
    private RecyclerView my_partner_income_rv;
    private RecyclerCommonAdapter<MyPartner.ResultBean> adapter;
    private List<MyPartner.ResultBean> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_partner);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initDatas() {
//        CommonUtil.stateSetting(this, R.color.white_top_color);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this, true);
            WinguoAcccountManagerUtils.userMyPartner(this, WinguoAccountDataMgr.getUserName(this), this);
        } else {
            ToastUtil.showToast(this,getString(R.string.no_net));
        }

    }

    private void initViews() {
//        back = (FrameLayout) findViewById(R.id.top_back);
//        topTitle = (TextView) findViewById(R.id.layout_title);


        my_partner_myqr = (FrameLayout) findViewById(R.id.my_partner_myqr);
        my_partner = (FrameLayout) findViewById(R.id.my_partner);
        my_partner_name_right = (TextView) findViewById(R.id.my_partner_name_right);
        my_partner_income_tip = (TextView) findViewById(R.id.my_partner_income_tip);
        my_partner_income_rv = (RecyclerView) findViewById(R.id.my_partner_income_rv);
        my_partner_income_rv.setItemAnimator(new DefaultItemAnimator());
        my_partner_income_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        my_partner_income_rv.addItemDecoration(decoration);


        fillData();
    }

    private void fillData() {
//        topTitle.setText(getString(R.string.my_family_partner_ac));
        adapter = new RecyclerCommonAdapter<MyPartner.ResultBean>(this, R.layout.my_partner_item, data) {

            @Override
            protected void convert(RecylcerViewHolder holder, MyPartner.ResultBean resultBean, int position) {
                holder.setText(R.id.my_partner_name, resultBean.getAccount());
                holder.setText(R.id.my_partner_income, getString(R.string.my_partner_income));
            }
        };
        my_partner_income_rv.setAdapter(adapter);

    }

    private void initListener() {
//        back.setOnClickListener(this);
        my_partner_myqr.setOnClickListener(this);
        my_partner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.top_back:
//                finish();
//                break;
            case R.id.my_partner_myqr:
                if (SPUtils.contains(this, "userShopURL")) {
                    Intent rl_register_qr_code = new Intent(MyPartnerActivity.this, MyQRCodeActivity.class);
                    startActivity(rl_register_qr_code);
                } else {
                    Toast.makeText(MyPartnerActivity.this, R.string.you_on_stop, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_partner:

                break;

        }
    }

    /**
     * 获取我的伙伴
     *
     * @param result
     */
    @Override
    public void userMyPartner(final String result) {
        // TODO: 2017/5/6
        try {
            //{"status":"error","text":"\u767b\u5f55\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\u3002","code":-101}
            //{"Result":"NULL","code":"-1","count":0,"hasmore":"0"}
            // {"Result":{"cid":["39362","39491","39493","39525"],"account":["test1111","13800138026","13800138660","13640030212"]},"code":"0","count":4,"hasmore":"0"}
            JSONObject root = new JSONObject(result);
            String code = root.getString("code");
            switch (code) {
                case "-101":
                    //登录过期
                    String text = root.getString("text");
                    ToastUtil.showToast(this,text);
                    break;
                case "-1":
                    //没有伙伴
                    // TODO: 2017/5/6 无伙伴
                    my_partner_income_rv.setVisibility(View.GONE);
                    my_partner_income_tip.setText(getString(R.string.my_partner_no_tip));
                    LoadDialog.dismiss(this);
                    break;
                case "0":
                    MyPartner myPartner = null;
                    try {
                        myPartner = GsonUtil.json2Obj(result, MyPartner.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();


                    } finally {
                        int count = myPartner.getCount();
                        if (count != 0) {
                            // TODO: 2017/5/6 有伙伴
                            if (data.isEmpty()) {
                                data.addAll(myPartner.getResult());
                                adapter.notifyDataSetChanged();
                                LoadDialog.dismiss(this);

                            }

                        }
                    }

                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            //登录过期

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LoadDialog.dismiss(this);
        }


    }

    @Override
    public void userMyPartnerErrorMsg(int message) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, getString(R.string.no_net_or_service_no));
    }

}
