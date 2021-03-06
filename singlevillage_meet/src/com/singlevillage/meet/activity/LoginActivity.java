package com.singlevillage.meet.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.singlevillage.meet.client.Client;
import com.singlevillage.meet.client.ClientOutputThread;
import com.singlevillage.meet.common.bean.User;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.common.tran.bean.TranObjectType;
import com.singlevillage.meet.common.util.Constants;
import com.singlevillage.meet.service.GetMsgService;
import com.singlevillage.meet.util.DialogFactory;
import com.singlevillage.meet.util.Encode;
import com.singlevillage.meet.util.ErrorCodeHelper;
import com.singlevillage.meet.util.HttpUtils;
import com.singlevillage.meet.util.SharePreferenceUtil;
import com.singlevillage.meet.util.UserDB;
import com.singlevillage.meet.util.Utils;

/**
 * 登录
 * 
 * @author way
 * 
 */
public class LoginActivity extends MyActivity implements OnClickListener {
	
	private TextView mBtnRegister;
	private Button mBtnLogin;
	private EditText mAccounts, mPassword;
	private CheckBox mAutoSavePassword;
	private MyApplication application;
	private MenuInflater mi;// 菜单
	private SharePreferenceUtil mSharePreferenceUtil;
/*	private View mMoreView;// “更多登录选项”的view
	private ImageView mMoreImage;// “更多登录选项”的箭头图片
	private View mMoreMenuView;// “更多登录选项”中的内容view
	
	private boolean mShowMenu = false;// “更多登录选项”的内容是否显示
*/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		application = (MyApplication) this.getApplicationContext();
		mSharePreferenceUtil = new SharePreferenceUtil(this, Constants.SAVE_USER);

