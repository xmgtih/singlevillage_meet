package com.singlevillage.meet.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.singlevillage.meet.activity.R.id;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.util.FileSystem;
import com.singlevillage.meet.util.IOUtil;
import com.singlevillage.meet.util.Utils;

public class HeadPhotoSettingActivity extends MyActivity {
	
	
	private final int   PICK_FROM_FILE = 1;
	private CropImageView mHeadImageView;
	private Button    msetButton;
	private Button    mReSetButton;
	private Button    mFinishButton;

	private FrameLayout headImageViewContainer;
	private LinearLayout mHeadImageResetFinish;
	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);

		setContentView(R.layout.activity_head_photo_setting);
		
		initView();
	}
	public void initView(){
		headImageViewContainer = (FrameLayout)findViewById(R.id.headImageViewContainer);
		msetButton = (Button)findViewById(R.id.setHeadPhotoButton);
		msetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent  openPicsIntent = new Intent(Intent.ACTION_PICK,  
		                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				openPicsIntent.setType("image/*");
				startActivityForResult(openPicsIntent, PICK_FROM_FILE);
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode != RESULT_OK) {  
            return;  
        }  
		switch (requestCode) {
		case PICK_FROM_FILE:
		{
           processUnCroppedPic(data);
		}
			break;

		default:
			break;
		}
		
	}
	
    private void   processUnCroppedPic(Intent data)
    {
		Uri  imageUri = data.getData();
		Bitmap  imageBitMap = decodeUriAsBitmap(imageUri);
		
		mHeadImageView = new CropImageView(HeadPhotoSettingActivity.this);//(CropImageView)findViewById(R.id.headImageView);
		mHeadImageView.setImageBitmap(imageBitMap);
		mHeadImageView.setAspectRatio(1, 1);//设置比例为一比一
		mHeadImageView.setFixedAspectRatio(true);//设置允许按比例截图，如果不设置就是默认的任意大小截图
		headImageViewContainer.addView(mHeadImageView);
		
		msetButton.setVisibility(View.GONE);
		mHeadImageResetFinish = (LinearLayout)findViewById(R.id.headResetFinish);
		mHeadImageResetFinish.setVisibility(View.VISIBLE);
		mReSetButton = (Button)findViewById(R.id.resetHeadPhotoButton);
		mReSetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent  openPicsIntent = new Intent(Intent.ACTION_PICK,  
		                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				openPicsIntent.setType("image/*");
				startActivityForResult(openPicsIntent, PICK_FROM_FILE);
			}
		});
		mFinishButton = (Button)findViewById(R.id.finshiHeadPhotoButton);
		mFinishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Bitmap headPhotoBitMap = mHeadImageView.getCroppedImage();
				Utils.saveHeadPhoto(headPhotoBitMap);
			}
		});
		
    }
	
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
//        	bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        	bitmap = decodeSampledBitmapFromResource(uri, headImageViewContainer.getWidth(), headImageViewContainer.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    
    

    public  Bitmap decodeSampledBitmapFromResource(Uri uri,
            int reqWidth, int reqHeight) throws FileNotFoundException {
    	Log.i(MyApplication.TAG, "decodeSampledBitmapFromResource, reqwidth"+ reqWidth +", reqHeight"+ reqHeight);
    	// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri),null, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return      BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri),null, options);
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options,
    		int reqWidth, int reqHeight) {
    	// 源图片的高度和宽度
    	final int height = options.outHeight;
    	final int width = options.outWidth;
    	Log.i(MyApplication.TAG, "calculateInSampleSize, height"+ height +", width"+ width);
    	int inSampleSize = 1;
    	if (height > reqHeight || width > reqWidth) {
    		// 计算出实际宽高和目标宽高的比率
    		final int heightRatio = Math.round((float) height / (float) reqHeight);
    		final int widthRatio = Math.round((float) width / (float) reqWidth);
    		// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
    		// 一定都会大于等于目标的宽和高。
    		inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    	}
    	Log.i(MyApplication.TAG, "calculateInSampleSize, inSampleSize"+ inSampleSize);
    	return inSampleSize;
    }
    
	
	@Override
	public void getMessage(TranObject msg) {
		

	}

}
