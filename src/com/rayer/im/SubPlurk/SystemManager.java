package com.rayer.im.SubPlurk;

import com.rayer.util.event.EventManager;
import com.rayer.util.plurk.PlurkController;

public class SystemManager extends EventManager {
	
	SystemManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	static SystemManager msDefaultInst;
	public static SystemManager getInst() {
		if(msDefaultInst == null) {
			msDefaultInst = new SystemManager();
		}
		return msDefaultInst;
	}
	
	PlurkController mController = new PlurkController();
	
	
	public PlurkController getPlurkController() {
		return mController;
	}

}
