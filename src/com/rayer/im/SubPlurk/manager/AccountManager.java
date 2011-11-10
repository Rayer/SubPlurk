package com.rayer.im.SubPlurk.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventManager;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.PlurkControllerMT;
import com.rayer.util.plurk.events.OnPlurkLogin;
import com.rayer.util.plurk.events.OnPlurkLogout;

public class AccountManager {
	private static final String LAST_LOGINID_PREF = "com.rayer.im.SubPlurk.lastlogin_id_pref";
	private static final String LAST_LOGINPSW_PREF = "com.rayer.im.SubPlurk.lastlogin_psw_pref";
	String currentLoginAcc;
	String currentLoginPass;
	
	boolean isLoggedIn;
	
	Context context;
	SharedPreferences sharedPref;
	
	EventProcessHandler mHandler = new EventProcessHandler(){

		@Override
		public void processEvent(Class<? extends EventBase> event, int arg1,
				int arg2, Object obj) {
			if(event == OnPlurkLogin.class && arg1 == OnPlurkLogin.LOGIN_SUCCESS) {
				isLoggedIn = true;
				sharedPref.edit().putString(LAST_LOGINID_PREF, currentLoginAcc).commit();
				sharedPref.edit().putString(LAST_LOGINPSW_PREF, currentLoginPass).commit();
			}
			
		}};
	
	AccountManager(Context context) {
		EventManager em = SystemManager.getInst();
		em.registerHandler(OnPlurkLogin.class, mHandler);
		em.registerHandler(OnPlurkLogout.class, mHandler);
		sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	}
	
	public void attempLogging(String loginID, String loginPass) {
		//final EventManager em = SystemManager.getInst();
		final PlurkControllerMT pc = SystemManager.getInst().getPlurkController();
		currentLoginAcc = loginID;
		currentLoginPass = loginPass;
		pc.async_login(loginID, loginPass);		
	}
	
	public String getLastLoginSucceedAcc() {
		return sharedPref.getString(LAST_LOGINID_PREF, "");
	}
	
	public String getLastLoginSucceedPass() {
		return sharedPref.getString(LAST_LOGINPSW_PREF, "");
	}

}
