package com.winguo.cart.view.store;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.product.modle.productattribute.NumberAddSubView;
import com.winguo.utils.ScreenUtil;
import com.winguo.utils.ToastUtil;


/**
 * Created by Admin on 2017/1/6.
 */

public class ChangedGoodsCountWindow extends PopupWindow implements NumberAddSubView.OnButtonClickListenter{

    private final View changedGoodsCountView;
    private Activity context;
    private final TextView tv_cancel_btn;
    private final TextView tv_comfirm_btn;
    private final NumberAddSubView changed_goods_count_addsub_view;
    private IChangedAfterCountInterface iChangedAfterCountInterface;

    public ChangedGoodsCountWindow(Activity context, int limit_bay, final int goodsNum) {
        super(context);
        this.context = context;
        //把布局显示出来
        changedGoodsCountView = View.inflate(context, R.layout.changed_goods_count_view, null);
        tv_cancel_btn = (TextView) changedGoodsCountView.findViewById(R.id.tv_cancel_btn);
        tv_comfirm_btn = (TextView) changedGoodsCountView.findViewById(R.id.tv_comfirm_btn);
        changed_goods_count_addsub_view = (NumberAddSubView) changedGoodsCountView.findViewById(R.id.changed_goods_count_addsub_view);
        changed_goods_count_addsub_view.setValue(goodsNum);
        changed_goods_count_addsub_view.setMinValue(1);
        if (limit_bay!=0) {
            changed_goods_count_addsub_view.setMaxValue(limit_bay);

        }
        changed_goods_count_addsub_view.setOnButtonClickListenter(this);
        this.setContentView(changedGoodsCountView);
        //设置大小
        this.setWidth(ScreenUtil.getScreenWidth(context));
        this.setHeight((ScreenUtil.getScreenHeight(context)) /4);
        // 设置popupWindow以外可以触摸
        this.setOutsideTouchable(true);
        // 以下两个设置点击空白处时，隐藏掉pop窗口
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
        backgroundAlpha(0.3f);
        // 添加pop窗口关闭事件
        this.setOnDismissListener(new poponDismissListener());
        // 设置动画--这里按需求设置成系统输入法动画
        this.setAnimationStyle(R.style.AnimBottom);

        //点击取消
        tv_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击确定
        tv_comfirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tv_count.setText(String.valueOf(changed_goods_count_addsub_view.getValue()));
                iChangedAfterCountInterface.getCount(changed_goods_count_addsub_view.getValue());
                dismiss();
            }
        });
    }
    /**
     * PopouWindow设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onButtonAddClick(View view, int value) {
        int maxValue = changed_goods_count_addsub_view.getMaxValue();
        if (value==maxValue){
            ToastUtil.showToast(context,"此商品最多购买"+maxValue+"件");
        }
    }

    @Override
    public void onButtonSubClick(View view, int value) {

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
    //返回的数据
    public void setOnChangedAfterCount(IChangedAfterCountInterface iChangedAfterCountInterface){
        this.iChangedAfterCountInterface = iChangedAfterCountInterface;
    }
    //返回修改后的回调接口
    public interface IChangedAfterCountInterface{
        void getCount(int count);
    }
}
