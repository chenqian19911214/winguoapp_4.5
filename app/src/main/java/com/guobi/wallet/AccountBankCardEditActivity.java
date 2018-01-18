package com.guobi.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guobi.account.GBAccountCallback;
import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountBank;
import com.guobi.account.WinguoAccountBankCard;
import com.guobi.account.WinguoAccountBranchesEntity;
import com.guobi.util.DrawableDecoder;
import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.base.GBBaseActivity;
import com.winguo.personalcenter.wallet.bankcard.control.BankCardControl;
import com.winguo.personalcenter.wallet.bankcard.model.RequestBankListCallback;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ToastUtil;
import com.winguo.view.ExpandCollapseAnimation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 银行卡编辑
 * 添加银行卡信息
 */
public class AccountBankCardEditActivity extends GBBaseActivity implements OnClickListener,RequestBankListCallback {

    private static final int REQUEST_UPDATE = 0;
    private static final int REQUEST_CARD_ERROR = 1;

    private ArrayList<WinguoAccountBank> banks;
    private ArrayList<WinguoAccountBankCard> cards;

    private final ArrayList<WinguoAccountBranchesEntity> branchesProvinces = new ArrayList<WinguoAccountBranchesEntity>();
    private final ArrayList<WinguoAccountBranchesEntity> provinces = new ArrayList<WinguoAccountBranchesEntity>();
    private final ArrayList<WinguoAccountBranchesEntity> cities = new ArrayList<WinguoAccountBranchesEntity>();
    private final ArrayList<WinguoAccountBranchesEntity> branches = new ArrayList<WinguoAccountBranchesEntity>();

    private View lastOpen;

    private View mMenuTarget;
    private AccountMenuPopupWindow mMenuPopupWindow;

    // private View mBankSelectButton;
    private EditText mBankEditText;
    private EditText mBankBranchesEditText;
    private EditText mUserNameEditText;
    private EditText mBankCardEditText;
    private CheckBox mNormalCheck;
    private View mBankListLayout;
    private ListView mBankList;
    private BankListAdapter mBankListAdapter;

    //private LauncherPreferences mPreferences = LauncherPreferences.getInstance();

    private int mEditType;

    private int mCardIndex;

    private WinguoAccountBankCard mCard;
    private WinguoAccountBankCard mNewCard;

    private WinguoAccountBank mBank;

    private GBAccountMgr mAccountMgr;
    private AccountBankCardActivityManager mAccountActivityMgr;

    Object mTrashLock = new Object();
    boolean isTrash = false;
    boolean isRequsting = false;

    private int mDetailBankCardIndex = 0;

    private int mCurrentProvinceIndex = -1;
    private int mCurrentCityIndex = -1;
    private int mCurrentBranchesIndex = -1;

    private String mCurrentProvinceID;
    private String mCurrentProvinceName;
    private String mCurrentCityID;
    private String mCurrentCityName;
    private String mCurrentBranchesID;
    private String mCurrentBranchesName;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CommonUtil.stateSetting(this,R.color.white_top_color);
        setContentView(R.layout.hybrid4_account_bankcard_edit_layout);

        mAccountMgr = GBAccountMgr.getInstance();
        mAccountActivityMgr = AccountBankCardActivityManager.getInstance();
        mAccountActivityMgr.pushActivity(this);

