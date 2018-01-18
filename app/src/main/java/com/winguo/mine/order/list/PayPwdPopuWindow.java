package com.winguo.mine.order.list;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;

public class PayPwdPopuWindow extends PopupWindow {
    private Activity mActivity;
    private EditText pay_pwd_et;
    private IPopuWindowConfirmListener mListener;

    public PayPwdPopuWindow(Activity context, IPopuWindowConfirmListener listener) {
        super(context);
        this.mActivity = context;
        this.mListener = listener;
        //把布局显示出来
        View view = View.inflate(context, R.layout.pay_pwd_popuwindow, null);
        RelativeLayout pay_pwd_cancel_rl = (RelativeLayout) view.findViewById(R.id.pay_pwd_cancel_rl);
        TextView pay_pwd_confirm_tv = (TextView) view.findViewById(R.id.pay_pwd_confirm_tv);
        pay_pwd_et = (EditText) view.findViewById(R.id.pay_pwd_et);
        TextView forget_pay_pwd_tv = (TextView) view.findViewById(R.id.forget_pay_pwd_tv);
        this.setContentView(view);

        pay_pwd_et.requestFocus();
        InputMethodManager imm = (InputMethodManager) pay_pwd_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        //设置大小
        this.setWidth(ScreenUtil.getScreenWidth(context));
        this.setHeight((ScreenUtil.getScreenHeight(context)) / 4);
        // 设置popupWindow以外可以触摸
        this.setOutsideTouchable(true);
        // 以下两个设置点击空白处时，隐藏掉pop窗口
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
        backgroundAlpha(0.3f);
        // 添加pop窗口关闭事件
        this.setOnDismissListener(new PopuOnDismissListener());
        // 设置动画--这里按需求设置成系统输入法动画
        this.setAnimationStyle(R.style.AnimBottom);
        //点击取消
        pay_pwd_cancel_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击确定
        pay_pwd_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pay_pwd_et.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    if (NetWorkUtil.isNetworkAvailable(mActivity)) {
                        mListener.confirm(pwd);
                    } else {
                        ToastUtil.show(mActivity, mActivity.getString(R.string.offline));
                    }
                    dismiss();
                } else {
                    ToastUtil.show(mActivity, "请输入支付密码!");
                }
            }
        });
        forget_pay_pwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.forgetPwd();
            }
        });
    }

    /**
     * PopuWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    private class PopuOnDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

   public interface IPopuWindowConfirmListener {

        void confirm(String pwd);

        void forgetPwd();
    }
}
