package com.winguo.product.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */

public  class LogoBean implements Serializable {
    public String content;
    public String modifyTime;

    @Override
    public String toString() {
        return "LogoBean{" +
                "content='" + content + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }
}
