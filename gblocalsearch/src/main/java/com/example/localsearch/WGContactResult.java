package com.example.localsearch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.List;

/**
 * 联系人 结果
 * @author lujibeat
 *
 */
public final class WGContactResult{

	private final String mContactID;
	private List<WGResultText> phoneNumberList;
	private WGResultText contactName;//联系人标红文本
	private String contact;//联系人
	private String contactPhone;
	private Bitmap photo;
	private String city;

	public String getContact() {
		return contact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public WGContactResult(final String contactID) {
		// TODO Auto-generated constructor stub
		mContactID=contactID;
	}
	public WGContactResult(String contactName,String phoneNum,String mContactID,Bitmap photo) {
		this.contact= contactName;
		this.contactPhone = phoneNum;
		this.photo = photo;
		this.mContactID = mContactID;
	}

	public final void setPhoto(Bitmap photo){
		this.photo = photo;
	}
	
	public final void setContactName(WGResultText contactName){
		this.contactName = contactName;
	}
	/**
	 * 
	 * @return WGResultText 联系人姓名
	 */
	public final WGResultText getContactName(){
		return contactName;
	}
	/**
	 * 
	 * @return String 联系人ID
	 */
	public final String getContactID(){
		return mContactID;
	}

	public void setPhoneNumberList(List<WGResultText> phoneNumberList) {
		// TODO Auto-generated method stub
		this.phoneNumberList = phoneNumberList;
	}
	/**
	 * 
	 * @return List<WGResultText> 联系人电话号码列表
	 */
	public List<WGResultText> getPhoneNumberList(){
		return phoneNumberList;
	}
	/**
	 * 
	 * @return Bitmap 联系人头像
	 */
	public Bitmap getPhoto(){
		return toRoundCornerImage(photo,20);
	}
	/**
	 * 获取圆角位图的方法
	 *
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	private  Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		// 抗锯齿
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
