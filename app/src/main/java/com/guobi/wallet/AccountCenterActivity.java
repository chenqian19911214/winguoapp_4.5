package com.guobi.wallet;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountCallback;
import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountInfo;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.GBAccountStatus;
import com.guobi.account.NetworkUtils;
import com.guobi.account.WinguoAccountBalance;
import com.guobi.account.WinguoAccountCurrency;
import com.guobi.util.BitmapDecoder;
import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.base.GBBaseActivity;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.ToastUtil;
import com.winguo.utils.WinguoAcccountManagerUtils;
import com.winguo.view.ExpandCollapseAnimation;

import java.util.ArrayList;

/**
 * 用户钱包中心中心
 */
@Deprecated
public class AccountCenterActivity extends GBBaseActivity implements OnClickListener {
    private ArrayList<AccountItem> mData = new ArrayList<AccountItem>();

    private LinearLayout mLayout;
    private View lastOpen;
    private AccountMenuPopupWindow mMenuPopupWindow;
    private View mMenuTarget;
    private ImageView mRefresh;
    private TextView mStatusView;
    private TextView mUserView;
    private ImageView mLoginTitleBGView;
    private TextureView textureView;

    private ImageView mHeadIcon;

    private Button mLogin;
    private Bitmap mLoginTitleBGBitmap;
    // private LinearLayout mAssistLayout;

    private GBAccountMgr mAccountMgr;
    private GBAccountInfo mAccountInfo;
    private LinearLayout bankSetting;
    private ObjectAnimator rotate;
    private LinearLayout hybrid4_account_content_container;

    // private static final int REQUEST_LOGIN = 3;
    // private static final int LOGOUT_DIALOG = 7;

    // private static final int REFRESH_SUC = 0;
    // private static final int QUIT_OVER = 1;
    // private static final int STATUS_CHANGED = 2;

    // private static final int FREE_REGISTER = 0;
    // private static final int CHANGE_ACCOUNT = 1;
    // private static final int CHANGE_PASSWORD = 2;
    //
    // private static final int FEEDBACK = 3;
    // private static final int HELP = 4;
    // private static final int QUIT = 5;

