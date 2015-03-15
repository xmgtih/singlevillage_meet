package com.singlevillage.meet.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.singlevillage.meet.client.Client;
import com.singlevillage.meet.client.ClientOutputThread;
import com.singlevillage.meet.common.bean.User;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.common.tran.bean.TranObjectType;
import com.singlevillage.meet.util.DialogFactory;
import com.singlevillage.meet.util.Encode;
import com.way.chat.activity.R;

public class RegisterActivity extends MyActivity implements OnClickListener {

	private Button mBtnRegister;
	private Button mRegBack;
	private EditText mRegPhoneNum;
	private MyCount   mMyCount;
	private MyApplication application;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_input_phone_num);
		application = (MyApplication) this.getApplicationContext();
		initView();

	}

	public void initView() {
		mBtnRegister = (Button) findViewById(R.id.register_btn);
		mRegBack = (Button) findViewById(R.id.reg_back_btn);
		mBtnRegister.setOnClickListener(this);
		mRegBack.setOnClickListener(this);

		mRegPhoneNum = (EditText) findViewById(R.id.reg_phone_num);

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
