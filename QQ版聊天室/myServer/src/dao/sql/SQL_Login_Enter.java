package dao.sql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import server.Interface_Login_Enter;

public class SQL_Login_Enter implements Interface_Login_Enter{
	static Logger logger = Logger.getLogger(SQL_Login_Enter.class);
	static private SQL_Util sqlUtil;
	static {
		try {
			Load();
		} catch (ClassNotFoundException | IOException | SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			logger.info("static Load() ����----------------");
		}
		
	}
	
	//�ж� ���û����Ƿ�ע����
	@Override
	public boolean if_Username_Exist(String username) throws SQLException {
		ResultSet resultSet = sqlUtil.mapSqlExecuteQuery("select_user_name", username);
		if (resultSet.next()) {
			return true;
		}
		return false;
	}
	
	//ע���˺�
	@Override
	public void login(String username,String pwd) throws SQLException {
		sqlUtil.mapSqlExecuteUpdate("insert_user", username,pwd);
	}
	//��֤  �û��� �� ����
	@Override
	public boolean if_Username_pwd(String username, String pwd) throws SQLException {
		ResultSet resultSet = sqlUtil.mapSqlExecuteQuery("select_user_data", username,pwd);
		if (resultSet.next()) {
			return true;
		}
		return false;
	}

	//�õ�һ��  ȫ���û� �û����б�
	@Override
	public ArrayList<String> get_Sum_username() {
		ArrayList<String> sun_username_list = new ArrayList<String>();
		try {
			ResultSet resultSet = sqlUtil.mapSqlExecuteQuery("select_sum_user_data");
				while(resultSet.next()) {
					sun_username_list.add( resultSet.getString("username") );
				}
				return sun_username_list;
		} catch (SQLException e) {
			logger.info("sql ����");
		}		
		return null;
	}
	
	//�޸�����
	@Override
	public void set_Pwd(String username, String pwd)throws SQLException {
		try { //update userData set pwd=? where username=?
			sqlUtil.mapSqlExecuteUpdate("update_user_pwd", pwd ,username);
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			logger.debug("sql ����");
		}
	}


	
	
	static private void Load() throws ClassNotFoundException, IOException, SQLException {
		
		sqlUtil = new SQL_Util();
		sqlUtil.addPreparedStatement("insert_user", "insert userData values(?,?)");
		sqlUtil.addPreparedStatement("update_user_pwd", "update userData set pwd=? where username=?");
		sqlUtil.addPreparedStatement("select_user_name", "select username,pwd from userData where username=?");
		sqlUtil.addPreparedStatement("select_user_data", "select username,pwd from userData where username=? and pwd=?");
		sqlUtil.addPreparedStatement("select_sum_user_data", "select username,pwd from userData");
		logger.info("SQL��������سɹ�-------------------");
		try {
			sqlUtil.sqlExecuteUpdate("create table userData(\r\n" + 
					"	username char(30),\r\n" + 
					"	pwd char(30)\r\n" + 
					")character set utf8 collate utf8_general_ci;");
			
			logger.info("�����ɹ�");
		} catch (Exception e) {
			logger.info("���Ѵ���");
		}
		logger.info("����Է���ʹ����");
	
	}


	
	
}





