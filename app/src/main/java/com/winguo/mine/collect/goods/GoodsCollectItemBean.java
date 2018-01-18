package com.winguo.mine.collect.goods;

/**
 * @author hcpai
 * @desc 商品收藏item
 */
public class GoodsCollectItemBean {
    private int gid;
    private String name;
    private int t_freight_temp_id;
    private double price;
    private PicBean pic;
    private int m_item_counts;
    private String city_name;
    private int sale_qty;
    private int stock;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getT_freight_temp_id() {
        return t_freight_temp_id;
    }

    public void setT_freight_temp_id(int t_freight_temp_id) {
        this.t_freight_temp_id = t_freight_temp_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PicBean getPic() {
        return pic;
    }

    public void setPic(PicBean pic) {
        this.pic = pic;
    }

    public int getM_item_counts() {
        return m_item_counts;
    }

    public void setM_item_counts(int m_item_counts) {
        this.m_item_counts = m_item_counts;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getSale_qty() {
        return sale_qty;
    }

    public void setSale_qty(int sale_qty) {
        this.sale_qty = sale_qty;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static class PicBean {
        @Override
        public String toString() {
            return "PicBean{" +
                    "modifyTime=" + modifyTime +
                    ", content='" + content + '\'' +
                    '}';
        }

        private int modifyTime;
        private String content;

        public int getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(int modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Override
    public String toString() {
        return "GoodsCollectItemBean{" +
                "gid=" + gid +
                ", name='" + name + '\'' +
                ", t_freight_temp_id=" + t_freight_temp_id +
                ", price=" + price +
                ", pic=" + pic +
                ", m_item_counts=" + m_item_counts +
                ", city_name='" + city_name + '\'' +
                ", sale_qty=" + sale_qty +
                ", stock=" + stock +
                '}';
    }
}
