package com.example.constant;

import java.net.Socket;

public class Constant {
	
	public static final int CMOUSE_MOVE_CMD       = 0X01;      //�������ƶ�
	public static final int MOUSE_LEFT_CLICK_CMD  = 0X02;      //������
	public static final int MOUSE_RIGHT_CLICK_CMD = 0X03;      //����һ�
	public static final int SHUT_DOWN_CMD         = 0XFF;      //�ػ�
	public static final int RESTART_CMD           = 0XFE;      //����
	public static final int MOUSE_DOUBLE_CLICK_CMD   = 0X06;	   //���˫��
	
	public static final int MOVE_WINDOW_DOWN      = 0X09;
	public static final int MOVE_WINDOW_UP        = 0X90;
	
	public static final int MOUSE_TROLLING        = 0XB0;      //����Ϲ���	
	public static final int MOUSE_BROLLING        = 0XB2;      //����¹���

	
	public static final int SMOUSE_TMOVE_CMD      = 0XE1;      //����ϵ����ƶ�
	public static final int SMOUSE_LMOVE_CMD      = 0XE2;      //����󵥲��ƶ�
	public static final int SMOUSE_RMOVE_CMD      = 0XE3;      //����ҵ����ƶ�
	public static final int SMOUSE_BMOVE_CMD      = 0XE4;      //����µ����ƶ�
	
	public static final int TAB_CMD               = 0XA0;
	public static final int SMOVE_CMD             = 0XA3;
	public static final int CMOVE_CMD             = 0XA6;
	public static final int ROLE_CMD              = 0xA9;
	
	public static final int ACK_CMD               = 0XAC;
	
	public static final int ACK                   = 0XAB;
	
	public static final int ROLE_STEP             = 1;
	public static final int MOVE_STEP			  = 10;
	
	public static int BITMAP_WIDTH = 0;
	public static int BITMAP_HEIGHT = 0;
	
	public static final String SHUTDOWN = "cmd shutdown";
	public static final String RESTART = "cmd restart";
	
	
	public static final String PC_ID_ADD = "";
	public static final int PORT = 9090;
	
	public static final String FILE_NAME = "COCO";
	public static Socket SOCKET = null;
	
}
