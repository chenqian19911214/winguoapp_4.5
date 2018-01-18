package com.winguo.bean;


import java.io.Serializable;

/**
 * 搜索历史实体
 * Created by admin on 2017/4/20.
 */

public class SearchHistory implements Serializable {

    /**
     * 1.搜索商品类型
     * 2.店铺类型
     */
    public final static int SHOP_TYPE = 1;
    /**
     * 搜索本地类型
     * 商品类型
     */
    public final static int LOCAL_TYPE = 2;
    /**
     * 搜索服务类型
     */
    public final static int SERVER_TYPE = 3;

    public String name;
    public int type;  // 商品 本地 服务

    public SearchHistory(String name, int type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SearchHistory p = (SearchHistory) obj;
        if (name == null) {
            if (p.name != null) {
                return false;
            }
        } else if (!name.equals(p.name)) {
            return false;
        }
        if (p.type != type) {
            return false;
        }

        return true;
    }


    @Override
    public String toString() {
        return "SearchHistory{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
