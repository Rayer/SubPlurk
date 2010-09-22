package com.rayer.util.plurk;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.rayer.im.SubPlurk.SystemManager;
import com.rayer.util.event.EventManager;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.events.OnPlurkLogin;

/**
 * Suspending... wait until main controller completed.
 * In most case, this will cooperate with EventManager as default event processing unit. Therefore, you can override the event manager by creating other EventManagerInterface(NYI)
 * @author rayer
 *
 */
public class PlurkControllerMT {

	EventProcessHandler mHandler = new EventProcessHandler();
	EventManager mEventManager = SystemManager.getInst();
	
	PlurkController mController = new PlurkController();
	
	public PlurkControllerMT() {
		//mEventManager.registerHandler(OnPlurkLogin.class, mHandler);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 */
	public void Login(final String username, final String password) {
		
		mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.ATTEMPING_LOGIN));
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				JSONObject retObj = null;
				try {
					retObj = mController.login(username, password);
				} catch (ClientProtocolException e) {
					mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.LOGIN_CERTIFICATION_ERROR));
					e.printStackTrace();
				} catch (IOException e) {
					mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.LOGIN_TIMEOUT));
					e.printStackTrace();
				}
				
				mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.LOGIN_SUCCESS).setJSONObject(retObj));
			}});
		thread.start();
	}
	
	

}
