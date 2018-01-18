package com.winguo.mine.history;

/**
 * @author hcpai
 * @desc 浏览记录itemBean(新增分类时间)
 */
public class HistoryItemBean {
    @Override
    public String toString() {
        return "HistoryItemBean{" +
                "m_item_id='" + m_item_id + '\'' +
                ", m_item_min_price='" + m_item_min_price + '\'' +
                ", m_item_name='" + m_item_name + '\'' +
                ", m_item_access_rdatetime='" + m_item_access_rdatetime + '\'' +
                ", m_maker_id='" + m_maker_id + '\'' +
                ", m_maker_name_ch='" + m_maker_name_ch + '\'' +
                ", m_maker_mobile_url='" + m_maker_mobile_url + '\'' +
                ", image_url='" + image_url + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    /**
     * m_item_id : 31088
     * m_item_min_price : 228.00
     * m_item_name : 佳贝利玫瑰红葡萄酒
     * m_item_access_rdatetime : 2017-02-17 16:52:59
     * m_maker_id : 2157
     * m_maker_name_ch : 蓝泉酒业专营店
     * m_maker_mobile_url : 521cj
     * image_url : http://g1.imgdev.winguo.com/group1/M00/1D/BE/wKgAi1YbV_qAJbLCAADVsjNkPGA45..jpg
     */

    private String m_item_id;
    private String m_item_min_price;
    private String m_item_name;
    private String m_item_access_rdatetime;
    private String m_maker_id;
    private String m_maker_name_ch;
    private String m_maker_mobile_url;
    private String image_url;
    /**
     * 分类的时间
     */
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getM_item_id() {
        return m_item_id;
    }

    public void setM_item_id(String m_item_id) {
        this.m_item_id = m_item_id;
    }

    public String getM_item_min_price() {
        return m_item_min_price;
    }

    public void setM_item_min_price(String m_item_min_price) {
        this.m_item_min_price = m_item_min_price;
    }

    public String getM_item_name() {
        return m_item_name;
    }

    public void setM_item_name(String m_item_name) {
        this.m_item_name = m_item_name;
    }

    public String getM_item_access_rdatetime() {
        return m_item_access_rdatetime;
    }

    public void setM_item_access_rdatetime(String m_item_access_rdatetime) {
        this.m_item_access_rdatetime = m_item_access_rdatetime;
    }

    public String getM_maker_id() {
        return m_maker_id;
    }

    public void setM_maker_id(String m_maker_id) {
        this.m_maker_id = m_maker_id;
    }

    public String getM_maker_name_ch() {
        return m_maker_name_ch;
    }

    public void setM_maker_name_ch(String m_maker_name_ch) {
        this.m_maker_name_ch = m_maker_name_ch;
    }

    public String getM_maker_mobile_url() {
        return m_maker_mobile_url;
    }

    public void setM_maker_mobile_url(String m_maker_mobile_url) {
        this.m_maker_mobile_url = m_maker_mobile_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
