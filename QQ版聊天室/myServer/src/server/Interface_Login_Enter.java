package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Interface_Login_Enter {
	//�ж� ���û����Ƿ�ע����
	public boolean if_Username_Exist(String username) throws SQLException, IOException;
	//ע���˺�
	void login(String username,String pwd) throws SQLException, IOException;
	//�û���¼  ��֤ �û��� ����
	public boolean if_Username_pwd(String username,String pwd) throws SQLException, IOException;
	//�õ�һ��  ȫ���û� �û����б�
	public ArrayList<String> get_Sum_username() throws IOException ;
	//�޸�����
	public void set_Pwd(String username,String pwd) throws IOException, SQLException;
}
