package com.winguo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.cloud.InitListener;
import com.winguo.R;
import com.winguo.base.BaseTitleActivity;
import com.winguo.net.GlideUtil;
import com.winguo.theme.controller.ThemeController;
import com.winguo.theme.modle.GoodsThemesBean;
import com.winguo.theme.modle.ResultBean;
import com.winguo.theme.view.IGoodsThemeView;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by Admin on 2017/5/12.
 * 首页 主题活动 内页
 */

public class ThemeActivity extends BaseTitleActivity implements View.OnClickListener,IGoodsThemeView{

    private ListView lv_theme;
    private TextView one_theme_title;
    private TextView two_theme_title;
    private FrameLayout shop_theme_container;
    private Button btn_request_net;
    private View noNetView;
    private View noDataView;
    private View themeView;
    private ThemeController themeController;
    private ThemeAdapter adapter;
    private List<ResultBean> mResult;
    private String cate_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        setBackBtn();
        Intent intent = getIntent();
        String oneTheme = intent.getStringExtra("oneTheme");
        String twoTheme = intent.getStringExtra("twoTheme");
        cate_id = intent.getStringExtra("cate_id");
        shop_theme_container = (FrameLayout) findViewById(R.id.shop_theme_container);
        //没有网络
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        shop_theme_container.addView(noNetView);
        //没有数据
        noDataView = View.inflate(this, R.layout.loading_empty, null);
        TextView textView = (TextView) noDataView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.loading_empty));
        shop_theme_container.addView(noDataView);
        //有数据
        themeView = View.inflate(this, R.layout.shop_theme_layout, null);
        lv_theme = (ListView) themeView.findViewById(R.id.lv_theme);
        View themeHeadView = View.inflate(this, R.layout.shop_theme_head_layout, null);
        one_theme_title = (TextView) themeHeadView.findViewById(R.id.one_theme_title);
        two_theme_title = (TextView) themeHeadView.findViewById(R.id.two_theme_title);
        lv_theme.addHeaderView(themeHeadView);
        shop_theme_container.addView(themeView);
        noNetView.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
        themeView.setVisibility(View.GONE);
        adapter = new ThemeAdapter();
        themeController = new ThemeController(this);
        one_theme_title.setText(oneTheme);
        two_theme_title.setText(twoTheme);
        init();
        initListener();
    }

    private void initListener() {
        btn_request_net.setOnClickListener(this);
        lv_theme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position-1)>=0) {
                    ResultBean resultBean = mResult.get(position - 1);
                    Intent productIntent = new Intent(ThemeActivity.this, ProductActivity.class);
                    productIntent.putExtra("gid", Integer.valueOf(resultBean.m_topics_goods_id));
                    CommonUtil.printI("传递过去的商品id", resultBean.m_topics_goods_id);
                    startActivity(productIntent);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_request_net:
                noNetView.setVisibility(View.GONE);
                init();
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void init() {

        if (NetWorkUtil.noHaveNet(this)){
            //没有网络
            noNetView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
            themeView.setVisibility(View.GONE);
            return;
        }
        LoadDialog.show(this);
        //请求数据
        if (themeController!=null){
            themeController.getData(this,cate_id);
        }
    }

    /**
     * 请求网络返回的数据
     * @param goodsThemesBean theme数据
     */
    @Override
    public void GoodsThemeData(GoodsThemesBean goodsThemesBean) {
        LoadDialog.dismiss(this);
        if (goodsThemesBean==null){
            //没有网络
            noNetView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
            themeView.setVisibility(View.GONE);
        }else{
            String code = goodsThemesBean.Code;
            if ("-1".equals(code)){
                //没有数据
                noDataView.setVisibility(View.VISIBLE);
                noNetView.setVisibility(View.GONE);
                themeView.setVisibility(View.GONE);
            }else if ("0".equals(code)){
                //有数据
                themeView.setVisibility(View.VISIBLE);
                noNetView.setVisibility(View.GONE);
                noDataView.setVisibility(View.GONE);
                mResult = goodsThemesBean.Result;
                lv_theme.setAdapter(adapter);
            }
        }
    }



    class  ThemeAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            int count=0;
            if (mResult!=null){
                count=mResult.size();
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
            if (convertView==null){
                convertView=View.inflate(ThemeActivity.this,R.layout.theme_list_item,null);
                holder=new ViewHolder();
                holder.theme_iv= (ImageView) convertView.findViewById(R.id.theme_iv);
                holder.theme_item_text= (TextView) convertView.findViewById(R.id.theme_item_text);
                holder.theme_item_price= (TextView) convertView.findViewById(R.id.theme_item_price);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            ResultBean resultBean = mResult.get(position);
            holder.theme_item_text.setText(resultBean.m_topics_title);
            holder.theme_item_price.setText(String.format(getString(R.string.product_price),resultBean.m_topics_ext));
            GlideUtil.getInstance().loadImage(ThemeActivity.this,resultBean.m_topics_thumb_url,
                    R.drawable.column_theme_loading_bg,R.drawable.column_theme_error_bg,holder.theme_iv);
            return convertView;
        }
        class ViewHolder {
            ImageView theme_iv;
            TextView theme_item_text,theme_item_price;
        }
    }



}
