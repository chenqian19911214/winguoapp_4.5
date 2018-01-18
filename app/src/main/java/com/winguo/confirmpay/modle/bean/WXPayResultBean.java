package com.winguo.confirmpay.modle.bean;

/**
 * @author hcpai
 * @time 2017/10/10  17:23
 * @desc 调起微信支付需要接参数bean
 */

public class WXPayResultBean {

    /**
     * appid : wx8e9301cd64ba6263
     * code : 0
     * msg : app端微信支付。
     * noncestr : z6yipqqantqkvnr2j5yw438iitbj213m
     * package : Sign=WXPay
     * partnerid : 1462738202
     * prepayid : wx20171012115555a677b62a130399364693
     * result_code : SUCCESS
     * return_code : SUCCESS
     * return_msg : OK
     * sign : A273AC583C2E6C154B6B0A9C932F4256
     * status : success
     * timestamp : 1507780555
     * trade_type : APP
     */

    private String appid;
    private String code;
    private String msg;
    private String noncestr;
    private String packageX;
    private String partnerid;
    private String prepayid;
    private String result_code;
    private String return_code;
    private String return_msg;
    private String sign;
    private String status;
    private String timestamp;
    private String trade_type;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