    // private static final int FREE_REGISTER = 0;
    // private static final int CHANGE_ACCOUNT = 1;
    //
    // private static final int FEEDBACK = 2;
    // private static final int QUIT = 3;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        CommonUtil.stateSetting(this,R.color.white_top_color);
        setContentView(R.layout.hybrid4_account_usercenter_layout);
        mAccountMgr = GBAccountMgr.getInstance();
        registerReceiver();
        initViews();
        if (NetworkUtils.isConnectNet(this)) {
            LoadDialog.show(AccountCenterActivity.this);
            requestData();
        } else {
            ToastUtil.showToast(AccountCenterActivity.this,getString(R.string.timeout));
            initData();
            initLayout();
        }
    }

    private void requestData() {
        WinguoAcccountManagerUtils.takeUserBalnce(AccountCenterActivity.this, new WinguoAcccountManagerUtils.BalanceData() {
            @Override
            public void getBalance(WinguoAccountBalance winguoAccountBalance) {

                mAccountMgr.mAccountInfo.winguoBalance = winguoAccountBalance;

                WinguoAcccountManagerUtils.takeUserCurrency(AccountCenterActivity.this, new WinguoAcccountManagerUtils.UserCurrencyData() {
                    @Override
                    public void getCurrency(WinguoAccountCurrency currency) {
                        mAccountMgr.mAccountInfo.winguoCurrency = currency;
                        LoadDialog.dismiss(AccountCenterActivity.this);
                        initData();
                        initLayout();
                        hybrid4_account_content_container.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void currencyErrorMsg(int message) {
                        LoadDialog.dismiss(AccountCenterActivity.this);
                        ToastUtil.showToast(AccountCenterActivity.this, GBAccountError.getErrorMsg(AccountCenterActivity.this,message));
                        initData();
                        initLayout();
                        hybrid4_account_content_container.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void balanceErrorMsg(int message) {
                LoadDialog.dismiss(AccountCenterActivity.this);
                ToastUtil.showToast(AccountCenterActivity.this, GBAccountError.getErrorMsg(AccountCenterActivity.this,message));
                initData();
                initLayout();
                hybrid4_account_content_container.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initViews() {

        mLayout = (LinearLayout) findViewById(R.id.hybrid4_account_ac_layout);
        hybrid4_account_content_container = (LinearLayout) findViewById(R.id.hybrid4_account_content_container);
        bankSetting = (LinearLayout) findViewById(R.id.winguo_left_menu_wallet);
        bankSetting.setOnClickListener(this);

        findViewById(R.id.hybrid4_account_ac_back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.hybrid4_account_ac_back_title);
        title.setText(R.string.hybrid4_account_my_wallet);

        View bankCard = findViewById(R.id.hybrid4_account_fun_bank_card);
        bankCard.setOnClickListener(this);


        mRefresh = (ImageView) findViewById(R.id.hybrid4_account_ac_refresh);
        mRefresh.setOnClickListener(this);
        rotate =  ObjectAnimator.ofFloat(mRefresh, "rotation", 0, 360f).setDuration(1500);

        mMenuTarget = findViewById(R.id.hybrid4_account_menu);
        mMenuTarget.setOnClickListener(this);


    }

    private void dismissFuncNew(View view) {
        View newHintView = view.findViewById(R.id.hybrid4_account_fun_new_hint);
        newHintView.setVisibility(View.GONE);
    }


    private void headIconSet(byte[] bufs) {
        final int length = bufs.length;

        Bitmap bitmap = BitmapFactory.decodeByteArray(bufs, 0, length);

        if (bitmap != null) {
            int shadow = (int) getResources().getDimension(R.dimen.hybrid4_account_head_shadow_radius);
            int size = (int) getResources().getDimension(R.dimen.hybrid4_account_head_icon_size);
            Bitmap bit = Bitmap.createBitmap(size, size, Config.ARGB_4444);

            size -= shadow;

            Canvas canvas = new Canvas(bit);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawARGB(0, 0, 0, 0);

            Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint1.setColor(Color.WHITE);
            paint1.setShadowLayer((int) (shadow * 0.6f), (int) (shadow * 0.4f), (int) (shadow * 0.6f), Color.BLACK);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.drawCircle(size / 2, size / 2, size / 2, paint1);
            canvas.restore();

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);

            int sc = canvas.saveLayer(0, 0, size, size, null,
                    Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

            canvas.drawCircle(size / 2, size / 2, size / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            int b = size, r = size, t = 0, l = 0;

            float per = (float) bw / bh;

            if (per < 0.8 || per > 1.25) { // 不是所有都拉伸
                if (per > 1) {
                    float w = (float) size / per;
                    t = (int) ((size - w) / 2);
                    b = (int) (w + t);
                } else {
                    float h = (float) per * size;
                    l = (int) ((size - h) / 2);
                    r = (int) (l + h);
                }
            }

            canvas.drawBitmap(bitmap, new Rect(0, 0, bw, bh), new Rect(l, t, r, b), paint);
            canvas.restoreToCount(sc);

            mHeadIcon.setImageBitmap(bit);
            bitmap.recycle();
        } else {
            mHeadIcon.setImageResource(R.drawable.hybrid4_account_user_head);
        }
    }

    private void initLayout() {
        final int count = mData.size();

        for (int i = 0; i < count; i++) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.hybrid4_account_uc_expandable_item, null);
            Holder holder = new Holder();

            holder.name = (TextView) layout.findViewById(R.id.hybrid4_account_ac_item_name);
            holder.money = (TextView) layout.findViewById(R.id.hybrid4_account_ac_item_accont);
            holder.info = (TextView) layout.findViewById(R.id.hybrid4_account_ac_item_info);
            holder.state = (ImageView) layout.findViewById(R.id.hybrid4_account_ac_item_state);

            holder.des = (TextView) layout.findViewById(R.id.hybrid4_account_content_des_textview);

            AccountItem item = mData.get(i);
            holder.name.setText(item.name);
            holder.money.setText(item.account);

            if (item.des != null)
                holder.des.setText(item.des);
            else
                holder.des.setText("");

            View pandaView = layout.findViewById(R.id.hybrid4_account_expandable);
            pandaView.setVisibility(View.GONE);

            View content = layout.findViewById(R.id.hybrid4_account_expandable_toggle);
            Drawable drawable = getStateLisDrawable(Color.TRANSPARENT, R.drawable.hybrid4_account_ac_content_pressed);
            content.setBackgroundDrawable(drawable);
            // 暂时屏蔽详情
            holder.info.setVisibility(View.INVISIBLE);
            holder.state.setVisibility(View.INVISIBLE);
            //content.setOnClickListener(this);

            layout.setTag(holder);

            mLayout.addView(layout);

            if (i != count - 1) {
                View v = new View(this);
                // v.setBackgroundResource(R.drawable.hybrid4_account_ac_content_bottom_divider);
                v.setBackgroundColor(0xffcecece);
                mLayout.addView(v, new LinearLayout.LayoutParams(-1, DpToPx(this, 1)));
            } else {
                View v = new View(this);
                v.setBackgroundResource(R.drawable.hybrid4_account_ac_content_bottom_divider);
                mLayout.addView(v, new LinearLayout.LayoutParams(-1, DpToPx(this, 2)));
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case AccountMenuPopupWindow.LOGOUT_DIALOG:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                String message = getString(R.string.hybrid4_account_info_logout);
                dialog.setMessage(message);

                return dialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GBAccountMgr.ACTION_ACCOUNT_STATUS_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void unregisterReceiver() {
        // Log.e("dsds", "unregisterReceiver");
        unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(GBAccountMgr.ACTION_ACCOUNT_STATUS_CHANGED)) {

                mHandler.sendEmptyMessage(AccountMenuPopupWindow.STATUS_CHANGED);
            }
        }

    };

    private void accountStatusChange() {
        changeData();
        invalidateViews();
    }

    private void invalidateViews() {
        final int count = mLayout.getChildCount();
        int num = 0;

        for (int i = 0; i < count; i++) {
            View view = mLayout.getChildAt(i);
            if (!(view instanceof LinearLayout)) {
                num -= 1;
                continue;
            }

            LinearLayout layout = (LinearLayout) view;
            AccountItem accountItem = mData.get(i + num);

            Holder holder = (Holder) layout.getTag();
            holder.money.setText(accountItem.account);

            if (accountItem.des != null)
                holder.des.setText(accountItem.des);
            else
                holder.des.setText("");
        }
    }

    private int DpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private StateListDrawable getStateLisDrawable(int color, int resId) {
        Drawable normal = new ColorDrawable(color);
        Drawable pressed = getResources().getDrawable(resId);

        return getNewStateListDrawable(normal, pressed);
    }

    private StateListDrawable getNewStateListDrawable(Drawable drawableNormal, Drawable drawablePress) {
        StateListDrawable listDrawable = new StateListDrawable();

        listDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePress);
        listDrawable.addState(new int[]{android.R.attr.state_selected}, drawablePress);
        listDrawable.addState(new int[]{android.R.attr.state_focused}, drawablePress);
        listDrawable.addState(new int[]{}, drawableNormal);

        return listDrawable;
    }

    private void changeData() {
        mAccountInfo = mAccountMgr.getAccountInfo();
        AccountItem item = mData.get(0);

        if (mAccountInfo.winguoBalance != null && mAccountInfo.status == GBAccountStatus.usr_logged) {
            WinguoAccountBalance accountBalance = mAccountInfo.winguoBalance;
            item.account = "￥" + accountBalance.balance;
            // StringBuffer buffer = new StringBuffer();
            // buffer.append(accountBalance.incomeDescription)

            item.des = accountBalance.incomeDescription;
        } else {
            item.account = "-";
            item.des = "";
        }

        AccountItem item2 = mData.get(1);
        if (mAccountInfo.winguoCurrency != null && mAccountInfo.status == GBAccountStatus.usr_logged) {
            item2.account = String.valueOf((int) mAccountInfo.winguoCurrency.avaliable);
            StringBuffer buffer = new StringBuffer();

            buffer.append(getString(R.string.hybrid4_account_gb_all)).append(" : ")
                    .append((int) mAccountInfo.winguoCurrency.all).append("\n")
                    .append(getString(R.string.hybrid4_account_gb_frozen)).append(" : ")
                    .append((int) mAccountInfo.winguoCurrency.frozen);

            item2.des = buffer.toString();
        } else {
            item2.account = "-";
            item2.des = "";
        }

    }

    private void initData() {
        AccountItem item = new AccountItem();
        item.name = getString(R.string.hybrid4_account_balance);
        mAccountInfo = mAccountMgr.getAccountInfo();
        if (mAccountInfo.winguoBalance != null) {
            WinguoAccountBalance accountBalance = mAccountInfo.winguoBalance;
            item.account = "￥" + accountBalance.balance;
            // StringBuffer buffer = new StringBuffer();
            // buffer.append(accountBalance.incomeDescription)

            item.des = accountBalance.incomeDescription;
        } else
            item.account = "-";

        mData.add(item);

        AccountItem item2 = new AccountItem();
        item2.name = getString(R.string.hybrid4_account_gb);

        if (mAccountInfo.winguoCurrency != null) {
            item2.account = String.valueOf((int) mAccountInfo.winguoCurrency.avaliable);
            StringBuffer buffer = new StringBuffer();

            buffer.append(getString(R.string.hybrid4_account_gb_all)).append(" : ")
                    .append((int) mAccountInfo.winguoCurrency.all).append("\n")
                    .append(getString(R.string.hybrid4_account_gb_frozen)).append(" : ")
                    .append((int) mAccountInfo.winguoCurrency.frozen);

            item2.des = buffer.toString();
        } else
            item2.account = "-";
        mData.add(item2);

    }

    @Override
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.hybrid4_account_expandable_toggle:
                ViewGroup parent = (ViewGroup) view.getParent();
                View target = parent.findViewById(R.id.hybrid4_account_expandable);

                final int type = target.getVisibility() == View.VISIBLE ? ExpandCollapseAnimation.COLLAPSE
                        : ExpandCollapseAnimation.EXPAND;

                Holder holder = (Holder) parent.getTag();

                if (target == lastOpen) { // 说明关闭
                    lastOpen = null;
                    animateView(target, holder.state, ExpandCollapseAnimation.COLLAPSE);
                    return;
                } else {
                    if (type == ExpandCollapseAnimation.EXPAND) {
                        if (target.getMeasuredHeight() == 0) {
                            target.measure(parent.getMeasuredWidth(), parent.getMeasuredHeight());
                            target.requestLayout();
                        }

                        if (lastOpen != null) {
                            Holder holder2 = (Holder) ((ViewGroup) lastOpen.getParent()).getTag();
                            animateView(lastOpen, holder2.state, ExpandCollapseAnimation.COLLAPSE);
                        }

                        animateView(target, holder.state, type);
                        lastOpen = target;
                    }
                }

                break;
            case R.id.hybrid4_account_ac_back:
                finish();
                break;

            case R.id.hybrid4_account_menu:
                showMenu(view);
                break;


            case R.id.hybrid4_account_ac_refresh:
                if (NetworkUtils.isConnectNet(this)) {
                    if (mAccountInfo.status != GBAccountStatus.usr_logged)
                        return;
                    if (mIsRefreshing)
                        return;
                    refreshAccount(view);
                } else {
                    ToastUtil.showToast(this,getString(R.string.no_net_dialog_title));
                }

                break;
            //银行卡设置
            case R.id.winguo_left_menu_wallet:

                if (mAccountInfo.status == GBAccountStatus.usr_logged) {
                    Intent gettingBankIntent = new Intent(this, AccountBankCardGettingActivity.class);
                    startActivityForResult(gettingBankIntent, AccountMenuPopupWindow.REQUEST_GETTING_BANKCARDS);
                    //view.setEnabled(false);
                    return;
                }
                AccountBankCardActivityManager.getInstance().toast(this.getApplicationContext(),
                        R.string.hybrid4_account_please_login_first);

                break;
            default:
                break;
        }
    }

    private void refreshAccount(final View view) {
        mIsRefreshing = true;
        rotate.start();

        mAccountMgr.refreshAccountInfo(new GBAccountCallback() {

            @Override
            public void onTaskEnd(int resultCode, String errMsg) {
               // rotate.cancel();
                if (resultCode == 0)
                    mHandler.sendEmptyMessage(AccountMenuPopupWindow.REFRESH_SUC);
                else
                    Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();

                mIsRefreshing = false;
            }

            @Override
            public void onTaskBegin() {
            }

            @Override
            public void onTaskCanceled() {
                //animation.cancel();
               // rotate.cancel();
                mIsRefreshing = false;
            }
        });
    }

    private boolean mIsRefreshing = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if (keyCode == KeyEvent.KEYCODE_MENU) {
        // if (mMenuPopupWindow == null || !mMenuPopupWindow.isShowing())
        // showMenu(mMenuTarget);
        // else
        // closeMenu();
        // return true;
        // }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        AccountBankCardActivityManager.getInstance().clear();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        AccountBankCardActivityManager.getInstance().clear();
        super.onDestroy();
        unregisterReceiver();
        if (mLoginTitleBGBitmap != null && !mLoginTitleBGBitmap.isRecycled()) {
            mLoginTitleBGBitmap.recycle();
            mLoginTitleBGBitmap = null;
        }
    }

    private void showMenu(View view) {
        mMenuPopupWindow = new AccountMenuPopupWindow(this);
        mMenuPopupWindow.showAsDropDown(view);
    }

    private void closeMenu() {
        if (mMenuPopupWindow != null && mMenuPopupWindow.isShowing()) {
            mMenuPopupWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AccountMenuPopupWindow.REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    accountStatusChange();
                }
                break;
            case AccountMenuPopupWindow.REQUEST_GETTING_BANKCARDS:

               /* View bankCard = findViewById(R.id.hybrid4_account_fun_bank_card);
                bankCard.setEnabled(true);*/

                break;
            default:
                break;
        }
    }

    private void animateView(final View target, final View state, final int type) {
        final int duration = 350;
        Animation animation = new ExpandCollapseAnimation(target, type);

        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (type == ExpandCollapseAnimation.COLLAPSE) {
                    target.setVisibility(View.GONE);
                }
            }
        });
        animation.setDuration(duration);

        RotateAnimation ani;
        if (type == ExpandCollapseAnimation.EXPAND) {
            ani = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            ani = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        ani.setDuration(duration);
        ani.setFillAfter(true);
        state.startAnimation(ani);

        target.startAnimation(animation);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AccountMenuPopupWindow.REFRESH_SUC: {
                    final String info = getString(R.string.hybrid4_account_info_refresh_success);
                    Toast.makeText(AccountCenterActivity.this, info, Toast.LENGTH_SHORT).show();
                    changeData();
                    invalidateViews();
                    break;
                }
                case AccountMenuPopupWindow.STATUS_CHANGED:
                    accountStatusChange();
                    break;
                default:
                    break;
            }
        }

    };

    class Holder {
        TextView name;
        TextView money;
        TextView info;
        ImageView state;

        TextView des;
    }

    class AccountItem {
        String name;
        String account;
        String des;
    }

    private Bitmap getBitmap(int width, int height, int resID, Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resID, width, height, config, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
