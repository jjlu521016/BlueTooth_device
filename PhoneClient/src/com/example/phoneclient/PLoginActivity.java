package com.example.phoneclient;

import java.io.IOException;
import java.net.Socket;

import com.example.constant.Constant;
import com.example.phoneclient.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class PLoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private boolean D = true;
	private static final int SHOW_DIALOG = 1000;
	private static final int FINISH_DIALOG = 1001;
	private static final int SHOW_TOAST = 1002;

	private Socket mSocket = null;
	private EditText mAddET, mPortET;
	private Button mLoginBtn;
	private CheckBox mSaveCheckBox;
	private boolean mFlag = true, MSucFlag;
	private Context mContext;
	private String mAdd = null, mAddT;
	private int mPort, mPortT;
	private Dialog mDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.mContext = this;

		SharedPreferences Preferences = this.getSharedPreferences(
				Constant.FILE_NAME, MODE_PRIVATE);
		mAdd = Preferences.getString("ADD", null);
		mPort = Preferences.getInt("PORT", 9090);
		init();
	}

	private void init() {

		mAddET = (EditText) findViewById(R.id.login_add);
		mPortET = (EditText) findViewById(R.id.login_port);
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		if (mAdd != null) {
			mAddET.setText(mAdd);
			mPortET.setText("" + mPort);
		}
		mSaveCheckBox = (CheckBox) findViewById(R.id.auto_save);
		mLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new LoginThread().start();
			}
		});
		mSaveCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mFlag = isChecked;

			}
		});

	}

	private boolean login(String add, int port) {
		try {
			mSocket = new Socket(add, port);

		} catch (Exception e) {
			if (mSocket != null) {
				try {
					mSocket.close();
				} catch (IOException e1) {
					mSocket = null;
					return false;
				}
				mSocket = null;
			}
			return false;

		}

		Constant.SOCKET = mSocket;
		return true;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DIALOG:
				DisplayMetrics DM = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(DM);
				mDialog = com.example.constant.DialogFactory
						.creatRequestDialog(mContext, "正在连接PC...",
								DM.widthPixels);
				mDialog.show();
				break;
			case FINISH_DIALOG:
				mDialog.dismiss();
				if (MSucFlag) {
					if (mFlag) {
						SharedPreferences sp = mContext.getSharedPreferences(
								Constant.FILE_NAME, MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("ADD", mAddT);
						editor.putInt("PORT", mPortT);
						editor.commit();
					}
					Intent intent = new Intent();
					intent.setClass(PLoginActivity.this, PMainActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(mContext, "连接失败。。。", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case SHOW_TOAST:
				Toast.makeText(mContext,(String)msg.obj, Toast.LENGTH_SHORT)
				.show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
	
	private class LoginThread extends Thread{
		
		@Override
		public void run() {
			
			MSucFlag = false;
			mAddT = mAddET.getText().toString().trim();
			String sport = mPortET.getText().toString().trim();

			if (mAddT.equals("") || sport.equals("")) {
				Message msg = mHandler.obtainMessage();
				msg.what = SHOW_TOAST;
				msg.obj = "输入有误。。。";
				mHandler.sendMessage(msg);
				
			} else {
				if ((mPortT = Integer
						.parseInt(sport)) < 1024){
					Message msg = mHandler.obtainMessage();
					msg.what = SHOW_TOAST;
					msg.obj = "端口号不对。。。";
					mHandler.sendMessage(msg);
					return;
				}
				Message msg = mHandler.obtainMessage();
				msg.what = SHOW_DIALOG;
				mHandler.sendMessage(msg);
				if (login(mAddT, mPortT)) {
					MSucFlag = true;
				}
				Message msg1 = mHandler.obtainMessage();
				msg1.what = FINISH_DIALOG;
				mHandler.sendMessage(msg1);
			}		
			super.run();
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
