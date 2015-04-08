package com.singlevillage.meet.adapter;

import java.util.LinkedList;



import com.singlevillage.meet.activity.MyApplication;
import com.singlevillage.meet.activity.R;
import com.singlevillage.meet.activity.RecentChatEntity;
import com.singlevillage.meet.activity.R.drawable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MeetMsgChatAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<RecentChatEntity> list;
	private MyApplication application;
	private LayoutInflater inflater;
	private int[] imgs = { R.drawable.icon, R.drawable.f1, R.drawable.f2,
			R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6,
			R.drawable.f7, R.drawable.f8, R.drawable.f9 };
	public MeetMsgChatAdapter(Context context, LinkedList<RecentChatEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		application = (MyApplication) context.getApplicationContext();
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
