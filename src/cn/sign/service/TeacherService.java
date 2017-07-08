package cn.sign.service;


import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import cn.sign.dao.TeacherDao;
import cn.sign.model.Teacher;

@Service("teacherService")
public class TeacherService {
	@Resource
	private TeacherDao teacherDao;
	
	/**
	 * 通过输入的账号密码检查教师是否存在
	 * return boolean，参数teacher获得t_name
	 */
	public boolean checkUserById(Teacher teacher) {
		Teacher t = teacherDao.getTeacherById(teacher.getT_id());
		
		if(t!=null){//用户存在
			if(teacher.getPwd().equals(t.getPwd())){//密码正确
				teacher.setT_name(t.getT_name());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查询所有teacher
	 */
	public List<Teacher> getAllTeacher(){
		return teacherDao.getAllTeacher();
	}
}
