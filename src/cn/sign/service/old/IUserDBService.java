package cn.sign.service.old;

import java.util.ArrayList;
import java.util.List;

import cn.sign.model.old.UserBean;

public interface IUserDBService {
	public boolean insertUser(UserBean user);
	public boolean deleteUser(String id);
	public boolean updateUser(UserBean user);
	public UserBean getUserById(String id);
	public ArrayList<UserBean> getUsersBySN(int startSN, int endSN);
	public ArrayList<UserBean> getAllUsers();
	public ArrayList<UserBean> getAllHavePhotoUsers();
	public int getCount();
	public boolean checkUserByPwd(UserBean user);
	public List<UserBean> getUsersByAccount(String ID);
	public UserBean getUniqueUserByAccount(String account);
	public boolean deleteAllUsers();
}
