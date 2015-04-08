package com.singlevillage.meet.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeetMulti2OneFragment extends Fragment {
	
	private View  mMainView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		mMainView = inflater.inflate(R.layout.meet_multi_one_fragment_view, container, false);
		initView(mMainView);
		 
		 return  mMainView;
	}
	
	private void initView(View view)
	{
//		ListView  interestTopicListView = (ListView)view.findViewById(R.id.interest_topic);
//		String[] interestTopics = {"hehe","hehehee","hehehehehehe"};
//		ArrayAdapter<String>  interestTopicsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, interestTopics);
//		
//		interestTopicListView.setAdapter(interestTopicsAdapter);
	}
 
}
