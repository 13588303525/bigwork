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
	 * ͨ��������˺��������ʦ�Ƿ���� return boolean������teacher���t_name
	 */
	public List<Object[]> getSignInfoByCourseId(int c_id) {
		List<Object[]> signList = signDao.getSignListByCourseId(c_id);
		if(signList.size()==0){	//���γ���ʱû��ǩ����¼
			return null;
		}
		
		List<Object[]> stuSignList = new ArrayList<Object[]>();
		ArrayList<Object> obj;
		for(Object[] stu : signList){
			obj = new ArrayList<Object>();
			String[] scores = stu[2].toString().split(",");
			obj.add(stu[0]);	//ѧ��
			obj.add(stu[1]);	//����
			int mark = 0;
			for(String score : scores){
				mark += Integer.parseInt(score);
				obj.add(score);	//ǩ���÷�
			}
			obj.add(mark);		//�ܷ�
			stuSignList.add(obj.toArray());
			
		}
		
		/*	������scoreΪint��ʱ�����ݣ������������ǳ��󣬲�������
		
		List<Object[]> stuSignList = new ArrayList<Object[]>();
		
		int currentStu = (int) signList.get(0)[0];	//��ǰѧ���ı�ʶ
		int mark = 0;	//��¼��ǰѧ�����γ�ǩ�����ۼƵ÷�
		ArrayList<Object> obj = new ArrayList<Object>();	//���ڴ洢��ǰѧ����ǩ����¼
		obj.add(signList.get(0)[0]);	//��ӵ�ǰѧ����ѧ�š�����
		obj.add(signList.get(0)[1]);
		for(int i=0; i<signList.size(); i++){
			if(currentStu == (int) signList.get(i)[0]){	//ͬһ��ѧ����ǩ����¼
				obj.add(signList.get(i)[2]);	//����ѧ����ǩ���÷�score��ӵ���¼��
				mark += (int) signList.get(i)[2];	//��¼��ѧ�����ۼƵ÷�
			}else{
				obj.add(mark);	//�����һ��ѧ����ǩ���ܵ÷�
				stuSignList.add(obj.toArray());	//�����һλѧ���ÿγ̵�ǩ����¼��stuSignList��
				currentStu = (int) signList.get(i)[0];	//����currentStu��ʶ
				mark = 0;	//�����һλѧ�����ۼƵ÷�
				
				obj = new ArrayList<Object>();	//������ѧ����ǩ����¼
				obj.add(signList.get(i)[0]);
				obj.add(signList.get(i)[1]);
			}
		}*/
		return stuSignList;
	}

}