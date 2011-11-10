package com.rayer.im.SubPlurk.manager;

import android.content.Context;

import com.rayer.util.event.EventManager;
import com.rayer.util.gps.LocationService;
import com.rayer.util.plurk.PlurkControllerMT;

/**
 * Storage of some global variables, or system-dependent services.
 * @author rayer
 *
 */
public class SystemManager extends EventManager {
	
	SystemManager() {
		super();
	}

	static SystemManager msDefaultInst;
	public static SystemManager getInst() {
		if(msDefaultInst == null) {
			msDefaultInst = new SystemManager();
		}
		return msDefaultInst;
	}
	
	Context mContext;
	AccountManager mAccount;
	
	public void init(Context context) {
		if(mContext != null)
			return;
		
		mContext = context;		
		mLocation.init(mContext, this);	
		mAccount = new AccountManager(context);
	}
	
	//很多情況下, 有些元件因為缺乏Context所以還來不及被產生
	//所以要用它來確認一次這些元件的產生
	public void confirmComponents(Context context) {
		init(context);
	}
	
	PlurkControllerMT mControllerMT = new PlurkControllerMT(this);
	LocationService mLocation = new LocationService();
	
	public PlurkControllerMT getPlurkController() {
		return mControllerMT;
	}
	public LocationService getLocationService() {
		return mLocation;
	}
	
	public AccountManager getAccountManager() {
		return mAccount;
	}
	
}
