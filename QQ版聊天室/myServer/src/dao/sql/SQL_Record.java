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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			logger.info("static Load() 错误----------------");
		}
		
	}
	
	
	static public int insert(String name,String boradcast) throws ClassNotFoundException, IOException, SQLException  {
		int len ;
		try {
			len = sqlUtil.mapSqlExecuteUpdate("insert", new Timestamp(new Date().getTime()), name, boradcast);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			logger.info("SQL_Record insert 错误---------------");
			//可能 数据库连接的属性改了/有人操作数据库把表删除了
			Load();
			len =insert( name, boradcast);
		}
		return len;
	}
	

	static private void Load() throws ClassNotFoundException, IOException, SQLException {
		
			sqlUtil = new SQL_Util();
			sqlUtil.addPreparedStatement("insert", "insert liaotian values(?,?,?)");
			sqlUtil.addPreparedStatement("select", "select name,broadcast from liaotian");
			logger.info("SQL工具类加载成功-------------------");
			try {
				sqlUtil.sqlExecuteUpdate("create table liaotian(\r\n" + 
						"	time datetime,\r\n" + 
						"	name char(20),\r\n" + 
						"	broadcast varchar(100)\r\n" + 
						")character set utf8 collate utf8_general_ci;");
				
				logger.info("表创建成功");
			} catch (Exception e) {
				logger.info("表已存在");
			}
			logger.info("表可以放心使用了");
		
	}
	
}
