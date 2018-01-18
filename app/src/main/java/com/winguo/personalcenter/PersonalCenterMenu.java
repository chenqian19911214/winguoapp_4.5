package com.winguo.personalcenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.activity.Student_creation_Activity;
import com.winguo.app.StartApp;
import com.winguo.base.BaseFragment;
import com.winguo.bean.PersonInfo;
import com.winguo.login.LoginActivity;
import com.winguo.mine.collect.MyCollectActivity;
import com.winguo.personalcenter.myorder.MyTotalOrderActivity;
import com.winguo.personalcenter.setting.PersonalActivity;
import com.winguo.personalcenter.setting.SetCenterActivity;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;
import com.winguo.utils.LruCacheUtils;
import com.winguo.utils.PersonInfoSQLOperateImpl;
import com.winguo.utils.PersonInfoSQLiteOpenHelper;
import com.winguo.utils.SPUtils;
import com.winguo.utils.WinguoAcccountManagerUtils;
import com.winguo.view.CircleImageView;
import com.winguo.view.IGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/10/9.
 * 个人中心
 */

public class PersonalCenterMenu extends BaseFragment implements AdapterView.OnItemClickListener {

    List<Map<String, Object>> moreMenu_data = new ArrayList<>();
    private IGridView personal_center_more_menu;
    private RelativeLayout personal_center_space_station;
    private ImageView personal_center_user_qrcode;
    private ImageView personal_center_setting;
    private CircleImageView personal_center_user_img;
    private TextView personal_center_user_nickname;
    private RelativeLayout personal_center_user_wallet;
    private LinearLayout personal_center_cart_shopping;
    private ImageView personal_center_user_open_maker ,personal_center_u;
    private MyBroadcastReceiver receiver;
    private RelativeLayout personal_center_login_area;

    @Override
    protected int getLayout() {
        return R.layout.personal_center_menu_layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //注册广播 获取登陆后用户账号 头像等
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGIN_SUCCESS);
        intentFilter.addAction(Constants.QUIT_SUCCESS);
        intentFilter.addAction(Constants.OPEN_SUCCESS);
        intentFilter.addAction(Constants.MODIFY_PHOTO);
        intentFilter.addAction(Constants.MODIFY_NICK);
        receiver = new MyBroadcastReceiver();
        context.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void initData() {
        moreMenuData();
    }

    /**
     * 从资源文件（comm_faction_array.xml）中读取存放的（常用功能--图片 文本）数据
     */
    private void moreMenuData() {
        //从res 资源文件中读取 存放数据（图片.文本）
        String[] comm_iconNames = resources.getStringArray(R.array.personal_center_more_menu_name);
        TypedArray commTA = resources.obtainTypedArray(R.array.personal_center_more_menu_icons);
        int[] comm_icons = new int[commTA.length()];
        for (int i = 0; i < commTA.length(); i++) {
            comm_icons[i] = commTA.getResourceId(i, 0);
        }
        commTA.recycle();//释放资源
        for (int i = 0; i < comm_icons.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", comm_icons[i]);
            map.put("text", comm_iconNames[i]);
            moreMenu_data.add(map);
        }
    }

