package com.guobi.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

/**
 * Created by chenq on 2017/1/3.
 */
public class WinguoAccountBank implements Parcelable {
    /**
     * 银行ID
     */
    public String id;

    /**
     * 银行代码
     */
    public String code;

    /**
     * 银行名称
     */
    public String name;

    /**
     * 缩写
     */
    public String nameAB;


    public final WinguoAccountBank copy() {
        final WinguoAccountBank c = new WinguoAccountBank();
        c.id = this.id;
        c.name = this.name;
        c.code = this.code;
        c.nameAB = this.nameAB;
        return c;
    }

    public final void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "id:		" + id);
            GBLogUtils.DEBUG_DISPLAY(this, "Name:	" + name);
            GBLogUtils.DEBUG_DISPLAY(this, "code:	" + code);
            GBLogUtils.DEBUG_DISPLAY(this, "nameAB:	" + nameAB);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public String toString() {
        return "WinguoAccountBank{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", nameAB='" + nameAB + '\'' +
                '}';
    }
}
