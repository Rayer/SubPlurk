package com.rayer.util.gps;

import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventParamBase;

public class OnLocationUpdate extends EventBase {
	
	
	enum UpdateType {
		GPS,
		WIFI,
		UNKNOWN
	}
	
	UpdateType mType;
	int mLongitudeE6;
	int mLatitudeE6;
	
	public OnLocationUpdate(UpdateType updateType, int longitude, int latitude) {
		mType = updateType;
		mLongitudeE6 = longitude;
		mLatitudeE6 = latitude;
	}

	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnLocationUpdate.class){

			@Override
			public int getArg1() {
				return mLongitudeE6;
			}

			@Override
			public int getArg2() {
				return mLatitudeE6;
			}

			@Override
			public Object getObj() {
				return mType;
			}};
	}

}
