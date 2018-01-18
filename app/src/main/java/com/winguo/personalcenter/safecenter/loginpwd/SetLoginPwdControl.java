package com.winguo.personalcenter.safecenter.loginpwd;

import android.content.Context;

/**
 * Created by admin on 2017/10/11.
 */

public class SetLoginPwdControl implements ISetLoginPwd {

    private IRequestResultHandle resultHandle;
    public SetLoginPwdControl(IRequestResultHandle resultHandle) {
        this.resultHandle = resultHandle;
    }

    @Override
    public void setLoginPwd(Context context, String newPwd) {

    }

    @Override
    public void modifyLoginPwdByOld(Context context,String oldpwd,String newPwd) {
        RequestNetUtils.requestModifyoginPwdByOld(context,oldpwd,newPwd,resultHandle);
    }

}
