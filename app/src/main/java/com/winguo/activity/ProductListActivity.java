package com.winguo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.winguo.base.BaseTitleActivity;
import com.winguo.net.GlideUtil;
import com.winguo.productList.controller.ProductListController;
import com.winguo.productList.modle.ItemEntitys;
import com.winguo.productList.bean.ProductListBean;
import com.winguo.productList.bean.CityItemBean;
import com.winguo.productList.bean.ProvinceCityBean;
import com.winguo.productList.bean.ProvinceItemBean;
import com.winguo.productList.bean.ClassifyName;
import com.winguo.productList.bean.FiltersItemBean;
import com.winguo.productList.bean.ValueEntity;
import com.winguo.productList.view.IProductListView;
import com.winguo.productList.view.ProvinceCityWindow;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.R;
import com.winguo.view.CityPicker;
import com.winguo.view.CustomGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表
 * Created by Administrator on 2016/12/8.
 */

public class ProductListActivity extends BaseTitleActivity implements IProductListView ,View.OnClickListener{
    private TextView tv_synthesize;
    private TextView tv_sales_volume;
    private TextView tv_price;
    private ListView lv_product_list;
    private FrameLayout fl_product_container;
    private ImageView iv_product_to_top;
    private View emptyView;
    private View errorView;
    //获取数据的页码数
    private int page = 1;
    //排序方式,默认/价格/销量
    private String orders = "m_item_id";//默认
    //排序,默认是降序
    private String sort = "desc";
    //判断点击的是否还是已经加载的/default/sale/price
    private String isState = "default";
    //还有没有更多数据
    private boolean hasMore = true;
    //是否正在加载更多
    private boolean isLoadingMore = false;
    //返回数据的状态
    private static final int LOADING_STATE = 1;
    private static final int EMPTY_STATE = 2;
    private static final int ERROR_STATE = 3;
    private static final int SUCCESS_STATE = 4;
    //返回数据的状态
    private int STATE = LOADING_STATE;
    //存放listView数据源数据集合
    private List<ItemEntitys> productList;
    private View successView;
    private ViewGroup.LayoutParams params;
    private DrawerLayout drawerLayout;
    private ProductListAdapter adapter;
    private LinearLayout ll_has_more;
    private TextView no_has_more;
    private ProductListController controller;
    //要搜索的关键字
    private String text;
    private LinearLayout ll_right_menu;
    private ImageView iv_price;
    private RelativeLayout ll_default;
    private RelativeLayout ll_sales_volume;
    private RelativeLayout ll_price;
    private ProductListOnItemListener onItemListener;
    private ProductListOnScrollListener productListOnScrollListener;
    private int mCategorieId;
    private FrameLayout fl_drawer_layout_container;
    private EditText et_low_price;
    private EditText et_hight_price;
    private TextView tv_product_screen_province;
    private TextView tv_product_screen_city;
    private CustomGridView product_screen_grid_view;
    //筛选中数据
    private String mLowPrice = null;//最低价
    private String mHightPrice = null;//最高价
    private int mProvinceCode = 0;//省
    private int mCityCode = 0;//市
    private String mProvince = "";//省
    private String mCity = "";//市
    private String mClassifyName = null;//分类名称
    private int mProsition = -1;//分类最后确定的位置
    private boolean isLowPriceChanged = false;//筛选低价格是否有变化
    private boolean isHightPriceChanged = false;//筛选高价格是否有变化
    private boolean isLocationChanged = false;//筛选地址是否有变化
    private boolean isClassifyChanged = false;//筛选分类是否有变化

    private String lowPrice = null;//填写的最低价格
    private String hightPrice = null;//填写的最高价格
    private int provinceCode;//选中的省
    private int cityCode;//选中的市
    private String classifyName;//选中的分类名称
    private int lastProsition = -1;//选中的分类位置

