package com.rayer.util.provisioner;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ResourceProxy<T> {
	ArrayList<ResourceProvisioner<T> > mProvisionerList = new ArrayList<ResourceProvisioner<T> >();
	ArrayList<ResourceProvisioner<T> > mWaitToWriteList = new ArrayList<ResourceProvisioner<T> >();
	/**
	 * Add a provisioner. The proxy will ORDERLLY try to obtain resource from these provisioners.<br>
	 * Be careful these provisioner will NOT check its existence before add into the proxy.
	 * @param provisioner root is ResourceProvisioner<T>. And there is some frequency used provisioner such like FileSystemResourceProvisioner and InternetResourceProvisioner 
	 * @return now provisioner count. 
	 */
	public int addProvisioner(ResourceProvisioner<T> provisioner) {
		if(provisioner != null)
			mProvisionerList.add(provisioner);
		return mProvisionerList.size();
	}
	
	/**
	 * Get resource via blocking method.
	 * @param listener as its name, usually not useful while in non-async mode, but may have some use in special case. Can be null.
	 * @return Resource
	 */
	public T getResource(ResourceProxyListener<T> listener) {
		mWaitToWriteList.clear();
		
		T target = null;
		Iterator<ResourceProvisioner<T> > itor = mProvisionerList.iterator();
		while(itor.hasNext()) {
			ResourceProvisioner<T> targetProvisioner = itor.next();
			target = targetProvisioner.getResource(getIndentificator());
			
			if(target != null) {
				for(ResourceProvisioner<T> r : mWaitToWriteList)
					r.setResource(getIndentificator(), target);
				break;
			}
			
			mWaitToWriteList.add(targetProvisioner);
		}
		return target;
	}
	
	/**
	 * Get resource via non-blocking(asynchronized) method
	 * @param listener listener as its name mentioned.
	 */
	public void getResourceAsync(final ResourceProxyListener<T> listener) {
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				T target = getResource(listener);// TODO Auto-generated method stub
				listener.onFinishedLoading(target);
			}});
		t.run();
		
	}
	
	public abstract String getIndentificator();
	
	public interface ResourceProxyListener<T> {
		void onNotifyCacheAvailible(boolean isCacheAvailible);
		void onNotifyCacheDownloadCompleted();
		void onNotifyErrorOccures(Exception e);
		void onFinishedLoading(T t);
	}
}


//public abstract class ResourceProxy<T> {
//
//	String mCacheDir;
//	String mCacheName;
//	String mUrl;
//	
//	//FYI
//	ResourceProvisioner<T> mExtraResourceProvisioner;
//	
//	public ResourceProxy() {
//		mCacheDir = getCacheDir();
//		mCacheName = getCacheName();
//		mUrl = getUrl();
//	}
//	public abstract String getCacheDir();
//	public abstract String getCacheName();
//	public abstract String getUrl();
//	public abstract ResourceProvisioner<T> getExtraProvisioner();
//	
//	public T getResource() {
//		if(getExtraProvisioner() != null) {
//			T ret = getExtraProvisioner().getResource(getCacheName());
//			if(ret != null)
//				return ret;
//		}
//			
//		InputStream targetInputStream = selectTargetStreamFromDefaultSource(null);
//		return createFromStream(targetInputStream);
//	}
//	
//	InputStream selectTargetStreamFromDefaultSource(ResourceProxyListener<T> listener) {
//		//先檢查parent cache dir有沒有
//		File parent = new File(mCacheDir);
//		if(parent.exists() == false)
//			parent.mkdirs();
//		//首先先去檔案尋找一下有沒有這東西
//		InputStream targetInputStream = null;
//		
//		if(mCacheDir.endsWith("/") == false)
//			mCacheDir = mCacheDir + "/";
//		File f = new File(mCacheDir + mCacheName);
//		try {
//			boolean exists = f.exists();
//			if(listener != null)
//				listener.onNotifyCacheAvailible(exists);
//			if(exists)
//				targetInputStream = new FileInputStream(f);
//			else {
//				URLConnection conn = new URL(mUrl).openConnection();
//				targetInputStream = conn.getInputStream();
//				
//				f.createNewFile();
//				FileOutputStream fo = new FileOutputStream(f);
//				byte[] buffer = new byte[4096];
//				int len;
//				while((len = targetInputStream.read(buffer)) > 0)
//					fo.write(buffer, 0, len);
//				
//				fo.close();
//				
//				if(listener != null)
//					listener.onNotifyCacheDownloadCompleted();
//				
//				targetInputStream = new FileInputStream(f);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			if(listener != null)
//				listener.onNotifyErrorOccures(e);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			if(listener != null)
//				listener.onNotifyErrorOccures(e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			if(listener != null)
//				listener.onNotifyErrorOccures(e);
//		}
//		return targetInputStream;
//	}
//	
//	/**
//	 * Define how to create a target object from a stream (mandatory). WARNING! InputStream should .close() manually!
//	 * @param is source input stream, the source is vary.
//	 * @return Object which form from stream
//	 */
//	public abstract T createFromStream(InputStream is);
//	
//	public boolean getResourceAsync(final ResourceProxyListener<T> listener) {
//		Thread t = new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				InputStream targetInputStream = selectTargetStreamFromDefaultSource(listener);
//				listener.onFinishedLoading(createFromStream(targetInputStream));
//				
//			}});
//		t.run();
//		return true;
//	}
//	
//
//
//	//以下未實作
//	public void getResourceAsync(Handler inNotificationHandler) {
//		return;
//		
//	}
//	
//
//	
//	
//	public interface ResourceProxyListener<T> {
//		void onNotifyCacheAvailible(boolean isCacheAvailible);
//		void onNotifyCacheDownloadCompleted();
//		void onNotifyErrorOccures(Exception e);
//		void onFinishedLoading(T t);
//	}
//	
//	/**
//	 * 
//	 * @author rayer
//	 *
//	 */
//	public class ResourceProvisionerParameters {
//		ResourceProvisionerParameters(ResourceProvisioner<?> provisioner, boolean writeIntoThisProvisioner){}
//		
//	}
//	
//}
