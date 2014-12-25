package cn.java6;

import java.io.IOException;
import java.net.Socket;

public class UserControlSocket {
	
	private Socket mSocket = null;
	private WriteThread mWriteThread = null;
	private ReaderThread mReaderThread = null;
	
	public UserControlSocket(Socket mSocket) throws Exception{
		
		this.mSocket = mSocket;
		this.mWriteThread = new WriteThread(mSocket.getOutputStream());
		this.mReaderThread = new ReaderThread(this,mSocket.getInputStream());
	}
	
	
	public void sendMsg2WT(int w, int h, boolean flag){
		mWriteThread.setWH(w, h);
		mWriteThread.setFlag(flag);
	}
	
	public void start(){
		if (mWriteThread != null)
			mWriteThread.start();
		if (mReaderThread != null)
			mReaderThread.start();	
	}
	
	public void quit(){
		try {
			mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mWriteThread.quit();
		mReaderThread.quit();
		
	}

}
