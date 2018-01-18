package com.winguo.cart.bean;

import com.winguo.cart.bean.OrderBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */

public class CartDataBean implements Serializable{


    /**
     * detail : {"shopInfo":[{"shopId":2145,"shopName":"京东旗舰店","goodsNum":5,"totalPrice":2995,"has_invoice":1,"is_repaired":1,"is_returned":1,"goodsItems":{"data":[{"skuid":30959,"icon":{"modifyTime":1442811022,"content":"http://g1.img.winguo.com/group1/M00/1B/D1/wKgAi1X_jI6AbXaZAAFYFRi8woY126.jpg"},"distributor_id":0,"share_rate":"0.00%","name":"OPPO R7 金色 移动4G手机 双卡双待","item_id":30963,"size":"均码","color":"FFFFFF","color_name":"默认","num":1,"largess_qty":0,"price":1999,"cost_price":1999,"stock":100,"limit_buy":"","discount":"0.0%","discount_type":"不打折"},{"skuid":39469,"icon":{"modifyTime":1442474906,"content":"http://g1.img.winguo.com/group1/M00/0D/AC/wKgAi1X6a5qAEINQAACCkDkBE9s365.jpg"},"distributor_id":0,"share_rate":"0.00%","name":"爱科技（AKG）Q350 大师系列 时尚入耳式耳机 手机耳机 iPhone6非常伴侣 白色","item_id":39473,"size":"","color":"","color_name":"","num":1,"largess_qty":0,"price":249,"cost_price":249,"stock":100,"limit_buy":"","discount":"0.0%","discount_type":"不打折"},{"skuid":39470,"icon":{"modifyTime":1442474907,"content":"http://g1.img.winguo.com/group1/M00/0D/AC/wKgAi1X6a5uAEfUNAACeg67Qm-w468.jpg"},"distributor_id":0,"share_rate":"0.00%","name":"爱科技（AKG）Q350 大师系列 时尚入耳式耳机 手机耳机 iPhone6非常伴侣 绿色","item_id":39474,"size":"","color":"","color_name":"","num":3,"largess_qty":0,"price":249,"cost_price":249,"stock":100,"limit_buy":"","discount":"0.0%","discount_type":"不打折"}]}},{"shopId":2264,"shopName":"宝儿玩具旗舰店","goodsNum":8,"totalPrice":239.2,"has_invoice":0,"is_repaired":0,"is_returned":0,"goodsItems":{"data":{"skuid":86618,"icon":{"modifyTime":1462343863,"content":"http://g1.img.winguo.com/group1/M00/4D/00/wKgAi1cpmLeAdkquAAJ1q_cz00g69..jpg"},"distributor_id":0,"share_rate":"3.34%","name":"充电触屏婴儿童智能音乐手机模型电话宝宝玩具早教启蒙益智1-3岁","item_id":86036,"size":"","color":"FF0000","color_name":"红色","num":8,"largess_qty":0,"price":29.9,"cost_price":29.9,"stock":50,"limit_buy":"","discount":"0.0%","discount_type":"不打折"}}},{"shopId":2361,"shopName":"涂涂乐绘本","goodsNum":1,"totalPrice":9.9,"has_invoice":0,"is_repaired":0,"is_returned":0,"goodsItems":{"data":{"skuid":89158,"icon":{"modifyTime":1473754150,"content":"http://g1.img.winguo.com/group1/M00/4D/B5/wKgAjFfXtCaAGADQAADAMt54OYI63..jpg"},"distributor_id":0,"share_rate":"0.00%","name":"AR魔法学校 小龙万迪 原创卡通iPhone6手机壳","item_id":86782,"size":"iphone6 A款 透明软壳","color":"","color_name":"","num":1,"largess_qty":0,"price":9.9,"cost_price":9.9,"stock":10000,"limit_buy":"","discount":"0.0%","discount_type":"不打折"}}}]}
     * max_largess_qty : 0
     * goodsNum : 14
     * totalprice : 3244.1
     * pay_method : 0
     */

    public OrderBean order;


    @Override
    public String toString() {
        return "CartDataBean{" +
                "order=" + order +
                '}';
    }
}
