package com.winguo.personalcenter.setting.moudle;

import com.winguo.base.BaseRequestCallBack;

/**
 * Created by admin on 2017/10/16.
 */

public interface RequestModifyHeadCallBack extends BaseRequestCallBack{

    void requestModifyHeadResult(String result);
    void requestModifyHeadErrorCode(int errorcode);

}
