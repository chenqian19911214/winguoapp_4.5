package com.winguo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.winguo.R;
import com.winguo.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2017/3/21.
 */

public class WebViewQRUtils {

    //解决网页中二维码图片识别功能
    private CustomDialog mCustomDialog;
    private ArrayAdapter<String> adapter;
    /**判断是否为二维码*/
    private boolean isQR;
    private Result result;//二维码解析结果
    private File file;
    private Activity activity;

    public WebViewQRUtils(Activity activity) {
        this.activity = activity;
    }

    public void startOnLongCallBack(String imgUrl){
        // 获取到图片地址后做相应的处理
        MyAsyncTask mTask = new MyAsyncTask();
        mTask.execute(imgUrl);
        showDialog();
    }

    /**
     * 判断是否为二维码
     * param url 图片地址
     * return
     */
    private boolean decodeImage(String sUrl){
        result = DecodeImage.handleQRCodeFormBitmap(getBitmap(sUrl));
        if(result == null){
            isQR = false;
        }else {
            isQR = true;
        }
        return isQR;
    }
    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (isQR){
                handler.sendEmptyMessage(0);
            }
        }
        @Override
        protected String doInBackground(String... params) {
            decodeImage(params[0]);
            return null;
        }
    }
    /**
     * 根据地址获取网络图片
     * @param sUrl 图片地址
     * @return
     * @throws IOException
     */
    public Bitmap getBitmap(String sUrl){
        try {
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                saveMyBitmap(bitmap,"code");//先把bitmap生成jpg图片
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示Dialog
     * param v
     */
    private void  showDialog() {
        initAdapter();
        mCustomDialog = new CustomDialog(activity) {
            @Override
            public void initViews() {
                // 初始CustomDialog化控件
                ListView mListView = (ListView) findViewById(R.id.lv_dialog);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 点击事件
                        switch (position) {
                            case 0:

                                sendToFriends();//把图片发送给好友
                                closeDialog();
                                break;
                            case 1:
                                saveImageToGallery(activity);
                                closeDialog();
                                break;
                            case 2:
                                Toast.makeText(activity, "已收藏", Toast.LENGTH_LONG).show();
                                closeDialog();
                                break;
                            case 3:
                                goIntent();
                                closeDialog();
                                break;
                        }

                    }
                });
            }
        };
        mCustomDialog.show();
    }

    /**
     * 初始化数据
     */
    private void initAdapter() {
        adapter = new ArrayAdapter<String>(activity, R.layout.item_dialog);
        adapter.add("发送给朋友");
        adapter.add("保存到手机");
        adapter.add("收藏");
    }
    /**
     * 是二维码时，才添加为识别二维码
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                if (isQR){
                    adapter.add("识别图中二维码");
                }
                adapter.notifyDataSetChanged();
            }
        };
    };

    /**
     * 发送给好友
     */
    private void sendToFriends() {
        Intent intent=new Intent(Intent.ACTION_SEND);
        Uri imageUri=  Uri.parse(file.getAbsolutePath());
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
    }

    /**
     * bitmap 保存为jpg 图片
     * @param mBitmap 图片源
     * @param bitName  图片名
     */
    public void saveMyBitmap(Bitmap mBitmap,String bitName)  {
        file= new File( Environment.getExternalStorageDirectory()+"/"+bitName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 先保存到本地再广播到图库
     * */
    public void saveImageToGallery(Context context) {

        // 其次把文件插入到系统图库
        try {

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), "code", null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void goIntent(){
        Uri uri = Uri.parse(result.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        activity.startActivity(intent);
    }

}
