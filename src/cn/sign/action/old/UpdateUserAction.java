/**
 * Project             :FaceRecognise project
 * Comments            :更新用户控制类，
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.action.old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import cn.sign.model.old.ManageModel;
import cn.sign.model.old.UpdateUserModel;
import cn.sign.model.old.UserBean;
import cn.sign.service.old.RecogniseService;
import cn.sign.view.AddUserView;
import cn.sign.view.ManageView;
import cn.sign.view.UpdateUserView;

public class UpdateUserAction implements ActionListener {

	// 修改用户信息解密那
	private UpdateUserView updateUserView;
	// 修改用户信息模型
	private UpdateUserModel updateUserModel;
	// Jtable模型
	private DefaultTableModel model;

	//
	private ManageView manageView;
	private int FACENUM = 0; //人脸采集的数量，即addButton点击的数量
	//
	private ManageModel manageModel;
	// 行号
	private int row;
	// 修改用户
	private UserBean user;

	/**
	 * Description :构造函数
	 * 
	 * @param id
	 *            :修改用户ID
	 * @param row
	 *            ：该用户所在行号
	 * @param model
	 *            ：Jtable 模型
	 * @param mode
	 *            : 修改模式
	 * @return UpdateUserAction
	 */
	public UpdateUserAction(ManageView manageView, String id, int row, DefaultTableModel model, int mode) {
		updateUserModel = new UpdateUserModel();
		manageModel = new ManageModel();
		this.manageView = manageView;
		this.model = model;
		// 根据ID号获的用户
		user = updateUserModel.getUserById(id);

		this.model = model;
		this.row = row;

		updateUserView = new UpdateUserView(this, user);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new UpdateResponseThread(e.getActionCommand()).start();
	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class UpdateResponseThread extends Thread {
		private String actionCommand;

		public UpdateResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {

			if (actionCommand.equals("buttonEntryPhotos")) {// 录入照片按钮响应
				
				// 显示提示信息
				updateUserView.getLabelShowResult().setText("正在录入照片！");

				// 调用RecogniseService录入照片，result用来存放结果
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				//addUserModel.entryPhotos(result);
				updateUserModel.entryPhotosManual(result);
				
			}else if (actionCommand.equals("buttonAdd")) {// 添加人脸按钮响应
				if(FACENUM < 6){
					System.out.println("添加");
					BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
					RecogniseService.getInstance(result).manualCollectMode(); //null代表人脸采集刚开始，并未成功？
					
					if(RecogniseService.gotFace){//有人脸时才算添加一次
						FACENUM++;
					}
				}else{
					try {
						// 含有照片标识置为true
						// 显示提示信息
						updateUserView.getLblelEntryPhoto().setText("是");
						updateUserView.getLabelShowResult().setText("结束！");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				}
			} else if (actionCommand.equals("buttonSure")) {// 确定按钮响应
				user.setId(updateUserView.getTextFieldAccount().getText());
				user.setPwd(new String(updateUserView.getPasswordField()
						.getPassword()));
				user.setName(updateUserView.getTextFieldName().getText());
				user.setHavePhoto(updateUserView.getLblelEntryPhoto().getText());
				user.setPicsPath("pics/");

				updateUserModel.updateUser(user);

				updateUserView.getLabelShowResult().setText("成功!");
				
				//加载显示列表
				if (SwingUtilities.isEventDispatchThread()) {
					manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
							manageModel.getUsersByAccount(""));
				} else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// 显示全部查询字段结果，不设分页
							manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
									manageModel.getUsersByAccount(""));

						}

					});
				}

				
				
				
				//更新人脸数据
				
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);

				try {
					boolean r = result.take();
					if (r) {// 返回true，成功更新人脸库，弹出提示对话框
						JOptionPane.showMessageDialog(updateUserView, "人脸库更新完毕！");
					} else {// 返回false，更新人脸库失败，弹出提示对话框
						JOptionPane.showMessageDialog(updateUserView, "至少需要两个人照片信息才能更新人脸库");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
			} else if (actionCommand.equals("buttonReset")) {// 重置按钮响应
				reset();
			} else if (actionCommand.equals("buttonQuit")) {// 退出按钮响应
				// 若开启了RecogniseService服务并且其状态不是等待状态，那么就将其设置为等待状态
				if (RecogniseService.getInstance() != null
						&& RecogniseService.getInstance().getState() != State.WAITING) {
					RecogniseService.getInstance(null).waitMode();
				}
				updateUserView.dispose();
			}
		}

	}

	public void reset() {
		updateUserView.getTextFieldAccount().setText("");
		updateUserView.getPasswordField().setText("");
		updateUserView.getTextFieldName().setText("");
	}

}
