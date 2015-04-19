package com.singlevillage.meet.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.fragment.MeetStartFragment;
import com.singlevillage.meet.fragment.MeetUnstartFragment;
import com.singlevillage.meet.util.DialogFactory;
import com.singlevillage.meet.util.HttpUtils;
import com.singlevillage.meet.util.Utils;

public class MeetActivity extends MyActivity {
	
	private final static int UNSTART = 1;
	private final static int START = 2;
	
	private Date mRemainingData;
	private TextView  mMsgTextView;
	private TextView  mPersonalMsgTextView;
	private RelativeLayout  mUnStartContain;
	private RelativeLayout  mStartContain;
    private DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private String  mRemainingTime ;
    private String  mStartTime;
    private String  mEndTime;
	private static int mStatus = 0;
	TextView mRemainningTimeView;
	private MyCount  mMyCount = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{//TODO 从上一个activity中取得当前的状态来加载meet具体fragment
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meet_layout);
		initView();
//		getMeetStatus();TODO  有网的时候打开

		
		updateView(UNSTART);
	}
	
	private void initView(){
		mMsgTextView = (TextView)findViewById(R.id.msg);
		mPersonalMsgTextView = (TextView)findViewById(R.id.personal_msg);
		mUnStartContain = (RelativeLayout)findViewById(R.id.unStart_contain);
		mStartContain = (RelativeLayout)findViewById(R.id.start_contain);
		
		return ;
	}
	
	private void updateView(int status)
	{
		if(UNSTART == status)
		{
			mStatus = UNSTART;
			 mUnStartContain.setVisibility(View.VISIBLE);
			 mStartContain.setVisibility(View.GONE);
			 TextView beginTimeView = (TextView)findViewById(R.id.beginTime);
//			 beginTimeView.setText(mStartTime+"开始");
			 beginTimeView.setText("09:32:33"+"开始");
			 mRemainningTimeView = (TextView)findViewById(R.id.remainingTime);
			 mRemainingTime="09:32:33";
//			 remainningTimeView.setText(mRemainingTime);
			 mRemainningTimeView.setText("09:32:33");
			 try {
				 mRemainingData = sdf.parse(mRemainingTime);
				 long remainSecondes = mRemainingData.getTime();
				 Date baseTime = sdf.parse("00:00:00");
				 long baseSeconds = baseTime.getTime();
				 if(null != mMyCount)
				 {
					 mMyCount.cancel();
					 mMyCount = null;
				 }
				 mMyCount = new MyCount(remainSecondes - baseSeconds, 1000);
				 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 	
		}
		else
		{
			 mStatus = UNSTART;
			 mUnStartContain.setVisibility(View.GONE);
			 mStartContain.setVisibility(View.VISIBLE);
			
		}

		
	}
	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub


	}
	
	
	private void getMeetStatus()
	{
		String requestUrl = HttpUtils.MEET_STATUS;
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				Method.GET, requestUrl, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i(Utils.TAG,response.toString());
						int retCode = response.optInt("code");
						if (0 == retCode) {//ok
							JSONObject  data = response.optJSONObject("data");
							String status = data.optString("status");
							if("open".equalsIgnoreCase(status)){//start
								
								updateView(UNSTART);

							}
							else if("close".equalsIgnoreCase(status)){//unstart
								mRemainingTime = data.optString("remaining");
								mStartTime = data.optString("start_time");
								updateView(START);
							}
						}else{//error
							DialogFactory.ToastDialog(MeetActivity.this,
									"单身村", "服务器出现异常");
					  }

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						DialogFactory.ToastDialog(MeetActivity.this,
								"单身村", "网络有问题");
					}
				});

		HttpUtils.sendJsonRequest(jsonRequest);
	}
	
	
	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		private long mMillisInFuture;
		
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			mMillisInFuture = millisInFuture; 
		}

		@Override
		public void onFinish() {
			getMeetStatus();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if(millisUntilFinished <= mMillisInFuture/2)
			{
				getMeetStatus();//TODO 重新获取
			}
		
		    mRemainingData.setTime(millisUntilFinished);
		    mRemainningTimeView.setText(sdf.format(mRemainingData));
		}
	} 
	

}
