package com.winguo.mine.order.list;

import android.view.View;

/**
 * @author hcpai
 * @desc 只要是Pop的包装类 就需要实现该接口
 */
public interface IPopWindowProtocal {
    void initUI();

    void initData();

    void onShow(View anchor);

    void onDismiss();
}
