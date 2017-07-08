/**
 * Project             :FaceRecognise project
 * Comments            :增加用户控制类，
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 * 2 | 2013-4-30 |增加“是否有照片”这一项的判断 |jxm  
 */
package cn.sign.action.old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import cn.sign.model.old.AddUserModel;
import cn.sign.model.old.ManageModel;
import cn.sign.model.old.UserBean;
import cn.sign.service.old.RecogniseService;
import cn.sign.view.AddUserView;
import cn.sign.view.ManageView;

public class AddUserAction implements ActionListener {
	
	//添加用户界面
	private AddUserView addUserView;
	//添加用户模型
	private AddUserModel addUserModel;
	//JTable模型
	private DefaultTableModel model;
	
	private int FACENUM = 0; //人脸采集的数量，即addButton点击的数量
	//
	private ManageModel manageModel;
	private ManageView manageView;
	//是否已录入照片标识
	private boolean isEntryPhotos = false;

	/**
	 * Description :构造函数
	 * 
	 * @param model
	 *            :Jtable模型
	 * @param page
	 *            ：当前所处的页
	 * @return AddUserAction
	 */
	public AddUserAction(ManageView manageView,DefaultTableModel model) {
		addUserView = new AddUserView(this);
		manageModel = new ManageModel();
		this.model = model;
		this.manageView = manageView;
		addUserModel = new AddUserModel();
	}

	/**
	 * Description :增加用户界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		new AddUserResponseThread(e.getActionCommand()).start();
	}

	/**
	 * Description :将增加用户界面的填写信息和提示信息清空
	 * 
	 * @return void
	 */
	public void reset() {
		addUserView.getTextFieldAccount().setText("");
		addUserView.getPasswordField().setText("");
		addUserView.getTextFieldName().setText("");
		addUserView.getLabelShowResult().setText("");
	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class AddUserResponseThread extends Thread {
		private String actionCommand;
		public AddUserResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {
			if (actionCommand.equals("buttonEntryPhotos")) {// 录入照片按钮响应

				// 显示提示信息
				addUserView.getLabelShowResult().setText("正在录入照片！");

				// 调用RecogniseService录入照片，result用来存放结果
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				//addUserModel.entryPhotos(result);
				addUserModel.entryPhotosManual(result);

			} else if (actionCommand.equals("buttonAdd")) {//添加照片按钮响应
				if(FACENUM < 6){
					System.out.println("添加");
					BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
					RecogniseService.getInstance(result).manualCollectMode(); //null代表人脸采集刚开始，并未成功？
					
					if(RecogniseService.gotFace){//有人脸时才算添加一次
						FACENUM++;
					}
					if(FACENUM==5){
						isEntryPhotos = true;
					}
				}else{
					try {
						// 含有照片标识置为true

						// 显示提示信息
						addUserView.getLabelHavePhoto().setText("是");
						addUserView.getLabelShowResult().setText("结束！");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				}
			}else if (actionCommand.equals("buttonSure")) {// 确定按钮响应

				FACENUM=0;
				/*
				 * 读取填入的用户信息
				 */
				UserBean user = new UserBean();
				
				if("".equals(addUserView.getTextFieldAccount().getText())){
					JOptionPane.showMessageDialog(addUserView, "用户名不能为空");	
				}else{
					user.setId(addUserView.getTextFieldAccount().getText()); //获得账号
					
					String pwd = new String(addUserView.getPasswordField()
							.getPassword());
					if("".equals(pwd)){
						JOptionPane.showMessageDialog(addUserView, "密码不能为空");	
					}else{
						user.setPwd(pwd);//获得密码
						
						if("".equals(addUserView.getTextFieldName().getText())){
							user.setName("未填写");
						}else{
							user.setName(addUserView.getTextFieldName().getText());	
						}
						
						if (isEntryPhotos) {
							user.setHavePhoto("是");
							isEntryPhotos = false;
						} else {
							user.setHavePhoto("否");
						}
						
						user.setPicsPath("pics/");

						// 添加用户信息，包括数据存储（存储身份信息）和文件存储（存储照片信息）
						addUserModel.addUser(user);
						
						//更新人脸数据
						if(user.getHavePhoto().equals("是")){

							BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
							manageModel.train(result);

							try {
								boolean r = result.take();
								if (r) {// 返回true，成功更新人脸库，弹出提示对话框
									JOptionPane.showMessageDialog(addUserView, "人脸库更新完毕！");
								} else {// 返回false，更新人脸库失败，弹出提示对话框
									JOptionPane.showMessageDialog(addUserView, "至少需要两个人照片信息才能更新人脸库");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							//
						}
						
						// 设置提示信息
						addUserView.getLabelShowResult().setText("成功！");
						
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
					}
				}
			} else if (actionCommand.equals("buttonReset")) {// 重置按钮响应
				// 重置
				reset();
			} else if (actionCommand.equals("buttonQuit")) {// 退出按钮响应
				if (RecogniseService.getInstance() != null
						&& RecogniseService.getInstance().getState() != State.WAITING) {
					// 若开启了RecogniseService服务并且其状态不是等待状态，那么就将其设置为等待状态
					RecogniseService.getInstance(null).waitMode();
					
					// 结束RecogniseService线程
					//RecogniseService.getInstance().setFlag(false);
					// 释放摄像头capture资源
					//RecogniseService.getInstance().releaseCapture();
				}

				// 添加用户窗口消失
				addUserView.dispose();
			} 
			
			

		}
	}
}
