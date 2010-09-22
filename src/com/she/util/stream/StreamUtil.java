package com.she.util.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StreamUtil {
	
	/**
	 * Code was gotten from http://www.kodejava.org/examples/266.html
	 * @param is InputStream which needed to be converted
	 * @return String convert from InputStream, or "" if error occurs.
	 * @throws IOException throw by BufferedReader.readLine
	 */
	public static String InputStreamToString(InputStream is) throws IOException {
		/*
		* To convert the InputStream to String we use the BufferedReader.readLine()
		* method. We iterate until the BufferedReader return null which means
		* there's no more data to read. Each line will appended to a StringBuilder
		* and returned as String.
		*/
			
		if (is != null) {
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
			}
			} finally {
					is.close();
			}
			return sb.toString();
			} else {        
				return "";
			}
		}
	

	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
	    int len;

	    while((len = in.read(buffer)) >= 0)
	      out.write(buffer, 0, len);

	    in.close();
	    out.close();
	    
		}
}
