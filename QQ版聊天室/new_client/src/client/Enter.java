package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;






public class Enter extends JFrame {
	JLabel label_err;
	JTextField text_usernamae;
	JPasswordField  text_pwd;
	JCheckBox auto_Enter,remember_pwd;
	JButton button_Submit,button_to_Login;
	Socket socket;
	boolean auto_Enter_flag;
	static {
		try {  
			File file = new File("user_data.properties");
			if (!file.exists()) {
				// 文件若不存在 就创建
				 file.createNewFile(); 
			}
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}
	
	public Enter(Socket so,boolean auto_Enter_flag) {
		super();
		socket = so;
		this.auto_Enter_flag = auto_Enter_flag;
		initView();
		initData();	//使用 保存的user记录
	}
	
	public void initView() {
		ImageIcon img = new  ImageIcon(getClass().getClassLoader().getResource("img_enter.jpg"));
        this.setSize(img.getIconWidth()+15, img.getIconHeight()+20);
        this.setTitle("登录");//设置窗口标题
        this.getContentPane().setLayout(null);//设置布局控制器
        
		
        JLabel BeiJing = new JLabel(img);
        BeiJing.setBounds(0, -15, img.getIconWidth(), img.getIconHeight());
         
		label_err = new JLabel();
		label_err.setBounds(0,83,200,20);
		label_err.setForeground(Color.red);
		label_err.setText("");
		
		text_usernamae = new JTextField();
		text_usernamae.setBounds(120,113,190,25);
		text_usernamae.setBorder(null);   	//去掉边框

		text_pwd = new JPasswordField();
		text_pwd.setBounds(120,153,190,25);
		text_pwd.setEchoChar('*');
		text_pwd.setBorder(null);   	//去掉边框
		
		
		button_to_Login = new JButton();
		button_to_Login.setBounds(283,193,78,20);
		button_to_Login.setText("注册账号");
		button_to_Login.setContentAreaFilled(false);   //去掉背景
		button_to_Login.setBorder(null);   	//去掉边框
		button_to_Login.addActionListener(new class_to_Login());//添加监听器类，其主要的响应都由监听器类的方法实现

		
		button_Submit = new JButton();
        button_Submit.setBounds(90,221,256,41);
        button_Submit.setText("");
        button_Submit.setContentAreaFilled(false);   //去掉背景
        button_Submit.setBorder(null);   	//去掉边框
        button_Submit.addActionListener(new class_button_Submit());//添加监听器类，其主要的响应都由监听器类的方法实现
       
        
        auto_Enter = new JCheckBox("自动登录");
		auto_Enter.setBounds(90, 192, 78, 23);
		auto_Enter.setContentAreaFilled(false);   //去掉背景
		auto_Enter.addActionListener(new class_Auto_Enter());//添加监听器类，其主要的响应都由监听器类的方法实现
		
		remember_pwd = new JCheckBox("记住密码");
		remember_pwd.setBounds(190, 192, 78, 23);
		remember_pwd.setContentAreaFilled(false);   //去掉背景
		remember_pwd.addActionListener(new class_Remember_pwd());//添加监听器类，其主要的响应都由监听器类的方法实现
		
		getContentPane().add(label_err,null);
		getContentPane().add(text_usernamae,null);
		getContentPane().add(text_pwd,null);
		getContentPane().add(auto_Enter);
		getContentPane().add(remember_pwd);
		getContentPane().add(button_Submit,null);
		getContentPane().add(button_to_Login,null);	
		getContentPane().add(BeiJing);
		
		//加载 （add）完  再加这几句
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一定要设置关闭
				setVisible(true);
				setLocationRelativeTo(null);
	}
	
	public void initData() {
		try {
			FileReader fileReader = new FileReader("user_data.properties");
			Properties properties = new Properties();
			properties.load(fileReader);
			System.out.println("本地记录   auto_Enter ："+properties.getProperty("auto_Enter"));
			System.out.println("本地记录   remember_pwd ："+properties.getProperty("remember_pwd"));
			text_usernamae.setText( properties.getProperty("username") );
			if ("true".equals(  properties.getProperty("remember_pwd") )) {
				remember_pwd.setSelected(true);
				text_pwd.setText( properties.getProperty("pwd") );
			}
			if ("true".equals(  properties.getProperty("auto_Enter") )) {
				auto_Enter.setSelected(true);
					System.out.println("auto_Enter_flag  "+auto_Enter_flag);
				if (auto_Enter_flag) {
					System.out.println("准备  自动登录");
					enter_enter();
					System.out.println("自动登录");
				}
			}
			fileReader.close();
		} catch (  IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	
	class class_Auto_Enter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			if (auto_Enter.isSelected()) {
				//如果 按了自动登录  会同时 记住密码
				remember_pwd.setSelected(true);
				System.out.println("点击 自动登录");
				(new class_Remember_pwd()).actionPerformed(null);
			}
		}
		
	}
	
	class class_Remember_pwd implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成的方法存根
			System.out.println("点击 记住密码");
			
		}
		
	}
	
	class class_to_Login implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
				Login login =new Login(socket );		
				login.setLocationRelativeTo(Enter.this);
		}
		
	}
	
	public String get_Socket_Data() {
		synchronized (Manage.enter) {
			System.out.println("解锁");
			Manage.enter.notifyAll();
			return Manage.getData();
		}
	}
	
	public void enter_enter() {

		// TODO 自动生成的方法存根
		String user_name = text_usernamae.getText().toString();
		String pwd = text_pwd.getText().toString();
		System.out.println(user_name);
		if (user_name.isEmpty()||pwd.isEmpty()) {
			label_err.setText("请输入用户名和密码");
			return;
		}else {
			String flag_Enter = "";
			try {
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("Enter");
				out.writeUTF(user_name);
				out.writeUTF(pwd);
				flag_Enter = get_Socket_Data();
				System.out.println(flag_Enter);
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				System.out.println("flag获取错误");
			}
			
			if ("OK_Enter".equals(flag_Enter)) {
				try {
					FileWriter fileWriter = new FileWriter("user_data.properties");
					Properties properties = new Properties();
					properties.setProperty("username", user_name);
					if (remember_pwd.isSelected()) {	//记住密码
						properties.setProperty("remember_pwd", "true");
						properties.setProperty("pwd", pwd);
					}else {
						properties.setProperty("remember_pwd", "false");
					}
					if (auto_Enter.isSelected()) {
						properties.setProperty("auto_Enter", "true");
					}else {
						properties.setProperty("auto_Enter", "false");
					}
					properties.store(fileWriter, null);
					fileWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				LiaoTian liaoTian = new LiaoTian(user_name,socket);		
				liaoTian.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一定要设置关闭
				liaoTian.setVisible(true);
				liaoTian.setLocationRelativeTo(this);
				dispose(); 	//关闭窗口
				
			}else {
				label_err.setText(flag_Enter);
				return;
			}
			
		}
		
		
	
	}
	
	class class_button_Submit implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			enter_enter();
		}

		
	}
	
}

