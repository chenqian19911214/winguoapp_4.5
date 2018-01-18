package com.winguo.net;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.FileUtil;
import com.winguo.utils.XmlUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by on 2016/9/22.
 */
public class MyOkHttpUtils {
    /**
     * 设置缓存路径
     *
     * @param fileName 缓存文件名称
     * @return
     */
    private static OkHttpClient setClient(String fileName) {
        File fileDir = null;
        if (FileUtil.isSdcardAvailable()) {
            fileDir = new File(FileUtil.getSdDir(CommonUtil.getAppContext(), "doc"));
        } else {
            fileDir = new File(FileUtil.getDiskDir(CommonUtil.getAppContext(), "doc"));
        }
        if (fileName == null) {
            File cacheFile = new File(fileDir, String.valueOf(System.currentTimeMillis()));
        }
        File cacheFile = new File(fileDir, fileName);
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        //创建缓存对象
        final Cache cache = new Cache(cacheFile, cacheSize);
        return new OkHttpClient().newBuilder().cache(cache).build();

    }

    /**
     * @param url          下载文件的url
     * @param fileCallBack 回调
     */
    public static void downFile(String url, Map<String, String> map, FileCallBack fileCallBack) {
        OkHttpUtils
                .post()
                .params(map)
                .url(url)
                .build()
                .execute(fileCallBack);
    }

