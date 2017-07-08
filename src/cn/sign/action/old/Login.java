/**
 * Project             :FaceRecognise project
 * Comments            :登陆控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 * 2 | 2013-8-16 | 添加回车键登陆功能；加载界面时出现动态提示效果 | jxm 
 */
package cn.sign.action.old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.sign.model.old.LoginModel;
import cn.sign.model.old.UserBean;
import cn.sign.service.old.RecogniseService;
import cn.sign.view.LoginView;

/**
 * @author acer
 * @登录界面的监听器
 * @1、LoginAction：初始化，加载loginView视图并重写关闭窗口的方法
 * @2、actionPerformed：监听按钮的点击事件，判断是登录还是取消，登录时调用login（）方法
 * @3、login：根据TabbedPane标签下的登录按钮组，处理不同模式下的登录
 * @4、stateChanged：响应TabbedPane标签改变，
 * 应对recogniseService人脸识别服务， 要在普通用户登录的情况下释放摄像头资源等
 */
public class Login implements ActionListener, ChangeListener, KeyListener {

	// 登陆界面
	private LoginView loginView;
	// RecogniseService服务
	private RecogniseService recogniseService;
	// 存放RecogniseService服务结果
	private BlockingQueue<Integer> recogniseResult = new LinkedBlockingDeque<Integer>();
	// 登陆模型
	private LoginModel userModel;

