package com.rayer.im.SubPlurk.views;

import com.rayer.im.SubPlurk.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class LoginDrawerView extends RelativeLayout {

	public LoginDrawerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public LoginDrawerView(Context context) {
		super(context);
		initialize();
	}
	
	public void initialize() {
		LayoutInflater li = LayoutInflater.from(getContext());
		//li.inflate(R.layout.login_drawer_view, this);
		li.inflate(R.layout.login_drawer_view, this);		
	}
	
	

}
