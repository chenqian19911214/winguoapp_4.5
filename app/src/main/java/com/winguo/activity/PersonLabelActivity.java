package com.winguo.activity;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guobi.gblocation.GBDLocation;
import com.guobi.gblocation.ILocationCallBck;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.flowlayout.FlowLayout;
import com.winguo.flowlayout.TagAdapter;
import com.winguo.flowlayout.TagFlowLayout;
import com.winguo.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人标签设置界面
 * Created by admin on 2017/4/21.
 */
@Deprecated
public class PersonLabelActivity extends BaseActivity {

    List<String> myLabelData = new ArrayList<>();
    List<String> labelData = new ArrayList<>();
    private ImageView back;
    private ImageView shopCart;
    private TextView topTitle;
    private ScrollView scrollView;
    private FrameLayout locationFL;
    private TextView location;
    private TagFlowLayout myLabel;
    private TextView refresh;
    private TagFlowLayout chioceLabel;
    private Button modify;
    private TagAdapter<String> myLabelAdapter;
    private TagAdapter<String> chioceLabelAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_person_label;
    }

    @Override
    protected void initData() {
        CommonUtil.stateSetting(this, R.color.white_top_color);
        myLabelData.add("二次元");
        myLabelData.add("好日哈寒");
        myLabelData.add("国服");
        myLabelData.add("潮流式");
        myLabelData.add("顽固");
        myLabelData.add("呆板式");
        myLabelData.add("二次元");
        myLabelData.add("好日哈寒");

        labelData.add("3C数码");
        labelData.add("服装搭配");
        labelData.add("追星达人");
        labelData.add("3C数码1");
        labelData.add("服装搭配1");
        labelData.add("追星达人1");
        labelData.add("3C数码2");
        labelData.add("服装搭配2");
        labelData.add("追星达人");
    }

    @Override
    protected void initViews() {

        back = (ImageView) findViewById(R.id.top_back);
        topTitle = (TextView) findViewById(R.id.layout_title);
        shopCart = (ImageView) findViewById(R.id.my_shopping_cart);
        scrollView = (ScrollView) findViewById(R.id.person_label_scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        locationFL = (FrameLayout) findViewById(R.id.person_label_def_location_fl);
        location = (TextView) findViewById(R.id.person_label_def_location);
        myLabel = (TagFlowLayout) findViewById(R.id.person_my_label_rv);
        refresh = (TextView) findViewById(R.id.person_chioce_label_refresh);
        chioceLabel = (TagFlowLayout) findViewById(R.id.person_chioce_label_rv);
        modify = (Button) findViewById(R.id.person_label_modify);

        fillData();
        setMyLabel();

    }

    private void setMyLabel() {
        myLabelAdapter = new TagAdapter<String>(myLabelData) {
            @Override
            public View getView(FlowLayout parent, final int position, String s) {
                View v =  mInflater.inflate(R.layout.person_my_lables_item, myLabel, false);
                ((TextView)v.findViewById(R.id.lable_item_content)).setText(myLabelData.get(position));
                v.findViewById(R.id.lable_item_content_modify).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myLabelData.remove(position);
                        myLabelAdapter.notifyDataChanged();
                    }
                });
                return v;
            }
        };
        myLabel.setAdapter(myLabelAdapter);

        //可选标签
        chioceLabelAdapter = new TagAdapter<String>(labelData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, chioceLabel, false);
                tv.setText(labelData.get(position));
                return tv;
            }
        };

        chioceLabel.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                myLabelData.add(0,labelData.get(position));
                myLabelAdapter.notifyDataChanged();
                return false;
            }
        });
        chioceLabel.setAdapter(chioceLabelAdapter);
    }

    /**
     * 填充数据
     */
    private void fillData() {
        topTitle.setText(getString(R.string.winguo_person_label_recommend));
        //定位到 省份 城市信息
        if (GBDLocation.getInstance().getCity() != null) {
            location.setText(GBDLocation.getInstance().getCity());
        } else {
            location.setText(getString(R.string.person_label_set_location_false));
            GBDLocation.getInstance().startLocation(new ILocationCallBck() {
                @Override
                public void locationCall(String name) {

                }
            });
        }


    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        shopCart.setOnClickListener(this);
        locationFL.setOnClickListener(this);
        refresh.setOnClickListener(this);
        modify.setOnClickListener(this);
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
                //跳转购物车

                break;
            case R.id.person_label_def_location_fl:
                //默认位置

                break;
            case R.id.person_chioce_label_refresh:
               //换一批标签

                break;
            case R.id.person_label_modify:
               //修改

                break;

        }
    }
}