	/**
	 * Description :构造函数 新建LoginView对象，并且重写该窗口的关闭方法
	 * 
	 * @return LoginAction
	 */
	public Login() {

		loginView = new LoginView(this); // 创建登录视图界面的对象（完成初始化）

		// 重写loginView窗口的关闭方法
		loginView.addWindowListener(new WindowAdapter() {

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
		userModel = new LoginModel();
	}

	/**
	 * 监听登录和取消按钮的出发事件 登陆按钮分3种：1、普通用户登录 2、人脸识别登录 3、管理员登录 Description
	 * :登陆界面的按钮响应函数：重写ActionListener监听器的actionPerformed（）方法
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("buttonLogin")) {// 登陆按钮响应
			login(); // 调用登录方法，处理不同模式下的登录
		} else if (e.getActionCommand().equals("buttonCancle")) {// 取消登陆
			if (RecogniseService.getInstance() != null) {

				// 结束RecogniseService线程
				RecogniseService.getInstance().setFlag(false);

				// 释放摄像头capture资源
				RecogniseService.getInstance().releaseCapture();

				// System.exit(0);
			}
			System.exit(0);

		}
	}

	/**
	 * Description :响应TabbedPane标签改变：重写ChangeListener监听器的stateChanged（）方法
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (recogniseService == null) {// 如果RecogniseService为空，则创建RecogniseService
					recogniseService = RecogniseService.getInstance(null);
				}

				if (Login.this.loginView.getTabbedPane().getSelectedIndex() > 0) {// 在人脸识别登陆模式和管理模式下需要人脸识别服务，如果等待则唤醒
					if (recogniseService.getState() == State.WAITING) {
						RecogniseService.getInstance(null).Resume();
					}
				} else if (Login.this.loginView.getTabbedPane().getSelectedIndex() == 0) {// 若切换到用户名加密码模式，不需要人脸识别，则让其处于等待状态
					if (recogniseService.getState() != State.WAITING) {
						// recogniseService.setM_mode(MODES.MODE_WAIT);
						recogniseService.waitMode();
					}
				}

			}
		}).start();

	}

	/**
	 * 根据TabbedPane标签下的登录按钮组，处理不同模式下的登录
	 */
	public void login() {
		if (this.loginView.getTabbedPane().getSelectedIndex() == 0) {// 用户名加密码登陆

			UserBean tempUser = new UserBean();
			tempUser.setId(this.loginView.getTextFieldAccount().getText());
			tempUser.setPwd(new String(this.loginView.getPasswordField().getPassword()));

			// 检查用户是否存在
			boolean result = userModel.checkUserByPwd(tempUser);
			if (!result) {// 不存在，弹出提示对话框
				JOptionPane.showMessageDialog(loginView, "密码或是账号错误");
			} else {// 存在
				loginView.dispose();
				openManage(tempUser.getId(),0);
			}
		} else if (this.loginView.getTabbedPane().getSelectedIndex() == 1) {// 人脸识别登陆模式
			RecogniseService.getInstance(recogniseResult).recognitionMode();
			try {
				// 取出结果
				int r = recogniseResult.take(); // 获取并移除此recogniseResult队列的头部
				if (r >= 0) {// ID为0，管理员

					// 非管理模式登陆，参数为1
					// new ManageAction(1);
					//r=0非管理员登陆模式
					openManage(String.valueOf(r),0);//String.valueOf(r),当删除了某个用户数据表数据但没有更新数据库时，会出现界面title没有名称的情况
					
					loginView.dispose();
					
					RecogniseService.getInstance(null).waitMode();
				} else if (r == -1) {// 不存在该人，弹出提示对话框
					JOptionPane.showMessageDialog(loginView, "未注册用户！");
				} else if (r == -2) {
					JOptionPane.showMessageDialog(loginView, "管理员你好：还未进行人脸库更新，人脸识别功能不可用，请尽快更新人脸库");

					openManage(String.valueOf(r),0);
					loginView.dispose();
					RecogniseService.getInstance(null).waitMode();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if (this.loginView.getTabbedPane().getSelectedIndex() == 2) {// 密码&人脸登陆

			UserBean tempUser = new UserBean();
			tempUser.setId(this.loginView.getTextFieldAccount_1().getText());
			tempUser.setPwd(new String(this.loginView.getPasswordField_1().getPassword()));
			
			// 先检查用户名加密码是否正确
			boolean result = userModel.checkUserByPwd(tempUser);
			
			if (result) {// 用户名加密码正确
				
				int r = -1;
				// 人脸识别
				RecogniseService.getInstance(recogniseResult).recognitionMode();
				try {
					// 取得结果
					r = recogniseResult.take();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				if (r == Integer.parseInt(tempUser.getId())) {// 用户名加密码和人脸识别结果一致
					openManage(String.valueOf(r),0);//String.valueOf(r),当删除了某个用户数据表数据但没有更新数据库时，会出现界面title没有名称的情况

					loginView.dispose();
					RecogniseService.getInstance(null).waitMode();
				} else if (r == -1) {// 人脸识别未通过

					if ("是".equals(tempUser.getHavePhoto())) {
						JOptionPane.showMessageDialog(loginView, "人脸识别未通过");
					} else if ("否".equals(tempUser.getHavePhoto())) {
							JOptionPane.showMessageDialog(loginView, "未录入照片，不允许进入管理模式");
					}
				} else if (r == -2) {
					JOptionPane.showMessageDialog(loginView, "还未进行人脸库更新，人脸识别功能不可用");
				} else {// 人脸识别通过，但是和用户名加密码的结果不一致
					JOptionPane.showMessageDialog(loginView, "人脸识别与登录账号不是同一人");
				}

			} else {// 账号加密码错误
				JOptionPane.showMessageDialog(loginView, "密码或是账号错误");
			}
		}

	}

	// 加载管理员的管理界面
	public void openManage(String id, int mode) {
		new OpenManageThread(id, mode).start();
	}

	class OpenManageThread extends Thread {
		private int mode;
		private String id;
		private BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();

		public OpenManageThread(String id, int mode) {
			this.id = id;
			this.mode = mode;
		}

		@Override
		public void run() {

			new Manage(id, mode, result);
			try {
				result.take();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 重写KeyListener监听器的方法
	 */
	@Override
	public void keyPressed(KeyEvent e) { // 该方法在本类中没有被调用过，以下三个方法可能没有使用
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			login();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
