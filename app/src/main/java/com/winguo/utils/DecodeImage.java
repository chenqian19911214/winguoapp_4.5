package com.winguo.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;
/**
 * 图片二维码识别
 * */
public class DecodeImage {

    /**
     * 图片二维码识别
     * @param bitmap
     * @return Result
     * */
	public static Result handleQRCodeFormBitmap(Bitmap bitmap) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType,String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        RGBLuminanceSource source =new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));  
        QRCodeReader reader2= new QRCodeReader();
        Result result = null;
        try {
        	try {
                result = reader2.decode(bitmap1,hints);
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }
        	
        } catch (NotFoundException e) {
                e.printStackTrace();
        }
        return result;
    }

}
