package com.example.phoneclient;

import java.io.IOException;

import com.example.constant.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class PMainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, android.view.GestureDetector.OnGestureListener {

	private static final String TAG = "PMainActivity";
	private static boolean D = true;
	private PSurfaceView mSurfaceView = null;
	private PReaderThread mPReaderThread = null;
	private PWriteThread mPWriteThread = null;
	private GestureDetector mGestureDetector;
	private CheckBox mCheckBox;
	private Spinner mSpinner;
	private Button mMTLBtn, mMTRBtn;
	private Button mMMTBTn, mMMRBtn, mMMBBtn, mMMLBtn;
	private boolean mFlag = false;
	private DisplayMetrics mDM;
	private int mWidth, mHeight;
	private int mLeve = 0;
	private boolean mMoveWindow = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mDM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDM);
		init();
	}

	private void init() {
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setOnDoubleTapListener(new OnDoubleTapListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				float BX = e.getX();
				float BY = e.getY();
				mPWriteThread.setMoveCMDXY(Constant.CMOVE_CMD,
						Constant.CMOUSE_MOVE_CMD, BX, BY);
				mPWriteThread.setmFlag(true);
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mPWriteThread.setTabCMD(Constant.TAB_CMD,
						Constant.MOUSE_LEFT_CLICK_CMD);
				mPWriteThread.setmFlag(true);

				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				// 双击响应很多次

				return false;
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// 双击响应一次
				float BX = e.getX();
				float BY = e.getY();
				mPWriteThread.setMoveCMDXY(Constant.CMOVE_CMD,
						Constant.CMOUSE_MOVE_CMD, BX, BY);
				mPWriteThread.setmFlag(true);
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mPWriteThread.setDoubleTabCMD(Constant.TAB_CMD,
						Constant.MOUSE_DOUBLE_CLICK_CMD);
				mPWriteThread.setmFlag(true);
				return false;
			}
		});
		mSurfaceView = (PSurfaceView) findViewById(R.id.surfaceview);
		mWidth = mDM.widthPixels;
		mHeight = mDM.heightPixels;

		try {
			mPWriteThread = new PWriteThread(Constant.SOCKET.getOutputStream(),
					mWidth, mHeight);
			mPWriteThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mPReaderThread = new PReaderThread(Constant.SOCKET.getInputStream());
			mPReaderThread.setSurfaceView(mSurfaceView);
			mSurfaceView.setRectD(mPWriteThread, mWidth, mHeight);
			mPReaderThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mSpinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.spinner_items, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.drawable.drop_list_ys);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mLeve = arg2 + 1;
				if (D)
					Log.e(TAG, "" + mLeve);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mCheckBox = (CheckBox) findViewById(R.id.midchb);
		mMTLBtn = (Button) findViewById(R.id.mtleft);
		mMTRBtn = (Button) findViewById(R.id.mtright);
		mMMTBTn = (Button) findViewById(R.id.mmtop);
		mMMRBtn = (Button) findViewById(R.id.mmright);
		mMMBBtn = (Button) findViewById(R.id.mmbm);
		mMMLBtn = (Button) findViewById(R.id.mmleft);

		mMTLBtn.setOnClickListener(this);
		mMTRBtn.setOnClickListener(this);
		mMMTBTn.setOnClickListener(this);
		mMMRBtn.setOnClickListener(this);
		mMMBBtn.setOnClickListener(this);
		mMMLBtn.setOnClickListener(this);

		mCheckBox.setOnCheckedChangeListener(this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
		 * if (event.getPointerCount() == 1) { switch (event.getAction()) { case
		 * MotionEvent.ACTION_DOWN: mBX = (int) event.getX(); mBY = (int)
		 * event.getY(); break; case MotionEvent.ACTION_MOVE: int aX = (int)
		 * event.getX(); int aY = (int) event.getY(); if (Math.abs(aX - mBX) >=
		 * 10 || Math.abs(aY - mBY) >= 10){ mBX = aX; mBX = aY;
		 * mPWriteThread.setMoveCMDXY(Constant.CMOVE_CMD,
		 * Constant.CMOUSE_MOVE_CMD, mBX, mBY); } break; case
		 * MotionEvent.ACTION_UP: int UaX = (int) event.getX(); int UaY = (int)
		 * event.getY(); mPWriteThread.setMoveCMDXY(Constant.CMOVE_CMD,
		 * Constant.CMOUSE_MOVE_CMD, UaX, UaY); break;
		 * 
		 * default: break; } } synchronized (object) { try { object.wait(TIME);
		 * } catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		mFlag = isChecked;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mtleft: // 鼠标左击
			mPWriteThread.setTabCMD(Constant.TAB_CMD,
					Constant.MOUSE_LEFT_CLICK_CMD);
			mPWriteThread.setmFlag(true);
			break;

		case R.id.mtright: // 鼠标右击
			mPWriteThread.setTabCMD(Constant.TAB_CMD,
					Constant.MOUSE_RIGHT_CLICK_CMD);
			mPWriteThread.setmFlag(true);
			break;
		case R.id.mmtop: // 如果mFlag=true鼠标向上滚动否则是向上移动
			if (mFlag) {
				mPWriteThread.setRoleCMD(Constant.ROLE_CMD,
						Constant.MOUSE_TROLLING, -Constant.ROLE_STEP);
				mPWriteThread.setmFlag(true);
			} else {
				mPWriteThread.setMoveCMD(Constant.SMOVE_CMD,
						Constant.SMOUSE_TMOVE_CMD,
						-(mLeve * Constant.MOVE_STEP));
				mPWriteThread.setmFlag(true);
			}
			break;
		case R.id.mmbm: // 如果mFlag=true鼠标向下滚动否则是向下移动
			if (mFlag) {
				mPWriteThread.setRoleCMD(Constant.ROLE_CMD,
						Constant.MOUSE_BROLLING, Constant.ROLE_STEP);
				mPWriteThread.setmFlag(true);
			} else {
				mPWriteThread
						.setMoveCMD(Constant.SMOVE_CMD,
								Constant.SMOUSE_BMOVE_CMD,
								(mLeve * Constant.MOVE_STEP));
				mPWriteThread.setmFlag(true);
			}
			break;
		case R.id.mmright: // 鼠标向右移动
			mPWriteThread.setMoveCMD(Constant.SMOVE_CMD,
					Constant.SMOUSE_RMOVE_CMD, (mLeve * Constant.MOVE_STEP));
			mPWriteThread.setmFlag(true);
			break;
		case R.id.mmleft: // 鼠标向左移动
			mPWriteThread.setMoveCMD(Constant.SMOVE_CMD,
					Constant.SMOUSE_LMOVE_CMD, -(mLeve * Constant.MOVE_STEP));
			mPWriteThread.setmFlag(true);
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "关机").setIcon(
				android.R.drawable.ic_menu_delete);

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "重启").setIcon(
				android.R.drawable.ic_menu_edit);

		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "帮助").setIcon(
				android.R.drawable.ic_menu_help);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case Menu.FIRST + 1:

			mPWriteThread.setTabCMD(Constant.TAB_CMD, Constant.SHUT_DOWN_CMD);
			mPWriteThread.setmFlag(true);
			break;
		case Menu.FIRST + 2:

			mPWriteThread.setTabCMD(Constant.TAB_CMD, Constant.RESTART_CMD);
			mPWriteThread.setmFlag(true);
			break;
		case Menu.FIRST + 3:

			Toast.makeText(this, "帮助菜单被点击了", Toast.LENGTH_LONG).show();
			break;
		default:
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float BX = e2.getX();
		float BY = e2.getY();
		mPWriteThread.setMoveCMDXY(Constant.CMOVE_CMD,
				Constant.CMOUSE_MOVE_CMD, BX, BY);
		mPWriteThread.setmFlag(true);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if (D)
			Log.e(TAG, "onLongPress" + e.getX());
		if (mMoveWindow) {
			mMoveWindow = false;
			mPWriteThread.setMoveWindow(Constant.TAB_CMD,
					Constant.MOVE_WINDOW_UP);
		} else {
			mMoveWindow = true;
			mPWriteThread.setMoveWindow(Constant.TAB_CMD,
					Constant.MOVE_WINDOW_DOWN);
		}

		mPWriteThread.setmFlag(true);

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (D)
			Log.e(TAG, "onScroll" + e1.getX());
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (D)
			Log.e(TAG, "onSingleTapUp" + e.getX());
		return false;
	}
}
