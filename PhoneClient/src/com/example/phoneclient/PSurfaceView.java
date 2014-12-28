package com.example.phoneclient;

import com.example.constant.Constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PSurfaceView extends SurfaceView implements Runnable,SurfaceHolder.Callback{

	private static final String TAG = "PSurfaceView";
	private static boolean D = false;
	private Bitmap mBitmap;
	private boolean mFlag;
	private Thread mThread = null;
	private boolean mRunning, mQuit;
	private Canvas mCanvas;
	private Rect mRectS, mRectD;
	private SurfaceHolder mHolder = null;
	private PWriteThread mPWriteThread;
	
	public PSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (mHolder == null){
			mHolder = this.getHolder();
			mHolder.setFormat(PixelFormat.TRANSPARENT);//
			mHolder.addCallback(this);
		}
	}

	public PSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (mHolder == null){
			mHolder = this.getHolder();
			mHolder.setFormat(PixelFormat.TRANSPARENT);//
			mHolder.addCallback(this);
		}
	}

	public PSurfaceView(Context context) {
		super(context);
		if (mHolder == null){
			mHolder = this.getHolder();
			mHolder.setFormat(PixelFormat.TRANSPARENT);//
			mHolder.addCallback(this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mThread != null){
			quit();
		}
		mThread = new Thread(this);
		mThread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		quit();
		
	}


	public void draw() {
		mCanvas = mHolder.lockCanvas();
		if (mCanvas == null)
			return ;
		if (mBitmap != null){
			mCanvas.drawColor(Color.WHITE);
			mCanvas.drawBitmap(mBitmap, mRectS, mRectD, null);
		}
		mHolder.unlockCanvasAndPost(mCanvas);
	}

	public void setBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
		if (mRectS == null) {
			mRectS = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
			if (D)
				Log.e(TAG, "11"+Constant.BITMAP_WIDTH+":"+Constant.BITMAP_HEIGHT);
		}
	}
	
	public void setRectS(int mViewW, int mViewH){
		Constant.BITMAP_WIDTH = mViewW;
		Constant.BITMAP_HEIGHT = mViewH;		
		mPWriteThread.setACKCMD(Constant.ACK_CMD, Constant.ACK);
		mPWriteThread.setmFlag(true);
		if (D)
			Log.e(TAG, "22"+Constant.BITMAP_WIDTH+":"+Constant.BITMAP_HEIGHT);
	}
	
	public void setRectD(PWriteThread mPWriteThread, int mViewW, int mViewH){
		this.mPWriteThread = mPWriteThread;
		mRectD = new Rect(0, 0, mViewW, mViewH);
	}

	public synchronized void setFlag(boolean mFlag) {
		this.mFlag = mFlag;
	}
	
	public synchronized boolean isFlag(){
		return mFlag;
	}
	
	public void quit(){
		mQuit = true;
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
			if (isFlag()){
				setFlag(false);
				draw();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mRunning = false;
		
	}

	
}
