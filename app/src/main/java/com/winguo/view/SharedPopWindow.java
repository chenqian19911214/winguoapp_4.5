package com.winguo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winguo.R;

import java.util.List;

/**
 * 分享选择
 */

public class SharedPopWindow extends PopupWindow {

    private final View mMenuView;
    private final TextView cancel;
    private Context context;
    public SharedPopWindow(Context context, View.OnClickListener clickListener) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.shared_select_pop, null,false);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setOnDismissListener(new PoponDismissListener());


        LinearLayout qq = (LinearLayout) mMenuView.findViewById(R.id.share_dao1_1);
        LinearLayout sinaWeibo = (LinearLayout) mMenuView.findViewById(R.id.share_dao1_2);
        LinearLayout weiChat = (LinearLayout) mMenuView.findViewById(R.id.share_dao1_3);
        LinearLayout haoyou = (LinearLayout) mMenuView.findViewById(R.id.share_dao1_4);
        qq.setOnClickListener(clickListener);
        sinaWeibo.setOnClickListener(clickListener);
        weiChat.setOnClickListener(clickListener);
        haoyou.setOnClickListener(clickListener);

        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        cancel = (TextView) mMenuView.findViewById(R.id.share_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.myPopupWindpw_anim_style);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);


    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(0.5f);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
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
