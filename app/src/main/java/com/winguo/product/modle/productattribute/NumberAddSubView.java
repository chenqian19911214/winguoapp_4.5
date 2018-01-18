package com.winguo.product.modle.productattribute;

/**
 * Created by Administrator on 2016/12/27.
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;


/**
 * 作用：自定义数字加减控件
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    public Button btn_sub;
    public Button btn_add;
    private TextView tv_num;
    private Context mContext;

    /**
     * 设置默认值
     */
    private int value = 1;
    private int minValue = 1;
    private int maxValue = 200;

    public NumberAddSubView(Context context) {
        super(context);
        initView(context);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }



    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
//        //得到属性
//        if (attrs != null) {
//            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.NumberAddSubView, defStyleAttr, 0);
//            int value = a.getInt(R.styleable.NumberAddSubView_value, 1);
//            setValue(value);
//
//            int maxValue = a.getInt(R.styleable.NumberAddSubView_maxValue, Integer.MAX_VALUE);
//            setMaxValue(maxValue);
//
//            int minValue = a.getInt(R.styleable.NumberAddSubView_minValue, 1);
//            setMinValue(minValue);
//        }
//
//            Drawable btnSubBackground = a.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
//            if (btnSubBackground != null)
//                btn_sub.setBackground(btnSubBackground);
//
//            Drawable btnAddBackground = a.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
//            if (btnAddBackground != null)
//                btn_sub.setBackground(btnAddBackground);
//
//            Drawable textViewBackground = a.getDrawable(R.styleable.NumberAddSubView_textViewBackground);
//            if (textViewBackground != null)
//                tv_num.setBackground(textViewBackground);
//
//            a.recycle();

//        }
    }

    private void initView(Context context) {

        //第三个参数：把当前View加载到NumberAddSubView控件上
        View.inflate(context, R.layout.number_add_sub_view, this);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_add = (Button) findViewById(R.id.btn_add);
        tv_num = (TextView) findViewById(R.id.tv_num);
        setMinValue(value);
        setValue(minValue);
        setMaxValue(maxValue);

        btn_sub.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    public int getValue() {
        String val = tv_num.getText().toString();
        if (!TextUtils.isEmpty(val)) {
            value = Integer.parseInt(val);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_num.setText(value + "");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sub) {
//            Toast.makeText(mContext,"减",Toast.LENGTH_SHORT).show();

            subNum();
            if (onButtonClickListenter != null) {
                onButtonClickListenter.onButtonSubClick(v, value);
            }
        }
        if (v.getId() == R.id.btn_add) {
//            Toast.makeText(mContext,"加",Toast.LENGTH_SHORT).show();
            addNum();
            if (onButtonClickListenter != null) {
                onButtonClickListenter.onButtonAddClick(v, value);
            }
        }
    }

    /**
     * 减少数据
     */
    private void subNum() {
        if (value > minValue) {
            value = value - 1;
            tv_num.setText(value + "");
        }
    }

    /**
     * 添加数据
     */
    private void addNum() {
        if (value < maxValue) {
            value = value + 1;
            tv_num.setText(value + "");
        }
    }

    public interface OnButtonClickListenter {
        /**
         * 当增加按钮被点击的时候回调该方法
         *
         * @param view
         * @param value
         */
        public void onButtonAddClick(View view, int value);

        /**
         * 当减少按钮被点击的时候回调这个方法
         *
         * @param view
         * @param value
         */
        public void onButtonSubClick(View view, int value);
    }

    private OnButtonClickListenter onButtonClickListenter;

    public void setOnButtonClickListenter(OnButtonClickListenter onButtonClickListenter) {
        this.onButtonClickListenter = onButtonClickListenter;
    }
}

