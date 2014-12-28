package cn.java6;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;


import javax.imageio.ImageIO;

public class WriteThread extends Thread {

	private DataOutputStream mDos;
	private Robot mRobot;
	private Dimension mDimension;
	private Rectangle mRectangle;
	private boolean mRunning,mQuit,mFlag;
	private String mPath;
	private int mWidth, mHeight,mNewWidth,mNewHeight;
	private BufferedImage mTargetImage;
	private ByteArrayOutputStream mImageStream;
	public WriteThread(OutputStream mOs) throws Exception {
		
		this.mDos = new DataOutputStream(mOs);
		this.mRobot = new Robot();
		this.mDimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.mWidth = mDimension.width;
		this.mHeight = mDimension.height;
		this.mRectangle = new Rectangle(this.mWidth, this.mHeight);			
		this.mPath = this.getClass().getResource("").getFile()+"/mouse.jpg";   
		System.out.println(mPath);
		this.mImageStream = new ByteArrayOutputStream();        
		this.mFlag = true;
	}
	
	public void setFlag(boolean mFlag){
		this.mFlag = mFlag;
	}
	
	public void setWH(int w, int h){
		this.mNewWidth = w;
		this.mNewHeight = h;
		this.mTargetImage = new BufferedImage(mNewWidth,mNewHeight,BufferedImage.TYPE_INT_RGB);
	}
	private void _sendImg() throws Exception {
		mRunning = true;
		mQuit = false;		
		BufferedImage cursor= ImageIO.read(new File(mPath/*"G:\\a\\mouse.jpg"*/));// « Û±ÍÕº∆¨
		
		while (mRunning) {
			if (mQuit)
				break;
			Point p = MouseInfo.getPointerInfo().getLocation();
			BufferedImage bui = mRobot.createScreenCapture(mRectangle);
			bui.createGraphics().drawImage(cursor, p.x, p.y, null);
			compressImage(bui);
		}
		mRunning = false;

	}
	
	public void quit(){
		try {
			mDos.close();
			mDos = null;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mQuit = true;
		interrupt();
		while (mRunning) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
	}
	
    public void compressImage(BufferedImage _image) throws Exception {  
        
    	Graphics2D g = mTargetImage.createGraphics();
        g.drawImage(_image, 0, 0, mNewWidth, mNewHeight, null);                      
        g.dispose();
        mTargetImage.flush();
        mImageStream.reset();
		
		boolean resultWrite = ImageIO.write(mTargetImage, "jpg", mImageStream);
		if (!resultWrite){
			return;
		}
		byte[] tagInfo = mImageStream.toByteArray();
		mDos.writeInt(tagInfo.length + 5);
		mDos.writeByte((byte) 4);
		mDos.write(tagInfo);
		mDos.flush();
		Thread.sleep(50);
    }  

	@Override
	public void run() {
		try {
			mDos.writeByte((byte)5);
			mDos.writeInt(Constant.ACK_CMD);
			mDos.writeByte((byte)5);
			mDos.writeInt(mWidth);
			mDos.writeByte((byte)5);
			mDos.writeInt(mHeight);
			while(mFlag){
				Thread.sleep(200);
			}
			_sendImg();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
