package com.winguo.personalcenter.wallet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountBalance;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.bean.BalanceAndCash;
import com.winguo.personalcenter.wallet.balance.MyBalanceManagerActivity;
import com.winguo.personalcenter.wallet.balance.WalletActivity;
import com.winguo.personalcenter.wallet.bankcard.BankCardActivity;
import com.winguo.personalcenter.wallet.control.MyWalletControl;
import com.winguo.personalcenter.wallet.moudle.RequestBalanceCallback;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;
import com.winguo.view.IGridView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新版
 * 我的钱包中心
 */
public class MyWalletCenterActivity extends BaseActivity implements WinguoAcccountManagerUtils.IBalanceAndCashCallback {
    private List<Map<String, Object>> lifeservie_data = new ArrayList<>();
    private FrameLayout top_back;
    private TextView layout_title;
    private ImageView my_wallet_center_banner;
    private TextView my_wallet_center_recharge_ic;
    private TextView mywallet_center_balance;
    private FrameLayout mywallet_center_balance_rl;
    private FrameLayout mywallet_center_cash_coupon;
    private FrameLayout mywallet_center_balance_cash;
    private RelativeLayout mywallet_center_bank_card;
    private TextView mywallet_center_credit_card_repay;
    private TextView mywallet_center_character_loan;
    private IGridView mywallet_center_life_service;
    private MyWalletControl control;

    @Override
    protected int getLayout() {
        return R.layout.activity_mywallet_center;
    }

