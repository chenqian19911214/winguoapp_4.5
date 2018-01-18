package com.winguo.bean;

/**
 * Created by admin on 2018/1/9.
 */

public class ThemeProductBean {
    private String name;
    private String icURL;

    public ThemeProductBean(String name, String icURL) {
        this.name = name;
        this.icURL = icURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcURL() {
        return icURL;
    }

    public void setIcURL(String icRes) {
        this.icURL = icRes;
    }
}
