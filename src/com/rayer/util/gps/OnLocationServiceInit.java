package com.rayer.util.gps;

import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventParamBase;

public class OnLocationServiceInit extends EventBase {
	
	static public final int START_INITIALIZATION = 0;
	static public final int INITIALIZATION_WITH_WIFI = 1;
	static public final int INITIALIZATION_WITH_GPS = 2;
	static public final int INITIALIZATION_WITH_BOTH = 3;
	static public final int INITIALIZATION_FAILED = 4;
	
	int mStatus;
	
	public OnLocationServiceInit(int status) {
		mStatus = status;
	}
	
	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnLocationServiceInit.class){

			@Override
			public int getArg1() {
				return mStatus;
			}

			@Override
			public int getArg2() {
				return 0;
			}

			@Override
			public Object getObj() {
				return null;
			}};
	}

}
