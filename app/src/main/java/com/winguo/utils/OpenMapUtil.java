package com.winguo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 *跳转地图导航
 * Created by admin on 2017/6/19.
 */

public class OpenMapUtil {

    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;
    /**
     * 启动高德App进行实时导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2017/6/8,13:58
     * <h3>UpdateTime</h3> 2017/6/8,13:58
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname 非必填 POI 名称
     * @param lat 必填 纬度
     * @param lon 必填 经度
     * @param dev 必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style 必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static  void goToNaviActivity(Context context, String sourceApplication , String poiname , String lat , String lon , String dev , String style){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    /**
     * 启动高德地图
     * 默认公交车出行
     * 这里要注意的一点
     * 我用的是BD-09坐标需要转换成GCJ-02
     * 如果你用的是GCJ-02坐标  那就把下面转换的代码注释掉
     * */
    public static void goTominimap(Context context,String currentLongitude,String currentLatitude){
        //将bd-09坐标转换成gcj-02坐标
        double[] bd09_To_Gcj02 = OpenMapUtil.bd09_To_Gcj02(Double.parseDouble(currentLatitude),Double.parseDouble(currentLongitude));
        currentLatitude = bd09_To_Gcj02[0]+"";
        currentLongitude = bd09_To_Gcj02[1]+"";
        try {
            if (isInstallByRead("com.autonavi.minimap")) {
                Intent intent = new Intent("android.intent.action.VIEW",
                        android.net.Uri.parse(
                                "androidamap://route?sourceApplication=位位用车"+"&dlat="+ currentLatitude//终点的经度
                                        + "&dlon="+ currentLongitude//终点的纬度
                                        + "&dev=0" + "&t=1"));
                intent.addCategory("android.intent.category.DEFAULT");
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "没有安装高德地图客户端，请先下载该地图应用", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 启动百度地图
     * 要注意的一点
     * 这里的经度和纬度是反的
     * 如果线路规划失败
     * 那么一定是经纬度反了
     * */
    public static void goToBaiduMap(Context context,String currentLatitude,String currentLongitude,String locLatitude,String locLongitude){
        try {
            if (isInstallByRead("com.baidu.BaiduMap")) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/direction?origin=name:我的位置|latlng:"
                        +locLatitude//起始点纬度
                        +","
                        +locLongitude//起始点经度
                        +"&destination="
                        +currentLongitude//终点经度
                        +","
                        +currentLatitude//终点纬度
                        +"&mode=transit&sy=0&index=0&target=1"));
                intent.setPackage("com.baidu.BaiduMap");
                context.startActivity(intent); // 启动调用
            } else {
                Toast.makeText(context, "没有安装百度地图客户端，请先下载该地图应用", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:02
     * <h3>UpdateTime</h3> 2016/6/27,13:02
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static boolean openBaiduMapByAddress(Context context,String address){
        try {
            if (isInstallByRead("com.baidu.BaiduMap")) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&address="+address));
                intent.setPackage("com.baidu.BaiduMap");
                context.startActivity(intent); // 启动调用
                return true;
            } else {
               // Toast.makeText(context, "没有安装百度地图客户端，请先下载该地图应用", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void openMapByAddress(Context context,String address){
        boolean isOpenBaidu = openBaiduMapByAddress(context, address);
        if (!isOpenBaidu) {
            boolean isOpenGaode = openGaodeMapByAddress(context, address);
            if (!isOpenGaode)
                Toast.makeText(context, "没有安装百度/高德地图客户端，请先下载地图应用", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean openGaodeMapByAddress(Context context,String keyword){
        //URL: //uri.amap.com/search?keyword=美食&center=121.515601,31.233456&city=310000&view=map&src=mypage&coordinate=gaode&callnative=0
        try {
            if (isInstallByRead("com.autonavi.minimap")) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setPackage("com.autonavi.minimap");
                intent.setData(Uri.parse("androidamap://keywordNavi?sourceApplication=softname&keyword="+keyword+"&style=2"));
                context.startActivity(intent); // 启动调用
                return true;
            } else {
               // Toast.makeText(context, "没有安装高德地图客户端，请先下载该地图应用", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat,mgLon};
    }
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     * */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{latitude, lontitude};
    }
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**将gps84转为bd09
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_bd09(double lat,double lon){
        double[] gcj02 = gps84_To_Gcj02(lat,lon);
        double[] bd09 = gcj02_To_Bd09(gcj02[0],gcj02[1]);
        return bd09;
    }
    public static double[] bd09_To_gps84(double lat,double lon){
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        //保留小数点后六位
        gps84[0] = retain6(gps84[0]);
        gps84[1] = retain6(gps84[1]);
        return gps84;
    }

    /**保留小数点后六位
     * @param num
     * @return
     */
    private static double retain6(double num){
        String result = String .format("%.6f", num);
        return Double.valueOf(result);
    }

}
