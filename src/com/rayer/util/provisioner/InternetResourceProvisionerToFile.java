package com.rayer.util.provisioner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class InternetResourceProvisionerToFile extends
		InternetResourceProvisioner<File> {
	
	public InternetResourceProvisionerToFile(InternetResourceProvisionerToFileListener inListener) {
		mListener = inListener;
	}
	
	InternetResourceProvisionerToFileListener mListener;

	@Override
	public File formFromStream(InputStream is) {
		File root = new File(getTargetFileDir());
		root.mkdirs();
		
		File ret = new File(getTargetFileDir() + "/" + getIdentificator());
		if(ret.exists())
			ret.delete();
		if(mListener != null) 
			mListener.onStartDownload();
		//ret.createNewFile();
		try {
			FileOutputStream fos = new FileOutputStream(ret);
			byte[] buf = new byte[8192];
			int len;
			int totalLen = 0;
			int currentPercentage = 0;
			while((len = is.read(buf)) > 0) {
				fos.write(buf, 0, len);
				totalLen += len;
				int percent = (totalLen * 100)/ getTotalLength();
				if(currentPercentage < percent) {
					currentPercentage = percent;
					if(mListener != null)
						mListener.onPrePercentDownloaded(currentPercentage);
				}
			}
			
			fos.close();
		} catch (FileNotFoundException e) {
			handleError(ret, e);
			return null;
		} catch (IOException e) {
			handleError(ret, e);
			return null;
		}
		
		if(mListener != null)
			mListener.onEndDownload();
		
		return ret;
	}


	void handleError(File ret, Exception e) {
		if(mListener != null)
			mListener.onError(e, ret);
		e.printStackTrace();
	}


	@Override
	public abstract String getUrlAddress(String identificator);
	public abstract String getTargetFileDir();
}
