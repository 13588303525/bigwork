package cn.sign.model.old;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.sign.service.old.RecogniseService;
import cn.sign.service.old.UserDBService;

public class AddUserModel {
	private UserDBService userDBService;

	public AddUserModel() {
		userDBService = new UserDBService();
	}
	
	public void addUser(UserBean user) {
		RecogniseService.getInstance(null).saveUser(user);
	}
	
	public void entryPhotosManual(BlockingQueue result) {
		if (RecogniseService.getInstance(null).getState() == State.WAITING) {
			RecogniseService.getInstance(null).Resume();
		}
		RecogniseService.getInstance(result).entryPhotoManual();
	}
	public void entryPhotos(BlockingQueue result) {
		if (RecogniseService.getInstance(null).getState() == State.WAITING) {
			RecogniseService.getInstance(null).Resume();
		}
		RecogniseService.getInstance(result).entryPhoto();
	}

}
