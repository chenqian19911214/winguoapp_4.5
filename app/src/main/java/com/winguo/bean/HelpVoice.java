package com.winguo.bean;

import java.util.List;

/**
 * Created by admin on 2016/12/28.
 */

public class HelpVoice {

    public int iconHead;
    public String topTitle;
    public String topTitleTip;
    public List<String> resData;

    public HelpVoice(int iconHead, String topTitle, String topTitleTip, List<String> resData) {
        this.iconHead = iconHead;
        this.topTitle = topTitle;
        this.topTitleTip = topTitleTip;
        this.resData = resData;
    }
}
