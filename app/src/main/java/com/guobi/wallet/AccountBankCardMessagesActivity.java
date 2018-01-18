package com.guobi.wallet;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guobi.account.GBAccountCallback;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountBank;
import com.guobi.account.WinguoAccountBankCard;
import com.guobi.util.DrawableDecoder;
import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.base.GBBaseActivity;
import com.winguo.utils.CommonUtil;

/**
 * 用户银行卡列表
 */
public class AccountBankCardMessagesActivity extends GBBaseActivity implements OnClickListener {

    private static final int REQUEST_EDIT_CARD = 0;
    private static final int REQUEST_DELETE_CARD = 1;
    private static final int REQUEST_CARD_ERROR = 2;

    private View mMenuTarget;
    private ListView mListView;
    private View mUpdateLayout;
    private Button mButtonAdd;

    private BankcardAdapter mBankcardAdapter;

    //private LauncherPreferences mPreferences = LauncherPreferences.getInstance();

    private AccountMenuPopupWindow mMenuPopupWindow;

    private ArrayList<WinguoAccountBankCard> cards;
    private ArrayList<WinguoAccountBank> banks;
    private WinguoAccountBankCard tryDeletCard;

    private GBAccountMgr mAccountMgr;
    private AccountBankCardActivityManager mAccountActivityMgr;

    Object mTrashLock = new Object();
    boolean isTrash = false;

    private int mDetailBankCardIndex = 0;

