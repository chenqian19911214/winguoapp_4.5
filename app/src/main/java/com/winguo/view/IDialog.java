package com.winguo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;

/**
 * 自定义权限获取对话框
 */

public class IDialog extends Dialog {

    public IDialog(Context context) {
        super(context);
    }

    public IDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected IDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private String helpButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private OnClickListener helpButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String positiveButtonText, OnClickListener listener) {
            this.negativeButtonText = positiveButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
        public Builder setHelpButton(String helpButtonText, OnClickListener listener) {
            this.helpButtonText = helpButtonText;
            this.helpButtonClickListener = listener;
            return this;
        }



        public IDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final IDialog dialog = new IDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_permisson_layout, null,false);
            FrameLayout.LayoutParams lp =  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(30,10,30,10);
            dialog.addContentView(layout, lp);

            ((TextView) layout.findViewById(R.id.title)).setText(title);

            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.permission_positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.permission_positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.permission_positiveButton).setVisibility(
                        View.GONE);
            }
            // 消极按钮点击处理
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.permission_negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.permission_negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.permission_negativeButton).setVisibility(
                        View.GONE);
            }

            //帮助说明点击
            if (helpButtonText != null) {
                ((TextView) layout.findViewById(R.id.permission_help))
                        .setText(helpButtonText);
                if (helpButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.permission_help))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    //dialog.dismiss();
                                    helpButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEUTRAL);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.permission_help).setVisibility(
                        View.GONE);
            }


            if (contentView != null) {
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout, lp);
            return dialog;
        }

    }
}
