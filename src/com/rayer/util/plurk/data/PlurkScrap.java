package com.rayer.util.plurk.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rayer.util.databridge.DebugBridge;
import com.rayer.util.databridge.JSONConverter;

/**
 * PlurkScrap
 * @author rayer
 *
 */
public class PlurkScrap {
	public Integer 	plurk_id;
	public String 	qualifier;
	public String 	qualifier_translated;
	public Integer 	is_unread;
	public Integer 	plurk_type;
	public Integer 	user_id;
	public Integer 	owner_id;
	public String 	posted;
	public Integer 	no_comments;
	public String 	content;
	public String 	content_raw;
	public Integer	response_count;
	public Integer	response_seen;
	
	protected JSONObject limited_to;
	
	public PlurkScrap(JSONObject obj) {
		JSONConverter.extractFromJSON(PlurkScrap.class, this, obj);
		try {
			limited_to = obj.getJSONObject("limited_to");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public String toString() {
		return DebugBridge.attachDebugInfo(PlurkScrap.class, this);
	}
	
	static public ArrayList<PlurkScrap> createPlurkArray(JSONArray obj) throws JSONException {
		ArrayList<PlurkScrap> ret = new ArrayList<PlurkScrap>();
		for(int counter = 0; counter < obj.length(); ++counter) {
			PlurkScrap scrap = new PlurkScrap((JSONObject)obj.get(counter));
			ret.add(scrap);
		}
		return ret;
	}
	
}
