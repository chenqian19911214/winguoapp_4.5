package com.winguo.search.nearby;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.winguo.R;


/**
 * Created by Administrator on 2016/12/19.
 * 自定义对话框弹窗
 */
public class NearbyCustomDialog {

    private final Button nearby_search_negative_btn;
    private final Button nearby_search_positive_btn;
    private final AlertDialog dialog1;

    public NearbyCustomDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.CustomDialog);
        dialog1 = dialog.create();
        dialog1.show();
        Window window = dialog1.getWindow();
        assert window != null;
        window.setContentView(R.layout.nearby_search_delete);
        nearby_search_negative_btn = (Button) window.findViewById(R.id.nearby_search_negative_btn);
        nearby_search_positive_btn = (Button) window.findViewById(R.id.nearby_search_positive_btn);
    }


    /**
     * 确定按钮
     *
     * @param listener
     */
    public void setPositiveButton(View.OnClickListener listener) {
        nearby_search_positive_btn.setOnClickListener(listener);
    }

    /**
     * 取消按钮
     *
     * @param listener
     */
    public void setNegativeButton( View.OnClickListener listener) {
        nearby_search_negative_btn.setOnClickListener(listener);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog1.dismiss();
    }
}
