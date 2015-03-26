package com.singlevillage.meet.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.singlevillage.meet.common.tran.bean.TranObject;
import com.way.chat.activity.R;

public class MeetMsgActivity extends MyActivity {
	
	private TextView  mMsgTextView;
	private TextView  mPersonalMsgTextView;
	private ListView mRecentListView;// 最近会话的listView
	private MyApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{//TODO 从上一个activity中取得当前的状态来加载meet具体fragment
//		application = (MyApplication) this.getApplicationContext();
//		setContentView(R.layout.tab1);
//		mRecentListView = (ListView) lay1.findViewById(R.id.tab1_listView);
//		mRecentListView.setAdapter(application.getmRecentAdapter());// 先设置空对象，要么从数据库中读出
		super.onCreate(savedInstanceState);
	}
	
	private void updateFragment()
	{//TODO 通过getMessage获取后台状态来更新fragment
		
	}
	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub

	}

}
