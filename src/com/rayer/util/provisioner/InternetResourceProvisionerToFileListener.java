package com.rayer.util.provisioner;

import java.io.File;

public interface InternetResourceProvisionerToFileListener {
	/**
	 * While at checkpoint, provisioner will inform this method.<br>
	 * @param checkPoint
	 */
	void onPrePercentDownloaded(int checkPoint);
	
	/**
	 * Occurs while download started
	 */
	void onStartDownload();
	
	/**
	 * Occurs while download ended
	 */
	void onEndDownload();
	
	/**
	 * Occurs while error occurred.
	 */
	void onError(Throwable t, File f);
	
}
