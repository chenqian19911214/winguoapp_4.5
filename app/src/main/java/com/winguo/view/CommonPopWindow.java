package com.winguo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winguo.R;

/**
 * Created by admin on 2017/10/30.
 * 通用
 */

public class CommonPopWindow extends PopupWindow {

    private final View mMenuView;
    private Activity activity;

    public CommonPopWindow(Context context,int layRes,int [] clickIDs ,View.OnClickListener clickListener) {
        super(context);
        this.activity = (Activity) context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(layRes, null,false);
        for (int i = 0; i < clickIDs.length; i++) {
            mMenuView.findViewById(clickIDs[i]).setOnClickListener(clickListener);
        }

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.myPopupWindpw_anim_style);
        this.setOnDismissListener(new PoponDismissListener());
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int top = mMenuView.findViewById(R.id.pop_layout).getTop();
                int  y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top) {
                        dismiss();
                    }
                }
                return true;
            }
        });

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
        WindowManager.LayoutParams lp =activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
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
