package com.winguo.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.winguo.R;


/**
 * Created by Administrator on 2017/1/14.
 * 自定义对话框弹窗
 */
public class ConfirmDialog {
    private final TextView tv_titl;
    private final TextView tv_content_text;
    private final TextView tv_content1_text;
    private final TextView btn;
    private final AlertDialog dialog1;

    public ConfirmDialog(Context context, boolean falg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog1 = dialog.create();
        dialog1.setCancelable(falg);
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setContentView(R.layout.confirm_dialog_view);
        tv_titl = (TextView) window.findViewById(R.id.tv_title_text);
        tv_content_text = (TextView) window.findViewById(R.id.tv_content_text);
        tv_content1_text = (TextView) window.findViewById(R.id.tv_content1_text);
        btn = (TextView) window.findViewById(R.id.dialog_prositive_btn);
    }
    public void setDialogTitle(String res){
        tv_titl.setText(res);
    }
    public void setDialogContent(String res){
        tv_content_text.setText(res);
    }
    public void setDialogContent1(String res){
        tv_content1_text.setText(res);
    }
    /**
     * 确定按钮
     * @param res
     * @param listener
     */
    public void setPositiveButton(String res, View.OnClickListener listener){
        btn.setText(res);
        btn.setOnClickListener(listener);
    }



    /**
     * 关闭对话框
     */
    public void dismiss(){
        dialog1.dismiss();
    }


//    private  TextView tv_titl;
//    private  TextView tv_content;
//    private  TextView btn;
//    private OnClickPositiveButton onClickPositiveButton;
//    private String title;
//    private String content;
//
//    public ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.confirm_dialog_view);
//        tv_titl = (TextView) findViewById(R.id.tv_title_text);
//        tv_content = (TextView) findViewById(R.id.tv_content_text);
//        btn = (TextView) findViewById(R.id.dialog_prositive_btn);
//        tv_titl.setText(getDialogTitle());
//        tv_content.setText(getDialogContent());
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickPositiveButton.onClickBtn();
//            }
//        });
//    }
//
//
//    public void setDialogTitle(String title){
//        this.title = title;
//    }
//    private String getDialogTitle(){
//        return title;
//    }
//    public void setDialogContent(String content){
//        this.content = content;
//    }
//    private String getDialogContent(){
//        return content;
//    }
//    public  void setOnClickPositiveButton(OnClickPositiveButton onClickPositiveButton){
//
//        this.onClickPositiveButton = onClickPositiveButton;
//    }
//
//    public interface OnClickPositiveButton{
//        void onClickBtn();
//    }
}
