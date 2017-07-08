package cn.sign.dao;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.sign.model.Student;
import cn.sign.util.HibernateUtil;
import net.sf.json.JSONArray;

@Repository("scDao")
public class ScDao {

	public List<Student> getStudentsByCourseId(int c_id) {
		String hql = "select s_id from Sc where c_id = ?";
		Query qry = HibernateUtil.getSession().createQuery(hql);
		qry.setParameter(0, c_id);
		List<Integer> idList = qry.list();

		String hql_id = "from Student where s_id = ?";
		List<Student> stuList = new ArrayList<Student>();
		for (int id : idList) {
			Query q = HibernateUtil.getSession().createQuery(hql_id);
			q.setParameter(0, id);
			Student s = (Student) q.uniqueResult();
			stuList.add(s);
		}
		JSONArray jStu = JSONArray.fromObject(stuList);
		System.out.println(jStu.toString());
		//HibernateUtil.closeSession(); //不close也没有影响
		return stuList;
	}
}
