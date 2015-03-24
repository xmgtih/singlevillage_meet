package com.singlevillage.meet.activity;

import java.util.ArrayList;
import java.util.List;

import com.way.chat.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class VerticalScrollTextActivity extends Activity {


	VerticalScrollTextView mSampleView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vertical_scroll_textview);
		mSampleView = (VerticalScrollTextView) findViewById(R.id.sampleView1);
		List lst=new ArrayList<Sentence>();
		for(int i=0;i<30;i++){
			if(i%2==0){
				Sentence sen=new Sentence(i,i+"hehe ");
				lst.add(i, sen);
			}else{
				Sentence sen=new Sentence(i,i+"hehehehe");
				lst.add(i, sen);
			}
		}	
		//��View�������
		mSampleView.setList(lst);
		//����View
		mSampleView.updateUI();		
	}	
}