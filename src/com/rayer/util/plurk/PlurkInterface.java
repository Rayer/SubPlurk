package com.rayer.util.plurk;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.rayer.util.network.PostObject;

public interface PlurkInterface {
	
	//String getAPIKey();
	
	/**
	 * Not implemented yet. Waiting for https protocol implemented.
	 */
	void register(PostObject obj);
	
	/**
	 * Login, just like regular login. Be advised, you may want to catch ClientProtocolException first rather then IOException.
	 * @param username Username
	 * @param password Password
	 * @return null if Login failed, otherwise like getOwnProfile()
	 * @throws IOException maybe caused by timeout? I can't sure.
	 * @throws ClientProtocolException occurs while login failed, in Plurk's case, it is 400(BAD REQUEST) mostly
	 */
	JSONObject login(String username, String password) throws ClientProtocolException, IOException;
	
	void logout();
	
	
	void update(PostObject obj);
	void updatePicture(PostObject obj);
	
}
