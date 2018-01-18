package com.winguo.personalcenter.setting.moudle;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by admin on 2017/10/16.
 * 个人资料 实现功能
 */

public interface ISetPersonal {
    void setUserHead(Context context,@NonNull File headFile);
    void setUserNick(Context context,@NonNull String newNick);
    void bindReferrTel(Context context,@NonNull String tel);
}
