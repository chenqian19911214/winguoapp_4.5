package com.winguo.pay;


import com.winguo.pay.modle.bean.ExpressMethodBean;

/**
 * Created by Admin on 2017/1/10.
 */

public interface IExpressageCallBack {
    void onBackExpressageMethodData(ExpressMethodBean dispatchMethodBean);
}