        banks = mAccountActivityMgr.getBanks();
        if (banks.isEmpty()) {
            BankCardControl control = new BankCardControl(this);
            if (NetWorkUtil.isNetworkAvailable(this)) {
                LoadDialog.show(this, true);
                control.getBankList(this);
            } else {
                ToastUtil.showToast(this,getString(R.string.timeout));
                return;
            }
        }
        initData();
    }

    private void initData() {
        CommonUtil.printI("我获取到的银行信息",banks.toString());
        cards = mAccountActivityMgr.getCards();
        recoverData();
        initViews();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void recoverData() {
        RecoverDatas recoverDatas = (RecoverDatas) getLastNonConfigurationInstance();
        if (recoverDatas != null) {
            if (recoverDatas.mBank != null) {
                this.mBank = recoverDatas.mBank.copy();
            } else {
                this.mBank = null;
            }
            this.mCardIndex = recoverDatas.mCardIndex;
            this.mEditType = recoverDatas.mEditType;

            this.mCurrentProvinceID = recoverDatas.mCurrentProvinceID;
            this.mCurrentProvinceName = recoverDatas.mCurrentProvinceName;
            this.mCurrentCityID = recoverDatas.mCurrentCityID;
            this.mCurrentCityName = recoverDatas.mCurrentCityName;
            this.mCurrentBranchesID = recoverDatas.mCurrentBranchesID;
            this.mCurrentBranchesName = recoverDatas.mCurrentBranchesName;

        } else {
            Intent intent = getIntent();
            mEditType = intent.getIntExtra("editType", R.string.hybrid4_account_bankcard_add);
            mCardIndex = intent.getIntExtra("cardIndex", -1);
        }
    }

    private void initViews() {
        // Intent intent = getIntent();
        //Typeface typeface = mPreferences.getCurTypeface();

        // mEditType = intent.getIntExtra("editType",
        // R.string.hybrid4_account_bankcard_add);
        // mCardIndex = intent.getIntExtra("cardIndex", -1);

        Button handleBtn = (Button) findViewById(R.id.hybrid4_account_bank_card_handle);
        //handleBtn.setTypeface(typeface);
        handleBtn.setText(mEditType);
        handleBtn.setOnClickListener(this);

        findViewById(R.id.hybrid4_account_ac_back).setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.hybrid4_account_ac_back_title);
        title.setText(R.string.hybrid4_account_my_bank_card);
        //title.setTypeface(typeface);

        mMenuTarget = findViewById(R.id.hybrid4_account_menu);
        mMenuTarget.setOnClickListener(this);

        mNormalCheck = (CheckBox) findViewById(R.id.hybrid4_account_bankcard_checkbox_setnormal);
        TextView mNormalCheckText = (TextView) findViewById(R.id.hybrid4_account_bankcard_checkbox_setnormal_text);
        //mNormalCheckText.setTypeface(typeface);

        mBankEditText = (EditText) findViewById(R.id.hybrid4_account_bankcard_bank_editor);
        //mBankEditText.setTypeface(typeface);
        mBankBranchesEditText = (EditText) findViewById(R.id.hybrid4_account_bankcard_bank_branches_editor);
        //mBankBranchesEditText.setTypeface(typeface);
        mUserNameEditText = (EditText) findViewById(R.id.hybrid4_account_bankcard_usename_editor);
        //mUserNameEditText.setTypeface(typeface);
        mBankCardEditText = (EditText) findViewById(R.id.hybrid4_account_bankcard_cardnumber_editor);
        //mBankCardEditText.setTypeface(typeface);
        if (mCardIndex >= 0 && mCardIndex < cards.size()) {
            mCard = cards.get(mCardIndex);
            if (mCard != null) {
                mBankEditText.setText(mCard.bankName);
                mBankBranchesEditText.setText(mCard.bankBranchesName);
                mUserNameEditText.setText(mCard.accountName);
                mBankCardEditText.setText(mCard.cardNumberFull);
                mNormalCheck.setChecked(mCard.isDefault);

                this.mCurrentProvinceID = mCard.provinceID;
                this.mCurrentProvinceName = mCard.provinceName;
                this.mCurrentCityID = mCard.cityID;
                this.mCurrentCityName = mCard.cityName;
                this.mCurrentBranchesID = mCard.bankBranchesID;
                this.mCurrentBranchesName = mCard.bankBranchesName;

                for (WinguoAccountBank bank : banks) {
                    if (bank != null && bank.id.equals(mCard.bid)) {
                        mBank = bank;
                        break;
                    }
                }
            }
        }

        LinearLayout bankSelectorLayout = (LinearLayout) findViewById(R.id.hybrid4_account_bankcard_select_bank_layout);
        LinearLayout bankBranchesLayout = (LinearLayout) findViewById(
                R.id.hybrid4_account_bankcard_select_bank_branches_layout);
        LinearLayout usenameInputLayout = (LinearLayout) findViewById(
                R.id.hybrid4_account_bankcard_input_usename_layout);
        LinearLayout bankcardInputLayout = (LinearLayout) findViewById(
                R.id.hybrid4_account_bankcard_input_cardnumber_layout);

        bankSelectorLayout.findViewById(R.id.hybrid4_account_bankcard_select_editor_rect).setOnClickListener(this);
        // mBankSelectButton =
        bankSelectorLayout.findViewById(R.id.hybrid4_account_bankcard_bank_selector).setOnClickListener(this);
        View bankSelectorHelp = bankSelectorLayout.findViewById(R.id.hybrid4_account_bankcard_help);
        bankSelectorHelp.setOnClickListener(this);
        TextView bankSelectorHelpDesc = (TextView) bankSelectorLayout
                .findViewById(R.id.hybrid4_account_bankcard_help_desc);
        //bankSelectorHelpDesc.setTypeface(typeface);
        bankSelectorHelpDesc.setText(R.string.hybrid4_account_bankcard_help_msg_select_bank);

        bankBranchesLayout.findViewById(R.id.hybrid4_account_bankcard_bank_branches_editor_rect)
                .setOnClickListener(this);
        View bankBranchesHelp = bankBranchesLayout.findViewById(R.id.hybrid4_account_bankcard_help2);
        bankBranchesHelp.setOnClickListener(this);
        TextView bankBranchesHelpDesc = (TextView) bankBranchesLayout
                .findViewById(R.id.hybrid4_account_bankcard_help_desc2);
        //bankBranchesHelpDesc.setTypeface(typeface);
        bankBranchesHelpDesc.setText(R.string.hybrid4_account_bankcard_help_msg_select_bank_branches);

        View usenameInputHelp = usenameInputLayout.findViewById(R.id.hybrid4_account_bankcard_help3);
        usenameInputHelp.setOnClickListener(this);
        TextView usenameInputHelpDesc = (TextView) usenameInputLayout
                .findViewById(R.id.hybrid4_account_bankcard_help_desc3);
        //usenameInputHelpDesc.setTypeface(typeface);
        usenameInputHelpDesc.setText(R.string.hybrid4_account_bankcard_help_msg_input_bankcard_usename);

        View bankcardInputHelp = bankcardInputLayout.findViewById(R.id.hybrid4_account_bankcard_help4);
        bankcardInputHelp.setOnClickListener(this);
        TextView bankcardInputHelpDesc = (TextView) bankcardInputLayout
                .findViewById(R.id.hybrid4_account_bankcard_help_desc4);
        //bankcardInputHelpDesc.setTypeface(typeface);
        bankcardInputHelpDesc.setText(R.string.hybrid4_account_bankcard_help_msg_input_bankcard_number);

        mBankListLayout = findViewById(R.id.hybrid4_account_bankcard_banklist_layout);
        mBankList = (ListView) findViewById(R.id.hybrid4_account_bankcard_banklist);
        mBankList.setOnItemClickListener(bankListItemClickListener);
        mBankList.setCacheColorHint(Color.argb(0, 0, 0, 0));
        // 中间不要线了 省事- - ！！
        /*
         * mBankList.setDivider(getResources().getDrawable(
		 * R.drawable.hybrid4_account_bank_list_divider));
		 */
        Drawable drawable = getStateLisDrawable(Color.TRANSPARENT, R.drawable.hybrid4_account_ac_content_pressed);
        mBankList.setSelector(drawable);

        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {

            try {
                Method method = ListView.class.getMethod("setOverScrollMode", int.class);
                method.invoke(mBankList, View.OVER_SCROLL_NEVER);
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
            mBankList.setVerticalFadingEdgeEnabled(false);
        }

        mBankList.setBackgroundResource(R.drawable.hybrid4_account_bank_list_background);
        mBankListAdapter = new BankListAdapter();
        mBankList.setAdapter(mBankListAdapter);
    }

    @Override
    protected void onDestroy() {
        synchronized (mTrashLock) {
            isTrash = true;
        }
        mAccountActivityMgr.removeActivity(this);

		/*
		 * mCurrentProvinceIndex = -1; mCurrentCityIndex = -1;
		 * mCurrentBranchesIndex = -1;
		 * 
		 * mCurrentProvinceID = null; mCurrentProvinceName = null;
		 * mCurrentCityID = null; mCurrentCityName = null; mCurrentBranchesID =
		 * null; mCurrentBranchesName = null;
		 * 
		 * mNewCard = null;
		 * 
		 * if (mCard != null) { mBankEditText.setText(mCard.bankName);
		 * mBankBranchesEditText.setText(mCard.bankBranchesName);
		 * mUserNameEditText.setText(mCard.accountName);
		 * mBankCardEditText.setText(mCard.cardNumberFull);
		 * mNormalCheck.setChecked(mCard.isDefault); } else {
		 * mBankEditText.setText(""); mBankBranchesEditText.setText("");
		 * mUserNameEditText.setText(""); mBankCardEditText.setText("");
		 * mNormalCheck.setChecked(false); } mCard = null;
		 */

        super.onDestroy();
        try {
            this.removeDialog(AccountMenuPopupWindow.REQUEST_UPDATE);
            this.removeDialog(AccountMenuPopupWindow.UPDATE_LIST);
        } catch (Exception e) {
        }
        branchesProvinces.clear();
        provinces.clear();
        cities.clear();
        branches.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        mCurrentProvinceIndex = -1;
        mCurrentCityIndex = -1;
        mCurrentBranchesIndex = -1;

        mCurrentProvinceID = null;
        mCurrentProvinceName = null;
        mCurrentCityID = null;
        mCurrentCityName = null;
        mCurrentBranchesID = null;
        mCurrentBranchesName = null;

        mNewCard = null;

        if (mCard != null) {
            mBankEditText.setText(mCard.bankName);
            mBankBranchesEditText.setText(mCard.bankBranchesName);
            mUserNameEditText.setText(mCard.accountName);
            mBankCardEditText.setText(mCard.cardNumberFull);
            mNormalCheck.setChecked(mCard.isDefault);
        } else {
            mBankEditText.setText("");
            mBankBranchesEditText.setText("");
            mUserNameEditText.setText("");
            mBankCardEditText.setText("");
            mNormalCheck.setChecked(false);
        }

        branchesProvinces.clear();
        provinces.clear();
        cities.clear();
        branches.clear();
        removeDialog();

        RecoverDatas recoverDatas = new RecoverDatas();
        recoverDatas.mEditType = mEditType;
        recoverDatas.mCardIndex = mCardIndex;

        if (mCard != null) {
            recoverDatas.mCurrentProvinceID = mCard.provinceID;
            recoverDatas.mCurrentProvinceName = mCard.provinceName;
            recoverDatas.mCurrentCityID = mCard.cityID;
            recoverDatas.mCurrentCityName = mCard.cityName;
            recoverDatas.mCurrentBranchesID = mCard.bankBranchesID;
            recoverDatas.mCurrentBranchesName = mCard.bankBranchesName;
            recoverDatas.mBank = new WinguoAccountBank();
            recoverDatas.mBank.id = mCard.bid;
            recoverDatas.mBank.name = mCard.bankName;
        }

        return recoverDatas;
    }

    @Override
    public void requestBankList(List<WinguoAccountBank> bankss) {
        LoadDialog.dismiss(this);
        if (bankss != null) {
            banks.addAll(bankss);
            mBankListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestBankListErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }

    class RecoverDatas {
        public WinguoAccountBank mBank;
        public int mEditType;
        public int mCardIndex;

        public String mCurrentProvinceID;
        public String mCurrentProvinceName;
        public String mCurrentCityID;
        public String mCurrentCityName;
        public String mCurrentBranchesID;
        public String mCurrentBranchesName;
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
            case R.id.hybrid4_account_bank_card_handle:
                //确认提交银行卡 请求
                if (!isRequsting) {
                    isRequsting = true;
                    setBankCardDetail();
                }
                break;
            case R.id.hybrid4_account_bankcard_help2:

                textExpAnim(view, R.id.hybrid4_account_bankcard_help_desc2);

                break;
            case R.id.hybrid4_account_bankcard_help3:

                textExpAnim(view, R.id.hybrid4_account_bankcard_help_desc3);

                break;
            case R.id.hybrid4_account_bankcard_help4:

                textExpAnim(view, R.id.hybrid4_account_bankcard_help_desc4);

                break;
            case R.id.hybrid4_account_bankcard_help:

                textExpAnim(view, R.id.hybrid4_account_bankcard_help_desc);

                break;
            case R.id.hybrid4_account_bankcard_bank_selector:
                showBankList();
                break;

            case R.id.hybrid4_account_bankcard_select_editor_rect:
                if (lastOpen != null && lastOpen.equals(mBankListLayout)) {
                    return;
                }
                showBankList();
                break;

            case R.id.hybrid4_account_bankcard_bank_branches_editor_rect:

                if (mBank == null) {
                    mAccountActivityMgr.toast(this.getApplicationContext(), R.string.hybrid4_account_bankcard_select_bank);
                    return;
                }

                if (branchesProvinces.isEmpty()) {
                    showDialog(AccountMenuPopupWindow.REQUEST_PROVINCE);
                    mAccountMgr.getProvinceList(provinces, new ProvinceCallBack());
                } else {
                    removeDialog(AccountMenuPopupWindow.SELECT_PROVINCE);
                    showDialog(AccountMenuPopupWindow.SELECT_PROVINCE);
                }
                break;
            default:
                break;
        }
    }

    private void textExpAnim(View view, int id) {
        ViewGroup parent = (ViewGroup) view.getParent().getParent();
        View target = parent.findViewById(id);

			/*
			 * if (lastOpen != null && lastOpen.equals(mBankListLayout)) {
			 * mBankSelectButton.clearAnimation(); }
			 */

        if (target == lastOpen) {
            lastOpen = null;
            animateView(target, ExpandCollapseAnimation.COLLAPSE);
        } else {
            hideInput();
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
    }

    /**
     * 创建不同的弹窗
     */
    private Dialog newSelectDialog(final int titleID) {

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(-1, -1);
        window.setContentView(R.layout.hybrid4_account_bankcard_edit_layout_brancheslayout);

        TextView title = (TextView) window.findViewById(R.id.hybrid4_account_bankcard_edit_layout_brancheslayout_title);
        title.setText(titleID);
        //title.setTypeface(mPreferences.getCurTypeface());
        ListView listView = (ListView) window
                .findViewById(R.id.hybrid4_account_bankcard_edit_layout_brancheslayout_list);
        TextView backView = (TextView) window
                .findViewById(R.id.hybrid4_account_bankcard_edit_layout_brancheslayout_btn_back);
        //backView.setTypeface(mPreferences.getCurTypeface());
        backView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.cancel();
            }
        });

        TextView cancelView = (TextView) window
                .findViewById(R.id.hybrid4_account_bankcard_edit_layout_brancheslayout_btn_cancel);
        //cancelView.setTypeface(mPreferences.getCurTypeface());
        cancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                removeBranchesDialog();
            }
        });
        hideInput();

        BranchesAdapter branchesAdapter = new BranchesAdapter();

        if (!branchesAdapter.initData(titleID)) {
            dialog.cancel();
            return null;
        }

        listView.setAdapter(branchesAdapter);

        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {

            try {
                Method method = ListView.class.getMethod("setOverScrollMode", int.class);
                method.invoke(listView, View.OVER_SCROLL_NEVER);
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
            listView.setVerticalFadingEdgeEnabled(false);
        }

        removeBranchesReuqestDialog();

        return dialog;
    }

    /**
     * 刷新支行信息
     */
    private void updateBranches() {

        try {
            BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
            BranchesCity branchesCity = (BranchesCity) branchesProvince.cities.get(mCurrentCityIndex);
            WinguoAccountBranchesEntity branches = branchesCity.branches.get(mCurrentBranchesIndex);

            this.mCurrentProvinceID = branchesProvince.id;
            this.mCurrentProvinceName = branchesProvince.name;
            this.mCurrentCityID = branchesCity.id;
            this.mCurrentCityName = branchesCity.name;
            this.mCurrentBranchesID = branches.id;
            this.mCurrentBranchesName = branches.name;

            mBankBranchesEditText.setText(mCurrentBranchesName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        removeBranchesDialog();
    }

    /**
     * 重置支行
     */
    private void resetBranches() {
        this.mCurrentProvinceID = null;
        this.mCurrentProvinceName = null;
        this.mCurrentCityID = null;
        this.mCurrentCityName = null;
        this.mCurrentBranchesID = null;
        this.mCurrentBranchesName = null;
        mBankBranchesEditText.setText("");
        branchesProvinces.clear();
    }

    /**
     * 检查支行信息
     */
    private boolean hasBranches() {
        return mCurrentProvinceID != null && mCurrentProvinceName != null && mCurrentCityID != null
                && mCurrentCityName != null && mCurrentBranchesID != null && mCurrentBranchesName != null;
    }

    /**
     * 检查银行卡是否有变化
     */
    private boolean cardHasChanged() {
        try {
            if (mNewCard.bid.equals(mCard.bid) && mNewCard.bankName.equals(mCard.bankName)
                    && mNewCard.cardNumberFull.equals(mCard.cardNumberFull) && mNewCard.isDefault == mCard.isDefault
                    && mNewCard.accountName.equals(mCard.accountName) && mNewCard.provinceID.equals(mCard.provinceID)
                    && mNewCard.provinceName.equals(mCard.provinceName) && mNewCard.cityID.equals(mCard.cityID)
                    && mNewCard.cityName.equals(mCard.cityName) && mNewCard.bankBranchesID.equals(mCard.bankBranchesID)
                    && mNewCard.bankBranchesName.equals(mCard.bankBranchesName)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    class BranchesAdapter extends BaseAdapter implements OnClickListener {
        private ArrayList<WinguoAccountBranchesEntity> list;
        private int titleID = -1;
		/*
		 * BranchesAdapter(int titleID) { this.titleID = titleID; try { switch
		 * (titleID) { case R.string.hybrid4_account_bankcard_select_province:
		 * mCurrentProvinceIndex = -1; list = branchesProvinces; break; case
		 * R.string.hybrid4_account_bankcard_select_city: { mCurrentCityIndex =
		 * -1; BranchesProvince branchesProvince = (BranchesProvince)
		 * branchesProvinces.get(mCurrentProvinceIndex); list =
		 * branchesProvince.cities; } break; case
		 * R.string.hybrid4_account_bankcard_select_branches: {
		 * mCurrentBranchesIndex = -1; BranchesProvince branchesProvince =
		 * (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
		 * BranchesCity branchesCity = (BranchesCity)
		 * branchesProvince.cities.get(mCurrentCityIndex); list =
		 * branchesCity.branches; } break; default: break; } } catch (Exception
		 * e) { e.printStackTrace(); }
		 * 
		 * }
		 */

        boolean initData(int titleID) {
            this.titleID = titleID;
            try {
                switch (titleID) {
                    case R.string.hybrid4_account_bankcard_select_province:
                        mCurrentProvinceIndex = -1;
                        list = branchesProvinces;
                        break;
                    case R.string.hybrid4_account_bankcard_select_city: {
                        mCurrentCityIndex = -1;
                        BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
                        list = branchesProvince.cities;
                    }
                    break;
                    case R.string.hybrid4_account_bankcard_select_branches: {
                        mCurrentBranchesIndex = -1;
                        BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
                        BranchesCity branchesCity = (BranchesCity) branchesProvince.cities.get(mCurrentCityIndex);
                        list = branchesCity.branches;
                    }
                    break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (list == null || list.isEmpty()) {
                return false;
            }
            return true;
        }

        @Override
        public int getCount() {
            return list.size();
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
            synchronized (mTrashLock) {
                BranchesHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(AccountBankCardEditActivity.this)
                            .inflate(R.layout.hybrid4_account_bankcard_edit_braches_item, null);

                    holder = new BranchesHolder();
                    holder.text = (TextView) convertView
                            .findViewById(R.id.hybrid4_account_bankcard_edit_branches_item_text);
                    //holder.text.setTypeface(mPreferences.getCurTypeface());
					/*
					 * holder.hintArrows = (ImageView) convertView
					 * .findViewById(R.id.
					 * hybrid4_account_bankcard_edit_branches_hint_arrows);
					 */
                    if (titleID == R.string.hybrid4_account_bankcard_select_branches) {
                        View hintArrows = convertView
                                .findViewById(R.id.hybrid4_account_bankcard_edit_branches_hint_arrows);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) hintArrows
                                .getLayoutParams();
                        layoutParams.width = 0;
                        layoutParams.leftMargin = 0;
                        layoutParams.rightMargin = 0;
                        hintArrows.setLayoutParams(layoutParams);
                    }
                    holder.hintChecked = (ImageView) convertView
                            .findViewById(R.id.hybrid4_account_bankcard_edit_branches_hint_checked);
                    convertView.setTag(holder);
                    convertView.setOnClickListener(this);
                } else {
                    holder = (BranchesHolder) convertView.getTag();
                }

                holder.text.setText("");
                holder.hintChecked.setVisibility(View.INVISIBLE);
                holder.hintChecked.setTag(position);

                try {
                    WinguoAccountBranchesEntity branchesEntity = list.get(position);
                    holder.text.setText(branchesEntity.name);

                    switch (titleID) {
                        case R.string.hybrid4_account_bankcard_select_province:
                            if (mCurrentProvinceIndex == -1) {
                                if (mCurrentProvinceID != null && mCurrentProvinceID.equals(branchesEntity.id)) {
                                    holder.hintChecked.setVisibility(View.VISIBLE);
                                }
                            } else if (mCurrentProvinceIndex == position) {
                                holder.hintChecked.setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.string.hybrid4_account_bankcard_select_city:
                            if (mCurrentCityIndex == -1) {
                                if (mCurrentCityID != null && mCurrentCityID.equals(branchesEntity.id)) {
                                    holder.hintChecked.setVisibility(View.VISIBLE);
                                }
                            } else if (mCurrentCityIndex == position) {
                                holder.hintChecked.setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.string.hybrid4_account_bankcard_select_branches:
                            if (mCurrentBranchesIndex == -1) {
                                if (mCurrentBranchesID != null && mCurrentBranchesID.equals(branchesEntity.id)) {
                                    holder.hintChecked.setVisibility(View.VISIBLE);
                                }
                            } else if (mCurrentBranchesIndex == position) {
                                holder.hintChecked.setVisibility(View.VISIBLE);
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return convertView;
            }
        }

        @Override
        public void onClick(View view) {
            BranchesHolder holder = (BranchesHolder) view.getTag();
            int position = (Integer) holder.hintChecked.getTag();
            update(position);
            this.notifyDataSetChanged();
        }

        private void update(int position) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                try {
                    switch (titleID) {
                        case R.string.hybrid4_account_bankcard_select_province: {
                            mCurrentProvinceIndex = position;
                            BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(position);
                            if (branchesProvince == null) {
                                return;
                            }

                            if (!branchesProvince.cities.isEmpty()) {
                                AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_CITY);
                                AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.SELECT_CITY);
                                return;
                            }
                            AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_CITY);
                            AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.REQUEST_CITY);
                            mAccountMgr.getCityList(branchesProvince.id, cities, new CityCallBack());
                        }

                        break;
                        case R.string.hybrid4_account_bankcard_select_city: {
                            mCurrentCityIndex = position;
                            BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces
                                    .get(mCurrentProvinceIndex);
                            BranchesCity branchesCity = (BranchesCity) branchesProvince.cities.get(position);

                            if (branchesCity == null) {
                                return;
                            }

                            if (!branchesCity.branches.isEmpty()) {
                                AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
                                AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
                                return;
                            }
                            AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
                            AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.REQUEST_BRANCHES);
                            mAccountMgr.getBankBrancheList(mBank.id, branchesCity.id, branches, new BankBranchesCallBack());
                        }

                        break;
                        case R.string.hybrid4_account_bankcard_select_branches: {
                            mCurrentBranchesIndex = position;

                            BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces
                                    .get(mCurrentProvinceIndex);
                            BranchesCity branchesCity = (BranchesCity) branchesProvince.cities.get(mCurrentCityIndex);
                            WinguoAccountBranchesEntity winguoAccountBranchesEntity = branchesCity.branches.get(position);
                            if (winguoAccountBranchesEntity == null) {
                                return;
                            }
                            updateBranches();
                        }

                        break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class BranchesHolder {
        TextView text;
        ImageView hintChecked;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case AccountMenuPopupWindow.LOGOUT_DIALOG:
                return newProgressDialog(getString(R.string.hybrid4_account_info_logout));
            case AccountMenuPopupWindow.REQUEST_UPDATE:
                switch (mEditType) {
                    case R.string.hybrid4_account_bankcard_add:
                        return newProgressDialog(getString(R.string.hybrid4_account_bankcard_adding));
                    case R.string.hybrid4_account_bankcard_alter:
                        return newProgressDialog(getString(R.string.hybrid4_account_bankcard_altering));
                }
            case AccountMenuPopupWindow.UPDATE_LIST:
                return newProgressDialog(getString(R.string.hybrid4_account_bankcard_updating_list));
            case AccountMenuPopupWindow.REQUEST_PROVINCE:
                return newProgressDialog(getString(R.string.hybrid4_account_bankcard_getting_province));
            case AccountMenuPopupWindow.REQUEST_CITY:
                return newProgressDialog(getString(R.string.hybrid4_account_bankcard_getting_city));
            case AccountMenuPopupWindow.REQUEST_BRANCHES:
                return newProgressDialog(getString(R.string.hybrid4_account_bankcard_getting_bank_branches));
            case AccountMenuPopupWindow.SELECT_PROVINCE:
                return newSelectDialog(R.string.hybrid4_account_bankcard_select_province);
            case AccountMenuPopupWindow.SELECT_CITY:
                return newSelectDialog(R.string.hybrid4_account_bankcard_select_city);
            case AccountMenuPopupWindow.SELECT_BRANCHES:
                return newSelectDialog(R.string.hybrid4_account_bankcard_select_branches);
            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private ProgressDialog newProgressDialog(String msg) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage(msg);
        return dialog;
    }

    private void removeDialog() {
        this.removeDialog(AccountMenuPopupWindow.REQUEST_UPDATE);
        this.removeDialog(AccountMenuPopupWindow.UPDATE_LIST);
        removeBranchesDialog();
    }

    private void removeBranchesDialog() {
        removeBranchesReuqestDialog();
        this.removeDialog(AccountMenuPopupWindow.SELECT_PROVINCE);
        this.removeDialog(AccountMenuPopupWindow.SELECT_CITY);
        this.removeDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
    }

    private void removeBranchesReuqestDialog() {
        this.removeDialog(AccountMenuPopupWindow.REQUEST_PROVINCE);
        this.removeDialog(AccountMenuPopupWindow.REQUEST_CITY);
        this.removeDialog(AccountMenuPopupWindow.REQUEST_BRANCHES);
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

    /**
     * 设置并检查银行卡各项信息
     */
    private void setBankCardDetail() {
        if (mCard != null) {
            mNewCard = mCard.copy();
        } else {
            mNewCard = new WinguoAccountBankCard();
        }

        boolean isDefault = mNormalCheck.isChecked();
        mNewCard.isDefault = isDefault;

        if (mBank == null) {
            mAccountActivityMgr.toast(this.getApplicationContext(), R.string.hybrid4_account_bankcard_select_bank);
            isRequsting = false;
            return;
        }
        mNewCard.bankName = mBank.name;
        mNewCard.bid = mBank.id;

        if (!hasBranches()) {
            mAccountActivityMgr.toast(this.getApplicationContext(),
                    R.string.hybrid4_account_bankcard_select_bank_branches);
            isRequsting = false;
            return;
        }
        mNewCard.provinceID = mCurrentProvinceID;
        mNewCard.provinceName = mCurrentProvinceName;
        mNewCard.cityID = mCurrentCityID;
        mNewCard.cityName = mCurrentCityName;
        mNewCard.bankBranchesID = mCurrentBranchesID;
        mNewCard.bankBranchesName = mCurrentBranchesName;

        String newUserName = mUserNameEditText.getText().toString();
        if (newUserName == null || newUserName.trim().equals("")) {
            mAccountActivityMgr.toast(this.getApplicationContext(), R.string.hybrid4_account_bankcard_input_usename);
            isRequsting = false;
            return;
        }
        mNewCard.accountName = newUserName;

        String newCardNumber = mBankCardEditText.getText().toString();
        if (newCardNumber == null || newCardNumber.trim().equals("")) {
            mAccountActivityMgr.toast(this.getApplicationContext(), R.string.hybrid4_account_bankcard_input_cardnumber);
            isRequsting = false;
            return;
        }

        mNewCard.cardNumberFull = newCardNumber;

        if (!cardHasChanged()) {
            // 未发生变化，不予提交
            mAccountActivityMgr.toast(this.getApplicationContext(), R.string.hybrid4_account_bankcard_unchanged);
            isRequsting = false;
            return;
        }

        // 校验银行卡号
        boolean matchResult = matchBankCardNumber(mNewCard.cardNumberFull);

        // 校验不通过，弹出提示，用户可以选择继续提交
        if (!matchResult) {
            Intent intent = new Intent(this, SimpleYesNoDialog.class);
            intent.putExtra("title", getResources().getString(R.string.hybrid4_account_bankcard_match_hint_title));
            intent.putExtra("msg", getResources().getString(R.string.hybrid4_account_bankcard_match_error_msg));
            this.startActivityForResult(intent, REQUEST_UPDATE);
            return;
        } else if (mEditType == R.string.hybrid4_account_bankcard_add) {
            //添加
            Intent intent = new Intent(this, SimpleYesNoDialog.class);
            intent.putExtra("title", getResources().getString(R.string.hybrid4_account_bankcard_match_hint_title));
            intent.putExtra("msg", getResources().getString(R.string.hybrid4_account_bankcard_match_success_msg));
            this.startActivityForResult(intent, REQUEST_UPDATE);
        } else {
            //修改
            Intent intent = new Intent(this, SimpleYesNoDialog.class);
            intent.putExtra("title", getResources().getString(R.string.hybrid4_account_bankcard_match_hint_title));
            intent.putExtra("msg", getResources().getString(R.string.hybrid4_account_bankcard_modify_success_msg));
            this.startActivityForResult(intent, REQUEST_UPDATE);
        }

    }

    /**
     * 添加修改 银行卡信息
     */
    private void doRequest() {
        switch (mEditType) {
            case R.string.hybrid4_account_bankcard_add: {
                boolean result = mAccountMgr.addBankCard(mNewCard, new AddBankCardDetailCallback());
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(this, false);
                    removeDialog();
                }
            }
            break;
            case R.string.hybrid4_account_bankcard_alter: {
                boolean result = mAccountMgr.setBankCardDetail(mNewCard, new SetBankCardDetailCallback());
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(this, false);
                    removeDialog();
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_UPDATE:
                isRequsting = false;
                if (resultCode == Activity.RESULT_OK) {
                    doRequest();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 银行卡号校验：Luhn algorithm
     * <p>
     * 校验方法：取出银行卡号最后一位作为校验码，其余位数从右端开始提取基数，
     * 偶数位的数字不变直接作为基数，奇数位的数字乘以2作为基数(如果乘积大于9，就把乘积个位和十位的数字相加作为基数),
     * 所有基数相加再加上校验码的和能刚好被10整除，如果不符合，则不是一个标准的银行卡号 注 意：此方法不能验证所有银行卡号
     */
    private boolean matchBankCardNumber(String number) {

        try {

            int length = number.length();

            // 排除长度在 12 15 16 17 18 19 位之外的号码
            if ((length < 15 && length != 12) || length > 19) {
                return false;
            }

            // 取出校验位
            char checkChr = number.charAt(length - 1);
            length--;

            int count = Integer.parseInt(checkChr + "");

            for (int i = 1; i <= length; i++) {
                char chr = number.charAt(length - i);

                if (i % 2 == 0) {
                    // 从提取校验码之后最右端开始数的偶数位直接作为基数
                    count += Integer.parseInt(chr + "");
                } else {
                    // 从提取校验码之后最右端开始数的奇数位 *2作为基数，如果乘积大于9，个位十位相加作为基数
                    int product = Integer.parseInt(chr + "") * 2;
                    count += product / 10;
                    count += product % 10;
                }
            }

            if (count % 10 == 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加回调
     */
    class AddBankCardDetailCallback implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.REQUEST_UPDATE);
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }
                if (resultCode != 0) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);

                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_UPDATE);
                    return;
                }
                mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                        R.string.hybrid4_account_bankcard_add_succeed);
                Intent it = new Intent();
                setResult(RESULT_OK,it);
                finish();
                // 重新读取银行卡列表
             /*  boolean result = mAccountMgr.getBankCardList(cards, new BankCardCallBack());
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(AccountBankCardEditActivity.this, true);
                }*/
            }
        }

        @Override
        public void onTaskCanceled() {
        }
    }

    /**
     * 修改回调
     */
    class SetBankCardDetailCallback implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.REQUEST_UPDATE);
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }
                if (resultCode != 0) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);

                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_UPDATE);
                    return;
                }
                mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                        R.string.hybrid4_account_bankcard_alter_succeed);

                // 重新读取银行卡列表
                boolean result = mAccountMgr.getBankCardList(cards, new BankCardCallBack());
                if (!result) {
                    mAccountActivityMgr.showStatusLockErr(AccountBankCardEditActivity.this, true);
                }
            }
        }

        @Override
        public void onTaskCanceled() {
        }
    }

    /**
     * 读取银行卡回调
     */
    class BankCardCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.UPDATE_LIST);
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {

            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                if (resultCode != 0) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);

                    // 已经添加或修改成功，但是刷新失败，跳转到有记录页面，并显示刷新按钮
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.clearDatas();
                        mAccountActivityMgr.jumpToActivity(AccountBankCardEditActivity.this,
                                AccountBankCardMessagesActivity.class);
                    }
                    return;
                }

                boolean cardEmpty = cards.isEmpty();
                boolean bankEmpty = banks.isEmpty();

                if (cardEmpty && bankEmpty) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                            R.string.hybrid4_account_bankcard_readerr);
                    mAccountActivityMgr.popActivity(AccountBankCardEditActivity.this);
                } else if (cardEmpty) {
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.jumpToActivity(AccountBankCardEditActivity.this,
                                AccountBankCardNoMessageActivity.class);
                    }
                } else {
                    mDetailBankCardIndex = 0;
                    boolean result = mAccountMgr.getBankCardDetail(cards.get(mDetailBankCardIndex),
                            new DetailBankCardCallBack());
                    if (!result) {
                        mAccountActivityMgr.showStatusLockErr(AccountBankCardEditActivity.this, true);
                    }
                }
            }
        }

        @Override
        public void onTaskCanceled() {
        }
    }

    /**
     * 读取银行卡详细信息
     */
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
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);

                    // 已经添加或修改成功，但是刷新失败，跳转到有记录页面，并显示刷新按钮
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.clearDatas();
                        mAccountActivityMgr.jumpToActivity(AccountBankCardEditActivity.this,
                                AccountBankCardMessagesActivity.class);
                    }
                    return;
                }

                if (mDetailBankCardIndex > cards.size() - 1) {
                    if (mAccountActivityMgr.getActivityCount() > 0) {
                        mAccountActivityMgr.jumpToActivity(AccountBankCardEditActivity.this,
                                AccountBankCardMessagesActivity.class);
                    }
                } else {
                    boolean result = mAccountMgr.getBankCardDetail(cards.get(mDetailBankCardIndex), this);
                    if (!result) {
                        mAccountActivityMgr.showStatusLockErr(AccountBankCardEditActivity.this, true);
                    }
                    mDetailBankCardIndex++;
                }
            }
        }

        @Override
        public void onTaskCanceled() {
        }
    }

    /**
     * 读取省份列表回调
     */
    class ProvinceCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }
                branchesProvinces.clear();
            }
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                if (resultCode != 0) {
                    if (errMsg != null) {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);
                    } else {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                                getString(R.string.hybrid4_account_bankcard_getting_err));
                    }

                    provinces.clear();
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_PROVINCE);
                    return;
                } else if (provinces.isEmpty()) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                            getString(R.string.hybrid4_account_bankcard_getting_err));
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_PROVINCE);
                }

                int size = provinces.size();
                for (int i = 0; i < size; i++) {
                    WinguoAccountBranchesEntity province = provinces.get(i);
                    BranchesProvince branchesProvince = new BranchesProvince();
                    branchesProvince.id = province.id;
                    branchesProvince.name = province.name;
                    branchesProvinces.add(branchesProvince);
                }
                /*  // 钓鱼岛是中国的 !!!!
                BranchesProvince branchesProvince = new BranchesProvince();
                branchesProvince.id = "01065961114";
                branchesProvince.name = getString(R.string.hybrid4_account_bankcard_our_diaoyudao);
                branchesProvinces.add(branchesProvince);*/
                provinces.clear();
                AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_PROVINCE);
                AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.SELECT_PROVINCE);
            }
        }

        @Override
        public void onTaskCanceled() {

        }
    }

    /**
     * 读取城市列表回调
     */
    class CityCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }
                try {
                    BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
                    branchesProvince.cities.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                BranchesProvince branchesProvince = null;
                try {
                    branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (branchesProvince != null && "01065961114".equals(branchesProvince.id)) {
                    resultCode = 0;
                    BranchesCity branchesCity = new BranchesCity();
                    branchesCity.id = "01065961114";
                    branchesCity.name = getString(R.string.hybrid4_account_bankcard_our_diaoyudao);
                    cities.add(branchesCity);
                }

                if (resultCode != 0) {
                    if (errMsg != null) {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);
                    } else {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                                getString(R.string.hybrid4_account_bankcard_getting_err));
                    }

                    cities.clear();
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_CITY);
                    return;
                } else if (cities.isEmpty()) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                            getString(R.string.hybrid4_account_bankcard_getting_err));
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_CITY);
                }

                if (branchesProvince != null) {
                    int size = cities.size();
                    for (int i = 0; i < size; i++) {
                        WinguoAccountBranchesEntity city = cities.get(i);
                        BranchesCity branchesCity = new BranchesCity();
                        branchesCity.id = city.id;
                        branchesCity.name = city.name;
                        branchesProvince.cities.add(branchesCity);
                    }
                }
                cities.clear();
                AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_CITY);
                AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.SELECT_CITY);
            }
        }

        @Override
        public void onTaskCanceled() {

        }
    }

    /**
     * 读取支行列表回调
     */
    class BankBranchesCallBack implements GBAccountCallback {

        @Override
        public void onTaskBegin() {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }
                try {
                    BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
                    BranchesCity branchesCity = (BranchesCity) branchesProvince.cities.get(mCurrentCityIndex);
                    branchesCity.branches.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onTaskEnd(int resultCode, String errMsg) {
            synchronized (mTrashLock) {
                if (isTrash) {
                    return;
                }

                BranchesCity branchesCity = null;
                try {
                    BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces.get(mCurrentProvinceIndex);
                    branchesCity = (BranchesCity) branchesProvince.cities.get(mCurrentCityIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mBank != null && branchesCity != null && "01065961114".equals(branchesCity.id)) {
                    mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                            mBank.name + getString(R.string.hybrid4_account_bankcard_our_diaoyudao_err1));
                    branches.clear();
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_BRANCHES);
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
                    AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
                    return;
                }

                if (resultCode != 0) {
                    if (errMsg != null) {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(), errMsg);
                    } else {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                                getString(R.string.hybrid4_account_bankcard_getting_err));
                    }

                    branches.clear();
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_BRANCHES);
                    return;
                } else if (branches.isEmpty()) {
                    // 没有返回错误信息，但是还是没找到支行，发出提示
                    String cityName = null;
                    String bankName = null;
                    try {

                        BranchesProvince branchesProvince = (BranchesProvince) branchesProvinces
                                .get(mCurrentProvinceIndex);
                        branchesCity = (BranchesCity) branchesProvince.cities.get(mCurrentCityIndex);
                        cityName = branchesCity.name;
                        bankName = mBank.name;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (cityName != null && bankName != null) {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                                getString(R.string.hybrid4_account_bankcard_nobranches_err1) + cityName
                                        + getString(R.string.hybrid4_account_bankcard_nobranches_err2) + bankName);
                    } else {
                        mAccountActivityMgr.toast(AccountBankCardEditActivity.this.getApplicationContext(),
                                getString(R.string.hybrid4_account_bankcard_getting_err));
                    }
                    AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.REQUEST_BRANCHES);
                }

                if (branchesCity != null) {
                    branchesCity.branches.addAll(branches);
                }
                branches.clear();
                AccountBankCardEditActivity.this.removeDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
                AccountBankCardEditActivity.this.showDialog(AccountMenuPopupWindow.SELECT_BRANCHES);
            }
        }

        @Override
        public void onTaskCanceled() {

        }
    }

    private void animateView(final View target, final int type) {
        final int duration = 350;
        Animation animation = new ExpandCollapseAnimation(target, type);

        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

                if (target.equals(mBankListLayout)) {
                    mBankList.setEnabled(false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (type == ExpandCollapseAnimation.COLLAPSE) {
                    target.setVisibility(View.GONE);
                }

                if (target.equals(mBankListLayout)) {
                    mBankList.setEnabled(true);
                }

            }
        });
        animation.setDuration(duration);

        target.startAnimation(animation);
    }

    private void showBankList() {

        View target = mBankListLayout;

        if (target == lastOpen) {
            lastOpen = null;
            animateView(target, ExpandCollapseAnimation.COLLAPSE);
            // mBankSelectButton.clearAnimation();
        } else {
            hideInput();
            if (lastOpen != null) {
                animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
            }

            if (target.getMeasuredHeight() == 0) {
                target.measure(target.getMeasuredWidth(), target.getMeasuredHeight());
                target.requestLayout();
            }
            animateView(target, ExpandCollapseAnimation.EXPAND);
            lastOpen = target;
            // rotateView(mBankSelectButton, 180, 0.5f, 0.5f);
        }
        hideInput();
    }

    private void hideInput() {
        try {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mUserNameEditText.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mBankCardEditText.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    private void showMenu(View view) {
        mMenuPopupWindow = new AccountMenuPopupWindow(this);
        mMenuPopupWindow.showAsDropDown(view);
    }

    private void closeMenu() {
        if (mMenuPopupWindow != null) {
            mMenuPopupWindow.dismiss();
            mMenuPopupWindow = null;
        }
    }

    private OnItemClickListener bankListItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            WinguoAccountBank bank = banks.get(position);
            onBankChange(bank);
            showBankList();
        }
    };

    private class BankListAdapter extends BaseAdapter {

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

            BankHolder bankHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(AccountBankCardEditActivity.this)
                        .inflate(R.layout.hybrid4_account_bank_item, null);

                bankHolder = new BankHolder();
                bankHolder.bankIcon = (ImageView) convertView.findViewById(R.id.hybrid4_account_bank_item_image);
                bankHolder.bankName = (TextView) convertView.findViewById(R.id.hybrid4_account_bank_item_text);
                bankHolder.bankName.setTextColor(0xff555555);
                bankHolder.bankName.setTextSize(15);
                //bankHolder.bankName.setTypeface(mPreferences.getCurTypeface());
                convertView.setTag(bankHolder);
            } else {
                bankHolder = (BankHolder) convertView.getTag();
            }

            WinguoAccountBank bank = banks.get(position);

            if (bank != null) {
                bankHolder.bankName.setText(bank.name);
                bankHolder.bankIcon.setImageDrawable(getDrawablebyName("hybrid4_account_bank_icon_" + bank.id));
            }
            return convertView;
        }
    }

    private class BankHolder {
        ImageView bankIcon;
        TextView bankName;
    }

    private class BranchesProvince extends WinguoAccountBranchesEntity {
        public final ArrayList<WinguoAccountBranchesEntity> cities = new ArrayList<WinguoAccountBranchesEntity>();
    }

    private class BranchesCity extends WinguoAccountBranchesEntity {
        public final ArrayList<WinguoAccountBranchesEntity> branches = new ArrayList<WinguoAccountBranchesEntity>();
    }

    private void onBankChange(WinguoAccountBank bank) {
        if (bank == null) {
            return;
        }
        if (mBank == null || mBank.id != null && !mBank.id.equals(bank.id)) {
            // 改变银行,清空支行
            resetBranches();
        }
        mBank = bank;
        mBankEditText.setText(mBank.name);
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

    private Drawable getDrawablebyName(String imageResourceName) {
        return DrawableDecoder.createFromResource(this, imageResourceName);
    }

	/*
	 * private void rotateView(View view,float rotation,float pivotXValue,float
	 * pivotYValue){ if(view == null){ return; } view.clearAnimation();
	 * RotateAnimation rotateAnimation = new RotateAnimation(0, rotation,
	 * Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF,
	 * pivotYValue); rotateAnimation.setFillEnabled(true);
	 * rotateAnimation.setFillAfter(true); rotateAnimation.setDuration(0);
	 * rotateAnimation.setRepeatCount(1); view.startAnimation(rotateAnimation);
	 * }
	 */
}
