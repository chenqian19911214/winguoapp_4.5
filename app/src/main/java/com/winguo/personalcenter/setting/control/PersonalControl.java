package com.winguo.personalcenter.setting.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.winguo.base.BaseRequestCallBack;
import com.winguo.personalcenter.setting.moudle.ISetPersonal;
import com.winguo.personalcenter.setting.moudle.RequestBindTelCallback;
import com.winguo.personalcenter.setting.moudle.RequestModifyHeadCallBack;
import com.winguo.personalcenter.setting.moudle.PersonalRequestNet;
import com.winguo.personalcenter.setting.moudle.RequestModifyNickCallback;

import java.io.File;

/**
 * Created by admin on 2017/10/16.
 * 个人资料
 */

public class PersonalControl implements ISetPersonal {

    private BaseRequestCallBack requestCallBack;
    private PersonalRequestNet requestNet;

    public PersonalControl(BaseRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
        this.requestNet = new PersonalRequestNet();
    }

    @Override
    public void setUserHead(Context context, @NonNull File headFile) {
        requestNet.requestModifyHead(context,headFile,(RequestModifyHeadCallBack) requestCallBack);
    }

    @Override
    public void setUserNick(Context context, @NonNull String newNick) {
        requestNet.requestModifyNickName(context,newNick,(RequestModifyNickCallback)requestCallBack);
    }

    /**
     * 绑定推荐人手机号
     * @param context
     * @param tel
     */
    @Override
    public void bindReferrTel(Context context, @NonNull String tel) {
        requestNet.requestBindingRefereeTel(context,tel,(RequestBindTelCallback) requestCallBack);
    }
}
