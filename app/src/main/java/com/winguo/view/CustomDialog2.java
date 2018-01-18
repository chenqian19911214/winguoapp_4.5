package com.winguo.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.winguo.R;


/**
 * Created by Administrator on 2016/12/19.
 * 自定义对话框弹窗
 */
public class CustomDialog2 {

    private final TextView tv_titl;
    private final Button btn1;
    private final Button btn2;
    private final AlertDialog dialog1;

    public CustomDialog2(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.CustomDialog);
        dialog1 = dialog.create();
        dialog1.show();
        Window window = dialog1.getWindow();
        assert window != null;
        window.setContentView(R.layout.common_dialog_view2);
        tv_titl = (TextView) window.findViewById(R.id.tv_title_text);
        btn1 = (Button) window.findViewById(R.id.dialog_btn1);
        btn2 = (Button) window.findViewById(R.id.dialog_btn2);
    }

    public void setDialogTitle(String res) {
        tv_titl.setText(res);
    }

    private void setBtn1Text(String res) {
        btn1.setText(res);
    }

    private void setBtn2Text(String res) {
        btn2.setText(res);
    }

    /**
     * 确定按钮
     *
     * @param res
     * @param listener
     */
    public void setPositiveButton(String res, View.OnClickListener listener) {
        setBtn2Text(res);
        btn2.setOnClickListener(listener);
    }

    /**
     * 取消按钮
     *
     * @param res
     * @param listener
     */
    public void setNegativeButton(String res, View.OnClickListener listener) {
        setBtn1Text(res);
        btn1.setOnClickListener(listener);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog1.dismiss();
    }
}
