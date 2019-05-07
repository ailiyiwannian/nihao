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
        this.setTitle("�޸�����");//���ô��ڱ���
        this.getContentPane().setLayout(null);//���ò��ֿ�����
        //�����������
        this.setAlwaysOnTop(true);        
        
        System.out.println("�޸�����ҳ��");
		label_old_pwd = new JLabel();
		label_old_pwd.setBounds(10,5,200,20);
		label_old_pwd.setText("������ԭ����");
		
		label_pwd = new JLabel();
		label_pwd.setBounds(30,67,52,20);
		label_pwd.setText("����");
		
		label_again_pwd = new JLabel();
		label_again_pwd.setBounds(20,102,72,20);
		label_again_pwd.setText("ȷ������");
		

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
        button.setText("�޸�");
        button.addActionListener(this);//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��

		
        
		getContentPane().add(label_old_pwd,null);
		getContentPane().add(label_pwd,null);
		getContentPane().add(label_again_pwd,null);
		getContentPane().add(label_err_old_pwd,null);
		getContentPane().add(label_error_again_pwd,null);
		getContentPane().add(text_old_pwd,null);
		getContentPane().add(text_pwd,null);
		getContentPane().add(text_again_pwd,null);
		getContentPane().add(button,null);
		
		//���� ��add����  �ټ��⼸��
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//һ��Ҫ���ùر�
		setVisible(true);
//		setLocationRelativeTo(null);
	}
	
	public String get_Socket_Data() {
		synchronized (Manage.set_pwd) {
			System.out.println("set_pwd ����");
			Manage.set_pwd.notifyAll();
			return Manage.getData();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		boolean flag = true;
		String old_pwd = text_old_pwd.getText().toString();
		String new_pwd = text_pwd.getText().toString();
		String again_pwd = text_again_pwd.getText().toString();
		//�������
		if (new_pwd.isEmpty()||again_pwd.isEmpty()) {
			label_error_again_pwd.setText("����������");
			flag = false;
		}else if (!new_pwd.equals(again_pwd)) {
			label_error_again_pwd.setText("2�����벻һ��");
			System.out.println("����  ��"+new_pwd +"  and  "+again_pwd);
			flag = false;
		}else {
			label_error_again_pwd.setText(null);
		}
		//��� �������Ƿ���ȷ
		if (old_pwd.isEmpty()) {
			label_err_old_pwd.setText("�����������");
			flag = false;
		}else {		//  ������ ��� �������Ƿ���ȷ
			try {
				String if_User_Pwd = null;
				out.writeUTF("if_User_Pwd");
				out.writeUTF(old_pwd);
				System.out.println("���� ����");
				if_User_Pwd = get_Socket_Data();
				
				// �ж� �Ƿ�ע��ɹ� �û����Ƿ�ռ�� �뻻һ������
				if ("OK_pwd".equals(if_User_Pwd)) {
					label_err_old_pwd.setText("");
					System.out.println("OK_pwd ��������ȷ");
				}else {
					label_err_old_pwd.setText(if_User_Pwd);
					flag = false;
				}		
				
			} catch (IOException  e1) {
				System.out.println("user_Exist��ȡ����");
			}
									
		}
		System.out.println("�Ƿ������޸�����    :"+flag);
		//�����û��������� �����û�  
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
				System.out.println("user_Exist��ȡ����");
				}
								
				
			dispose(); 	//�رմ���

		}
		

			
		
	}
	
	
	
}
