package com.singlevillage.meet.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.util.DialogFactory;
import com.singlevillage.meet.util.HttpUtils;
import com.singlevillage.meet.util.Utils;

public class RegisterVerifyActivity extends MyActivity {
	
	private EditText   mInputNumText;
	private Button     mButton;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_mdn_singlevillage);
//		application = (MyApplication) this.getApplicationContext();
//		mSharePreferenceUtil = new SharePreferenceUtil(this, Constants.SAVE_USER);

		initView();
	//	mi = new MenuInflater(this);
	}
	
	public void initView() {
		mInputNumText = (EditText)findViewById(R.id.input_mdn);
		mButton = (Button)findViewById(R.id.verify_btn);
		mButton.setOnClickListener(new OnClickListener() {
	    
			@Override
			public void onClick(View v) {
				
				String phoneNum = mInputNumText.getText().toString();
				if(TextUtils.isEmpty(phoneNum)){
					DialogFactory.ToastDialog(RegisterVerifyActivity.this, "单身村注册",
							"请输入注册手机号码");
					return;
				}
				
				String requestUrl = HttpUtils.VERIFY_PHONE+phoneNum;
				JsonObjectRequest jsonRequest = new JsonObjectRequest(
						Method.GET, requestUrl, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								Log.i(Utils.TAG,response.toString());
								int retCode = response.optInt("code");
								if (0 == retCode) {//ok
									goRegister();
								}else{
									goNotice();
								}

							}

						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								
								DialogFactory.ToastDialog(RegisterVerifyActivity.this,
										"单身村注册", "网络有问题");
							}
						});

				HttpUtils.sendJsonRequest(jsonRequest);
			}
		});
		
	}

		
	private void goRegister(){
		Intent intent = new Intent(RegisterVerifyActivity.this, HeadPhotoSettingActivity.class);
		startActivity(intent);
	}
	private void goNotice(){
		Intent intent = new Intent(RegisterVerifyActivity.this, HeadPhotoSettingActivity.class);
		startActivity(intent);
		//TODO调试用
//		Intent intent = new Intent(RegisterVerifyActivity.this, RegisterNoticeActivity.class);
//		startActivity(intent);
	}

	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub

	}

}
