package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//   为了防止  聊天界面  和 修改密码界面  socket的 readUTF  相互冲突
public class Manage implements Runnable{
	static Object login;		//不要在这里    new Object();   不然 退出登录功能  重开页面  对象锁会外泄被别人   ，不被Manage拿到
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
		
		// TODO 自动生成的方法存根
		//循环监听  data
		System.err.println("Manage run开始");
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				System.err.println("一个manage挂掉了");
			}
		
	}
	
	public void myThread_oper() throws InterruptedException, IOException {
		while(true) {
			System.out.println("启动");
			temp_sum = in.readUTF();
			System.out.println("Manage  ： temp_sum 为 ("+temp_sum+")");
			split_array = temp_sum.split(",,,");
			if (split_array.length == 1) {
				data = operator = split_array[0];
			}else {
				operator = split_array[0];
				data = split_array[1];
			}
			//标识符 代表聊天数据
			if ("qunliao".equals(operator)||"user_list".equals(operator)||"siliao".equals(operator)) {  
				System.out.println("manage  liaotian 上锁");
				liaotian.notifyAll();
				liaotian.wait();	//让出对象锁
				System.out.println("manage  liaotian 解锁");
			}else if ("flag_Enter".equals(operator)) {
				System.out.println("manage  enter 上锁");
				enter.notifyAll();
				enter.wait();	//让出对象锁
				System.out.println("manage  enter 解锁");
			}else if ("if_Username_Exist".equals(operator)||"flag_Login".equals(operator)) {
				System.out.println("manage  login 上锁");
				login.notifyAll();
				login.wait();	//让出对象锁
				System.out.println("manage  login 解锁");
			}else if ("if_User_Pwd".equals(operator)||"set_Pwd".equals(operator)) {
				System.out.println("manage  set_pwd 上锁");
				set_pwd.notifyAll();
				set_pwd.wait();	//让出对象锁
				System.out.println("manage  set_pwd 解锁");
			}else if ("quit".equals(operator)) {
				 myMain_Client.obj = new myMain_Client(false);
				 System.err.println("一个manage挂掉了");
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}


	
	
}
