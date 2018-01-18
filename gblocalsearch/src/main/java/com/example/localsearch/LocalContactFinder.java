package com.example.localsearch;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.searchutils.StringMatcher;
import com.guobi.common.wordpy.Wordpy;
import com.guobi.gfc.gblocalsearch.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人查找功能，用的系统uri查找功能
 *
 * @author lujibeat
 */

public final class LocalContactFinder {
    private Context context;
    private String rawKey = "";
    private String type;
    private static List<WGContactResult> contactList;

    /**
     * @param context
     * @param type    搜索执行类型，详见StringMatcher类
     */
    public LocalContactFinder(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    public void setRawKey(String rawKey) {
        this.rawKey = rawKey;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Init(Context mContext) {

        if (contactList == null) {
            contactList = getContactsPhoneList(mContext);
        }
    }

    /**
     * 联系人查找方法
     *
     * @return List<WGContactResult>
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public List<WGContactResult> contactSearch() {

        List<WGContactResult> contactResults = new ArrayList<>();
        List<WGContactResult> combineResults = new ArrayList<>();
        if (contactList == null) {
            contactList = getContactsPhoneList(context);
        }

        for (WGContactResult i : contactList) {
            Log.i("WGContactResult contact", "" + i.getContactPhone());
        }

        try {
            int searchType = StringMatcher.SEARCH_TYPE;
            String[] keyword = rawKey.split("");

            for (String judge : keyword) {
                if (Wordpy.IsHanZi(judge)) {
                    searchType = StringMatcher.HANZI_TYPE;
                    break;
                }
            }
            if (type != null && type.equals("voice")) {
                searchType = StringMatcher.ALL_TYPE;
            }

            for (WGContactResult item : contactList) {

                String contactName = item.getContact();
                String contactPhone = item.getContactPhone();
                String contactid = item.getContactID();
                Bitmap contactPhoto = item.getPhoto();

                List<WGResultText> phoneList = new ArrayList<WGResultText>();
                phoneList.add(new WGResultText(contactPhone));
                StringMatcher matcher = new StringMatcher(rawKey, contactName, searchType);
                WGResultText resultText = matcher.getResult();
                if (resultText != null) {

                    WGContactResult result = new WGContactResult(contactid);
                    result.setContactName(resultText);
                    result.setPhoneNumberList(phoneList);
                    result.setPhoto(contactPhoto);
                    contactResults.add(result);
                    continue;
                }
                StringMatcher phoneMatcher = new StringMatcher(rawKey, contactPhone, searchType);
                if (phoneMatcher.getResult() != null) {
                    WGContactResult result = new WGContactResult(contactid);
                    result.setContactName(new WGResultText(contactName));
                    List<WGResultText> singleList = new ArrayList<WGResultText>();
                    singleList.add(phoneMatcher.getResult());
                    result.setPhoneNumberList(singleList);
                    result.setPhoto(contactPhoto);
                    contactResults.add(result);
                    continue;
                }

            }
//	        long end2 = System.currentTimeMillis();
//	        Log.d("搜索结果时间", String.valueOf(start2 - end2));
//	        long start3 = System.currentTimeMillis();
            combineResults = combine(contactResults);
//	        long end3 = System.currentTimeMillis();
//	        Log.d("结果排序时间", String.valueOf(start3 - end3));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("contact", "------" + e.getMessage());
        }

        return combineResults;

		
			/*final StringBuffer sbf = new StringBuffer("content://com.android.contacts/search_suggest_query/");
            sbf.append(rawKey);
			sbf.append("?limit=50");
			final Uri uri = Uri.parse(sbf.toString());
			final String order = 
					ContactsContract.Data.DISPLAY_NAME + "," + 
					ContactsContract.Data.MIMETYPE;
			cursor = context.getContentResolver().query(
				uri,
				null, null, null,
				order
			);
			if( cursor==null || cursor.moveToFirst()==false){
				return null;
			}
			
			int commitCount=0;
			String contactID; 
			String displayName;
			String phoneNum;
			final int total = cursor.getCount();
			int resultCount=0;
									
			do{ 	
				resultCount++;
				WGContactResult result = null;
    			contactID = cursor.getString(0);
    			displayName = cursor.getString(1);
    	    	if( contactID !=null && contactID.length()>0
    	    		&& displayName !=null && displayName.length()> 0){
    	    		final WGContactResult contactResult = new WGContactResult(contactID);
    	    			
    	    		phoneNum = cursor.getString(2);
	    	    	if( phoneNum!=null ){ // 通过电话号码命中
	    	    		contactResult.setContactName(new WGResultText(displayName));
	    	    		LinkedList<WGResultText> phone = new LinkedList<WGResultText>();
	    	    		phone.add(new WGResultText(
	    	    			phoneNum,StringMatcher.indexOf(phoneNum,rawKey)));
	    	    		contactResult.setPhoneNumberList(phone);
	    	    	}else{ // 通过名称命中
	    	    		contactResult.setContactName(new WGResultText(
	    	    			displayName,StringMatcher.indexOfIgnoreCase(displayName,rawKey)));
	    	    		List<String> numberList = getPhoneList(context,contactID);
	    	    		List<WGResultText> phoneNumberList = new LinkedList<WGResultText>();
	    	    		for(String number : numberList){
	    	    			WGResultText phoneNumber = new WGResultText(number);
	    	    			phoneNumberList.add(phoneNumber);
	    	    		}
	    	    		
	    	    		contactResult.setPhoneNumberList(phoneNumberList);
	    	    	}
	    	    	result = contactResult;
	    	    }
	    		
    			if( result!=null ){
    				commitCount++;
    				contactResults.add(result);
    			}
    			
			}while(cursor.moveToNext());
						
			return contactResults;
		
		}catch(Exception e){
			e.printStackTrace();
		}finally {
    		try{
    			if (cursor != null && !cursor.isClosed()){
    				cursor.close();
    			}
    			cursor = null;
    		}catch (Exception e) {
				// ignore
			}
    	}
		return null;*/
    }

    /**
     * 通过ID获取联系人电话列表
     *
     * @param context
     * @param curContanctID
     * @return
     */
    public static List<WGResultText> getPhoneList(Context context, String curContanctID) {
        List<WGResultText> phoneList = new ArrayList<WGResultText>();
        ContentResolver cr = context.getContentResolver();
        Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + curContanctID,
                null, null);
        for (int j = 0; j < phone.getCount(); j++) {
            phone.moveToPosition(j);
            String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            WGResultText resultText = new WGResultText(strPhoneNumber);
            phoneList.add(resultText);
        }
        return phoneList;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<WGContactResult> getContactsPhoneList(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            List<WGContactResult> contactResults = new ArrayList<WGContactResult>();
            ContentResolver cr = context.getContentResolver();
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{
                    Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID, Photo.PHOTO_ID}, null, null, null);
            Bitmap contactPhoto = null;
            if (phone != null) {
                while (phone.moveToNext()) {
                    String contactName = phone.getString(0);
                    String contactPhone = phone.getString(1);
                    Long contactid = phone.getLong(2);
                    String contactidStr = String.valueOf(contactid);
                    Long photoid = phone.getLong(3);

                    //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                    if (photoid > 0) {
                        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
                        contactPhoto = BitmapFactory.decodeStream(input);
                    } else {
                        contactPhoto = drawTextToBitmap(context, R.drawable.conntactionbackground, contactName);
                    }
                    contactResults.add(new WGContactResult(contactName, contactPhone, contactidStr, contactPhoto));
                }
            }
            if (contactList == null)
                contactList = contactResults;

