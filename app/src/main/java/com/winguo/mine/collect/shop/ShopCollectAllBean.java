package com.winguo.mine.collect.shop;

/**
 * @author hcpai
 * @desc 总店铺收藏(自定义)
 */
public class ShopCollectAllBean {
    private boolean IsEmpty;
    private boolean isObject;
    private ShopCollectObjBean goodsCollectObjBean;
    private ShopCollectArryBean goodsCollectArryBean;

    public boolean isEmpty() {
        return IsEmpty;
    }

    public void setEmpty(boolean empty) {
        IsEmpty = empty;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
    }

    public ShopCollectObjBean getGoodsCollectObjBean() {
        return goodsCollectObjBean;
    }

    public void setGoodsCollectObjBean(ShopCollectObjBean goodsCollectObjBean) {
        this.goodsCollectObjBean = goodsCollectObjBean;
    }

    public ShopCollectArryBean getGoodsCollectArryBean() {
        return goodsCollectArryBean;
    }

    public void setGoodsCollectArryBean(ShopCollectArryBean goodsCollectArryBean) {
        this.goodsCollectArryBean = goodsCollectArryBean;
    }
}