    @Override
    protected void initView(View view) {
        //重新设置左侧 个人中心宽度为屏幕的四分之三
      /*  RelativeLayout left_menu = (RelativeLayout) view.findViewById(R.id.left_menu);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = (ScreenUtil.getScreenWidth(context)) * 3 / 4;
        left_menu.setLayoutParams(params);*/

        personal_center_setting = view.findViewById(R.id.personal_center_setting);
        personal_center_login_area = view.findViewById(R.id.personal_center_login_area);
        personal_center_user_qrcode = view.findViewById(R.id.personal_center_user_qrcode);
        personal_center_user_img = view.findViewById(R.id.personal_center_user_img);
        personal_center_user_nickname = view.findViewById(R.id.personal_center_user_nickname);
        personal_center_user_open_maker = view.findViewById(R.id.personal_center_user_open_maker); //开通创客
        personal_center_user_wallet = view.findViewById(R.id.personal_center_user_wallet);
        personal_center_u = view.findViewById(R.id.personal_center_u);
        personal_center_space_station = view.findViewById(R.id.personal_center_space_station);
        personal_center_cart_shopping = view.findViewById(R.id.personal_center_cart_shopping);
        personal_center_more_menu = view.findViewById(R.id.personal_center_more_menu);
        String[] from = {"image", "text"};
        int[] to = {R.id.more_menu_item_ic, R.id.more_menu_item_name};
        SimpleAdapter adapter = new SimpleAdapter(context, moreMenu_data, R.layout.personal_center_more_menu_item, from, to);
        personal_center_more_menu.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        personal_center_setting.setOnClickListener(this);
        personal_center_user_qrcode.setOnClickListener(this);
        personal_center_user_img.setOnClickListener(this);
        personal_center_user_nickname.setOnClickListener(this);
        personal_center_user_open_maker.setOnClickListener(this);
        personal_center_user_wallet.setOnClickListener(this);
        personal_center_space_station.setOnClickListener(this);
        personal_center_cart_shopping.setOnClickListener(this);
        personal_center_more_menu.setOnItemClickListener(this);
        personal_center_u.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it = null;
        switch (position) {
            case 0:
                //我的订单
                if (info != null) {
                    it = new Intent();
                    it.putExtra(ActionUtil.ACTION_ORDER_STATUS, 0);
                    it.setClass(mContext, MyTotalOrderActivity.class);
                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_ALL_ORDER);
                }
                break;
            case 1:
                //我的收藏
                if (info != null) {
                    it = new Intent();
                    it.putExtra("collect", 1);
                    it.setClass(mContext, MyCollectActivity.class);
                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_GOOD_ATTENTION);
                }

                break;
            case 2:
                //我的伙伴
                if (info != null) {
                    it = new Intent(context, MyPartnerActivity.class);
                } else {
                    it = new Intent(mContext, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_MY_PARTNER);
                }
                break;


