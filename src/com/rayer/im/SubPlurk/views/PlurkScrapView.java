package com.rayer.im.SubPlurk.views;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.SystemManager;
import com.rayer.util.plurk.data.PlurkScrap;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlurkScrapView extends RelativeLayout {
	
	ImageView mPortraitIV;
	WebView mContentTV;

	public PlurkScrapView(Context context, PlurkScrap scrap) {
		super(context);
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.plurk_scrap_layout, this);
		
		mPortraitIV =(ImageView) findViewById(R.id.plurk_scrap_portrait_ib);
		mContentTV = (WebView) findViewById(R.id.plurk_scrap_message_tv);
		
		//mContentTV.loadData(scrap.content, "text/html", "utf-8");
		mContentTV.loadDataWithBaseURL(null, scrap.content, "text/html", "utf-8", null);
		mPortraitIV.setImageBitmap(SystemManager.getInst().getPlurkController().getPortraitMedium(scrap.owner_id));
		
	}

}
