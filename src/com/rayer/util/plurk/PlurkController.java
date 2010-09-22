package com.rayer.util.plurk;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.rayer.util.network.PostObject;

/**
 * 這個版本是無Multi-thread的版本的實作，後面要再做一個把他包起來
 * @author rayer
 *
 */
public class PlurkController implements PlurkInterface {

	static final String API_KEY = "FCP5FqGgydpb4IijJcYZ6yUqQQTzHXer";
	static final String MAIN_URL = "http://plurk.com/API/";
	
//	EventManager mEventManager = SystemManager.getInst();
//	EventProcessHandler mHandler = new EventProcessHandler();
	
	HttpClient mClient = new DefaultHttpClient();
	
	public PlurkController() {
		PostObject.setAPIParams("api_key", API_KEY);
	}
	
	@Override
	public void register(PostObject obj) {
		//not implemented yet.
	}
	
	//需要一個中央事件控制系統

	//等等把這個改成multi-thread
	//想想怎麼改比較好
	
	@Override
	public JSONObject login(String username, String password) throws ClientProtocolException, IOException {
		
		String result = "";
		PostObject.setAPIParams( "api_key", API_KEY);
		PostObject obj = new PostObject("Users/login", "username", username, "password", password);
		
		JSONObject json = null;


		HttpGet get = new HttpGet(MAIN_URL + obj.toString());
		Log.d("subplurk", "attemping connect to : " + MAIN_URL + obj.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();

			//mClient.execute(get, handler);
		result = mClient.execute(get, handler);

		
//		Log.d("subplurk", "ret : " + result);
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public void update(PostObject obj) {

	}

	@Override
	public void updatePicture(PostObject obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}


}
