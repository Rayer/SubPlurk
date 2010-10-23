package com.rayer.util.plurk.data;

import org.json.JSONObject;

import com.rayer.util.databridge.JSONConverter;

public class PlurkUser {
	public String display_name;
	public String nick_name;
	public Integer has_profile_image;
	public String location;
	public Boolean is_premium;	//...這啥小
	public String date_of_birth;
	public Double karma;
	public String full_name;
	public Integer gender;
	public String timezone;
	public Integer id;
	public Integer avatar;
	
	public PlurkUser(JSONObject obj) {
		formFromJSON(obj);
	}
	
	public void formFromJSON(JSONObject obj) {
		JSONConverter.extractFromJSON(PlurkUser.class, this, obj);
	}
}
