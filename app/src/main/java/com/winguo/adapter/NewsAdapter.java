package com.winguo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.bean.NewsItemData;
import com.winguo.net.GlideUtil;

import java.util.List;

/**
 * 新闻listview设配器
 * Created by admin on 2016/12/7.
 */

public class NewsAdapter extends CommonAdapter {

    private List<NewsItemData.ResultBean.DataBean> mDatas;


    public NewsAdapter(Context context, List<NewsItemData.ResultBean.DataBean> mDatas, int mItemLayoutId) {

        super(context, mDatas, mItemLayoutId);
        this.mDatas = mDatas;
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {

        View view = helper.getConvertView();

        TextView text_test = (TextView) view.findViewById(R.id.text_test);
        TextView author_name = (TextView) view.findViewById(R.id.author_name_id);
        TextView date = (TextView) view.findViewById(R.id.date_id);
        ImageView imageView = (ImageView) view.findViewById(R.id.newsImageid);


        text_test.setText(mDatas.get(position).getTitle());
        author_name.setText(mDatas.get(position).getAuthor_name());
        date.setText(mDatas.get(position).getDate());
        GlideUtil.getInstance().loadImage(mContext, mDatas.get(position).getThumbnail_pic_s(), imageView);

    }


}
