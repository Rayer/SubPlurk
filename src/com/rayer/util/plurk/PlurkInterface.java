package com.rayer.util.plurk;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.rayer.util.network.PostObject;
import com.rayer.util.plurk.data.PlurkScrap;
import com.rayer.util.plurk.data.PublicUserInfo;
import com.rayer.util.plurk.data.UserInfo;

public interface PlurkInterface {
	
	//String getAPIKey();
	
	/**
	 * Implementation of /API/Users/register
	 * Not implemented yet. Waiting for https protocol implemented.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	JSONObject register(String nickname, String fullname, String password, String gender, String date_of_birth, String opt_email) throws ClientProtocolException, IOException;
	
	/**
	 * Implementation of /API/Users/login
	 * Login, just like regular login. Be advised, you may want to catch ClientProtocolException first rather then IOException.
	 * @param username Username
	 * @param password Password
	 * @return null if Login failed, otherwise like getOwnProfile()
	 * @throws IOException maybe caused by timeout? I can't sure.
	 * @throws ClientProtocolException occurs while login failed, in Plurk's case, it is 400(BAD REQUEST) mostly
	 */
	JSONObject login(String username, String password) throws ClientProtocolException, IOException;
	
	/**
	 * Implementation of /API/Users/logout
	 * Logout.
	 * @return true for successfully logout, false for otherwise
	 */
	boolean logout();
	
	
	void update(PostObject obj);
	void updatePicture(PostObject obj);
	
	//API/Profile
	PublicUserInfo getPublicProfile(int uid);
	UserInfo getOwnProfile();
	
	/**
	 * Implementation of /API/Responses/get.
	 * @param plurk_id
	 * @param offset optional, it means only fetch responses from an offset - could be 0(no offset set), 5, 10 or 15.
	 */
	 ArrayList<PlurkScrap> getResponser(int plurk_id, int offset);
	
}
