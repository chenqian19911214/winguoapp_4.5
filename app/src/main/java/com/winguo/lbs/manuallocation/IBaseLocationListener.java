package com.winguo.lbs.manuallocation;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.lbs.bean.LbsLocationBean;

import java.util.List;


/**
 * @author hcpai
 * @desc 百度地位回调接口
 */

public abstract class IBaseLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        GBLogUtils.DEBUG_DISPLAY("IBaseLocationListener",bdLocation+"");
        if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
            if (bdLocation.getCity() == null) {
                onError();
            } else {
                LbsLocationBean lbsLocationBean = new LbsLocationBean();
                lbsLocationBean.setLocType(bdLocation.getLocType());
                lbsLocationBean.setLatitude(bdLocation.getLatitude());
                lbsLocationBean.setLongitude(bdLocation.getLongitude());
                lbsLocationBean.setCity(bdLocation.getCity());
                if (bdLocation.getPoiList() != null && !bdLocation.getPoiList().isEmpty()) {
                    final List<Poi> poiList = bdLocation.getPoiList();
                    lbsLocationBean.setDescribe(poiList.get(0).getName());
                }
                onResponse(lbsLocationBean);
            }
        } else {
            GBLogUtils.DEBUG_DISPLAY("IBaseLocationListener",bdLocation+"else");
            onError();
        }

    }


    /**
     * 定位成功
     */
    protected abstract void onResponse(LbsLocationBean lbsLocationBean);

    /**
     * 定位失败
     */
    protected abstract void onError();
}
