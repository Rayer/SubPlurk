package com.rayer.util.plurk.events;

import org.json.JSONObject;

import com.rayer.util.event.EventBase;
import com.rayer.util.event.EventParamBase;

public class OnPlurkLogin extends EventBase {

	public static int ATTEMPING_LOGIN = 0;
	public static int LOGIN_SUCCESS = 1;
	public static int LOGIN_CERTIFICATION_ERROR = 2;
	public static int LOGIN_TIMEOUT = 3;
	
	int mLoginStatus = 0;
	JSONObject mRetObj = null;
	
	public OnPlurkLogin(int loginStatus) {
		mLoginStatus = 0;
	}
	
	public OnPlurkLogin setJSONObject(JSONObject retObj) {
		mRetObj = retObj;
		return this;
	}
	
	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnPlurkLogin.class){

			@Override
			public int getArg1() {
				// TODO Auto-generated method stub
				return mLoginStatus;
			}

			@Override
			public int getArg2() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getObj() {
				// TODO Auto-generated method stub
				return mRetObj;
			}};
	}

}
