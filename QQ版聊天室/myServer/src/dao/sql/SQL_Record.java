package dao.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;



public class SQL_Record {
	static Logger logger = Logger.getLogger(SQL_Record.class);
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
	
	
	static public int insert(String name,String boradcast) throws ClassNotFoundException, IOException, SQLException  {
		int len ;
		try {
			len = sqlUtil.mapSqlExecuteUpdate("insert", new Timestamp(new Date().getTime()), name, boradcast);
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			logger.info("SQL_Record insert ����---------------");
			//���� ���ݿ����ӵ����Ը���/���˲������ݿ�ѱ�ɾ����
			Load();
			len =insert( name, boradcast);
		}
		return len;
	}
	

	static private void Load() throws ClassNotFoundException, IOException, SQLException {
		
			sqlUtil = new SQL_Util();
			sqlUtil.addPreparedStatement("insert", "insert liaotian values(?,?,?)");
			sqlUtil.addPreparedStatement("select", "select name,broadcast from liaotian");
			logger.info("SQL��������سɹ�-------------------");
			try {
				sqlUtil.sqlExecuteUpdate("create table liaotian(\r\n" + 
						"	time datetime,\r\n" + 
						"	name char(20),\r\n" + 
						"	broadcast varchar(100)\r\n" + 
						")character set utf8 collate utf8_general_ci;");
				
				logger.info("�����ɹ�");
			} catch (Exception e) {
				logger.info("���Ѵ���");
			}
			logger.info("����Է���ʹ����");
		
	}
	
}
