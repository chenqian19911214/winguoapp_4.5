package com.winguo.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.winguo.R;
import com.winguo.interfaces.OnCommentClickListener;

import java.util.List;

/**
 * 便利签 适配器
 * Created by admin on 2017/4/12.
 */

public class LabelsAdapter extends RecyclerCommonAdapter<String>{
    private OnCommentClickListener onCommentClickListener;
    private Context context;
    public LabelsAdapter(Context context, int layoutId, List<String> datas, OnCommentClickListener onCommentClickListener) {
        super(context, layoutId, datas);
        this.context = context;
        this.onCommentClickListener = onCommentClickListener;
    }

    @Override
    protected void convert(RecylcerViewHolder holder, String s, int position) {
        holder.setText(R.id.lable_item_content,s);
        ImageView del = holder.getView(R.id.lable_item_content_modify);
        if (isshowBox) {
            del.setVisibility(View.VISIBLE);
        } else {
           del.setVisibility(View.GONE);
        }
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.popshow_anim);
        //设置删除按钮显示的动画
        if (isshowBox)
            del.startAnimation(animation);

        del.setOnClickListener(onCommentClickListener);
        del.setTag(position);

    }

    private  boolean isshowBox;
    public void setModify(){
        //取反
        isshowBox = !isshowBox;
    }
}
