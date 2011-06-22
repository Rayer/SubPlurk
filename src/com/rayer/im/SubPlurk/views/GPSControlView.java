package com.rayer.im.SubPlurk.views;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.SystemManager;
import com.rayer.util.event.EventManager;
import com.rayer.util.event.EventProcessHandler;
import com.rayer.util.gps.LocationService;
import com.rayer.util.gps.LocationService.RequestAddressListener;
import com.rayer.util.gps.OnLocationServiceInit;
import com.rayer.util.gps.OnLocationUpdate;
import com.rayer.views.SparklingView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GPSControlView extends RelativeLayout {
	
	TextView mPosition_tv;
	TextView mAddress_tv;
	SparklingView mStatus_sv;
	Button mPostLocation_btn;
	Button mPostAddress_btn;
	MapView mMapView;
	
	MapController mMapController;
	
	EventManager mEm = SystemManager.getInst();
	
	Bitmap mGPSStatusOK;
	Bitmap mGPSStatusFailed;
	LocationService mLs;
	
	
	EventProcessHandler mHandler = new EventProcessHandler() {
		
		static final int ON_OBTAINING_ADDRESS = 1;

		@Override
		public void handleMessage(Message msg) {
			int identificator = msg.what;
			
			if(identificator == OnLocationUpdate.class.hashCode())
				processLocationUpdate(msg);
			
			if(identificator == OnLocationServiceInit.class.hashCode())
				processServiceUp(msg);
			
			if(identificator == ON_OBTAINING_ADDRESS)
				processDisplayAddress(msg);
				
			super.handleMessage(msg);
		}

		private void processDisplayAddress(Message msg) {
			mAddress_tv.setText((String)msg.obj);
			
		}

		private void processServiceUp(Message msg) {
			if(msg.arg1 == OnLocationServiceInit.INITIALIZATION_WITH_GPS)
				setIndicatorOnline(true);
		}

		private void processLocationUpdate(Message msg) {
			
			GeoPoint gp = new GeoPoint(msg.arg1, msg.arg2);
			mPosition_tv.setText(generatePositionString(gp));
			//mPosition_tv.setText(SystemManager.getInst().getLocationService().getCurrentGeoCoding());
			mLs.requestAddress(new RequestAddressListener(){

				@Override
				public void onAddressFetched(String address) {
					Message msg = obtainMessage();
					msg.what = ON_OBTAINING_ADDRESS;
					msg.obj = address;
					sendMessage(msg);
				}});
			
		}
		
		
	};

	public GPSControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	protected void setIndicatorOnline(boolean b) {
		mStatus_sv.setSparklingBitmaps(b ? mGPSStatusOK : mGPSStatusFailed, null);
		mStatus_sv.setIndicatorText(b ? "GPS Initialize succeed!" : "Waiting for GPS Respond...");
		mStatus_sv.startSparkling();
		
	}

	private void initialize() {
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.gps_control_view, this);
		
		mEm.registerHandler(OnLocationUpdate.class, mHandler);
		mEm.registerHandler(OnLocationServiceInit.class, mHandler);
		
		mLs = SystemManager.getInst().getLocationService();
		
		mPosition_tv = (TextView) findViewById(R.id.gps_control_location_tv);
		mStatus_sv = (SparklingView) findViewById(R.id.gps_control_status_sv);
		mAddress_tv = (TextView) findViewById(R.id.gps_control_address_tv);
		mPostLocation_btn = (Button) findViewById(R.id.gps_control_postlocation_btn);
		mPostAddress_btn = (Button) findViewById(R.id.gps_control_postaddress_btn);
		mMapView = (MapView) findViewById(R.id.gps_control_mapview);
		mMapController = mMapView.getController();
		
		//mPosition_ts.setText("Position determining...");
		
		mGPSStatusOK = BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.arrow_up_float);
		mGPSStatusFailed = BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.arrow_down_float);
		
		setIndicatorOnline(false);
		
		SystemManager.getInst().confirmComponents(getContext());
		//initialize map control
		mMapController.animateTo(mLs.getLocation());
		mMapController.setZoom(16);
		
		MapView.LayoutParams mlp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, mLs.getLocation(), 1);
		
	}
	
	private String generatePositionString(GeoPoint gp) {
		StringBuilder sb = new StringBuilder();
		sb.append("Pos : ");
		sb.append((double)gp.getLatitudeE6() / 1E6);
		sb.append(" / ");
		sb.append((double)gp.getLongitudeE6() / 1E6);
		
		return sb.toString();
	}
	
	
	

}
