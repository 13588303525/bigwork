package cn.sign.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sign.dao.ScDao;
import cn.sign.model.Student;

@Service("scService")
public class ScService {
	@Resource
	private ScDao scDao;

	/**
	 * 通过输入的账号密码检查教师是否存在 return boolean，参数teacher获得t_name
	 */
	public List<Student> getStudentsByCourseId(int courseId) {
		List<Student> stuList = scDao.getStudentsByCourseId(courseId);
		if(stuList.size()>0)
			return stuList;
		return null;
	}

}
