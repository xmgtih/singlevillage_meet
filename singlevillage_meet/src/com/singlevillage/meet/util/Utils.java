package com.singlevillage.meet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Utils {
	
	public static final String TAG = "meet";

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	public static float getScreenDensity(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception ex) {

		}
		return 1.0f;
	}
	
	public static int optInt(final String string, final int def) 
	{
		try 
		{
			if(!TextUtils.isEmpty(string))
			{
				return Integer.parseInt(string);
			}
		} 
		catch (NumberFormatException e) 
		{
			
		}
		return def;
	}
	
	private final static String headPhotoFileName = "headPhoto.png";
	private final static String headPhotoSubDir = "headPhoto";
	public static void saveHeadPhoto(Bitmap headPhotoBitmap)
	{
		File photoHeadFile = FileSystem.getProperFile(GlobalVar.getAppContext(), headPhotoSubDir, headPhotoFileName);
		IOUtil.createFile(photoHeadFile);
        FileOutputStream fOut = null;  
        try {  
            fOut = new FileOutputStream(photoHeadFile);  
            headPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
            fOut.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fOut.close();   
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return;
	}
	
	public static Bitmap getHeadPhoto()
	{
		File photoHeadFile = FileSystem.getProperFile(GlobalVar.getAppContext(), headPhotoSubDir, headPhotoFileName);
		if(null == photoHeadFile || !photoHeadFile.exists())
		{
			return null;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(photoHeadFile);
			Bitmap bitmap  = BitmapFactory.decodeStream(fis);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
			
		}finally{
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}