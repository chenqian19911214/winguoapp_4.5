package com.guobi.gfc.VoiceFun.scene;


import java.util.ArrayList;
import java.util.List;


import com.guobi.gfc.VoiceFun.utils.NumAttrQuery;
import com.umeng.analytics.MobclickAgent;
import com.guobi.gfc.VoiceFun.scene.SendSmsActivity.PhoneNum;
import com.guobi.gfc.VoiceFun.utils.ContactUtils.Contact;
import com.winguo.R;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CallActivity extends Activity implements OnClickListener {

    private String mRawContent;
    private boolean mIsContact;
    private ListView mListView;
    private TextView mPropmtView;

    private Button mSendButton;

    private ArrayList<PhoneNum> mPhoneList = new ArrayList<>();
    private String mMsg;

    private boolean mIsAutoCall = false;
    private static final int REQUEST_PREMISSION_CALL = 0X123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicefun_call_layout);
        if (Intents.checkPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            initContent();
            initViews();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},Constants.REQUEST_CODE_PERMISSIONS_CALL_PHONE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Constants.REQUEST_CODE_PERMISSIONS_CALL_PHONE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
                initContent();
                initViews();
            } else {
                Intents.noPermissionStatus(this, "打电话权限未开");
                autoCallCancel();
            }
        }
        if (Constants.REQUEST_CODE_PERMISSIONS_WRITE_SD == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Intents.noPermissionStatus(this, "存储权限未开");
                autoCallCancel();
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

    private void initViews() {
        mListView = (ListView) findViewById(R.id.voicefun_send_msg_listview);
        mPropmtView = (TextView) findViewById(R.id.voicefun_call_prompt);

        if (mMsg == null) {
            CallAdapter mAdapter = new CallAdapter();
            mListView.setAdapter(mAdapter);
            if (mPhoneList.size() == 1) {
                // TODO
                View v = findViewById(R.id.voicefun_call_go_dial);
                v.setVisibility(View.VISIBLE);

                mSendButton = (Button) v;
                v.setOnClickListener(this);

                final String txt = getString(R.string.voicefun_prompt_calling) + mPhoneList.get(0).ownership;
                mPropmtView.setText(txt);
            }
        } else {
            mPropmtView.setText(mMsg);
            mListView.setVisibility(View.GONE);

            View v = findViewById(R.id.voicefun_call_go_dial);
            v.setVisibility(View.VISIBLE);
            v.setOnClickListener(this);
        }

        TextView rawInput = (TextView) findViewById(R.id.voicefun_rawinput_content);
        rawInput.setText(mRawContent);

        findViewById(R.id.voicefun_layout_title_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voicefun_layout_title_back:
                Intent intent = new Intent();
                intent.putExtra("result", "canceled");
                setResult(RESULT_CANCELED, intent);

                finish();
                break;

            case R.id.voicefun_call_go_dial:
                if (mMsg == null) {
                    if (mIsAutoCall) { // cancel
                        autoCallCancel();
                    } else {
                        PhoneNum num = mPhoneList.get(0);
                        dialPhone(num);
                    }
                    return;
                }

                try {
                    Intent intent2 = new Intent(Intent.ACTION_DIAL);
                    startActivity(intent2);

                    finish();
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            default:
                break;
        }
    }

    private void autoCallCancel() {
        if (!mIsAutoCall)
            return;

        mIsAutoCall = false;
        mHandler.removeMessages(CALL_BY_ZERO);
        mSendButton.setText(R.string.voicefun_text_call);
    }

    @Override
    protected void onStop() {
        super.onStop();
        autoCallCancel();
    }

    private static final int CALL_BY_ZERO = 0x214;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;

            switch (what) {
                case CALL_BY_ZERO:
                    int second = msg.arg1;
                    if (!mIsAutoCall)
                        return;

                    if (second <= 0) {
                        PhoneNum num = mPhoneList.get(0);
                        mIsAutoCall = false;
                        dialPhone(num);
                    } else {
                        second--;
                        Message message = new Message();
                        message.what = CALL_BY_ZERO;
                        message.arg1 = second;

                        mHandler.sendMessageDelayed(message, 1000);
                        goOneMinute(second);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mPhoneList != null && mPhoneList.size() == 1) {
            mIsAutoCall = true;

            final int min = 3;

            goOneMinute(min);
            Message message = new Message();
            message.what = CALL_BY_ZERO;
            message.arg1 = min;

            mHandler.sendMessageDelayed(message, 1800);
        }
    }

    private void goOneMinute(final int min) {
        String msg = getString(R.string.voicefun_text_call_backwards_cancel, min);
        Spannable spannable = new SpannableString(msg);

        float size = 15 * getResources().getDisplayMetrics().density + 0.5f;
        spannable.setSpan(new AbsoluteSizeSpan((int) size), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSendButton.setText(spannable);
    }

    private void initContent() {
        final Intent intent = getIntent();

        mRawContent = getString(R.string.voicefun_text_original) + intent.getStringExtra("rawinput");
        mIsContact = intent.getBooleanExtra("isContact", false);

        Contact contact = intent.getParcelableExtra("person_names");
        if (contact == null) {
            // finish();
            mMsg = intent.getStringExtra("msg");
            return;
        }

        // for (Contact contact : contactList) {
        String name = contact.name;

        List<String> nums = contact.numbers;
        if (nums == null || nums.isEmpty())
            return;

        for (String num : nums) {
            if (num == null || num.isEmpty())
                continue;

            PhoneNum phoneNum = new PhoneNum();
            phoneNum.ownership = name;
            phoneNum.num = num;

            String addr = NumAttrQuery.query(this, num);
            phoneNum.addr = addr;

            mPhoneList.add(phoneNum);
        }
        // }
    }

    private void dialPhone(PhoneNum num) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.CALL");
            intent.setData(Uri.parse("tel:" + num.num));
            startActivity(intent);
            finish();
        } catch (Exception e) {
        }
    }

    class CallAdapter extends BaseAdapter {

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
                View v = LayoutInflater.from(CallActivity.this).inflate(R.layout.voicefun_send_smg_item, null);
                convertView = v;

                holder = new Holder();
                v.setTag(holder);

                holder.name = (TextView) v.findViewById(R.id.voicefun_msg_item_name);
                holder.num = (TextView) v.findViewById(R.id.voicefun_msg_item_num);

                holder.del = (Button) v.findViewById(R.id.voicefun_msg_item_del);
                holder.del.setText(R.string.voicefun_info_dial);
                if (getCount() <= 1)
                    holder.del.setVisibility(View.GONE);

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

            holder.del.setTag(position);
            holder.del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    PhoneNum num = mPhoneList.remove(pos);

                    dialPhone(num);
                }
            });
            // holder.del.setVisibility(View.VISIBLE);
            // holder.status.setVisibility(View.GONE);

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

}
