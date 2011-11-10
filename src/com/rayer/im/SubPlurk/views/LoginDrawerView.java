package com.rayer.im.SubPlurk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.rayer.im.SubPlurk.R;
import com.rayer.im.SubPlurk.manager.SystemManager;
import com.rayer.util.plurk.PlurkControllerMT;

public class LoginDrawerView extends RelativeLayout {
	
	EditText mAccount_et;
	EditText mPassword_et;
	Button mLogin_btn;

	PlurkControllerMT mController;
	
	
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
		li.inflate(R.layout.login_drawer_view, this);	
		
		mAccount_et = (EditText) findViewById(R.id.login_drawer_acc_et);
		mPassword_et = (EditText) findViewById(R.id.login_drawer_pass_et);
		mLogin_btn = (Button) findViewById(R.id.login_drawer_login_btn);
		
		mAccount_et.setText(SystemManager.getInst().getAccountManager().getLastLoginSucceedAcc());
		mPassword_et.setText(SystemManager.getInst().getAccountManager().getLastLoginSucceedPass());
		
		mController = SystemManager.getInst().getPlurkController();
		
		mLogin_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//mController.async_login(mAccount_et.getText().toString(), mPassword_et.getText().toString());
				SystemManager.getInst().getAccountManager().attempLogging(mAccount_et.getText().toString(), mPassword_et.getText().toString());
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
			}});
	}
	
	

}
