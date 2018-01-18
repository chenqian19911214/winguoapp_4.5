package com.winguo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.winguo.R;

/**
 * Created by admin on 2017/2/27.
 */

public class ShopMorePopWindow extends PopupWindow {

    private Activity context;
    private View.OnClickListener onClickListener;
    public ShopMorePopWindow(Activity context, View.OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.onClickListener = onClickListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View moreView = inflater.inflate(R.layout.shop_more_menu, null,false);
        this.setContentView(moreView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.shopAnimationPop);
        this.setOnDismissListener(new PoponDismissListener());

        LinearLayout shared = (LinearLayout) moreView.findViewById(R.id.shop_more_shared);
        LinearLayout voiceHelp = (LinearLayout) moreView.findViewById(R.id.shop_more_refresh);
        LinearLayout moreSearch = (LinearLayout) moreView.findViewById(R.id.shop_user_feedBack);

        shared.setOnClickListener(onClickListener);
        voiceHelp.setOnClickListener(onClickListener);
        moreSearch.setOnClickListener(onClickListener);

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
        backgroundAlpha(0.5f);
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class PoponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

}
