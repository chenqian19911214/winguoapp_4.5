package com.winguo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.winguo.R;

import java.lang.reflect.Field;

/**
 * 日期选择器
 * Created by admin on 2017/4/12.
 */

public class ChooseDateUtil implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    Context context;
    AlertDialog dialog;
    ChooseDateInterface dateInterface;
    NumberPicker npYear, npMonth, npDay;
    TextView tvCancel, tvSure;
    int[] newDateArray = new int[3];

    public void createDialog(Context context, String[] oldDateArray, ChooseDateInterface dateInterface) {
        this.context = context;
        this.dateInterface = dateInterface;
        newDateArray[0] = Integer.parseInt(oldDateArray[0]);
        newDateArray[1] = Integer.parseInt(oldDateArray[1]);
        newDateArray[2] = Integer.parseInt(oldDateArray[2]);

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.date_pick_pop_layout);
        //初始化控件
        tvCancel = (TextView) window.findViewById(R.id.tvCancel);
        tvSure = (TextView) window.findViewById(R.id.tvSure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        npYear = (NumberPicker) window.findViewById(R.id.npYear);
        npMonth = (NumberPicker) window.findViewById(R.id.npMonth);
        npDay = (NumberPicker) window.findViewById(R.id.npDay);
        //设置选择器最小值、最大值
        npYear.setMinValue(1900);
        npYear.setMaxValue(2100);
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        //设置选择器初始值
        npYear.setValue(newDateArray[0]);
        npMonth.setValue(newDateArray[1]);
        npDay.setValue(newDateArray[2]);
        //设置监听
        npYear.setOnValueChangedListener(this);
        npMonth.setOnValueChangedListener(this);
        npDay.setOnValueChangedListener(this);
        //去除分割线
        setNumberPickerDividerColor(npYear);
        setNumberPickerDividerColor(npMonth);
        setNumberPickerDividerColor(npDay);
        //设置字体颜色
        setNumberPickerTextColor(npYear, Color.parseColor("#4d4d4d"));
        setNumberPickerTextColor(npMonth, Color.parseColor("#4d4d4d"));
        setNumberPickerTextColor(npDay, Color.parseColor("#4d4d4d"));

        resizeNumberPicker(npYear);
        resizeNumberPicker(npMonth);
        resizeNumberPicker(npDay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dialog.dismiss();
                break;
            case R.id.tvSure:
                dialog.dismiss();
                dateInterface.sure(newDateArray);
                break;
        }
    }

    //选择器选择值监听
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.npYear:
                newDateArray[0] = newVal;
                npDay.setMaxValue(DateUtil.getNumberOfDays(newDateArray[0], newDateArray[1]));
                break;
            case R.id.npMonth:
                newDateArray[1] = newVal;
                npDay.setMaxValue(DateUtil.getNumberOfDays(newDateArray[0], newDateArray[1]));
                break;
            case R.id.npDay:
                newDateArray[2] = newVal;
                break;
        }
    }

    /**
     * 重设NumberPicker 高度
     * @param np
     */
    private void resizeNumberPicker(NumberPicker np){

        int screenWidth = ScreenUtil.getScreenWidth((Activity) context);
        LinearLayout.LayoutParams params = null;
        switch (screenWidth) {
            case 720:
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,160);
                break;
            case 1080:
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,270);
                break;
            default:
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,160);
                break;
        }

        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);

    }
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.transparent)));// pf.set(picker, new Div)
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
