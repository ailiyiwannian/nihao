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
	
	JMenuBar menubar;   //菜单条
    JMenu menu; //菜单
    JMenuItem itemOpen, itemSave;   //菜单项
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
			System.out.println("in out 错误");
		}
		//设置要不要标题栏
//		setUndecorated(true);
		
		this.setSize(width,height);	//420,380
        this.setTitle("聊天室");//设置窗口标题
        this.getContentPane().setLayout(null);//设置布局控制器
         
       
      
  //*************  最上面的标题栏    ***********************************
        menubar = new JMenuBar();
        menubar.setBackground(Color.white);
        
        jLabel_tible = new JLabel("   欢迎使用聊天室                                                     ");
        menubar.add(jLabel_tible);
 
    	//用来 把其他组件挤到右边
        jlable_username = new JLabel("用户名: "+username +getBlank( 7 -username.length()));	
        menubar.add(jlable_username);
        
        button_setPwd = new JButton("修改密码"); 
        button_setPwd.setSize(20, 30);
        button_setPwd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		(new Thread(new thread_setPwd())).start();

        	}
        });
        
        button_quit = new JButton("退出登录"); 
        button_quit.setSize(20, 30);
        button_quit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 try {
					out.writeUTF("quit");
					dispose(); 	//关闭窗口
//					myMain_Client.enter.setVisible(true);
//					myMain_Client.obj = new myMain_Client();s
					return;
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
        	}
        });
        
        menu = new JMenu(" "+"用户操作"+" ");
        menu.setForeground(Color.blue);
        menu.add(button_setPwd);
        menu.add(button_quit);
        menubar.add(menu);
        
       
        this.setJMenuBar(menubar);
        
        
   //*************************************************************************
        
        text_liaotian = new JTextArea ();
        text_liaotian.setLineWrap(true);  //自动换行
        jsp_liaotian=new JScrollPane(text_liaotian); //滚动条
        jsp_liaotian.setBounds(10,10,290,300);
        
        text_input = new JTextField ();
        text_input.setBounds(10,315,290,25);
        
        
        JList_user_list=new JList<Button>();
        jsp_user_list=new JScrollPane(JList_user_list); //滚动条

        label_num = new JLabel();
        label_num.setBounds(310,10,90,25);
        label_num.setText("在线人数：xx");
        	
        
		button_send = new JButton();
        button_send.setBounds(320,314,60,25);
        button_send.setText("提交");
        button_send.addActionListener(this);//添加监听器类，其主要的响应都由监听器类的方法实现
        
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
			out.writeUTF("qunliao");  //标识符 代表群聊数据
			out.writeUTF(username);
			out.writeUTF(text_input.getText().toString());
			text_input.setText("");
			
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
	}

	
	
	//用户列表 中 不在线的用户 按钮
	class class_NoEnter_user_button implements ActionListener {
		LiaoTian obj;
		public class_NoEnter_user_button(LiaoTian obj) {
			this.obj = obj;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//(e.getActionCommand()).replace(" ",""); //去掉 空格后的名字
			JOptionPane.showMessageDialog(obj,"用户 ："+(e.getActionCommand()).replace(" ","")+" 不在线");

			 }
	}
	

	//用户列表 中 在线的用户 按钮
	class class_YesEnter_user_button implements ActionListener {
		LiaoTian obj;
		public class_YesEnter_user_button(LiaoTian obj) {
			this.obj = obj;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String obj_username = (e.getActionCommand()).replace(" ",""); //去掉 空格后的名字
			String My_username = username;
			if (obj_username.equals(My_username)) {	   //  自己点自己
				JOptionPane.showMessageDialog(null , "   有什么心理话想对自己说吗", "皮皮虾你点自己干嘛 ╮(╯▽╰)╭",JOptionPane.PLAIN_MESSAGE);
			}else {
				
				String data = (String)JOptionPane.showInputDialog(obj,"私信给 "+obj_username,"私聊",JOptionPane.PLAIN_MESSAGE,null,null,null);  
				try {
					out.writeUTF("siliao");
					out.writeUTF(obj_username);
					out.writeUTF(My_username);
					out.writeUTF(data);
					String temp = text_liaotian.getText().toString();
					text_liaotian.setText(temp + "私信发送给："+obj_username+" ("+ "内容 : "+data+ ")\r\n");
					
				} catch (IOException e1) {
					System.err.println("私聊 发送错误");
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
		// 循环监听
		while(true) {
			try {
				synchronized (Manage.liaotian) {
					System.out.println("synchronized (Manage.liaotian)  解锁一次");
					//第一次不加   notifyAll() 和 liaotian.wait() 结束的时候 多加一次
					operator = Manage.getData();
					System.out.println("liaotian operator：" +operator );   ////////////////////
					
					if ("qunliao".equals(operator)) {
						//获取数据
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						form_user_name = Manage.getData();
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						data = Manage.getData();
						
						temp = text_liaotian.getText().toString();
						text_liaotian.setText(temp + form_user_name + " : "+data+ "\r\n");
						//设置滚动条 到底部
						text_liaotian.setCaretPosition(text_liaotian.getDocument().getLength());
					}else if ("siliao".equals(operator)) {
						//获取数据
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						form_user_name = Manage.getData();
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						data = Manage.getData();
						
						temp = text_liaotian.getText().toString();
						text_liaotian.setText(temp + "私聊信息   ("+form_user_name + " : "+data+ ")\r\n");
						//设置滚动条 到底部
						text_liaotian.setCaretPosition(text_liaotian.getDocument().getLength());
					}else if("user_list".equals(operator)) {
						//获取数据
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						username_Enter_list = Manage.getData();
						Manage.liaotian.notifyAll();
						Manage.liaotian.wait();
						username_Sum_list = Manage.getData();
						//分割数据
						username_Enter_array = username_Enter_list.split(",&,");
						username_Sum_array = username_Sum_list.split(",&,");
						System.out.println(username_Enter_list+" "+"（共 "+username_Enter_array.length+"个）");
						System.out.println(username_Sum_list+" "+"（共 "+username_Sum_array.length+"个）");
						temp="";
						//重新加载新的组件
						remove(jsp_user_list);
						JList_user_list=new JList<Button>();
						jsp_user_list=new JScrollPane(JList_user_list); //滚动条
						jsp_user_list.setBounds(305,35,0,0);
//					     JList_user_list.setOpaque(false);	  //去掉背景
						jsp_user_list.setBorder(null);   	//去掉边框
						add(jsp_user_list, null);
						//设置组件
						JList_user_list.setLayout(new GridLayout(username_Sum_array.length+1,1));
						JList_user_list.setPreferredSize(new Dimension(80,20*username_Sum_array.length));//设置JList大小
						int hight = 20*(username_Sum_array.length+1);
						if (hight<273) {
							jsp_user_list.setSize(110, hight);
						}else
							jsp_user_list.setSize(110, 273);
					//往组件上添加用户列表	
						//添加在线用户   黑色字体
						for(String it :username_Enter_array) {
							JButton btn = new JButton("     "+it);  //加空格 补齐
							btn.setHorizontalAlignment(JButton.LEFT);	//左对齐
							btn.setBorder(null);   	//去掉边框
//							btn.setContentAreaFilled(false);   //去掉背景
							btn.setBackground(Color.white);
							btn.addActionListener(YesEnter_user);
							JList_user_list.add(btn);
						}
//						添加 不在线用户   灰色字体
						for(String it :username_Sum_array) {
							
							if (!quertString(it, username_Enter_array)) {
								JButton btn = new JButton("       "+it);  //加空格 
								btn.setHorizontalAlignment(JButton.LEFT);	//左对齐
								btn.setBorder(null);   	//去掉边框
//								btn.setContentAreaFilled(false);   //去掉背景
								btn.setForeground(Color.gray);  //不在线的用户 灰色字体
								btn.setBackground(Color.white);
								btn.addActionListener(NoEnter_user);
								JList_user_list.add(btn);
							}
						}
						label_num.setText("群成员："+username_Enter_array.length+"/"+username_Sum_array.length);
					}else {
						System.out.println("********************错误*********************");
					}
					
					Manage.liaotian.notifyAll();
					Manage.liaotian.wait();
				}
				
			} catch ( Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				
			}
		}
			
	}
		
}
	
	class thread_setPwd implements Runnable{
		@Override
		public void run() {
			// TODO 自动生成的方法存根
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
