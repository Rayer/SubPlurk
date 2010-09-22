package com.rayer.util.event;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;


public class EventProcessHandler extends Handler {
	
	//Monitor function
	ArrayList<Integer> mMonitoringEventList = new ArrayList<Integer>();
	public ArrayList<Integer> getListeningEvents() {
		return mMonitoringEventList;
	}
	
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
	}
	

}
