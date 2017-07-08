package cn.sign.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.sign.util.HibernateUtil;

@Repository("signDao")
public class SignDao {

	public List<Object[]> getSignListByCourseId(int c_id) {
		//��ǩ�����ѧ�����в�����γ̵�ѧ��id��name��score(�ַ������Զ��Ÿ���ÿ�εĵ÷�)
		String hql = "select sg.s_id, stu.s_name, sg.score from Sign sg, Student stu where sg.s_id = stu.s_id and c_id = ?";	
		Query qry = HibernateUtil.getSession().createQuery(hql);
		qry.setParameter(0, c_id);
		List<Object[]> signList = qry.list();
		
		return signList;
	}
}
