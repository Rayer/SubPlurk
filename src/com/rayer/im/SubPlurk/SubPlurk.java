package com.rayer.im.SubPlurk;

import android.app.Activity;
import android.os.Bundle;
import com.rayer.im.SubPlurk.views.LoginDrawerView;
import com.rayer.util.event.EventProcessHandler;

public class SubPlurk extends Activity {
	
	EventProcessHandler mHandler = new EventProcessHandler(){};
    
    LoginDrawerView mLoginDrawer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash);       
    }

}