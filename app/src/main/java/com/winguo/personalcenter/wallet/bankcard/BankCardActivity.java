package com.winguo.personalcenter.wallet.bankcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guobi.account.GBAccountError;
import com.guobi.account.WinguoAccountBankCard;
import com.guobi.wallet.AccountBankCardEditActivity;
import com.winguo.R;
import com.winguo.adapter.CommonAdapter;
import com.winguo.adapter.RecyclerAdapter;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseActivity;
import com.winguo.personalcenter.wallet.bankcard.control.BankCardControl;
import com.winguo.personalcenter.wallet.bankcard.model.RequestAccountBankCardListCallback;
import com.winguo.utils.LoadDialog;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/26.
 * 银行卡 列表
 */

public class BankCardActivity extends BaseActivity implements RequestAccountBankCardListCallback{

    private FrameLayout top_back;
    private ImageView top_title_more;
    private TextView layout_title;
    private RecyclerView mywallet_bank_card_rv;
    private BankCardControl cardControl;

    @Override
    protected int getLayout() {
        return R.layout.activity_mywallet_bank_card;
    }

    @Override
    protected void initData() {
        cardControl = new BankCardControl(this);
        if (NetWorkUtil.isNetworkAvailable(this)) {
            LoadDialog.show(this,true);
            cardControl.getAccountBankCardList(this);
        } else {
            ToastUtil.showToast(this,getString(R.string.timeout));
        }
    }

    @Override
    protected void initViews() {
        top_back = (FrameLayout) findViewById(R.id.top_back);
        top_title_more = (ImageView) findViewById(R.id.top_title_more);
        top_title_more.setVisibility(View.VISIBLE);
        layout_title = (TextView) findViewById(R.id.layout_title);
        mywallet_bank_card_rv = (RecyclerView) findViewById(R.id.mywallet_bank_card_rv);

        layout_title.setText("银行卡");
        mywallet_bank_card_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mywallet_bank_card_rv.setItemAnimator(new DefaultItemAnimator());
        mywallet_bank_card_rv.addItemDecoration(new SpacesItemDecoration(ScreenUtil.dipToPx(BankCardActivity.this,10)));

    }



    @Override
    protected void setListener() {
        top_back.setOnClickListener(this);
        top_title_more.setOnClickListener(this);
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
            case R.id.top_title_more:
                //跳转添加银行卡
                Intent intent = new Intent(this, AccountBankCardEditActivity.class);
                intent.putExtra("editType", R.string.hybrid4_account_bankcard_add);
                startActivityForResult(intent,BANK_CARD_ADD);
                break;
        }
    }

    @Override
    public void requestAccountBankCardList(List<WinguoAccountBankCard> cards) {
        LoadDialog.dismiss(this);
        if (cards != null) {
            initAdapter(cards);
        }
    }

    private void initAdapter(final List<WinguoAccountBankCard> cards) {
        RecyclerCommonAdapter<WinguoAccountBankCard> adapter = new RecyclerCommonAdapter<WinguoAccountBankCard>(this,R.layout.bank_card_items,cards) {
            @Override
            protected void convert(RecylcerViewHolder holder, WinguoAccountBankCard winguoAccountBankCard, int position) {
                int bankIcon = cardControl.getBankIcon(winguoAccountBankCard.bankName);
                int bankIconBg = cardControl.getBankIconBG(winguoAccountBankCard.bankName);
                if (bankIconBg != 0) {
                    holder.setBackgroundRes(R.id.bank_card_item,bankIconBg);
                }
                if (bankIcon != 0) {
                    holder.setImageResource(R.id.my_bank_card_type_icon,bankIcon);
                }
                holder.setText(R.id.my_bank_card_banktype,String.format(getString(R.string.my_bank_card_type),winguoAccountBankCard.bankName));
               // holder.setText(R.id.my_bank_card_cardtype,String.format(getString(R.string.my_bank_card_type),winguoAccountBankCard.bankName)); //
                holder.setText(R.id.my_bank_card_code,String.format(getString(R.string.my_bank_card_code),winguoAccountBankCard.cardNumberTail));
            }
        };
        mywallet_bank_card_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                WinguoAccountBankCard winguoAccountBankCard = cards.get(position);
                String p_bid = winguoAccountBankCard.p_bid;
                Intent it  = new Intent(BankCardActivity.this,BankCardDetailActivity.class);
                it.putExtra("p_bid",p_bid);
                startActivityForResult(it,BANK_CARD_DETAIL);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    private final static int BANK_CARD_DETAIL = 0X56;
    private final static int BANK_CARD_ADD = 0X45;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BANK_CARD_ADD:
                case BANK_CARD_DETAIL:
                    if (data != null) {
                        Intent it = new Intent();
                        setResult(RESULT_OK,it);
                        if (NetWorkUtil.isNetworkAvailable(this)) {
                            LoadDialog.show(this,true);
                            cardControl.getAccountBankCardList(this);
                        } else {
                            ToastUtil.showToast(this,getString(R.string.timeout));
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void requestAccountBankCardListErrorCode(int errorcode) {
        LoadDialog.dismiss(this);
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this,errorcode));
    }
}
