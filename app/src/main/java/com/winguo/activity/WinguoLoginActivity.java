package com.winguo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.client.android.camera.ZxingCaptureActivity;
import com.guobi.account.GBAccountError;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountDataMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.guobi.account.WinguoAccountKey;
import com.guobi.wallet.AccountBankCardGettingActivity;
import com.winguo.MainActivity;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseActivity;
import com.winguo.dynamic.FriendsDynamicActivity;
import com.winguo.listener.ITextWatcher;
import com.winguo.mine.collect.MyCollectActivity;
import com.winguo.mine.history.HistoryLogActivity;
import com.winguo.mine.order.MyOrderActivity;
import com.winguo.personalcenter.MyPartnerActivity;
import com.winguo.personalcenter.MyQRCodeActivity;
import com.winguo.personalcenter.OpenSpaceActivity;
import com.winguo.personalcenter.safecenter.AccountSafeActivity;
import com.winguo.personalcenter.safecenter.loginpwd.ModifyPwdActivity;
import com.winguo.personalcenter.safecenter.paypwd.ModifyPayWayActivity;
import com.winguo.personalcenter.setting.FeedBackActivity;
import com.winguo.personalcenter.setting.SetCenterActivity;
import com.winguo.personalcenter.wallet.MyWalletCenterActivity;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 问果app 2.0 登录页面
 * Created by admin on 2017/4/5.
 */
@Deprecated
public class WinguoLoginActivity extends BaseActivity implements WinguoAcccountManagerUtils.ITakeWinguoGeneralResult {


    private View lastOpen;

    private EditText mUserEditText;
    private EditText mPwdEditText;

    private static final int REQUEST_REGISTER = 3;
    private static final int FORGET_PWD = 4;
    private boolean mIsSwitchAccount = false;
    private MyBroadcastReceiver receiver;
    private String userName;
    private String userPwd;
    private TextView accountTip;
    private Button login;
    private ImageView clear;
    private Intent flagIntent;
    private boolean isMySpace;


