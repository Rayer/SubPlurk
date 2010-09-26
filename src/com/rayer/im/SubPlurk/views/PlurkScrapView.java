package com.rayer.im.SubPlurk.views;

import java.util.ArrayList;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.SystemManager;
import com.rayer.util.plurk.PlurkController;
import com.rayer.util.plurk.data.PlurkScrap;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
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
				Log.d("SubPlurk", "Attemping getting response id : " + scrap.plurk_id);

				if(scrap.response_count == 0)
					return;
				
				
				PlurkController pc = SystemManager.getInst().getPlurkController();
				ArrayList<PlurkScrap> scrapList = pc.getResponser(scrap.plurk_id, 0);
				StringBuilder sb = new StringBuilder();
				for(PlurkScrap s : scrapList)
					sb.append(s.toString() + " / ");
				
				new AlertDialog.Builder(getContext()).setTitle("Response debug info").setMessage(sb.toString()).show();
				
			}});
		
		//for debug only
		mContentWV.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				Log.d("SubPlurk", "Attemping getting response id : " + scrap.plurk_id);

				if(scrap.response_count == 0)
					return false;
				
				
				PlurkController pc = SystemManager.getInst().getPlurkController();
				ArrayList<PlurkScrap> scrapList = pc.getResponser(scrap.plurk_id, 0);
				StringBuilder sb = new StringBuilder();
				for(PlurkScrap s : scrapList)
					sb.append(s.toString() + " / ");
				
				new AlertDialog.Builder(getContext()).setTitle("Response debug info").setMessage(sb.toString()).show();
				return false;
			}});
		
		
	}
	
	void setReadingState(boolean isUnread, int count) {
		mReadingCountTV.setText("" + count);
		mReadingCountTV.setBackgroundColor(isUnread ? android.graphics.Color.RED : android.graphics.Color.TRANSPARENT);
		mReadingCountTV.setTextColor(isUnread ? android.graphics.Color.WHITE : android.graphics.Color.GRAY);
	}

}
