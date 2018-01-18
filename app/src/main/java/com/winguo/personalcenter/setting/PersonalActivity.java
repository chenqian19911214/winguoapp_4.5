package com.winguo.personalcenter.setting;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.account.GBAccountError;
import com.guobi.account.GBAccountMgr;
import com.guobi.account.WinguoAccountGeneral;
import com.winguo.R;
import com.winguo.app.StartApp;
import com.winguo.base.BaseTitleActivity;
import com.winguo.mine.address.acitvity.AddressManageActivity;
import com.winguo.personalcenter.MyQRCodeActivity;
import com.winguo.personalcenter.setting.control.PersonalControl;
import com.winguo.personalcenter.setting.moudle.RequestModifyHeadCallBack;
import com.winguo.utils.ActionUtil;
import com.winguo.utils.ChooseDateInterface;
import com.winguo.utils.ChooseDateUtil;
import com.winguo.utils.Constants;
import com.winguo.utils.ImageUtil;
import com.winguo.utils.Intents;
import com.winguo.utils.LruCacheUtils;
import com.winguo.utils.NetWorkUtil;
import com.winguo.utils.PersonInfoSQLOperateImpl;
import com.winguo.utils.PersonInfoSQLiteOpenHelper;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CircleImageView;
import com.winguo.view.GenderChoicePopWindow;
import com.winguo.view.SelectPicPopWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 个人资料（头像修改 个人信息）
 * Created by admin on 2016/12/8.
 */

public class PersonalActivity extends BaseTitleActivity implements ImageUtil.CropHandler, View.OnClickListener, RequestModifyHeadCallBack {

