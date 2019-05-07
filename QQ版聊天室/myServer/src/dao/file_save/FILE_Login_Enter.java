package dao.file_save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import server.Core;
import server.Interface_Login_Enter;

public class FILE_Login_Enter implements Interface_Login_Enter{
	File file = new File("userdata.properties");
	static Logger logger = Logger.getLogger(Core.class);
	
	//读入
	private Properties getProperties() throws IOException {
		FileReader in = new FileReader(file); 
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		return properties;
	}
	
	//写出
		private void outProperties(Properties properties) throws IOException {
			FileWriter out = new FileWriter(file);
			properties.store(out, null);
			out.close();
		}
	
	
	
	//判断 该用户名是否被注册了
	@Override
	public boolean if_Username_Exist(String username) throws SQLException, IOException {
		Properties properties = getProperties();
		Set<String> set = properties.stringPropertyNames();
		for(String it : set) {
			if (username.equals(it)) {
				return true;
			}
		}
		return false;
	}

	
	//注册账号
	@Override
	public void login(String username, String pwd) throws SQLException, IOException {
		//读入
		Properties properties = getProperties();
		//修改
		properties.setProperty(username, pwd);
		//写出
		outProperties(properties);
	}

	//用户登录  验证 用户名 密码
	@Override
	public boolean if_Username_pwd(String username, String pwd) throws SQLException, IOException {
		//读入
		Properties properties = getProperties();
		if (pwd.equals( properties.getProperty(username) )) {
			return true;
		}else {
			return false;
		}
	}

	//得到一份  全部用户 用户名列表
	@Override
	public ArrayList<String> get_Sum_username() throws IOException {
		//读入
		Properties properties = getProperties();
		Set<String> set = properties.stringPropertyNames();
		ArrayList<String> username_list = new ArrayList<String>();
		for(String it : set) {
			username_list.add(it);
		}
		return username_list;
	}

	//修改密码
	@Override
	public void set_Pwd(String username, String pwd) throws SQLException, IOException {
		//读入
		Properties properties = getProperties();
		//修改
		properties.setProperty(username, pwd);
		//写出
		outProperties(properties);
	}

}
