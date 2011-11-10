package com.rayer.im.SubPlurk;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.google.android.maps.MapActivity;
import com.rayer.im.SubPlurk.manager.SystemManager;
import com.rayer.im.SubPlurk.views.LoginDrawerView;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.events.OnPlurkLogin;

public class SubPlurk extends MapActivity {
	    
	RelativeLayout mMainSplash;
    LoginDrawerView mLoginDrawer;
    SlidingDrawer mDrawer;
    ProgressDialog mPd;
    
    SystemManager mSys;
    
	EventProcessHandler mHandler = new EventProcessHandler(){

		@Override
		public void handleMessage(Message msg) {
			int identificator = msg.what;
			
			if(identificator == OnPlurkLogin.class.hashCode())
				processLogin(msg);
			super.handleMessage(msg);
		}

		private void processLogin(Message msg) {
			
			switch(msg.arg1) {
			case OnPlurkLogin.ATTEMPING_LOGIN:
				mPd.setTitle("Login");
				mPd.setMessage("Attemping Login...");
				mPd.show();
				break;
			case OnPlurkLogin.LOGIN_SUCCESS:
				mDrawer.close();
				mPd.dismiss();
				break;
			case OnPlurkLogin.LOGIN_CERTIFICATION_ERROR:
				mPd.dismiss();
				break;
			case OnPlurkLogin.LOGIN_TIMEOUT:
				mPd.dismiss();
				break;
			}
				
		}};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash);
        
        mSys = SystemManager.getInst();
        mSys.init(this);
        
        mSys.registerHandler(OnPlurkLogin.class, mHandler);
        mSys.removeHandler(OnPlurkLogin.class, mHandler);
        
        mMainSplash = (RelativeLayout) findViewById(R.id.main_splash);
        mDrawer = (SlidingDrawer) findViewById(R.id.main_login_drawer);
        
        mPd = new ProgressDialog(this);
    }
    
    @Override
    protected void onStart() {
    	mSys.getLocationService().enableService();
    	super.onStart();
    }

	@Override
	protected void onStop() {
		mSys.getLocationService().disableService();
		super.onStop();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
    

}