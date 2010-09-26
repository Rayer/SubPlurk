package com.rayer.util.provisioner;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ResourceProxy<T> {
	boolean mForceUpdate = false;
	
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
	 * Toggles force update. Force update means use only LAST provisioner as resource and attempt to write it into previous provisioners.
	 * @param forceUpdate
	 */
	public void setForceUpdate(boolean forceUpdate) {
		mForceUpdate = forceUpdate;
	}
	
	/**
	 * Get resource via blocking method.
	 * @param listener as its name, usually not useful while in non-async mode, but may have some use in special case. Can be null.
	 * @return Resource
	 */
	public T getResource(ResourceProxyListener<T> listener) {
		mWaitToWriteList.clear();
		
		ResourceProvisioner<T> lastProvisioner = mProvisionerList.get(mProvisionerList.size() - 1);
		
		T target = null;
		Iterator<ResourceProvisioner<T> > itor = mProvisionerList.iterator();
		while(itor.hasNext()) {
			ResourceProvisioner<T> targetProvisioner = itor.next();
			//Force Update機制調整 在Force Update下 只會取最後一個Provisioner並且把他寫入前面所有的Provisioner
			if(mForceUpdate == false || targetProvisioner != lastProvisioner)
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



