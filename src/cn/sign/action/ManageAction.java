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
	
	private List<Object[]> signInfoList;//����ǩ����Ϣ�б�
	public List<Object[]> getSignInfoList() {return signInfoList;}

	private int c_id;//ͨ������γ̡�����course��id
	public void setC_id(int c_id) {this.c_id = c_id;}


	private List<Course> courseList;//���ؿγ���Ϣ��select�б�ѡ��
	public List<Course> getCourseList() {return courseList;}

	private int t_id;
	public void setT_id(int t_id) {this.t_id = t_id;}
	
	//���ǩ����Ϣ�������
	public String getSignInfoView(){
		courseList = courseService.getCourseByT_id(t_id);
		return "signInfoView";
	}

	//��ʾǩ����Ϣ�б�
	public String showsSignInfo() throws IOException{
		signInfoList = signService.getSignInfoByCourseId(c_id);
		return "signInfoList";
	}
	
	//���ʶ��ǩ���������
	public String getRecognizeSignView(){
		courseList = courseService.getCourseByT_id(t_id);
		return "recognizeSignView";
	}
	//��ʾʶ��ǩ���б�
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
