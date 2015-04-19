package com.singlevillage.meet.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PersonalMsgTab extends Fragment {
	private TextView  mMsgTextView;
	private TextView  mPersonalMsgTextView;
	private ListView mRecentListView;// 最近会话的listView
	private MyApplication application;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View tabView = inflater.inflate(R.layout.activity_meet_msg, container, false);
		mRecentListView = (ListView) tabView.findViewById(R.id.tab1_listView);
		
		application = (MyApplication)getActivity().getApplicationContext();
		mRecentListView.setAdapter(application.getmRecentAdapter());// 先设置空对象，要么从数据库中读出
		
		return  tabView;
	
	}

}
