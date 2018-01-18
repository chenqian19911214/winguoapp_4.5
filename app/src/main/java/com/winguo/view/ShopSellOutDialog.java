package com.winguo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;

/**
 * 商品售罄弹窗
 */

public class ShopSellOutDialog extends Dialog {

    public ShopSellOutDialog(Context context) {
        super(context);
    }

    public ShopSellOutDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShopSellOutDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private int titleColor;
        private int messageColor;
        private int positiveButtonTextColor;
        private int negativeButtonTextColor;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

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
        public Builder setTitleColor(int colorid) {
            this.titleColor = colorid;
            return this;
        }
        public Builder setMessage(String message){
            this.message = message;
            return this;
        }
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }
        public Builder setMessageColor(int colorid) {
            this.messageColor = colorid;
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

        public Builder setPositiveButtonColor(int colorid) {
            this.positiveButtonTextColor = colorid;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonTex, OnClickListener listener) {
            this.negativeButtonText = negativeButtonTex;
            this.negativeButtonClickListener = listener;
            return this;
        }
        public Builder setNegativeButtonColor(int colorid) {
            this.negativeButtonTextColor = colorid;
            return this;
        }




        public ShopSellOutDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ShopSellOutDialog dialog = new ShopSellOutDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.shop_sell_out_dialog, null,false);
            FrameLayout.LayoutParams lp =  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(30,10,30,10);
            dialog.addContentView(layout, lp);
            TextView titleTV =  layout.findViewById(R.id.title);


            TextView positiveButton = layout.findViewById(R.id.positiveButton);
            TextView negativeButton = layout.findViewById(R.id.negativeButton);
            LinearLayout content =  layout.findViewById(R.id.content);
            if (title != null) {
                titleTV.setText(this.title);
                if (titleColor!=0)
                    titleTV.setTextColor(context.getResources().getColor(titleColor));

            } else {
                titleTV.setVisibility(View.GONE);
            }

            if (positiveButtonText != null) {
                positiveButton.setText(positiveButtonText);
                if (positiveButtonTextColor != 0)
                    positiveButton.setTextColor(context.getResources().getColor(positiveButtonTextColor));

                if (positiveButtonClickListener != null) {
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.permission_positiveButton).setVisibility(
                        View.GONE);
            }
            // 消极按钮点击处理
            if (negativeButtonText != null) {
                negativeButton.setText(negativeButtonText);
                if (negativeButtonTextColor != 0)
                    negativeButton.setTextColor(context.getResources().getColor(negativeButtonTextColor));
                if (negativeButtonClickListener != null) {
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                negativeButton.setVisibility(View.GONE);
            }

            if (message != null) {
                TextView messageTV = layout.findViewById(R.id.message);
                messageTV.setText(message);
                if (messageColor != 0)
                    messageTV.setTextColor(context.getResources().getColor(messageColor));

            }else if (contentView != null) {
                content.removeAllViews();
                content.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout, lp);
            return dialog;
        }

    }
}
