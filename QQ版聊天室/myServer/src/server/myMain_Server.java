package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;




public class myMain_Server {
	
	static Logger logger = Logger.getLogger(myMain_Server.class);
	
	static boolean if_usr_sql;
	
	public static void main(String[] args) throws  ClassNotFoundException {
		ServerSocket serverSocket = null;
		try {
			//是否使用数据库保存数据(用户注册信息 , [聊天消息记录]) ,否则用 文件保存用户注册信息  ,聊天记录有日志文件自带了  不重复
			// 若要使用需要参加 名为"cxp"的数据库  ,可通过dao.sql 下的jdbc.properties  进行配置
			
			if_usr_sql = false;
			
			logger.info("服务器启动");
			if (if_usr_sql) {
				Class.forName("dao.sql.SQL_Record");
				Class.forName("dao.sql.SQL_Login_Enter");
			}else {		//userdata.properties 如果不存在就创建
				File file = new File("userdata.properties");
				if (!file.exists()) {
					file.createNewFile();
				}
				logger.info("userdata.properties 已存在 放心使用");
			}
			
			serverSocket = new ServerSocket(3389);
			while(true) {
				Socket socket = serverSocket.accept();
				Thread thread = new Thread(new Core(socket));
				thread.start();
				logger.debug("一个新的scoket---------------------------");
			}
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			try {
				serverSocket.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}				
	
	}
}
