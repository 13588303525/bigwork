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
	 * ͨ��������˺��������ʦ�Ƿ����
	 * return boolean������teacher���t_name
	 */
	public boolean checkUserById(Teacher teacher) {
		Teacher t = teacherDao.getTeacherById(teacher.getT_id());
		
		if(t!=null){//�û�����
			if(teacher.getPwd().equals(t.getPwd())){//������ȷ
				teacher.setT_name(t.getT_name());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��ѯ����teacher
	 */
	public List<Teacher> getAllTeacher(){
		return teacherDao.getAllTeacher();
	}
}
