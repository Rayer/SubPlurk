package com.rayer.util.plurk;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.rayer.util.event.EventManagerInterface;
import com.rayer.util.plurk.events.OnPlurkLogin;

/**
 * Suspending... wait until main controller completed.
 * In most case, this will cooperate with EventManager as default event processing unit. Therefore, you can override the event manager by creating other EventManagerInterface(NYI)
 * @author rayer
 *
 */
public class PlurkControllerMT extends PlurkController {

	EventManagerInterface mEventManager;
	
	public PlurkControllerMT(EventManagerInterface em) {
		mEventManager = em;
	}
	
	/**
	 * login - async version.
	 * @param username PlurkID or e-mail
	 * @param password Plurk password
	 * @see login
	 * @see com.rayer.util.plurk.events.OnPlurkLogin
	 */
	public synchronized void async_login(final String username, final String password) {
		
		mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.ATTEMPING_LOGIN));
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				JSONObject retObj = null;
				
				try {
					retObj = login(username, password);
				} catch (ClientProtocolException e) {
					mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.LOGIN_CERTIFICATION_ERROR));
					e.printStackTrace();
					return;
				} catch (IOException e) {
					mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.LOGIN_TIMEOUT));
					e.printStackTrace();
					return;
				}
				
				mEventManager.sendMessage(new OnPlurkLogin(OnPlurkLogin.LOGIN_SUCCESS).setJSONObject(retObj));
			}});

		thread.start();
	}
		
}
