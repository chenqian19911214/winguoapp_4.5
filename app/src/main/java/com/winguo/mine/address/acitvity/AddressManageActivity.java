package com.winguo.mine.address.acitvity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.activity.BaseActivity2;
import com.winguo.mine.address.AddressManageHandle;
import com.winguo.mine.address.bean.AddressDeleteBean;
import com.winguo.mine.address.bean.AddressUpdateBean;
import com.winguo.pay.modle.bean.AddressInfoBean;
import com.winguo.pay.modle.store.ItemBean;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.RequestCodeConstant;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.util.List;

import static android.view.View.inflate;


/**
 * @author hcpai
 * @desc 地址管理
 */
public class AddressManageActivity extends BaseActivity2 implements SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout back_rl;
    /**
     * 标题
     */
    private TextView address_title_tv;
    /**
     * 地址管理
     */
    private TextView address_manage_tv;
    /**
     * 添加收货地址
     */
    private Button address_add_btn;
    private View mNoDataView;
    private View mSuccessView;
    /**
     * 内容
     */
    private ListView mListView;
    private boolean mIsManage;
    private int mPosition;
    private List<ItemBean> mItems;
    private AddressAdapter mAdapter;
    private boolean mIsSelectAddress = false;
    private boolean mIsUpdate = false;
    private View noNetView;
    private Button btn_request_net;
    //private SwipeRefreshLayout mSwipeLayout;
    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    protected int getViewId() {
        return R.layout.activity_address_manage;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initViews() {
        back_rl = (RelativeLayout) findViewById(R.id.back_rl);
        address_title_tv = (TextView) findViewById(R.id.address_title_tv);
        address_manage_tv = (TextView) findViewById(R.id.address_manage_tv);
        FrameLayout address_container = (FrameLayout) findViewById(R.id.address_container);
        address_add_btn = (Button) findViewById(R.id.address_add_btn);
        //(给容器添加布局)
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //没有数据
        if (mNoDataView == null) {
            mNoDataView = inflate(this, R.layout.loading_empty, null);
            TextView textView = (TextView) mNoDataView.findViewById(R.id.empty_data_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_address);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            textView.setText(getString(R.string.no_address));
            address_container.addView(mNoDataView, params);
        }
        //有数据
        if (mSuccessView == null) {
            mSuccessView = inflate(this, R.layout.order_listview, null);
            mListView = (ListView) mSuccessView.findViewById(R.id.order_lv);
            address_container.addView(mSuccessView, params);
            //mSwipeLayout = (SwipeRefreshLayout) mSuccessView.findViewById(R.id.swipe_container);
        }
        //无网络
        if (noNetView == null) {
            noNetView = View.inflate(this, R.layout.no_net, null);
            btn_request_net = (Button) noNetView.findViewById(R.id.btn_request_net);
            TextView no_net_tv = (TextView) noNetView.findViewById(R.id.no_net_tv);
            Drawable drawableTop = getResources().getDrawable(R.drawable.no_net);
            no_net_tv.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            address_container.addView(noNetView);
        }
        //默认为加载中...
        mNoDataView.setVisibility(View.GONE);
        LoadDialog.show(this);
        noNetView.setVisibility(View.GONE);
        mSuccessView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mIsManage = (boolean) getIntent().getExtras().get(ActionUtil.ACTION_ADDRESS_MANAGE);
        //管理地址
        updateUI();
        LoadDialog.show(this);
        getDataByNetStatus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
        LoadDialog.show(this);
        getDataByNetStatus();
    }

    /**
     * 通过网络状态加载数据
     */
    private void getDataByNetStatus() {
        if (NetWorkUtil.isNetworkAvailable(this)) {
            noNetView.setVisibility(View.GONE);
            AddressManageHandle.getAddressList(this, handler);
            mIsUpdate = false;
        } else {
            LoadDialog.dismiss(this);
            mSuccessView.setVisibility(View.GONE);
            noNetView.setVisibility(View.VISIBLE);
            //mSwipeLayout.setRefreshing(false);
            ToastUtil.show(this, getString(R.string.offline));
        }
    }

    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
       // mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        //mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
        //        android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        //mSwipeLayout.setProgressBackgroundColor(R.color.red); // 设定下拉圆圈的背景
        //mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        back_rl.setOnClickListener(this);
        address_manage_tv.setOnClickListener(this);
        address_add_btn.setOnClickListener(this);
        btn_request_net.setOnClickListener(this);
    }

    /**
     * 处理子线程传递的消息
     *
     * @param msg 消息载体
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            //获取地址
            case RequestCodeConstant.REQUEST_GET_ADDRESS_LIST:
                handlerAddressLists((AddressInfoBean) msg.obj);
                break;
            //删除地址
            case RequestCodeConstant.REQUEST_DELETEADDRESS:
                handlerDeleteAddress((AddressDeleteBean) msg.obj);
                break;
            //更新地址
            case RequestCodeConstant.REQUEST_UPDATEADDRESS:
                AddressUpdateBean addressUpdateBean = (AddressUpdateBean) msg.obj;
                if (addressUpdateBean == null) {
                    //mSwipeLayout.setRefreshing(false);
                    ToastUtil.show(this, getString(R.string.timeout));
                    defAddress = "";
                    return;
                }
                if (addressUpdateBean.getMessage().getCode() == 0) {
                    AddressManageHandle.getAddressList(this, handler);
                    mIsUpdate = true;
                } else {
                    ToastUtil.showToast(this, addressUpdateBean.getMessage().getText());
                }
                break;
        }
    }

    private String defAddress = "";

    /**
     * 处理删除地址
     *
     * @param addressDeleteBean
     */
    private void handlerDeleteAddress(AddressDeleteBean addressDeleteBean) {
        if (addressDeleteBean == null) {
           // mSwipeLayout.setRefreshing(false);
            ToastUtil.show(this, getString(R.string.timeout));
            return;
        }
        if (addressDeleteBean.getMessage().getCode() == 0) {
            ToastUtil.show(this, "删除地址成功");
            mItems.remove(mPosition);
            if (mItems.size() == 0) {
                mSuccessView.setVisibility(View.GONE);
                mNoDataView.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.show(this, "删除地址失败");
        }
    }

    /**
     * 处理收件人信息
     *
     * @param addressInfoBean
     */
    private void handlerAddressLists(AddressInfoBean addressInfoBean) {
        LoadDialog.dismiss(this);
        noNetView.setVisibility(View.GONE);
        //mSwipeLayout.setRefreshing(false);
        if (addressInfoBean == null) {
            //mSwipeLayout.setRefreshing(false);
            ToastUtil.show(this, getString(R.string.timeout));
            return;
        }
        if (addressInfoBean.userInfos == null) {
            //没有数据
            mNoDataView.setVisibility(View.VISIBLE);
            mSuccessView.setVisibility(View.GONE);
        } else {
            //有数据
            mNoDataView.setVisibility(View.GONE);
            mSuccessView.setVisibility(View.VISIBLE);
            mItems = addressInfoBean.userInfos.item;
            if (mAdapter == null) {
                mAdapter = new AddressAdapter(this, mItems);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(mItems);
                mAdapter.notifyDataSetChanged();
                mSuccessView.setVisibility(View.VISIBLE);
                LoadDialog.dismiss(this);
            }
            if (mIsUpdate) {
                ToastUtil.showToast(this, "更新地址成功!");
                mIsUpdate = false;

            }
            //返回默认地址
            if (addressInfoBean.userInfos.item != null)
                takeDefAddress(addressInfoBean.userInfos.item);


            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //返回前一个页面需要的数据,并关闭自己
                    ItemBean itemBean = mItems.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("itemBean", itemBean);
                    setResult(RequestCodeConstant.FIRST_ADD_ADDRESS, intent);
                    finish();
                }
            });
        }
    }

    /**
     * 返回默认收货地址
     * @param items
     */
    private void takeDefAddress(List<ItemBean> items) {
        String address = "";
        for (ItemBean ib:items) {
            int isDefaultAddress = ib.isDefaultAddress;
            if (isDefaultAddress == 1) {
                address = ib.provinceName+ib.cityName+ib.areaName+ib.townName+ib.dinfo_address;
                Intent it = new Intent();
                it.putExtra("defAddress",address);
                setResult(RESULT_OK,it);
                break;
            }
        }
        if (TextUtils.isEmpty(address)) {
            Intent it = new Intent();
            it.putExtra("defAddress",getString(R.string.user_area));
            setResult(RESULT_OK,it);
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
            case R.id.back_rl:
                finish();
                break;
            case R.id.address_manage_tv:
                //点击管理
                if (address_manage_tv.getText().equals("管理")) {
                    mIsManage = true;
                } else {
                    //点击选择
                    mIsManage = false;
                    mSuccessView.setVisibility(View.GONE);
                    LoadDialog.show(this);
                    AddressManageHandle.getAddressList(this, handler);
                }
                mIsSelectAddress = true;
                if (mAdapter!=null)
                    mAdapter.notifyDataSetChanged();
                updateUI();
                break;
            case R.id.address_add_btn:
                //跳转到新建地址界面
                Intent intent = new Intent();
                intent.setClass(this, AddressEditActivity.class);
                intent.putExtra(ActionUtil.ADDRESS_EDIT_INFO, (Parcelable[]) null);
                intent.putExtra(ActionUtil.ADDRESS_EDIT_OR_ADD, false);
                startActivity(intent);
                break;
            //重新加载数据
            case R.id.btn_request_net:
                LoadDialog.show(this);
                getDataByNetStatus();
                break;
        }
    }

    /**
     * 更新UI
     */
    private void updateUI() {
        if (mIsManage) {
            if (mIsSelectAddress) {
                address_manage_tv.setText(getString(R.string.address_select));
                address_manage_tv.setVisibility(View.VISIBLE);
            } else {
                address_manage_tv.setVisibility(View.GONE);
            }
            address_title_tv.setText(getString(R.string.address_manage_title));
        } else {
            //选择地址
            address_title_tv.setText(getString(R.string.address_select_title));
            address_manage_tv.setText(getString(R.string.address_manage));
            address_manage_tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 下拉刷新的回调接口
     */
    @Override
    public void onRefresh() {
        getDataByNetStatus();
    }

    /*----------------------adapter部分-----------------------*/

    private class AddressAdapter extends BaseAdapter {
        private Context mContext;
        private List<ItemBean> mList;
        private int currentPosition = -1;

        public AddressAdapter(Context context, List<ItemBean> list) {
            mContext = context;
            mList = list;
        }

        public void setData(List<ItemBean> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
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
                convertView = View.inflate(mContext, R.layout.address_item, null);
                viewHolder.address_receiver_tv = (TextView) convertView.findViewById(R.id.address_receiver_tv);
                viewHolder.address_common_tv = (TextView) convertView.findViewById(R.id.address_common_tv);
                viewHolder.address_phone_tv = (TextView) convertView.findViewById(R.id.address_phone_tv);
                viewHolder.address_address_tv = (TextView) convertView.findViewById(R.id.address_address_tv);
                viewHolder.address_edit_rl = (LinearLayout) convertView.findViewById(R.id.address_edit_rl);
                viewHolder.address_common_iv = (ImageView) convertView.findViewById(R.id.address_common_iv);
                viewHolder.address_edit_btn = (Button) convertView.findViewById(R.id.address_edit_btn);
                viewHolder.address_delete_btn = (Button) convertView.findViewById(R.id.address_delete_btn);
                viewHolder.address_update_btn = (Button) convertView.findViewById(R.id.address_update_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ItemBean itemBean = mList.get(position);
            //初始化数据

            viewHolder.address_phone_tv.setText(itemBean.dinfo_mobile);
            viewHolder.address_receiver_tv.setText(itemBean.dinfo_received_name);
            String address = itemBean.provinceName + "省 " + itemBean.cityName + " " + itemBean.areaName + " " + itemBean.townName + "\n" + itemBean.dinfo_address;
            viewHolder.address_address_tv.setText(address);
            viewHolder.address_edit_rl.setVisibility(mIsManage ? View.VISIBLE : View.GONE);
            if (itemBean.is_update == 1) {
                viewHolder.address_update_btn.setVisibility(View.VISIBLE);
            } else {
                viewHolder.address_update_btn.setVisibility(View.GONE);
            }
            //显示默认地址
            if (itemBean.isDefaultAddress == 1) {
                //购物车入口
                if (!mIsManage) {
                    viewHolder.address_common_tv.setText("默认");
                    viewHolder.address_common_tv.setVisibility(View.VISIBLE);
                } else {
                    //个人中心入口
                    viewHolder.address_common_tv.setVisibility(View.GONE);
                    viewHolder.address_common_iv.setSelected(true);
                    currentPosition = position;
                }
            } else {
                viewHolder.address_common_tv.setVisibility(View.GONE);
                viewHolder.address_common_iv.setSelected(false);
            }
            setListener(viewHolder, itemBean, position);
            return convertView;
        }

        //初始化监听
        private void setListener(final ViewHolder viewHolder, final ItemBean itemBean, final int position) {
            //升级地址
            viewHolder.address_update_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(ActionUtil.ADDRESS_EDIT_INFO, itemBean);
                    intent.putExtra(ActionUtil.ADDRESS_EDIT_OR_ADD, true);
                    intent.setClass(mContext, AddressEditActivity.class);
                    startActivity(intent);
                }
            });
            //编辑地址
            viewHolder.address_edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(ActionUtil.ADDRESS_EDIT_INFO, itemBean);
                    intent.putExtra(ActionUtil.ADDRESS_EDIT_OR_ADD, true);
                    intent.setClass(mContext, AddressEditActivity.class);
                    startActivity(intent);
                }
            });
            //删除地址
            viewHolder.address_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialog2 dialog = new CustomDialog2(mContext);
                    dialog.setDialogTitle(getResources().getString(R.string.addreess_delete_dialog_title));
                    dialog.setNegativeButton(getResources().getString(R.string.negative_text), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton(getResources().getString(R.string.positive_text), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (NetWorkUtil.isNetworkAvailable(AddressManageActivity.this)) {
                                mPosition = position;
                                AddressManageHandle.deleteAddress(AddressManageActivity.this, handler, itemBean.addressId);
                                dialog.dismiss();
                            } else {
                                ToastUtil.show(AddressManageActivity.this, getString(R.string.offline));
                            }
                        }
                    });
                }
            });
            //默认地址
            viewHolder.address_common_iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LoadDialog.show(mContext);
                    if (currentPosition == position) {
                        //选择
                        if (viewHolder.address_common_iv.isSelected()) {
                            if (NetWorkUtil.isNetworkAvailable(AddressManageActivity.this)) {
                                AddressManageHandle.updateAddressInfo(AddressManageActivity.this, handler, itemBean.dinfo_received_name, itemBean.dinfo_zip + "", itemBean.dinfo_address, itemBean.dinfo_mobile, itemBean.provinceCode, itemBean.cityCode, itemBean.areaCode, itemBean.townCode, 0, itemBean.addressId, 0);
                                currentPosition = -1;
                            } else {
                                ToastUtil.show(AddressManageActivity.this, getString(R.string.offline));
                            }
                        } else {
                            if (NetWorkUtil.isNetworkAvailable(AddressManageActivity.this)) {
                                defAddress = itemBean.provinceName;
                                AddressManageHandle.updateAddressInfo(AddressManageActivity.this, handler, itemBean.dinfo_received_name, itemBean.dinfo_zip + "", itemBean.dinfo_address, itemBean.dinfo_mobile, itemBean.provinceCode, itemBean.cityCode, itemBean.areaCode, itemBean.townCode, 0, itemBean.addressId, 1);
                            } else {
                                ToastUtil.show(AddressManageActivity.this, getString(R.string.offline));
                            }
                            currentPosition = position;
                        }
                    } else {
                        //选中
                        if (NetWorkUtil.isNetworkAvailable(AddressManageActivity.this)) {
                            AddressManageHandle.updateAddressInfo(AddressManageActivity.this, handler, itemBean.dinfo_received_name, itemBean.dinfo_zip + "", itemBean.dinfo_address, itemBean.dinfo_mobile, itemBean.provinceCode, itemBean.cityCode, itemBean.areaCode, itemBean.townCode, 0, itemBean.addressId, 1);
                        } else {
                            ToastUtil.show(AddressManageActivity.this, getString(R.string.offline));
                        }
                    }
                }
            });
        }
    }

    class ViewHolder {
        private TextView address_receiver_tv, address_phone_tv, address_address_tv, address_common_tv;
        private LinearLayout address_edit_rl;
        private ImageView address_common_iv;
        private Button address_edit_btn, address_delete_btn, address_update_btn;
    }


}
