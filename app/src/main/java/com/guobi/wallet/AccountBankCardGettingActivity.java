package com.guobi.wallet;

import com.guobi.account.GBAccountCallback;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountBank;
import com.guobi.account.WinguoAccountBankCard;
import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.base.GBBaseActivity;
import com.winguo.utils.CommonUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 获取用户绑 定银行卡信息
 * （如没有添加银行卡 跳转AccountBankCardNoMessageActivity
 * 如有跳转AccountBankCardMessageActivity）
 */
public class AccountBankCardGettingActivity extends GBBaseActivity implements OnClickListener {

    private GBAccountMgr mAccountMgr;
    private AccountBankCardActivityManager mAccountActivityMgr;

    private ArrayList<WinguoAccountBank> banks;
    private ArrayList<WinguoAccountBankCard> cards;

    private BankCardCallBack mBankCardCallBack = new BankCardCallBack();
    private BankCallBack mBankCallBack = new BankCallBack();
    private DetailBankCardCallBack mDetailBankCardCallBack = new DetailBankCardCallBack();

    private int mDetailBankCardIndex = 0;

    Object mTrashLock = new Object();
    boolean isTrash = false;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hybrid4_account_bank_getting_layout);
        CommonUtil.stateSetting(this,R.color.white_top_color);

        mAccountMgr = GBAccountMgr.getInstance();
        mAccountActivityMgr = AccountBankCardActivityManager.getInstance();
        mAccountActivityMgr.pushActivity(this);
        mAccountActivityMgr.popActivityExcept(this.getClass());

        banks = mAccountActivityMgr.getBanks();
        cards = mAccountActivityMgr.getCards();
        initViews();
        getMessages();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initViews() {
        findViewById(R.id.hybrid4_account_ac_back).setOnClickListener(this);
        //Typeface typeface = mPreferences.getCurTypeface();

        TextView title = (TextView) findViewById(R.id.hybrid4_account_ac_back_title);
        title.setText(R.string.hybrid4_account_my_bank_card);
        //title.setTypeface(typeface);

        TextView msg = (TextView) findViewById(R.id.hybrid4_account_progress_message);
        //msg.setTypeface(typeface);

        findViewById(R.id.hybrid4_account_menu).setVisibility(View.GONE);
    }

    private void getMessages() {
        mAccountMgr.cancelOp();

        boolean result = mAccountMgr.getBankList("2", "1", banks, mBankCallBack);
        if (!result) {
            mAccountActivityMgr.showStatusLockErr(this, true);
        }

    }

    /**
     * 银行读取回调
     */
    class BankCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {

        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {

            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                if (resultCode != 0) {
                    mAccountActivityMgr.toast(AccountBankCardGettingActivity.this.getApplicationContext(), errMsg);

                    mAccountActivityMgr.popActivity(AccountBankCardGettingActivity.this);
                    return;
                }

                boolean result = mAccountMgr.getBankCardList(cards, mBankCardCallBack);//获取银行卡接口回调
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(AccountBankCardGettingActivity.this, true);
                }
            }

        }

        @Override
        public void onTaskCanceled() {
            // TODO Auto-generated method stub

        }
    }

    /**
     * 银行卡获取回调
     */
    class BankCardCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {

            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                if (resultCode != 0) {
                    cards.clear();
                    mAccountActivityMgr.toast(AccountBankCardGettingActivity.this.getApplicationContext(), errMsg);

                    mAccountActivityMgr.popActivity(AccountBankCardGettingActivity.this);
                    return;
                }
//                banks.addAll(GBAccountMgr.getInstance().mAccountInfo.banks);
                boolean cardEmpty = cards.isEmpty();
                boolean bankEmpty = banks.isEmpty();

                if (cardEmpty && bankEmpty) {  //银行卡、银行数据为空
                    mAccountActivityMgr.toast(AccountBankCardGettingActivity.this.getApplicationContext(),
                            R.string.hybrid4_account_bankcard_readerr);
                    mAccountActivityMgr.popActivity(AccountBankCardGettingActivity.this);
                } else if (cardEmpty) { //银行卡数据空
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.jumpToActivity(AccountBankCardGettingActivity.this,
                                AccountBankCardNoMessageActivity.class);
                    }
                } else {
                    boolean result = mAccountMgr.getBankCardDetail(cards.get(mDetailBankCardIndex),
                            mDetailBankCardCallBack);

                    if (!result) {
                        mAccountActivityMgr.showStatusLockErr(AccountBankCardGettingActivity.this, true);
                    }
                }
            }
        }

        @Override
        public void onTaskCanceled() {
            // TODO Auto-generated method stub

        }
    }

    class DetailBankCardCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                if (resultCode != 0) {
                    cards.clear();
                    mAccountActivityMgr.toast(AccountBankCardGettingActivity.this.getApplicationContext(), errMsg);

                    mAccountActivityMgr.popActivity(AccountBankCardGettingActivity.this);
                    return;
                }

                if (mDetailBankCardIndex > cards.size() - 1) {
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.jumpToActivity(AccountBankCardGettingActivity.this,
                                AccountBankCardMessagesActivity.class);
                    }
                } else {
                    boolean result = mAccountMgr.getBankCardDetail(cards.get(mDetailBankCardIndex), this);
                    if (!result) {
                        mAccountActivityMgr.showStatusLockErr(AccountBankCardGettingActivity.this, true);
                    }
                    mDetailBankCardIndex++;
                }
            }
        }

        @Override
        public void onTaskCanceled() {
            // TODO Auto-generated method stub

        }
    }

    @Override
    protected void onDestroy() {
        synchronized (mTrashLock) {
            isTrash = true;
        }
        mAccountActivityMgr.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hybrid4_account_ac_back:
                mAccountActivityMgr.popActivity(this);
                break;
            default:
                break;
        }
    }


}
