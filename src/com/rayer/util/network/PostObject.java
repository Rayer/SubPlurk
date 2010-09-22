package com.rayer.util.network;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class PostObject {
	
	HashMap<String, String> mPostObjectMap = new HashMap<String, String>();
	static String APIKEY_LABEL = "";
	static String APIKEY_VALUE = "";
	
	public static void setAPIParams(String label, String value) {
		APIKEY_LABEL = label;
		APIKEY_VALUE = value;
	}
	
	String mPrefix = "";
	/**
	 * Create a PostObject object, String will be pair of [parameters, value]
	 * A rather dangerous way, but due to convenience...
	 * @param paramList Odd(1 3 5 7 ...) order will be Parameters, and even(2 4 6 8 ...) order will be value.
	 */
	public PostObject(String prefix, String... paramList) {
		if(paramList.length == 0 || paramList.length % 2 != 0)
			throw new RuntimeException("PostObject's paramList length must be even, and greater then 0");
		
		if(APIKEY_LABEL.equals("") == false && APIKEY_VALUE.equals("") == false)
			mPostObjectMap.put(APIKEY_LABEL, APIKEY_VALUE);	
		
		boolean orderIsParam = true;
		String buffer = null;
		for(String s : paramList) {
			if(orderIsParam) {
				buffer = s;
				if(mPostObjectMap.containsKey(s))
					throw new RuntimeException("dupicated keys found");
			}
			else
				mPostObjectMap.put(buffer, s);
			
			orderIsParam = !orderIsParam;
			mPrefix = prefix;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(mPrefix);
		sb.append("?");
		boolean first = true;
		Set<Entry<String, String> > set = mPostObjectMap.entrySet();
		for(Entry<String, String> e : set ) {
			if(first == false)
				sb.append("&");
			sb.append(e.getKey());
			sb.append("=");
			sb.append(e.getValue());
			first = false;
		}
		
		return sb.toString();
	}
	
	/**
	 * use to validate if this object contains all of necessary components that strarray specified. 
	 * @param obj
	 * @param str
	 * @return
	 */
	static public boolean validateKeys(PostObject obj, String[] str) {
		return false;
	}

}
