package com.winguo.bean;

/**
 * Created by chenq on 2017/1/3.
 */

public class PublicKeyJson {

    /**
     * hash : a2V5JTNELS0tLS1CRUdJTitQVUJMSUMrS0VZLS0tLS0lMEFNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXEyb0NTYzB5YmZxWXNmUldXZlZLJTBBWVE5WW5TRWxmNkI3QUtQcUJ4WlpqayUyQmI5Ukd6T0RxQUxTUmJkNzMlMkZLa3ZkQWZNS3lhTDJ3SFpqaEclMkZzM3MyaCUwQW9EMHYybVA0RlEzclpYVnBDN05TRmxtVEhKSkhyWUM3eWowYld1ejY3aTZsQVY4TmZOVCUyQnclMkY1UU8lMkZubWVaNmglMEFvRk5Sc24xWmFPc1lleXpEVFYlMkJXcElhVlFrdFJZU1JGTFZRcEpXSFEwajl2WTZYNGFxN2pZbTNrV1dIa3pNanclMEF4TW96cUdQbXM4QUNQRDlzeTVPZHN5Y013VTNUWW00NWJBbCUyRnNnYzI2NEp1eWdvZzRzMzhPZUttS2N3R0h2OXMlMEEyREk4NzFCazN6QzR2MWVBYWdqUDRHa2tlZng0YXR4cE9ReDAwa3hBayUyQmFkNGNEMHRzY05nVm1wNSUyQnhQb2RCZyUwQXdRSURBUUFCJTBBLS0tLS1FTkQrUFVCTElDK0tFWS0tLS0tJTBBJTI2dXVpZCUzRDU4NmM2OTA3NTlkYWMlMjZ0b2tlbiUzRDJCN0YzOTMzLThGNzEtNUNFNC1BRDQzLTMzQjU3MzExRTkzNw==
     * status : success
     * text : 获取公钥成功
     * code : 0
     */

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public  class MessageBean {
        private String hash;
        private String status;
        private String text;
        private int code;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "MessageBean{" +
                    "hash='" + hash + '\'' +
                    ", status='" + status + '\'' +
                    ", text='" + text + '\'' +
                    ", code=" + code +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PublicKeyJson{" +
                "message=" + message +
                '}';
    }
}
