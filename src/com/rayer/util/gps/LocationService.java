package com.rayer.util.gps;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.rayer.util.event.EventManagerInterface;
import com.rayer.util.gps.OnLocationUpdate.UpdateType;

public class LocationService implements LocationListener{
	private static final String PREF_LAST_USE_AUTO = "is_use_auto";
	static final String PREF_LAST_LONGTITUDE_1E6 = "last_longtitude_1e6";
	static final String PREF_LAST_LATITUDE_1E6 = "last_latitude_1e6";
	static final String PREF_LAST_POSITION = "last_position";
	
	//Default location : Taipei Main Station
	static final int DEF_POSITION_LATITUDE1E6 = 25045925;
	static final int DEF_POSITION_LONGTITUDE1E6 = 121577700;
	
	
	static LocationService msLocService;
	public static LocationService getInstance()
	{
		if(msLocService == null)
			msLocService = new LocationService();
		
		return msLocService;
	}
	
	
	static final int LOCATION_MIN_DIST_UPDATE = 5;
	
	LocationManager mLocMgr;
	Context mContext;
	EventManagerInterface mEm;
	GpsStatus mGpsStatus;
	/**
	 * 
	 * @param context Application Context
	 * @param em EventManager, if set to null, it will never send any message via EventManager
	 * @return
	 */
	public boolean init(Context context, EventManagerInterface em)
	{
		mContext = context;
		mEm = em;
		mLocMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		if(mEm != null)
			mEm.sendMessage(new OnLocationServiceInit(OnLocationServiceInit.START_INITIALIZATION));
		
		if(mLocMgr == null)
		{
			if(mEm != null)
				mEm.sendMessage(new OnLocationServiceInit(OnLocationServiceInit.INITIALIZATION_FAILED));
			
			createNoGPSWarningToast();
			return false;
		}
		
		getManualSetPreference();
		
		if(mUseAutoPosition == true)
			return enableService();
		
		return true;
	}
	
	boolean mUseAutoPosition = true;
	boolean mGPSStatus;
	Location mAutoPosition;
	GeoPoint mManualSetPosition;
	boolean mIsFirstUpdate = true;
	
	
	public void setUseAutoPosition(boolean useAuto)
	{
		
		if(useAuto == true)
		{
			mUseAutoPosition = true;
			SharedPreferences setting = mContext.getSharedPreferences(PREF_LAST_POSITION, 0);
			setting.edit()
				.putBoolean(PREF_LAST_USE_AUTO, mUseAutoPosition)
				.putInt(PREF_LAST_LATITUDE_1E6, mManualSetPosition.getLatitudeE6())
				.putInt(PREF_LAST_LONGTITUDE_1E6, mManualSetPosition.getLongitudeE6())
				.commit();
			
			Log.d("LocationService", "Use auto position : true saved");
			enableService();
		}
		
		mUseAutoPosition = useAuto;
	}
	
	public GeoPoint getAutoSetPosition()
	{
		GeoPoint ret = null;
		if(mAutoPosition == null)
		{
			//GeoPoint man = getManualSetPreference();
			//ret = new GeoPoint(man.getLatitudeE6(), man.getLongitudeE6());
			//createNoGPSWarningToast();
			
			Location lastKnown = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			ret = new GeoPoint((int)(lastKnown.getLatitude() * 1E6), (int)(lastKnown.getLongitude() * 1E6));
		}
		else
			ret = new GeoPoint((int)(mAutoPosition.getLatitude() * 1E6), (int)(mAutoPosition.getLongitude() * 1E6));
		return ret;
	}
	
	public void setManualSetPosition(GeoPoint inPos)
	{
		mManualSetPosition = inPos;
		SharedPreferences setting = mContext.getSharedPreferences(PREF_LAST_POSITION, 0);
		setting.edit()
			.putBoolean(PREF_LAST_USE_AUTO, mUseAutoPosition)
			.putInt(PREF_LAST_LATITUDE_1E6, mManualSetPosition.getLatitudeE6())
			.putInt(PREF_LAST_LONGTITUDE_1E6, mManualSetPosition.getLongitudeE6())
			.commit();
	}
	
	public boolean isUsingAutoPosition()
	{
		return mUseAutoPosition;
	}
	
