package com.winguo.personalcenter.setting.moudle;

import com.winguo.base.BaseRequestCallBack;

/**
 * 绑定推荐人手机 回调接口
 */

public interface RequestBindTelCallback extends BaseRequestCallBack {
    void requestBindTelResult(String result);
    void requestBindTelErrorCode(int errorcode);
}
