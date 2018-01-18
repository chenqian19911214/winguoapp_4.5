package com.winguo.mine.order;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 用户订单数量
 */
public class OrderCountBean implements Serializable {

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean implements Serializable{
        //3个月内
        private int cnt;  //订单总数
        private int no_pay;//待付款
        private int no_send;//待发货
        private int no_receipt;//待收货
        private int no_comment;//待评论
        private int refund;//退款中
        //3个月以前
        private int cnt_ago;
        private int no_pay_ago;
        private int no_send_ago;
        private int no_receipt_ago;
        private int no_comment_ago;
        private int refund_ago;

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public int getNo_pay() {
            return no_pay;
        }

        public void setNo_pay(int no_pay) {
            this.no_pay = no_pay;
        }

        public int getNo_send() {
            return no_send;
        }

        public void setNo_send(int no_send) {
            this.no_send = no_send;
        }

        public int getNo_receipt() {
            return no_receipt;
        }

        public void setNo_receipt(int no_receipt) {
            this.no_receipt = no_receipt;
        }

        public int getNo_comment() {
            return no_comment;
        }

        public void setNo_comment(int no_comment) {
            this.no_comment = no_comment;
        }

        public int getRefund() {
            return refund;
        }

        public void setRefund(int refund) {
            this.refund = refund;
        }

        public int getCnt_ago() {
            return cnt_ago;
        }

        public void setCnt_ago(int cnt_ago) {
            this.cnt_ago = cnt_ago;
        }

        public int getNo_pay_ago() {
            return no_pay_ago;
        }

        public void setNo_pay_ago(int no_pay_ago) {
            this.no_pay_ago = no_pay_ago;
        }

        public int getNo_send_ago() {
            return no_send_ago;
        }

        public void setNo_send_ago(int no_send_ago) {
            this.no_send_ago = no_send_ago;
        }

        public int getNo_receipt_ago() {
            return no_receipt_ago;
        }

        public void setNo_receipt_ago(int no_receipt_ago) {
            this.no_receipt_ago = no_receipt_ago;
        }

        public int getNo_comment_ago() {
            return no_comment_ago;
        }

        public void setNo_comment_ago(int no_comment_ago) {
            this.no_comment_ago = no_comment_ago;
        }

        public int getRefund_ago() {
            return refund_ago;
        }

        public void setRefund_ago(int refund_ago) {
            this.refund_ago = refund_ago;
        }

        @Override
        public String toString() {
            return "MessageBean{" +
                    "cnt=" + cnt +
                    ", no_pay=" + no_pay +
                    ", no_send=" + no_send +
                    ", no_receipt=" + no_receipt +
                    ", no_comment=" + no_comment +
                    ", refund=" + refund +
                    ", cnt_ago=" + cnt_ago +
                    ", no_pay_ago=" + no_pay_ago +
                    ", no_send_ago=" + no_send_ago +
                    ", no_receipt_ago=" + no_receipt_ago +
                    ", no_comment_ago=" + no_comment_ago +
                    ", refund_ago=" + refund_ago +
                    '}';
        }
    }
}
