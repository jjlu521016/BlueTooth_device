package cn.java6;
import java.io.IOException;

public class TEST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.net.ServerSocket sc = null;
		try {
			sc = new java.net.ServerSocket(Constant.PORT);
			System.out.println("�����������ɹ�");
			while (true) {
				java.net.Socket client = sc.accept();
				UserControlSocket mSocket = new UserControlSocket(client);
				mSocket.start();
				System.out.println("���ӳɹ�");
			}
			

		} catch (Exception ef) {
			
			ef.printStackTrace();
		}finally{
			System.out.println("����");			
			if (sc != null){
				try {
					sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sc = null;
			}
		}
		
	}

}
