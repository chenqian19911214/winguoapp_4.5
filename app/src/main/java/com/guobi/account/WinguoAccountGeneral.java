package com.guobi.account;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 保存登陆成功后的数据
 * Created by chenq on 2017/1/3.
 */
public class WinguoAccountGeneral implements Serializable {
    public String id; // 自动分配的ID
    public String accountName; //正式登录账号名
    public String userName; //用户名(昵称)
    public String password; //密码

    public int void_flag; //0.有效，1.无效
    public int type; //0.个人，1.公司

    public int usr_type;// 0: 个人网店
    public boolean shared; // true: 表示审核通过的个人网店

    public String icoUrl; // 头像图片下载地址
    public String shopName; // 网店名称
    public String mobileShopAddr; // 移动商城地址
    public String pcShopAddr; // PC商城地址

    public String regDate; //注册时间
    public String updateDate; //更新时间
    public String lastLoginDate; //最后登录时间

    public String telMobile; //绑定的手机号码
    public String qr_code; //二维码地址

    public String maker_id; // 店铺id
    public String maker_shop_type; //店铺类型 0旗舰店 1专卖店 2 专营店 3本地服务 4实体店

    /**
     * 头像图片数据
     * 如果这个值为NULL，表示该用户没有头像，显示默认头像
     */
    public byte[] icoData;

    public String shopAddrPrefix;//个人商城前缀
    /**
     * 现金券
     */
    public double cashCoupon;
    /**
     * 信用卡余额
     */
    public String cashCredit;

    public int isCreater;  //0 普通 1 创客

    /**
     * 注 添加新的字段时，copy() 方法依照添加
     */
    public final WinguoAccountGeneral copy() {
        final WinguoAccountGeneral c = new WinguoAccountGeneral();
        c.id = this.id;
        c.accountName = this.accountName;
        c.userName = this.userName;
        c.password = this.password;
        c.cashCoupon = this.cashCoupon;
        c.cashCredit = this.cashCredit;
        c.void_flag = this.void_flag;
        c.isCreater= this.isCreater;
        c.type = this.type;
        c.usr_type = this.usr_type;
        c.shared = this.shared;
        c.icoUrl = this.icoUrl;
        c.shopName = this.shopName;
        c.mobileShopAddr = this.mobileShopAddr;
        c.pcShopAddr = this.pcShopAddr;
        c.regDate = this.regDate;
        c.updateDate = this.updateDate;
        c.lastLoginDate = this.lastLoginDate;
        c.telMobile = this.telMobile;
        c.shopAddrPrefix = this.shopAddrPrefix;
        c.qr_code = this.qr_code;
        c.maker_id = this.maker_id;
        c.maker_shop_type = this.maker_shop_type;
        if (this.icoData != null) {
            c.icoData = this.icoData.clone();
        } else {
            c.icoData = null;
        }
        return c;
    }


    public final void dump() {
        if (GBLogUtils.DEBUG) {
            GBLogUtils.DEBUG_DISPLAY(this, "id:		" + id);
            GBLogUtils.DEBUG_DISPLAY(this, "accountName:	" + accountName);
            GBLogUtils.DEBUG_DISPLAY(this, "void_flag:		" + void_flag);
            GBLogUtils.DEBUG_DISPLAY(this, "type:	" + type);
            GBLogUtils.DEBUG_DISPLAY(this, "usr_type:	" + usr_type);
            GBLogUtils.DEBUG_DISPLAY(this, "shared:	" + shared);
            GBLogUtils.DEBUG_DISPLAY(this, "icoUrl:	" + icoUrl);
            GBLogUtils.DEBUG_DISPLAY(this, "shopName:	" + shopName);
            GBLogUtils.DEBUG_DISPLAY(this, "mobileShopAddr:	" + mobileShopAddr);
            GBLogUtils.DEBUG_DISPLAY(this, "pcShopAddr:	" + pcShopAddr);
            GBLogUtils.DEBUG_DISPLAY(this, "regDate:		" + regDate);
            GBLogUtils.DEBUG_DISPLAY(this, "updateDate:	" + updateDate);
            GBLogUtils.DEBUG_DISPLAY(this, "lastLoginDate:		" + lastLoginDate);
            GBLogUtils.DEBUG_DISPLAY(this, "password:		" + password);
            GBLogUtils.DEBUG_DISPLAY(this, "isCreater:		" + isCreater);
            GBLogUtils.DEBUG_DISPLAY(this, "cashCoupon:		" + cashCoupon);
            GBLogUtils.DEBUG_DISPLAY(this, "telMobile:	" + telMobile);
            GBLogUtils.DEBUG_DISPLAY(this, "shopAddrPrefix:	" + shopAddrPrefix);
            GBLogUtils.DEBUG_DISPLAY(this, "make_id:	" + maker_id);
            GBLogUtils.DEBUG_DISPLAY(this, "make_shop_type:	" + maker_shop_type);

            if (icoData != null) {
                GBLogUtils.DEBUG_DISPLAY(this, "icoData(" + icoData.length + "):" +
                        icoData.toString());
            }
        }
    }

    @Override
    public String toString() {
        return "WinguoAccountGeneral{" +
                "id='" + id + '\'' +
                ", accountName='" + accountName + '\'' +
                ", userName='" + userName + '\'' +
                ", void_flag=" + void_flag +
                ", type=" + type +
                ", password=" + password +
                ", usr_type=" + usr_type +
                ", shared=" + shared +
                ", icoUrl='" + icoUrl + '\'' +
                ", shopName='" + shopName + '\'' +
                ", mobileShopAddr='" + mobileShopAddr + '\'' +
                ", pcShopAddr='" + pcShopAddr + '\'' +
                ", regDate='" + regDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", isCreater='" + isCreater + '\'' +
                ", telMobile='" + telMobile + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", maker_id='" + maker_id + '\'' +
                ", maker_shop_type='" + maker_shop_type + '\'' +
                ", icoData=" + Arrays.toString(icoData) +
                ", shopAddrPrefix='" + shopAddrPrefix + '\'' +
                '}';
    }
}
