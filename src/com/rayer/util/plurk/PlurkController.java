package com.rayer.util.plurk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rayer.util.network.PostObject;
import com.rayer.util.plurk.data.PlurkScrap;
import com.rayer.util.plurk.data.PlurkUser;
import com.rayer.util.plurk.data.PlurksUsers;
import com.rayer.util.plurk.data.UserInfo;
import com.rayer.util.provisioner.FileSystemResourceProvisioner;
import com.rayer.util.provisioner.InternetResourceProvisioner;
import com.rayer.util.provisioner.MemoryCacheResourceProvisioner;
import com.rayer.util.provisioner.ResourceProvisioner;
import com.rayer.util.provisioner.ResourceProxy;

/**
 * 這個版本是Single thread的版本的實作，後面要再做一個把他包起來
 * @author rayer
 *
 */
public class PlurkController implements PlurkInterface {

	static final String API_KEY = "FCP5FqGgydpb4IijJcYZ6yUqQQTzHXer";
	static final String MAIN_URL = "http://plurk.com/API/";
	
	ResourceProvisioner<Bitmap> mUserAvatarFileSystemCacheMedium;
	ResourceProvisioner<Bitmap> mUserAvatarCacheMedium;
	
	UserInfo mUserInfo;
	PlurksUsers mInPlurkUsers;
	
	boolean mIsLoggedIn = false;
		
//	EventManager mEventManager = SystemManager.getInst();
//	EventProcessHandler mHandler = new EventProcessHandler();
	
	public PlurkController() {
		PostObject.setAPIParams("api_key", API_KEY);
		PostObject.setPostfix(MAIN_URL);
	
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
			}

