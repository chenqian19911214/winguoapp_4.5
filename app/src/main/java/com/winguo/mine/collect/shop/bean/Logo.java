package com.winguo.mine.collect.shop.bean;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc ${TODD}
 */
public class Logo implements Serializable {
    public String content;
    public String modifyTime;

    @Override
    public String toString() {
        return "Logo{" +
                "content='" + content + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
