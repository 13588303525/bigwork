/**
 * Project             :FaceRecognise project
 * Comments            :用户信息数据库操作类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.service.old;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sign.dao.old.UserDao;
import cn.sign.model.old.UserBean;

/**
 * 实现用户的相关数据操作：增、删、改、查……
 * @author acer
 *
 */
@Service("userService")
public class UserDBService implements IUserDBService{
	@Resource
	private UserDao userDao;
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public boolean insertUser(UserBean user) {
		String sql = "INSERT INTO USER_TABLE(ID, PWD, NAME, HAVEPHOTO, PICSPATH) VALUES (?,?,?,?,?)";
		String[] parameters = { user.getId(), user.getPwd(),
				user.getName(), user.getHavePhoto(), user.getPicsPath() };
		boolean flag = userDao.executeUpdate(sql, parameters);
		return flag;
	}

	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	public boolean deleteUser(String id) {
		String sql = "DELETE FROM USER_TABLE WHERE ID = ?";
		String[] parameters = { String.valueOf(id) };
		boolean flag = userDao.executeUpdate(sql, parameters);
		return flag;
	}

	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public boolean updateUser(UserBean user) {
		String sql = "UPDATE USER_TABLE SET PWD = ?,NAME = ?, HAVEPHOTO = ?,PICSPATH = ? WHERE ID = ?";
		String[] parameters = {user.getPwd(), user.getName(), user.getHavePhoto(), user.getPicsPath(), String.valueOf(user.getId()) };
		boolean flag = userDao.executeUpdate(sql, parameters);
		return flag;
	}

	/**
	 * 通过用户Id获得用户信息
	 * @param id
	 * @return
	 */
	public UserBean getUserById(String id) {
		String sql = "FROM UserBean WHERE id = " + id;
		List<UserBean> al = userDao.queryExecute(sql);
		if(al.size()>0){
			UserBean user = new UserBean();
			for(UserBean u : al){
				user = u;
			} 
			return user;
		}
		else {
			return null;
		}
	}

	/**
	 * 获得Id序号在startSN ~ endSN范围内的用户
	 * @param startSN
	 * @param endSN
	 * @return
	 */
	public ArrayList<UserBean> getUsersBySN(int startSN, int endSN) {
		String sql = "SELECT * FROM UserBean WHERE id >= "+String.valueOf(startSN)+" and id <= "+String.valueOf(endSN);
		List<UserBean> al = userDao.queryExecute(sql);

		ArrayList<UserBean> users = new ArrayList<UserBean>();
		for (UserBean u : al) {
			UserBean user = u;
			users.add(user);
		}
		return users;
	}

	/**
	 * 获得所有用户
	 * @return
	 */
	public ArrayList<UserBean> getAllUsers() {
		String sql = "FROM UserBean";
		List<UserBean> al = userDao.queryExecute(sql);

		ArrayList<UserBean> users = new ArrayList<UserBean>();
		for (UserBean u : al) {
			UserBean user = u;
			users.add(user);
		}
		return users;
	}

	/**
	 * 获得所有保存有照片的用户
	 * @return
	 */
	public ArrayList<UserBean> getAllHavePhotoUsers() {
		String sql = "FROM UserBean WHERE havePhoto = '是'";
		List<UserBean> al = userDao.queryExecute(sql);

		ArrayList<UserBean> users = new ArrayList<UserBean>();
		for (UserBean u : al) {
			UserBean user = u;
			users.add(user);
		}
		return users;
	}

	/**
	 * 获得当前数据库中用户的数量
	 * @return
	 */
	public int getCount() {
		String sql = "FROM UserBean";
		List<UserBean> al = userDao.queryExecute(sql);
		int count = al.size();
		
		return count;
	}

	/**
	 * 通过输入的账号密码检查用户是否存在
	 * @param user
	 * @return
	 */
	public boolean checkUserByPwd(UserBean user) {
		
		boolean flag = false;
		String sql = "FROM UserBean WHERE id = "+user.getId()+" AND pwd = "+user.getPwd() ;
		List<UserBean> al = userDao.queryExecute(sql);
		if(al.size()>0){
			//user = al.get(0);	//这种写法user会指向al.get(0)获得的UserBean对象，所以指向原user对象的值并没有变
			user.setName(al.get(0).getName());
			user.setHavePhoto(al.get(0).getHavePhoto());
			user.setPicsPath(al.get(0).getPicsPath());
			flag = true;
		}
		
		return flag;
	}

	/**
	 * 模糊查找：获得账号中包含相关字符的所有用户
	 * @param account
	 * @return
	 */
	
	public List<UserBean> getUsersByAccount(String ID) {
		String[] parameters = new String[1];
		if ("%".equals(ID)) {
			parameters[0] = "%[%]%";
		} else {
			parameters[0] = "%" + ID + "%";
		}
		String sql = "FROM UserBean WHERE id LIKE "+parameters[0]+" ORDER BY id";

		List<UserBean> al = userDao.queryExecute(sql);
		return al;
	}

	/**
	 * 精确查找：根据账号获得指定唯一的用户
	 * @param account
	 * @return
	 */
	public UserBean getUniqueUserByAccount(String account) {
		String sql = "SELECT * FROM USER_TABLE WHERE ACCOUNT = "+account;
		List<UserBean> al = userDao.queryExecute(sql);
		if (al.size() > 0) {
			UserBean user = al.get(0);
			return user;
		}else{
			return null;
		}
	}

	/**
	 * 删除所有用户
	 * @return
	 */
	public boolean deleteAllUsers() {
		String sql = "DELETE FROM USER_TABLE WHERE ID > 0";
		return userDao.executeUpdate(sql, null);
	}

}
