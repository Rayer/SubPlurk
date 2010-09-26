package com.rayer.im.SubPlurk.views;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.SystemManager;
import com.rayer.util.plurk.data.PlurkScrap;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlurkScrapView extends RelativeLayout {
	
	ImageView mPortraitIV;
	WebView mContentWV;
	TextView mReadingCountTV;
	
	ListView mReplyLV;
	
	TextView mDebuggerTV;

	public PlurkScrapView(Context context, final PlurkScrap scrap) {
		super(context);
		LayoutInflater li = LayoutInflater.from(context);
		li.inflate(R.layout.plurk_scrap_layout, this);
		
		mPortraitIV =(ImageView) findViewById(R.id.plurk_scrap_portrait_ib);
		mContentWV = (WebView) findViewById(R.id.plurk_scrap_message_tv);
		mReadingCountTV = (TextView) findViewById(R.id.plurk_scrap_reply_count_tv);
		mReplyLV = (ListView) findViewById(R.id.plurk_scrap_replyviews_lv);
		
		//mContentTV.loadData(scrap.content, "text/html", "utf-8");
		mContentWV.loadDataWithBaseURL(null, scrap.content, "text/html", "utf-8", null);
		mPortraitIV.setImageBitmap(SystemManager.getInst().getPlurkController().getPortraitMedium(scrap.owner_id));
		
		setReadingState(scrap.is_unread == 1, scrap.response_count);
		//setReadingState()
		
		mContentWV.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(scrap.response_count == 0)
					return;
				
			}});
		
		//for debug only
		mContentWV.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				new AlertDialog.Builder(getContext()).setTitle("Debug info ").setMessage(scrap.toString()).show();
				return true;
			}});
		
		
	}
	
	void setReadingState(boolean isUnread, int count) {
		mReadingCountTV.setText("" + count);
		mReadingCountTV.setBackgroundColor(isUnread ? android.graphics.Color.RED : android.graphics.Color.TRANSPARENT);
		mReadingCountTV.setTextColor(isUnread ? android.graphics.Color.WHITE : android.graphics.Color.GRAY);
	}

}
