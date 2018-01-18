package com.winguo.classify;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.activity.ProductListActivity;
import com.winguo.activity.Search2Activity;
import com.winguo.activity.SearchActivity;
import com.winguo.net.GlideUtil;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author hcpai
 * @desc 商品分类
 */

public class ClassifyActivity extends BaseActivity2 {
    private View mSuccessView;
    private EditText classify_et;
    private FrameLayout cart_back_btn;
    private ExpandableListView mExpandableListView;
    /**
     * 控制列表的展开
     */
    private int sign;
    private ArrayList<ClassifySecondBean> mClassifySecondBeans;
    private ClassifyFirstBean mClassifyFirstBean;
    /**
     * 无网络
     */
    private View noNetView;
    private Button btn_request_net;
    //记录一级分类的数量
    private int count = 0;

    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_classify;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        CommonUtil.stateSetting(this, R.color.white_top_color);
        classify_et = (EditText) findViewById(R.id.classify_et);
        cart_back_btn = (FrameLayout) findViewById(R.id.cart_back_btn);
        classify_et.setFocusable(false);
        FrameLayout classify_container = (FrameLayout) findViewById(R.id.classify_container);
        //给容器添加布局
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //有数据
        mSuccessView = View.inflate(this, R.layout.classify_expandable, null);
        mExpandableListView = (ExpandableListView) mSuccessView.findViewById(R.id.classify_expandable_list);
        classify_container.addView(mSuccessView, params);
        //无网络
        noNetView = View.inflate(this, R.layout.no_net, null);
        btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
        TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
        Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
        no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        classify_container.addView(noNetView);
        LoadDialog.show(this);
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getDataByNetStatus();
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        cart_back_btn.setOnClickListener(this);
        //点击重新加载
        btn_request_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataByNetStatus();
            }
        });
        classify_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入搜索页面
                Intent intent = new Intent(ClassifyActivity.this, Search2Activity.class);
                startActivity(intent);
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                //判断是否展开
                if (sign == -1) {
                    // 展开被选的group
                    mExpandableListView.expandGroup(groupPosition);
                    // 设置被选中的group置于顶端
                    mExpandableListView.setSelectedGroup(groupPosition);
                    sign = groupPosition;
                } else if (sign == groupPosition) {
                    mExpandableListView.collapseGroup(sign);
                    sign = -1;
                } else {
                    mExpandableListView.collapseGroup(sign);
                    // 展开被选的group
                    mExpandableListView.expandGroup(groupPosition);
                    // 设置被选中的group置于顶端
                    mExpandableListView.setSelectedGroup(groupPosition);
                    sign = groupPosition;
                }
                return true;
            }
        });
    }


    /**
     * 根据网络状态加载数据
     */
    private void getDataByNetStatus() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            noNetView.setVisibility(View.GONE);
            ClassifyHandler.getFirstCategories(handler, 1, 100);
        } else {
            LoadDialog.dismiss(this);
            if (mSuccessView.getVisibility() != View.VISIBLE) {
                noNetView.setVisibility(View.VISIBLE);
            }
            ToastUtil.show(this, getString(R.string.offline));
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case RequestCodeConstant.REQUEST_GET_FIRST_CATEGORIES:
                count = 1;
                mClassifyFirstBean = (ClassifyFirstBean) msg.obj;
                if (mClassifyFirstBean == null) {
                    ToastUtil.show(this, getString(R.string.timeout));
                    return;
                }
                /**
                 * 一次性请求全部二级数据
                 */
                loadSecondData(mClassifyFirstBean);
                break;
            case RequestCodeConstant.REQUEST_GET_SECOND_CATEGORIES:
                //每次请求二级数据时添加到list,并且记录count,添加完成后处理二级数据
                ClassifySecondBean classifySecondBean = (ClassifySecondBean) msg.obj;
                if (classifySecondBean == null) {
                    ToastUtil.show(this, getString(R.string.timeout));
                    return;
                }
                mClassifySecondBeans.add(classifySecondBean);
                if (count == mClassifyFirstBean.getRoot().getItems().getData().size()) {
                    handlerSecondData(mClassifySecondBeans);
                }
                count++;
                break;
        }
    }

    /**
     * 处理点击事件
     *
     * @param v 控件
     */
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.cart_back_btn:
                finish();
                break;
        }

    }

    /**
     * 加载二级分类数据
     *
     * @param classifyFirstBean
     */
    private void loadSecondData(ClassifyFirstBean classifyFirstBean) {
        //部分url暂时不传进去(从json数据知道url的值,直接写死)
        //String url = classifyFirstBean.getRoot().getUrl();
        if (NetWorkUtil.isNetworkAvailable(this)) {
            List<ClassifyFirstBean.RootBean.ItemsBean.DataBean> data = classifyFirstBean.getRoot().getItems().getData();
            //正式版母婴分类不存在
            data.remove(3);
            for (int i = 0; i < data.size(); i++) {
                ClassifyHandler.getSecondCategories(this, handler, null, data.get(i).getId(), 1, 100, false, i);
            }
            mClassifySecondBeans = new ArrayList<>();
        } else {
            ToastUtil.show(this, getString(R.string.offline));
        }
    }

    /**
     * 处理二级分类数据
     */
    private void handlerSecondData(ArrayList<ClassifySecondBean> list) {
        Map<Integer, List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean>> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            ClassifySecondBean.RootBean.CategoriesBean categories = list.get(i).getRoot().getCategories();
            List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean> item = categories.getItem();
            int position = categories.getPosition();
            map.put(position, item);
        }
        LoadDialog.dismiss(this);
        mSuccessView.setVisibility(View.VISIBLE);
        mExpandableListView.setAdapter(new ExpandableListViewAdapter(mClassifyFirstBean.getRoot().getItems().getData(), map));
    }

    /*----------------------ExpandableListViewAdapter部分-----------------------*/
    private class ExpandableListViewAdapter extends BaseExpandableListAdapter {
        private List<ClassifyFirstBean.RootBean.ItemsBean.DataBean> mFristDatas;
        private Map<Integer, List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean>> mSecondDatas;

        public ExpandableListViewAdapter(List<ClassifyFirstBean.RootBean.ItemsBean.DataBean> firstDatas, Map<Integer, List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean>> secondDatas) {
            mFristDatas = firstDatas;
            mSecondDatas = secondDatas;
        }

        /**
         * 获取一级标签总数
         */
        @Override
        public int getGroupCount() {
            return mFristDatas.size();
        }

        /**
         * 获取一级标签下二级标签的总数
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        /**
         * 获取一级标签内容
         */
        @Override
        public Object getGroup(int groupPosition) {
            return mFristDatas.get(groupPosition);
        }

        /**
         * 获取一级标签下二级标签的内容
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mSecondDatas.get(groupPosition);
        }

        /**
         * 获取一级标签的ID
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * 获取二级标签的ID
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         * 指定位置相应的组视图
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * 对一级标签进行设置
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            FirstViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new FirstViewHolder();
                convertView = View.inflate(ClassifyActivity.this, R.layout.classify_expandable_first_item, null);
                viewHolder.classify_first_icon_iv = (ImageView) convertView.findViewById(R.id.classify_first_icon_iv);
                viewHolder.classify_first_arrows_v = (ImageView) convertView.findViewById(R.id.classify_first_arrows_v);
                viewHolder.classify_first_big_tv = (TextView) convertView.findViewById(R.id.classify_first_big_tv);
                viewHolder.classify_first_small_tv = (TextView) convertView.findViewById(R.id.classify_first_small_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (FirstViewHolder) convertView.getTag();
            }
            if (isExpanded) {
                viewHolder.classify_first_arrows_v.setBackgroundDrawable(getResources().getDrawable(R.drawable.classify_first_arrows_bottom));

            } else {
                viewHolder.classify_first_arrows_v.setBackgroundDrawable(getResources().getDrawable(R.drawable.classify_arrows_right));
            }
            ClassifyFirstBean.RootBean.ItemsBean.DataBean dataBean = mFristDatas.get(groupPosition);
            GlideUtil.getInstance().loadImage(ClassifyActivity.this, dataBean.getImage().getContent(),R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, viewHolder.classify_first_icon_iv);
            viewHolder.classify_first_big_tv.setText(dataBean.getName());
            viewHolder.classify_first_small_tv.setText(dataBean.getCategory_name());
            return convertView;
        }

        /**
         * 对一级标签下的二级标签进行设置
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View inflate = View.inflate(ClassifyActivity.this, R.layout.classify_expandable_second_item, null);
            MyGridView myGridView = (MyGridView) inflate.findViewById(R.id.gridview);
            List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean> itemBean = mSecondDatas.get(groupPosition);
            myGridView.setAdapter(new GridAdapter(itemBean));
            return inflate;
        }

        /**
         * 当选择子节点的时候，调用该方法
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class FirstViewHolder {
            ImageView classify_first_icon_iv, classify_first_arrows_v;
            TextView classify_first_big_tv, classify_first_small_tv;
        }
    }

    /*----------------------GridAdapter的适配器-----------------------*/
    private class GridAdapter extends BaseAdapter {
        List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean> mGridADatas;

        public GridAdapter(List<ClassifySecondBean.RootBean.CategoriesBean.ItemBean> datas) {
            mGridADatas = datas;
        }

        @Override
        public int getCount() {
            if (mGridADatas != null) {
                return mGridADatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mGridADatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(ClassifyActivity.this, R.layout.gridview_item, null);
                viewHolder.text = (TextView) convertView.findViewById(R.id.gridview_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final ClassifySecondBean.RootBean.CategoriesBean.ItemBean itemBean = mGridADatas.get(position);
            viewHolder.text.setText(itemBean.getName());
            viewHolder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ClassifyActivity.this, ProductListActivity.class);
                    intent.putExtra(ActionUtil.ACTION_CLASSIFY_ID, itemBean.getCate_id());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView text;
        }
    }
}
