package com.example.localsearch;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.searchutils.StringMatcher;
import com.guobi.common.wordpy.Wordpy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信内容检索关键词
 */
public class LocalSmsFinder {

    private String rawKey;
    private String type;
    private Context context;
    private static List<WGSmsResult> smsList;

    public LocalSmsFinder(Context context, String type) {
        this.type = type;
        this.context = context;
    }

    public void setRawKey(String rawKey) {
        this.rawKey = rawKey;
    }

    public void Init(Context mContext) {

        if (smsList == null) {
            smsList = getSms(mContext);
        }
    }


    public List<WGSmsResult> smsResults() {

        List<WGSmsResult> smsResults = new ArrayList<WGSmsResult>();
        if (smsList == null) {
            smsList = getSms(context);
        }
        int searchType = StringMatcher.SEARCH_TYPE;
        String[] keyword = rawKey.split("");

        for (String judge : keyword) {
            if (Wordpy.IsHanZi(judge)) {
                searchType = StringMatcher.HANZI_TYPE;
                break;
            }
        }
        if (type != null && type.equals("voice")) {
            searchType = com.example.searchutils.StringMatcher.ALL_TYPE;
        }
        for (WGSmsResult sms : smsList) {

            String smsAddress = sms.getAddress();
            String smsBody = sms.getBody();
            String smsPerson = sms.getPerson();
            String smsDate = sms.getDate();
            StringMatcher matcher = new StringMatcher(rawKey, smsBody, searchType);
            WGResultText resultText = matcher.getResult();
            if (resultText != null) {
                WGSmsResult result = new WGSmsResult(smsAddress,smsPerson,smsDate);
                result.setBodyResult(resultText);
                result.setLength(smsBody.length() + "");
                smsResults.add(result);
                continue;
            }
        }

        if(smsResults.isEmpty()) {
            return null;
        }
        return smsResults;
    }

    public static List<WGSmsResult> getSms(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            List<WGSmsResult> smsResults = new ArrayList<WGSmsResult>();
            ContentResolver resolver = context.getContentResolver();

            Cursor smsCursor = null;
            try {
                smsCursor = resolver.query(Uri.parse("content://sms/"), new String[]{"_id", "address", "body", "person", "date", "type"}, null, null, null);

                if (smsCursor != null) {
                    do {
                        if (smsCursor.moveToFirst()){
                            String smsAddress = smsCursor.getString(1);
                            String smsBody = smsCursor.getString(2);
                            int smsPerson = smsCursor.getInt(3);
                            int smsDate = smsCursor.getInt(4);

                            WGSmsResult result = new WGSmsResult(smsAddress, smsBody, "" + smsPerson, "" + smsDate);
                            result.setLength(smsBody.length() + "");
                            smsResults.add(result);
                        }



                    } while (smsCursor.moveToNext());
                }

                if (smsResults.size() > 0) {
                    return smsResults;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("contact", "------" + e.getMessage());
            } finally {
                try {
                    if (smsCursor != null && !smsCursor.isClosed()) {
                        smsCursor.close();
                        smsCursor = null;
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        } else {
            ((Activity)context).requestPermissions(new String[]{Manifest.permission.READ_SMS},45);

        }
        return null;

    }

    public static final class WGSmsResult {

        private final static String SMS_URI_ALL = "content://sms/"; //所有短信
        private final static String SMS_URI_INBOX = "content://sms/inbox";//收件箱
        private final static String SMS_URI_SEND = "content://sms/sent"; //已发送
        private final static String SMS_URI_DRAFT = "content://sms/draft";//草稿箱

        private String address;//电话号码
        private WGResultText bodyResult;//短信内容
        private String body;//短信内容
        private String person;//联系人id
        private String date; //发件日期
        private String length; //短信内容长度

        public WGSmsResult(String address,String person,String date) {
            this.address = address;
        }

        public WGSmsResult(String address, String body, String person, String date) {
            this.address = address;
            this.body = body;
            this.person = person;
            this.date = date;
        }

        public void setBodyResult(WGResultText bodyResult) {
            this.bodyResult = bodyResult;
        }

        public String getAddress() {
            return address;
        }

        public String getBody() {
            return body;
        }

        public WGResultText getBodyResult() {
            return bodyResult;
        }

        public String getPerson() {
            return person;
        }

        public String getDate() {
            return date;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }
    }

}
