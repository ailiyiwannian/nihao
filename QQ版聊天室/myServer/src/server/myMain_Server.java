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
			//�Ƿ�ʹ�����ݿⱣ������(�û�ע����Ϣ , [������Ϣ��¼]) ,������ �ļ������û�ע����Ϣ  ,�����¼����־�ļ��Դ���  ���ظ�
			// ��Ҫʹ����Ҫ�μ� ��Ϊ"cxp"�����ݿ�  ,��ͨ��dao.sql �µ�jdbc.properties  ��������
			
			if_usr_sql = false;
			
			logger.info("����������");
			if (if_usr_sql) {
				Class.forName("dao.sql.SQL_Record");
				Class.forName("dao.sql.SQL_Login_Enter");
			}else {		//userdata.properties ��������ھʹ���
				File file = new File("userdata.properties");
				if (!file.exists()) {
					file.createNewFile();
				}
				logger.info("userdata.properties �Ѵ��� ����ʹ��");
			}
			
			serverSocket = new ServerSocket(3389);
			while(true) {
				Socket socket = serverSocket.accept();
				Thread thread = new Thread(new Core(socket));
				thread.start();
				logger.debug("һ���µ�scoket---------------------------");
			}
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			try {
				serverSocket.close();
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}				
	
	}
}
