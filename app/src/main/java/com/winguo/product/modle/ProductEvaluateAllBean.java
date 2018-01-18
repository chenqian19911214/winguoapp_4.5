package com.winguo.product.modle;

import com.winguo.product.modle.bean.EvaluateArrBean;
import com.winguo.product.modle.bean.EvaluateObjectBean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 自定义商品评论
 */
public class ProductEvaluateAllBean implements Serializable{
    private EvaluateObjectBean evaluateObjectBean;
    private EvaluateArrBean evaluateArrBean;
    private boolean isObject;
    private boolean hasData;

    @Override
    public String toString() {
        return "ProductEvaluateAllBean{" +
                "evaluateObjectBean=" + evaluateObjectBean +
                ", evaluateArrBean=" + evaluateArrBean +
                ", isObject=" + isObject +
                ", hasData=" + hasData +
                '}';
    }

    public EvaluateObjectBean getEvaluateObjectBean() {
        return evaluateObjectBean;
    }

    public void setEvaluateObjectBean(EvaluateObjectBean evaluateObjectBean) {
        this.evaluateObjectBean = evaluateObjectBean;
    }

    public EvaluateArrBean getEvaluateArrBean() {
        return evaluateArrBean;
    }

    public void setEvaluateArrBean(EvaluateArrBean evaluateArrBean) {
        this.evaluateArrBean = evaluateArrBean;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }
}
