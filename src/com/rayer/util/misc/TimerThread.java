package com.rayer.util.misc;

public class TimerThread extends Thread {
	
	
	
	public interface Listener {
		void OnTimerStart();
		void OnTimerRepeat();
		void OnTimerExpired();
	}
}
