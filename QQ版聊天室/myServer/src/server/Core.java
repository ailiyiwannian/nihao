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
				logger.debug("登录/注册  operator 为："+operator);
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
			logger.debug(userData.username+" 已连接");
			out.writeUTF("flag_Enter,,,"+"OK_Enter");
			broadcast_connection_flag();	
			
				//不断监听
			while(true) {
				operator = in.readUTF();
				logger.debug("聊天/其他操作  operator 为："+operator);
				
				if ("qunliao".equals(operator)) {  //标识符 代表群聊数据
					from_username = in.readUTF();
					data = in.readUTF();
					logger.debug("服务器收到    [ "+from_username+"  : "+data+" ]");
					broadcast(from_username, data);
				}else if ("siliao".equals(operator)) {  //标识符 代表私聊数据
					String obj_username = in.readUTF();
					from_username = in.readUTF();
					data = in.readUTF();
					for(Core it :user_list) {
						if (obj_username.equals(it.userData.username)) {
							it.out.writeUTF("siliao");
							it.out.writeUTF("siliao,,,"+from_username);
							it.out.writeUTF("siliao,,,"+data+" ");	//结尾加空格 ，防止data为空 ，无法被客户端 manage 分割
							break;
						}
					}
				}else if ("if_User_Pwd".equals(operator)||"set_Pwd".equals(operator)) {//标识符 代表修改密码
					finish_set_pwd(operator);
				}else if ("quit".equals(operator)) {		//标识符 代表退出登录
					//从在线用户列表中除去   并更新数据 广播给客户端
					user_list.remove(this);
					broadcast_connection_flag();
					out.writeUTF("quit");
					logger.debug(this.userData.username +" 断开连接");
					return;  //关闭线程
				}
			
		}
			
		} catch (IOException e) {
			//断开连接
			if (userData !=null) {
				user_list.remove(this);
				logger.debug(this.userData.username +" 断开连接");
			}
			try {
				//更新在线成员人数
				broadcast_connection_flag();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		
	}
	
	//返回值 用户是否重复登录
	public boolean if_User_Enter(String nameuser_temp) throws IOException {
		for(Core it: user_list) {
			if(nameuser_temp.equals( it.userData.username) ) {
				out.writeUTF("flag_Enter,,,"+"该用户已登录，不能重复登录");
				return true;
			}
		}
		
		return false;
		
	}
	
	//  账号/密码对不对  成功返回 用户信息  失败null
	public UserData if_user_id_pwd(String username ,String pwd) throws IOException {
		try {
			
				boolean flag = Login_Enter_Object.if_Username_pwd(username, pwd);
				if (flag) {
					return (new UserData(username, pwd));
				}else
					return null;
			
		} catch (SQLException e) {
			logger.info("if_user_id_pwd 错误");
			out.writeUTF("服务器 错误");
		}
		return null;
	}
	
	public UserData get_Enter_User() throws IOException {
		UserData UserData = null;
				String username_temp = in.readUTF();
				String pwd_temp = in.readUTF();
				//账号或密码对不对
				UserData = if_user_id_pwd(username_temp,pwd_temp);
				if (UserData ==null ) {
					out.writeUTF("flag_Enter,,,"+"用户名或密码错误"); 
					return null;
				}
				//判断 用户是否重复登录    并out.writeUTF 反馈客户端
				if (if_User_Enter(username_temp)) 
					return null;
				
		return UserData;
	}
	
	public void finish_Login(String operator) throws IOException {
		logger.debug("调用了 finish_Login() ");
		try {
			if ("if_Username_Exist".equals(operator)) {
				String username = in.readUTF();
				boolean flag = Login_Enter_Object.if_Username_Exist(username);
				logger.debug("SQL_Login_Enter.if_Username_Exist(username) 结果为 "+flag);
				if (flag) {
					out.writeUTF("if_Username_Exist,,,"+"用户名已被注册 请换一个名字");
				}else
					out.writeUTF("if_Username_Exist,,,"+"un_Exist");
					
				
			}else if ("Login".equals(operator)) {
				String username = in.readUTF();
				String pwd = in.readUTF();
				logger.debug("准备注册  账号 :"+username+  "  密码  :"+pwd);
				Login_Enter_Object.login(username, pwd);
				out.writeUTF("flag_Login,,,"+"OK_Login");
				
			}
	
		} catch (Exception e) {
			logger.info("服务器  账号注册失败");
			out.writeUTF("flag_Login,,,"+"服务器  账号注册失败");
		}
		
		
		
		
	}
	
	public void finish_set_pwd(String operator) throws IOException {
		logger.debug("调用了 finish_set_pwd() ");
		try {
			//验证  旧密码对不对
			if ("if_User_Pwd".equals(operator)) {
				String old_pwd = in.readUTF();
				logger.info("旧密码"+old_pwd);
				UserData temp = if_user_id_pwd(this.userData.username,old_pwd);	//保险一点用数据库的密码，不用userData.pwd
				if (temp !=null ) {
					out.writeUTF("if_User_Pwd,,,"+"OK_pwd");
					logger.info("旧密码  ok");
				}else {
					out.writeUTF("if_User_Pwd,,,"+"原密码不正确");
					logger.info("if_User_Pwd,,,"+"旧密码  错误");
				}
				return;
			}else if ("set_Pwd".equals(operator)) {    //修改密码
				String new_pwd = in.readUTF();
				Login_Enter_Object.set_Pwd(this.userData.username, new_pwd);
				//先改服务器的  避免sql错误 影响
				this.userData.pwd = new_pwd;
				out.writeUTF("set_Pwd,,,"+"OK_set_pwd");
				logger.info("修改密码   ok");
			}
	
		} catch (Exception e) {
			logger.info("服务器  修改密码失败");
			logger.info("set_Pwd,,,"+"OK_set_pwd");
		}
		
		
		
		
		
	}
	
	public void broadcast(String formuser ,String data) throws IOException {
		String user_name_list = "广播给 : ";
		for(Core it :user_list) {
			it.out.writeUTF("qunliao");
			it.out.writeUTF("qunliao,,,"+formuser);
			it.out.writeUTF("qunliao,,,"+data+" ");	//结尾加空格 ，防止data为空 ，无法被客户端 manage 分割
			user_name_list += it.userData.username;
		}
		logger.debug(user_name_list);
		save_liaotian(formuser,data);
	}

	public void broadcast_connection_flag() throws IOException {
		//获得  在线用户名单
		String username_list="";
		String sun_username_list = "";
		for(Core it :user_list) {
			username_list += it.userData.username+",&,";	//  ,,, 分隔符
		}
		logger.debug("在线用户 ："+username_list);
		//获得  在线用户名单
		ArrayList<String> sun_username_list_temp = Login_Enter_Object.get_Sum_username();
		for(String it :sun_username_list_temp) {
			sun_username_list += it+",&,";
		}
		logger.debug("全部用户 ："+sun_username_list);
		//广播
		for(Core it :user_list) {
			it.out.writeUTF("user_list");
//			it.out.writeUTF("在线人数："+user_list.size());    //由客户端统计就好
			it.out.writeUTF("user_list,,,"+username_list);
			it.out.writeUTF("user_list,,,"+sun_username_list);
		}
		
	}
	
	public void save_liaotian(String formuser ,String data) throws IOException {
		logger.debug("save_liaotian函数调用");
		
		if(myMain_Server.if_usr_sql) {
			//历史记录保存数据库
			try {//保存记录到数据库
				SQL_Record.insert(formuser, data);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				logger.info("数据插入数据库是失败-----------------------");
			}
		}
	}
}

