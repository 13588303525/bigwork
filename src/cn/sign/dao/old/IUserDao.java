package cn.sign.dao.old;

import java.util.List;

import cn.sign.model.old.UserBean;

public interface IUserDao {
	public boolean executeUpdate(String sql, String[] parameters);
	public List<UserBean> queryExecute(String sql);
}
