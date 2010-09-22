package com.rayer.util.plurk.data;

import java.lang.reflect.Field;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 先暫時全部都寫String, 以後在慢慢改
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
	public Integer 	avatar;
	public Integer 	gender;
	public String 	page_title;
	public Double 	karma;
	public Integer 	recruited;
	public String 	relationship;
	
	public UserInfo(JSONObject userObject) {
		
		Field[] fields = UserInfo.class.getFields();
		for(Field f : fields) {
			Log.d("SubPlurk", "getting info : " + f.getName());
			try {
				f.set(this, userObject.get(f.getName()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
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
}