			@Override
			public boolean clearAllCachedResource() {
				// TODO Auto-generated method stub
				return false;
			}};
			
		//這很麻煩 因為她的indentificator要等到拿到UserInfo才能決定
		//mInternetUserAvatarProvider = new ResourceProvisioner<Bitmap>(){};
		
	}
	
	JSONObject getPublicProfileImpl(String identificator) {
		PostObject post = new PostObject("/Profile/getPublicProfile", "user_id", identificator);
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> res = new BasicResponseHandler();
		
		HttpClient client = new DefaultHttpClient();
		
		JSONObject response = null;
		try {
			response = new JSONObject(client.execute(get, res));
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
		HttpClient client = new DefaultHttpClient();
		
		JSONObject ret = null;
		String result = client.execute(get, handler);
		
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
		//HttpPost post = new HttpPost(obj.toString());
		Log.d("subplurk", "attemping connect to : " + obj.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();
//		ResponseHandler<String> handler = new ResponseHandler<String>(){
//
//			@Override
//			public String handleResponse(HttpResponse response)
//					throws ClientProtocolException, IOException {
//				response.
//				return null;
//			}};
		HttpClient client = new DefaultHttpClient();

			//mClient.execute(get, handler);
		result = client.execute(get, handler);
//		Log.d("subplurk", "ret : " + result);
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mUserInfo = new UserInfo(json.getJSONObject("user_info"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO change to merge therefore.
		mInPlurkUsers = new PlurksUsers(json);
		
		//做cache
		getInPlurkAvatars(mInPlurkUsers);
		return json;
	}
	
	public PlurksUsers getCurrentInPlurkUsers() {
		return mInPlurkUsers;
	}
	
	public Bitmap getPlurkerAvatar(final int uuid) {
		ResourceProxy<Bitmap> rp = new ResourceProxy<Bitmap>() {

			@Override
			public String getIndentificator() {
				// TODO Auto-generated method stub
				return "" + uuid;
			}
		};
		
		rp.addProvisioner(mUserAvatarCacheMedium);
		rp.addProvisioner(mUserAvatarFileSystemCacheMedium);
		
		return rp.getResource(null);
	}
	
	public ArrayList<Bitmap> getInPlurkAvatars(PlurksUsers users) {
		HashMap<Integer, PlurkUser> map = users.getUserMap();
		ArrayList<Bitmap> ret = new ArrayList<Bitmap>();
		
		for(final Entry<Integer, PlurkUser> e : map.entrySet()) {
			ResourceProxy<Bitmap> rp = new ResourceProxy<Bitmap>(){

				@Override
				public String getIndentificator() {
					// TODO Auto-generated method stub
					return "" + e.getKey();
				}};
				
			rp.addProvisioner(mUserAvatarCacheMedium);
			rp.addProvisioner(mUserAvatarFileSystemCacheMedium);
			rp.addProvisioner(new InternetResourceProvisioner<Bitmap>(){

				@Override
				public Bitmap formFromStream(InputStream is) {
					return BitmapFactory.decodeStream(is);
				}

				@Override
				public String getUrlAddress(String identificator) {
					return createAvatarUrlMedium(e.getValue());
				}});
			ret.add(rp.getResource(null));
		}
		
		return ret;

	}

	@Override
	public boolean logout() {
		PostObject.setAPIParams("api_key", API_KEY);
		PostObject obj = new PostObject("Users/logout");
		HttpGet get = new HttpGet(MAIN_URL + obj.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		HttpClient client = new DefaultHttpClient();

		try {
			client.execute(get, handler);
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

//	@Override
//	public PublicUserInfo getPublicProfile(final int uid) {
//		return null;
//	}

	@Override
	public UserInfo getOwnProfile() {
		PostObject obj = new PostObject("Profile/getOwnProfile");
		JSONObject json = null;

		String result = null;

		HttpGet get = new HttpGet(obj.toString());
		Log.d("subplurk", "attemping connect to : " + obj.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpClient client = new DefaultHttpClient();

			//mClient.execute(get, handler);
		try {
			result = client.execute(get, handler);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		Log.d("subplurk", "ret : " + result);
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mUserInfo = new UserInfo(json.getJSONObject("user_info"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mUserInfo;

	}



	@Override
	public ArrayList<PlurkScrap> getResponses(int plurk_id, int offset) {
		PostObject post = new PostObject("Responses/get", "plurk_id", "" + plurk_id, "from_response", offset == 0 ? "" : "" + offset);
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> res = new BasicResponseHandler();
		ArrayList<PlurkScrap> retArray = new ArrayList<PlurkScrap>();
		
		HttpClient client = new DefaultHttpClient();
		
		try {
			String ret = client.execute(get, res);
			JSONObject json = new JSONObject(ret);
			
			JSONArray arr = json.getJSONArray("responses");
			for(int counter = 0; counter < arr.length(); ++counter)
				retArray.add(new PlurkScrap((JSONObject)arr.get(counter)));
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return retArray;
	}

	//最好別用這東西
	@Override
	public JSONObject getPlurks(String offset, String string, String filter) {
		PostObject post = new PostObject("Timeline/getPlurks", "offset", offset, "limit", "" + string, "filter", filter);
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> res = new BasicResponseHandler();
		
		HttpClient client = new DefaultHttpClient();

		JSONObject json = null;
		try {
			String ret = client.execute(get, res);
			json = new JSONObject(ret);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}

	
	
	public UserInfo getUserInfo() {
		return mUserInfo;
	}
	
	public enum AvatarSize {
		SMALL,
		MEDIUM,
		BIG
	}
	
	public Bitmap getUserAvatar(AvatarSize size) {
		String url = null;
		
		switch(size) {
		case SMALL:
			url = mUserInfo.createAvatarUrlSmall();
			break;
		case MEDIUM:
			url = mUserInfo.createAvatarUrlMedium();
			break;
		case BIG:
			url = mUserInfo.createAvatarUrlBig();
			break;
		}
		
		final String targetUrl = url;
				
		ResourceProxy<Bitmap> rp = new ResourceProxy<Bitmap>() {

			@Override
			public String getIndentificator() {
				// TODO Auto-generated method stub
				return targetUrl;
			}
		};
		
		rp.addProvisioner(new FileSystemResourceProvisioner<Bitmap>("./sdcard/.SubPlurk/cache/userAvatar"){

			@Override
			public Bitmap formFromStream(InputStream in) {
				return BitmapFactory.decodeStream(in);
			}

			@Override
			public void writeToOutputStream(Bitmap target, FileOutputStream fo) {
				target.compress(CompressFormat.PNG, 100, fo);
			}});
		
		rp.addProvisioner(new InternetResourceProvisioner<Bitmap>(){

			@Override
			public Bitmap formFromStream(InputStream is) {
				return BitmapFactory.decodeStream(is);
			}

			@Override
			public String getUrlAddress(String identificator) {
				return targetUrl;
			}});
		
		return rp.getResource(null);
	}
	
	/**
	 * Get url 
	 * @param user
	 * @return
	 */
	public String createAvatarUrlBig(PlurkUser user) {
		if(user.has_profile_image == 0)
			return "http://www.plurk.com/static/default_big.gif";
		
		if(user.avatar.equals(-1) || user.avatar.equals(0))
			return "http://avatars.plurk.com/" + user.id + "-big.jpg";
		
		return "http://avatars.plurk.com/" + user.id + "-big" + user.avatar + ".jpg";
	}
	
	public String createAvatarUrlMedium(PlurkUser user) {
		if(user.has_profile_image == 0)
			return "http://www.plurk.com/static/default_medium.gif";
		
		if(user.avatar.equals(-1) || user.avatar.equals(0))
			return "http://avatars.plurk.com/" + user.id + "-medium.gif";
		
		return "http://avatars.plurk.com/" + user.id + "-medium" + user.avatar + ".gif";
	}
	
	public String createAvatarUrlSmall(PlurkUser user) {
		if(user.has_profile_image == 0)
			return "http://www.plurk.com/static/default_small.gif";
		
		if(user.avatar.equals(-1) || user.avatar.equals(0))
			return "http://avatars.plurk.com/" + user.id + "-small.gif";
		
		return "http://avatars.plurk.com/" + user.id + "-small" + user.avatar + ".gif";
	}



}
