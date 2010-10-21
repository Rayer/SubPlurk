package com.rayer.im.SubPlurk;

import android.content.Context;

import com.rayer.util.event.EventManager;
import com.rayer.util.gps.LocationService;
import com.rayer.util.plurk.PlurkControllerMT;
import com.rayer.util.plurk.data.PublicUserInfo;
import com.rayer.util.provisioner.ResourceProvisioner;

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
	
	public void init(Context context) {
		mContext = context;
		mLocation.init(mContext, this);	
	}
	
	PlurkControllerMT mControllerMT = new PlurkControllerMT(this);
	LocationService mLocation = new LocationService();
	
	ResourceProvisioner<PublicUserInfo> mUserInfoCache;
	public PlurkControllerMT getPlurkController() {
		return mControllerMT;
	}
	public LocationService getLocationService() {
		return mLocation;
	}
	
}
