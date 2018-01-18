package com.guobi.gfc.VoiceFun.utils;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.guobi.gfc.VoiceFun.scene.SendSmsActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public final class MsgUtils {
    public static interface OnSendMsgListener {
        public void sendOver(String msg);
    }

    /**
     * 给在通讯录中的人发短信 自动获取该人的第一个可发送短信的号码，并发送出去
     *
     * @param person  通讯录中的名字
     * @param content 要发送的内容
     * @return 发送结果描述字符串
     */
    public static final void sendMsg(final String person, final String content,
                                     final OnSendMsgListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟发送，休眠2秒，正式代码去掉
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (listener != null)
                    listener.sendOver("发送成功");
            }
        }).start();
        // return "发送成功";
    }

    public static final void sendMsgEx(final Context context, final String contactID,
                                       final String content, final OnSendMsgListener listener) {
        if (contactID == null || contactID.isEmpty()) {
            if (listener != null) {
                listener.sendOver("失败");
            }
            return;
        }

        LinkedList<String> list = new LinkedList<String>();
        listAllPhoneNumbers(context, contactID, list, 1);

        if (list.isEmpty()) {
            if (listener != null) {
                listener.sendOver("失败");
            }
            return;
        }

        final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                sentIntent, 0);

//		if (listener != null) { //发送成功与否不验证了，如果有其他软件拦截你，你永远都收不到消息，因为根本就没去发短信；
//			// register the Broadcast Receivers
//			BroadcastReceiver receiver = new BroadcastReceiver() {
//				@Override
//				public void onReceive(Context _context, Intent _intent) {
//
//					switch (getResultCode()) {
//					case Activity.RESULT_OK: // 真正的发送成功在这里
//						Log.e(SENT_SMS_ACTION, "发送成功");
//						listener.sendOver("发送成功");
//						// Toast.makeText(context,
//						// "短信发送成功", Toast.LENGTH_SHORT)
//						// .show();
//						break;
//					// case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//					// break;
//					// case SmsManager.RESULT_ERROR_RADIO_OFF:
//					// break;
//					// case SmsManager.RESULT_ERROR_NULL_PDU:
//					// break;
//					default:
//						Log.e(SENT_SMS_ACTION, "发送失败");
//						listener.sendOver("发送失败");
//						break;
//					}
//
//					context.unregisterReceiver(this);
//				}
//			};
//
//			context.registerReceiver(receiver,
//					new IntentFilter(SENT_SMS_ACTION));
//		}

        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(content);

        final String num = list.get(0);
        for (String text : divideContents) {
            smsManager.sendTextMessage(num, null, text, sentPI, null); // deliverPI
        }

        if (listener != null) // 不是 真正的发送成功，上面注释的才是，灵犀那边也算这里为发送成功
            listener.sendOver("发送成功");
    }

    /**
     * 这个完全够了，上面的有点多余
     *
     * @param context
     * @param number
     * @param content
     * @param listener
     */
    public static final void sendMsgExByNumber(final Context context, final String number,
                                               final String content, final OnSendMsgListener listener) {
        if (number == null || number.isEmpty()) {
            if (listener != null) {
                listener.sendOver("失败");
            }
            return;
        }

        final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                sentIntent, 0);

        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);

        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(content);

        for (String text : divideContents) {
            smsManager.sendTextMessage(number, null, text, sentPI, deliverPI); // deliverPI
        }

        if (listener != null) // 不是 真正的发送成功，上面注释的才是，灵犀那边也算这里为发送成功
            listener.sendOver("发送成功");
    }


    public static int listAllPhoneNumbers(Context context,
                                          String curContanctID, List<String> numbers, int want) { //LinkedList<String> descriprions,
//		if (descriprions != null) {
//			descriprions.clear();
//		}
        if (numbers != null) {
            numbers.clear();
        }

        Cursor cursor = null;
        try {
            final String sel = new String("(" + ContactsContract.Data.MIMETYPE
                    + "=" + "'"
                    + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    + "'" + " AND " + ContactsContract.Data.CONTACT_ID + "=" + "'"
                    + curContanctID + "'" + ")");
            ContentResolver cr = context.getContentResolver();

            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            cursor = cr.query(ContactsContract.Data.CONTENT_URI, projection,
                    sel, null, ContactsContract.CommonDataKinds.Phone.TYPE);

            int count = 0;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        final String phoneNum = cursor.getString(0);
                        if (numbers != null && phoneNum != null) {
                            numbers.add(phoneNum);
                            count++;
                        }
                        if (want >= 0 && count >= want) {
                            break;
                        }
                    } while (cursor.moveToNext());
                }

            }
            return count;
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
        return 0;
    }


    /**
     *
     */
    public static void listAllPhoneNumbers(Context context, Uri uri, List<SendSmsActivity.PhoneNum> numbers) {

        if (numbers == null) {
            return;
        }
        Cursor cursor = null;

        try {

            ContentResolver cr = context.getContentResolver();
            cursor = cr.query(uri, null, null, null, null);

            if (cursor == null)
                return;

            if (cursor.moveToFirst()) {
                do {
                    int nameFieldColumnIndex = cursor
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String name = cursor.getString(nameFieldColumnIndex);

                    //	            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactId = cursor.getString(idColumn);

                    ArrayList<String> list = new ArrayList<String>();
                    listAllPhoneNumbers(context, contactId, list, -1);

                    //				final String phoneNum = cursor.getString(0);
                    //				final String name = cursor.getString(0);

                    if (list.isEmpty())
                        continue;

                    for (String phoneNum : list) {
                        if (phoneNum == null || phoneNum.isEmpty())
                            continue;

                        //					Contact contact = new Contact();
                        SendSmsActivity.PhoneNum phoneNum2 = new SendSmsActivity.PhoneNum();
                        if (name == null || name.isEmpty())
                            phoneNum2.ownership = phoneNum;
                        else
                            phoneNum2.ownership = name;

                        phoneNum2.num = phoneNum;

                        String addr = NumAttrQuery.query(context, phoneNum);
                        phoneNum2.addr = addr;
                        //contact.addNumber(phoneNum);
                        numbers.add(phoneNum2);
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Exception e) {

            }
        }
    }

    private MsgUtils() {
    }


}
