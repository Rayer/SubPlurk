package com.rayer.util.plurk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rayer.util.network.PostObject;
import com.rayer.util.plurk.data.PlurkScrap;
import com.rayer.util.plurk.data.PublicUserInfo;
import com.rayer.util.plurk.data.UserInfo;
import com.rayer.util.provisioner.FileSystemResourceProvisioner;
import com.rayer.util.provisioner.InternetResourceProvisioner;
import com.rayer.util.provisioner.MemoryCacheResourceProvisioner;
import com.rayer.util.provisioner.ResourceProvisioner;
import com.rayer.util.provisioner.ResourceProxy;

/**
 * 這個版本是無Multi-thread的版本的實作，後面要再做一個把他包起來
 * @author rayer
 *
 */
public class PlurkController implements PlurkInterface {

	static final String API_KEY = "FCP5FqGgydpb4IijJcYZ6yUqQQTzHXer";
	static final String MAIN_URL = "http://plurk.com/API/";
	
	ResourceProvisioner<PublicUserInfo> mUserInfoCache;
	ResourceProvisioner<PublicUserInfo> mInternetUserInfoProvisioner;
	
	ResourceProvisioner<Bitmap> mUserAvatarFileSystemCacheMedium;
	ResourceProvisioner<Bitmap> mUserAvatarCacheMedium;
	
//	EventManager mEventManager = SystemManager.getInst();
//	EventProcessHandler mHandler = new EventProcessHandler();
	
	HttpClient mClient = new DefaultHttpClient();
	
	public PlurkController() {
		PostObject.setAPIParams("api_key", API_KEY);
		PostObject.setPostfix(MAIN_URL);
		
		mUserInfoCache = new MemoryCacheResourceProvisioner<PublicUserInfo>(){

			@Override
			public boolean destroyElement(PublicUserInfo source) {
				// TODO Auto-generated method stub
				return false;
			}};
			
		mInternetUserInfoProvisioner = new ResourceProvisioner<PublicUserInfo>(){

			@Override
			public PublicUserInfo getResource(String identificator) {
				JSONObject response = getPublicProfileImpl(identificator);
				PublicUserInfo ret = new PublicUserInfo(response);		
				return ret;
			}

			@Override
			public boolean setResource(String identificator,
					PublicUserInfo targetResource) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean dereferenceResource(String identificator) {
				// TODO Auto-generated method stub
				return false;
			}};
		
		mUserAvatarFileSystemCacheMedium = new FileSystemResourceProvisioner<Bitmap>("./sdcard/.subplurk/cache/medium/") {

			@Override
			public Bitmap formFromStream(InputStream in) {
				return BitmapFactory.decodeStream(in);
			}

			@Override
			public void writeToOutputStream(Bitmap target, FileOutputStream fo) {
				
				target.compress(CompressFormat.PNG, 100, fo);

			}
		};
			
		mUserAvatarCacheMedium = new MemoryCacheResourceProvisioner<Bitmap>(){

			@Override
			public boolean destroyElement(Bitmap source) {
				source.recycle();
				return true;
			}};
			
		//這很麻煩 因為她的indentificator要等到拿到UserInfo才能決定
		//mInternetUserAvatarProvider = new ResourceProvisioner<Bitmap>(){};
		
	}
	
	JSONObject getPublicProfileImpl(String identificator) {
		PostObject post = new PostObject("/Profile/getPublicProfile", "user_id", identificator);
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> res = new BasicResponseHandler();
		
		JSONObject response = null;
		try {
			response = new JSONObject(mClient.execute(get, res));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	
	@Override
	public JSONObject register(String nickname, String fullname, String password, String gender, String date_of_birth, String opt_email) throws ClientProtocolException, IOException {
		PostObject post = new PostObject("/Users/register", "nick_name", nickname, "full_name", fullname, "password", password, "gender", gender, "date_of_birth", date_of_birth, "email", "opt_email");
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		
		JSONObject ret = null;
		String result = mClient.execute(get, handler);
		
		try {
			ret = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	//需要一個中央事件控制系統

	//等等把這個改成multi-thread
	//想想怎麼改比較好
	
	@Override
	public JSONObject login(String username, String password) throws ClientProtocolException, IOException {
		
		String result = "";
		//PostObject.setAPIParams( "api_key", API_KEY);
		PostObject obj = new PostObject("Users/login", "username", username, "password", password);
		
		JSONObject json = null;


		HttpGet get = new HttpGet(obj.toString());
		Log.d("subplurk", "attemping connect to : " + obj.toString());
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
	public boolean logout() {
		PostObject.setAPIParams( "api_key", API_KEY);
		PostObject obj = new PostObject("Users/logout");
		HttpGet get = new HttpGet(MAIN_URL + obj.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();

		try {
			mClient.execute(get, handler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		
		return true;
	}

	@Override
	public void update(PostObject obj) {

	}

	@Override
	public void updatePicture(PostObject obj) {

	}

	@Override
	public PublicUserInfo getPublicProfile(final int uid) {
		ResourceProxy<PublicUserInfo> rp = new ResourceProxy<PublicUserInfo>(){

			@Override
			public String getIndentificator() {
				// TODO Auto-generated method stub
				return "" + uid;
			}};
			
		rp.addProvisioner(mUserInfoCache);
		rp.addProvisioner(mInternetUserInfoProvisioner);
		return rp.getResource(null);
	}

	@Override
	public UserInfo getOwnProfile() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Bitmap getPortraitMedium(final int uid) {
		
		final PublicUserInfo info = getPublicProfile(uid);
		
		ResourceProxy<Bitmap> rp = new ResourceProxy<Bitmap>(){

			@Override
			public String getIndentificator() {
				return "" + uid;
			}};
			
		rp.addProvisioner(mUserAvatarFileSystemCacheMedium);
		rp.addProvisioner(mUserAvatarCacheMedium);
		rp.addProvisioner(new InternetResourceProvisioner<Bitmap>(){

			@Override
			public Bitmap formFromStream(InputStream is) {
				return BitmapFactory.decodeStream(is);
			}

			@Override
			public String getUrlAddress(String identificator) {
				// TODO Auto-generated method stub
				Log.d("SubPlurk", "Attemping to get avatar from url : " + info.getUserInfo().createAvatarUrlMedium());
				return info.getUserInfo().createAvatarUrlMedium();
			}});
			
			return rp.getResource(null);
	}

	@Override
	public ArrayList<PlurkScrap> getResponser(int plurk_id, int offset) {
		PostObject post = new PostObject("Responses/get", "plurk_id", "" + plurk_id, "from_response", offset == 0 ? "" : "" + offset);
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> res = new BasicResponseHandler();
		ArrayList<PlurkScrap> retArray = new ArrayList<PlurkScrap>();
		
		try {
			String ret = mClient.execute(get, res);
			JSONObject json = new JSONObject(ret);
			
			JSONArray arr = json.getJSONArray("responses");
			for(int counter = 0; counter < arr.length(); ++counter)
				retArray.add(new PlurkScrap((JSONObject)arr.get(counter)));
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retArray;
	}




}
