package com.winguo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.example.localsearch.WGContactResult;
import com.example.localsearch.WGResultText;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.R;
import com.winguo.interfaces.OnCommentClickListener;
import com.winguo.utils.CommonUtil;

import java.util.List;

/**
 * 联系人适配器
 * Created by admin on 2016/12/7.
 */

public class ConntactAdapter extends CommonAdapter<WGContactResult> {


    private OnCommentClickListener onCommentClickListener;

    public ConntactAdapter(Context context, List<WGContactResult> mDatas, int itemLayoutId, OnCommentClickListener onCommentClickListener) {
        super(context, mDatas, itemLayoutId);
        this.onCommentClickListener = onCommentClickListener;
    }

    @Override
    public void convert(ViewHolder helper, WGContactResult item, int position) {

        String contactID = item.getContactID();

        WGResultText contactName = item.getContactName();
        CommonUtil.printI("***", "==============================================4564");
        helper.setText(R.id.search_contact_name, makeSpannableString(contactName));
        WGResultText number = item.getPhoneNumberList().get(0);
        String content = number.getContent();
        helper.setText(R.id.search_contact_number, makeSpannableString(number));
        helper.getView(R.id.search_contact_nameandnumber).setOnClickListener(onCommentClickListener);
        helper.getView(R.id.search_contact_nameandnumber).setTag(position);
        helper.setText(R.id.search_contact_city, item.getCity());

        helper.getView(R.id.search_contact_message_icon).setOnClickListener(onCommentClickListener);
        helper.getView(R.id.search_contact_message_icon).setTag(position);
        helper.getView(R.id.search_contact_phone_icon).setOnClickListener(onCommentClickListener);
        helper.getView(R.id.search_contact_phone_icon).setTag(position);
        helper.setImageBitmap(R.id.search_imageid,item.getPhoto());

    }


    /**
     * 处理关键字高亮（红色）
     *
     * @param resultText
     * @return
     */


    public SpannableStringBuilder makeSpannableString(WGResultText resultText) {
        if (resultText == null || resultText.getContent() == null)
            return null;

        int len = resultText.getHightLightLen();
        int start = resultText.getHightLightPos();
        String content = resultText.getContent();
        SpannableStringBuilder styled = new SpannableStringBuilder(content);

        if (start > -1 && len > 0 && start + len < content.length()) {
            styled.setSpan(new ForegroundColorSpan(Color.RED),
                    start, start + len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return styled;
    }

}
