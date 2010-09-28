package com.rayer.im.SubPlurk;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rayer.im.SubPlurk.Activities.MainPlurkActivity;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.PlurkControllerMT;
import com.rayer.util.plurk.events.OnPlurkLogin;

public class SubPlurk extends Activity {
	
    EventProcessHandler mHandler = new EventProcessHandler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			int identificator = msg.what;
			
			if(identificator == OnPlurkLogin.class.hashCode())
				processLogin(msg);

		}

		private void processLogin(Message msg) {
			Log.d("SubPlurk", "Get login message : " + msg.arg1);
			int result = msg.arg1;
			switch(result) {
			case OnPlurkLogin.ATTEMPING_LOGIN:
				mPd.show();
				break;
			case OnPlurkLogin.LOGIN_SUCCESS:
				mPd.dismiss();
				Toast.makeText(SubPlurk.this, "Login Succeed..", Toast.LENGTH_LONG).show();
				Bundle bundle = new Bundle();
				bundle.putString("login_data", ((JSONObject)msg.obj).toString());
				
				Intent intent = new Intent();
				intent.setClass(SubPlurk.this, MainPlurkActivity.class);
				intent.putExtras(bundle);
				
				startActivity(intent);
				
				break;
			default:
				mPd.dismiss();
				Toast.makeText(SubPlurk.this, "Login failed...", Toast.LENGTH_LONG).show();
				mLoginBtn.setEnabled(true);
				break;
			}
			
		}};
    
    EditText mPlurkidET;
    EditText mPlurkPassET;
    Button mLoginBtn;
    
    ProgressDialog mPd;
    
    SystemManager mSys;
    PlurkControllerMT mController;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_splash);
        
        mSys = SystemManager.getInst();
        mController = mSys.getPlurkController();
        mSys.registerHandler(OnPlurkLogin.class, mHandler);
        
        
        
        mPlurkidET = (EditText) findViewById(R.id.login_splash_plurkid_et);
        mPlurkPassET = (EditText) findViewById(R.id.login_splash_plurkpass_et);
        mLoginBtn = (Button) findViewById(R.id.login_splash_login_btn);
        
        mPd = new ProgressDialog(this);
        mPd.setTitle("Logging in to Plurk");
        mPd.setMessage("Attemping logging...");
        
        mLoginBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mController.async_login(mPlurkidET.getText().toString(), mPlurkPassET.getText().toString());
			}});
    }
}