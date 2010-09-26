package com.rayer.im.SubPlurk;

import com.rayer.util.event.EventManager;
import com.rayer.util.plurk.PlurkController;
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
	
	PlurkController mController = new PlurkController();
	
	ResourceProvisioner<PublicUserInfo> mUserInfoCache;
	
	
	public PlurkController getPlurkController() {
		return mController;
	}
}
