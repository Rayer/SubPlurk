package com.rayer.util.plurk.data;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class PlurksUsers {
	//ArrayList<PlurkUser> mUserList = new ArrayList<PlurkUser>();
	HashMap<Integer, PlurkUser> mUserMap = new HashMap<Integer, PlurkUser>();
	
	public PlurksUsers(JSONObject json) {
		JSONObject users = null;
		try {
			users = (JSONObject) json.get("plurks_users");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<?> iter = users.keys();
		while(iter.hasNext()) {
			String objID = (String) iter.next();
			try {
				PlurkUser user = new PlurkUser(users.getJSONObject(objID));
				mUserMap.put(Integer.parseInt(objID), user);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public HashMap<Integer, PlurkUser> getUserMap() {
		// TODO Auto-generated method stub
		return mUserMap;
	}
	

}
