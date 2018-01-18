package com.winguo.search.nearby;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winguo.R;

/**
 * @author hcpai
 * @desc 点击切换附近店铺或者商品
 */

public class NearbySearchChangePopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private INearbySearchPopupWindow listener;

    public NearbySearchChangePopupWindow(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View changeView = inflater.inflate(R.layout.nearby_search_change, null, false);
        this.setContentView(changeView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.AnimationPreview);
        this.setOnDismissListener(new PoponDismissListener());

        TextView change_goods = (TextView) changeView.findViewById(R.id.nearby_search_change_goods_tv);
        TextView change_shop = (TextView) changeView.findViewById(R.id.nearby_search_change_shop_tv);

        change_goods.setOnClickListener(this);
        change_shop.setOnClickListener(this);
    }

    public void setListener(INearbySearchPopupWindow listener) {
        this.listener = listener;
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1.0f);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby_search_change_goods_tv:
                if (listener != null) {
                    //表示商品搜索
                    listener.onItemClickListener(1);
                    dismiss();
                }
                break;
            case R.id.nearby_search_change_shop_tv:
                if (listener != null) {
                    //表示店铺搜索
                    listener.onItemClickListener(0);
                    dismiss();
                }
                break;
        }
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    private class PoponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    interface INearbySearchPopupWindow {
        void onItemClickListener(int type);
    }
}
