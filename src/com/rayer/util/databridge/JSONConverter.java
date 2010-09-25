package com.rayer.util.databridge;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONConverter {
	
	public static void extractFromJSON(Class<?> targetClass, Object targetObject, JSONObject userObject) {
		Field[] fields = targetClass.getFields();
		for(Field f : fields) {
			Log.d("SubPlurk", "getting info : " + f.getName()); 
			try {
				f.set(targetObject, userObject.get(f.getName()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
}
