package cn.sign.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.sign.model.Teacher;
import cn.sign.util.HibernateUtil;

@Repository("teacherDao")
public class TeacherDao {
	
	/**
	 * ��id��ѯteacher
	 */
	public Teacher getTeacherById(int id){
		String hql = "from Teacher where t_id = ?";
		Query qry = HibernateUtil.getSession().createQuery(hql);
		qry.setParameter(0, id);
		return (Teacher)qry.uniqueResult();
	}
	
	/**
	 * ��ѯ����teacher
	 */
	public List<Teacher> getAllTeacher(){
		String hql = "from Teacher order by t_id";
		Query qry = HibernateUtil.getSession().createQuery(hql);
		return qry.list();
	}
	
	
	
}
