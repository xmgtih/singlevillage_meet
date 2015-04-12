package com.singlevillage.meet.activity;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.singlevillage.meet.common.tran.bean.TranObject;
import com.singlevillage.meet.fragment.MeetStartFragment;
import com.singlevillage.meet.fragment.MeetUnstartFragment;

public class MeetActivity extends MyActivity {
	
	private TextView  mMsgTextView;
	private TextView  mPersonalMsgTextView;
	private static int mStatus = 0;
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
        DownloadTask  downloadTask = new DownloadTask();
        downloadTask.execute();
		super.onCreate(savedInstanceState);
//        getMessage(null);

	}
	
	private void updateFragment(int status)
	{//TODO 通过getMessage获取后台状态来更新fragment
		if(0 == mStatus)
		{
			mStatus = 1;
			 FragmentTransaction transaction = getFragmentManager().beginTransaction();
			 MeetStartFragment newFragment = new MeetStartFragment();
	         // Replace whatever is in the fragment_container view with this fragment,
	         // and add the transaction to the back stack so the user can navigate back
	         transaction.replace(R.id.meet_view, newFragment);
	         transaction.addToBackStack(null);

	         // Commit the transaction
	         transaction.commit();			
		}
		else
		{
			mStatus = 0;
			 FragmentTransaction transaction = getFragmentManager().beginTransaction();
			 MeetUnstartFragment newFragment = new MeetUnstartFragment();
	         // Replace whatever is in the fragment_container view with this fragment,
	         // and add the transaction to the back stack so the user can navigate back
	         transaction.replace(R.id.meet_view, newFragment);
	         transaction.addToBackStack(null);

	         // Commit the transaction
	         transaction.commit();	
			
		}
        DownloadTask  downloadTask = new DownloadTask();
        downloadTask.execute();

		
	}
	@Override
	public void getMessage(TranObject msg) {
		// TODO Auto-generated method stub


	}
	
	class DownloadTask extends AsyncTask<Integer, Integer, String> {
		// 后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型

		@Override
		protected void onPreExecute() {
			// 第一个执行方法
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			int i = 1;

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "执行完毕";
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// 这个函数在doInBackground调用publishProgress时触发，虽然调用时只有一个参数
			// 但是这里取到的是一个数组,所以要用progesss[0]来取值
			// 第n个参数就用progress[n]来取值
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(String result) {
			// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			updateFragment(0);
			super.onPostExecute(result);
		}

	}  

}
