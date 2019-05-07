package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Interface_Login_Enter {
	//判断 该用户名是否被注册了
	public boolean if_Username_Exist(String username) throws SQLException, IOException;
	//注册账号
	void login(String username,String pwd) throws SQLException, IOException;
	//用户登录  验证 用户名 密码
	public boolean if_Username_pwd(String username,String pwd) throws SQLException, IOException;
	//得到一份  全部用户 用户名列表
	public ArrayList<String> get_Sum_username() throws IOException ;
	//修改密码
	public void set_Pwd(String username,String pwd) throws IOException, SQLException;
}
