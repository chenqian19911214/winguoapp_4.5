package com.guobi.gblocation;

import android.content.Context;
import android.util.Log;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by admin on 2016/12/23.
 */

public class GBDLocation {

    private Context context;
    private double longitude;
    private double latitude;
    private String city;
    private String province;
    private boolean hasAdd;

    public boolean isHasAdd() {
        return hasAdd;
    }

    private String locationName;
    private String addressStr;
    //  http://api.map.baidu.com/geocoder/v2/?address=羊城商贸&output=json&ak=5o2Uer7LZQlmcPs6MZNA6AD2

    public String getLocationName() {
        return locationName;
    }

    public String getAddress() {
        return addressStr;
    }

    private final Object mLock = new Object();
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    private static GBDLocation _instance = null;

    private GBDLocation(Context mContext) {
        this.context = mContext;
        onCreate();
    }

    public static GBDLocation createInstance(Context appContext) {
        if (_instance == null)
            _instance = new GBDLocation(appContext);
        return _instance;
    }

    public static final GBDLocation getInstance() {

        return _instance;
    }

    public static final void destroyInstance() {
        if (_instance != null) {
            _instance.trash();
            _instance = null;
        }
    }


    public void onCreate() {
        mLocationClient = new LocationClient(context);     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }

    public class MyLocationListener implements BDLocationListener {


        private Address address;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (locationInfoCallBck != null) {
                if (location != null ) {
                    locationInfoCallBck.locationCall(location);
                } else {
                    locationInfoCallBck.locationCall(null);
                }
            }

            hasAdd = location.hasAddr();
           /* city = location.getCity();
            province = location.getProvince();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            addressStr = location.getAddrStr();
            Log.e("address", "adress" + address);//详情地址
            address = location.getAddress();*/
           // Log.e("address", "adress" + address.country + address.province + address.district + address.street + address.streetNumber);
            StringBuffer sb = new StringBuffer(256);

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                city = location.getCity();
                province = location.getProvince();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                addressStr = location.getAddrStr();

                address = location.getAddress();
                Log.e("TypeGpsLocation", " TypeGpsLocation adress" + address);//详情地址

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                city = location.getCity();
                province = location.getProvince();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                addressStr = location.getAddrStr();

                address = location.getAddress();
                Log.e("address", "TypeNetWorkLocation adress" + address);//详情地址

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                city = location.getCity();
                province = location.getProvince();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                addressStr = location.getAddrStr();

                address = location.getAddress();
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                Log.e("address", "TypeOffLineLocation adress" + address);//详情地址
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据


            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                String name = list.get(0).getName();
                locationName = name;
                if (mCallBack != null) {
                    Log.e("hcpai", "onReceiveLocation1: ");
                    mCallBack.locationCall(name);
                }
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            } else {
                Log.e("hcpai", "onReceiveLocation2: ");
                if (mCallBack!=null)
                    mCallBack.locationCall(null);
            }

            stopLocation();

            Log.i("BaiduLocationApiDem", sb.toString());
        }
        /*@Override
        public void onConnectHotSpotMessage(String s, int i) {

        }*/
    }

    private ILocationCallBck mCallBack;
    private ILocationInfoCallBck locationInfoCallBck;

    public void startLocation(ILocationCallBck callBack) {
        this.mCallBack = callBack;
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }
    public void startRequestLocation(ILocationInfoCallBck callBack) {
        this.locationInfoCallBck = callBack;
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }


    public void stopLocation() {
        if (mLocationClient.isStarted()) {
            synchronized (mLock) {
                mLocationClient.stop();
            }
        }
    }

    public final void trash() {
        stopLocation();
        mLocationClient.unRegisterLocationListener(myListener);
    }
}