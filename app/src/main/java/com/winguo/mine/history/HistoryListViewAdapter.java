package com.winguo.mine.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winguo.R;
import com.winguo.net.GlideUtil;
import com.winguo.utils.NetWorkUtil;
import com.winguo.view.CustomDialog2;

import java.util.ArrayList;

public class HistoryListViewAdapter extends BaseAdapter {
    /**
     * 标题部分
     */
    private static final int TYPE_TITLE = 0;
    /**
     * 内容部分
     */
    private static final int TYPE_ITEM = 1;
    private Context mContext;
    private ArrayList<HistoryCategoryItemBean> mListDatas;
    private LayoutInflater mInflater;
    private IHistoryOnItemClickListener mListener;

    public HistoryListViewAdapter(Context context, ArrayList<HistoryCategoryItemBean> datas, IHistoryOnItemClickListener listener) {
        this.mListDatas = datas;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mListener = listener;
    }

    public void setData(ArrayList<HistoryCategoryItemBean> listDatas) {
        this.mListDatas = listDatas;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mListDatas) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (HistoryCategoryItemBean historyCategoryItemBean : mListDatas) {
                count += historyCategoryItemBean.getItemCount();
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {

        // 异常情况处理
        if (null == mListDatas || position < 0 || position > getCount()) {
            return null;
        }
        // 同一分类内，第一个元素的索引值
        int categoryFirstIndex = 0;
        for (HistoryCategoryItemBean historyCategoryItemBean : mListDatas) {
            int size = historyCategoryItemBean.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categoryFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return historyCategoryItemBean.getItem(categoryIndex);
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categoryFirstIndex += size;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == mListDatas || position < 0 || position > getCount()) {
            return TYPE_ITEM;
        }
        int categoryFirstIndex = 0;
        for (HistoryCategoryItemBean historyCategoryItemBean : mListDatas) {
            int size = historyCategoryItemBean.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categoryFirstIndex;
            if (categoryIndex == 0) {
                return TYPE_TITLE;
            }
            categoryFirstIndex += size;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            //内容部分
            case TYPE_ITEM:
                ContentViewHolder viewHolder;
                if (null == convertView) {
                    viewHolder = new ContentViewHolder();
                    convertView = mInflater.inflate(R.layout.history_item, null);
                    viewHolder.history_recyclerview_item_rl = (RelativeLayout) convertView.findViewById(R.id.history_recyclerview_item_rl);
                    viewHolder.history_product_name_tv = (TextView) convertView.findViewById(R.id.history_product_name_tv);
                    //viewHolder.history_shop_name_tv = (TextView) convertView.findViewById(R.id.history_shop_name_tv);
                    viewHolder.history_product_price_tv = (TextView) convertView.findViewById(R.id.history_product_price_tv);
                    viewHolder.history_delete_btn = (Button) convertView.findViewById(R.id.history_delete_btn);
                    viewHolder.history_product_picture_iv = (ImageView) convertView.findViewById(R.id.history_product_picture_iv);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ContentViewHolder) convertView.getTag();
                }
                final HistoryItemBean historyItemBean = (HistoryItemBean) getItem(position);
                viewHolder.history_product_name_tv.setText(historyItemBean.getM_item_name());
                //viewHolder.history_shop_name_tv.setText(historyItemBean.getM_maker_name_ch());
                viewHolder.history_product_price_tv.setText("¥ " + historyItemBean.getM_item_min_price());
                GlideUtil.getInstance().loadImage(mContext, historyItemBean.getImage_url(), R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, viewHolder.history_product_picture_iv);
                viewHolder.history_recyclerview_item_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClick(historyItemBean, position);
                    }
                });
                viewHolder.history_delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CustomDialog2 dialog = new CustomDialog2(mContext);
                        dialog.setDialogTitle(mContext.getResources().getString(R.string.myhistory_dialog_title));
                        dialog.setNegativeButton(mContext.getResources().getString(R.string.negative_text), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setPositiveButton(mContext.getResources().getString(R.string.positive_text), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //删除浏览历史
                                if (!NetWorkUtil.isNetworkAvailable(mContext)) {
                                    Toast.makeText(mContext, mContext.getString(R.string.offline), Toast.LENGTH_SHORT).show();
                                } else {
                                    mListener.delHistoryLog(historyItemBean.getM_item_id(), position, historyItemBean.getDate());
                                    dialog.dismiss();
                                }
                            }
                        });

                    }
                });
                break;
            //标题部分
            case TYPE_TITLE:
                convertView = mInflater.inflate(R.layout.history_item_title, null);
                TextView history_item_title_tv = (TextView) convertView.findViewById(R.id.history_item_title_tv);
                String date = (String) getItem(position);
                history_item_title_tv.setText(date);
                break;
        }
        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_TITLE;
    }

    private class ContentViewHolder {
        private TextView history_product_name_tv;
        //private TextView history_shop_name_tv;
        private TextView history_product_price_tv;
        private ImageView history_product_picture_iv;
        private RelativeLayout history_recyclerview_item_rl;
        private Button history_delete_btn;

    }

    /**
     * item的点击回调接口
     */
    interface IHistoryOnItemClickListener {
        void onItemClick(HistoryItemBean historyBean, int position);

        void delHistoryLog(String gid, int position, String dateTitle);
    }
}
