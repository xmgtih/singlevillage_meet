package com.singlevillage.meet.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singlevillage.meet.adapter.CropOptionAdapter;
import com.singlevillage.meet.client.Client;
import com.singlevillage.meet.client.ClientOutputThread;
import com.singlevillage.meet.common.bean.User;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.common.tran.bean.TranObjectType;
import com.singlevillage.meet.util.DialogFactory;
import com.singlevillage.meet.util.Encode;
import com.singlevillage.meet.util.ErrorCodeHelper;
import com.singlevillage.meet.util.FileSystem;
import com.singlevillage.meet.util.HttpUtils;
import com.singlevillage.meet.util.IOUtil;
import com.singlevillage.meet.util.Utils;

public class RegisterActivity extends MyActivity implements OnClickListener {

	private final int   PICK_FROM_FILE = 1;
	private final int   PICK_FROM_CAMERA = 2; 
	private final int   CROP_FROM_CAMERA = 3;
	
	private Button mBtnRegister;
	private ImageView mPhotoView;
	private Button mRegBack;
	private EditText mRegPhoneNum;
	private MyCount   mMyCount;
	private MyApplication mApplication;
	private RadioGroup  mRadiogroup;
	private RadioButton  mMale;
	private RadioButton  mFemale;
	private String mSex = "";
	private String mName;
	private String mHomeTown;
	private String mTel;
	private int    mAge;
	private int    mHeight;//cm
	private int    mWeight;//kg
	private String mSchool;
	private String mCompany;
	private Button mRegFinishButton;
	private Uri mImgeUri;
	private Uri mCropImageUri;
    public static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_information_singlevillage);
		mApplication = (MyApplication) this.getApplicationContext();
//		File cropFile = FileSystem.getInternalFile(this, "crop", "temp.jpeg");
//		IOUtil.createFile(cropFile);
//		mCropImageUri = Uri.parse(cropFile.getAbsolutePath());
		mCropImageUri = Uri
        .fromFile(Environment.getExternalStorageDirectory())
        .buildUpon()
        .appendPath(CROP_CACHE_FILE_NAME)
        .build();
		initView();
		

	}

	public void initView() {
		
		mPhotoView = (ImageView)findViewById(R.id.photo);
		mPhotoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent  openPicsIntent = new Intent(Intent.ACTION_PICK,  
//		                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				openPicsIntent.setType("image/*");
//				startActivityForResult(openPicsIntent, PICK_FROM_FILE);
				doCrop2();
			}
		});
		EditText nameText = (EditText)findViewById(R.id.input_name);
		mName = nameText.getText().toString();
		mRadiogroup = (RadioGroup)findViewById(R.id.select_gender);
		mMale = (RadioButton)findViewById(R.id.male);
	    mFemale = (RadioButton)findViewById(R.id.female);
		mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if(checkedId == mMale.getId()){
					mSex = "male";
				}else if(checkedId == mFemale.getId()){
					mSex = "female";
				}
			} 
			
		});
		
		EditText ageText = (EditText)findViewById(R.id.input_age);
		mAge = Utils.optInt(ageText.getText().toString(),0);
		
		EditText homeTownText = (EditText)findViewById(R.id.input_hometown);
		mHomeTown = homeTownText.getText().toString();
		
		EditText heightText = (EditText)findViewById(R.id.input_height);
		mHeight = Utils.optInt(heightText.getText().toString(),0);
		
		EditText weightText = (EditText)findViewById(R.id.input_weight);
		mWeight = Utils.optInt(weightText.getText().toString(),0);		
		
		EditText schoolText = (EditText)findViewById(R.id.input_shool);
		mSchool = schoolText.getText().toString();
		
		EditText companyText = (EditText)findViewById(R.id.input_company);
		mCompany = companyText.getText().toString();
		
		mRegFinishButton = (Button)findViewById(R.id.register_finish_btn);
		mRegFinishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {

					JSONObject requestJson = new JSONObject();
					requestJson.put("sex", mSex);
					requestJson.put("password", "");//TODO
					requestJson.put("age", mAge);
					requestJson.put("name", mName);
					requestJson.put("company", mCompany);
					requestJson.put("education", mCompany);//TODO
					requestJson.put("university", mCompany);//TODO
					requestJson.put("hometown", mHomeTown);
					requestJson.put("height", mHeight);
					requestJson.put("nationality", mHeight);//TODO

					JsonObjectRequest jsonRequest = new JsonObjectRequest(
							Method.POST, HttpUtils.REGISTER, requestJson,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									Log.i(Utils.TAG,response.toString());
									int retCode = response.optInt("code");
									if (ErrorCodeHelper.code.CODE_SUCCESS == retCode) {//ok
										//TODO 跳到登录
										DialogFactory.ToastDialog(RegisterActivity.this, "单身村注册",
												"注册成功，回登录界面登录试试吧");
										if (mDialog.isShowing())
											mDialog.dismiss();
										
									}else{//
										DialogFactory.ToastDialog(RegisterActivity.this, "单身村注册",
												"服务器异常");
										if (mDialog.isShowing())
											mDialog.dismiss();
									}

								}

							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									if (mDialog.isShowing())
										mDialog.dismiss();
									DialogFactory.ToastDialog(RegisterActivity.this,
											"单身村登录", "网络异常");
								}
							});

					HttpUtils.sendJsonRequest(jsonRequest);
					showRequestDialog();
				} catch (JSONException e) {
					if (mDialog.isShowing())
						mDialog.dismiss();
					DialogFactory.ToastDialog(RegisterActivity.this, "单身村注册",
							"请重新输入");
				}
				
			}
		});
		
		
		
		
		
		
		
