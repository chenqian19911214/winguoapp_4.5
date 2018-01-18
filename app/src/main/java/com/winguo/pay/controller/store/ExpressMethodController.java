package com.winguo.pay.controller.store;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.winguo.pay.modle.bean.ExpressMethodBean;
import com.winguo.pay.modle.dispatchmethod.ExpressageRequestNet;
import com.winguo.pay.view.IExpressageView;
import com.winguo.utils.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/1/10.
 */

public class ExpressMethodController {

    private Context context;
    private IExpressageView iExpressageView;
    private int count;
    private ExpressageRequestNet expressageRequestNet;
    private int number=0;
    private List<ExpressMethodBean> expressMethodBeans=new ArrayList<>();


    public ExpressMethodController(Context context,IExpressageView iExpressageView, int count) {
        this.context = context;
        this.iExpressageView = iExpressageView;
        this.count = count;
        expressageRequestNet = new ExpressageRequestNet();
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            number++;
            switch (msg.what){
                case ExpressageRequestNet.EXPRESSAGE_METHOD_MESSAGE:
                    ExpressMethodBean expressMethodBean= (ExpressMethodBean) msg.obj;
                    expressMethodBeans.add(expressMethodBean);
                    Log.i(ExpressMethodController.class.getSimpleName(),"1+++++"+expressMethodBeans.size());
                    if (number==count) {
                        Log.i(ExpressMethodController.class.getSimpleName(),"2+++++"+expressMethodBeans.size());
                        LoadDialog.dismiss(context);
                        iExpressageView.expressMethodData(expressMethodBeans);
                    }
                    break;
            }
        }
    };

    public void onBackDispatchMethodData(Context context,int temp_id, int shop_id, int recid) {
        expressageRequestNet.getDispatchMethodData(context,temp_id, shop_id, recid,mHandler);
    }


}
