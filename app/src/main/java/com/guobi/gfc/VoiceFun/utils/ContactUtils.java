package com.guobi.gfc.VoiceFun.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.guobi.gfc.VoiceFun.config.VoiceFunEngineConfig;
import com.guobi.gfc.VoiceFun.jcseg.ASegment;
import com.guobi.gfc.VoiceFun.jcseg.ComplexSeg;
import com.guobi.gfc.VoiceFun.jcseg.Dictionary;
import com.guobi.gfc.VoiceFun.jcseg.ResultWord;
import com.guobi.gfc.VoiceFun.jcseg.core.Config;
import com.guobi.gfc.VoiceFun.jcseg.core.ILexicon;
import com.guobi.gfc.VoiceFun.phrase.SMSPhraseAnalyzer;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.winguo.utils.Constants;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactUtils {

    public static final int ANY = 0;
    public static final int LIKE = 1;
    public static final int FIRST = 2;
    public static final int FIRST_GROUP = 3;

    private static ASegment mSegment;
    private static Dictionary mDictionary;
    private static ComponentName mContactSyncService;

    public static final int getSuggestContactsBySpeech(Context context, String voice, List<String> result, int flag, float score) throws Exception {

        createSegment(context);
        startContactSyncService(context);
        GBLogUtils.DEBUG_DISPLAY("--getSuggestContactsBySpeech startContactSyncService", "+++++++++++++++++++++++");
        mDictionary.setScore(score);
        GBLogUtils.DEBUG_DISPLAY("--getSuggestContactsBySpeech mDictionary.setScore", "+++++++++++++++++++++++");
        mSegment.reset(new StringReader(voice));
        GBLogUtils.DEBUG_DISPLAY("--getSuggestContactsBySpeech mSegment.reset(new StringReader(voice));", "+++++++++++++++++++++++");

        result.clear();
        ResultWord word = null;
        int count = 0;
        int end = -1;
//			GBLogUtils.DEBUG_DISPLAY("--getSuggestContactsBySpeech ResultWord word = null;", "+++++++++++++++++++++++"+);
        while ((word = mSegment.next()) != null) {
            GBLogUtils.DEBUG_DISPLAY("--getSuggestContactsBySpeech while ((word = mSegment.next()) != null) ", "+++++++++++++++++++++++" + word.getValue());
            count++;
            if (word.getType() == ILexicon.VOICE_XING_MING) {
                String name = word.getValue();
                if (flag == FIRST_GROUP && result.size() >= 1) {
                    String name0 = voice.substring(word.getPosition(), word.getPosition() + word.getLength0());
                    if (PinyinUtils.xingming(name0, name) >= SMSPhraseAnalyzer.W_SCORE_2) {
                        result.add(word.getValue());
                    } else {
                        return end;
                    }
                } else {
                    result.add(word.getValue());
                }
                end = word.getPosition() + word.getLength0();

            } else if (word.getType() == ILexicon.CJK_WORDS) {

            } else {
                if (flag == FIRST_GROUP) {
                    if (count >= 2) {
                        return end;
                    }
                }
            }
            if (flag == FIRST) {
                if (result.size() >= 1 || count >= 2) {
                    return end;
                }
            }
        }

        if (flag == LIKE) {
            if (count - result.size() <= result.size() - 1) {
                return end;
            } else {
                result.clear();
                return -1;
            }
        } else {  // flag == ANY
            return end;
        }


    }

    private static void createSegment(Context context) throws Exception {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (mSegment == null) {
                Dictionary dic = mDictionary = new Dictionary();
                mSegment = new ComplexSeg(null, dic, false);

                Cursor cursor = null;

                try {
                    ContentResolver cr = context.getContentResolver();
                    Uri URI = ContactsContract.Contacts.CONTENT_URI;
                    String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
                    cursor = cr.query(URI, projection, null, null, null);

                    if (cursor == null) return;

                    while (cursor.moveToNext()) {
                        String name = cursor.getString(0);
                        if (!TextUtils.isEmpty(name) && name.length() <= Config.MAX_LENGTH) {
                            dic.add(0, name, ILexicon.VOICE_XING_MING);
                            GBLogUtils.DEBUG_DISPLAY("-- createSegment contact.db", name);
                        }
                    }

                    dic.add(0, "和", 10000, ILexicon.CJK_WORDS);
                    dic.add(0, "跟", 10000, ILexicon.CJK_WORDS);
                    dic.add(0, "与", 10000, ILexicon.CJK_WORDS);
                    dic.optimize();

                } catch (Exception e) {
                    e.printStackTrace();
                    GBLogUtils.DEBUG_DISPLAY("-- createSegment e", e.getMessage());
                } finally {
                    try {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                            GBLogUtils.DEBUG_DISPLAY("-- createSegment finally", "");
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } else {
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.REQUEST_CODE_PERMISSIONS);
        }

    }

    private static void startContactSyncService(Context context) {
        if (mContactSyncService == null) {
            Intent intent = new Intent(context, ContactSyncService.class);
            mContactSyncService = context.startService(intent);
        }
    }

    public static List<Contact> findContact(Context context, String contactName) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            Cursor c = null;
            List<Contact> result = new ArrayList<>();
            try {
                Uri uri = ContactsContract.Contacts.CONTENT_URI;
                String[] projection1 = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
                String where = ContactsContract.Contacts.DISPLAY_NAME + "='" + contactName + "'";
                ContentResolver cr = context.getContentResolver();
                c = cr.query(uri, projection1, where, null, null);
                if (c == null) return null;

                while (c.moveToNext()) {
                    String contactId = c.getString(0);
                    String displayName = c.getString(1);

                    Contact contact = new Contact();
                    contact.id = contactId;
                    contact.name = displayName;

                    String[] projection = new String[]{Phone.NUMBER};
                    Cursor phone = cr.query(Phone.CONTENT_URI, projection,
                            Phone.CONTACT_ID + "=" + contactId, null, null);
                    for (int i = 0, n = phone.getCount(); i < n; i++) {
                        phone.moveToNext();
                        contact.addNumber(phone.getString(0));
                    }

                    result.add(contact);

                    phone.close();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (c != null && !c.isClosed()) {
                        c.close();
                        c = null;
                    }
                } catch (Exception e) {

                }
            }
        } else {
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.REQUEST_CODE_PERMISSIONS);
        }
        return null;
    }

    public static void updateContactForSpeech(Context context) {
        SpeechRecognizer iat = SpeechRecognizer.getRecognizer();
        if (iat == null) {
            SpeechRecognizer.createRecognizer(context, new SpeechInitListener(context));
        } else {
            new SpeechInitListener(context).onInit(ErrorCode.SUCCESS);
        }
    }

    /**
     * 通话记录里面的信息
     *
     * @param context
     * @param total
     * @return
     */
    public static ArrayList<Contact> getCallsInfo(Context context, final int total) {
        ArrayList<Contact> list = new ArrayList<Contact>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;

        HashMap<String, Boolean> map = new HashMap<String, Boolean>();

        try {

            cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");  // 降序排列
            int count = 0;

            if (cursor != null && cursor.getCount() > 0) {
                boolean flag = cursor.moveToFirst(); // 降序先取第一条

                while (flag) {
                    final String name = cursor.getString(cursor.getColumnIndex("name"));
                    final String phoneNum = cursor.getString(cursor.getColumnIndex("number"));

                    if (phoneNum == null || phoneNum.length() == 0 || name == null || name.length() == 0) {
                        flag = cursor.moveToNext();
                        continue;
                    }

                    if (map.containsKey(name)) { // 过滤重复名字
                        flag = cursor.moveToNext();
                        continue;
                    }

                    Contact object = new Contact();
                    object.name = name;

                    map.put(name, true);
                    list.add(object);
                    count++;

                    if (count >= total)
                        break;

                    flag = cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            map.clear();
            try {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Exception e) {
                //ignore this
            }
        }
        return list;
    }

    private static class SpeechInitListener implements InitListener {

        private Context context;
        private Handler handler;

        public SpeechInitListener(Context context) {
            this.context = context;
            this.handler = new Handler();
        }

        @Override
        public void onInit(int code) {
            System.out.println("xunfei: init for contact");
            if (code == ErrorCode.SUCCESS) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final StringBuffer names = new StringBuffer();

                        Cursor cursor = null;
                        try {
                            final ContentResolver cr = context.getContentResolver();
                            final Uri URI = ContactsContract.Contacts.CONTENT_URI;
                            final String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
                            cursor = cr.query(URI, projection, null, null, null);

                            if (cursor == null) return;

                            while (cursor.moveToNext()) {
                                String name = cursor.getString(0);
                                if (!TextUtils.isEmpty(name) && name.length() <= Config.MAX_LENGTH) {
                                    names.append(name);
                                    names.append('\n');
                                }
                            }

                            mSpeechContactQueryListener.onContactQueryFinish(names.toString(), true);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (cursor != null && !cursor.isClosed()) {
                                    cursor.close();
                                    cursor = null;
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                });
            } else {
                System.err.println("xunfei: init faild: " + code);
            }
        }
    }

    private static ContactListener mSpeechContactQueryListener = new ContactListener() {
        @Override
        public void onContactQueryFinish(String contactInfos, boolean changeFlag) {
            SpeechRecognizer iat = SpeechRecognizer.getRecognizer();
            iat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            iat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            int ret = iat.updateLexicon("contact", contactInfos, mSpeechUpdateLexiconListener);
            if (ret != ErrorCode.SUCCESS) {
                System.err.println("xunfei: update contact faild: " + ret);
            }
        }

    };

    private static LexiconListener mSpeechUpdateLexiconListener = new LexiconListener() {
        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                System.err.println(error.toString());
            } else {
                System.err.println("xunfei: update contact OK.");
            }
        }
    };


    /**
     * 查询联系人
     */
    public static class ContactSyncService extends Service {

        private final static String TAG = "news_data_item: ContactSyncService";

        // 延时处理同步联系人，若在延时期间，通话记录数据库未改变，即判断为联系人被改变了
        private final static int ELAPSE_TIME = 5000;

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        updataContact();
                        break;
                }
            }
        };

        public ContentObserver mContactObserver = new ContentObserver(new Handler()) {

            @Override
            public void onChange(boolean selfChange) {
                // 当系统联系人数据库发生更改时触发此操作

                Log.i(TAG, "Contact changed, wait for 10s..");

                // 去掉多余的或者重复的同步
                mHandler.removeMessages(0);

                // 延时ELAPSE_TIME(10秒）发送同步信号“0”
                mHandler.sendEmptyMessageDelayed(0, ELAPSE_TIME);
            }

        };

        // 当通话记录数据库发生更改时触发此操作
        private ContentObserver mCallLogObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                // 当通话记录数据库发生更改时触发此操作
                Log.i(TAG, "CallLog");
                // 如果延时期间发现通话记录数据库也改变了，即判断为联系人未被改变，取消前面的同步
                mHandler.removeMessages(0);
            }

        };

        // 在此处处理联系人被修改后应该执行的操作

        public void updataContact() {
            try {
                mSegment.destroy();
                mSegment = null;

                createSegment(this.getApplicationContext());
                if (VoiceFunEngineConfig.REG_ENGINE_MODE == VoiceFunEngineConfig.REG_ENGINE_IFLY) {
                    updateContactForSpeech(this.getApplicationContext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Log.i(TAG, "OnCreate");
            // 注册监听通话记录数据库
            getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, mCallLogObserver);
            // 注册监听联系人数据库
            getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mContactObserver);

        }

        @Override
        public void onDestroy() {
            getContentResolver().unregisterContentObserver(mCallLogObserver);
            getContentResolver().unregisterContentObserver(mContactObserver);
            super.onDestroy();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return flags;
        }

    }


    public static class Contact implements Parcelable {
        public String id;
        public String name;
        public List<String> numbers;

        public Contact() {
        }

        public Contact(String number) {
            addNumber(number);
        }

        public Contact(String name, String number) {
            this.name = name;
            addNumber(number);
        }

        public void addNumber(String number) {
            if (numbers == null) {
                numbers = new ArrayList<String>();
            }
            numbers.add(number);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(name);
            sb.append(": ");
            if (numbers != null) {
                for (int i = 0, n = numbers.size(); i < n; i++) {
                    String number = numbers.get(i);
                    sb.append(number);
                    if (i < n - 1) {
                        sb.append(", ");
                    }
                }
            }

            return sb.toString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeList(numbers);
        }

        public static final Creator<Contact> CREATOR = new Creator<Contact>() {
            @Override
            public Contact[] newArray(int size) {
                return new Contact[size];
            }

            @Override
            public Contact createFromParcel(Parcel in) {
                return new Contact(in);
            }
        };

        private Contact(Parcel in) {
            id = in.readString();
            name = in.readString();
            // 话说一定要这样
            numbers = in.readArrayList(String.class.getClassLoader());
        }
    }

}
