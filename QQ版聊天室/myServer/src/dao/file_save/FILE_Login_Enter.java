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
	
	//����
	private Properties getProperties() throws IOException {
		FileReader in = new FileReader(file); 
		Properties properties = new Properties();
		properties.load(in);
		in.close();
		return properties;
	}
	
	//д��
		private void outProperties(Properties properties) throws IOException {
			FileWriter out = new FileWriter(file);
			properties.store(out, null);
			out.close();
		}
	
	
	
	//�ж� ���û����Ƿ�ע����
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

	
	//ע���˺�
	@Override
	public void login(String username, String pwd) throws SQLException, IOException {
		//����
		Properties properties = getProperties();
		//�޸�
		properties.setProperty(username, pwd);
		//д��
		outProperties(properties);
	}

	//�û���¼  ��֤ �û��� ����
	@Override
	public boolean if_Username_pwd(String username, String pwd) throws SQLException, IOException {
		//����
		Properties properties = getProperties();
		if (pwd.equals( properties.getProperty(username) )) {
			return true;
		}else {
			return false;
		}
	}

	//�õ�һ��  ȫ���û� �û����б�
	@Override
	public ArrayList<String> get_Sum_username() throws IOException {
		//����
		Properties properties = getProperties();
		Set<String> set = properties.stringPropertyNames();
		ArrayList<String> username_list = new ArrayList<String>();
		for(String it : set) {
			username_list.add(it);
		}
		return username_list;
	}

	//�޸�����
	@Override
	public void set_Pwd(String username, String pwd) throws SQLException, IOException {
		//����
		Properties properties = getProperties();
		//�޸�
		properties.setProperty(username, pwd);
		//д��
		outProperties(properties);
	}

}
