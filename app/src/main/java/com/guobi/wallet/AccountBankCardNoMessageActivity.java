package com.guobi.wallet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guobi.account.WinguoAccountBank;
import com.guobi.util.DrawableDecoder;
import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.base.GBBaseActivity;
import com.winguo.utils.CommonUtil;
import com.winguo.view.ExpandCollapseAnimation;

/**
 * 无记录
 */
public class AccountBankCardNoMessageActivity extends GBBaseActivity implements OnClickListener {

    private static final int REQUEST_ADDING_CARD = 1;


    private GridView mGridView;
    private View mBankListLayout;

    private AccountMenuPopupWindow mMenuPopupWindow;

    private View lastOpen;


    private ArrayList<WinguoAccountBank> banks;

    private AccountBankCardActivityManager mAccountActivityMgr;

    private boolean mIsResetedListLayout = false;
    private boolean misAdding = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hybrid4_account_bank_no_message_layout);
        CommonUtil.stateSetting(this,R.color.white_top_color);

        mAccountActivityMgr = AccountBankCardActivityManager.getInstance();
        mAccountActivityMgr.pushActivity(this);
        mAccountActivityMgr.popActivityExcept(this.getClass());
        banks = mAccountActivityMgr.getBanks();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.hybrid4_account_ac_back).setOnClickListener(this);
        //Typeface typeface = mPreferences.getCurTypeface();

        TextView title = (TextView) findViewById(R.id.hybrid4_account_ac_back_title);
        title.setText(R.string.hybrid4_account_my_bank_card);
        //title.setTypeface(typeface);

        View mMenuTarget = findViewById(R.id.hybrid4_account_menu);
        mMenuTarget.setOnClickListener(this);

        Button view_add = (Button) findViewById(R.id.hybrid4_account_bank_card_add);
        view_add.setOnClickListener(this);
        //view_add.setTypeface(typeface);

        TextView hint1 = (TextView) findViewById(R.id.hybrid4_account_bankcard_help_hint_1);
        //hint1.setTypeface(typeface);

        TextView hint2 = (TextView) findViewById(R.id.hybrid4_account_bankcard_help_hint_2);
        //hint2.setTypeface(typeface);

        TextView hint3 = (TextView) findViewById(R.id.hybrid4_account_bankcard_help_hint_3);
        //hint3.setTypeface(typeface);

		/*((TextView) findViewById(R.id.hybrid4_account_bankcard_help_desc1_title)).setTypeface(typeface);
        ((TextView) findViewById(R.id.hybrid4_account_bankcard_help_desc1_msg)).setTypeface(typeface);
		((TextView) findViewById(R.id.hybrid4_account_bankcard_help_desc2_title)).setTypeface(typeface);
		((TextView) findViewById(R.id.hybrid4_account_bankcard_help_desc2_msg)).setTypeface(typeface);
		((TextView) findViewById(R.id.hybrid4_account_bankcard_help_desc3_title)).setTypeface(typeface);
		((TextView) findViewById(R.id.hybrid4_account_bankcard_help_desc3_msg)).setTypeface(typeface);*/

        View bankcardInputHelp = findViewById(R.id.hybrid4_account_bankcard_help);
        bankcardInputHelp.setOnClickListener(this);

        mGridView = (GridView) findViewById(R.id.hybrid4_account_bank_list);

        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            try {
                Method method = GridView.class.getMethod("setOverScrollMode", int.class);
                method.invoke(mGridView, View.OVER_SCROLL_NEVER);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            mGridView.setVerticalFadingEdgeEnabled(false);
        }

        BankAdapter mBankAdapter = new BankAdapter();
        mGridView.setAdapter(mBankAdapter);

        mBankListLayout = findViewById(R.id.hybrid4_account_bank_list_layout);
    }

    private void resetListLayoutParams() {

        if (mIsResetedListLayout) {
            return;
        }

        try {
            LinearLayout.LayoutParams bankListLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            bankListLP.leftMargin = DpToPx(this, 12);
            bankListLP.rightMargin = DpToPx(this, 12);
            bankListLP.topMargin = DpToPx(this, 8);
            int measuredHeight = mBankListLayout.getMeasuredHeight();
            bankListLP.height = measuredHeight;
            mBankListLayout.setLayoutParams(bankListLP);
            mIsResetedListLayout = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADDING_CARD:
                misAdding = false;
                break;

            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 重新显示时清除栈中其他的activity
        mAccountActivityMgr.popActivityExcept(this.getClass());
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        mAccountActivityMgr.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hybrid4_account_ac_back:
                mAccountActivityMgr.popActivity(this);
                break;

            case R.id.hybrid4_account_menu:
                showMenu(view);
                break;

            case R.id.hybrid4_account_bank_card_add:
                if (!misAdding) {
                    Intent intent = new Intent(this, AccountBankCardEditActivity.class);
                    intent.putExtra("editType", R.string.hybrid4_account_bankcard_add);
                    startActivityForResult(intent, REQUEST_ADDING_CARD);
                    misAdding = true;
                }
                break;

            case R.id.hybrid4_account_bankcard_help:
                resetListLayoutParams();

                ViewGroup parent = (ViewGroup) view.getParent().getParent();
                View target = parent.findViewById(R.id.hybrid4_account_bankcard_help_desc);

                if (target == lastOpen) {
                    lastOpen = null;
                    animateView(target, ExpandCollapseAnimation.COLLAPSE);
                } else {
                    if (lastOpen != null) {
                        animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
                    }

                    if (target.getMeasuredHeight() == 0) {
                        target.measure(parent.getMeasuredWidth(), parent.getMeasuredHeight());
                        target.requestLayout();
                    }

                    animateView(target, ExpandCollapseAnimation.EXPAND);
                    lastOpen = target;
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // switch (keyCode) {
        // case KeyEvent.KEYCODE_MENU:
        // if (mMenuPopupWindow == null || !mMenuPopupWindow.isShowing()) {
        // showMenu(mMenuTarget);
        // } else {
        // closeMenu();
        // }
        // return true;
        // default:
        // break;
        // }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu(View view) {
        mMenuPopupWindow = new AccountMenuPopupWindow(this);
        mMenuPopupWindow.showAsDropDown(view);
    }

    private void closeMenu() {
        if (mMenuPopupWindow != null) {
            mMenuPopupWindow.dismiss();
        }
    }

    private class BankAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return banks.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {

                convertView = LayoutInflater.from(AccountBankCardNoMessageActivity.this)
                        .inflate(R.layout.hybrid4_account_bank_item2, null);

                holder = new Holder();
                holder.bank = (ImageView) convertView.findViewById(R.id.hybrid4_account_bank_image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            WinguoAccountBank bank = banks.get(position);
            holder.bank.setImageDrawable(getDrawablebyName("hybrid4_account_bank_" + bank.id));

            int height = mGridView.getMeasuredHeight() / (int) (Math.ceil(banks.size() * 1.0f / 3));

            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);

            convertView.setLayoutParams(lp);

            return convertView;
        }

    }

    class Holder {
        ImageView bank;
    }

    private void animateView(final View target, final int type) {

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

        target.startAnimation(animation);
    }

    private int DpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private Drawable getDrawablebyName(String imageResourceName) {
        return DrawableDecoder.createFromResource(this, imageResourceName);
    }
}
