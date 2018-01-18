package com.winguo.pay.view;

import com.winguo.pay.modle.bean.PhysicalConfirmOderBean;
import com.winguo.pay.modle.bean.PhysicalSubmitOrderBean;

/**
 * Created by Admin on 2017/6/29.
 */

public interface IPhysicalComfirmOrderView {
    void getPhysicalConfirmOderData(PhysicalConfirmOderBean physicalConfirmOderBean);
    void getPhysicalSubmitOderData(PhysicalSubmitOrderBean physicalSubmitOrderBean);
}
