/**
 * Project             :FaceRecognise project
 * Comments            :�û���Ϣ���ݿ������
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ���� | jxm 
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
 * ʵ���û���������ݲ���������ɾ���ġ��顭��
 * @author acer
 *
 */
@Service("userService")
public class UserDBService implements IUserDBService{
	@Resource
	private UserDao userDao;
	
	/**
	 * ����û�
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
	 * ɾ���û�
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
	 * �����û�
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
	 * ͨ���û�Id����û���Ϣ
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
	 * ���Id�����startSN ~ endSN��Χ�ڵ��û�
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
	 * ��������û�
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
	 * ������б�������Ƭ���û�
	 * @return
	 */
	public ArrayList<UserBean> getAllHavePhotoUsers() {
		String sql = "FROM UserBean WHERE havePhoto = '��'";
		List<UserBean> al = userDao.queryExecute(sql);

		ArrayList<UserBean> users = new ArrayList<UserBean>();
		for (UserBean u : al) {
			UserBean user = u;
			users.add(user);
		}
		return users;
	}

	/**
	 * ��õ�ǰ���ݿ����û�������
	 * @return
	 */
	public int getCount() {
		String sql = "FROM UserBean";
		List<UserBean> al = userDao.queryExecute(sql);
		int count = al.size();
		
		return count;
	}

	/**
	 * ͨ��������˺��������û��Ƿ����
	 * @param user
	 * @return
	 */
	public boolean checkUserByPwd(UserBean user) {
		
		boolean flag = false;
		String sql = "FROM UserBean WHERE id = "+user.getId()+" AND pwd = "+user.getPwd() ;
		List<UserBean> al = userDao.queryExecute(sql);
		if(al.size()>0){
			//user = al.get(0);	//����д��user��ָ��al.get(0)��õ�UserBean��������ָ��ԭuser�����ֵ��û�б�
			user.setName(al.get(0).getName());
			user.setHavePhoto(al.get(0).getHavePhoto());
			user.setPicsPath(al.get(0).getPicsPath());
			flag = true;
		}
		
		return flag;
	}

	/**
	 * ģ�����ң�����˺��а�������ַ��������û�
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
	 * ��ȷ���ң������˺Ż��ָ��Ψһ���û�
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
	 * ɾ�������û�
	 * @return
	 */
	public boolean deleteAllUsers() {
		String sql = "DELETE FROM USER_TABLE WHERE ID > 0";
		return userDao.executeUpdate(sql, null);
	}

}
