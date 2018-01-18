package com.winguo.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/6.
 */

public class MyPartner {


    /**
     * Result : [{"cid":"39362","account":"test1111"},{"cid":"39491","account":"13800138026"},{"cid":"39493","account":"13800138660"},{"cid":"39525","account":"13640030212"}]
     * code : 0
     * count : 4
     * hasmore : 0
     */

    private String code;
    private int count;
    private String hasmore;
    private List<ResultBean> Result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHasmore() {
        return hasmore;
    }

    public void setHasmore(String hasmore) {
        this.hasmore = hasmore;
    }

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    public static class ResultBean {
        /**
         * cid : 39362
         * account : test1111
         */

        private String cid;
        private String account;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
