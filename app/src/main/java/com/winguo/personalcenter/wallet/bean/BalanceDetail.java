package com.winguo.personalcenter.wallet.bean;

import java.util.List;

/**
 * Created by admin on 2018/1/15.
 */

public class BalanceDetail {

    /**
     * root : {"all_balance":943253.3,"balance":943253.3,"has_more_items":1,"count":149,"items":{"data":[{"jpy_id":153131,"createdate":"2018-01-10 13:20:33","type":"","title":"创客分利","price":750.45,"latest_balance":943253.3},{"jpy_id":153130,"createdate":"2018-01-10 13:16:45","type":"","title":"创客分利","price":497.35,"latest_balance":942502.85},{"jpy_id":153129,"createdate":"2018-01-10 12:14:28","type":"支付订单货款","title":"订单：201801100000001638","price":-2500,"latest_balance":942005.5},{"jpy_id":153128,"createdate":"2018-01-10 10:47:11","type":"","title":"创客分利","price":750.45,"latest_balance":944505.5},{"jpy_id":153127,"createdate":"2018-01-10 10:33:30","type":"","title":"创客分利","price":410,"latest_balance":943755.05},{"jpy_id":153126,"createdate":"2018-01-09 13:37:10","type":"","title":"创客分利","price":730,"latest_balance":943345.05},{"jpy_id":153125,"createdate":"2018-01-09 11:17:00","type":"","title":"创客分利","price":1160,"latest_balance":942615.05},{"jpy_id":153122,"createdate":"2018-01-08 15:44:57","type":"","title":"创客分利利率","price":505,"latest_balance":941455.05},{"jpy_id":153121,"createdate":"2018-01-08 15:51:27","type":"支付订单货款","title":"订单：201801080000001616","price":-2550,"latest_balance":940950.05},{"jpy_id":153120,"createdate":"2018-01-05 11:11:20","type":"支付订单货款","title":"订单：201801050000001605","price":-2499.95,"latest_balance":943500.05}]}}
     */

    private RootBean root;

    public RootBean getRoot() {
        return root;
    }

    public void setRoot(RootBean root) {
        this.root = root;
    }

    public static class RootBean {
        /**
         * all_balance : 943253.3
         * balance : 943253.3
         * has_more_items : 1
         * count : 149
         * items : {"data":[{"jpy_id":153131,"createdate":"2018-01-10 13:20:33","type":"","title":"创客分利","price":750.45,"latest_balance":943253.3},{"jpy_id":153130,"createdate":"2018-01-10 13:16:45","type":"","title":"创客分利","price":497.35,"latest_balance":942502.85},{"jpy_id":153129,"createdate":"2018-01-10 12:14:28","type":"支付订单货款","title":"订单：201801100000001638","price":-2500,"latest_balance":942005.5},{"jpy_id":153128,"createdate":"2018-01-10 10:47:11","type":"","title":"创客分利","price":750.45,"latest_balance":944505.5},{"jpy_id":153127,"createdate":"2018-01-10 10:33:30","type":"","title":"创客分利","price":410,"latest_balance":943755.05},{"jpy_id":153126,"createdate":"2018-01-09 13:37:10","type":"","title":"创客分利","price":730,"latest_balance":943345.05},{"jpy_id":153125,"createdate":"2018-01-09 11:17:00","type":"","title":"创客分利","price":1160,"latest_balance":942615.05},{"jpy_id":153122,"createdate":"2018-01-08 15:44:57","type":"","title":"创客分利利率","price":505,"latest_balance":941455.05},{"jpy_id":153121,"createdate":"2018-01-08 15:51:27","type":"支付订单货款","title":"订单：201801080000001616","price":-2550,"latest_balance":940950.05},{"jpy_id":153120,"createdate":"2018-01-05 11:11:20","type":"支付订单货款","title":"订单：201801050000001605","price":-2499.95,"latest_balance":943500.05}]}
         */

        private double all_balance;
        private double balance;
        private int has_more_items;
        private int count;
        private ItemsBean items;

        public double getAll_balance() {
            return all_balance;
        }

        public void setAll_balance(double all_balance) {
            this.all_balance = all_balance;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getHas_more_items() {
            return has_more_items;
        }

        public void setHas_more_items(int has_more_items) {
            this.has_more_items = has_more_items;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ItemsBean getItems() {
            return items;
        }

        public void setItems(ItemsBean items) {
            this.items = items;
        }

        public static class ItemsBean {
            private List<DataBean> data;

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * jpy_id : 153131
                 * createdate : 2018-01-10 13:20:33
                 * type :
                 * title : 创客分利
                 * price : 750.45
                 * latest_balance : 943253.3
                 */

                private int jpy_id;
                private String createdate;
                private String type;
                private String title;
                private double price;
                private double latest_balance;

                public int getJpy_id() {
                    return jpy_id;
                }

                public void setJpy_id(int jpy_id) {
                    this.jpy_id = jpy_id;
                }

                public String getCreatedate() {
                    return createdate;
                }

                public void setCreatedate(String createdate) {
                    this.createdate = createdate;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public double getLatest_balance() {
                    return latest_balance;
                }

                public void setLatest_balance(double latest_balance) {
                    this.latest_balance = latest_balance;
                }
            }
        }
    }
}
