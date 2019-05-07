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
				// �ļ��������� �ʹ���
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
		initData();	//ʹ�� �����user��¼
	}
	
	public void initView() {
		ImageIcon img = new  ImageIcon(getClass().getClassLoader().getResource("img_enter.jpg"));
        this.setSize(img.getIconWidth()+15, img.getIconHeight()+20);
        this.setTitle("��¼");//���ô��ڱ���
        this.getContentPane().setLayout(null);//���ò��ֿ�����
        
		
        JLabel BeiJing = new JLabel(img);
        BeiJing.setBounds(0, -15, img.getIconWidth(), img.getIconHeight());
         
		label_err = new JLabel();
		label_err.setBounds(0,83,200,20);
		label_err.setForeground(Color.red);
		label_err.setText("");
		
		text_usernamae = new JTextField();
		text_usernamae.setBounds(120,113,190,25);
		text_usernamae.setBorder(null);   	//ȥ���߿�

		text_pwd = new JPasswordField();
		text_pwd.setBounds(120,153,190,25);
		text_pwd.setEchoChar('*');
		text_pwd.setBorder(null);   	//ȥ���߿�
		
		
		button_to_Login = new JButton();
		button_to_Login.setBounds(283,193,78,20);
		button_to_Login.setText("ע���˺�");
		button_to_Login.setContentAreaFilled(false);   //ȥ������
		button_to_Login.setBorder(null);   	//ȥ���߿�
		button_to_Login.addActionListener(new class_to_Login());//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��

		
		button_Submit = new JButton();
        button_Submit.setBounds(90,221,256,41);
        button_Submit.setText("");
        button_Submit.setContentAreaFilled(false);   //ȥ������
        button_Submit.setBorder(null);   	//ȥ���߿�
        button_Submit.addActionListener(new class_button_Submit());//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��
       
        
        auto_Enter = new JCheckBox("�Զ���¼");
		auto_Enter.setBounds(90, 192, 78, 23);
		auto_Enter.setContentAreaFilled(false);   //ȥ������
		auto_Enter.addActionListener(new class_Auto_Enter());//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��
		
		remember_pwd = new JCheckBox("��ס����");
		remember_pwd.setBounds(190, 192, 78, 23);
		remember_pwd.setContentAreaFilled(false);   //ȥ������
		remember_pwd.addActionListener(new class_Remember_pwd());//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��
		
		getContentPane().add(label_err,null);
		getContentPane().add(text_usernamae,null);
		getContentPane().add(text_pwd,null);
		getContentPane().add(auto_Enter);
		getContentPane().add(remember_pwd);
		getContentPane().add(button_Submit,null);
		getContentPane().add(button_to_Login,null);	
		getContentPane().add(BeiJing);
		
		//���� ��add����  �ټ��⼸��
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//һ��Ҫ���ùر�
				setVisible(true);
				setLocationRelativeTo(null);
	}
	
	public void initData() {
		try {
			FileReader fileReader = new FileReader("user_data.properties");
			Properties properties = new Properties();
			properties.load(fileReader);
			System.out.println("���ؼ�¼   auto_Enter ��"+properties.getProperty("auto_Enter"));
			System.out.println("���ؼ�¼   remember_pwd ��"+properties.getProperty("remember_pwd"));
			text_usernamae.setText( properties.getProperty("username") );
			if ("true".equals(  properties.getProperty("remember_pwd") )) {
				remember_pwd.setSelected(true);
				text_pwd.setText( properties.getProperty("pwd") );
			}
			if ("true".equals(  properties.getProperty("auto_Enter") )) {
				auto_Enter.setSelected(true);
					System.out.println("auto_Enter_flag  "+auto_Enter_flag);
				if (auto_Enter_flag) {
					System.out.println("׼��  �Զ���¼");
					enter_enter();
					System.out.println("�Զ���¼");
				}
			}
			fileReader.close();
		} catch (  IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
	
	class class_Auto_Enter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			if (auto_Enter.isSelected()) {
				//��� �����Զ���¼  ��ͬʱ ��ס����
				remember_pwd.setSelected(true);
				System.out.println("��� �Զ���¼");
				(new class_Remember_pwd()).actionPerformed(null);
			}
		}
		
	}
	
	class class_Remember_pwd implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO �Զ����ɵķ������
			System.out.println("��� ��ס����");
			
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
			System.out.println("����");
			Manage.enter.notifyAll();
			return Manage.getData();
		}
	}
	
	public void enter_enter() {

		// TODO �Զ����ɵķ������
		String user_name = text_usernamae.getText().toString();
		String pwd = text_pwd.getText().toString();
		System.out.println(user_name);
		if (user_name.isEmpty()||pwd.isEmpty()) {
			label_err.setText("�������û���������");
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
				// TODO �Զ����ɵ� catch ��
				System.out.println("flag��ȡ����");
			}
			
			if ("OK_Enter".equals(flag_Enter)) {
				try {
					FileWriter fileWriter = new FileWriter("user_data.properties");
					Properties properties = new Properties();
					properties.setProperty("username", user_name);
					if (remember_pwd.isSelected()) {	//��ס����
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
				liaoTian.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//һ��Ҫ���ùر�
				liaoTian.setVisible(true);
				liaoTian.setLocationRelativeTo(this);
				dispose(); 	//�رմ���
				
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

