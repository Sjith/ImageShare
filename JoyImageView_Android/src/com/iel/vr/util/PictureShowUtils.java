package com.iel.vr.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class PictureShowUtils {
	
	//Drawable → Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
	        
	        Bitmap bitmap = Bitmap
	                        .createBitmap(
	                                        drawable.getIntrinsicWidth(),
	                                        drawable.getIntrinsicHeight(),
	                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
	                                                        : Bitmap.Config.RGB_565);
	        Canvas canvas = new Canvas(bitmap);
	        //canvas.setBitmap(bitmap);
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
	        drawable.draw(canvas);
	        return bitmap;
	}
	
//	            从资源中获取Bitmap
//		Resources res=getResources();
//		Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.pic);
	
	
	//Bitmap → byte[]
	public static byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //除了PNG还有很多常见格式，如jpeg等
		return baos.toByteArray();
		}
	
	//byte[] → Bitmap
	public static Bitmap Bytes2Bimap(byte[] b){
	    if(b.length==0){
	    	return null;
	    }
	    else {
	    	return BitmapFactory.decodeByteArray(b, 0, b.length);
	    }
  }

}
