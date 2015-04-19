package com.singlevillage.meet.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singlevillage.meet.activity.ChatMsgEntity;
import com.singlevillage.meet.activity.FriendListActivity;
import com.singlevillage.meet.activity.Mul2OneActivity;
import com.singlevillage.meet.activity.MyApplication;
import com.singlevillage.meet.activity.R;
import com.singlevillage.meet.activity.R.drawable;
import com.singlevillage.meet.activity.R.id;
import com.singlevillage.meet.activity.R.layout;
import com.singlevillage.meet.client.Client;
import com.singlevillage.meet.client.ClientInputThread;
import com.singlevillage.meet.client.ClientOutputThread;
import com.singlevillage.meet.client.MessageListener;
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

/**
 * 收取消息服务
 * 
 * @author way
 * 
 */
public class GetMsgService extends Service {
	private static final int MSG = 0x001;
	private MyApplication application;
	private Client client;
	private NotificationManager mNotificationManager;
	private boolean isStart = false;// 是否与服务器连接上
	private Notification mNotification;
	private Context mContext = this;
	private SharePreferenceUtil util;
	private MessageDB messageDB;
	// 收到用户按返回键发出的广播，就显示通知栏
	private BroadcastReceiver backKeyReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "QQ进入后台运行", 0).show();
			setMsgNotification();
		}
	};
	// 用来更新通知栏消息的handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG:
			{
				JSONObject response = (JSONObject)msg.obj;
				JSONObject retData = response.optJSONObject("data");
				JSONArray  msgList = retData.optJSONArray("msg_list");
				int newMsgNum = application.getNewMsgNum();// 从全局变量中获取
				newMsgNum += msgList.length();//新信息个数
				application.setNewMsgNum(newMsgNum);// 再设置为全局变量
				for(int i = 0;  i < msgList.length(); i++)
				{
					String userId = response.optString("user_id");
					String name = response.optString("name");
					String content = response.optString("msg");
					String time = response.optString("time");
					//图片Url待加入
					ChatMsgEntity entity = new ChatMsgEntity();
					entity.setName(name);
					entity.setDate(time);
					entity.setMessage(content);
//					entity.setImg();TODO
					entity.setMsgType(true);//来自对方

					messageDB.saveMsg(0, entity);//uerID 应该改为房间号 TODO
				}
					// 更新通知栏
					int icon = R.drawable.notify_newmessage;
//					CharSequence tickerText = name + ":" + content;
					long when = System.currentTimeMillis();
					mNotification = new Notification(icon, "", when);

					mNotification.flags = Notification.FLAG_NO_CLEAR;
					// 设置默认声音
					mNotification.defaults |= Notification.DEFAULT_SOUND;
					// 设定震动(需加VIBRATE权限)
					mNotification.defaults |= Notification.DEFAULT_VIBRATE;
					mNotification.contentView = null;

					Intent intent = new Intent(mContext,
							FriendListActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							mContext, 0, intent, 0);
					mNotification.setLatestEventInfo(mContext, util.getName()
							+ " (" + newMsgNum + "条新消息)", "",
							contentIntent);
					mNotificationManager.notify(Constants.NOTIFY_ID, mNotification);// 通知一下才会生效哦

				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {// 在onCreate方法里面注册广播接收者
		// TODO Auto-generated method stub
		super.onCreate();
		messageDB = new MessageDB(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.BACKKEY_ACTION);
		registerReceiver(backKeyReceiver, filter);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		application = (MyApplication) this.getApplicationContext();
		client = application.getClient();
		application.setmNotificationManager(mNotificationManager);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		util = new SharePreferenceUtil(getApplicationContext(),
				Constants.SAVE_USER);
		
		
		isStart = client.start();
		application.setClientStart(isStart);
		System.out.println("client start:" + isStart);
		if (isStart) {
			ClientInputThread in = client.getClientInputThread();
			in.setMessageListener(new MessageListener() {

				@Override
				public void Message(TranObject msg) {
					// System.out.println("GetMsgService:" + msg);
					if (util.getIsStart()) {// 如果 是在后台运行，就更新通知栏，否则就发送广播给Activity
						if (msg.getType() == TranObjectType.MESSAGE) {// 只处理文本消息类型
							// System.out.println("收到新消息");
							// 把消息对象发送到handler去处理
							Message message = handler.obtainMessage();
							message.what = MSG;
							message.getData().putSerializable("msg", msg);
							handler.sendMessage(message);
						}
					} else {
						Intent broadCast = new Intent();
						broadCast.setAction(Constants.ACTION);
						broadCast.putExtra(Constants.MSGKEY, msg);
						sendBroadcast(broadCast);// 把收到的消息已广播的形式发送出去
					}
				}
			});
		}
	}

	@Override
	// 在服务被摧毁时，做一些事情
	public void onDestroy() {
		super.onDestroy();
		if (messageDB != null)
			messageDB.close();
		unregisterReceiver(backKeyReceiver);
		mNotificationManager.cancel(Constants.NOTIFY_ID);
		// 给服务器发送下线消息
		if (isStart) {
			ClientOutputThread out = client.getClientOutputThread();
			TranObject<User> o = new TranObject<User>(TranObjectType.LOGOUT);
			User u = new User();
			u.setId(Integer.parseInt(util.getId()));
			o.setObject(u);
			out.setMsg(o);
			// 发送完之后，关闭client
			out.setStart(false);
			client.getClientInputThread().setStart(false);
		}
		// Intent intent = new Intent(this, GetMsgService.class);
		// startService(intent);
	}

	/**
	 * 创建通知
	 */
	private void setMsgNotification() {
		int icon = R.drawable.notify;
		CharSequence tickerText = "";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);

		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(mContext.getPackageName(),
				R.layout.notify_view);
		contentView.setTextViewText(R.id.notify_name, util.getName());
		contentView.setTextViewText(R.id.notify_msg, "手机QQ正在后台运行");
		contentView.setTextViewText(R.id.notify_time, MyDate.getDate());
		// 指定个性化视图
		mNotification.contentView = contentView;

		Intent intent = new Intent(this, FriendListActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(Constants.NOTIFY_ID, mNotification);
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
							
							
							Message message = handler.obtainMessage();
							message.what = MSG;
							message.obj = response;
							handler.sendMessage(message);
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
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
}