	public GeoPoint getManualSetPreference()
	{
		if(mManualSetPosition != null)
			return mManualSetPosition;
		
		SharedPreferences settings = mContext.getSharedPreferences(PREF_LAST_POSITION, 0);
		int lastLatitude = settings.getInt(PREF_LAST_LATITUDE_1E6, DEF_POSITION_LATITUDE1E6);
		int lastLongtitude = settings.getInt(PREF_LAST_LONGTITUDE_1E6, DEF_POSITION_LONGTITUDE1E6);
		mManualSetPosition = new GeoPoint(lastLatitude, lastLongtitude);
		mUseAutoPosition = settings.getBoolean(PREF_LAST_USE_AUTO, true);
		return mManualSetPosition;
	}
	
	public GeoPoint getLocation()
	{
		
		return mUseAutoPosition ? 
				getAutoSetPosition() : 
				getManualSetPreference();
	}
	
	public double getLatitudeDouble()
	{
		Log.d("LocationService", "feeding Latitude : " + (getLocation().getLatitudeE6()) / (double)1E6);
		return (getLocation().getLatitudeE6()) / (double)1E6; 
	}
	
	public double getLongtitudeDouble()
	{
		Log.d("LocationService", "feeding Longitude : " + (getLocation().getLongitudeE6()) / (double)1E6);
		return (getLocation().getLongitudeE6()) / (double)1E6;
	}
	
	public boolean enableService()
	{
//		if(mLocMgr == null)
//			processNoGPSDevice();
			
		mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
		mAutoPosition = mLocMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(mAutoPosition == null)
			mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(mAutoPosition == null && mUseAutoPosition == true)
		{
			this.setUseAutoPosition(false);
			return false;
		}
		
		return true;
	}

	public void disableService()
	{
		mLocMgr.removeUpdates(this);
	}
	
	private void createNoGPSWarningToast() {
		LinearLayout layout = new LinearLayout(mContext);
		ImageView iv = new ImageView(mContext);
		iv.setImageResource(android.R.drawable.stat_notify_error);
		
		TextView tv = new TextView(mContext);
		tv.setText("No GPS detected.");
		
		layout.addView(iv);
		layout.addView(tv);
		
		Toast toast = new Toast(mContext);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
	
//	public String getCurrentGeoCoding() {
//		Geocoder gc = new Geocoder(mContext);
//		String ret = "No address found!";
//		try {
//			Address addr = (gc.getFromLocation(mAutoPosition.getLatitude(), mAutoPosition.getLongitude(), 1)).get(0);
//			ret = addr.getThoroughfare();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return ret;
//	}

	//Location Listener
	@Override
	final public void onLocationChanged(Location location) {
		if(mIsFirstUpdate == true && mEm != null)
			mEm.sendMessage(new OnLocationServiceInit(OnLocationServiceInit.INITIALIZATION_WITH_GPS));

		mIsFirstUpdate = false;
		mAutoPosition = location;
		
		if(mEm != null)
			mEm.sendMessage(new OnLocationUpdate(UpdateType.GPS, (int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6)));
		
		
	}

	@Override
	final public void onProviderDisabled(String provider) {

		
	}

	@Override
	final public void onProviderEnabled(String provider) {
		
	}

	@Override
	final public void onStatusChanged(String provider, int status, Bundle extras) {
		if(mGpsStatus == null)
			mGpsStatus = mLocMgr.getGpsStatus(null);
		else
			mLocMgr.getGpsStatus(mGpsStatus);
		
		mEm.sendMessage(new OnGPSStatusChanged(mGpsStatus));
		
	}

	Thread mAddressRequestThread = null;
	public void requestAddress(final RequestAddressListener requestAddressListener) {
		
		//不需要request那麼多次, 只要request最新的結果就可以了
		if(mAddressRequestThread != null)
			return;
		
		mAddressRequestThread = new Thread(){

			//need to set a timeout time?
			@Override
			public void run() {
				Geocoder gc = new Geocoder(mContext);
				String ret = new String();
				try {
					Address addr = (gc.getFromLocation(mAutoPosition.getLatitude(), mAutoPosition.getLongitude(), 1)).get(0);
					int addressLineCount = addr.getMaxAddressLineIndex();
					if(addressLineCount >= 0)
						for(int counter = 0; counter < addressLineCount; ++counter)
							ret += addr.getAddressLine(counter) + " ";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				requestAddressListener.onAddressFetched(ret);
				mAddressRequestThread = null;
				
				super.run();
			}};
			
			mAddressRequestThread.run();
	}
	
	public interface RequestAddressListener {
		void onAddressFetched(String address);
	}
	
	
}

