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
	// �������
	private static Connection ct = null;
	private static PreparedStatement ps = null;

	/**
	 * Description :ִ������ɾ���Ĳ���
	 * 
	 * @return boolean:ִ�н��
	 */
	@Override
	public boolean executeUpdate(String sql, String[] parameters) {
		boolean flag = false;
		try {
			Session session = sessionFactory.openSession();

			Transaction tx = session.beginTransaction();

			// ҵ�����

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
			e.printStackTrace();// �����׶�
		}
		return flag;
	}

	/**
	 * Description :ִ�в�ѯ����
	 * 
	 * @return ArrayList<Object[]>:���ز�ѯ���
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
