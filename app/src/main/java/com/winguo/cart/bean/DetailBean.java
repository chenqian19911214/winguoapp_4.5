package com.winguo.cart.bean;

import com.winguo.cart.model.store.seekcart.ShopInfoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public  class DetailBean implements Serializable{
    /**
     * shopId : 2145
     * shopName : 京东旗舰店
     * goodsNum : 5
     * totalPrice : 2995
     * has_invoice : 1
     * is_repaired : 1
     * is_returned : 1
     * goodsItems : {"data":[{"skuid":30959,"icon":{"modifyTime":1442811022,"content":"http://g1.img.winguo.com/group1/M00/1B/D1/wKgAi1X_jI6AbXaZAAFYFRi8woY126.jpg"},"distributor_id":0,"share_rate":"0.00%","name":"OPPO R7 金色 移动4G手机 双卡双待","item_id":30963,"size":"均码","color":"FFFFFF","color_name":"默认","num":1,"largess_qty":0,"price":1999,"cost_price":1999,"stock":100,"limit_buy":"","discount":"0.0%","discount_type":"不打折"},{"skuid":39469,"icon":{"modifyTime":1442474906,"content":"http://g1.img.winguo.com/group1/M00/0D/AC/wKgAi1X6a5qAEINQAACCkDkBE9s365.jpg"},"distributor_id":0,"share_rate":"0.00%","name":"爱科技（AKG）Q350 大师系列 时尚入耳式耳机 手机耳机 iPhone6非常伴侣 白色","item_id":39473,"size":"","color":"","color_name":"","num":1,"largess_qty":0,"price":249,"cost_price":249,"stock":100,"limit_buy":"","discount":"0.0%","discount_type":"不打折"},{"skuid":39470,"icon":{"modifyTime":1442474907,"content":"http://g1.img.winguo.com/group1/M00/0D/AC/wKgAi1X6a5uAEfUNAACeg67Qm-w468.jpg"},"distributor_id":0,"share_rate":"0.00%","name":"爱科技（AKG）Q350 大师系列 时尚入耳式耳机 手机耳机 iPhone6非常伴侣 绿色","item_id":39474,"size":"","color":"","color_name":"","num":3,"largess_qty":0,"price":249,"cost_price":249,"stock":100,"limit_buy":"","discount":"0.0%","discount_type":"不打折"}]}
     */

    public List<ShopInfoBean> shopInfo;


    @Override
    public String toString() {
        return "DetailBean{" +
                "shopInfo=" + shopInfo +
                '}';
    }
}
