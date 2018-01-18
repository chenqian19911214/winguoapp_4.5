package com.winguo.mine.collect.goods;

/**
 * @author hcpai
 * @desc 总商品收藏(自定义)
 */
public class GoodsCollectAllBean {
    private boolean IsEmpty;
    private boolean isObject;
    private GoodsCollectObjBean goodsCollectObjBean;
    private GoodsCollectArryBean goodsCollectArryBean;

    @Override
    public String toString() {
        return "GoodsCollectAllBean{" +
                "IsEmpty=" + IsEmpty +
                ", isObject=" + isObject +
                ", goodsCollectObjBean=" + goodsCollectObjBean +
                ", goodsCollectArryBean=" + goodsCollectArryBean +
                '}';
    }

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

    public boolean getIsObject() {
        return isObject;
    }

    public void setIsObject(boolean object) {
        isObject = object;
    }

    public GoodsCollectObjBean getGoodsCollectObjBean() {
        return goodsCollectObjBean;
    }

    public void setGoodsCollectObjBean(GoodsCollectObjBean goodsCollectObjBean) {
        this.goodsCollectObjBean = goodsCollectObjBean;
    }

    public GoodsCollectArryBean getGoodsCollectArryBean() {
        return goodsCollectArryBean;
    }

    public void setGoodsCollectArryBean(GoodsCollectArryBean goodsCollectArryBean) {
        this.goodsCollectArryBean = goodsCollectArryBean;
    }
}
