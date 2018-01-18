package com.google.zxing.client.android.camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.google.zxing.client.android.camera.open.OpenCamera;
import com.winguo.R;
import com.winguo.login.register.RegisterActivity;
import com.winguo.login.register.ScanRegisterActivity;
import com.winguo.utils.DecodeImage;
import com.winguo.view.SelfDialog;
import com.winguo.view.VoucherDialog;

import java.io.FileNotFoundException;

public class ZxingCaptureActivity extends BaseCaptureActivity implements View.OnClickListener {

    private static final String TAG = "WeChatCaptureActivity";

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    private ImageButton Actionback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_capture);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        Actionback = (ImageButton) findViewById(R.id.actionbar_back_id);
//        Actionflashlight = (ImageButton) findViewById(R.id.actionbar_flashlight_id);
        findViewById(R.id.open_photo).setOnClickListener(this);
        findViewById(R.id.register_zxing_id).setOnClickListener(this);

        Actionback.setOnClickListener(this);
//        Actionflashlight.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {

        if (rawResult.getText().length() == 0) {// 返回结果为空 重新扫描
            reScan();
        } else {
            playBeepSoundAndVibrate();// 执行声音 振动
            ImplementZxing(rawResult);
        }
    }

    /**
     * 执行 Zxing 操作
     */
    private void ImplementZxing(Result rawResult) {
        String result = rawResult.getText();
        String url = "";
        Log.i(TAG, "  rawResult :" + rawResult);
        String winguoregister = rawResult.toString();
        if (winguoregister.contains("winguo.com/register")) {

            Intent intent = new Intent(getBaseContext(), ScanRegisterActivity.class);
            intent.setAction("zxingRegister");
            intent.putExtra("mobileShopAddr", winguoregister);
            startActivity(intent);
            finish();
        } else {
            methodImplementation(result);

        }
    }

    /**
     * 执行zxing 原来的方法
     */
    private void methodImplementation(String result) {
        String url;
        if (result.length() > 5) {
            String http = result.substring(0, 4);
            String httpCase = http.toLowerCase();//转换成小写
            String https = result.substring(0, 5);
            String httpsCase = https.toLowerCase();

            if ((https.equals("https") || (https.equals("HTTPS"))) && https.length() >= 5) {

                url = httpsCase + result.substring(5);

                if (url.contains("winguo")) {
                    OpenUrl(url);
                } else {

                    openDialog(getApplicationContext());
                }


            } else if ((http.equals("http") || (http.equals("HTTP"))) && http.length() >= 4) {// 如果是网址，就打开本地浏览器

                url = httpCase + result.substring(4);

                if (url.contains("winguo")) {
                    OpenUrl(url);
                } else {

                    openDialog(getApplicationContext());
                }

            } else {// 否则就直接弹出 Toast

               openDialog(getApplicationContext());

            }
        } else {
            openDialog(getApplicationContext());
        }

    }


    /**
     * @param contex 不是问果二维码的 对话框
     */
    private void openDialog(Context contex) {
        final SelfDialog dialog=new SelfDialog(ZxingCaptureActivity.this,false,getString(R.string.qr_wrong_title),
                getString(R.string.qr_wrong_content1),  getString(R.string.qr_wrong_content2),getString(R.string.qr_wrong_cancle_btn),
                getString(R.string.qr_wrong_confirm_btn));
        dialog.setOnClickNegativeButton(new SelfDialog.OnClickNegativeButton() {
            @Override
            public void onClickNegativeButton() {
                dialog.dismiss();
                reScan();
            }
        });
        dialog.setOnClickPositiveButton(new SelfDialog.OnClickPositiveButton() {
            @Override
            public void onClickPositiveButton() {
                dialog.dismiss();
                Intent intent1 = new Intent(ZxingCaptureActivity.this, RegisterActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        dialog.show();
    }

    /**
     * 打开内置浏览器
     */
    private void OpenUrl(String url) {
        Log.i(TAG, "http :" + url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        startActivity(intent);
        finish();
    }

    boolean isOpen = false;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.actionbar_back_id:
                finish();
                break;

//            case R.id.actionbar_flashlight_id:
//
//                if (!isOpen) {
//
//                    isLightEnable(true);
////                    Actionflashlight.setBackgroundResource(R.drawable.sdton);
//                    isOpen = true;
//                } else {
//                    Log.i(TAG, "false");
//                    isLightEnable(false);
//                    Actionflashlight.setBackgroundResource(R.drawable.sdtoff);
//                    isOpen = false;
//                }
//                break;
            case R.id.open_photo://打开相册

                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
                break;
            case R.id.register_zxing_id:
                //快速注册
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("Exception", "requestCode;" + requestCode + "  resultCode:" + resultCode);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));//将拿到的 二维码数据转换从Bitmap;
                Result result = DecodeImage.handleQRCodeFormBitmap(bitmap);//通过zxing 识别二维码
                if (result != null) {
                    playBeepSoundAndVibrate();// 执行声音 振动
                    ImplementZxing(result);
                } else {
                    Toast.makeText(this, R.string.QRCodenull, Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                Log.i("Exception", "Exception" + e.getMessage(), e);
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 打开关闭闪光灯
     *
     * @param isEnable isEnable = true 打开闪光灯  false 关闭闪光灯；
     */
    private void isLightEnable(boolean isEnable) {
        Camera camera = OpenCamera.getCamera();
        if (isEnable) {

            if (camera != null) {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameter);
            }
        } else {
            if (camera != null) {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameter);
            }
        }
    }
}
