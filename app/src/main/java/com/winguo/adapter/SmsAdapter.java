package com.winguo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.example.localsearch.LocalSmsFinder;
import com.example.localsearch.WGResultText;
import com.winguo.R;
import com.winguo.interfaces.OnCommentClickListener;

import java.util.List;

/**
 * Created by admin on 2017/1/4.
 */

public class SmsAdapter extends CommonAdapter<LocalSmsFinder.WGSmsResult> {


    public SmsAdapter(Context context, List<LocalSmsFinder.WGSmsResult> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, final LocalSmsFinder.WGSmsResult item, int position) {
        helper.setText(R.id.search_sms_len,item.getLength()+"/"+item.getLength());
        helper.setText(R.id.search_sms_person,item.getPerson());
        final String person = item.getPerson();
        WGResultText body = item.getBodyResult();
        helper.setText(R.id.search_sms_body,makeSpannableString(body));
        helper.getView(R.id.search_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("address", item.getAddress());
                intent.setType("vnd.android-dir/mms-sms");
                mContext.startActivity(intent);
              /*
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setType("vnd.android-dir/mms-sms");
                it.setData(Uri.parse("content://mms-sms/conversations/"+person));
                mContext.startActivity(it);*/
            }
        });


    }


    /**
     * 处理关键字高亮（红色）
     * @param
     * @return
     */


    public SpannableStringBuilder makeSpannableString(WGResultText resultText) {
        if (resultText == null || resultText.getContent() == null)
            return null;

        int len = resultText.getHightLightLen();
        int start = resultText.getHightLightPos();
        String content = resultText.getContent();
        SpannableStringBuilder styled = new SpannableStringBuilder(content);

        if (start > -1 && len > 0&& start+len< content.length()) {
            styled.setSpan(new ForegroundColorSpan(Color.RED),
                    start, start + len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return styled;
    }

}
