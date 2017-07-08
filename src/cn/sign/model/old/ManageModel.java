package cn.sign.model.old;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.sign.service.old.RecogniseService;
import cn.sign.service.old.UserDBService;

public class ManageModel {
	private UserDBService userDBService;

	public ManageModel() {
		userDBService = new UserDBService();
	}

	public UserBean getUserById(String id){
		return new UserDBService().getUserById(id);
	}
	public void train(BlockingQueue<Boolean> result) {
		RecogniseService.getInstance(result).train();
	}

	public Object[][] getUsersByAccount(String ID) {
		//此处因为hibernate反射UserBean，故暂有问题
		//ArrayList<Object[]> al = (ArrayList<Object[]>) userDBService.getUsersByAccount(ID);
		ArrayList<Object[]> al = null;
		Object[][] data = new Object[al.size()][];
		for (int i = 0; i < al.size(); i++) {
			data[i] = al.get(i);
		}
		return data;
	}

	public void deleteUserById(String id) {
		userDBService.deleteUser(id);
		for (int i = 0; i < 6; i++) {
			File file = new File("pics/" + id + "_" + i + ".bmp");
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public void refreshUserTable(DefaultTableModel model, Object[][] data) {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		for (int i = 0; i < data.length; i++) {
			model.addRow(data[i]);
		}
	}


	public boolean deleteAllUsers() {
		File srcFile = new File("pics/");
		File[] files = srcFile.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.startsWith("0")) {
					return false;
				} else {
					return true;
				}
			}
		});
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].exists()) {
					files[i].delete();
				}
			}
		}

		File file = new File("dat/data.xml");
		if (file.exists()) {
			file.delete();
		}
		userDBService.deleteAllUsers();

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
