package com.winguo.pay;

import com.winguo.pay.modle.bean.PhysicalConfirmOderBean;
import com.winguo.pay.modle.bean.PhysicalSubmitOrderBean;

/**
 * Created by Admin on 2017/6/29.
 */

public interface IPhysicalComfirmOrderCallBack {
    void getPhysicalConfirmOrderCallBack(PhysicalConfirmOderBean physicalConfirmOderBean);
    void getPhysicalSubmitOrderCallBack(PhysicalSubmitOrderBean physicalSubmitOrderBean);
}