    private boolean mIsEditing = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hybrid4_account_bank_message_layout);
        CommonUtil.stateSetting(this,R.color.white_top_color);

        mAccountMgr = GBAccountMgr.getInstance();
        mAccountActivityMgr = AccountBankCardActivityManager.getInstance();
        mAccountActivityMgr.pushActivity(this);
        mAccountActivityMgr.popActivityExcept(this.getClass());

        banks = mAccountActivityMgr.getBanks();
        cards = mAccountActivityMgr.getCards();

        initViews();
    }

    private void initViews() {
        //Typeface typeface = mPreferences.getCurTypeface();

        mButtonAdd = (Button) findViewById(R.id.hybrid4_account_bank_card_add);
        mButtonAdd.setOnClickListener(this);
        //mButtonAdd.setTypeface(typeface);

        TextView errMsg = (TextView) findViewById(R.id.hybrid4_account_bankcard_msg);
        //errMsg.setTypeface(typeface);

        findViewById(R.id.hybrid4_account_ac_back).setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.hybrid4_account_ac_back_title);
        title.setText(R.string.hybrid4_account_my_bank_card);
        //title.setTypeface(typeface);

        mUpdateLayout = findViewById(R.id.hybrid4_account_bankcard_update_layout);
        Button updateBtn = (Button) findViewById(R.id.hybrid4_account_bankcard_update);
        updateBtn.setOnClickListener(this);
        //updateBtn.setTypeface(typeface);

        mListView = (ListView) findViewById(R.id.hybrid4_account_bank_card_list);

        mBankcardAdapter = new BankcardAdapter();
        mListView.setAdapter(mBankcardAdapter);

        mMenuTarget = findViewById(R.id.hybrid4_account_menu);
        mMenuTarget.setOnClickListener(this);

        updateState();
    }

    private void updateState() {
        if (cards.isEmpty()) {
            mListView.setVisibility(View.GONE);
            mButtonAdd.setVisibility(View.INVISIBLE);
            mUpdateLayout.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mButtonAdd.setVisibility(View.VISIBLE);
            mUpdateLayout.setVisibility(View.GONE);
        }
    }

    private void updataData() {
        if (mBankcardAdapter != null) {
            mBankcardAdapter.notifyDataSetChanged();
        }
        updateState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_EDIT_CARD:
                mIsEditing = false;
                break;
            case REQUEST_DELETE_CARD:
                mIsEditing = false;
                if (resultCode == Activity.RESULT_OK) {
                    deleteCrad();
                }
            case REQUEST_CARD_ERROR:
                mIsEditing = false;
                if (resultCode == Activity.RESULT_OK) {
                    deleteCrad();
                }
                break;
            default:
                break;
        }

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAccountActivityMgr.popActivityExcept(this.getClass());
        updataData();
        MobclickAgent.onResume(this);
    }

	/*
     * @Override protected void onRestart() { super.onRestart(); //
	 * 重新显示时清除栈中其他的activity
	 * mAccountActivityMgr.popActivityExcept(this.getClass()); updataData(); }
	 */

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

                if (!mIsEditing) {
                    Intent intent = new Intent(this, AccountBankCardEditActivity.class);
                    intent.putExtra("editType", R.string.hybrid4_account_bankcard_add);
                    startActivityForResult(intent, REQUEST_EDIT_CARD);
                    mIsEditing = true;
                }
                break;

            case R.id.hybrid4_account_bankcard_item_editcard:
                if (!mIsEditing) {
                    Object tag = view.getTag();
                    if (tag != null) {
                        Intent intent = new Intent(this, AccountBankCardEditActivity.class);
                        intent.putExtra("editType", R.string.hybrid4_account_bankcard_alter);
                        intent.putExtra("cardIndex", (Integer) tag);
                        startActivityForResult(intent, REQUEST_EDIT_CARD);
                        mIsEditing = true;
                    }
                }
                break;

            case R.id.hybrid4_account_bankcard_item_deletecard:
                if (!mIsEditing) {
                    Object tag = view.getTag();
                    if (tag != null) {
                        deleteBankCard((Integer) tag);
                    }
                }
                break;

            case R.id.hybrid4_account_bankcard_update:
                // 重新读取银行卡列表
                boolean result = mAccountMgr.getBankCardList(cards, new BankCardCallBack());
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(this, false);
                    removeDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        synchronized (mTrashLock) {
            isTrash = true;
        }
        mAccountActivityMgr.removeActivity(this);
        super.onDestroy();
        removeDialog();
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

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        String message;
        switch (id) {
            case AccountMenuPopupWindow.LOGOUT_DIALOG:
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                message = getString(R.string.hybrid4_account_info_logout);
                dialog.setMessage(message);

                return dialog;
            case AccountMenuPopupWindow.REQUEST_DELETE:
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                message = getString(R.string.hybrid4_account_bankcard_deleting);
                dialog.setMessage(message);
                return dialog;

            case AccountMenuPopupWindow.UPDATE_LIST:
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                message = getResources().getString(R.string.hybrid4_account_bankcard_updating_list);
                dialog.setMessage(message);

                return dialog;
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void deleteBankCard(int cardIndex) {
        if (cardIndex >= 0 && cardIndex < cards.size()) {
            WinguoAccountBankCard card = cards.get(cardIndex);
            showDeleteDialog(card);
        }
    }

    private void showDeleteDialog(final WinguoAccountBankCard card) {
        tryDeletCard = card;

        if (card == null) {
            mAccountActivityMgr.toast(this.getApplicationContext(), R.string.hybrid4_account_bankcard_readerr);
            mIsEditing = false;
            return;
        }

        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append("************").append(card.cardNumberTail);

        StringBuilder message = new StringBuilder().append(card.bankName).append("\n").append(cardNumber);

        Intent intent = new Intent(this, SimpleYesNoDialog.class);
        intent.putExtra("title", getResources().getString(R.string.hybrid4_account_bankcard_confirm_delete));
        intent.putExtra("msg", message.toString());
        this.startActivityForResult(intent, REQUEST_DELETE_CARD);
        mIsEditing = true;
    }

    private void deleteCrad() {
        if (tryDeletCard == null) {
            return;
        }
        boolean result = mAccountMgr.deleteBankCard(tryDeletCard.p_bid, new DeleteCardCallBack());
        if (!result) {
            mAccountActivityMgr.showStatusLockErr(AccountBankCardMessagesActivity.this, false);
            removeDialog();
        }
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

    /**
     * 删除银行卡回调
     */
    private class DeleteCardCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            AccountBankCardMessagesActivity.this.showDialog(AccountMenuPopupWindow.REQUEST_DELETE);
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }
                if (resultCode != 0) {
                    mAccountActivityMgr.toast(AccountBankCardMessagesActivity.this.getApplicationContext(), errMsg);

                    AccountBankCardMessagesActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_DELETE);
                    return;
                }
                mAccountActivityMgr.toast(AccountBankCardMessagesActivity.this.getApplicationContext(),
                        R.string.hybrid4_account_bankcard_delete_succeed);
                // 重新读取银行卡列表
                boolean result = mAccountMgr.getBankCardList(cards, new BankCardCallBack());
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(AccountBankCardMessagesActivity.this, true);
                }
            }
        }

        @Override
        public void onTaskCanceled() {
            // TODO Auto-generated method stub

        }
    }

    /**
     * 读取银行卡回调
     */
    class BankCardCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            AccountBankCardMessagesActivity.this.showDialog(AccountMenuPopupWindow.UPDATE_LIST);
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {

            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                if (resultCode != 0) {
                    mAccountActivityMgr.toast(AccountBankCardMessagesActivity.this.getApplicationContext(), errMsg);

                    mAccountActivityMgr.clearDatas();
                    // 删除成功，刷新列表失败
                    updataData();
                    removeDialog();
                    return;
                }

                boolean cardEmpty = cards.isEmpty();
                boolean bankEmpty = banks.isEmpty();

                if (cardEmpty && bankEmpty) {
                    mAccountActivityMgr.toast(AccountBankCardMessagesActivity.this.getApplicationContext(),
                            R.string.hybrid4_account_bankcard_readerr);

                    updataData();
                    removeDialog();
                } else if (cardEmpty) {
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.jumpToActivity(AccountBankCardMessagesActivity.this,
                                AccountBankCardNoMessageActivity.class);
                    }
                } else {
                    mDetailBankCardIndex = 0;
                    boolean result = mAccountMgr.getBankCardDetail(cards.get(mDetailBankCardIndex),
                            new DetailBankCardCallBack());
                    if (!result) {
                        mAccountActivityMgr.showStatusLockErr(AccountBankCardMessagesActivity.this, true);
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
                    mAccountActivityMgr.toast(AccountBankCardMessagesActivity.this.getApplicationContext(), errMsg);

                    mAccountActivityMgr.clearDatas();
                    updataData();
                    removeDialog();
                    return;
                }

                if (mDetailBankCardIndex > cards.size() - 1) {
                    updataData();
                    removeDialog();
                } else {
                    boolean result = mAccountMgr.getBankCardDetail(cards.get(mDetailBankCardIndex), this);
                    if (!result) {
                        mAccountActivityMgr.showStatusLockErr(AccountBankCardMessagesActivity.this, true);
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

    private void removeDialog() {
        AccountBankCardMessagesActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_DELETE);
        AccountBankCardMessagesActivity.this.removeDialog(AccountMenuPopupWindow.UPDATE_LIST);
    }

    private class BankcardAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cards.size();
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
                convertView = LayoutInflater.from(AccountBankCardMessagesActivity.this)
                        .inflate(R.layout.hybrid4_account_bankcard_item, null);
                holder = new Holder();
                //Typeface curTypeface = mPreferences.getCurTypeface();

                holder.bankIcon = (ImageView) convertView.findViewById(R.id.hybrid4_account_bankcard_item_image);

                holder.bankName = (TextView) convertView.findViewById(R.id.hybrid4_account_bankcard_item_bankname);
                //holder.bankName.setTypeface(curTypeface);

                holder.cardNumber = (TextView) convertView.findViewById(R.id.hybrid4_account_bankcard_item_cardnumber);
                //holder.cardNumber.setTypeface(curTypeface);

                holder.normalCard = (TextView) convertView.findViewById(R.id.hybrid4_account_bankcard_item_normalcard);
                //holder.normalCard.setTypeface(curTypeface);

                holder.editCard = (Button) convertView.findViewById(R.id.hybrid4_account_bankcard_item_editcard);
                //holder.editCard.setTypeface(curTypeface);
                holder.editCard.setOnClickListener(AccountBankCardMessagesActivity.this);

                holder.deleteCard = (Button) convertView.findViewById(R.id.hybrid4_account_bankcard_item_deletecard);
                //holder.deleteCard.setTypeface(curTypeface);
                holder.deleteCard.setOnClickListener(AccountBankCardMessagesActivity.this);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            WinguoAccountBankCard card = cards.get(position);

            holder.bankIcon.setImageDrawable(getDrawablebyName("hybrid4_account_bank_icon_" + card.bid));
            holder.bankName.setText(card.bankName);

            StringBuilder cardNumber = new StringBuilder();
            cardNumber.append("************").append(card.cardNumberTail);

            holder.cardNumber.setText(cardNumber);
            if (card.isDefault) {
                holder.normalCard.setVisibility(View.VISIBLE);
            } else {
                holder.normalCard.setVisibility(View.INVISIBLE);
            }

            holder.editCard.setTag(position);
            holder.deleteCard.setTag(position);

            return convertView;
        }

    }

    static class Holder {
        ImageView bankIcon;
        TextView bankName;
        TextView cardNumber;
        TextView normalCard;
        Button editCard;
        Button deleteCard;
    }

    private Drawable getDrawablebyName(String imageResourceName) {
        return DrawableDecoder.createFromResource(this, imageResourceName);
    }
}
