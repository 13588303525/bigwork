package cn.sign.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.sign.model.Course;
import cn.sign.util.HibernateUtil;

@Repository("courseDao")
public class CourseDao {

	public List<Course> getCourseByT_id(int t_id){
		String hql = "from Course where t_id = ?";
		Query qry = HibernateUtil.getSession().createQuery(hql);
		qry.setParameter(0, t_id);
		return qry.list();
	}
}
