package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import dao.file_save.FILE_Login_Enter;
import dao.sql.SQL_Login_Enter;
import dao.sql.SQL_Record;


public class Core implements Runnable{
	
	static Logger logger = Logger.getLogger(Core.class);
	static ArrayList<Core> user_list= new ArrayList<Core>();
	static Interface_Login_Enter Login_Enter_Object;
	Socket socket;
	UserData userData;
	DataInputStream in;
	DataOutputStream out;
	
	static {
		if (myMain_Server.if_usr_sql) {
			Login_Enter_Object = new SQL_Login_Enter();
		}else {
			Login_Enter_Object = new FILE_Login_Enter();
		}
	}
	
	public  Core(Socket socket) throws IOException  {
		this.socket = socket;
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());				
	}
	
	@Override
	public void run() {
		String operator;
		String from_username;
		String data;
		try {
			while(true) {
				operator = in.readUTF();
				logger.debug("��¼/ע��  operator Ϊ��"+operator);
				if ("Enter".equals(operator)) {
					userData = get_Enter_User();
					if (userData != null) {
						break;
					}
				}else if ("if_Username_Exist".equals(operator)||"Login".equals(operator)) {
					finish_Login(operator);
				}
				
			}

			user_list.add(this);
			logger.debug(userData.username+" ������");
			out.writeUTF("flag_Enter,,,"+"OK_Enter");
			broadcast_connection_flag();	
			
				//���ϼ���
			while(true) {
				operator = in.readUTF();
				logger.debug("����/��������  operator Ϊ��"+operator);
				
				if ("qunliao".equals(operator)) {  //��ʶ�� ����Ⱥ������
					from_username = in.readUTF();
					data = in.readUTF();
					logger.debug("�������յ�    [ "+from_username+"  : "+data+" ]");
					broadcast(from_username, data);
				}else if ("siliao".equals(operator)) {  //��ʶ�� ����˽������
					String obj_username = in.readUTF();
					from_username = in.readUTF();
					data = in.readUTF();
					for(Core it :user_list) {
						if (obj_username.equals(it.userData.username)) {
							it.out.writeUTF("siliao");
							it.out.writeUTF("siliao,,,"+from_username);
							it.out.writeUTF("siliao,,,"+data+" ");	//��β�ӿո� ����ֹdataΪ�� ���޷����ͻ��� manage �ָ�
							break;
						}
					}
				}else if ("if_User_Pwd".equals(operator)||"set_Pwd".equals(operator)) {//��ʶ�� �����޸�����
					finish_set_pwd(operator);
				}else if ("quit".equals(operator)) {		//��ʶ�� �����˳���¼
					//�������û��б��г�ȥ   ���������� �㲥���ͻ���
					user_list.remove(this);
					broadcast_connection_flag();
					out.writeUTF("quit");
					logger.debug(this.userData.username +" �Ͽ�����");
					return;  //�ر��߳�
				}
			
		}
			
		} catch (IOException e) {
			//�Ͽ�����
			if (userData !=null) {
				user_list.remove(this);
				logger.debug(this.userData.username +" �Ͽ�����");
			}
			try {
				//�������߳�Ա����
				broadcast_connection_flag();
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
		
	}
	
	//����ֵ �û��Ƿ��ظ���¼
	public boolean if_User_Enter(String nameuser_temp) throws IOException {
		for(Core it: user_list) {
			if(nameuser_temp.equals( it.userData.username) ) {
				out.writeUTF("flag_Enter,,,"+"���û��ѵ�¼�������ظ���¼");
				return true;
			}
		}
		
		return false;
		
	}
	
	//  �˺�/����Բ���  �ɹ����� �û���Ϣ  ʧ��null
	public UserData if_user_id_pwd(String username ,String pwd) throws IOException {
		try {
			
				boolean flag = Login_Enter_Object.if_Username_pwd(username, pwd);
				if (flag) {
					return (new UserData(username, pwd));
				}else
					return null;
			
		} catch (SQLException e) {
			logger.info("if_user_id_pwd ����");
			out.writeUTF("������ ����");
		}
		return null;
	}
	
	public UserData get_Enter_User() throws IOException {
		UserData UserData = null;
				String username_temp = in.readUTF();
				String pwd_temp = in.readUTF();
				//�˺Ż�����Բ���
				UserData = if_user_id_pwd(username_temp,pwd_temp);
				if (UserData ==null ) {
					out.writeUTF("flag_Enter,,,"+"�û������������"); 
					return null;
				}
				//�ж� �û��Ƿ��ظ���¼    ��out.writeUTF �����ͻ���
				if (if_User_Enter(username_temp)) 
					return null;
				
		return UserData;
	}
	
	public void finish_Login(String operator) throws IOException {
		logger.debug("������ finish_Login() ");
		try {
			if ("if_Username_Exist".equals(operator)) {
				String username = in.readUTF();
				boolean flag = Login_Enter_Object.if_Username_Exist(username);
				logger.debug("SQL_Login_Enter.if_Username_Exist(username) ���Ϊ "+flag);
				if (flag) {
					out.writeUTF("if_Username_Exist,,,"+"�û����ѱ�ע�� �뻻һ������");
				}else
					out.writeUTF("if_Username_Exist,,,"+"un_Exist");
					
				
			}else if ("Login".equals(operator)) {
				String username = in.readUTF();
				String pwd = in.readUTF();
				logger.debug("׼��ע��  �˺� :"+username+  "  ����  :"+pwd);
				Login_Enter_Object.login(username, pwd);
				out.writeUTF("flag_Login,,,"+"OK_Login");
				
			}
	
		} catch (Exception e) {
			logger.info("������  �˺�ע��ʧ��");
			out.writeUTF("flag_Login,,,"+"������  �˺�ע��ʧ��");
		}
		
		
		
		
	}
	
	public void finish_set_pwd(String operator) throws IOException {
		logger.debug("������ finish_set_pwd() ");
		try {
			//��֤  ������Բ���
			if ("if_User_Pwd".equals(operator)) {
				String old_pwd = in.readUTF();
				logger.info("������"+old_pwd);
				UserData temp = if_user_id_pwd(this.userData.username,old_pwd);	//����һ�������ݿ�����룬����userData.pwd
				if (temp !=null ) {
					out.writeUTF("if_User_Pwd,,,"+"OK_pwd");
					logger.info("������  ok");
				}else {
					out.writeUTF("if_User_Pwd,,,"+"ԭ���벻��ȷ");
					logger.info("if_User_Pwd,,,"+"������  ����");
				}
				return;
			}else if ("set_Pwd".equals(operator)) {    //�޸�����
				String new_pwd = in.readUTF();
				Login_Enter_Object.set_Pwd(this.userData.username, new_pwd);
				//�ȸķ�������  ����sql���� Ӱ��
				this.userData.pwd = new_pwd;
				out.writeUTF("set_Pwd,,,"+"OK_set_pwd");
				logger.info("�޸�����   ok");
			}
	
		} catch (Exception e) {
			logger.info("������  �޸�����ʧ��");
			logger.info("set_Pwd,,,"+"OK_set_pwd");
		}
		
		
		
		
		
	}
	
	public void broadcast(String formuser ,String data) throws IOException {
		String user_name_list = "�㲥�� : ";
		for(Core it :user_list) {
			it.out.writeUTF("qunliao");
			it.out.writeUTF("qunliao,,,"+formuser);
			it.out.writeUTF("qunliao,,,"+data+" ");	//��β�ӿո� ����ֹdataΪ�� ���޷����ͻ��� manage �ָ�
			user_name_list += it.userData.username;
		}
		logger.debug(user_name_list);
		save_liaotian(formuser,data);
	}

	public void broadcast_connection_flag() throws IOException {
		//���  �����û�����
		String username_list="";
		String sun_username_list = "";
		for(Core it :user_list) {
			username_list += it.userData.username+",&,";	//  ,,, �ָ���
		}
		logger.debug("�����û� ��"+username_list);
		//���  �����û�����
		ArrayList<String> sun_username_list_temp = Login_Enter_Object.get_Sum_username();
		for(String it :sun_username_list_temp) {
			sun_username_list += it+",&,";
		}
		logger.debug("ȫ���û� ��"+sun_username_list);
		//�㲥
		for(Core it :user_list) {
			it.out.writeUTF("user_list");
//			it.out.writeUTF("����������"+user_list.size());    //�ɿͻ���ͳ�ƾͺ�
			it.out.writeUTF("user_list,,,"+username_list);
			it.out.writeUTF("user_list,,,"+sun_username_list);
		}
		
	}
	
	public void save_liaotian(String formuser ,String data) throws IOException {
		logger.debug("save_liaotian��������");
		
		if(myMain_Server.if_usr_sql) {
			//��ʷ��¼�������ݿ�
			try {//�����¼�����ݿ�
				SQL_Record.insert(formuser, data);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				logger.info("���ݲ������ݿ���ʧ��-----------------------");
			}
		}
	}
}