    /**
     * get请求,不加缓存地址
     *
     * @param map               请求参数，存入map集合中
     * @param url               请求url
     * @param tag               请求标记
     * @param stringCallBackack 请求回调
     */
    public static void get(HashMap<String, String> map, String url, String tag, final IStringCallBack stringCallBackack) {
        OkHttpUtils
                .get()
                .params(map)
                .url(url)
                .tag(tag)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        boolean json = isJson(response);
                        if (json && response != null) {
                            stringCallBackack.stringReturn(response);
                        } else {
                            response = XmlUtil.xmlToJson(response);
                            stringCallBackack.stringReturn(response);
                        }

                    }
                });
    }

    /**
     * get请求，带缓存地址
     *
     * @param fileName          自定义缓存名称
     * @param map               请求参数，存入map集合中
     * @param url               请求url
     * @param tag               请求标记
     * @param stringCallBackack 请求回调
     */
    public static void get(String fileName, HashMap<String, String> map, String url, int tag, final IStringCallBack stringCallBackack) {
        OkHttpUtils
                .initClient(setClient(fileName))
                .get()
                .params(map)
                .url(url)
                .tag(tag)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public boolean validateReponse(Response response, int id) {
                        if (!response.isSuccessful()) {
                            stringCallBackack.exceptionMessage(response.message());
                        }
                        return super.validateReponse(response, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //错误的处理
                        if (e!=null)
                            stringCallBackack.exceptionMessage(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        boolean json = isJson(response);
                        if (json && response != null) {
                            stringCallBackack.stringReturn(response);
                        } else {
                            response = XmlUtil.xmlToJson(response);
                            stringCallBackack.stringReturn(response);
                        }

                    }
                });

    }

    /**
     * post请求，不带缓存地址
     *
     * @param url        请求url
     * @param tag        请求标记
     * @param map        请求参数，存入map集合中
     * @param stringBack 请求回调
     */
    public static void post(String url, int tag, HashMap<String, String> map, final IStringCallBack stringBack) {

        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(tag)
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {

                    @Override
                    public boolean validateReponse(Response response, int id) {
                        if (!response.isSuccessful()) {
                            stringBack.exceptionMessage(response.message());
                        }
                        return super.validateReponse(response, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        if(e!=null){
                            CommonUtil.printD("请求失败：=", e.toString());//D/请求失败：=: java.net.SocketTimeoutException
                            stringBack.exceptionMessage(e.getMessage());
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        boolean json = isJson(response);
                        if (json && response != null) {
                            stringBack.stringReturn(response);
                        } else {
                            response = XmlUtil.xmlToJson(response);
                            stringBack.stringReturn(response);

                        }

                    }

                });

    }

    /**
     * post请求，带缓存地址
     *
     * @param fileName   自定义缓存名称
     * @param map        请求参数，存入map集合中
     * @param url        请求url
     * @param tag        请求标记
     * @param stringBack 请求回调
     */
    public static void post(String fileName, String url, int tag, HashMap<String, String> map, final IStringCallBack stringBack) {

        OkHttpUtils
                .initClient(setClient(fileName))
                .post()
                .url(url)
                .params(map)
                .tag(tag)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public boolean validateReponse(Response response, int id) {
                        if (!response.isSuccessful()) {
                            stringBack.exceptionMessage(response.message());
                        }
                        return super.validateReponse(response, id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();

                        if(e!=null){
                            CommonUtil.printD("请求失败：1=", e.toString());
                            stringBack.exceptionMessage(e.toString());
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        boolean json = isJson(response);
                        if (json && response != null) {
                            stringBack.stringReturn(response);
                        } else {
                            response = XmlUtil.xmlToJson(response);
                            stringBack.stringReturn(response);
                        }
                    }

                });
    }

    /**
     * 文件上传
     *
     * @param url            上传地址
     * @param tag            上传标记
     * @param map            上传的字符参数
     * @param file           上传的文件
     * @param fileKey        是上传一个文件的键值名
     * @param filename       文件的名字
     * @param stringCallBack 回调接口
     */
    public static void upLoadOneFile(String url, int tag, HashMap<String, String> map, File file, String fileKey, String filename, StringCallback stringCallBack) {
        OkHttpUtils.post()
                .addFile(fileKey, filename, file)
                .url(url)
                .params(map)
                .tag(tag)
                .build()
                .execute(stringCallBack);
        //对一次上传多个文件的例子
//        PostFormBuilder builder =  OkHttpUtils.post().url(url).params().tag();//
//        for(int i=1;i<files.length;i++){
//            builder.addParams();
//            .....
//        }
//        builder.build().execute(stringCallBack);

    }


    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * 取消网络请求
     *
     * @param tag 请求标记
     */
    public static void cancleTag(int tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }


    public static Bitmap getBitmapFormUrl(String url) {

        Bitmap bitmap = null;
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(3000, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            Request request = new Request.Builder().url(url).build();
            ResponseBody body = client.newCall(request).execute().body();
            InputStream in = body.byteStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    public static String getJsonStrFormUrlByPost(IStringCallBack callBack, String url) {
        String result = "";
        OkHttpClient client = null;
        Request request = null;
        try {
           /* Response response = OkHttpUtils.post()
                    .tag(0)
                    .url(url)
                    .build().readTimeOut(3000).connTimeOut(3000).execute();*/
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5000, TimeUnit.SECONDS);
            client = builder.build();
            request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            boolean successful = response.isSuccessful();
            int code = response.code();
            String message = response.message();
            String s = response.request().url().toString();
            GBLogUtils.DEBUG_DISPLAY("getJsonStrFormUrlByPost=====code", code + "");
            GBLogUtils.DEBUG_DISPLAY("getJsonStrFormUrlByPost=====message", message + "");
            GBLogUtils.DEBUG_DISPLAY("getJsonStrFormUrlByPost=====s", s + "");

            if (successful) {
                String res = response.body().string();
                boolean json = isJson(res);
                if (!json) {
                    result = XmlUtil.xmlToJson(res);
                } else {
                    result = res;
                }
            } else {
                callBack.exceptionMessage(response.message());
                throw new IOException(String.format("请求版本时网络异常,response code:%s", response.code()));
            }

        } catch (SocketTimeoutException e) {
            //网络访问超时处理
           // ToastUtil.showToast(context, "网络访问超时，请检查网络！");
            callBack.exceptionMessage("网络访问超时，请检查网络！");

        } catch (UnknownHostException e) {
            //ToastUtil.showToast(context, "未连接网络！");
            e.printStackTrace();
            callBack.exceptionMessage("未连接网络！");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
        return result;
    }


}
