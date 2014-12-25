package com.example.phoneclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.constant.Constant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PReaderThread implements Runnable {

	private DataInputStream mDinPs = null;
	private PSurfaceView mSurfaceView = null;
	private Thread mThread = null;
	private boolean mRunning, mQuit;
	// ����һ��Bitmap ������ImageView��ÿ��ͼ
	private Bitmap mBitmap;
	// �Ž��յ����ݵ�����
	private byte[] mData;

	public PReaderThread(InputStream in) {

		mDinPs = new DataInputStream(in);
	}

	public void start() {
		if (mDinPs == null)
			return;
		if (mThread != null)
			quit();
		mThread = new Thread(this);
		mThread.start();
	}

	public void quit() {

		mQuit = true;
		if (mDinPs != null) {
			try {
				mDinPs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDinPs = null;
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

		try {
			mDinPs.readByte();
			int cmd = mDinPs.readInt();
			if (cmd == Constant.ACK_CMD) {
				mDinPs.readByte();
				int w = mDinPs.readInt();
				mDinPs.readByte();
				int h = mDinPs.readInt();
				mSurfaceView.setRectS(w, h);
				mRunning = true;
				mQuit = false;

				while (mRunning) {
					if (mQuit || mDinPs == null || mSurfaceView == null)
						break;

					mData = new byte[mDinPs.readInt() - 5];

					mDinPs.readByte();
					// ע�⣬����Ҫ��readfully
					mDinPs.readFully(mData);
					// ע�⣬����Ҫ����bmm ,���򱨴�
					if (mBitmap != null) {
						mBitmap.recycle();
						mBitmap = null;
					}
					mBitmap = BitmapFactory.decodeByteArray(mData, 0,
							mData.length);
					mSurfaceView.setBitmap(mBitmap);
					mSurfaceView.setFlag(true);
					Thread.sleep(100);
				}

			}
		} catch (Exception e) {
			mRunning = false;
			quit();
		}
		mRunning = false;
	}

	public void setSurfaceView(PSurfaceView mSurfaceView) {
		this.mSurfaceView = mSurfaceView;
	}

}
