package client;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import client.LiaoTian.liaotian_read;


public class myMain_Client {
	static myMain_Client obj;
	static Enter enter;
	
	public  myMain_Client(boolean auto_Enter_flag) {
		Socket socket;
		
		try {
			System.err.println("new��һ�� myMain_Client");
			socket = new Socket("127.0.0.1",3389);
			Manage.setSocket(socket);
			new Thread(new Manage()).start();
			enter = new Enter(socket, auto_Enter_flag);		
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			System.out.println("����ʧ��");
		}
	
	}
	
	
	public static void main(String[] args) {
		 obj = new myMain_Client(true);
		
		
	}
	
	
	
}
