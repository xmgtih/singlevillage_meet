package com.singlevillage.meet.activity;

import java.util.LinkedList;

import com.singlevillage.meet.adapter.RecentChatAdapter;
import com.singlevillage.meet.client.Client;
import com.singlevillage.meet.common.util.Constants;
import com.singlevillage.meet.util.GlobalVar;
import com.singlevillage.meet.util.SharePreferenceUtil;

import android.app.Application;
import android.app.NotificationManager;

public class MyApplication extends Application {
	public static final String TAG = "meet";
	private Client client;// 客户端
	private boolean isClientStart;// 客户端连接是否启动
	private NotificationManager mNotificationManager;
	private int newMsgNum = 0;// 后台运行的消息
	private LinkedList<RecentChatEntity> mRecentList;
	private RecentChatAdapter mRecentAdapter;
	private int recentNum = 0;

	@Override
	public void onCreate() {
		GlobalVar.setAppContext(this.getApplicationContext());
		SharePreferenceUtil util = new SharePreferenceUtil(this,
				Constants.SAVE_USER);
		System.out.println(util.getIp() + " " + util.getPort());
		client = new Client(util.getIp(), util.getPort());// 从配置文件中读ip和地址
		mRecentList = new LinkedList<RecentChatEntity>();
		RecentChatEntity  recentChatEntity1 = new RecentChatEntity();
		recentChatEntity1.setId(1);
		recentChatEntity1.setCount(1);
		recentChatEntity1.setImg(1);
		recentChatEntity1.setMsg("hehe");
		recentChatEntity1.setName("sdfadf");
		recentChatEntity1.setTime("sdfadfasdfsdf");
		
		mRecentList.add(recentChatEntity1);
		mRecentAdapter = new RecentChatAdapter(getApplicationContext(),
				mRecentList);
		super.onCreate();
	}

	public Client getClient() {
		return client;
	}

	public boolean isClientStart() {
		return isClientStart;
	}

	public void setClientStart(boolean isClientStart) {
		this.isClientStart = isClientStart;
	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	public void setmNotificationManager(NotificationManager mNotificationManager) {
		this.mNotificationManager = mNotificationManager;
	}

	public int getNewMsgNum() {
		return newMsgNum;
	}

	public void setNewMsgNum(int newMsgNum) {
		this.newMsgNum = newMsgNum;
	}

	public LinkedList<RecentChatEntity> getmRecentList() {
		return mRecentList;
	}

	public void setmRecentList(LinkedList<RecentChatEntity> mRecentList) {
		this.mRecentList = mRecentList;
	}

	public RecentChatAdapter getmRecentAdapter() {
		return mRecentAdapter;
	}

	public void setmRecentAdapter(RecentChatAdapter mRecentAdapter) {
		this.mRecentAdapter = mRecentAdapter;
	}

	public int getRecentNum() {
		return recentNum;
	}

	public void setRecentNum(int recentNum) {
		this.recentNum = recentNum;
	}
}
