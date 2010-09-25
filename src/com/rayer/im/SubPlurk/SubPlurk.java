package com.rayer.im.SubPlurk;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rayer.im.SubPlurk.views.PlurkScrapView;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.plurk.PlurkController;
import com.rayer.util.plurk.data.PlurkScrap;
import com.rayer.util.plurk.data.UserInfo;
import com.she.util.stream.PatchInputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubPlurk extends Activity {
	
    EventProcessHandler mHandler = new EventProcessHandler(){};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        
        ImageView avatar_iv = (ImageView) findViewById(R.id.user_info_avatar_iv);
        TextView id_tv = (TextView) findViewById(R.id.user_info_id_tv);
        TextView nickname_tv = (TextView) findViewById(R.id.user_info_nickname_tv);
        TextView realname_tv = (TextView) findViewById(R.id.user_info_fullname_tv);
        TextView karma_tv = (TextView) findViewById(R.id.user_info_karma_tv);
        TextView detail_tv = (TextView) findViewById(R.id.user_info_jsdetail_tv);
        
        PlurkController con = SystemManager.getInst().getPlurkController();
        JSONObject JSONObj = null;
		try {
			JSONObj = con.login("killercat", "2jriojdi");
		} catch (ClientProtocolException e) {
			Log.d("SubPlurk", "CPE exception");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("SubPlurk", "IO exception");
			e.printStackTrace();
		} 
        //obj.toString();
        //tv.setText(obj.toString());
        UserInfo user = null;
        JSONObject userinfo = null;
        try {
			userinfo = JSONObj.getJSONObject("user_info");
			user = new UserInfo(userinfo);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		URL url;
		PatchInputStream avatarStream = null;


		try {
			url = new URL(user.createAvatarUrlBig());
			
			avatarStream = new PatchInputStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Bitmap avatarBM = BitmapFactory.decodeStream(avatarStream);
		
		avatar_iv.setImageBitmap(avatarBM);
		id_tv.setText(user.getID().toString());
		nickname_tv.setText(user.getNickName());
		realname_tv.setText(user.getRealName());
		karma_tv.setText(user.getKarma().toString());
		//detail_tv.setText(JSONObj.toString());
		
		JSONArray arr = null;
		try {
			arr = JSONObj.getJSONArray("plurks");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//detail_tv.setText(arr.toString());
		
		HashMap<Integer, PlurkScrap> list = new HashMap<Integer, PlurkScrap>();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_layout);
		for(int i = 0; i < arr.length(); ++i) {
			PlurkScrap unit = null;
			try {
				unit = new PlurkScrap((JSONObject) arr.get(i));
				PlurkScrapView view = new PlurkScrapView(this, unit);
				layout.addView(view);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.put(unit.plurk_id, unit);
		}
		
		StringBuilder sb = new StringBuilder();
		for(Entry<Integer, PlurkScrap> e : list.entrySet())
			sb.append(e.getValue().toString());
		

			
		

    }
}