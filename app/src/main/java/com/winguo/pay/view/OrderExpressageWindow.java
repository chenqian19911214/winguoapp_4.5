package com.winguo.pay.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.winguo.R;
import com.winguo.pay.modle.bean.ExpressMethodBean;
import com.winguo.pay.modle.bean.ItemBean;
import com.winguo.utils.ScreenUtil;

import java.util.List;

/**
 * Created by Admin on 2017/1/6.
 */

public class OrderExpressageWindow extends PopupWindow {
    private Activity context;
    private ExpressMethodBean expressMethodBean;
    private TextView tv;
    private PayExpandableAdapter payExpandableAdapter;
    private  int choosedProsition=0;


    public OrderExpressageWindow(final Activity context, final ExpressMethodBean expressMethodBean, final TextView tv, final PayExpandableAdapter payExpandableAdapter) {
        super(context);
        this.context = context;
        this.expressMethodBean = expressMethodBean;
        this.tv = tv;
        this.payExpandableAdapter = payExpandableAdapter;
        List<ItemBean> item = expressMethodBean.expressage.item;
        for (int i=0;i<item.size();i++){
            if (item.get(i).isChoosed){
                choosedProsition=i;
            }
        }

        //把布局显示出来
        View dispatch_method_view = View.inflate(context, R.layout.dispatch_method_view, null);
        ListView lv_dispatch_method= (ListView) dispatch_method_view.findViewById(R.id.lv_dispatch_method);
        TextView tv_dispatch_method_colse_btn= (TextView) dispatch_method_view.findViewById(R.id.tv_dispatch_method_colse_btn);
        this.setContentView(dispatch_method_view);
        //设置大小
        this.setWidth(ScreenUtil.getScreenWidth(context));
        this.setHeight((ScreenUtil.getScreenHeight(context))*1/3);
        // 设置popupWindow以外可以触摸
        this.setOutsideTouchable(true);
        // 以下两个设置点击空白处时，隐藏掉pop窗口
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
        backgroundAlpha(0.4f);
        // 添加pop窗口关闭事件
        this.setOnDismissListener(new poponDismissListener());
        // 设置动画--这里按需求设置成系统输入法动画
        this.setAnimationStyle(R.style.AnimBottom);
        //显示数据
        final DispatchAdapter adapter=new DispatchAdapter();
        lv_dispatch_method.setAdapter(adapter);
        //条目点击事件
        lv_dispatch_method.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=choosedProsition) {
                    expressMethodBean.expressage.item.get(position).isChoosed = true;
                    expressMethodBean.expressage.item.get(choosedProsition).isChoosed = false;
                    choosedProsition = position;
                    adapter.notifyDataSetChanged();
                    //写一个广播,用来更新点击后的价格
                    Intent intent=new Intent(context.getResources().getString(R.string.update_price_url));
                    context.sendBroadcast(intent);
                }
            }
        });
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        changed_goods_count_addsub_view.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = changed_goods_count_addsub_view.findViewById(R.id.ll_changed_goods_count)
//                        .getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                       return
//                    }
//                }
//                return true;
//            }
//        });
        //点击取消
        tv_dispatch_method_colse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemBean itemBean = expressMethodBean.expressage.item.get(choosedProsition);
                tv.setText(itemBean.name+" "+itemBean.price+" 元");
                //payExpandableAdapter.notifyDataSetChanged();
                dismiss();
            }
        });


    }

    class ViewHolder{
        TextView tv_express_name,tv_express_price;
        CheckBox express_name_selected_btn;
    }
    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
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
    class DispatchAdapter extends BaseAdapter{

            @Override
            public int getCount() {
                return expressMethodBean.expressage.item.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView==null){
                    convertView = View.inflate(context, R.layout.express_window_item, null);
                    holder=new ViewHolder();
                    holder.tv_express_name= (TextView) convertView.findViewById(R.id.tv_express_name);
                    holder.tv_express_price= (TextView) convertView.findViewById(R.id.tv_express_price);
                    holder.express_name_selected_btn= (CheckBox) convertView.findViewById(R.id.express_name_selected_btn);
                    convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }
                ItemBean itemBean = expressMethodBean.expressage.item.get(position);
                holder.tv_express_name.setText(itemBean.name);
                holder.tv_express_price.setText(String.valueOf(itemBean.price));
                    //根据是否被选中显示
                    holder.express_name_selected_btn.setChecked(itemBean.isChoosed);

                return convertView;
            }

    }



}
