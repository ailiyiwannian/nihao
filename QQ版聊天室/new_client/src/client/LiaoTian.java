package client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class LiaoTian extends JFrame implements ActionListener{
	Socket socket;
	String username;
	DataInputStream in;
	DataOutputStream out;
	Thread Thread_read;
	
	class_NoEnter_user_button  NoEnter_user = new class_NoEnter_user_button(this);
	class_YesEnter_user_button  YesEnter_user = new class_YesEnter_user_button(this);
	
	JMenuBar menubar;   //�˵���
    JMenu menu; //�˵�
    JMenuItem itemOpen, itemSave;   //�˵���
    JLabel jLabel_tible,jlable_username,jlable_blank_2;
	JLabel label_num,label_bj_sum;
	JButton button_send,button_setPwd,button_quit;
	JScrollPane jsp_liaotian,jsp_user_list;
	JTextArea  text_liaotian;
	JTextField text_input;
	JList<Button> JList_user_list;
	
	int width = 430;
	int height = 410;
	
	public LiaoTian(String username ,Socket socket) {
		super();
		this.socket = socket;
		this.username = username;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("in out ����");
		}
		//����Ҫ��Ҫ������
//		setUndecorated(true);
		
		this.setSize(width,height);	//420,380
        this.setTitle("������");//���ô��ڱ���
        this.getContentPane().setLayout(null);//���ò��ֿ�����
         
       
      
  //*************  ������ı�����    ***********************************
        menubar = new JMenuBar();
        menubar.setBackground(Color.white);
        
        jLabel_tible = new JLabel("   ��ӭʹ��������                                                     ");
        menubar.add(jLabel_tible);
 
    	//���� ��������������ұ�
        jlable_username = new JLabel("�û���: "+username +getBlank( 7 -username.length()));	
        menubar.add(jlable_username);
        
        button_setPwd = new JButton("�޸�����"); 
        button_setPwd.setSize(20, 30);
        button_setPwd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		(new Thread(new thread_setPwd())).start();

        	}
        });
        
        button_quit = new JButton("�˳���¼"); 
        button_quit.setSize(20, 30);
        button_quit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 try {
					out.writeUTF("quit");
					dispose(); 	//�رմ���
//					myMain_Client.enter.setVisible(true);
//					myMain_Client.obj = new myMain_Client();s
					return;
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
        	}
        });
        
        menu = new JMenu(" "+"�û�����"+" ");
        menu.setForeground(Color.blue);
        menu.add(button_setPwd);
        menu.add(button_quit);
        menubar.add(menu);
        
       
        this.setJMenuBar(menubar);
        
        
   //*************************************************************************
        
        text_liaotian = new JTextArea ();
        text_liaotian.setLineWrap(true);  //�Զ�����
        jsp_liaotian=new JScrollPane(text_liaotian); //������
        jsp_liaotian.setBounds(10,10,290,300);
        
        text_input = new JTextField ();
        text_input.setBounds(10,315,290,25);
        
        
        JList_user_list=new JList<Button>();
        jsp_user_list=new JScrollPane(JList_user_list); //������

        label_num = new JLabel();
        label_num.setBounds(310,10,90,25);
        label_num.setText("����������xx");
        	
        
		button_send = new JButton();
        button_send.setBounds(320,314,60,25);
        button_send.setText("�ύ");
        button_send.addActionListener(this);//��Ӽ������࣬����Ҫ����Ӧ���ɼ�������ķ���ʵ��
        
        getContentPane().add(label_num,null);
        getContentPane().add(jsp_liaotian,null);
        getContentPane().add(text_input,null);
        getContentPane().add(button_send,null);
        getContentPane().add(jsp_user_list, null);
        
        ImageIcon img = new  ImageIcon(getClass().getClassLoader().getResource("img_liaotian.jpg"));
        img.setImage(img.getImage().getScaledInstance(420, 350,Image.SCALE_DEFAULT));
        JLabel BeiJing = new JLabel(img);
        BeiJing.setBounds(-10, 0, 430, 350);
        getContentPane().add(BeiJing);
        
        
        Thread_read = new Thread(new liaotian_read());
        Thread_read.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			out.writeUTF("qunliao");  //��ʶ�� ����Ⱥ������
			out.writeUTF(username);
			out.writeUTF(text_input.getText().toString());
			text_input.setText("");
			
		} catch (IOException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		
	}

	
	
	//�û��б� �� �����ߵ��û� ��ť
	class class_NoEnter_user_button implements ActionListener {
		LiaoTian obj;
		public class_NoEnter_user_button(LiaoTian obj) {
			this.obj = obj;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//(e.getActionCommand()).replace(" ",""); //ȥ�� �ո�������
			JOptionPane.showMessageDialog(obj,"�û� ��"+(e.getActionCommand()).replace(" ","")+" ������");

			 }
	}
	

	//�û��б� �� ���ߵ��û� ��ť
	class class_YesEnter_user_button implements ActionListener {
		LiaoTian obj;
		public class_YesEnter_user_button(LiaoTian obj) {
			this.obj = obj;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String obj_username = (e.getActionCommand()).replace(" ",""); //ȥ�� �ո�������
			String My_username = username;
			if (obj_username.equals(My_username)) {	   //  �Լ����Լ�
				JOptionPane.showMessageDialog(null , "   ��ʲô��������Լ�˵��", "ƤƤϺ����Լ����� �r(�s���t)�q",JOptionPane.PLAIN_MESSAGE);
			}else {
				
				String data = (String)JOptionPane.showInputDialog(obj,"˽�Ÿ� "+obj_username,"˽��",JOptionPane.PLAIN_MESSAGE,null,null,null);  
				try {
					out.writeUTF("siliao");
					out.writeUTF(obj_username);
					out.writeUTF(My_username);
					out.writeUTF(data);
					String temp = text_liaotian.getText().toString();
					text_liaotian.setText(temp + "˽�ŷ��͸���"+obj_username+" ("+ "���� : "+data+ ")\r\n");
					
				} catch (IOException e1) {
					System.err.println("˽�� ���ʹ���");
				}
			}
		}
	}
	
	
	
	
	class liaotian_read implements Runnable{
		String form_user_name,data;
		String username_Enter_list,username_Sum_list;
		String [] username_Enter_array,username_Sum_array;
		String operator,temp;
		public boolean quertString(String scr ,String [] obj_arr) {
			for(String it : obj_arr) {
				if (it.equals(scr)) {
					return true;
				}
			}
			return false;
		}
		
		
	@Override
	public void run() {
		// ѭ������
		while(true) {
			try {
				synchronized (Manage.liaotian) {
					System.out.println("synchronized (Manage.liaotian)  ����һ��");
					//��һ�β���   notifyAll() �� liaotian.wait() ������ʱ�� ���һ��
					operator = Manage.getData();
					System.out.println("liaotian operator��" +operator );   ////////////////////
					
					if ("qunliao".equals(operator)) {
						//��ȡ����
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						form_user_name = Manage.getData();
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						data = Manage.getData();
						
						temp = text_liaotian.getText().toString();
						text_liaotian.setText(temp + form_user_name + " : "+data+ "\r\n");
						//���ù����� ���ײ�
						text_liaotian.setCaretPosition(text_liaotian.getDocument().getLength());
					}else if ("siliao".equals(operator)) {
						//��ȡ����
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						form_user_name = Manage.getData();
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						data = Manage.getData();
						
						temp = text_liaotian.getText().toString();
						text_liaotian.setText(temp + "˽����Ϣ   ("+form_user_name + " : "+data+ ")\r\n");
						//���ù����� ���ײ�
						text_liaotian.setCaretPosition(text_liaotian.getDocument().getLength());
					}else if("user_list".equals(operator)) {
						//��ȡ����
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						username_Enter_list = Manage.getData();
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						username_Sum_list = Manage.getData();
						//�ָ�����
						username_Enter_array = username_Enter_list.split(",&,");
						username_Sum_array = username_Sum_list.split(",&,");
						System.out.println(username_Enter_list+" "+"���� "+username_Enter_array.length+"����");
						System.out.println(username_Sum_list+" "+"���� "+username_Sum_array.length+"����");
						temp="";
						//���¼����µ����
						remove(jsp_user_list);
						JList_user_list=new JList<Button>();
						jsp_user_list=new JScrollPane(JList_user_list); //������
						jsp_user_list.setBounds(305,35,0,0);
//					     JList_user_list.setOpaque(false);	  //ȥ������
						jsp_user_list.setBorder(null);   	//ȥ���߿�
						add(jsp_user_list, null);
						//�������
						JList_user_list.setLayout(new GridLayout(username_Sum_array.length+1,1));
						JList_user_list.setPreferredSize(new Dimension(80,20*username_Sum_array.length));//����JList��С
						int hight = 20*(username_Sum_array.length+1);
						if (hight<273) {
							jsp_user_list.setSize(110, hight);
						}else
							jsp_user_list.setSize(110, 273);
					//�����������û��б�	
						//��������û�   ��ɫ����
						for(String it :username_Enter_array) {
							JButton btn = new JButton("     "+it);  //�ӿո� ����
							btn.setHorizontalAlignment(JButton.LEFT);	//�����
							btn.setBorder(null);   	//ȥ���߿�
//							btn.setContentAreaFilled(false);   //ȥ������
							btn.setBackground(Color.white);
							btn.addActionListener(YesEnter_user);
							JList_user_list.add(btn);
						}
//						��� �������û�   ��ɫ����
						for(String it :username_Sum_array) {
							
							if (!quertString(it, username_Enter_array)) {
								JButton btn = new JButton("       "+it);  //�ӿո� 
								btn.setHorizontalAlignment(JButton.LEFT);	//�����
								btn.setBorder(null);   	//ȥ���߿�
//								btn.setContentAreaFilled(false);   //ȥ������
								btn.setForeground(Color.gray);  //�����ߵ��û� ��ɫ����
								btn.setBackground(Color.white);
								btn.addActionListener(NoEnter_user);
								JList_user_list.add(btn);
							}
						}
						label_num.setText("Ⱥ��Ա��"+username_Enter_array.length+"/"+username_Sum_array.length);
					}else {
						System.out.println("********************����*********************");
					}
					
					Manage.liaotian.notifyAll();
					Manage.liaotian.wait();
				}
				
			} catch ( Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				
			}
		}
			
	}
		
}
	
	class thread_setPwd implements Runnable{
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			SetPwd setPwd =new SetPwd(socket);
			setPwd.setLocationRelativeTo(LiaoTian.this);
		}
		
	}
	
	
	
	public String getBlank(int x) {
		String string="";
		for(int i=0; i<x; i++) {
			string +=" ";
		}
		return string;
	}
}
