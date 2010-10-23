package com.rayer.util.plurk.data;

import org.json.JSONObject;

import com.rayer.util.databridge.JSONConverter;

/**
 * @author rayer
 *
 */
public class UserInfo {
	public Integer 	uid;
	public String 	nick_name;
	public String 	display_name;
	public String 	full_name;
	public String 	location;
	public String 	date_of_birth;
	public Integer 	has_profile_image;
	public Integer 	avatar = -1;
	public Integer 	gender;
	public String 	page_title;
	public Double 	karma;
	public Integer 	recruited;
	public String 	relationship;
	
	public UserInfo(JSONObject userObject) {
		
		JSONConverter.extractFromJSON(UserInfo.class, this, userObject);
	}


	
	public Integer getID() {
		return uid;
	}

	public String getNickName() {
		return nick_name;
	}
	
	public String getRealName() {
		return display_name;
	}
	
	public Double getKarma() {
		return karma == null ? 0.0 : karma;
	}
	
	/**
	 * placeholder, will use more flexible method
	 * @return
	 */
	public String createAvatarUrlBig() {
		if(has_profile_image == 0)
			return "http://www.plurk.com/static/default_big.gif";
		
		if(avatar.equals(-1) || avatar.equals(0))
			return "http://avatars.plurk.com/" + getID() + "-big.jpg";
		
		return "http://avatars.plurk.com/" + getID() + "-big" + avatar + ".jpg";
	}
	
	public String createAvatarUrlMedium() {
		if(has_profile_image == 0)
			return "http://www.plurk.com/static/default_medium.gif";
		
		if(avatar.equals(-1) || avatar.equals(0))
			return "http://avatars.plurk.com/" + getID() + "-medium.gif";
		
		return "http://avatars.plurk.com/" + getID() + "-medium" + avatar + ".gif";
	}
	
	public String createAvatarUrlSmall() {
		if(has_profile_image == 0)
			return "http://www.plurk.com/static/default_small.gif";
		
		if(avatar.equals(-1) || avatar.equals(0))
			return "http://avatars.plurk.com/" + getID() + "-small.gif";
		
		return "http://avatars.plurk.com/" + getID() + "-small" + avatar + ".gif";
	}
}
