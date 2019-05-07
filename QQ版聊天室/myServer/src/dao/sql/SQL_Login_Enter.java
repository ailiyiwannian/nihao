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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			logger.info("static Load() 错误----------------");
		}
		
	}
	
	//判断 该用户名是否被注册了
	@Override
	public boolean if_Username_Exist(String username) throws SQLException {
		ResultSet resultSet = sqlUtil.mapSqlExecuteQuery("select_user_name", username);
		if (resultSet.next()) {
			return true;
		}
		return false;
	}
	
	//注册账号
	@Override
	public void login(String username,String pwd) throws SQLException {
		sqlUtil.mapSqlExecuteUpdate("insert_user", username,pwd);
	}
	//验证  用户名 和 密码
	@Override
	public boolean if_Username_pwd(String username, String pwd) throws SQLException {
		ResultSet resultSet = sqlUtil.mapSqlExecuteQuery("select_user_data", username,pwd);
		if (resultSet.next()) {
			return true;
		}
		return false;
	}

	//得到一份  全部用户 用户名列表
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
			logger.info("sql 错误");
		}		
		return null;
	}
	
	//修改密码
	@Override
	public void set_Pwd(String username, String pwd)throws SQLException {
		try { //update userData set pwd=? where username=?
			sqlUtil.mapSqlExecuteUpdate("update_user_pwd", pwd ,username);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			logger.debug("sql 错误");
		}
	}


	
	
	static private void Load() throws ClassNotFoundException, IOException, SQLException {
		
		sqlUtil = new SQL_Util();
		sqlUtil.addPreparedStatement("insert_user", "insert userData values(?,?)");
		sqlUtil.addPreparedStatement("update_user_pwd", "update userData set pwd=? where username=?");
		sqlUtil.addPreparedStatement("select_user_name", "select username,pwd from userData where username=?");
		sqlUtil.addPreparedStatement("select_user_data", "select username,pwd from userData where username=? and pwd=?");
		sqlUtil.addPreparedStatement("select_sum_user_data", "select username,pwd from userData");
		logger.info("SQL工具类加载成功-------------------");
		try {
			sqlUtil.sqlExecuteUpdate("create table userData(\r\n" + 
					"	username char(30),\r\n" + 
					"	pwd char(30)\r\n" + 
					")character set utf8 collate utf8_general_ci;");
			
			logger.info("表创建成功");
		} catch (Exception e) {
			logger.info("表已存在");
		}
		logger.info("表可以放心使用了");
	
	}


	
	
}