            case 3:
                if (info != null) {
                    if (info.isCreater == 1) {
                        it = new Intent(getActivity(), CreaterIncomeActivity.class);
                    } else {
                        it = new Intent(getActivity(), Student_creation_Activity.class);
                    }
                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_STUDENT_CREATION);
                }
                break;
        }
        if (it != null)
            startActivity(it);

    }

    @Override
    protected void doClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.personal_center_setting:
                //设置
                if (info != null) {
                    it = new Intent(mContext, SetCenterActivity.class);
                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_SETTING);
                }
                startActivity(it);
                break;
            case R.id.personal_center_user_qrcode:
                //二维码
                if (info != null) {
                    if (info.shared) {
                        it = new Intent(mContext, MyQRCodeActivity.class);
                    } else {
                       // Toast.makeText(mContext, R.string.you_on_stop, Toast.LENGTH_SHORT).show();
                        it = new Intent(context, OpenSpaceActivity.class);
                        it.setAction(Constants.LOGIN_WAY_QR);
                        //return;
                    }

                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_QR);
                }

                startActivity(it);
                break;
            case R.id.personal_center_user_img:
            case R.id.personal_center_user_nickname:
            case R.id.personal_center_u:
                //登录
                if (info != null) {
                    it = new Intent(getActivity(), PersonalActivity.class);
                    startActivityForResult(it, 101);//头像更换 返回新头像uri
                } else {
                    it = new Intent(context, LoginActivity.class);
                    startActivity(it);
                }
                break;
            case R.id.personal_center_user_open_maker:
                //开通创客
                break;
            case R.id.personal_center_user_wallet:
                //跳转钱包
                if (info != null) {
                    it = new Intent(context, MyWalletCenterActivity.class);
                    // it = new Intent(context, AccountCenterActivity.class);
                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_WALLET);
                }
                startActivity(it);
                break;
            case R.id.personal_center_space_station:
                if (info != null) {
                    it = new Intent(mContext, OpenSpaceActivity.class);
                } else {
                    it = new Intent(context, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_OPEN_SPACE);
                }
                startActivity(it);
                break;
            case R.id.personal_center_cart_shopping:
                //购物车
                Intents.startCart(SPUtils.contains(context, "accountName"), mContext);
                break;
        }
    }

    private WinguoAccountGeneral info = null;

    /**
     * 接受广播 登录后用户信息
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            //登陆过后 通知修改头像、 账户名、余额、果币
            if (action.equals(Constants.LOGIN_SUCCESS)) {
                info = (WinguoAccountGeneral) intent.getSerializableExtra("info");

                Log.e("info time", "reg time :" + info.regDate + "lasttime :" + info.lastLoginDate);

                PersonInfoSQLOperateImpl operate = new PersonInfoSQLOperateImpl(context);
                PersonInfo p = operate.findByName(info.accountName);
                if (p != null) { //存在此账户 更新本地数据库
                    if (!TextUtils.isEmpty(info.telMobile))
                        operate.updata(info.accountName, PersonInfoSQLiteOpenHelper.USER_TEL, info.telMobile);
                    if (!TextUtils.isEmpty(info.userName))
                        operate.updata(info.accountName, PersonInfoSQLiteOpenHelper.USER_SPACE_NAME, info.userName);
                } else {  //不存在此账户  添加到本地数据库
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setAccountName(info.accountName);
                    personInfo.setAccountTel(info.telMobile);
                    personInfo.setAccountSpaceName(info.userName);
                    operate.add(personInfo);
                }
                //没有开通空间站
                String pizza = WinguoAccountDataMgr.getPizza(context);//微信登录 未保存密码来区分
                if (pizza == null) {
                    if (!TextUtils.isEmpty(info.userName)) {
                        personal_center_user_nickname.setText(info.userName);  //微信昵称
                    } else {
                        personal_center_user_nickname.setText(info.accountName); //问果账号
                    }
                } else {
                    personal_center_user_nickname.setText(info.userName); //问果账号
                }
                //personal_center_login_area.setBackgroundResource(R.drawable.drawlayout_login_bg);

                SPUtils.put(context, "userName", info.userName);//保存登录用户名（微信昵称）
                SPUtils.put(context, "accountName", info.accountName);//保存登陆后账号名
                SPUtils.put(context, "userImagURL", info.icoUrl);//保存用户头像

                //未开放商铺 shopName会为空
                if (!TextUtils.isEmpty(info.shopName)) {
                    SPUtils.put(context, "shopName", info.shopName);
                    //  personInfo.setAccountSpaceName(info.shopName);
                }
                if (info.isCreater == 1) {   //isCreater 为0 为普通消费者   为1 是大学生创客
                    personal_center_user_open_maker.setVisibility(View.VISIBLE);
                }
                //普通用户没有店铺链接
                if (info.shared) {  //是分销层用户 保存店铺地址
                    SPUtils.put(context, "userShopURL", info.mobileShopAddr);
                    //  personal_center_space_station.setVisibility(View.GONE);
                    //  userName.setText(info.shopName); //开通空间站
                } else {
                    //不是分销用户 开通空间站显示
                    //  personal_center_space_station.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(info.icoUrl)) {
                    personal_center_user_img.setTag(info.icoUrl);

                    if (StartApp.lruCache == null) {
                        StartApp.lruCache = new LruCacheUtils(mContext);
                    }
                    StartApp.lruCache.downLoaderBitmap(personal_center_user_img, new LruCacheUtils.ImageLoaderlistener() {
                        @Override
                        public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                            if (bitmap != null)
                                imageView.setImageBitmap(bitmap);
                        }
                    });
                }

            }

            if (action.equals(Constants.MODIFY_PHOTO)) {
                Bitmap newPhoto = intent.getParcelableExtra("newPhoto");
                personal_center_user_img.setImageBitmap(newPhoto);
            }
            if (action.equals(Constants.MODIFY_NICK)) {
                String newNick = intent.getStringExtra("newNick");
                personal_center_user_nickname.setText(newNick);
            }
            if (action.equals(Constants.OPEN_SUCCESS)) {
                //开启成功 隐藏按钮
                // personal_center_space_station.setVisibility(View.GONE);
                // WinguoAccountImpl.getGeneralInfo(getContext(), new WinguoAccountGeneral());
                WinguoAcccountManagerUtils.refreshGeneral(mContext);
            }

            if (action.equals(Constants.QUIT_SUCCESS)) {
                SPUtils.remove(context, "accountName");
                SPUtils.remove(context, "userName");
                SPUtils.remove(context, "userImagURL");
                SPUtils.remove(context, "userShopURL");

                WinguoAccountDataMgr.clear(context, false);//退出 清除账户本地库信息
                //personal_center_space_station.setVisibility(View.GONE);
                personal_center_user_nickname.setText("注册/登录");
                personal_center_user_open_maker.setVisibility(View.GONE);
                personal_center_user_img.setImageResource(R.drawable.winguo_personal_deafault_icon);
                //personal_center_login_area.setBackgroundResource(R.drawable.drawlayout_no_login_bg);
                info = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }


}
