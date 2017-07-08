package cn.sign.model.old;

import cn.sign.service.old.UserDBService;


/**
 * 登录检查：
 * 通过调用UserDBService类的方法验证用户的身份：
 *   1、checkUserByPwd：检查登录密码。 
 *   2、checkUserByFace：检查人脸。
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
