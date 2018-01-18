package com.winguo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.winguo.R;

/**
 * 现金券 Dialog
 * Created by chenq on 2017/12/11.ialog
 *  使用方式：
    private void  openVoucherDialog(Context context){
        VoucherDialog dialog = new VoucherDialog(context,"共享分享好友获得现金券",100);
        dialog.show();
     }
 * */
public class VoucherDialog extends Dialog {
    private Context context;
    private TextView dialog_text, dialog_title;
    private String title, text;
    private ImageView dialog_cancel;

    public VoucherDialog(@NonNull Context context, String title, String cash) {
        super(context, R.style.no_border_dialog);
        this.title = title;
        this.context = context;
        this.text = cash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void show() {
        super.show();
        backgroundAlpha(0.5f);
    }

    private void init() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.voucher_dialog_view, null);//获取自定义布局
        dialog_text = view.findViewById(R.id.voucher_dialog_text_id);
        dialog_title = view.findViewById(R.id.voucher_dialog_title_id);
        dialog_cancel = view.findViewById(R.id.voucher_dialog_cancel_id);
        dialog_text.setText(String.format(context.getResources().getString(R.string.cart_goods_price),text));
        dialog_title.setText(title);
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity)context).getWindow().setAttributes(lp);
    }

}