    private CircleImageView userHead;
    private ImageView userQR;
    private SelectPicPopWindow mMenuPop;
    private FrameLayout userNickFL;
    private FrameLayout userUidFL;
    private FrameLayout userQRFL;
    private FrameLayout userPhoneFL;
    private FrameLayout userGenderFL;
    private FrameLayout userAreaFL;
    private FrameLayout userAgeFL;
    private FrameLayout userHeadFL;
    private FrameLayout person_info_more_container;
    private FrameLayout person_info_more_fl;
    private TextView userNick;
    private TextView userArea;
    private TextView userGender;
    private TextView userAge;
    private TextView userPhone;
    private TextView userUID;
    private GenderChoicePopWindow genderChoice;
    private boolean isLogin;
    private String userImagURL;
    private String accountName;
    private PersonInfoSQLOperateImpl operate;
    private PersonalControl control;
    private Intent camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        setBackBtn();
        initDatas();
        initViews();
        initListener();
    }

    private void initListener() {

        userHeadFL.setOnClickListener(this);
        userAgeFL.setOnClickListener(this);
        userAreaFL.setOnClickListener(this);
        userGenderFL.setOnClickListener(this);
        userNickFL.setOnClickListener(this);
        userPhoneFL.setOnClickListener(this);
        userQRFL.setOnClickListener(this);
        userUidFL.setOnClickListener(this);
    }

    private void initViews() {

        userHeadFL = (FrameLayout) findViewById(R.id.user_head_fl);
        userAgeFL = (FrameLayout) findViewById(R.id.user_age_fl);
        userAreaFL = (FrameLayout) findViewById(R.id.user_area_fl);
        userGenderFL = (FrameLayout) findViewById(R.id.user_gender_fl);
        userPhoneFL = (FrameLayout) findViewById(R.id.user_phone_fl);
        userQRFL = (FrameLayout) findViewById(R.id.user_qr_fl);
        userUidFL = (FrameLayout) findViewById(R.id.user_uid_fl);
        userNickFL = (FrameLayout) findViewById(R.id.user_nickname_fl);
        person_info_more_fl = (FrameLayout) findViewById(R.id.person_info_more_fl);


        userHead = (CircleImageView) findViewById(R.id.user_head);
        userQR = (ImageView) findViewById(R.id.user_qr);
        userNick = (TextView) findViewById(R.id.user_nickname);
        userArea = (TextView) findViewById(R.id.user_area);
        userGender = (TextView) findViewById(R.id.user_gender);
        userAge = (TextView) findViewById(R.id.user_age);
        userPhone = (TextView) findViewById(R.id.user_phone);
        userUID = (TextView) findViewById(R.id.user_uid);

        if (isLogin) {
            if (!TextUtils.isEmpty(userImagURL)) {
                userHead.setTag(userImagURL);
                if (StartApp.lruCache != null) {
                    StartApp.lruCache.downLoaderBitmap(userHead, new LruCacheUtils.ImageLoaderlistener() {
                        @Override
                        public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                            if (bitmap!=null)
                                imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            }

        }


        mMenuPop = new SelectPicPopWindow(PersonalActivity.this, PersonalActivity.this);
        genderChoice = new GenderChoicePopWindow(this, this);

        setData();
    }

    private void setData() {
        //数据库获取保存的个人信息
        String address = operate.findByName(accountName, PersonInfoSQLiteOpenHelper.USER_DEFALUT_ADDRESS);
        String age = operate.findByName(accountName, PersonInfoSQLiteOpenHelper.USER_AGE);
        String sex = operate.findByName(accountName, PersonInfoSQLiteOpenHelper.USER_SEX);
        String spaceName = operate.findByName(accountName, PersonInfoSQLiteOpenHelper.USER_SPACE_NAME);
        String tel = operate.findByName(accountName, PersonInfoSQLiteOpenHelper.USER_TEL);

        if (age != null) {
            if (!TextUtils.isEmpty(age)) {
                userAge.setText(age);
            }
        }
        if (sex != null) {
            if (!TextUtils.isEmpty(sex)) {
                userGender.setText(sex);
            }
        }
        if (spaceName != null) {
            if (!TextUtils.isEmpty(spaceName)) {
                userNick.setText(spaceName);
            }
        }
        if (address != null)
            if (!TextUtils.isEmpty(address)) {
                userArea.setText(address);
            }
        if (tel != null) {
            if (!TextUtils.isEmpty(tel)) {
                Log.i("personal", "tel" + tel);
                String replace = tel.replaceAll("\\s*", "");
                String subhead = replace.substring(0, 3);
                String subfoot = replace.substring(7);
                String modPhone = subhead + "****" + subfoot;
                userPhone.setText(modPhone);
            }
        }
    }


    private void initDatas() {

        //是否登陆过 获取记录

        isLogin = SPUtils.contains(this, "accountName");
        if (isLogin) {
            accountName = (String) SPUtils.get(this, "accountName", "");
            userImagURL = (String) SPUtils.get(this, "userImagURL", "");
            operate = new PersonInfoSQLOperateImpl(this);
        }
        control = new PersonalControl(this);

    }

    private final static int NICKNAME_CODE = 0X145;
    private final static int DEFAULT_ADDRESS = 5;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//相机申请成功
                startActivityForResult(camera, ImageUtil.REQUEST_CAMERA);

            } else {//相机申请失败
                com.winguo.utils.Intents.noCameraPermissionStatus(this, getString(R.string.camera_permission_off), getString(R.string.give_up));
              //  Toast.makeText(this, getResources().getString(R.string.camera_permission_off), Toast.LENGTH_SHORT).show();
             //   Intents.OpenPermissionsSetting(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_gender_fl:
                //性别选择器
                genderChoice.showAtLocation(userAreaFL, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.gender_chioce_men:
                //男
                genderChoice.dismiss();
                userGender.setText(getString(R.string.personal_info_gender_men));
                operate.updata(accountName, PersonInfoSQLiteOpenHelper.USER_SEX, "男");
                break;
            case R.id.gender_chioce_women:
                //女
                genderChoice.dismiss();
                userGender.setText(getString(R.string.personal_info_gender_women));
                operate.updata(accountName, PersonInfoSQLiteOpenHelper.USER_SEX, "女");
                break;
            case R.id.user_age_fl:
                //出生日期 选择器
                chooseDateDialog();
                break;
            case R.id.user_area_fl:
                //默认 收货地址 跳转收货地址管理页面
                Intent intentAddress = new Intent();
                intentAddress.putExtra(ActionUtil.ACTION_ADDRESS_MANAGE, true);
                intentAddress.setClass(this, AddressManageActivity.class);
                startActivityForResult(intentAddress, DEFAULT_ADDRESS);
                break;
            case R.id.user_nickname_fl:
                //用户昵称 添加或修改
                Intent it = new Intent(this, CommonInputActivity.class);
                it.putExtra("type", "昵称");
                startActivityForResult(it, NICKNAME_CODE);
                break;

            case R.id.user_head_fl:
                //头像设置
                showSelectPop();
                break;
            case R.id.phone_camera://去打开相机
                mMenuPop.dismiss();
                camera = ImageUtil.getInstance(getContext()).buildCameraIntent();

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    try {
                        startActivityForResult(camera, ImageUtil.REQUEST_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Intents.OpenPermissionsSetting(this);
                    }
                }

                break;
            case R.id.phone_gallery:
                mMenuPop.dismiss();
                Intent gallery = ImageUtil.getInstance(getContext()).buildGalleryIntent();
                startActivityForResult(gallery, ImageUtil.REQUEST_GALLERY);
                break;
            case R.id.user_qr_fl:
                if (SPUtils.contains(this, "userShopURL")) {
                    startActivity(new Intent(PersonalActivity.this, MyQRCodeActivity.class));
                } else {
                    Toast.makeText(PersonalActivity.this, R.string.you_on_stop, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    //Choose Date 选择日期
    public void chooseDateDialog() {
        final ChooseDateUtil dateUtil = new ChooseDateUtil();
        String[] oldDateArray = "1990-07-09".split("-");
        dateUtil.createDialog(this, oldDateArray, new ChooseDateInterface() {
            @Override
            public void sure(int[] newDateArray) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy");
                int nowYear = Integer.parseInt(format.format(new Date()));
                userAge.setText("" + (nowYear - newDateArray[0]));
                operate.updata(accountName, PersonInfoSQLiteOpenHelper.USER_AGE, "" + (nowYear - newDateArray[0]));

            }
        });
    }

    private void showSelectPop() {
        mMenuPop.showAtLocation(userAreaFL, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private final static String TAG = PersonalActivity.class.getSimpleName();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "requestCode:" + requestCode + "  resultCode:" + resultCode + " data :" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DEFAULT_ADDRESS:
                    Log.i(TAG, "requestCode:");
                    if (data != null) {
                        String defAddress = data.getStringExtra("defAddress");
                        userArea.setText(defAddress);
                        operate.updata(accountName, PersonInfoSQLiteOpenHelper.USER_DEFALUT_ADDRESS, defAddress);
                    }
                    break;
                case NICKNAME_CODE:
                    Log.i(TAG, "requestCode:");
                    if (data != null) {
                        String result = data.getStringExtra("result");
                        userNick.setText(result);
                        GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.userName = result;//修改内存中 用户信息的昵称
                        //保存数据库
                        operate.updata(accountName, PersonInfoSQLiteOpenHelper.USER_SPACE_NAME, result);
                        setTitle(result);
                        //发广播 修改登陆头像
                        Intent modifyNick = new Intent(Constants.MODIFY_NICK);
                        modifyNick.putExtra("newNick", result);
                        sendBroadcast(modifyNick);
                    }

                    break;
            }
        }
        ImageUtil.getInstance(getContext()).setHandleResultListener(PersonalActivity.this, requestCode, resultCode, data);

    }

    @Override
    public Activity getContext() {
        return PersonalActivity.this;
    }

    @Override
    public void onPhotoCropped(Bitmap photo, int requstCode) {
        switch (requstCode) {
            case ImageUtil.RQ_CAMERA:
            case ImageUtil.RQ_GALLERY:
            case ImageUtil.REQUEST_CAMERA:
                newPhoto = photo;
                // TODO: 2017/9/27  头像上传服务器
                if (NetWorkUtil.isNetworkAvailable(this)) {
                    File cachedCropFile = ImageUtil.getInstance(getContext()).getCachedCropFile();
                    control.setUserHead(PersonalActivity.this, cachedCropFile);
                } else {
                    ToastUtil.showToast(this, getString(R.string.timeout));
                }

                break;
        }
    }

    @Override
    public void onCropCancel() {

    }

    private Bitmap newPhoto = null;

    @Override
    public void onCropFailed(String message) {
        ToastUtil.showToast(this, message);
    }


    @Override
    public void requestModifyHeadResult(String result) {
        // {"code":0,"msg":"\u6210\u529f","size":3053,"filename":"http:\/\/192.168.0.222\/images\/201710\/39633-0000.jpg"}
        try {
            JSONObject root = new JSONObject(result);
            int code = root.getInt("code");
            String msg = root.getString("msg");
            String headurl = root.getString("filename");
            if (code == 0) {
                ToastUtil.showToast(this, msg);
                userHead.setImageBitmap(newPhoto);
                GBAccountMgr.getInstance().mAccountInfo.winguoGeneral.icoUrl = headurl;
                SPUtils.put(PersonalActivity.this, "userImagURL", headurl);//  修改/保存用户头像
                setTitleHead(newPhoto); //修改基类中头像
                //发广播 修改登陆头像
                Intent modifyPhoto = new Intent(Constants.MODIFY_PHOTO);
                modifyPhoto.putExtra("newPhoto", newPhoto);
                sendBroadcast(modifyPhoto);
            } else {
                ToastUtil.showToast(this, "上传失败，请稍后再试！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestModifyHeadErrorCode(int errorcode) {
        ToastUtil.showToast(this, GBAccountError.getErrorMsg(this, errorcode));
    }

}
