package com.winguo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.winguo.R;

public class LoadDialog extends Dialog {
    private static LoadDialog loadDialog;
    private boolean cancelable;

    public LoadDialog(final Context ctx, boolean cancelable) {
        super(ctx);

        this.cancelable = cancelable;

        this.getContext().setTheme(R.style.myDialog);
        setContentView(R.layout.dialog_layout);
        // 必须放在加载布局后
        setParams();
        ImageView iv = findViewById(R.id.loading_iv);
        iv.setBackgroundResource(R.drawable.dialoging);
        AnimationDrawable anim = (AnimationDrawable) iv.getBackground();
        anim.start();
    }

    /**
     * 设置参数
     */
    private void setParams() {
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(false);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        // Dialog宽度
        //        lp.width = display.getWidth();
        Window window = getWindow();
        lp.alpha = 0.3f;
        window.setAttributes(lp);
        //        window.getDecorView().getBackground().setAlpha(0);
    }

    /**
     * 对返回键进行拦截
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!cancelable) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void show(Context context) {
        show(context, true);
    }


    /**
     * @param context
     * @param cancelable 是否可以取消
     */
    public static void show(Context context, boolean cancelable) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (loadDialog != null && loadDialog.isShowing()) {
            return;
        }
        loadDialog = new LoadDialog(context, cancelable);
        loadDialog.show();
    }

    /**
     * 是否正在显示
     *
     * @return
     */
    public static boolean isNowShowing() {
        if (loadDialog == null) {
            return false;
        }
        return loadDialog.isShowing();
    }


    /**
     * 取消弹窗
     *
     * @param context
     */
    public static void dismiss(Context context) {
        try {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    loadDialog = null;
                    return;
                }
            }
            if (loadDialog != null && loadDialog.isShowing()) {
                Context loadContext = loadDialog.getContext();
                if (loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing()) {
                        loadDialog = null;
                        return;
                    }
                }
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDialog = null;
        }
    }
}
