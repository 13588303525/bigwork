package cn.sign.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import cn.sign.model.Course;
import cn.sign.model.Student;
import cn.sign.model.Teacher;
import cn.sign.service.CourseService;
import cn.sign.service.ScService;
import cn.sign.service.SignService;

@Controller("manageAction")
public class ManageAction {
	@Resource
	private ScService scService;
	@Resource
	private CourseService courseService;
	@Resource
	private SignService signService;
	
	private List<Object[]> signInfoList;//返回签到信息列表
	public List<Object[]> getSignInfoList() {return signInfoList;}

	private int c_id;//通过点击课程、传入course的id
	public void setC_id(int c_id) {this.c_id = c_id;}


	private List<Course> courseList;//返回课程信息到select列表选项
	public List<Course> getCourseList() {return courseList;}

	private int t_id;
	public void setT_id(int t_id) {this.t_id = t_id;}
	
	//获得签到信息管理界面
	public String getSignInfoView(){
		courseList = courseService.getCourseByT_id(t_id);
		return "signInfoView";
	}

	//显示签到信息列表
	public String showsSignInfo() throws IOException{
		signInfoList = signService.getSignInfoByCourseId(c_id);
		return "signInfoList";
	}
	
	//获得识别签到管理界面
	public String getRecognizeSignView(){
		courseList = courseService.getCourseByT_id(t_id);
		return "recognizeSignView";
	}
	//显示识别签到列表
	public String showRecognizeSign(){
		//signInfoList = signService.getSignInfoByCourseId(c_id);
		return "recognizeSignList";
	}
	public String getChuchaiForm(){
		//signInfoList = signService.getSignInfoByCourseId(c_id);
		return "chuchaiForm";
	}
	
	public String getSummaryForm(){
		//signInfoList = signService.getSignInfoByCourseId(c_id);
		return "summaryForm";
	}
	
}
