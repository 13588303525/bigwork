package cn.sign.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.sign.model.Course;
import cn.sign.model.Teacher;
import cn.sign.service.CourseService;
import cn.sign.service.TeacherService;

@Controller("loginAction")
public class LoginAction {
	@Resource
	private TeacherService teacherService;
	//@Resource
	//private CourseService courseService;
	
	//private List<Course> courseList;
	//public List<Course> getCourseList() {return courseList;}

	private Teacher teacher;//struts传参
	public Teacher getTeacher() {return teacher;}
	public void setTeacher(Teacher teacher) {this.teacher = teacher;}

	public String execute() {
		boolean result = teacherService.checkUserById(teacher);// 检查用户是否存在
		if (result) {
			//courseList = courseService.getCourseByT_id(teacher.getT_id());
			
			return "manageView";
		} else {
			System.out.println("密码或是账号错误");
			return null;
		}
	}
}
