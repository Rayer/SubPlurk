package com.rayer.util.gps;

import android.location.GpsStatus;

import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventParamBase;

public class OnGPSStatusChanged extends EventBase {
	
	GpsStatus mStatus;
	OnGPSStatusChanged(GpsStatus status) {
		mStatus = status;
	}

	@Override
	public EventParamBase createParameters() {
		// TODO Auto-generated method stub
		return new EventParamBase(OnGPSStatusChanged.class){

			@Override
			public int getArg1() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getArg2() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getObj() {
				// TODO Auto-generated method stub
				return mStatus;
			}};
	}

}
