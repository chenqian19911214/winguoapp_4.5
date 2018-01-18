package com.winguo.productList.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.utils.ScreenUtil;
import com.winguo.view.CityPicker;


/**
 * Created by Admin on 2017/1/6.
 */

public class ProvinceCityWindow extends PopupWindow {
    private Activity context;
    private final CityPicker cityPicker;
    private String sheng;
    private String shi;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            sheng = bundle.getString("sheng");
            shi = bundle.getString("shi");
        }
    };

    public ProvinceCityWindow(Activity context, final Handler handler) {
        super(context);
        this.context = context;


        //把布局显示出来
        final View select_city_pop_view = View.inflate(context, R.layout.select_city_pop_view, null);
        cityPicker = (CityPicker) select_city_pop_view.findViewById(R.id.citypicker);
        final TextView select_city_ok = (TextView) select_city_pop_view.findViewById(R.id.select_city_ok);
        cityPicker.setCity(new CityPicker.testCity() {
            @Override
            public void cityAll(String sheng, String shi) {

                Message message = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("sheng", sheng);
                bundle.putString("shi", shi);
                message.setData(bundle);
                message.sendToTarget();
            }
        });
        select_city_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_city_ok.setClickable(false);
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("sheng", sheng);
                bundle.putString("shi", shi);
                message.setData(bundle);
                message.sendToTarget();
                dismiss();
            }
        });
        this.setContentView(select_city_pop_view);
        //设置大小
        this.setWidth(ScreenUtil.getScreenWidth(context));
        this.setHeight((ScreenUtil.getScreenHeight(context)) * 1 / 3);
        // 设置popupWindow以外可以触摸
        this.setOutsideTouchable(true);
        // 以下两个设置点击空白处时，隐藏掉pop窗口
        this.setFocusable(false);
        this.setBackgroundDrawable(new BitmapDrawable());
        // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
        backgroundAlpha(0.3f);
        // 添加pop窗口关闭事件
        this.setOnDismissListener(new poponDismissListener());
        // 设置动画--这里按需求设置成系统输入法动画
        this.setAnimationStyle(R.style.AnimBottom);

    }


    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }


}
