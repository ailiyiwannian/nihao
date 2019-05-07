package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//   Ϊ�˷�ֹ  �������  �� �޸��������  socket�� readUTF  �໥��ͻ
public class Manage implements Runnable{
	static Object login;		//��Ҫ������    new Object();   ��Ȼ �˳���¼����  �ؿ�ҳ��  ����������й������   ������Manage�õ�
	static Object enter;
	static Object liaotian;
	static Object set_pwd;
	static Socket socket;
	static DataInputStream in;
	static private String temp_sum;
	static private String operator;
	static private String [] split_array;
	static String data;
	
	
	@Override
	public void run() {
		login = new Object();
		enter = new Object();
		liaotian = new Object();
		set_pwd = new Object();
		
		// TODO �Զ����ɵķ������
		//ѭ������  data
		System.err.println("Manage run��ʼ");
		System.out.println(login);
		
		try {
			synchronized (login) {
				synchronized (enter) {
					synchronized (liaotian) {
						synchronized (set_pwd) {
							myThread_oper();
						}
					}
				}
			}
				
		} catch (IOException | InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				System.err.println("һ��manage�ҵ���");
			}
		
	}
	
	public void myThread_oper() throws InterruptedException, IOException {
		while(true) {
			System.out.println("����");
			temp_sum = in.readUTF();
			System.out.println("Manage  �� temp_sum Ϊ ("+temp_sum+")");
			split_array = temp_sum.split(",,,");
			if (split_array.length == 1) {
				data = operator = split_array[0];
			}else {
				operator = split_array[0];
				data = split_array[1];
			}
			//��ʶ�� ������������
			if ("qunliao".equals(operator)||"user_list".equals(operator)||"siliao".equals(operator)) {  
				System.out.println("manage  liaotian ����");
				liaotian.notifyAll();
				liaotian.wait();	//�ó�������
				System.out.println("manage  liaotian ����");
			}else if ("flag_Enter".equals(operator)) {
				System.out.println("manage  enter ����");
				enter.notifyAll();
				enter.wait();	//�ó�������
				System.out.println("manage  enter ����");
			}else if ("if_Username_Exist".equals(operator)||"flag_Login".equals(operator)) {
				System.out.println("manage  login ����");
				login.notifyAll();
				login.wait();	//�ó�������
				System.out.println("manage  login ����");
			}else if ("if_User_Pwd".equals(operator)||"set_Pwd".equals(operator)) {
				System.out.println("manage  set_pwd ����");
				set_pwd.notifyAll();
				set_pwd.wait();	//�ó�������
				System.out.println("manage  set_pwd ����");
			}else if ("quit".equals(operator)) {
				 myMain_Client.obj = new myMain_Client(false);
				 System.err.println("һ��manage�ҵ���");
				 return;
			}
		}
	
	}
	
	static String getData() {
		return data;
	}
	
	static public void setSocket(Socket so) {
		socket = so;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}


	
	
}