    @Override
    protected void initData() {

        flagIntent = getIntent();
        isMySpace = flagIntent.getBooleanExtra("isMySpace", false);
      /* if (flagIntent != null) {
            userName = flagIntent.getStringExtra("userName");
            userPwd = flagIntent.getStringExtra("userPwd");
        }*/
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("landscape");
        intentFilter.addAction("loginerror");
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void initViews() {
        Button register = (Button) findViewById(R.id.hybrid4_account_register_tv);
        register.setOnClickListener(this);

        TextView forgetTv = (TextView) findViewById(R.id.hybrid4_account_forgetpwd_tv);
        forgetTv.setOnClickListener(this);

        findViewById(R.id.hybrid4_account_login_back).setOnClickListener(this);

        clear = (ImageView) findViewById(R.id.winguo_login_clear_account);

        TextView title = (TextView) findViewById(R.id.hybrid4_account_login_title);

        final EditText pwdEt = (EditText) findViewById(R.id.hybrid4_account_login_pwd_et);
        final EditText userEt = (EditText) findViewById(R.id.hybrid4_account_login_user_et);
        accountTip = (TextView) findViewById(R.id.winguo_app_acoount_tip);

        login = (Button) findViewById(R.id.winguo_account_login_button);
        login.setOnClickListener(this);

        // CheckBox mRemCb = (CheckBox) findViewById(R.id.hybrid4_account_login_showpwd_cb);
        ToggleButton mRemCb = (ToggleButton) findViewById(R.id.hybrid4_account_login_showpwd_cb);
        mRemCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Selection.setSelection((Spannable) pwdEt.getText(), pwdEt.length());
                } else {
                    // 隐藏密码
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Selection.setSelection((Spannable) pwdEt.getText(), pwdEt.length());
                }
            }
        });

        mPwdEditText = pwdEt;
        mUserEditText = userEt;

        String userName = WinguoAccountDataMgr.getUserName(this);
        if (userName!=null) {
            if (!TextUtils.isEmpty(userName)) {
                String pizza = WinguoAccountDataMgr.getPizza(this);
                Log.i("login temp ame1 2" ,userName+"::pwd:"+pizza);
                clear.setVisibility(View.VISIBLE);
                userEt.setText(userName);
                pwdEt.setText(pizza);
            }

        }
        mIsSwitchAccount = getIntent().getBooleanExtra("isswitch", false);
        if (this.userName != null && userPwd != null) {
            userEt.setText(this.userName);
            pwdEt.setText(userPwd);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.winguo_app_login_layout;
    }

    @Override
    protected void setListener() {
        clear.setOnClickListener(this);
        setEditTextInhibitInputSpeChat(mUserEditText);
        mUserEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String editable = mUserEditText.getText().toString();
                if (!TextUtils.isEmpty(editable)) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPwdEditText.addTextChangedListener(new ITextWatcher(ITextWatcher.TYPE_PWD, mPwdEditText, this));


    }


    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Selection.setSelection(mUserEditText.getText(), mUserEditText.length());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case LOGIN_DIALOG:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                String message = getString(R.string.hybrid4_account_info_login);
                dialog.setMessage(message);

                return dialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.hybrid4_account_register_tv://注册

                Intent register = new Intent(this, ZxingCaptureActivity.class);
                startActivityForResult(register, REQUEST_REGISTER);
                break;

            case R.id.hybrid4_account_login_back://返回
                if (isMySpace) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    isMySpace = false;
                }
                finish();
                break;

            case R.id.hybrid4_account_forgetpwd_tv:

                Intent reset = new Intent(WinguoLoginActivity.this, ModifyPwdActivity.class);
                startActivityForResult(reset, FORGET_PWD);
                break;

            case R.id.winguo_account_login_button:
                submit(v);
                break;
            case R.id.winguo_login_clear_account:
                mUserEditText.setText("");
                break;
            default:
                break;
        }
    }


    private boolean isShow;

    private void submit(View view) {

        // WinguoAccountImpl.login(getApplicationContext(), "18602029479", "youketao1", StartApp.mKey);

        final String pwd = mPwdEditText.getText().toString().trim();
        final String name = mUserEditText.getText().toString().trim();

        if (pwd == null || name == null || pwd.length() == 0 || name.length() == 0) {
            String info = getString(R.string.hybrid4_account_userinfo_empty);
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        } else {

            if (mIsSwitchAccount) {
                //  mAccountMgr.switchLogin(name, pwd, new LoginCallBack(view));
            } else {
                showDialog(LOGIN_DIALOG);
                if (NetworkUtils.isConnectNet(this)) {
                    isShow = true;
                    WinguoAcccountManagerUtils.getKey(this, new WinguoAcccountManagerUtils.IPublicKey() {
                        @Override
                        public void publicKey(WinguoAccountKey key) {
                            StartApp.mKey = key;
                            //WinguoAccountImpl.login(getApplicationContext(), name, pwd, key);
                            WinguoAcccountManagerUtils.login(WinguoLoginActivity.this,name,pwd,key,true,WinguoLoginActivity.this);
                        }

                        @Override
                        public void publicKeyErrorMsg(int error) {

                            removeDialog(LOGIN_DIALOG);
                            Toast.makeText(WinguoLoginActivity.this, getString(R.string.no_net_or_service_no), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    removeDialog(LOGIN_DIALOG);
                    Toast.makeText(this, getString(R.string.feedback_submit_error), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isMySpace) {
            isMySpace = false;
            startActivity(new Intent(this,MainActivity.class));
            finish();
            return;
        }
        if (isShow) {
            removeDialog(LOGIN_DIALOG);
            isShow = false;
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_REGISTER:
                    setResult(resultCode);
                    finish();
                    break;
                case FORGET_PWD:
                    if (data != null) {
                        String newPwd = data.getStringExtra("newPwd");
                        mPwdEditText.setText(newPwd);
                    }
                    break;
                default:
                    break;
            }
        }
    }



    private static final int LOGIN_DIALOG = 7;

    @Override
    public void takeWinguoGeneralResult(int code, boolean isSuccess) {
        removeDialog(LOGIN_DIALOG);
        switch (code) {
            case 0:
                //成功
                break;
            case GBAccountError.REQUST_TIMEOUT:
                //请求超时 登录失败
                ToastUtil.showToast(this,getString(R.string.no_net_or_service_no));
                break;
            case GBAccountError.SESSION_TIMEOUT:
                //登陆过期
                ToastUtil.showToast(this,getString(R.string.session_out));
                break;
            case -3:
                //登陆失败
                ToastUtil.showToast(this,"用户名和密码输入错误！");
                break;
            case -2:
                ToastUtil.showToast(this,"登录密码超过6次输入错误，2小时候再试！");
                break;
            default:
                break;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("landscape")) {
                removeDialog(LOGIN_DIALOG);
                isShow = false;
                WinguoAccountGeneral info = (WinguoAccountGeneral) intent.getSerializableExtra("info");

                ToastUtil.showToast(context, info.accountName + "登陆成功");
                // TODO: 2017/5/6
                Intent startintent = null;
                String Extera = flagIntent.getStringExtra("putExtra");
                String storeid = flagIntent.getStringExtra("storeid");
                String shopid = flagIntent.getStringExtra("shopid");
                if (Extera != null) {
                    switch (Extera) {

                        case Constants.LOGIN_WAY_WALLET://钱包
                            startintent = new Intent(mContext, MyWalletCenterActivity.class);
                            break;
                        case Constants.LOGIN_WAY_FEEDBACK://意见反馈
                            startintent = new Intent(mContext, FeedBackActivity.class);
                            break;
                        case Constants.LOGIN_WAY_OPEN_SPACE://开通空间站
                            startintent = new Intent(mContext,OpenSpaceActivity.class);
                            break;
                        case Constants.LOGIN_WAY_QR://二维码
                            startintent = new Intent(mContext, MyQRCodeActivity.class);
                            break;
                        case Constants.LOGIN_WAY_PAY_PWD_MOD://支付密码修改
                            startintent = new Intent(mContext, ModifyPayWayActivity.class);
                            break;
                        case Constants.LOGIN_WAY_MEASSGE://动态
                            startintent = new Intent(mContext, FriendsDynamicActivity.class);
                            break;
                        case Constants.LOGIN_SETTING://设置中心
                            startintent = new Intent(mContext, SetCenterActivity.class);
                            break;
                        case Constants.ACCOUNT_SAFE://账户与安全
                            startintent = new Intent(mContext, AccountSafeActivity.class);
                            break;
                        case Constants.LOGIN_WAY_CART://购物车
                            startintent = new Intent(mContext, CartActivity.class);
                            break;
                        case Constants.LOGIN_WAY_HISTORY_LOG://购物车
                            startintent = new Intent(mContext, HistoryLogActivity.class);
                            break;
                        case Constants.LOGIN_WAY_BANK_CARD://银行卡列表
                            startintent = new Intent(mContext, AccountBankCardGettingActivity.class);
                            break;
                        case Constants.LOGIN_WAY_ALL_ORDER://全部订单
                            startintent = new Intent();
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 0);
                            startintent.setClass(mContext, MyOrderActivity.class);
                            break;
                        case Constants.LOGIN_WAY_WAIT_PAY://待付款
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 1);
                            break;
                        case Constants.LOGIN_WAY_WAIT_DELIVER_GOODS://待发货
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 2);
                            break;
                        case Constants.LOGIN_WAY_WAIT_TAKE_DELIVER_GOODS://待收货
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 3);
                            break;
                        case Constants.LOGIN_WAY_WAIT_EVALUATE://待评价
                            startintent = new Intent();
                            startintent.setClass(mContext, MyOrderActivity.class);
                            startintent.putExtra(ActionUtil.ACTION_ORDER_STATUS, 4);
                            break;
                        case Constants.LOGIN_WAY_GOOD_ATTENTION://商品收藏
                            startintent = new Intent();
                            startintent.putExtra("collect", 1);
                            startintent.setClass(mContext, MyCollectActivity.class);
                            break;
                        case Constants.LOGIN_WAY_STORE_ATTENTION://店铺收藏
                            startintent = new Intent();
                            startintent.putExtra("collect", 2);
                            startintent.setClass(mContext, MyCollectActivity.class);
                            break;
                        case Constants.LOGIN_WAY_MY_PARTNER://我的伙伴
                            startintent = new Intent();
                            startintent.setClass(mContext, MyPartnerActivity.class);
                            break;
                        case Constants.NEARBY_STORE_HOME_CART://附近实体店 底部购物车
                            startintent = new Intent();
                            startintent.putExtra("store_id",storeid);
                            startintent.setClass(mContext, NearbyStoreHomeActivity.class);
                            break;
                        case Constants.NEARBY_STORE_SHOP_SPEC://商品详情 选着规格
                            startintent = new Intent();
                            startintent.putExtra("store_id",storeid);
                            startintent.putExtra("shop_id",shopid);
                            startintent.setClass(mContext, ShopDetailActivity.class);
                            break;

                        default:
                    }
                    startActivity(startintent);
                }
                finish();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
