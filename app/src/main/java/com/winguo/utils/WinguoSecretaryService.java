package com.winguo.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.guobi.winguoapp.voice.VoiceFunActivity;
import com.winguo.view.TouchImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/30.
 */
@Deprecated
public class WinguoSecretaryService extends Service {

    private WindowManager mWindowManager;
    private LayoutInflater mLayoutInflater;
    // private View mFloatView;
    private TouchImageView mFloatView;
    private Intent ser ;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //初始化WindowManager对象和LayoutInflater对象
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(this);
    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        this.ser = intent;
        mFloatView = new TouchImageView(getApplicationContext(),mWindowManager);
        if (ser != null) {
            Bitmap bitmap = ser.getParcelableExtra("bitmap");
            if (bitmap != null) {
                setImageBitmap(bitmap);
            }
        }

        createView();
    }
    private void createView() {
        // TODO Auto-generated method stub
        //加载布局文件
        //mFloatView = mLayoutInflater.inflate(R.layout.main, null);

        mFloatView.setOnDoubleClickListener(new TouchImageView.OnDoubleClick() {
            @Override
            public void onDoubleClick(View view) {
                Toast.makeText(getApplicationContext(),"onDoubleClick", Toast.LENGTH_LONG).show();

            }
        });

        mFloatView.setOnClickListener(new TouchImageView.OnClick() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"onClick",Toast.LENGTH_LONG).show();
            }
        });
        mFloatView.setOnLongListener(new TouchImageView.OnLongClick() {
            @Override
            public void onLongClick(View view) {
              //  Toast.makeText(getApplicationContext(),"onLongClick",Toast.LENGTH_LONG).show();
                Intent ma = new Intent(getBaseContext(),VoiceFunActivity.class);
                ma.putExtra("analysis", "true");
                ma.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(ma);
            }
        });

    }



    public  boolean setImageBitmap(Bitmap bitmap){
        if (bitmap == null) {
            return false;
        } else {
            mFloatView.setImageBitmap(bitmap);
            return true;
        }
    }

    private static String TAG = WinguoSecretaryService.class.getSimpleName();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand==============  thread: "+Thread.currentThread().getName());
        return super.onStartCommand(intent, flags, startId);
    }

    /*由于直接startService(),因此该方法没用*/
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i(TAG,"onBind==============  thread: "+Thread.currentThread().getName()+ "intent   "+intent.getAction());
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeViewImmediate(mFloatView);
    }
}