//		mBtnRegister = (Button) findViewById(R.id.register_btn);
//		mRegBack = (Button) findViewById(R.id.reg_back_btn);
//		mBtnRegister.setOnClickListener(this);
//		mRegBack.setOnClickListener(this);
//
//		mRegPhoneNum = (EditText) findViewById(R.id.reg_phone_num);

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
            doCrop2();  
            break;  
        case PICK_FROM_FILE:  
            mImgeUri = data.getData();  
            doCrop2();  
            break;  
        case CROP_FROM_CAMERA:  
            if (null != mCropImageUri) {  
//                setCropImg(data);  
            	Bitmap  bitmap = decodeUriAsBitmap(mCropImageUri);
            	 mPhotoView.setImageBitmap(bitmap);  
                 saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"  
                         + System.currentTimeMillis() + ".jpeg", bitmap);  
            }  
            break;  
        }
		
	}
	
	private void setCropImg(Intent picdata) {  
        Bundle bundle = picdata.getExtras();  
        if (null != bundle) {  
            Bitmap mBitmap = bundle.getParcelable("data");  
            mPhotoView.setImageBitmap(mBitmap);  
            saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"  
                    + System.currentTimeMillis() + ".png", mBitmap);  
        }  
    }  
	
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
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
			intent.putExtra("outputX", 600);
			intent.putExtra("outputY", 600);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			
			intent.putExtra("return-data", false);
			intent.putExtra("output", mCropImageUri);

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
	
	private void doCrop2()
	{


		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);

		intent.setDataAndType(mCropImageUri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 2);

		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 600);

		intent.putExtra("outputY", 300);

		intent.putExtra("scale", true);

		intent.putExtra("return-data", false);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImageUri);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		intent.putExtra("noFaceDetection", true); // no face detection

		startActivityForResult(intent, CROP_FROM_CAMERA);
	}
	
	private void saveBitmap(String fileName, Bitmap mBitmap) {  
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
	

	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在注册中...");
		mDialog.show();
	}

	@Override
	public void onBackPressed() {// 捕获返回键
		toast(RegisterActivity.this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_btn:
			// showRequestDialog();
			estimate();
			break;
		case R.id.reg_back_btn:
			toast(RegisterActivity.this);
			break;
		default:
			break;
		}
	}

	private void toast(Context context) {
		new AlertDialog.Builder(context).setTitle("QQ注册")
				.setMessage("亲！您真的不注册了吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("取消", null).create().show();
	}

	private void estimate() {
		String regPhoneNum = mRegPhoneNum.getText().toString();

		if (regPhoneNum.equals("")) {
			DialogFactory.ToastDialog(RegisterActivity.this, "QQ注册",
					"亲！带*项是不能为空的哦");
		} else {
			String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
			Pattern p = Pattern.compile(regExp);
			Matcher m = p.matcher(regPhoneNum);

		  if(m.find()){
//				showRequestDialog();
			  if(null == mMyCount){
				  mMyCount = new MyCount(60*1000, 1000);
			  }
			  mMyCount.start();
		  }
		  else{
				DialogFactory.ToastDialog(RegisterActivity.this, "单身村注册",
				"手机号码不合法");
		  }
			  


//			if (passwd.equals(passwd2)) {
//				showRequestDialog();
//				// 提交注册信息
//				if (application.isClientStart()) {// 如果已连接上服务器
//					Client client = application.getClient();
////					Client client = GetMsgService.client;
//					ClientOutputThread out = client.getClientOutputThread();
//					TranObject<User> o = new TranObject<User>(
//							TranObjectType.REGISTER);
//					User u = new User();
//					u.setEmail(email);
//					u.setName(name);
//					u.setPassword(Encode.getEncode("MD5", passwd));
//					o.setObject(u);
//					out.setMsg(o);
//				} else {
//					if (mDialog.isShowing())
//						mDialog.dismiss();
//					DialogFactory.ToastDialog(this, "QQ注册", "亲！服务器暂未开放哦");
//				}
//
//			} else {
//				DialogFactory.ToastDialog(RegisterActivity.this, "QQ注册",
//						"亲！您两次输入的密码不同哦");
//			}
		}
	}

	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub
		switch (msg.getType()) {
		case REGISTER:
			User u = (User) msg.getObject();
			int id = u.getId();
			if (id > 0) {
//				if (mDialog != null) {
//					mDialog.dismiss();
//					mDialog = null;
//				}
//				DialogFactory.ToastDialog(RegisterActivity.this, "QQ注册",
//						"亲！请牢记您的登录QQ哦：" + id);
				mMyCount.cancel();//TODO  下面进入设置个人信息页面
			} else {
				if (mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				DialogFactory.ToastDialog(RegisterActivity.this, "QQ注册",
						"亲！很抱歉！QQ号暂时缺货哦");
			}
			break;

		default:
			break;
		}
	}
	
	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			mBtnRegister.setEnabled(true);
			mBtnRegister.setText("获取验证码");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mBtnRegister.setEnabled(false);
			mBtnRegister.setText("重新获取(" + millisUntilFinished / 1000 + ")秒");
		}
	} 
	
}
