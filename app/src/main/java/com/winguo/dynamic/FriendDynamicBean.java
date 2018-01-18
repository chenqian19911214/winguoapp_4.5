package com.winguo.dynamic;

import java.util.List;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class FriendDynamicBean {

    /**
     * Result : [{"headportrait":"http://g1.imgdev.winguo.com/group1/M00/00/0E/wKgAoVj9W5KAMPRxAACt4uovO3E17.jpeg","m_customer_account":"18203699123","m_customer_open_time":"2017-04-24 16:38:50","m_customer_shop_name":"问果牛逼","t_customer_income_commission":0},{"headportrait":"","m_customer_account":"18300602618","m_customer_open_time":"2017-04-24 16:12:31","m_customer_shop_name":"a杜沉默的石头","t_customer_income_commission":0},{"headportrait":"","m_customer_account":"15112119980","m_customer_open_time":"2017-04-17 18:18:33","m_customer_shop_name":"陈老师","t_customer_income_commission":0},{"headportrait":"","m_customer_account":"13523232323","m_customer_open_time":"0000-00-00 00:00:00","m_customer_shop_name":"","t_customer_income_commission":0}]
     * code : 0
     * count : 4
     * hasmore : 0
     */

    private String code;
    private int count;
    private String hasmore;
    /**
     * headportrait : http://g1.imgdev.winguo.com/group1/M00/00/0E/wKgAoVj9W5KAMPRxAACt4uovO3E17.jpeg
     * m_customer_account : 18203699123
     * m_customer_open_time : 2017-04-24 16:38:50
     * m_customer_shop_name : 问果牛逼
     * t_customer_income_commission : 0
     */

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
        private String headportrait;
        private String m_customer_account;
        private String m_customer_open_time;
        private String m_customer_shop_name;
        private int t_customer_income_commission;

        public String getHeadportrait() {
            return headportrait;
        }

        public void setHeadportrait(String headportrait) {
            this.headportrait = headportrait;
        }

        public String getM_customer_account() {
            return m_customer_account;
        }

        public void setM_customer_account(String m_customer_account) {
            this.m_customer_account = m_customer_account;
        }

        public String getM_customer_open_time() {
            return m_customer_open_time;
        }

        public void setM_customer_open_time(String m_customer_open_time) {
            this.m_customer_open_time = m_customer_open_time;
        }

        public String getM_customer_shop_name() {
            return m_customer_shop_name;
        }

        public void setM_customer_shop_name(String m_customer_shop_name) {
            this.m_customer_shop_name = m_customer_shop_name;
        }

        public int getT_customer_income_commission() {
            return t_customer_income_commission;
        }

        public void setT_customer_income_commission(int t_customer_income_commission) {
            this.t_customer_income_commission = t_customer_income_commission;
        }
    }
}
