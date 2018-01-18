package com.winguo.confirmpay.modle.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/1/23.
 */

public class PayOrderBean implements Serializable{

    /**
     * msg : app端支付宝支付信息。
     * code : 0
     * status : success
     * url : app_id=2017011905225483&biz_content=En2iS1SJETdNc7YFn8pomrxfFWLrxrbCv%2FGYHd73cqFbv1nTPpz7AMs2hlgnXxMA5qy%2BLGOPIZ0N%2FwIaqeB4X%2F2xtyOSqJEiBMHhCNo7FL3axhPJXMr9iAUPMqST9RFZ1mAT%2F0BipyHMEeQ%2FFLeCfPblbJLztn4tcpNsqO2LKU%2BpthvUVezAxwaXgJ4ZSSWHhY7epkR2cRZgoCW5IgTNpw%3D%3D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flocal.winguo.com%2Fmobile%2Fnotify_url&sign_type=RSA&timestamp=2017-01-23+14%3A31%3A11&version=1.0&sign=W8kY8RxnaGyTqhignxHB%2BGy2J3e6WmgPbdD3YI%2BBEVlWhZuYudLgEezin9ASz0%2BJNYEK3nKCa27JUO4adQ2XJP0Akz6qSQjp0TJLqI%2BDRSq%2BSATxbctZff9n9X0Z8E4RayJvBlSoSptwxAXKESZCDWU2iblM6ELEksUwekPkYgY%3D
     */
    public String msg;
    public int paytype;
    public String code;
    public String status;
    public String url;

    @Override
    public String toString() {
        return "PayOrderBean{" +
                "msg='" + msg + '\'' +
                ", paytype=" + paytype +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
