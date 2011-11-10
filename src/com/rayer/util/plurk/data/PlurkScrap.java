package com.rayer.util.plurk.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rayer.util.databridge.DebugBridge;
import com.rayer.util.databridge.JSONConverter;

/**
 * PlurkScrap
 * @author rayer
 *
 */

@DatabaseTable(tableName="plurkScrap")
public class PlurkScrap {
	@DatabaseField(id=true)
	public Integer 	plurk_id;
	@DatabaseField
	public String 	qualifier;
	@DatabaseField
	public String 	qualifier_translated;
	@DatabaseField
	public Integer 	is_unread;
	@DatabaseField
	public Integer 	plurk_type;
	@DatabaseField
	public Integer 	user_id;
	@DatabaseField
	public Integer 	owner_id;
	@DatabaseField
	public String 	posted;
	@DatabaseField
	public Integer 	no_comments;
	@DatabaseField
	public String 	content;
	@DatabaseField
	public String 	content_raw;
	@DatabaseField
	public Integer	response_count;
	@DatabaseField
	public Integer	response_seen;
	@DatabaseField
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
