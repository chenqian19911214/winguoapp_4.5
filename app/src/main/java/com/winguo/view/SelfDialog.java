package com.winguo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.winguo.R;
/**
 * Created by Administrator on 2016/12/19.
 * 自定义对话框
 */
public class SelfDialog extends Dialog {

    private Context context;
    private boolean cancelable;
    private String title;
    private String contactText1;
    private String contactText2;
    private String confirmButton;
    private String cancleButton;
    private OnClickPositiveButton onClickPositiveButton;
    private OnClickNegativeButton onClickNegativeButton;

    public SelfDialog( Context context, boolean cancelable,
                      String title, String contactText1, String contactText2, String confirmButton,
                      String cancleButton) {
        super(context, R.style.no_border_dialog);
        this.context = context;
        this.cancelable = cancelable;
        this.title = title;
        this.contactText1 = contactText1;
        this.contactText2 = contactText2;
        this.confirmButton = confirmButton;
        this.cancleButton = cancleButton;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.common_dialog_view, null);//获取自定义布局
        setContentView(view);
        TextView tv_title_text = view.findViewById(R.id.tv_title_text);
        TextView tv_contact_text1 =  view.findViewById(R.id.tv_contact_text1);
        TextView tv_contact_text2 = view.findViewById(R.id.tv_contact_text2);
        TextView dialog_btn1 =  view.findViewById(R.id.dialog_btn1);
        TextView dialog_btn2 =  view.findViewById(R.id.dialog_btn2);
        tv_title_text.setText(title);
        tv_contact_text1.setText(contactText1);
        tv_contact_text2.setText(contactText2);
        dialog_btn1.setText(cancleButton);
        dialog_btn2.setText(confirmButton);
        dialog_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickNegativeButton != null)
                    onClickNegativeButton.onClickNegativeButton();
            }
        });

        dialog_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickPositiveButton != null)
                    onClickPositiveButton.onClickPositiveButton();
            }
        });
    }

    public void setOnClickPositiveButton(OnClickPositiveButton onClickPositiveButton) {
        this.onClickPositiveButton = onClickPositiveButton;
    }

    public interface OnClickPositiveButton {
        void onClickPositiveButton();
    }

    public void setOnClickNegativeButton(OnClickNegativeButton onClickNegativeButton) {
        this.onClickNegativeButton = onClickNegativeButton;
    }

    public interface OnClickNegativeButton {
        void onClickNegativeButton();
    }
}
