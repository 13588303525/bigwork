/**
 * Project             :FaceRecognise project
 * Comments            :管理控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 * 2 | 2013-8-11 | 添加双击打开视频识别结果文件夹;添加删除所有用户按钮| jxm
 * 3 | 2013-8-13 | 修改“删除所有用户”中关于界面更新的bug| jxm
 */
package cn.sign.action.old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import cn.sign.model.old.ManageModel;
import cn.sign.model.old.UserBean;
import cn.sign.service.old.RecogniseService;
import cn.sign.view.ManageView;

public class Manage implements ActionListener, ListSelectionListener, ChangeListener, MouseListener {
	// 管理员界面
	private ManageView manageView;
	// 管理员模型
	private ManageModel manageModel;
	// 被选择的行数组
	private int[] selectedRows;

	private String strSearch;

	// 当前用户
	private UserBean user;
	private BlockingQueue<Boolean> loadViewResult;

	/**
	 * Description :构造函数
	 * 
	 * @param mode
	 *            : 登陆模式
	 * @return ManageAction
	 */
	public Manage(String id, int mode, BlockingQueue<Boolean> loadViewResult) {

		this.loadViewResult = loadViewResult;

		manageModel = new ManageModel();
		
		// 根据用户ID得到用户
		user = manageModel.getUserById(id);

		if (mode == 0) {// 管理模式登陆
			manageView = new ManageView(this);
		} else {// 非管理模式登陆
			manageView = new ManageView(this);
			// 管理模式下所有的按钮都不可用
			disableAllButton();
		}
		manageView.setTitle("普通用户:" + user.getName());
		// 重写manageView窗口的关闭方法
		manageView.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (RecogniseService.getInstance() != null) {

					// 结束RecogniseService线程
					RecogniseService.getInstance().setFlag(false);

					// 释放摄像头capture资源
					RecogniseService.getInstance().releaseCapture();
				}
				System.exit(0);
			}
		});
		try {
			this.loadViewResult.put(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Description :管理界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new ManageResponseThread(e.getActionCommand()).start();

	}

	/**
	 * Description :Jtable事件响应
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// 得到选择的行数组
		selectedRows = manageView.getTable().getSelectedRows();

		// 激活删除和修改按钮
		manageView.getButtonDelete().setEnabled(true);
		manageView.getButtonUpdate().setEnabled(true);

	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class ManageResponseThread extends Thread {
		private String actionCommand;

		public ManageResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {

			if (actionCommand.equals("buttonSeacher")) {// 查询按钮响应

				strSearch = manageView.getTextFieldSearch().getText();

				if ("".equals(strSearch)) {// 查询字段为空，则为全部查询（第一页）
					if (SwingUtilities.isEventDispatchThread()) {//事件分发线程
						// 显示全部查询字段结果，不设分页
						manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
								manageModel.getUsersByAccount(strSearch));

					} else {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								// 显示全部查询字段结果，不设分页
								manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
										manageModel.getUsersByAccount(strSearch));

							}

						});
					}

				} else {// 查询字段不为空

					if (SwingUtilities.isEventDispatchThread()) {
						// 显示全部查询字段结果，不设分页
						manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
								manageModel.getUsersByAccount(strSearch));
					} else {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// 显示全部查询字段结果，不设分页
								manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
										manageModel.getUsersByAccount(strSearch));
							}

						});
					}

				}
			}

			else if (actionCommand.equals("buttonTrain")) {// 训练图片按钮响应
				manageView.getButtonTrain().setEnabled(false);

				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);

				try {
					boolean r = result.take();
					if (r) {// 返回true，成功更新人脸库，弹出提示对话框
						JOptionPane.showMessageDialog(manageView, "人脸库更新完毕！");
					} else {// 返回false，更新人脸库失败，弹出提示对话框
						JOptionPane.showMessageDialog(manageView, "至少需要两个人照片信息才能更新人脸库");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				manageView.getButtonTrain().setEnabled(true);
			} else if (actionCommand.equals("buttonAdd")) {// 添加用户按钮响应

				// 添加用户
				new AddUserAction(manageView, (DefaultTableModel) manageView.getTable().getModel());
			} else if (actionCommand.equals("buttonUpdate")) {// 修改用户信息按钮响应
				if (selectedRows != null) {

					// 得到ID号，只取一个
					String id = manageView.getTable().getValueAt(selectedRows[0], 0).toString();
					// 修改用户
					new UpdateUserAction(manageView, id, selectedRows[0], (DefaultTableModel) manageView.getTable().getModel(), 0);

					manageView.getButtonUpdate().setEnabled(false);
				}

			} else if (actionCommand.equals("buttonDelete")) {// 删除按钮响应
				if (selectedRows != null) {// 删除
					for (int i = 0; i < selectedRows.length; i++) {
						String sn = manageView.getTable().getValueAt(selectedRows[i], 0).toString();
						if (sn != null && sn.trim()!="") {
							manageModel.deleteUserById(sn);
						}
					}
				}
				
				

				//更新人脸数据
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);
				
				try {
					boolean r = result.take();
					if (r) {// 返回true，成功更新人脸库，弹出提示对话框
						JOptionPane.showMessageDialog(manageView, "人脸库更新完毕！");
					} else {// 返回false，更新人脸库失败，弹出提示对话框
						JOptionPane.showMessageDialog(manageView, "至少需要两个人照片信息才能更新人脸库");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

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
	}

	/**
	 * Description :响应TabbedPane标签改变
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (manageView.getTabbedPane().getSelectedIndex() == 1) {// 切换到视频人脸识别任务

		}

	}

	/**
	 * Description :设置按钮不能使用
	 * 
	 * @return void
	 */
	public void disableAllButton() {
		manageView.getButtonAdd().setEnabled(false);
		manageView.getButtonSeacher().setEnabled(false);
		manageView.getButtonTrain().setEnabled(false);
		manageView.getButtonUpdate().setEnabled(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 2) {

			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	class OpenManageView extends Thread {

		@Override
		public void run() {
			manageView = new ManageView(Manage.this);
		}

	}
}
