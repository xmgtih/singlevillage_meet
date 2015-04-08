package com.singlevillage.meet.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.singlevillage.meet.adapter.CropOptionAdapter;
import com.singlevillage.meet.common.tran.bean.TranObject;

public class RegisterInfoSettingActivity extends MyActivity {
	
	private final int   PICK_FROM_FILE = 1;
	private final int   PICK_FROM_CAMERA = 2; 
	private final int   CROP_FROM_CAMERA = 3;
	
	
	private Uri mImgeUri;
	private  ImageView mImageView;
	private  Button    mUploadButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.register_information_setting);
		mImageView = (ImageView) findViewById(R.id.user_head_portiait);
		mUploadButton = (Button) findViewById(R.id.upload_head_portiait);
		mUploadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent  openPicsIntent = new Intent(Intent.ACTION_PICK,  
		                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				openPicsIntent.setType("image/*");
				startActivityForResult(openPicsIntent, PICK_FROM_FILE);  
			}
		});

		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(RESULT_OK != resultCode){
			return ;
		}
		
	    if (resultCode != RESULT_OK) {  
            return;  
        }  
        switch (requestCode) {  
        case PICK_FROM_CAMERA:  
            doCrop();  
            break;  
        case PICK_FROM_FILE:  
            mImgeUri = data.getData();  
            doCrop();  
            break;  
        case CROP_FROM_CAMERA:  
            if (null != data) {  
                setCropImg(data);  
            }  
            break;  
        }
		
	}

	private void setCropImg(Intent picdata) {  
        Bundle bundle = picdata.getExtras();  
        if (null != bundle) {  
            Bitmap mBitmap = bundle.getParcelable("data");  
            mImageView.setImageBitmap(mBitmap);  
            saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"  
                    + System.currentTimeMillis() + ".png", mBitmap);  
        }  
    }  
	private void doCrop() {

		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);
		int size = list.size();

		if (size == 0) {
			Toast.makeText(this, "can't find crop app", Toast.LENGTH_SHORT)
					.show();
			return;
		} else {
			intent.setData(mImgeUri);
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			// only one
			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);
				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));
				startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				// many crop app
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();
					co.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);
					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));
					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(
						getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("choose a app");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_FROM_CAMERA);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (mImgeUri != null) {
							getContentResolver().delete(mImgeUri, null, null);
							mImgeUri = null;
						}
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
	
	public void saveBitmap(String fileName, Bitmap mBitmap) {  
        File f = new File(fileName);  
        FileOutputStream fOut = null;  
        try {  
            f.createNewFile();  
            fOut = new FileOutputStream(f);  
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
            fOut.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fOut.close();  
                Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
    }  
	
	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub

	}

}