    @Override
    protected void initData() {
        moreLifeData();
        control = new MyWalletControl(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this, true);
            control.getBalanceAndCash(this);
        } else {
            ToastUtil.showToast(this, getString(R.string.timeout));
        }
    }

    /**
     * 从资源文件（comm_faction_array.xml）中读取存放的（常用功能--图片 文本）数据
     */
    private void moreLifeData() {
        //从res 资源文件中读取 存放数据（图片.文本）
        String[] comm_iconNames = resources.getStringArray(R.array.mywallet_center_life_service_name);
        TypedArray commTA = resources.obtainTypedArray(R.array.mywallet_center_life_service_new_icons);
        int[] comm_icons = new int[commTA.length()];
        for (int i = 0; i < commTA.length(); i++) {
            comm_icons[i] = commTA.getResourceId(i, 0);
        }
        commTA.recycle();//释放资源
        for (int i = 0; i < comm_icons.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", comm_icons[i]);
            map.put("text", comm_iconNames[i]);
            lifeservie_data.add(map);
        }
    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        layout_title = (TextView) findViewById(R.id.layout_title);
        layout_title.setText("我的钱包");

        mywallet_center_balance = (TextView) findViewById(R.id.mywallet_center_balance);
        my_wallet_center_recharge_ic = (TextView) findViewById(R.id.my_wallet_center_recharge_ic);
        my_wallet_center_banner = (ImageView) findViewById(R.id.my_wallet_center_banner);
        mywallet_center_balance_rl = (FrameLayout) findViewById(R.id.mywallet_center_balance_rl);
        mywallet_center_cash_coupon = (FrameLayout) findViewById(R.id.mywallet_center_cash_coupon);
        mywallet_center_bank_card = (RelativeLayout) findViewById(R.id.mywallet_center_bank_card);
        mywallet_center_balance_cash = (FrameLayout) findViewById(R.id.mywallet_center_balance_cash);
        mywallet_center_credit_card_repay = (TextView) findViewById(R.id.mywallet_center_credit_card_repay);
        mywallet_center_character_loan = (TextView) findViewById(R.id.mywallet_center_character_loan);
        mywallet_center_life_service = (IGridView) findViewById(R.id.mywallet_center_life_service);
        //无网络 余额显示 0.00
        if (!NetWorkUtil.isNetworkAvailable(this))
            mywallet_center_balance.setText(String.format(getString(R.string.mywallet_center_balance), 0.00 + ""));

        String[] from = {"image", "text"};
        int[] to = {R.id.my_wallet_more_menu_item_ic, R.id.my_wallet_more_menu_item_name};
        SimpleAdapter adapter = new SimpleAdapter(this, lifeservie_data, R.layout.my_wallet_center_more_item, from, to);
        mywallet_center_life_service.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        mywallet_center_balance_rl.setOnClickListener(this);
        mywallet_center_cash_coupon.setOnClickListener(this);
        mywallet_center_bank_card.setOnClickListener(this);
        mywallet_center_balance_cash.setOnClickListener(this);
        mywallet_center_credit_card_repay.setOnClickListener(this);
        mywallet_center_character_loan.setOnClickListener(this);
        my_wallet_center_recharge_ic.setOnClickListener(this);
        my_wallet_center_banner.setOnClickListener(this);

        mywallet_center_life_service.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //余额 充值
                        jumpBalanceManager();
                        break;
                    case 1:
                        //"住酒店"  http://touch.qunar.com/hotel/index
                        startBrower("http://touch.qunar.com/hotel/index");
                        break;
                    case 2:
                        // "电影"  http://m.maoyan.com
                        startBrower("http://m.maoyan.com/#type=movies");
                        break;
                    case 3:
                        //生活缴费
                        startBrower("http://life.ule.com/");

                        break;
                }
            }
        });
    }

    /**
     * 服务跳转 外部浏览器
     *
     * @param commUrl
     */
    private void startBrower(String commUrl) {

        try {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(commUrl));
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            if (!hasPreferredApp(this, intent)) {
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 判断系统是否设置了默认浏览器
     *
     * @param context
     * @param intent
     * @return
     */
    public boolean hasPreferredApp(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(resolveInfo.activityInfo.packageName);//resolveInfo.activityInfo.packageName --> android
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
            case R.id.my_wallet_center_recharge_ic:
            case R.id.my_wallet_center_banner:
                jumpBalanceManager();
                break;
            case R.id.mywallet_center_balance_cash:
                //余额 充值
                if (balanceAndCash != null) {
                    Intent it = new Intent(MyWalletCenterActivity.this, MyBalanceManagerActivity.class);
                    it.putExtra("Purse_balance", balanceAndCash.getItem().getPurse_balance());
                    it.putExtra("Cash_credit", balanceAndCash.getItem().getCash_credit());
                    startActivityForResult(it, BALANCE_REQUEST_CODE);
                } else {
                    ToastUtil.showToast(this, getString(R.string.timeout));
                }
                break;
            case R.id.mywallet_center_cash_coupon:
                //现金券
                if (balanceAndCash != null) {
                    Intent cash =  new Intent(MyWalletCenterActivity.this, WalletCashCouponActivity.class);
                    cash.putExtra("cashCoupon",balanceAndCash.getItem().getCash_coupon());
                    startActivity(cash);
                }
                break;
            case R.id.mywallet_center_bank_card:
                //银行卡
                //startActivity(new Intent(this,AccountBankCardGettingActivity.class));
                startActivity(new Intent(this, BankCardActivity.class));
                break;
            case R.id.mywallet_center_credit_card_repay:
                //信用卡还款
                ToastUtil.showToast(this, "此功能暂未开通，请后续关注..");
                break;
            case R.id.mywallet_center_character_loan:
                // 信用借贷
                ToastUtil.showToast(this, "此功能暂未开通，请后续关注..");
                break;

        }
    }

    private void jumpBalanceManager() {
        if (balanceAndCash != null) {
            Intent it = new Intent(MyWalletCenterActivity.this, WalletActivity.class);
            startActivityForResult(it, BALANCE_REQUEST_CODE);
        } else {
            ToastUtil.showToast(this, getString(R.string.timeout));
        }
    }

    private final static int BALANCE_REQUEST_CODE = 78;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BALANCE_REQUEST_CODE:
                    if (data != null) {
                        //充值成功 刷新个人资料 获取新的现金券数量
                        if (NetWorkUtil.isNetworkAvailable(MyWalletCenterActivity.this)) {
                            control.getBalanceAndCash(MyWalletCenterActivity.this);
                        } else {
                            ToastUtil.showToast(MyWalletCenterActivity.this,"刷新现金失败，请稍后再试!");
                        }
                    }
                    break;
            }
        }
    }


    private BalanceAndCash balanceAndCash = null;
    @Override
    public void getBalanceAndCash(BalanceAndCash balance) {
        if (balance != null) {
            LoadDialog.dismiss(this);
            balanceAndCash = balance;
            BalanceAndCash.ItemBean item = balance.getItem();
            Log.e("wallet 合计",""+balance.getItem().getPurse_balance()+" cash:"+balance.getItem().getCash_coupon());
            BigDecimal  bigDecimal =  new BigDecimal(item.getPurse_balance() + item.getCash_coupon()+item.getCash_credit());
            double total  =  bigDecimal.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
            mywallet_center_balance.setText(String.format(getString(R.string.mywallet_center_balance), total+ ""));
        }

    }

    @Override
    public void balanceAndCashErrorMsg(int message) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(MyWalletCenterActivity.this, GBAccountError.getErrorMsg(this, message));
    }
}
