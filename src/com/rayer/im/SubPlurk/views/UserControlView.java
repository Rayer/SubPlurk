package com.rayer.im.SubPlurk.views;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.SystemManager;
import com.rayer.util.event.EventManager;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.PlurkControllerMT;
import com.rayer.util.plurk.data.UserInfo;
import com.rayer.util.plurk.events.OnPlurkLogin;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserControlView extends RelativeLayout {

	ImageView mAvatar_iv;
	TextView mName_tv;
	TextView mUUID_tv;
	TextView mKarma_tv;
	RelativeLayout mKarma_rl;
	ProgressBar mKarma_pb;
	
	PlurkControllerMT mController;
	
	EventManager mEm;
	
	EventProcessHandler mHandler = new EventProcessHandler() {

		@Override
		public void handleMessage(Message msg) {
			int identificator = msg.what;
			
			if(identificator == OnPlurkLogin.class.hashCode())
				processUserProfile();
			super.handleMessage(msg);
		}

		private void processUserProfile() {
			UserInfo info = mController.getUserInfo();
			if(info == null)
				return;
			
			setData(info);
		}
		
	};
	
	public UserControlView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initialize();
	}
	
	public UserControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialize();
	}

	void initialize() {
		
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.user_control_view, this);
		
		mAvatar_iv = (ImageView) findViewById(R.id.user_control_avatar_iv);
		mName_tv = (TextView) findViewById(R.id.user_control_name_tv);
		mUUID_tv = (TextView) findViewById(R.id.user_control_uuid_tv);
		mKarma_rl = (RelativeLayout) findViewById(R.id.user_control_karma_rl);
		mKarma_tv = (TextView) findViewById(R.id.user_control_karma_tv);
		mKarma_pb = (ProgressBar) findViewById(R.id.user_control_karma_pb);
		
		mController = SystemManager.getInst().getPlurkController();
		
		mEm = SystemManager.getInst();
		mEm.registerHandler(OnPlurkLogin.class, mHandler);
				
	}
	
	void setData(UserInfo info) {
		//avatar再handler設定
		mName_tv.setText(info.nick_name);
		mUUID_tv.setText("" + info.uid);
		setKarma(info.karma);
	}
	
	void setKarma(double karma) {
		int BackgroundColor = android.graphics.Color.BLACK;
		
		if(karma > 95)
			BackgroundColor = android.graphics.Color.CYAN;
		else if(karma > 90)
			BackgroundColor = android.graphics.Color.BLUE;
		
		mKarma_tv.setText("" + karma);
		mKarma_rl.setBackgroundColor(BackgroundColor);
		
		mKarma_pb.setProgress((int)karma);
	}


}
