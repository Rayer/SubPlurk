package com.rayer.util.plurk.events;

import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventParamBase;

public class OnPlurkLogout extends EventBase {

	public static final int ATTEMPING_LOGOUT = 0;
	public static final int LOGIN_TIMEOUT = 1;
	public static final int LOGOUT_SUCCESS = 2;
	
	int mStatus;
	
	public OnPlurkLogout(int status) {
		mStatus = status;
	}

	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnPlurkLogout.class) {

			@Override
			public int getArg1() {
				// TODO Auto-generated method stub
				return mStatus;
			}

			@Override
			public int getArg2() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getObj() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}

}
