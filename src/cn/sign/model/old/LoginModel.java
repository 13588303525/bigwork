package cn.sign.model.old;

import cn.sign.service.old.UserDBService;


/**
 * ��¼��飺
 * ͨ������UserDBService��ķ�����֤�û�����ݣ�
 *   1��checkUserByPwd������¼���롣 
 *   2��checkUserByFace�����������
 * @author acer
 *
 */
public class LoginModel {

	private UserDBService userDBService;

	public LoginModel() {
		userDBService = new UserDBService();
	}

	public boolean checkUserByPwd(UserBean user) {
		//return true;
		return userDBService.checkUserByPwd(user);
	}

	public boolean checkUserByFace() {
		return false;
	}

}
