package com.singlevillage.meet.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singlevillage.meet.adapter.ChatMsgViewAdapter;
import com.singlevillage.meet.client.Client;
import com.singlevillage.meet.client.ClientOutputThread;
import com.singlevillage.meet.common.bean.TextMessage;
import com.singlevillage.meet.common.bean.User;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.common.tran.bean.TranObjectType;
import com.singlevillage.meet.common.util.Constants;
import com.singlevillage.meet.util.DialogFactory;
import com.singlevillage.meet.util.ErrorCodeHelper;
import com.singlevillage.meet.util.HttpUtils;
import com.singlevillage.meet.util.MessageDB;
import com.singlevillage.meet.util.MyDate;
import com.singlevillage.meet.util.SharePreferenceUtil;
import com.singlevillage.meet.util.Utils;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 聊天Activity
 * 
 * @author way
 */
public class Mul2OneActivity extends MyActivity implements OnClickListener {
	private final static  long LONG_TIME = 1000000000;
	private final static  long INTEVE = 2000;
	private Button mBtnSend;// 发送btn
	private Button mBtnBack;// 返回btn
	private EditText mEditTextContent;
	private TextView mFriendName;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
	private SharePreferenceUtil util;
	private User user;
	private MessageDB messageDB;
	private MyApplication application;
	private MyCount  mMyCount;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.chat);
		application = (MyApplication) getApplicationContext();
		messageDB = new MessageDB(this);
		user = (User) getIntent().getSerializableExtra("user");
		util = new SharePreferenceUtil(this, Constants.SAVE_USER);
		initView();// 初始化view
		initData();// 初始化数据
	}

	/**
	 * 初始化view
	 */
	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.chat_send);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.chat_back);
		mBtnBack.setOnClickListener(this);
		mFriendName = (TextView) findViewById(R.id.chat_name);
		mFriendName.setText(util.getName());
		mEditTextContent = (EditText) findViewById(R.id.chat_editmessage);
	}

	/**
	 * 加载消息历史，从数据库中读出
	 */
	public void initData() {
		List<ChatMsgEntity> list = messageDB.getMsg(user.getId());
		if (null != list && list.size() > 0) {
			for (ChatMsgEntity entity : list) {
				if (entity.getName().equals("")) {
					entity.setName(user.getName());
				}
				if (entity.getImg() < 0) {
					entity.setImg(user.getImg());
				}
				mDataArrays.add(entity);
			}
			Collections.reverse(mDataArrays);
		}
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		mListView.setSelection(mAdapter.getCount() - 1);
		if(null != mMyCount)
		{
			mMyCount.cancel();
			mMyCount = null;
		}
		mMyCount = new MyCount(LONG_TIME, INTEVE);//2S更新一次
		mMyCount.start();
	}
	
	@Override
	public void onBackPressed() {// 捕获返回按键事件，进入后台运行
		// TODO Auto-generated method stub
		// 发送广播，通知服务，已进入后台运行
		Intent i = new Intent();
		i.setAction(Constants.BACKKEY_ACTION);
		sendBroadcast(i);
		if(null != mMyCount)
		{
			mMyCount.cancel();
			mMyCount = null;
		}
		util.setIsStart(true);// 设置后台运行标志，正在运行
		finish();// 再结束自己
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		messageDB.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_send:// 发送按钮点击事件
			send();
			break;
		case R.id.chat_back:// 返回按钮点击事件
			finish();// 结束,实际开发中，可以返回主界面
			break;
		}
	}

	/**
	 * 发送消息
	 */
	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setName(util.getName());
			entity.setDate(MyDate.getDateEN());
			entity.setMessage(contString);
			entity.setImg(util.getImg());
			entity.setMsgType(false);

			messageDB.saveMsg(user.getId(), entity);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
			mEditTextContent.setText("");// 清空编辑框数据
			mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项

			//直接发送数据
			
			JSONObject requestJson = null;
			try {
				requestJson = new JSONObject();
				requestJson.put("msg", contString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Method.POST, HttpUtils.CHAT_SEND, requestJson,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.i(Utils.TAG,response.toString());
							int retCode = response.optInt("code");
							if (ErrorCodeHelper.code.CODE_SUCCESS == retCode) {//ok
							}else{//
								DialogFactory.ToastDialog(Mul2OneActivity.this, "单身村注册",
										"服务器异常");
							}

						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {

							DialogFactory.ToastDialog(Mul2OneActivity.this,
									"单身村", "网络异常");
						}
					});
			HttpUtils.sendJsonRequest(jsonRequest);
		}
	}
	private void getMsg(){
		
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				Method.GET, HttpUtils.CHAT_SEND, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {//TODO 解析并更新list
						Log.i(Utils.TAG,response.toString());
						int retCode = response.optInt("code");
						if (ErrorCodeHelper.code.CODE_SUCCESS == retCode) {//ok
							
							JSONObject retData = response.optJSONObject("data");
							JSONArray  msgList = retData.optJSONArray("msg_list");
							for(int i = 0;  i < msgList.length(); i++)
							{
								String userId = response.optString("user_id");
								String name = response.optString("name");
								String msg = response.optString("msg");
								String time = response.optString("time");
								//图片Url待加入
								ChatMsgEntity entity = new ChatMsgEntity();
								entity.setName(name);
								entity.setDate(time);
								entity.setMessage(msg);
//								entity.setImg();TODO
								entity.setMsgType(true);//来自对方

								messageDB.saveMsg(user.getId(), entity);

								mDataArrays.add(entity);
								mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
								mEditTextContent.setText("");// 清空编辑框数据
								mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
								
							}
							
							
						}else{//
							DialogFactory.ToastDialog(Mul2OneActivity.this, "单身村注册",
									"服务器异常");
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						DialogFactory.ToastDialog(Mul2OneActivity.this,
								"单身村", "网络异常");
					}
				});
		HttpUtils.sendJsonRequest(jsonRequest);
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

		}

		@Override
		public void onTick(long millisUntilFinished) {
			getMsg();
		}
	} 
	
	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub
	}
}