package com.rayer.im.SubPlurk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.manager.SystemManager;
import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.PlurkControllerMT;
import com.rayer.util.plurk.data.PlurkUser;
import com.rayer.util.plurk.events.OnPlurkLogin;


public class FriendPickerView extends LinearLayout {



	PlurkControllerMT mController;
	Gallery mAvatar_gal;
	TextView mName_tv;
	TextView mKarma_tv;
	
	Object[] mUsers;
	
	EventProcessHandler mHandler = new EventProcessHandler(){


		private void processLoginEvent(int msg) {
			if(msg == OnPlurkLogin.LOGIN_SUCCESS) {
				 mUsers = mController.getCurrentInPlurkUsers().getUserMap().values().toArray();
				 mAvatar_gal.setAdapter(new AvatarGalleryAdapter());
			}
				
		}

		@Override
		public void processEvent(Class<? extends EventBase> event, int arg1,
				int arg2, Object obj) {
			if(event == OnPlurkLogin.class)
				processLoginEvent(arg1);
		}};
	
	public FriendPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		
		SystemManager.getInst().registerHandler(OnPlurkLogin.class, mHandler);
		
		mController = SystemManager.getInst().getPlurkController();
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.friend_picker_view, this);
		 
		mName_tv = (TextView) findViewById(R.id.friend_picker_tv);
		mKarma_tv = (TextView) findViewById(R.id.friend_picker_karma_tv);
		mAvatar_gal = (Gallery) findViewById(R.id.friend_picker_gallery);
				 
		mAvatar_gal.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				PlurkUser user = (PlurkUser) mUsers[position];
				mName_tv.setText("Name : " + user.full_name);
				mKarma_tv.setText("Karma : " + user.karma);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		 
	}

	
	
	class AvatarGalleryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mUsers.length;
		}

		@Override
		public Object getItem(int position) {
			return mUsers[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PlurkUser user = (PlurkUser)mUsers[position];
			ImageView iv = new ImageView(getContext());
			iv.setImageBitmap(mController.getPlurkerAvatar(user.id));
			return iv;
		}

	}
}
