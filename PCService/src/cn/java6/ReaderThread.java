package cn.java6;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReaderThread extends Thread {

	private DataInputStream mDataInputStream;
	private Robot mRobot;
	private boolean mRunning,mQuit;
	private int mMove;
	private float mX, mY;
	private int mCMDType;
	private int mCMD;
	private int mW,mH;
	private UserControlSocket mControlSocket;
	public ReaderThread(UserControlSocket userControlSocket, InputStream inputStream) throws Exception {
		this.mControlSocket = userControlSocket;
		this.mDataInputStream = new DataInputStream(inputStream);
		this.mRobot = new Robot();
	}

	private void _readCmd(int cmd) throws Exception {
		switch (cmd) {
		
		case Constant.ACK_CMD:
			mDataInputStream.readByte();
			mCMD = mDataInputStream.readInt();
			mDataInputStream.readByte();
			mW = mDataInputStream.readInt();
			mDataInputStream.readByte();
			mH = mDataInputStream.readInt();
			break;
		case Constant.TAB_CMD:	
			mDataInputStream.readByte();
			mCMD = mDataInputStream.readInt();
			
			break;
		case Constant.SMOVE_CMD://单步移动
			
			mDataInputStream.readByte();
			mCMD = mDataInputStream.readInt();
			mDataInputStream.readByte();
			mMove = mDataInputStream.readInt();
			
			break;
			
		case Constant.CMOVE_CMD://坐标移动
			mDataInputStream.readByte();
			mCMD = mDataInputStream.readInt();
			mDataInputStream.readByte();
			mX = mDataInputStream.readFloat();
			mDataInputStream.readByte();
			mY = mDataInputStream.readFloat();		
			break;
		case Constant.ROLE_CMD://鼠标滚动
			mDataInputStream.readByte();
			mCMD = mDataInputStream.readInt();
			mDataInputStream.readByte();
			mMove = mDataInputStream.readInt();
			break;
		default:
			break;
		}
	}

	private class DoThread extends Thread {

		private int mcmd = -1;

		public DoThread(int cmd) {
			mcmd = cmd;

		}

		@Override
		public void run() {
			
			try {
				switch (mcmd) {
				case Constant.ACK:
					//通知写线程
					mControlSocket.sendMsg2WT(mW, mH, false);
					break;
				case Constant.MOVE_WINDOW_DOWN:
					mRobot.mousePress(InputEvent.BUTTON1_MASK);
					break;
				case Constant.MOVE_WINDOW_UP:
					mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
					break;
				case Constant.SHUT_DOWN_CMD:
					System.out.println("关机"+Constant.SHUTDOWN);
					Runtime.getRuntime().exec(Constant.SHUTDOWN);

					break;
				case Constant.RESTART_CMD:
					System.out.println("重启"+Constant.RESTART);
					Runtime.getRuntime().exec(Constant.RESTART);

					break;
				case Constant.MOUSE_LEFT_CLICK_CMD:
					mRobot.mousePress(InputEvent.BUTTON1_MASK);
					mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
					break;
				case Constant.MOUSE_RIGHT_CLICK_CMD:
					mRobot.mousePress(InputEvent.BUTTON3_MASK);
					mRobot.mouseRelease(InputEvent.BUTTON3_MASK);
				case Constant.MOUSE_TROLLING: //向上滚动
					mRobot.mouseWheel(mMove);
					break;
				case Constant.MOUSE_BROLLING: //向下滚动
					mRobot.mouseWheel(mMove);
					break;
				case Constant.SMOUSE_TMOVE_CMD: //向上移动				
				case Constant.SMOUSE_BMOVE_CMD: //向下移动
					Point p1 = MouseInfo.getPointerInfo().getLocation();
					p1.y += mMove;
					mRobot.mouseMove(p1.x, p1.y);
					break;
				case Constant.SMOUSE_LMOVE_CMD:   //向左移动
				case Constant.SMOUSE_RMOVE_CMD: //向右移动
					Point p2 = MouseInfo.getPointerInfo().getLocation();
					p2.x += mMove;
					mRobot.mouseMove(p2.x, p2.y);	
					break;
				case Constant.CMOUSE_MOVE_CMD:
					mRobot.mouseMove((int)mX, (int)mY);
					break;
				case Constant.MOUSE_DOUBLE_CLICK_CMD:
					mRobot.mousePress(InputEvent.BUTTON1_MASK);
					mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mRobot.mousePress(InputEvent.BUTTON1_MASK);
					mRobot.mouseRelease(InputEvent.BUTTON1_MASK);
					break;
				default:
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}

	}

	public void quit(){
		try {
			mDataInputStream.close();
			mDataInputStream = null;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		interrupt();
		mQuit = true;
		while (mRunning) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			mRunning = true;
			mQuit = false;
			while (mRunning) {
				if (mQuit)
					break;
				mDataInputStream.readByte();
				mCMDType = mDataInputStream.readInt();
				_readCmd(mCMDType);
				new DoThread(mCMD).start();
				Thread.sleep(100);
			}
			mRunning = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
