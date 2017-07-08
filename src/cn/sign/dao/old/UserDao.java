package cn.sign.dao.old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import cn.sign.model.old.UserBean;

@Repository("userDao")
public class UserDao implements IUserDao {
	@Resource
	SessionFactory sessionFactory;
	// 定义变量
	private static Connection ct = null;
	private static PreparedStatement ps = null;

	/**
	 * Description :执行增，删，改操作
	 * 
	 * @return boolean:执行结果
	 */
	@Override
	public boolean executeUpdate(String sql, String[] parameters) {
		boolean flag = false;
		try {
			Session session = sessionFactory.openSession();

			Transaction tx = session.beginTransaction();

			// 业务代码

			tx.commit();

			tx.rollback();

			ps = ct.prepareStatement(sql);

			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}

			}
			if (ps.executeUpdate() >= 1) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();// 开发阶段
		}
		return flag;
	}

	/**
	 * Description :执行查询操作
	 * 
	 * @return ArrayList<Object[]>:返回查询结果
	 */
	@Override
	public List<UserBean> queryExecute(String sql) {
		List<UserBean> result = null;
		try {
			Session session = sessionFactory.openSession();
			Query qry = session.createQuery(sql);
			result = qry.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
