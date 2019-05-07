package dao.sql;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;



public class SQL_Util {
	 static Logger logger = Logger.getLogger(SQL_Util.class);
	 private String url;
	 private String user;
	 private String password;
	 private String driver;
	 private Connection connection;
	 private Statement statement;
	 private Map<String, PreparedStatement> map = new HashMap<String,PreparedStatement>();
	 
	public SQL_Util() throws IOException, ClassNotFoundException, SQLException {
		
		//��ȡ�����ļ���Ϣ
		InputStream fr = SQL_Util.class.getResourceAsStream("jdbc.properties");
		Properties p = new Properties();
		p.load(fr);
		url = p.getProperty("url");
		user = p.getProperty("user");
		password = p.getProperty("password");
		driver = p.getProperty("driver");
		fr.close();
		
		//����������
		Class.forName(driver);
		//�������Ӷ���
		connection = DriverManager.getConnection(url, user, password);
		//����sql statement���� ֱ�ӵ���
		statement = connection.createStatement();
		logger.info("sql���ӳɹ�--------");
		
	}
	
	//��map���preparedStatement����
	public void addPreparedStatement(String key , String sql) throws SQLException {
		map.put(key, connection.prepareStatement(sql));
	}
	
	//�� map���PreparedStatement ��sql�ģ�����
	public int mapSqlExecuteUpdate(String key ,Object ...objects) throws SQLException {
		PreparedStatement preparedStatement = map.get(key);
		for(int i=0 ;i<objects.length;i++) {
			preparedStatement.setObject(i+1, objects[i]);
		}
		return preparedStatement.executeUpdate(); 
	}
	public ResultSet mapSqlExecuteQuery(String key , Object ...objects) throws SQLException {
		PreparedStatement preparedStatement = map.get(key);
		for (int i = 0; i < objects.length; i++) {
			preparedStatement.setObject(i+1, objects[i]);
		}
		return preparedStatement.executeQuery();
	}
	public int mapSqlExecuteUpdate(String key ) throws SQLException {
		PreparedStatement preparedStatement = map.get(key);	
		return preparedStatement.executeUpdate(); 
	}
	public ResultSet mapSqlExecuteQuery(String key) throws SQLException {
		PreparedStatement preparedStatement = map.get(key);
		return preparedStatement.executeQuery();
	}
	//ֱ�ӵ��� statement
	public int sqlExecuteUpdate(String sql) throws SQLException {
		return statement.executeUpdate(sql);
	}
	public ResultSet sqlExecuteQuery(String sql) throws SQLException {		
		return statement.executeQuery(sql);
	}
	

	
	
}












