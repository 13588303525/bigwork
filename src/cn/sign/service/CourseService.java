package cn.sign.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sign.dao.CourseDao;
import cn.sign.model.Course;

@Service
public class CourseService {
	@Resource
	CourseDao courseDao;
	
	/**
	 * ����t_Id��øý�ʦ�����пγ�
	 */
	public List<Course> getCourseByT_id(int t_id){
		return courseDao.getCourseByT_id(t_id);
	}
	
}