		initView();
	//	mi = new MenuInflater(this);
	}

	@Override
	protected void onResume() {// 在onResume方法里面先判断网络是否可用，再启动服务,这样在打开网络连接之后返回当前Activity时，会重新启动服务联网，
		super.onResume();
		if (isNetworkAvailable()) {
			
		} else {
			toast(this);
		}
	}

	public void initView() {
	//	mAutoSavePassword = (CheckBox) findViewById(R.id.auto_save_password);
		//mMoreView = findViewById(R.id.more);
	//	mMoreMenuView = findViewById(R.id.moremenu);
	//	mMoreImage = (ImageView) findViewById(R.id.more_image);
	//	mMoreView.setOnClickListener(this);

		mBtnRegister = (TextView) findViewById(R.id.regist_text);
		mBtnRegister.setOnClickListener(this);

		mBtnLogin = (Button) findViewById(R.id.login_btn);
		mBtnLogin.setOnClickListener(this);

		mAccounts = (EditText) findViewById(R.id.input_mdn);
		mPassword = (EditText) findViewById(R.id.input_passwd);
	/*自动保存密码功能
		if (mAutoSavePassword.isChecked()) {
			SharePreferenceUtil util = new SharePreferenceUtil(
					LoginActivity.this, Constants.SAVE_USER);
			mAccounts.setText(util.getId());
			mPassword.setText(util.getPasswd());
		}*/
	}

	/**
	 * “更多登录选项”内容的显示方法
	 * 
	 * @param bShow
	 *            是否显示
	 */
	/*public void showMoreView(boolean bShow) {
		if (bShow) {
			mMoreMenuView.setVisibility(View.GONE);
			mMoreImage.setImageResource(R.drawable.login_more_up);
			mShowMenu = true;
		} else {
			mMoreMenuView.setVisibility(View.VISIBLE);
			mMoreImage.setImageResource(R.drawable.login_more);
			mShowMenu = false;
		}
	}*/

	/**
	 * 处理点击事件
	 */
	public void onClick(View v) {

		switch (v.getId()) {
	/*	case R.id.more:
			showMoreView(!mShowMenu);
			break;*/
		case R.id.regist_text:
			goRegisterActivity();
			break;
		case R.id.login_btn:
			submit();
			break;
		default:
			break;
		}
	}

	/**
	 * 进入注册界面
	 */
	public void goRegisterActivity() {
		Intent intent = new Intent();
		intent.setClass(this, RegisterVerifyActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击登录按钮后，弹出验证对话框
	 */
	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在验证账号...");
		mDialog.show();
	}

	/**
	 * 提交账号密码信息到服务器
	 */
	private void submit() {
		String accounts = mAccounts.getText().toString();
		String password = mPassword.getText().toString();
		if (accounts.length() == 0 || password.length() == 0) {
			DialogFactory.ToastDialog(this, "单身村登录", "亲！帐号或密码不能为空哦");
		} else {
			
			try {

				JSONObject requestJson = new JSONObject();
				requestJson.put("tel", accounts);
				requestJson.put("password", password);
				JsonObjectRequest jsonRequest = new JsonObjectRequest(
						Method.POST, HttpUtils.LOG_IN, requestJson,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.i(Utils.TAG,response.toString());
								int retCode = response.optInt("code");
								if (0 == retCode) {//ok
									JSONObject data = response.optJSONObject("data");
									if(null != data){
										mSharePreferenceUtil.setToken(data.optString("token"));
										goMeetActivity();
									}else{
										DialogFactory.ToastDialog(LoginActivity.this,
												"单身村登录", "服务器异常");
									}
								}else if(retCode == ErrorCodeHelper.code.CODE_USER_NO_EXISTS){
									DialogFactory.ToastDialog(LoginActivity.this,
											"单身村登录", "用户不存在，请先注册");
									if (mDialog.isShowing())
										mDialog.dismiss();
								}else{//账号密码错误
									DialogFactory.ToastDialog(LoginActivity.this, "单身村登录",
											"亲！您的帐号或密码错误哦");
									if (mDialog.isShowing())
										mDialog.dismiss();
								}

							}

						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								if (mDialog.isShowing())
									mDialog.dismiss();
								DialogFactory.ToastDialog(LoginActivity.this,
										"单身村登录", "网络有问题");
							}
						});

				HttpUtils.sendJsonRequest(jsonRequest);
				showRequestDialog();
			} catch (JSONException e) {
				if (mDialog.isShowing())
					mDialog.dismiss();
				DialogFactory.ToastDialog(LoginActivity.this, "单身村登录",
						"请重新输入");
			}
		}
	}

	private void goMeetActivity(){
		Intent i = new Intent(LoginActivity.this,
				MeetActivity2.class);
		startActivity(i);

		if (mDialog.isShowing())
			mDialog.dismiss();
		finish();
		Toast.makeText(getApplicationContext(), "登录成功", 0).show();
	}
	@Override
	// 依据自己需求处理父类广播接收者收取到的消息
	public void getMessage(TranObject msg) {
	}

	@Override
	// 添加菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		mi.inflate(R.menu.login_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// 菜单选项添加事件处理
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.login_menu_setting:
			setDialog();
			break;
		case R.id.login_menu_exit:
			exitDialog(LoginActivity.this, "单身村提示", "亲！您真的要退出吗？");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {// 捕获返回按键
		exitDialog(LoginActivity.this, "单身村提示", "亲！您真的要退出吗？");
	}

	/**
	 * 退出时的提示框
	 * 
	 * @param context
	 *            上下文对象
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 */
	private void exitDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (application.isClientStart()) {// 如果连接还在，说明服务还在运行
							// 关闭服务
							Intent service = new Intent(LoginActivity.this,
									GetMsgService.class);
							stopService(service);
						}
						close();// 调用父类自定义的循环关闭方法
					}
				}).setNegativeButton("取消", null).create().show();
	}

	/**
	 * “设置”菜单选项的功能实现
	 */
	private void setDialog() {
		final View view = LayoutInflater.from(LoginActivity.this).inflate(
				R.layout.setting_view, null);
		new AlertDialog.Builder(LoginActivity.this).setTitle("设置服务器ip、port")
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 把ip和port保存到文件中
						EditText ipEditText = (EditText) view
								.findViewById(R.id.setting_ip);
						EditText portEditText = (EditText) view
								.findViewById(R.id.setting_port);
						String ip = ipEditText.getText().toString();
						String port = portEditText.getText().toString();
						SharePreferenceUtil util = new SharePreferenceUtil(
								LoginActivity.this, Constants.IP_PORT);
						if (ip.length() > 0 && port.length() > 0) {
							util.setIp(ip);
							util.setPort(Integer.valueOf(port));
							Toast.makeText(getApplicationContext(),
									"亲！保存成功，重启生效哦", 0).show();
							finish();
						}else{
							Toast.makeText(getApplicationContext(),
									"亲！ip和port都不能为空哦", 0).show();
						}
					}
				}).setNegativeButton("取消", null).create().show();
	}

	/**
	 * 判断手机网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager mgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	private void toast(Context context) {
		new AlertDialog.Builder(context)
				.setTitle("温馨提示")
				.setMessage("亲！您的网络连接未打开哦")
				.setPositiveButton("前往打开",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}
						}).setNegativeButton("取消", null).create().show();
	}
}
