package com.singlevillage.meet.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singlevillage.meet.activity.R;

public class MeetUnstartFragment extends Fragment {
	
	public static final String BEGIN_TIME = "begain_time";
	public static final String END_TIME = "end_time";
	private String mBeginTimeStr;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View containView = inflater.inflate(R.layout.meet_unstart_fragment_view, container, false);
//        if (savedInstanceState != null) {
//        	mBeginTime = savedInstanceState.getString(BEGIN_TIME);
//        	
//        }
		
		return containView;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
        	mBeginTimeStr = args.getString(BEGIN_TIME);
        	TextView beginTimeView = (TextView)getActivity().findViewById(R.id.beginTime);
    		beginTimeView.setText(mBeginTimeStr+"开始");
    			
        }
	}

	public void setBeginTime(String beginTimeStr){
		
		TextView beginTimeView = (TextView)getActivity().findViewById(R.id.beginTime);
		beginTimeView.setText(beginTimeStr+"开始");
		
	}

	
	public void setRemainingTime(String remainTimeStr){
		
		TextView remainingTimeView = (TextView)getActivity().findViewById(R.id.remainingTime);
		remainingTimeView.setText(remainTimeStr);
		
	}
 
}