            if (phone != null && !phone.isClosed()) {
                phone.close();
            }
            return contactResults;

        } else {
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 23);
        }
        return null;
    }

    /**
     * 改变图片背景
     */
    private static Bitmap drawTextToBitmap(Context mContext, int resourceId, String mText) {
        final String name0 = TextUtils.isEmpty(mText) ? "" : mText.substring(mText.length()-1);
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            // text size in pixels
            paint.setTextSize((int) (50 * scale));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();

            paint.getTextBounds(name0, 0, name0.length(), bounds);

            int bitmap_w=bitmap.getWidth();
            int bitmap_h=bitmap.getHeight();
            int text_w =  bounds.width();
            int text_h =  bounds.height();

            int x = (bitmap_w - text_w) / 2-bounds.left;
            int y = ((bitmap_h - text_h) / 2+text_h)-bounds.bottom;
            canvas.drawText(name0, x , y , paint);
            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    /**
     * 合并结果中重复部分
     *
     * @param contactResults
     * @return List<WGContactResult>
     */
    private static List<WGContactResult> combine(List<WGContactResult> contactResults) {
        List<WGContactResult> newList = new ArrayList<WGContactResult>();
        if (contactResults == null || contactResults.size() == 0) {
            return null;
        }
        newList.add(contactResults.get(0));
        boolean needAdd = true;
        for (int i = 1; i < contactResults.size(); i++) {
            for (WGContactResult result : newList) {
                if (contactResults.get(i).getContactID().equals(result.getContactID())) {
                    result.getPhoneNumberList().add(contactResults.get(i).getPhoneNumberList().get(0));
                    needAdd = false;
                    break;
                }
            }
            if (needAdd) {
                newList.add(contactResults.get(i));
            }
            needAdd = true;
        }
        return newList;
    }
}
