package com.rayer.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class SparklingView extends LinearLayout {
	
	public SparklingView(Context context) {
		super(context);
		initialize();
	}
	
	public SparklingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	//what
	static final int SPARKLING_CHANGE_COLOR = 0;
	
	//arg1
	static final int SPARKLING_BITMAP_FIRST = 1;
	static final int SPARKLING_BITMAP_SECOND = 2;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SPARKLING_CHANGE_COLOR) {
				mSparklingImageIV.setImageBitmap(msg.arg1 == SPARKLING_BITMAP_FIRST ? mSparklingImage1 : mSparklingImage2);
			}
			super.handleMessage(msg);
		}};

	ImageView mSparklingImageIV;
	TextView mDescriptionHolder;
	
	ControlThread mMaintainingThread;
	
	int mSparklingTimer = 1000; //1000ms
	Bitmap mSparklingImage1; //其實應該放兩個就夠了
	Bitmap mSparklingImage2;
	
	int mSparklingImageSizeWidth = 20; //in px
	int mSparklingImageSizeHeight = 20; // in px
	boolean mTicker = true;
	
	
	void initialize() {
		mSparklingImageIV = new ImageView(getContext());
		mDescriptionHolder = new TextView(getContext());
		
		LayoutParams imageLayoutParams = new LayoutParams(mSparklingImageSizeWidth, mSparklingImageSizeHeight);
		//imageLayoutParams.addRule(RelativeLayout.ALIGN_LEFT);
		//mSparklingImageIV.setId(CONSTANCE_IMAGEVIEW_ID);
		
		LayoutParams textLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//imageLayoutParams.addRule(LEFT_OF, CONSTANCE_IMAGEVIEW_ID);
		
		addView(mSparklingImageIV, imageLayoutParams);
		addView(mDescriptionHolder, textLayoutParams);
	}
	
	public TextView getTextView() {
		return mDescriptionHolder;
	}
	
	@Override
	protected void finalize() {
		if(mMaintainingThread != null)
			mMaintainingThread.terminate();
	}
	
	public void setSparklingBitmaps(Bitmap one, Bitmap two) {
		mSparklingImage1 = one;
		mSparklingImage2 = two;
	}
	
	public void setIndicatorText(String text) {
		mDescriptionHolder.setText(text);
	}
	
	public void startSparkling() {
		if(mMaintainingThread != null)
			mMaintainingThread.terminate();
		
		mMaintainingThread = new ControlThread();
		mMaintainingThread.start();
	}
	
	public void stopSparkling() {
		mMaintainingThread.terminate();
	}
	
	
	//先使用thread, 看不出得使用AsyncTask的必要
	class ControlThread extends Thread {
		boolean mRunning = true;
		
		public void terminate() {
			mRunning = false;
		}

		@Override
		public void run() {
			while(mRunning) {
				try {
					Message msg = mHandler.obtainMessage();
					msg.what = SPARKLING_CHANGE_COLOR;
					msg.arg1 = mTicker ? SPARKLING_BITMAP_FIRST : SPARKLING_BITMAP_SECOND;
					mHandler.sendMessage(msg);
					mTicker = !mTicker;
					Thread.sleep(mSparklingTimer);
				} catch (InterruptedException e) {
					terminate();
					e.printStackTrace();
				}
			}
			super.run();
		}
	}
}
