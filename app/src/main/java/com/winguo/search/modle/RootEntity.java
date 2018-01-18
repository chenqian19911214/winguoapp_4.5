package com.winguo.search.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */

public class RootEntity {
    /**
     * item : ["金","金万利","金万年","金万年圆珠笔","金三塔","金三塔睡衣","金三角","金三角线材","金三马","金三马家用梯","金业","金业组合","金丝小枣","金丝枣","金丝楠","金丝熊","金丝燕","金丝狐","金丝猴","金丝猴奶糖"]
     * has_more : 1
     */
    public List<String> item;
    public int has_more;

    @Override
    public String toString() {
        return "RootEntity{" +
                "item=" + item +
                ", has_more=" + has_more +
                '}';
    }
}
