package cn.sign.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sign.dao.ScDao;
import cn.sign.dao.SignDao;
import cn.sign.model.Student;

@Service("signService")
public class SignService {
	@Resource
	private SignDao signDao;

	/**
	 * 通过输入的账号密码检查教师是否存在 return boolean，参数teacher获得t_name
	 */
	public List<Object[]> getSignInfoByCourseId(int c_id) {
		List<Object[]> signList = signDao.getSignListByCourseId(c_id);
		if(signList.size()==0){	//本课程暂时没有签到记录
			return null;
		}
		
		List<Object[]> stuSignList = new ArrayList<Object[]>();
		ArrayList<Object> obj;
		for(Object[] stu : signList){
			obj = new ArrayList<Object>();
			String[] scores = stu[2].toString().split(",");
			obj.add(stu[0]);	//学号
			obj.add(stu[1]);	//姓名
			int mark = 0;
			for(String score : scores){
				mark += Integer.parseInt(score);
				obj.add(score);	//签到得分
			}
			obj.add(mark);		//总分
			stuSignList.add(obj.toArray());
			
		}
		
		/*	以下是score为int型时的数据，数据冗余量非常大，操作繁琐
		
		List<Object[]> stuSignList = new ArrayList<Object[]>();
		
		int currentStu = (int) signList.get(0)[0];	//当前学生的标识
		int mark = 0;	//记录当前学生本课程签到的累计得分
		ArrayList<Object> obj = new ArrayList<Object>();	//用于存储当前学生的签到记录
		obj.add(signList.get(0)[0]);	//添加当前学生的学号、姓名
		obj.add(signList.get(0)[1]);
		for(int i=0; i<signList.size(); i++){
			if(currentStu == (int) signList.get(i)[0]){	//同一个学生的签到记录
				obj.add(signList.get(i)[2]);	//将该学生的签到得分score添加到记录中
				mark += (int) signList.get(i)[2];	//记录该学生的累计得分
			}else{
				obj.add(mark);	//添加上一个学生的签到总得分
				stuSignList.add(obj.toArray());	//添加上一位学生该课程的签到记录到stuSignList中
				currentStu = (int) signList.get(i)[0];	//重置currentStu标识
				mark = 0;	//清空上一位学生的累计得分
				
				obj = new ArrayList<Object>();	//创建本学生的签到记录
				obj.add(signList.get(i)[0]);
				obj.add(signList.get(i)[1]);
			}
		}*/
		return stuSignList;
	}

}