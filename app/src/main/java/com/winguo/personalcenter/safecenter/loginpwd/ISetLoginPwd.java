package com.winguo.personalcenter.safecenter.loginpwd;

import android.content.Context;

/**
 * Created by admin on 2017/10/11.
 * 定义要实现的功能请求
 */

public interface ISetLoginPwd {
    void setLoginPwd(Context context,String newPwd);
    void modifyLoginPwdByOld(Context context, String oldpwd, String newPwd);
}
