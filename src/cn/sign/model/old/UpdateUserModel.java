package cn.sign.model.old;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.sign.service.old.RecogniseService;
import cn.sign.service.old.UserDBService;

public class UpdateUserModel {
	public void updateUser(UserBean user){
		RecogniseService.getInstance(null).updateUser(user);
	}
	public void entryPhotos(BlockingQueue result){//�Զ�¼��������ÿ��1��¼һ��
		if(RecogniseService.getInstance(null).getState() == State.WAITING){
			RecogniseService.getInstance(null).Resume();
		}
		//RecogniseService.getInstance(result).setM_mode(MODES.MODE_COLLECT_FACES);
		RecogniseService.getInstance(result).entryPhoto();//��ʱ���ǰ��Ա����ƶ����ɼ�ͼ��
	}
	public void entryPhotosManual(BlockingQueue result){//�ֹ�¼������
		if(RecogniseService.getInstance(null).getState() == State.WAITING){
			RecogniseService.getInstance(null).Resume();
		}
		//RecogniseService.getInstance(result).setM_mode(MODES.MODE_COLLECT_FACES);
		RecogniseService.getInstance(result).entryPhotoManual();//�ֶ�����ͼ��Ĳɼ�
	}
	public UserBean getUserById(String id){
		return new UserDBService().getUserById(id);
	}
	public void refreshUserTableUpdate(DefaultTableModel model, int index,
			UserBean user) {
		model.setValueAt(user.getId(), index, 0);
		model.setValueAt(user.getName(), index, 1);
		model.setValueAt(user.getHavePhoto(), index, 2);
	}
}
