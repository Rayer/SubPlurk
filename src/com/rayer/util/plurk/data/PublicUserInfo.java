package com.rayer.util.plurk.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.rayer.util.databridge.JSONConverter;

public class PublicUserInfo {
	public PublicUserInfo(JSONObject json) {
		JSONConverter.extractFromJSON(PublicUserInfo.class, this, json);
		JSONObject user = null;
		try {
			user = json.getJSONObject("user_info");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userInfo = new UserInfo(user);
	}
	
	public Integer friends_count;
	public Integer fans_count;
	public Boolean are_friends;
	public Boolean is_fan;
	public Boolean is_following;
	public Boolean has_read_permission;
	public String privacy;
	
	//不使用，等真正需要在用
	PlurkScrap[] plurks;
	
	UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}


}