    private View screenView;
    private ProvinceCityBean provinceCityBean;
    private TextView tv_screen_reset_btn;
    private TextView tv_screen_confirm_btn;
    private GridViewAdapter gridViewAdapter;
    private List<ValueEntity> values = new ArrayList<>();
    private List<ValueEntity> commonValues = new ArrayList<>();
    private final int SEAARCH_CHANGED = 88;
    private boolean hasMoreNotNet = false;
    private TextView tv_product_screen_btn;
    private ImageView iv_product_screen_btn;
    private Button btn_request_net;
    private RelativeLayout ll_product_screen_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setBackBtn();
        initViews();
        initListener();
    }

    private void initViews() {
        //创建数据源集合
        productList = new ArrayList<>();
        //获取外部传过来的数据
        Intent intent = getIntent();
        //从搜索的界面进来的
        text = intent.getStringExtra("text");
        //从分类的界面进来的
        mCategorieId = intent.getIntExtra(ActionUtil.ACTION_CLASSIFY_ID, -1);
        if (mCategorieId == -1) {
//            tv_search_text.setText(text);
        }
        //初始化控件
        initView();
        //判断网络状态
        showNetState();

        showView();



        //先显示加载中showView()-->请求网络,返回数据判断(load()-->getState()-->选择显示的界面showView()-->有数据的展示)
        //如果是加载失败,点击再次加载,先改变状态为加载中,再执行上面的 逻辑
    }

    /**
     * 根据网络状态显示界面
     */
    private void showNetState() {
        //切换显示状态
        if (NetWorkUtil.isNetworkAvailable(CommonUtil.getAppContext()) ||
                NetWorkUtil.isWifiConnected(CommonUtil.getAppContext())) {
            //初始化回调数据
            //城市的数据获取
            controller = new ProductListController(this);
//            SVProgressHUD.showWithStatus(this,"加载中...");
            LoadDialog.show(ProductListActivity.this);
            controller.getProvinceCityData();

        } else {
            //如果网络和WiFi都没有，就加载无网络
            //没有网络
            STATE = ERROR_STATE;
            //显示view
            showView();
        }
    }

    /**
     * 省市数据
     *
     * @param provinceCityBean
     */
    @Override
    public void getProvinceCity(ProvinceCityBean provinceCityBean) {
        if (provinceCityBean != null) {
            this.provinceCityBean = provinceCityBean;
            CommonUtil.printI("请求的省市数据+++++++++", provinceCityBean.toString());
            CityPicker.getaddressinfo(provinceCityBean);
            firstLoadingData(text, orders, sort, mCategorieId);
        } else {
//            SVProgressHUD.dismiss(ProductListActivity.this);
            LoadDialog.dismiss(ProductListActivity.this);
            STATE = ERROR_STATE;
            //显示view
            showView();
        }

    }

    /**
     * 分类名称数据
     *
     * @param classifyName
     */
    @Override
    public void getClassifyName(ClassifyName classifyName) {
        if (classifyName == null) {
            //没有网络
            STATE = ERROR_STATE;
            //显示view
            LoadDialog.dismiss(ProductListActivity.this);
            showView();
            return;
        }
        values.clear();
        commonValues.clear();
        if (classifyName.search != null) {
            List<FiltersItemBean> filtersItems = classifyName.search.filters.item;
            String name = "cname";
            for (FiltersItemBean filtersItemBean : filtersItems) {
                List<ValueEntity> value=filtersItemBean.values.value;
                if (name.equals(filtersItemBean.code)) {
                    //说明是分类名称
                    for (ValueEntity valueEntity:value){
                        if (!("".equals(valueEntity.label))){
                            commonValues.add(valueEntity);
                        }
                    }
                }
            }
            values.addAll(commonValues);
        }
        gridViewAdapter = new GridViewAdapter(values);
        loadingProductListData(text, orders, sort, mCategorieId);
    }

    /**
     * 第一次请求网络数据
     *
     * @param sort
     * @param text 传入要请求的关键字
     */
    private void firstLoadingData(String text, String orders, String sort, int categorieId) {
        if (controller != null)
            controller.getClassifyNameData(text, orders, sort, categorieId);
    }

    /**
     * 请求商品列表数据
     *
     * @param text
     * @param orders
     * @param sort
     * @param categorieId
     */
    private void loadingProductListData(String text, String orders, String sort, int categorieId) {
        CommonUtil.printI("修改后的请求++++++++++++", mLowPrice + "," +
                "," + mHightPrice + "," + mProvinceCode + "," + mCityCode + "," + classifyName);
        controller.getData(ProductListActivity.this, text, page, orders, sort, categorieId, mLowPrice, mHightPrice, mProvinceCode, mCityCode, mClassifyName);
    }

    //获取网络数据
    @Override
    public void getProductList(ProductListBean productListBeen) {
        //        //调用这个方法要先清除之前的数据
        //        productList.clear();
        //判断是否有数据
        CommonUtil.printI("回调函数返回到 结果:=", productListBeen.toString());
//        SVProgressHUD.dismiss(ProductListActivity.this);
        LoadDialog.dismiss(ProductListActivity.this);
        if (productListBeen == null) {
            //没有网络
            STATE = ERROR_STATE;
            //显示view
            showView();
            return;
        }
        if (productListBeen.search != null) {
            //有网络
            if (productListBeen.search.products != null) {

                //判断状态
                getState(productListBeen.search.products.item);//productListBeen.search.products.item
                //给listView的数据源数据
                productList.clear();
                productList.addAll(productListBeen.search.products.item);
                //判断是否有更多数据
                int hasMorecount = productListBeen.search.has_more_items.get(0);//集合中的两个值,取其中的任何一个都行
                CommonUtil.printI("还有没有更多的值:+++++=", hasMorecount + "");
                setHasMore(hasMorecount);
                CommonUtil.printI("还有没有更多hasMore:+++++=", hasMore + "");
                //显示view
                showView();
            } else {
                //没有数据
                STATE = EMPTY_STATE;
                //显示view
                showView();
            }
        }
    }

    //当加载更多时,获取更多数据返回
    @Override
    public void getMoreProductList(ProductListBean productListBeen) {
        if (productListBeen == null) {
            ll_has_more.setVisibility(View.GONE);
            no_has_more.setVisibility(View.VISIBLE);
            no_has_more.setText(getString(R.string.request_net2));
            hasMoreNotNet = true;
            return;
        }

        int hasMorecount = productListBeen.search.has_more_items.get(0);//集合中的两个值,取其中的任何一个都行
        if (hasMore) {
            //没有在加载更多
            isLoadingMore = false;
            //有数据
            productList.addAll(productListBeen.search.products.item);
            adapter.notifyDataSetChanged();
        }
        setHasMore(hasMorecount);
    }

    /**
     * 判断是否有更多数据
     *
     * @param hasMorecount
     */
    private void setHasMore(int hasMorecount) {
        if (hasMorecount == 0) {
            //没有更多数据
            hasMore = false;
            ll_has_more.setVisibility(View.GONE);
            no_has_more.setVisibility(View.VISIBLE);
        } else if (hasMorecount == 1) {
            //有更多数据
            hasMore = true;
            ll_has_more.setVisibility(View.VISIBLE);
            no_has_more.setVisibility(View.GONE);
        }
    }

    private void initListener() {

        ll_product_screen_btn.setOnClickListener(this);
        //综合排序
        ll_default.setOnClickListener(this);
        //销量排序
        ll_sales_volume.setOnClickListener(this);
        //价格排序
        ll_price.setOnClickListener(this);
        //快速返回开始
        iv_product_to_top.setOnClickListener(this);
        //条目点击事件
        onItemListener = new ProductListOnItemListener();

        //无网络点击按钮 刷新并隐藏
        btn_request_net.setOnClickListener(this);

        //listView的滑动监听
        productListOnScrollListener = new ProductListOnScrollListener();
        //筛选中省市的点击事件
        tv_product_screen_province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvinceCityWindow provinceCityWindow = new ProvinceCityWindow(ProductListActivity.this, handler);
                provinceCityWindow.showAtLocation(screenView, Gravity.BOTTOM, 0, 0);
            }
        });
        tv_product_screen_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvinceCityWindow provinceCityWindow = new ProvinceCityWindow(ProductListActivity.this, handler);
                provinceCityWindow.showAtLocation(screenView, Gravity.BOTTOM, 0, 0);
            }
        });
        //筛选中重置和确定的点击事件
        tv_screen_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();

            }
        });
        tv_screen_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPrice();
                screenLocation();
                screenClassifyName();
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
    }

    /**
     * 筛选后点击确定分类处理
     */
    private void screenClassifyName() {
        //处理分类
        if (mClassifyName == null) {
            if (classifyName != null) {
                mClassifyName = classifyName;
                isClassifyChanged = true;
            }
        } else {
            if (!(mClassifyName.equals(classifyName))) {
                mClassifyName = classifyName;
                isClassifyChanged = true;
            }
        }
        if (isLowPriceChanged || isHightPriceChanged || isLocationChanged || isClassifyChanged) {
            page=0;
            handler.sendEmptyMessage(SEAARCH_CHANGED);
        }
    }

    /**
     * 筛选后点击确定地址处理
     */
    private void screenLocation() {
        //处理地址
        if (provinceCode != 0) {
            if (mProvinceCode == 0) {
                mProvinceCode = provinceCode;
                mCityCode = cityCode;
                isLocationChanged = true;
            } else {
                if (mProvinceCode != provinceCode) {
                    //省份改变
                    mProvinceCode = provinceCode;
                    mCityCode = cityCode;
                    isLocationChanged = true;
                } else {
                    //省份没有变,看市
                    if (cityCode != 0) {
                        if (mCityCode == 0) {
                            mCityCode = cityCode;
                            isLocationChanged = true;
                        } else {
                            if (mCityCode != cityCode) {
                                mCityCode = cityCode;
                                isLocationChanged = true;
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 筛选后点击确定价格处理
     */
    private void screenPrice() {
        //获取输入框中数据
        lowPrice = et_low_price.getText().toString().trim();
        hightPrice = et_hight_price.getText().toString().trim();
        CommonUtil.printI("被修改后的最高价格++++++", hightPrice + "");
        //处理价格
        if (lowPrice != null) {
            if (mLowPrice == null) {
                //没有被赋值过
                mLowPrice = lowPrice;
                isLowPriceChanged = true;
            } else {
                if (!(mLowPrice.equals(lowPrice))) {
                    mLowPrice = lowPrice;
                    isLowPriceChanged = true;
                }
            }
        } else {
            if (mLowPrice != null) {
                mLowPrice = lowPrice;
                isLowPriceChanged = true;
            }
        }
        if (hightPrice != null) {
            if (mHightPrice == null) {
                //没有被赋值过
                mHightPrice = hightPrice;
                isHightPriceChanged = true;
            } else {
                if (!(mHightPrice.equals(hightPrice))) {
                    mHightPrice = hightPrice;
                    isHightPriceChanged = true;
                }
            }
        } else {
            if (mHightPrice != null) {
                mHightPrice = hightPrice;
                isHightPriceChanged = true;
            }
        }
    }

    /**
     * 点击重置
     */
    private void reset() {
        //清空临时的数据
        et_low_price.setText("");
        et_hight_price.setText("");
        tv_product_screen_province.setText("");
        tv_product_screen_city.setText("");
        provinceCode = 0;
        cityCode = 0;
        mProvince="";
        mCity="";
        lowPrice = null;
        hightPrice = null;
        //清空要请求网络的数据
        mProvinceCode = 0;
        mCityCode = 0;
        classifyName = null;
        if (values.size() > 0 && lastProsition != -1) {
            values.get(lastProsition).isChoosed = false;
            lastProsition = -1;
            classifyName = null;
        }
        if (gridViewAdapter != null) {
            gridViewAdapter.notifyDataSetChanged();
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String sheng = bundle.getString("sheng");
            String shi = bundle.getString("shi");
            tv_product_screen_province.setText(sheng);
            tv_product_screen_city.setText(shi);
            if (provinceCityBean != null) {
                List<ProvinceItemBean> provinceItem = provinceCityBean.province.item;
                for (ProvinceItemBean provinceItemBean : provinceItem) {
                    if (provinceItemBean.name.equals(sheng)) {
                        provinceCode = provinceItemBean.code;
                        mProvince=provinceItemBean.name;
                        List<CityItemBean> cityItem = provinceItemBean.cities.item;
                        for (CityItemBean cityItemBean : cityItem) {
                            if (cityItemBean.name.equals(shi)) {
                                cityCode = cityItemBean.code;
                                mCity=cityItemBean.name;
                            }
                        }
                    }
                }
            }
            if (msg.what == SEAARCH_CHANGED) {
//                SVProgressHUD.showWithStatus(ProductListActivity.this,"加载中...");
                LoadDialog.show(ProductListActivity.this);
                loadingProductListData(text, orders, sort, mCategorieId);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_request_net:
                //无网络 点击刷新 并隐藏
                errorView.setVisibility(View.GONE);
                showNetState();
                break;
            case R.id.ll_product_screen_btn:
                if (!("screen".equals(isState))) {
                    isState = "screen";
                    tv_product_screen_btn.setTextColor(getResources().getColor(R.color.selected_color));
                    iv_product_screen_btn.setSelected(true);
                    tv_synthesize.setTextColor(getResources().getColor(R.color.default_color));
                    tv_sales_volume.setTextColor(getResources().getColor(R.color.default_color));
                    tv_price.setTextColor(getResources().getColor(R.color.default_color));
                    iv_price.setSelected(false);
                    iv_price.setImageResource(R.drawable.press_default);

                }
                //筛选
                drawerLayout.openDrawer(Gravity.RIGHT);
                tv_product_screen_province.setText(mProvince);
                tv_product_screen_city.setText(mCity);
                //处理筛选中的数据和事件
                dealWithScreen();

                break;
            case R.id.ll_default:
                if (!("default".equals(isState))) {
                    isState = "default";
                    tv_synthesize.setTextColor(getResources().getColor(R.color.selected_color));
                    tv_sales_volume.setTextColor(getResources().getColor(R.color.default_color));
                    tv_price.setTextColor(getResources().getColor(R.color.default_color));
                    iv_price.setSelected(false);
                    iv_price.setImageResource(R.drawable.press_default);
                    tv_product_screen_btn.setTextColor(getResources().getColor(R.color.default_color));
                    iv_product_screen_btn.setSelected(false);
                    sort = "desc";
                    //综合排序
                    STATE = LOADING_STATE;
                    showView();
                    page = 1;
                    orders = "m_item_id";
//                    SVProgressHUD.showWithStatus(ProductListActivity.this,"加载中...");
                    LoadDialog.show(ProductListActivity.this);
                    firstLoadingData(text, orders, sort, mCategorieId);
                }
                break;
            case R.id.ll_sales_volume:
                if (!("sale".equals(isState))) {
                    isState = "sale";
                    tv_synthesize.setTextColor(getResources().getColor(R.color.default_color));
                    tv_sales_volume.setTextColor(getResources().getColor(R.color.selected_color));
                    tv_price.setTextColor(getResources().getColor(R.color.default_color));
                    iv_price.setSelected(false);
                    iv_price.setImageResource(R.drawable.press_default);
                    tv_product_screen_btn.setTextColor(getResources().getColor(R.color.default_color));
                    iv_product_screen_btn.setSelected(false);
                    sort = "desc";
                    //销量排序
                    STATE = LOADING_STATE;
                    showView();
                    page = 1;
                    orders = "sale_qty";
//                    SVProgressHUD.showWithStatus(ProductListActivity.this,"加载中...");
                    LoadDialog.show(ProductListActivity.this);
                    firstLoadingData(text, orders, sort, mCategorieId);

                }
                break;
            case R.id.ll_price:
                isState = "price";
                tv_synthesize.setTextColor(getResources().getColor(R.color.default_color));
                tv_sales_volume.setTextColor(getResources().getColor(R.color.default_color));
                tv_price.setTextColor(getResources().getColor(R.color.selected_color));
                iv_price.setSelected(true);
                tv_product_screen_btn.setTextColor(getResources().getColor(R.color.default_color));
                iv_product_screen_btn.setSelected(false);
                //价格排序
                STATE = LOADING_STATE;
                showView();
                page = 1;
                orders = "price";
                if ("desc".equals(sort)) {
                    sort = "asc";
                    iv_price.setImageResource(R.drawable.press_asc_2);
                } else {
                    sort = "desc";
                    iv_price.setImageResource(R.drawable.press_desc_2);
                }
//                SVProgressHUD.showWithStatus(ProductListActivity.this,"加载中...");
                LoadDialog.show(ProductListActivity.this);
                firstLoadingData(text, orders, sort, mCategorieId);
                break;
            case R.id.iv_product_to_top:
                //快速返回开始列表最顶端
                break;
        }

    }

    /**
     * 点击筛选按钮
     */
    private void dealWithScreen() {
        isLowPriceChanged = false;
        isHightPriceChanged = false;
        isLocationChanged = false;
        isClassifyChanged = false;

    }



    /**
     * 筛选中分类的适配器
     */
    class GridViewAdapter extends BaseAdapter {
        private List<ValueEntity> values;

        public GridViewAdapter(List<ValueEntity> values) {
            this.values = values;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (values != null) {
                count = values.size();
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
            ClassifyViewHolder holder;
            if (convertView == null) {
                holder = new ClassifyViewHolder();
                convertView = View.inflate(ProductListActivity.this, R.layout.classify_name_item, null);
                holder.tv_classify_name_item = (TextView) convertView.findViewById(R.id.tv_classify_name_item);
                convertView.setTag(holder);
            } else {
                holder = (ClassifyViewHolder) convertView.getTag();
            }

            if (values != null) {
                ValueEntity valueEntity = values.get(position);
                holder.tv_classify_name_item.setText(valueEntity.label);
                if (valueEntity.isChoosed) {
                    //被选中
                    holder.tv_classify_name_item.setBackgroundResource(R.drawable.screen_confirm_btn_bg);
                    holder.tv_classify_name_item.setTextColor(getResources().getColor(R.color.tv_screen_confirm_text_color));
                    classifyName = valueEntity.label;
                } else {
                    holder.tv_classify_name_item.setBackgroundResource(R.drawable.edit_text_bg);
                    holder.tv_classify_name_item.setTextColor(getResources().getColor(R.color.product_screen_title_text_color));
                }
            }
            return convertView;
        }
    }

    class ClassifyViewHolder {
        TextView tv_classify_name_item;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(ll_right_menu)) {
            //抽屉是打开的
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else
            super.onBackPressed();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ll_product_screen_btn = (RelativeLayout) findViewById(R.id.ll_product_screen_btn);
        //筛选布局
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fl_drawer_layout_container = (FrameLayout) findViewById(R.id.fl_drawer_layout_container);
        screenView = View.inflate(ProductListActivity.this, R.layout.product_list_screen_view, null);
        et_low_price = (EditText) screenView.findViewById(R.id.et_low_price);
        et_hight_price = (EditText) screenView.findViewById(R.id.et_hight_price);
        tv_product_screen_province = (TextView) screenView.findViewById(R.id.tv_product_screen_province);
        tv_product_screen_city = (TextView) screenView.findViewById(R.id.tv_product_screen_city);
        product_screen_grid_view = (CustomGridView) screenView.findViewById(R.id.product_screen_grid_view);
        tv_screen_reset_btn = (TextView) screenView.findViewById(R.id.tv_screen_reset_btn);
        tv_screen_confirm_btn = (TextView) screenView.findViewById(R.id.tv_screen_confirm_btn);
        fl_drawer_layout_container.addView(screenView);
        //顶部布局
        ll_default = (RelativeLayout) findViewById(R.id.ll_default);
        ll_sales_volume = (RelativeLayout) findViewById(R.id.ll_sales_volume);
        ll_price = (RelativeLayout) findViewById(R.id.ll_price);
        tv_synthesize = (TextView) findViewById(R.id.tv_synthesize);
        tv_sales_volume = (TextView) findViewById(R.id.tv_sales_volume);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_product_screen_btn = (TextView) findViewById(R.id.tv_product_screen_btn);

        iv_price = (ImageView) findViewById(R.id.iv_price);
        iv_product_screen_btn = (ImageView) findViewById(R.id.iv_product_screen_btn);

        //默认的选中默认
        tv_synthesize.setTextColor(getResources().getColor(R.color.selected_color));

        fl_product_container = (FrameLayout) findViewById(R.id.fl_product_container);

        iv_product_to_top = (ImageView) findViewById(R.id.iv_product_to_top);
        //侧滑菜单的控件
        ll_right_menu = (LinearLayout) findViewById(R.id.ll_product_list_right_menu);
        //设置侧滑菜单的大小
        int windowWight = ScreenUtil.getScreenWidth(ProductListActivity.this);
        int wimdowHight = ScreenUtil.getScreenHeight(ProductListActivity.this);
        ViewGroup.LayoutParams right_menu_params = ll_right_menu.getLayoutParams();
        right_menu_params.width = windowWight * 3 / 4;
        ll_right_menu.setLayoutParams(right_menu_params);

        //创建不同的视图
        params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //1、返回数据为空

        emptyView = View.inflate(ProductListActivity.this, R.layout.loading_empty, null);
        TextView textView = (TextView) emptyView.findViewById(R.id.empty_data_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_data);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        textView.setText(getString(R.string.loading_empty));
        fl_product_container.addView(emptyView, params);


        //2、没有网络或请求失败

        errorView = View.inflate(ProductListActivity.this, R.layout.no_net, null);
        TextView no_net_tv = (TextView) errorView.findViewById(R.id.no_net_tv);
        btn_request_net = (Button) errorView.findViewById(R.id.btn_request_net);
        Drawable drawableTop1 = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop1, null, null);
        fl_product_container.addView(errorView, params);


        //3、返回数据不为空,再创建成功的界面
        successView = View.inflate(ProductListActivity.this, R.layout.list_view, null);
        lv_product_list = (ListView) successView.findViewById(R.id.lv_product_list);
        //给listView添加脚布局
        View moreView = View.inflate(ProductListActivity.this, R.layout.loading_more_view, null);
        moreView.findViewById(R.id.no_more_line_view).setVisibility(View.VISIBLE);
        ll_has_more = (LinearLayout) moreView.findViewById(R.id.ll_has_more);
        ImageView progressBar = (ImageView) moreView.findViewById(R.id.progressBar);
        Glide.with(this).load(R.drawable.loading_more_bg).into(progressBar);
        no_has_more = (TextView) moreView.findViewById(R.id.no_has_more);
        lv_product_list.addFooterView(moreView);
    }

    /**
     * 根据返回数据改变显示的view
     */
    private void showView() {
        if (emptyView != null) {
            emptyView.setVisibility(STATE == EMPTY_STATE ? View.VISIBLE : View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(STATE == ERROR_STATE ? View.VISIBLE : View.GONE);
        }
        if (STATE == SUCCESS_STATE) {
            if (successView != null) {
                createSuccessView();
                successView.setVisibility(View.VISIBLE);
            }
            loadSuccessData();
        } else {
            successView.setVisibility(View.GONE);
        }
    }

    /**
     * 创建数据返回成功的界面
     *
     * @return
     */
    private void createSuccessView() {
        fl_product_container.removeView(successView);
        fl_product_container.addView(successView, params);
    }

    /**
     * 展示数据到控件上
     */
    private void loadSuccessData() {
        //设置适配器
        if (adapter == null) {
            adapter = new ProductListAdapter();
        }
        lv_product_list.setAdapter(adapter);
        lv_product_list.setOnItemClickListener(onItemListener);
        lv_product_list.setOnScrollListener(productListOnScrollListener);
        //筛选中分类名称
        product_screen_grid_view.setAdapter(gridViewAdapter);
        product_screen_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastProsition >= 0) {
                    values.get(lastProsition).isChoosed = false;
                }
                ValueEntity valueEntity = values.get(position);
                valueEntity.isChoosed = true;
                lastProsition = position;
                gridViewAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 返回状态来更新显示的界面
     *
     * @param datas
     * @return
     */
    private void getState(List datas) {

        if (datas.size() == 0) {
            //数据为空
            STATE = EMPTY_STATE;
        } else {
            //有数据
            STATE = SUCCESS_STATE;

        }

    }

    private class ProductListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int count = 0;
            if (productList != null) {
                count = productList.size();
            }
            return count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(ProductListActivity.this, R.layout.product_list_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_product_list_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_product_list_name);
                holder.tv_product_list_price = (TextView) convertView.findViewById(R.id.tv_product_list_price);
                holder.product_list_is_collect = (ImageView) convertView.findViewById(R.id.product_list_is_collect);
                holder.tv_sales_volume = (TextView) convertView.findViewById(R.id.tv_product_list_sales_volume);
                holder.tv_location = (TextView) convertView.findViewById(R.id.tv_product_list_location);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //给控件赋值
            ItemEntitys itemEntity = productList.get(position);
            //处理收藏
//            if (SPUtils.contains(ProductListActivity.this, "accountName")){
//                //没有登录就隐藏这个按钮
//                holder.product_list_is_collect.setVisibility(View.VISIBLE);
//                holder.product_list_is_collect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.product_list_is_collect.setClickable(false);
//                        LoadDialog.show(this);
//                        //取消商品收藏
//                        if (mIs_collect == 1) {
//                            if (NetWorkUtil.isNetworkAvailable(this)) {
//                                CollectHandler.deleteGoodsCollect(this, handler, gid);
//                            } else {
//                                LoadDialog.dismiss(ProductActivity.this);
//                                ToastUtil.show(this, getString(R.string.timeout));
//                            }
//                        } else {
//                            //添加商品收藏
//                            if (NetWorkUtil.isNetworkAvailable(this)) {
//                                CollectHandler.addProductCollect(this, handler, gid);
//                            } else {
//                                LoadDialog.dismiss(ProductActivity.this);
//                                ToastUtil.show(this, getString(R.string.timeout));
//                            }
//                        }
//                    }
//                });
//            }else{
//                //没有登录就隐藏这个按钮
//                holder.product_list_is_collect.setVisibility(View.GONE);
//            }
            String iconUrl = itemEntity.icon.content;
            GlideUtil.getInstance().loadImage(ProductListActivity.this, iconUrl, R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, holder.iv_icon);
            holder.tv_name.setText(itemEntity.name);
                if (TextUtils.isEmpty(itemEntity.price.special)) {
                    holder.tv_product_list_price.setText(String.format(getString(R.string.product_list_price), itemEntity.price.regular));
                }else{
                    holder.tv_product_list_price.setText(String.format(getString(R.string.product_list_price), itemEntity.price.special));
                }

            holder.tv_sales_volume.setText(String.format(getString(R.string.product_list_sales_volume),itemEntity.sale_qty));
            holder.tv_location.setText(String.format(getString(R.string.product_list_product_location),itemEntity.cityname));
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv_icon, product_list_is_collect;
        TextView tv_name, tv_product_list_price,tv_sales_volume,tv_location;
    }


    private class ProductListOnScrollListener implements AbsListView.OnScrollListener {
        int totalItemCount;
        int lastVisibleItem;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
            //当用户看到最后一个条目时,加载更多
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (totalItemCount == lastVisibleItem && hasMore && !isLoadingMore) {//看到最后一个条目并且有更多数据
                    isLoadingMore = true;
                    if (NetWorkUtil.noHaveNet(ProductListActivity.this)) return;
                    loadMoreData();
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.totalItemCount = totalItemCount;
            this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        }
    }

    /**
     * 获取更多数据
     */
    private void loadMoreData() {
        page += 1;
        if (controller != null)
            controller.getMoreData(ProductListActivity.this, text, page, orders, sort, mLowPrice, mHightPrice, mProvinceCode, mCityCode, mClassifyName, mCategorieId);

    }

    //条目点击事件
    private class ProductListOnItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            //脚布局不能点击
            if (position > productList.size() - 1) {
                if (hasMoreNotNet) {
                    hasMoreNotNet = false;
                    ll_has_more.setVisibility(View.VISIBLE);
                    no_has_more.setText(getString(R.string.no_has_more_text));
                    no_has_more.setVisibility(View.GONE);
                    if (controller != null)
                        controller.getMoreData(ProductListActivity.this, text, page, orders, sort, mLowPrice, mHightPrice, mProvinceCode, mCityCode, mClassifyName, mCategorieId);
                }
            } else {
                ItemEntitys itemEntity = productList.get(position);
                Intent productActivity = new Intent(ProductListActivity.this, ProductActivity.class);
                productActivity.putExtra("gid", itemEntity.entity_id);
                startActivity(productActivity);
            }
        }
    }

}
