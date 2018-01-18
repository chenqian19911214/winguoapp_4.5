package com.guobi.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountCallback;
import com.guobi.account.GBAccountInfo;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.GBAccountStatus;
import com.winguo.R;
import com.winguo.personalcenter.setting.FeedBackActivity;
import com.winguo.login.LoginActivity;
import com.winguo.login.register.RegisterActivity;


public class AccountMenuPopupWindow extends PopupWindow {

    private static final int FREE_REGISTER = 0;
    private static final int CHANGE_ACCOUNT = 1;
    private static final int FEEDBACK = 2;
    private static final int QUIT = 3;


    public static final int REQUEST_LOGIN = 3;
    public static final int REQUEST_GETTING_BANKCARDS = 4;
    public static final int LOGOUT_DIALOG = 7;
    public static final int REQUEST_UPDATE = 8;
    public static final int REQUEST_DELETE = 9;
    public static final int UPDATE_LIST = 10;
    /**
     * 选择省份
     */
    public static final int SELECT_PROVINCE = 11;
    /**
     * 选择城市
     */
    public static final int SELECT_CITY = 12;
    /**
     * 选择支行
     */
    public static final int SELECT_BRANCHES = 13;
    /**
     * 请求省份列表
     */
    public static final int REQUEST_PROVINCE = 14;
    /**
     * 请求城市列表
     */
    public static final int REQUEST_CITY = 15;
    /**
     * 请求支行列表
     */
    public static final int REQUEST_BRANCHES = 16;

    public static final int REFRESH_SUC = 0;
    public static final int QUIT_OVER = 1;
    public static final int STATUS_CHANGED = 2;

    private String[] mMenuArray;

    private Context mContext;


    //private LauncherPreferences mPreferences = LauncherPreferences.getInstance();

