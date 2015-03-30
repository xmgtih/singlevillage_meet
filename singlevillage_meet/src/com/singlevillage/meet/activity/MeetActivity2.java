package com.singlevillage.meet.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.singlevillage.meet.common.tran.bean.TranObject;

public class MeetActivity2 extends MyActivity implements OnClickListener{

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
	
	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);

		setContentView(R.layout.meet_layout2);
		initView();
		
		initTabIndicator();
		
		/**
		 * 设置监听
		 */
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{

				switch (position)
				{
				case 0:

//					mLiaotian.setTextColor(getResources().getColor(R.color.green));
					break;
				case 1:
//					mFaxian.setTextColor(getResources().getColor(R.color.green));
//					mTabFaxian.removeView(mBadgeViewforFaxian);
//					mBadgeViewforFaxian.setBadgeCount(15);
//					mTabFaxian.addView(mBadgeViewforFaxian);
					break;
				case 2:
//					mTongxunlu.setTextColor(getResources().getColor(R.color.green));

					break;
				}

				mCurrentIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			    if (positionOffset > 0)  
		        {  
		            ChangeColorIconWithTextView left = mTabIndicator.get(position);  
		            ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);  
		  
		            left.setIconAlpha(1 - positionOffset);  
		            right.setIconAlpha(positionOffset);  
		        }
			    return ;

			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		mViewPager.setAdapter(mAdapter);

		mViewPager.setCurrentItem(0);

	}
	
	private void initView()
	{
		mViewPager = (ViewPager)findViewById(R.id.meet_viewpager);
		MeetTab  meetTab = new MeetTab();
		PersonalMsgTab  personalMsgTab = new PersonalMsgTab();
		MeTab  meTab = new MeTab();

		mFragments.add(meetTab);
		mFragments.add(personalMsgTab);
		mFragments.add(meTab);
		/**
		 * 初始化Adapter
		 */
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public android.support.v4.app.Fragment getItem(int arg0) {
				
				return mFragments.get(arg0);
			}

			@Override
			public int getCount() {

				return mFragments.size();
			}
			
		};
		return ;
	}
	private void initTabIndicator()
	{
		ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
		ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
		ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);

		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	
	  @Override  
	    public void onClick(View v)  
	    {  
	  
	        resetOtherTabs();  
//	  
	        switch (v.getId())  
	        {  
	        case R.id.id_indicator_one:  
	            mTabIndicator.get(0).setIconAlpha(1.0f);  
	            mViewPager.setCurrentItem(0);  
	            break;  
	        case R.id.id_indicator_two:  
	            mTabIndicator.get(1).setIconAlpha(1.0f);  
	            mViewPager.setCurrentItem(1);  
	            break;  
	        case R.id.id_indicator_three:  
	            mTabIndicator.get(1).setIconAlpha(1.0f);  
	            mViewPager.setCurrentItem(2);  
	            break;  
	  
	        }  
	  
	    }  
	  
	    /** 
	     * 重置其他的Tab 
	     */  
	    private void resetOtherTabs()  
	    {  
	        for (int i = 0; i < mTabIndicator.size(); i++)  
	        {  
	            mTabIndicator.get(i).setIconAlpha(0);  
	        }  
	    }  
	  
	/**
	 * ViewPager的当前选中页
	 */
	private int mCurrentIndex;
	@Override
	public void getMessage(TranObject msg) {}


}
