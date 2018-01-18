package com.winguo.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.localsearch.LocalAppFinder;
import com.example.localsearch.LocalSmsFinder;
import com.example.localsearch.WGContactResult;
import com.winguo.R;
import com.winguo.bean.SearchKeyWord;
import com.winguo.interfaces.OnCommentClickListener;
import com.winguo.utils.Constants;

import java.util.List;

/**
 * 适配器工厂
 */

public class SearchAdapterFactory {

    public static CommonAdapter getDataAdapter(final Context mContext, final Activity activity, List datas, int typeID) {
        CommonAdapter adapter = null;
        switch (typeID) {

            case SearchKeyWord.TYPE_APP:

                final List<LocalAppFinder.AppResult> apps = (List<LocalAppFinder.AppResult>) datas;
                adapter = new AppAdapter(mContext, apps, R.layout.app_item_lv, new OnCommentClickListener() {
                    @Override
                    public void listViewItemClick(int position, View v) {
                        LocalAppFinder.AppResult appResult = apps.get(position);
                        String packageName = appResult.getPackageName();
                        switch (v.getId()) {
                            case R.id.app_item_ic:
                                Intent appIc = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                                mContext.startActivity(appIc);
                                break;
                            case R.id.app_item_fl:
                                Intent appName = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                                mContext.startActivity(appName);
                                break;
                        }

                    }

                });
                break;
            case SearchKeyWord.TYPE_CONTACT:
                final List<WGContactResult> contacts = (List<WGContactResult>) datas;
                adapter = new ConntactAdapter(mContext, contacts, R.layout.conntact_item_lv, new OnCommentClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void listViewItemClick(int position, View v) {
                        WGContactResult wgContactResult = contacts.get(position);
                        String contactID = wgContactResult.getContactID();
                        String content = wgContactResult.getPhoneNumberList().get(0).getContent();
                        Bitmap bitmap = wgContactResult.getPhoto();
                        switch (v.getId()) {

                            case R.id.search_contact_nameandnumber:
                                Intent it1 = new Intent(Intent.ACTION_EDIT, Uri.parse("content://com.android.contacts/contacts/" + contactID));
                                it1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(it1);
                                break;

                            case R.id.search_contact_phone_icon:
                                Intent itent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + content));

                                itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    mContext.startActivity(itent);
                                } else {
                                    //请求打电话权限
                                    activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Constants.REQUEST_CODE_PERMISSIONS);
                                }
                                break;
                            case R.id.search_contact_message_icon:
                                Intent sendIt = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+ content));
                                sendIt. setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(sendIt);
                                break;

                        }
                    }


                });
                break;
            case SearchKeyWord.TYPE_MESSAGE:
                //短信适配器
                final List<LocalSmsFinder.WGSmsResult> sms = (List<LocalSmsFinder.WGSmsResult> )datas;
                adapter = new SmsAdapter(mContext, sms, R.layout.search_sms_item);

                break;
        }

        return adapter;
    }

}
