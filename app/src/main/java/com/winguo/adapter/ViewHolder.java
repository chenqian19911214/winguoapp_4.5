package com.winguo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.net.GlideUtil;

/**
 * ViewHolder 配合 万能适配器CommonAdapter使用
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;
    private Context context;

    ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.context = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        //setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {

        if (convertView == null)
        {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }


    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {

        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * @param viewId
     * @param value
     * @return
     */
    public ViewHolder setText(int viewId , SpannableString value){
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }
    public ViewHolder setText(int viewId , String value){
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }
    public ViewHolder setText(int viewId , SpannableStringBuilder value){
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * 图片数据填充工具
     * @param viewId
     * @param drawable
     * @return
     */
    public ViewHolder setImageDrawable(int viewId , Drawable drawable){
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageResource(int viewId , int drawableId){
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId , Bitmap bitmap){
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 加载网页图片
     * 使用Glide
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageByURL(int viewId , String url){
        ImageView view = getView(viewId);
        GlideUtil.getInstance().loadImage(context,url,view);
        return this;
    }


    public View getConvertView()
    {
        return mConvertView;
    }


}