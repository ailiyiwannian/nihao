package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class SetPwd extends JFrame implements ActionListener{
	JLabel label_old_pwd,label_err_old_pwd;
	JLabel label_pwd,label_again_pwd,label_error_again_pwd;
	JTextField text_old_pwd;
	JPasswordField text_pwd,text_again_pwd;
	JButton button;
	Socket socket;
	DataOutputStream out;
	
	public SetPwd(Socket so) {
		super();
		socket = so;
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}
		this.setSize(285,215);
        this.setTitle("修改密码");//设置窗口标题
        this.getContentPane().setLayout(null);//设置布局控制器
        //窗口设置最顶层
        this.setAlwaysOnTop(true);        
        
        System.out.println("修改密码页面");
		label_old_pwd = new JLabel();
		label_old_pwd.setBounds(10,5,200,20);
		label_old_pwd.setText("请输入原密码");
		
		label_pwd = new JLabel();
		label_pwd.setBounds(30,67,52,20);
		label_pwd.setText("密码");
		
		label_again_pwd = new JLabel();
		label_again_pwd.setBounds(20,102,72,20);
		label_again_pwd.setText("确认密码");
		

		label_err_old_pwd = new JLabel();
		label_err_old_pwd.setBounds(77,47,200,20);
		label_err_old_pwd.setForeground(Color.red);
		label_err_old_pwd.setText("");
		
		
		label_error_again_pwd = new JLabel();
		label_error_again_pwd.setBounds(77,120,200,20);
		label_error_again_pwd.setForeground(Color.red);
		label_error_again_pwd.setText("");
		
		
		text_old_pwd = new JTextField();
		text_old_pwd.setBounds(77,25,160,25);

		text_pwd = new JPasswordField();
		text_pwd.setBounds(77,65,160,25);
		text_pwd.setEchoChar('*');

		text_again_pwd = new JPasswordField();
		text_again_pwd.setBounds(77,100,160,25);
		text_again_pwd.setEchoChar('*');
		
		button = new JButton();
        button.setBounds(101,145,60,25);
        button.setText("修改");
        button.addActionListener(this);//添加监听器类，其主要的响应都由监听器类的方法实现

		
        
		getContentPane().add(label_old_pwd,null);
		getContentPane().add(label_pwd,null);
		getContentPane().add(label_again_pwd,null);
		getContentPane().add(label_err_old_pwd,null);
		getContentPane().add(label_error_again_pwd,null);
		getContentPane().add(text_old_pwd,null);
		getContentPane().add(text_pwd,null);
		getContentPane().add(text_again_pwd,null);
		getContentPane().add(button,null);
		
		//加载 （add）完  再加这几句
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一定要设置关闭
		setVisible(true);
//		setLocationRelativeTo(null);
	}
	
	public String get_Socket_Data() {
		synchronized (Manage.set_pwd) {
			System.out.println("set_pwd 解锁");
			Manage.set_pwd.notifyAll();
			return Manage.getData();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		boolean flag = true;
		String old_pwd = text_old_pwd.getText().toString();
		String new_pwd = text_pwd.getText().toString();
		String again_pwd = text_again_pwd.getText().toString();
		//检测密码
		if (new_pwd.isEmpty()||again_pwd.isEmpty()) {
			label_error_again_pwd.setText("请输入密码");
			flag = false;
		}else if (!new_pwd.equals(again_pwd)) {
			label_error_again_pwd.setText("2次密码不一致");
			System.out.println("密码  ："+new_pwd +"  and  "+again_pwd);
			flag = false;
		}else {
			label_error_again_pwd.setText(null);
		}
		//检测 旧密码是否正确
		if (old_pwd.isEmpty()) {
			label_err_old_pwd.setText("请输入旧密码");
			flag = false;
		}else {		//  服务器 检测 旧密码是否正确
			try {
				String if_User_Pwd = null;
				out.writeUTF("if_User_Pwd");
				out.writeUTF(old_pwd);
				System.out.println("上锁 阻塞");
				if_User_Pwd = get_Socket_Data();
				
				// 判断 是否注册成功 用户名是否被占用 请换一个名字
				if ("OK_pwd".equals(if_User_Pwd)) {
					label_err_old_pwd.setText("");
					System.out.println("OK_pwd 旧密码正确");
				}else {
					label_err_old_pwd.setText(if_User_Pwd);
					flag = false;
				}		
				
			} catch (IOException  e1) {
				System.out.println("user_Exist获取错误");
			}
									
		}
		System.out.println("是否满足修改条件    :"+flag);
		//发送用户名和密码 创建用户  
		if (flag) {
			try {
				String set_Pwd;
				out.writeUTF("set_Pwd");
				out.writeUTF(new_pwd);
				set_Pwd = get_Socket_Data();
				System.out.println(set_Pwd);
				
				if (!"OK_set_pwd".equals(set_Pwd)) {
					label_err_old_pwd.setText(set_Pwd);
				}
				
				
			} catch (IOException e1) {
				System.out.println("user_Exist获取错误");
				}
								
				
			dispose(); 	//关闭窗口

		}
		

			
		
	}
	
	
	
}
