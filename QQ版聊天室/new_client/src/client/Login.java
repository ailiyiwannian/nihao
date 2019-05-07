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
import javax.swing.UIManager;



public class Login extends JFrame implements ActionListener{
	JLabel label_username,label_error_username;
	JLabel label_pwd,label_again_pwd,label_error_again_pwd;
	JTextField text_username;
	JPasswordField text_pwd,text_again_pwd;
	JButton button;
	Socket socket;
	DataOutputStream out;
	
	public Login(Socket so ) {	
		super();
		socket = so;
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}
        this.setSize(285,215);
        this.setTitle("ע��");//���ô��ڱ���
        this.getContentPane().setLayout(null);//���ò��ֿ�����
 
        System.out.println("ע��ҳ��");
		label_username = new JLabel();
		label_username.setBounds(10,5,200,20);
		label_username.setText("��ȡһ���û���");
		
		label_pwd = new JLabel();
		label_pwd.setBounds(30,67,52,20);
		label_pwd.setText("����");
		
		label_again_pwd = new JLabel();
		label_again_pwd.setBounds(20,102,72,20);
		label_again_pwd.setText("ȷ������");
		

		label_error_username = new JLabel();
		label_error_username.setBounds(77,47,200,20);
		label_error_username.setForeground(Color.red);
		label_error_username.setText("");
		
		
		label_error_again_pwd = new JLabel();
		label_error_again_pwd.setBounds(77,120,200,20);
		label_error_again_pwd.setForeground(Color.red);
		label_error_again_pwd.setText("");
		
		
		text_username = new JTextField();
		text_username.setBounds(77,25,160,25);

		text_pwd = new JPasswordField();
		text_pwd.setBounds(77,65,160,25);
		text_pwd.setEchoChar('*');

		text_again_pwd = new JPasswordField();
		text_again_pwd.setBounds(77,100,160,25);
		text_again_pwd.setEchoChar('*');

		
		button = new JButton();
        button.setBounds(101,145,60,25);
        button.setText("ע��");
        button.addActionListener(this);//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��

		
        
		getContentPane().add(label_username,null);
		getContentPane().add(label_pwd,null);
		getContentPane().add(label_again_pwd,null);
		getContentPane().add(label_error_username,null);
		getContentPane().add(label_error_again_pwd,null);
		getContentPane().add(text_username,null);
		getContentPane().add(text_pwd,null);
		getContentPane().add(text_again_pwd,null);
		getContentPane().add(button,null);
		
		//���� ��add����  �ټ��⼸��
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//һ��Ҫ���ùر�
		setVisible(true);
	}
	
	public String get_Socket_Data() {
		synchronized (Manage.login) {
			System.out.println("����");
			Manage.login.notifyAll();
			return Manage.getData();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		boolean flag = true;
		String user_name = text_username.getText().toString();
		String pwd = text_pwd.getText().toString();
		String again_pwd = text_again_pwd.getText().toString();
		//�������
		if (pwd.isEmpty()||again_pwd.isEmpty()) {
			label_error_again_pwd.setText("����������");
			flag = false;
		}else if (!pwd.equals(again_pwd)) {
			label_error_again_pwd.setText("2�����벻һ��");
			System.out.println("����  ��"+pwd +"  and  "+again_pwd);
			flag = false;
		}else {
			label_error_again_pwd.setText(null);
		}
		System.out.println("106 106 106");
		//����û����Ƿ���ȷ
		if (user_name.isEmpty()) {
			label_error_username.setText("�������û���");
			flag = false;
		}else if ("name_list".equals(user_name)) {
			label_error_username.setText("ϵͳռ�� �뻻һ������");
			flag = false;
		}else {		//����û������Ѵ���   
			try {
				String if_Usernae_Exist = null;
				out.writeUTF("if_Username_Exist");
				out.writeUTF(user_name);
				System.out.println("����");
				if_Usernae_Exist = get_Socket_Data();
				
				// �ж� �Ƿ�ע��ɹ� �û����Ƿ�ռ�� �뻻һ������
				if ("un_Exist".equals(if_Usernae_Exist)) {
					label_error_username.setText("");
					System.out.println("un_Exist ���û�������ע��");
				}else {
					label_error_username.setText(if_Usernae_Exist);
					flag = false;
				}		
				
			} catch (IOException  e1) {
				System.out.println("user_Exist��ȡ����");
			}
									
		}
		System.out.println("�Ƿ�����ע������    :"+flag);
		//�����û��������� �����û�  
		if (flag) {
			try {
				String flag_Login;
				out.writeUTF("Login");
				out.writeUTF(user_name);
				out.writeUTF(pwd);
				flag_Login = get_Socket_Data();
				System.out.println(flag_Login);
				
				if (!"OK_Login".equals(flag_Login)) {
					label_error_username.setText("flag_Login");
				}
				
				
			} catch (IOException e1) {
				System.out.println("user_Exist��ȡ����");
				}
								
			
			dispose(); 	//�رմ���

		}
		

			
		
	}
	
	
	
}