    public AccountMenuPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        mMenuArray = context.getResources().getStringArray(R.array.hybrid4_account_menu_array);
        this.setFocusable(true);
        this.setWidth(-2);
        this.setHeight(-2);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hybrid4_account_ac_content_bg));
        this.setContentView(getPopupLayout(null));
    }

    public AccountMenuPopupWindow(Context context, Drawable background, Drawable listDivider) {
        super(context);
        this.mContext = context;
        mMenuArray = context.getResources().getStringArray(R.array.hybrid4_account_menu_array);
        this.setFocusable(true);
        this.setWidth(-2);
        this.setHeight(-2);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(background);
        this.setContentView(getPopupLayout(listDivider));
    }

    private LinearLayout getPopupLayout(Drawable listDivider) {
        final GBAccountMgr accountMgr = GBAccountMgr.getInstance();
        if (accountMgr == null) {
            return null;
        }
        final GBAccountInfo accountInfo = accountMgr.getAccountInfo();
        if (accountInfo == null) {
            return null;
        }

        final String[] array = mMenuArray;
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        final int marginV = DpToPx(mContext, 28);
        final int marginH = DpToPx(mContext, 8);

        final int count = array.length;

        int textSize = 15;

        for (int i = 0; i < count; i++) {
            // 屏蔽切换账号功能
            if (i == 1) {
                continue;
            }
            if (accountInfo.status == GBAccountStatus.initializing && i == 0)
                continue;

//			if (mAccountInfo.status != GBAccountStatus.usr_logged && (i==1 || i==2 || i==5))
//				continue;

            // TODO CHNAGE
            if (accountInfo.status != GBAccountStatus.usr_logged && (i == 1 || i == 3))
                continue;

            String string = array[i];
            TextView textView = new TextView(mContext);

            textView.setTextColor(0xff555555);
            textView.setTextSize(textSize);
            textView.setText(string);
            //textView.setTypeface(mPreferences.getCurTypeface());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);

            // 单独处理"反馈"
            if (i == 2) {
                params.width = -1;
                textView.setPadding(marginV, marginH, 0, marginH);
            } else {
                textView.setPadding(marginV, marginH, marginV, marginH);
            }

            layout.addView(textView, params);

            if (i != count - 1) {
                View v = new View(mContext);
                if (listDivider == null) {
                    v.setBackgroundColor(0xffcecece);
                } else {
                    v.setBackgroundDrawable(listDivider);
                }

                layout.addView(v, new LinearLayout.LayoutParams(-1, DpToPx(mContext, 1)));
            }

            Drawable drawable = getStateLisDrawable(Color.TRANSPARENT, R.drawable.hybrid4_account_ac_content_pressed);
            textView.setBackgroundDrawable(drawable);

            final int num = i;
            textView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    AccountMenuPopupWindow.this.dismiss();

                    switch (num) {
                        case FREE_REGISTER: {
                            Intent intent = new Intent(mContext, RegisterActivity.class);
                            ((Activity) mContext).startActivityForResult(intent, REQUEST_LOGIN);
                            break;
                        }

//					case CHANGE_PASSWORD:
//						
//						break;
                        case FEEDBACK: {
                            Intent intent = new Intent(mContext, FeedBackActivity.class);
                            mContext.startActivity(intent);
                            break;
                        }
//					case HELP:
//						
//						break;

                        case CHANGE_ACCOUNT: {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.putExtra("isswitch", true);
                            ((Activity) mContext).startActivityForResult(intent, REQUEST_LOGIN);
                            break;
                        }
                        case QUIT: {
                            ((Activity) mContext).showDialog(LOGOUT_DIALOG);

                            if (accountMgr != null) {
                                accountMgr.logout(new GBAccountCallback() {

                                    @Override
                                    public void onTaskEnd(int resultCode, String errMsg) {
                                        Message message = new Message();
                                        message.what = QUIT_OVER;
                                        message.arg1 = resultCode;
                                        message.obj = errMsg;

                                        mHandler.sendMessage(message);
                                    }

                                    @Override
                                    public void onTaskBegin() {
                                    }

                                    @Override
                                    public void onTaskCanceled() {
                                        // TODO Auto-generated method stub

                                    }
                                });
                            }
//						
                        }

                        break;
                        default:
                            break;
                    }
                }
            });
        }

        return layout;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AccountMenuPopupWindow.QUIT_OVER:
                    ((Activity) mContext).removeDialog(AccountMenuPopupWindow.LOGOUT_DIALOG);
                    int code = msg.arg1;

                    if (code == 0) {
                        String info = mContext.getString(R.string.hybrid4_account_info_logout_success);
                        Toast.makeText(((ContextWrapper) mContext).getBaseContext(), info, Toast.LENGTH_SHORT).show();
                    } else {
                        String info = (String) msg.obj;
                        Toast.makeText(((ContextWrapper) mContext).getBaseContext(), info, Toast.LENGTH_SHORT).show();
                    }

                    // 不管退出成功与否，都清除银行卡界面产生的activity(如果有),回到中心页
                    AccountBankCardActivityManager.getInstance().popAllActivity();

                    break;
                default:
                    break;
            }
        }

    };

    private int DpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private StateListDrawable getStateLisDrawable(int color, int resId) {
        Drawable normal = new ColorDrawable(color);
        Drawable pressed = mContext.getResources().getDrawable(resId);

        return getNewStateListDrawable(normal, pressed);
    }

    private StateListDrawable getNewStateListDrawable(Drawable drawableNormal,
                                                      Drawable drawablePress) {
        StateListDrawable listDrawable = new StateListDrawable();

        listDrawable.addState(new int[]{android.R.attr.state_pressed},
                drawablePress);
        listDrawable.addState(new int[]{android.R.attr.state_selected},
                drawablePress);
        listDrawable.addState(new int[]{android.R.attr.state_focused},
                drawablePress);
        listDrawable.addState(new int[]{}, drawableNormal);

        return listDrawable;
    }

}
