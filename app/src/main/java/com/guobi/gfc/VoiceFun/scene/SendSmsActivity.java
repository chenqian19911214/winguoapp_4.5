package com.guobi.gfc.VoiceFun.scene;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.gfc.VoiceFun.utils.ContactUtils.Contact;
import com.guobi.gfc.VoiceFun.utils.MsgUtils;
import com.guobi.gfc.VoiceFun.utils.MsgUtils.OnSendMsgListener;
import com.guobi.gfc.VoiceFun.utils.NumAttrQuery;
import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SendSmsActivity extends Activity implements OnClickListener {

    private static final int SEND_MSG_SUCCESS = 1;
    private static final int SEND_MSG_FAILED = -1;
    private static final int SEND_MSG_ING = 2;

    private static final int SEND_SMS_BY_ZERO = 8;
    private static final int SEND_NEXT_SMS = 9;
    private static final int BACK_BY_ZERO = 10;
    private static final int DURATION = 300;

    private static final int MODE_NOT_SEND = 0;
    private static final int MODE_SEND_READY = 1;
    private static final int MODE_SEND_ING = 2;
    private static final int MODE_SEND_OVER = 4;

    private static final int PICK_CONTACT_REQUEST = 2;

    private String mMsgContent;

    private HashMap<String, Byte> mNumExistMap = new HashMap<String, Byte>();
    private ArrayList<PhoneNum> mPhoneList = new ArrayList<PhoneNum>();

    private ListView mListView;
    private MsgSendAdapter mAdapter;

    private int mMsgStatus[];
    private int mSendPosition;
    private static final int SEND_SMS_PERMISSON = 0X784;

    private Button mSendButton;
    private EditText mContentEt;
    private TextView mRawInput;
    private String mRawContent;

    private boolean mIsCanceled = false;
    private int mSendMode = 0;

    // private AICloudTTSEngine mTTSEngine;

    private boolean mIsContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.voicefun_send_mms_layout);
        if (Intents.checkPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            initSmsContent();
            initViews();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},Constants.REQUEST_CODE_PERMISSIONS_SEND_SMS);
            }
        }


        // mTTSEngine = AICloudTTSEngine.getInstance();
        // mTTSEngine.init(this, null, AppKey.APPKEY, AppKey.SECRETKEY);
        // // 指定默认中文合成
        // mTTSEngine.setLanguage(AIConstant.CN_TTS);
        //
        // // 默认女声
        // mTTSEngine.setRes("syn_chnsnt_zhilingf");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Constants.REQUEST_CODE_PERMISSIONS_SEND_SMS == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
                initSmsContent();
                initViews();
            } else {
                Intents.noPermissionStatus(this, "发短信权限未开");
                cancelAutoSendSms();
            }
        }

        if (Constants.REQUEST_CODE_PERMISSIONS_WRITE_SD == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Intents.noPermissionStatus(this, "存储权限未开");
                cancelAutoSendSms();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initSmsContent() {
        final Intent intent = getIntent();
        String content = intent.getStringExtra("msg_content");
        mMsgContent = content;

        mRawContent = getString(R.string.voicefun_text_original) + intent.getStringExtra("rawinput");
        mIsContact = intent.getBooleanExtra("isContact", false);

        if (!mIsContact) {
            ArrayList<String> data = intent.getStringArrayListExtra("person_names");
            if (data == null || data.size() == 0)
                return;

            for (String string : data) {
                PhoneNum phone = new PhoneNum();
                phone.num = string;
                phone.ownership = string;

                String addr = NumAttrQuery.query(this, string);
                phone.addr = addr;

                mPhoneList.add(phone);
                mNumExistMap.put(string, (byte) 0);
            }
        } else {
            ArrayList<Contact> contactList = intent.getParcelableArrayListExtra("person_names");
            if (contactList == null || contactList.size() == 0) {
                // finish();
                return;
            }

            for (Contact contact : contactList) {
                String name = contact.name;

                List<String> nums = contact.numbers;
                if (nums == null || nums.isEmpty())
                    continue;

                for (String num : nums) {
                    if (num == null || num.isEmpty())
                        continue;

                    PhoneNum phoneNum = new PhoneNum();
                    phoneNum.ownership = name;
                    phoneNum.num = num;

                    String addr = NumAttrQuery.query(this, num);
                    phoneNum.addr = addr;

                    mPhoneList.add(phoneNum);
                    mNumExistMap.put(num, (byte) 0);
                }
            }
        }

        mMsgStatus = new int[mPhoneList.size()];
    }


    private void initViews() {
        EditText editText = (EditText) findViewById(R.id.voicefun_msg_content);
        editText.setText(mMsgContent);
        mContentEt = editText;
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 获得焦点
                    if (MODE_SEND_READY == mSendMode)
                        cancelAutoSendSms();
                } else {
                    // 失去焦点

                }

            }
        });

        mListView = (ListView) findViewById(R.id.voicefun_send_msg_listview);
        mAdapter = new MsgSendAdapter();
        mListView.setAdapter(mAdapter);

        mSendButton = (Button) findViewById(R.id.voicefun_msg_send_button);
        mSendButton.setOnClickListener(this);

        mRawInput = (TextView) findViewById(R.id.voicefun_rawinput_content);
        mRawInput.setText(mRawContent);

        findViewById(R.id.voicefun_add_contact_layout).setOnClickListener(this);
        findViewById(R.id.voicefun_layout_title_back).setOnClickListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mSendPosition = -1;

        if (mMsgContent != null && !mMsgContent.isEmpty() && mPhoneList != null && !mPhoneList.isEmpty()) {
            final int min = 5;

            goOneMinute(min);

            Message message = new Message();
            message.what = SEND_SMS_BY_ZERO;
            message.arg1 = min;

            mHandler.sendMessageDelayed(message, 1800);

            mSendMode = MODE_SEND_READY;
            // sendNextMsg(null);
        }
    }

    private void goOneMinute(final int min) {
        String msg = getString(R.string.voicefun_text_backwards_cancel, min);
        Spannable spannable = new SpannableString(msg);

        float size = 15 * getResources().getDisplayMetrics().density + 0.5f;
        spannable.setSpan(new AbsoluteSizeSpan((int) size), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSendButton.setText(spannable);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case SEND_SMS_BY_ZERO: {
                    int min = msg.arg1;
                    if (min <= 0) {
                        // mSendPosition = -1;
                        sendMsgChange();

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSendMode = MODE_SEND_ING;
                                sendNextMsg(null);
                            }
                        }, DURATION);
                    } else {
                        min -= 1;
                        Message message = obtainMessage(SEND_SMS_BY_ZERO);
                        message.arg1 = min;

                        mHandler.sendMessageDelayed(message, 1000);

                        goOneMinute(min);
                    }
                }
                break;
                case SEND_NEXT_SMS:
                    if (!mIsCanceled)
                        sendMsgChange();

                    if (msg.obj == null)
                        sendNextMsg(null);
                    else
                        sendNextMsg((String) msg.obj);
                    break;
                case BACK_BY_ZERO:
                    int min = msg.arg1;
                    if (min <= 0) {
                        // mSendPosition = -1;

                        mHandler.post(mFinishRunnable);
                    } else {
                        min -= 1;
                        Message message = obtainMessage(BACK_BY_ZERO);
                        message.arg1 = min;

                        mHandler.sendMessageDelayed(message, 1000);

                        setSendOver(min);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void sendMsgChange() {
        final int count = mPhoneList.size();
        final String str = (mSendPosition + 1) + "/" + count;

        final String msg = getString(R.string.voicefun_text_sending_cancel, str);
        Spannable spannable = new SpannableString(msg);

        float size = 15 * getResources().getDisplayMetrics().density + 0.5f;
        final int end = msg.length() - 2;
        spannable.setSpan(new AbsoluteSizeSpan((int) size), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSendButton.setText(spannable);
    }

    private void setSendOver(int min) {
        String msg = getString(R.string.voicefun_text_send_over, min);

        Spannable spannable = new SpannableString(msg);

        float size = 15 * getResources().getDisplayMetrics().density + 0.5f;
        final int end = msg.length() - 2;
        spannable.setSpan(new AbsoluteSizeSpan((int) size), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSendButton.setText(spannable);
    }

    private void sendNextMsg(String msg) {
        if (mIsCanceled)
            return;

        if (mSendPosition >= 0 && mSendPosition < mMsgStatus.length)
            mMsgStatus[mSendPosition] = SEND_MSG_SUCCESS;

        if (mSendPosition >= (mPhoneList.size() - 1)) {
            adapterInvalidated();
            if (mSendPosition == mPhoneList.size() - 1) {
                mSendMode = MODE_SEND_OVER;
                Message message = new Message();
                message.what = BACK_BY_ZERO;
                message.arg1 = 3;

                // mTTSEngine.speak(getString(R.string.voicefun_prompt_send_over));
                mHandler.sendMessageDelayed(message, 1300);
            }
            return;
        }

        mSendPosition++;
        mMsgStatus[mSendPosition] = SEND_MSG_ING;

        if (mPhoneList == null || mPhoneList.isEmpty())
            return;

        String msgContent = new StringBuffer(mMsgContent).append(getString(R.string.voicefun_text_comefrom)).toString();
        MsgUtils.sendMsgExByNumber(this, mPhoneList.get(mSendPosition).num, msgContent, mSendListener);

        adapterInvalidated();
    }

    private Runnable mFinishRunnable = new Runnable() {

        @Override
        public void run() {
            // if (mTTSEngine != null)
            // mTTSEngine.stop();

            Intent intent = new Intent();
            intent.putExtra("result", "accepted");
            intent.putExtra("rawinput", mRawContent);
            setResult(RESULT_OK, intent);

            finish();
        }
    };

    private void adapterInvalidated() {
        // runOnUiThread(new Runnable() {
        // @Override
        // public void run() {
        mAdapter.notifyDataSetChanged();
        // }
        // });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voicefun_msg_send_button:
                switch (mSendMode) {
                    case MODE_NOT_SEND:
                        mIsCanceled = false;
                        if (mPhoneList.isEmpty()) {
                            String text = getString(R.string.voicefun_prompt_choose_contact);
                            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                            // mTTSEngine.speak(text);
                            return;
                        }

                        Editable editable = mContentEt.getText();

                        if (editable != null && !editable.toString().isEmpty()) {
                            // v.setEnabled(false);
                            mMsgContent = editable.toString();

                            mSendMode = MODE_SEND_ING;
                            sendNextMsg(null);

                            hideKeyboard(v);
                        } else {
                            String text = getString(R.string.voicefun_prompt_input_content);
                            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                            // mTTSEngine.speak(text);
                        }
                        break;
                    case MODE_SEND_READY:
                        cancelAutoSendSms();
                        break;
                    case MODE_SEND_ING:
                        mSendMode = MODE_NOT_SEND;
                        mIsCanceled = true;
                        // mHandler.removeMessages(what);
                        mSendButton.setText(R.string.voicefun_text_send);

                        mSendPosition = -1;
                        Arrays.fill(mMsgStatus, 0);
                        adapterInvalidated();
                        break;

                    case MODE_SEND_OVER:
                        mHandler.sendEmptyMessage(BACK_BY_ZERO);
                        mHandler.post(mFinishRunnable);
                        break;
                    default:
                        break;
                }

                break;
            case R.id.voicefun_add_contact_layout:
                try {
                    //添加联系人
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    // intent.setType("vnd.android.cursor.dir/phone");
                    startActivityForResult(intent, PICK_CONTACT_REQUEST);

                    if (MODE_SEND_READY == mSendMode)
                        cancelAutoSendSms();
                } catch (Exception e) {

                }
                break;
            case R.id.voicefun_layout_title_back:
                Intent intent = new Intent();
                intent.putExtra("result", "canceled");
                setResult(RESULT_CANCELED, intent);

                finish();
                break;
            default:
                break;
        }
    }

    private void cancelAutoSendSms() {
        mSendMode = MODE_NOT_SEND;
        mHandler.removeMessages(SEND_SMS_BY_ZERO);
        mSendButton.setText(R.string.voicefun_text_send);
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // if (mTTSEngine != null) {
        // mTTSEngine.stop();
        // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsCanceled = true;

        // if (mTTSEngine != null) {
        // mTTSEngine.destory();
        // mTTSEngine = null;
        // }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT_REQUEST:
                    Uri uri = data.getData();

                    if (uri == null)
                        return;

                    ArrayList<PhoneNum> list = new ArrayList<PhoneNum>();
                    MsgUtils.listAllPhoneNumbers(this, uri, list);

                    for (PhoneNum contact : list) { // 去重
                        String num = contact.num;

                        if (mNumExistMap.containsKey(num))
                            continue;

                        mNumExistMap.put(num, (byte) 0);
                        mPhoneList.add(contact);
                        mMsgStatus = new int[mPhoneList.size()];

                        mAdapter.notifyDataSetChanged();
                    }

                    break;

                default:
                    break;
            }
        }
    }

    private OnSendMsgListener mSendListener = new OnSendMsgListener() {

        @Override
        public void sendOver(String msg) {
            Message message = new Message();
            message.what = SEND_NEXT_SMS;
            message.obj = msg;

            mHandler.sendMessage(message);
        }
    };

    class MsgSendAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPhoneList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhoneList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                View v = LayoutInflater.from(SendSmsActivity.this).inflate(R.layout.voicefun_send_smg_item, null);
                convertView = v;

                holder = new Holder();
                v.setTag(holder);

                holder.name = (TextView) v.findViewById(R.id.voicefun_msg_item_name);
                holder.num = (TextView) v.findViewById(R.id.voicefun_msg_item_num);
                holder.del = (Button) v.findViewById(R.id.voicefun_msg_item_del);
                holder.status = (TextView) v.findViewById(R.id.voicefun_msg_item_status);
                holder.addr = (TextView) v.findViewById(R.id.voicefun_msg_item_addr);
            } else
                holder = (Holder) convertView.getTag();

            PhoneNum phoneNum = mPhoneList.get(position);

            holder.name.setText(phoneNum.ownership);
            // String number = new StringBuffer(phoneNum.addr).append("
            // ").append(phoneNum.num).toString();
            holder.num.setText(phoneNum.num);

            String addr = phoneNum.addr;
            if (!addr.isEmpty()) {
                holder.addr.setVisibility(View.VISIBLE);
                holder.addr.setText(phoneNum.addr);
            } else {
                holder.addr.setVisibility(View.GONE);
            }

            if (mSendMode > MODE_SEND_READY) {
                holder.del.setVisibility(View.GONE);
                holder.status.setVisibility(View.VISIBLE);

                if (mMsgStatus[position] == SEND_MSG_ING) {
                    holder.status.setText(R.string.voicefun_text_sending);
                } else if (mMsgStatus[position] == SEND_MSG_SUCCESS) {
                    holder.status.setText(R.string.voicefun_text_send_successed);
                } else if (mMsgStatus[position] == SEND_MSG_FAILED) {
                    holder.status.setText(R.string.voicefun_text_send_failed);
                } else {
                    holder.status.setText(R.string.voicefun_text_send_waiting);
                }
            } else {
                holder.del.setTag(position);
                holder.del.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (Integer) v.getTag();
                        PhoneNum num = mPhoneList.remove(pos);
                        mNumExistMap.remove(num.num);

                        mAdapter.notifyDataSetChanged();

                        if (MODE_SEND_READY == mSendMode)
                            cancelAutoSendSms();
                    }
                });
                holder.del.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    class Holder {
        TextView name;
        TextView num;
        TextView addr;
        Button del;
        TextView status;
    }

    public static class PhoneNum {
        public String ownership;
        public String num;
        public String addr;
    }
}
