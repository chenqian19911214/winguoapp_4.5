package com.winguo.pay.modle.store.selfbean;

import com.winguo.pay.modle.bean.DataBean;
import com.winguo.pay.modle.bean.ExpressMethodBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/5/2.
 */

public class SelfGoodsItem implements Serializable {
    public List<DataBean> data;
    public int temp_id;
    public ExpressMethodBean expressMethodBean;
    public String userMsg;

    @Override
    public String toString() {
        return "SelfGoodsItem{" +
                "data=" + data +
                ", temp_id=" + temp_id +
                '}';
    }
}
