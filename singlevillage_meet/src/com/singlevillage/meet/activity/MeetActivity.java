package com.singlevillage.meet.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.singlevillage.meet.common.tran.bean.TranObject;
import com.way.chat.activity.R;

public class MeetActivity extends MyActivity {
	
	private TextView  mMsgTextView;
	private TextView  mPersonalMsgTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{//TODO 从上一个activity中取得当前的状态来加载meet具体fragment
		setContentView(R.layout.meet_layout);
		mMsgTextView = (TextView)findViewById(R.id.msg);
		mPersonalMsgTextView = (TextView)findViewById(R.id.personal_msg);
        // Create an instance of ExampleFragment
		MeetUnstartFragment firstFragment = new MeetUnstartFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction()
                .add(R.id.meet_view, firstFragment).commit();
		
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
