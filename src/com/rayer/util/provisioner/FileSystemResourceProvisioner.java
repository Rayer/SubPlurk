package com.rayer.util.provisioner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

public abstract class FileSystemResourceProvisioner<T> implements ResourceProvisioner<T> {

	String mCacheDir;
	
	public FileSystemResourceProvisioner(String cacheDir) {
		
		mCacheDir = cacheDir;
		if(mCacheDir.endsWith("/") == false)
			mCacheDir = mCacheDir + "/";

	}
	@Override
	public T getResource(String identificator) {
		File parent = new File(mCacheDir);
		if(parent.exists() == false) {
			Log.d("hamibook2", "Attemp to create subdirectory :" + mCacheDir + " result : " + parent.mkdirs());
			parent.mkdirs();
		}
		InputStream targetInputStream = null;
		
		File f = new File(mCacheDir + identificator);
		if(f.exists()) {
			T ret;
			try {
				targetInputStream = new FileInputStream(f);
				ret = formFromStream(targetInputStream);
				targetInputStream.close();
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {				
				e.printStackTrace();
				return null;
			}
			return ret;
		}
		
		return null;
	}
	
	@Override
	public boolean dereferenceResource(String identificator) {
		//do nothing
		return true;
	}
	
	public String getRootDir() {
		return mCacheDir;
	}
	
	public abstract T formFromStream(InputStream in);

}
