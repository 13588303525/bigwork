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
	public void entryPhotos(BlockingQueue result){//自动录入人脸，每个1秒录一张
		if(RecogniseService.getInstance(null).getState() == State.WAITING){
			RecogniseService.getInstance(null).Resume();
		}
		//RecogniseService.getInstance(result).setM_mode(MODES.MODE_COLLECT_FACES);
		RecogniseService.getInstance(result).entryPhoto();//按时间和前后对比相似度来采集图像
	}
	public void entryPhotosManual(BlockingQueue result){//手工录入人脸
		if(RecogniseService.getInstance(null).getState() == State.WAITING){
			RecogniseService.getInstance(null).Resume();
		}
		//RecogniseService.getInstance(result).setM_mode(MODES.MODE_COLLECT_FACES);
		RecogniseService.getInstance(result).entryPhotoManual();//手动控制图像的采集
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
