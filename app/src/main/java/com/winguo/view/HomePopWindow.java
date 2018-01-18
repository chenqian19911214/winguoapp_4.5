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
 * Created by admin on 2017/2/20.
 */

public class HomePopWindow extends PopupWindow {
    private Context context;
    private View.OnClickListener onClickListener;
    public HomePopWindow(Context context, View.OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.onClickListener = onClickListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View moreView = inflater.inflate(R.layout.main_more_pop, null,false);
        this.setContentView(moreView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.AnimationPreview);
        this.setOnDismissListener(new PoponDismissListener());

        LinearLayout shared = (LinearLayout) moreView.findViewById(R.id.main_shared);
        LinearLayout voiceHelp = (LinearLayout) moreView.findViewById(R.id.main_more_qr);

        shared.setOnClickListener(onClickListener);
        voiceHelp.setOnClickListener(onClickListener);

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

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity)context).getWindow().setAttributes(lp);
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
