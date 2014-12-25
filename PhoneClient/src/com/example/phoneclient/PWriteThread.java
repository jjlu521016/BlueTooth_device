package com.example.phoneclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.example.constant.Constant;

public class PWriteThread implements Runnable {
	
	private static final String TAG = "PWriteThread";
	private boolean D = true;
	private DataOutputStream mDotPs = null;
	private Thread mThread = null;
	private boolean mRunning, mQuit;
	private boolean mFlag;

	private int mCMD; // 命令
	private float mX, mY; // 坐标
	private int mMove;// 移动的长度
	private int mRole;// 鼠标滚动长度;
	private int mCMDType;
	private int mWidth, mHeight;
	public synchronized boolean ismFlag() {
		return mFlag;
	}

	public synchronized void setmFlag(boolean mFlag) {
		this.mFlag = mFlag;
	}

	public PWriteThread(OutputStream outps, int mWidth, int mHeight) {
		this.mDotPs = new DataOutputStream(outps);
		this.mWidth = mWidth;
		this.mHeight = mHeight;
	}

	public void start() {
		if (mDotPs == null)
			return;
		if (mThread != null)
			quit();
		mThread = new Thread(this);
		setmFlag(false);
		mThread.start();
	}

	public void quit() {

		mQuit = true;
		if (mDotPs != null) {
			try {
				mDotPs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDotPs = null;
		}
		while (mRunning) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mThread = null;

	}

	@Override
	public void run() {
		mRunning = true;
		mQuit = false;
		while (mRunning) {
			if (mQuit)
				break;
			if (ismFlag()){
				setmFlag(false);
				//发送命令
				_sendCMD();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		mRunning = false;
		
	}

	private void _sendCMD() {
		try {
			switch (mCMDType) {
			case Constant.ACK_CMD:
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMDType);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMD);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mWidth);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mHeight);
				break;
			case Constant.TAB_CMD:
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMDType);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMD);

				break;
			case Constant.SMOVE_CMD:
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMDType);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMD);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mMove);
				
				break;
			case Constant.CMOVE_CMD:
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMDType);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMD);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeFloat(mX);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeFloat(mY);
				break;
			case Constant.ROLE_CMD:
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMDType);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mCMD);
				mDotPs.writeByte((byte) 5);
				mDotPs.writeInt(mRole);
				break;

			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mDotPs.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMoveCMDXY(int cmdtype, int mCMD, float x, float y) {
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;
		this.mX = (float) (x*Constant.BITMAP_WIDTH/(mWidth*1.0));
		this.mY = (float) (y*Constant.BITMAP_HEIGHT/(mHeight*1.0));
	}

	public void setTabCMD(int cmdtype, int mCMD) {
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;
	}

	public void setMoveCMD(int cmdtype, int mCMD, int mMove) {
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;
		this.mMove = mMove;
	}

	public void setRoleCMD(int cmdtype, int mCMD, int mRole) {
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;
		this.mRole = mRole;
	}
	
	public void setDoubleTabCMD(int cmdtype, int mCMD){
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;
	}
	
	public void setMoveWindow(int cmdtype, int mCMD){
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;
	}
	
	public void setACKCMD(int cmdtype, int mCMD){
		this.mCMDType = cmdtype;
		this.mCMD = mCMD;			
	}

}
