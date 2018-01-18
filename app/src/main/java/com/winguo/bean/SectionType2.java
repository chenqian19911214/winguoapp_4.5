package com.winguo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 附近实体店商品和店铺
 * Created by admin on 2017/6/6.
 */

public class SectionType2 extends SectionEntity<StoreDetail2.ResultBean.ItemsBean> implements Parcelable {
    private boolean isMore;
    private boolean isAdd;
    private int number;
    private double selectPrice;
    private String selectSpec;
    private String sku_id;
    private BaseViewHolder helper;

    public SectionType2(StoreDetail2.ResultBean.ItemsBean resultBean) {
        super(resultBean);
    }

    public SectionType2(boolean isHeader, String header, boolean isMore) {
        super(isHeader, header);
        this.isMore = isMore;
    }


    protected SectionType2(Parcel in) {
        isMore = in.readByte() != 0;
        isAdd = in.readByte() != 0;
        number = in.readInt();
        selectPrice = in.readDouble();
        selectSpec = in.readString();
        sku_id = in.readString();
        t = (StoreDetail2.ResultBean.ItemsBean) in.readSerializable();
    }

    public static final Creator<SectionType2> CREATOR = new Creator<SectionType2>() {
        @Override
        public SectionType2 createFromParcel(Parcel in) {
            return new SectionType2(in);
        }

        @Override
        public SectionType2[] newArray(int size) {
            return new SectionType2[size];
        }
    };

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public double getSelectPrice() {
        return selectPrice;
    }

    public void setSelectPrice(double selectPrice) {
        this.selectPrice = selectPrice;
    }

    public String getSelectSpec() {
        return selectSpec;
    }

    public void setSelectSpec(String selectSpec) {
        this.selectSpec = selectSpec;
    }

    public BaseViewHolder getHelper() {
        return helper;
    }

    public void setHelper(BaseViewHolder helper) {
        this.helper = helper;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isMore ? 1 : 0));
        dest.writeByte((byte) (isAdd ? 1 : 0));
        dest.writeInt(number);
        dest.writeDouble(selectPrice);
        dest.writeString(selectSpec);
        dest.writeString(sku_id);
      //  dest.writeSerializable(t);
    }
}
