package com.singlevillage.meet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MeetOne2MultiFragment extends Fragment {

	private View mMainView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mMainView = inflater.inflate(R.layout.meet_one_multi_fragment_view,
				container, false);
		initView(mMainView);

		return mMainView;
	}

	private void initView(View view) {
		LinearLayout headPhotoVerLayout = (LinearLayout) view
				.findViewById(R.id.head_photos);

		LinearLayout.LayoutParams headPhotoHorLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 1);


		LayoutInflater inflater = LayoutInflater.from(getActivity());

		for (int i = 0; i < 2; i++) {
			LinearLayout headPhotoHorLayout = new LinearLayout(getActivity());
			headPhotoHorLayout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < 3; j++) {
				View photoItem1 = inflater.inflate(
						R.layout.meet_one_multi_head_photo_item, null);

					headPhotoHorLayout.addView(photoItem1,
							headPhotoHorLayoutParams);

			}
			headPhotoVerLayout.addView(headPhotoHorLayout);
		}

		// ListView interestTopicListView =
		// (ListView)view.findViewById(R.id.interest_topic);
		// String[] interestTopics = {"hehe","hehehee","hehehehehehe"};
		// ArrayAdapter<String> interestTopicsAdapter = new
		// ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, interestTopics);
		//
		// interestTopicListView.setAdapter(interestTopicsAdapter);
	}

}
