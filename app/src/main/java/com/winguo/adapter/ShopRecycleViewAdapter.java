package com.winguo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.winguo.R;
import com.winguo.net.GlideUtil;
import com.winguo.productList.modle.ItemEntitys;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class ShopRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private OnItemClicklistener onItemClickListener;
    private List<ItemEntitys> productBeen;
    public static final int LOADING_STATUS = 0;
    public static final int LOADING_ERROR_STATUS = 1;
    public static final int NO_MORE_STATUS = 2;
    //默认状态
    private int MORE_STATUS = 0;


    public ShopRecycleViewAdapter(Context context, List<ItemEntitys> productBeen) {
        this.context = context;
        this.productBeen = productBeen;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View footView = LayoutInflater.from(context).inflate(R.layout.loading_more_view, parent, false);
            return new FooterViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            if (productBeen != null) {
                if (position == 0) {
                    ((ItemViewHolder) holder).product_list_line_view.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).product_list_line_view.setVisibility(View.VISIBLE);
                }
                ItemEntitys itemEntity = productBeen.get(position);
                String iconUrl = itemEntity.icon.content;
                GlideUtil.getInstance().loadImage(context, iconUrl, R.drawable.electric_theme_loading_bg, R.drawable.electric_theme_error_bg, ((ItemViewHolder) holder).iv_product_list_icon);
                ((ItemViewHolder) holder).tv_product_list_name.setText(itemEntity.name);
                if (TextUtils.isEmpty(itemEntity.price.special)) {
                    ((ItemViewHolder) holder).tv_product_list_price.setText(String.format(context.getResources().getString(R.string.product_list_price), itemEntity.price.regular));
                } else {
                    ((ItemViewHolder) holder).tv_product_list_price.setText(String.format(context.getResources().getString(R.string.product_list_price), itemEntity.price.special));
                }
                ((ItemViewHolder) holder).tv_product_list_sales_volume.setText(String.format(context.getResources().getString(R.string.product_list_sales_volume), itemEntity.sale_qty));
                ((ItemViewHolder) holder).tv_product_list_location.setText(String.format(context.getResources().getString(R.string.product_list_product_location), itemEntity.cityname));
                //条目点击事件
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, layoutPosition);
                    }
                });
            }

        } else if (holder instanceof FooterViewHolder) {
            //改变状态
            FooterViewHolder footerViewHolder= (FooterViewHolder) holder;
            Glide.with(context).load(R.drawable.loading_more_bg).into(footerViewHolder.progressBar);
            switch (MORE_STATUS){
                case LOADING_STATUS:
                    footerViewHolder.ll_has_more.setVisibility(View.VISIBLE);
                    footerViewHolder.no_has_more.setVisibility(View.GONE);
                    break;
                case LOADING_ERROR_STATUS:
                    footerViewHolder.ll_has_more.setVisibility(View.GONE);
                    footerViewHolder.no_has_more.setVisibility(View.VISIBLE);
                    footerViewHolder.no_has_more.setText(context.getText(R.string.request_net2));
                    break;
                case NO_MORE_STATUS:
                    footerViewHolder.ll_has_more.setVisibility(View.GONE);
                    footerViewHolder.no_has_more.setVisibility(View.VISIBLE);
                    footerViewHolder.no_has_more.setText(context.getText(R.string.no_has_more_text));
                    break;
            }
            //条目点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int layoutPosition = holder.getLayoutPosition();
                    if (MORE_STATUS==LOADING_ERROR_STATUS)
                    onItemClickListener.onItemClick(holder.itemView, layoutPosition);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return productBeen.size() == 0 ? 0 : productBeen.size() + 1;
    }

    public void setOnItemClickListener(OnItemClicklistener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClicklistener {
        void onItemClick(View view, int position);
    }

    //改变脚部局的状态
    public void changeMoreStatus(int status) {
        MORE_STATUS = status;
        notifyDataSetChanged();
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_product_list_icon;
    TextView tv_product_list_name;
    TextView tv_product_list_sales_volume;
    TextView tv_product_list_location;
    TextView tv_product_list_price;
    View product_list_line_view;


    public ItemViewHolder(View itemView) {
        super(itemView);
        iv_product_list_icon = (ImageView) itemView.findViewById(R.id.iv_product_list_icon);
        tv_product_list_name = (TextView) itemView.findViewById(R.id.tv_product_list_name);
        tv_product_list_sales_volume = (TextView) itemView.findViewById(R.id.tv_product_list_sales_volume);
        tv_product_list_location = (TextView) itemView.findViewById(R.id.tv_product_list_location);
        tv_product_list_price = (TextView) itemView.findViewById(R.id.tv_product_list_price);
        product_list_line_view = itemView.findViewById(R.id.product_list_line_view);

    }

}

class FooterViewHolder extends RecyclerView.ViewHolder {

    TextView no_has_more;
    LinearLayout ll_has_more, foot_layout;
    ImageView progressBar;

    public FooterViewHolder(View footView) {
        super(footView);
        no_has_more = (TextView) footView.findViewById(R.id.no_has_more);
        ll_has_more = (LinearLayout) footView.findViewById(R.id.ll_has_more);
        foot_layout = (LinearLayout) footView.findViewById(R.id.foot_layout);
        progressBar = (ImageView) footView.findViewById(R.id.progressBar);
    }


}
