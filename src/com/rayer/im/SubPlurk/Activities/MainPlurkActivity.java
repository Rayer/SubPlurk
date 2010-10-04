package com.rayer.im.SubPlurk.Activities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.views.PlurkScrapView;
import com.rayer.util.plurk.data.PlurkScrap;
import com.rayer.util.plurk.data.UserInfo;
import com.she.util.stream.PatchInputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainPlurkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        
        ImageView avatar_iv = (ImageView) findViewById(R.id.user_info_avatar_iv);
        TextView id_tv = (TextView) findViewById(R.id.user_info_id_tv);
        TextView nickname_tv = (TextView) findViewById(R.id.user_info_nickname_tv);
        TextView realname_tv = (TextView) findViewById(R.id.user_info_fullname_tv);
        TextView karma_tv = (TextView) findViewById(R.id.user_info_karma_tv);
        ListView plurk_scrap_lv = (ListView) findViewById(R.id.plurk_scarp_list_lv);
        
        Bundle bundle = getIntent().getExtras();
        String loginData = bundle.getString("login_data");
        JSONObject JSONObj = null;
		try {
			JSONObj = new JSONObject(loginData);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

		//final HashMap<Integer, PlurkScrap> list = new HashMap<Integer, PlurkScrap>();
		final ArrayList<PlurkScrap> list = new ArrayList<PlurkScrap>();

		for(int i = 0; i < arr.length(); ++i) {
			PlurkScrap unit = null;
			try {
				unit = new PlurkScrap((JSONObject) arr.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(unit);
		}

		new AlertDialog.Builder(this).setTitle("Login api response info : ").setMessage(JSONObj.toString()).show();



		plurk_scrap_lv.setAdapter(new BaseAdapter(){

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				//return new PlurkScrapView(MainPlurkActivity.this, list.get(position));
				if(convertView != null)
					return ((PlurkScrapView)convertView).initFromScrap(list.get(position));
				
				return new PlurkScrapView(MainPlurkActivity.this, list.get(position));

				
			}});		


    }


}
