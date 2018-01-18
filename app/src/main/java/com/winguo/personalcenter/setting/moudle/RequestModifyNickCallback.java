package com.winguo.personalcenter.setting.moudle;

import com.winguo.base.BaseRequestCallBack;

/**
 * Created by admin on 2017/10/17.
 * 修改昵称 回调接口
 */

public interface RequestModifyNickCallback extends BaseRequestCallBack {
    void requestModifyNickResult(String result);
    void requestModifyNickErrorCode(int errorcode);
}
