package com.rayer.util.databridge;

import java.lang.reflect.Field;

public class DebugBridge {
	
	public static String attachDebugInfo(Class<?> targetClass, Object targetObj) {
		Field[] fields = targetClass.getFields();
		
		StringBuilder sb = new StringBuilder();
		for(Field f : fields) {
			try {
				sb.append(f.getName() + " = " + f.get(targetObj) + " ");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
			
	}

}
