package com.winguo.personalcenter.safecenter.loginpwd;

/**
 * Created by admin on 2017/10/11.
 * 请求结果处理
 */

public interface IRequestResultHandle {
    void requestSetLoginPwdResult(String result);
    void requestSetLoginPwdErrorCode(int errorcode);
    void requestModfifyLoginPwdByOldResult(String result);
    void requestModfifyLoginPwdByOldErrorCode(int errorcode);
}
