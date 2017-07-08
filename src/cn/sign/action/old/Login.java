/**
 * Project             :FaceRecognise project
 * Comments            :��½������
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ���� | jxm 
 * 2 | 2013-8-16 | ��ӻس�����½���ܣ����ؽ���ʱ���ֶ�̬��ʾЧ�� | jxm 
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
 * @��¼����ļ�����
 * @1��LoginAction����ʼ��������loginView��ͼ����д�رմ��ڵķ���
 * @2��actionPerformed��������ť�ĵ���¼����ж��ǵ�¼����ȡ������¼ʱ����login��������
 * @3��login������TabbedPane��ǩ�µĵ�¼��ť�飬����ͬģʽ�µĵ�¼
 * @4��stateChanged����ӦTabbedPane��ǩ�ı䣬
 * Ӧ��recogniseService����ʶ����� Ҫ����ͨ�û���¼��������ͷ�����ͷ��Դ��
 */
public class Login implements ActionListener, ChangeListener, KeyListener {

	// ��½����
	private LoginView loginView;
	// RecogniseService����
	private RecogniseService recogniseService;
	// ���RecogniseService������
	private BlockingQueue<Integer> recogniseResult = new LinkedBlockingDeque<Integer>();
	// ��½ģ��
	private LoginModel userModel;

	/**
	 * Description :���캯�� �½�LoginView���󣬲�����д�ô��ڵĹرշ���
	 * 
	 * @return LoginAction
	 */
	public Login() {

		loginView = new LoginView(this); // ������¼��ͼ����Ķ�����ɳ�ʼ����

		// ��дloginView���ڵĹرշ���
		loginView.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (RecogniseService.getInstance() != null) {
					// ����RecogniseService�߳�
					RecogniseService.getInstance().setFlag(false);

					// �ͷ�����ͷcapture��Դ
					RecogniseService.getInstance().releaseCapture();
				}

				System.exit(0);
			}
		});
		userModel = new LoginModel();
	}

	/**
	 * ������¼��ȡ����ť�ĳ����¼� ��½��ť��3�֣�1����ͨ�û���¼ 2������ʶ���¼ 3������Ա��¼ Description
	 * :��½����İ�ť��Ӧ��������дActionListener��������actionPerformed��������
	 * 
	 * @param e
	 *            :��Ӧ�¼�
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("buttonLogin")) {// ��½��ť��Ӧ
			login(); // ���õ�¼����������ͬģʽ�µĵ�¼
		} else if (e.getActionCommand().equals("buttonCancle")) {// ȡ����½
			if (RecogniseService.getInstance() != null) {

				// ����RecogniseService�߳�
				RecogniseService.getInstance().setFlag(false);

				// �ͷ�����ͷcapture��Դ
				RecogniseService.getInstance().releaseCapture();

				// System.exit(0);
			}
			System.exit(0);

		}
	}

	/**
	 * Description :��ӦTabbedPane��ǩ�ı䣺��дChangeListener��������stateChanged��������
	 * 
	 * @param e
	 *            :��Ӧ�¼�
	 * @return void
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (recogniseService == null) {// ���RecogniseServiceΪ�գ��򴴽�RecogniseService
					recogniseService = RecogniseService.getInstance(null);
				}

				if (Login.this.loginView.getTabbedPane().getSelectedIndex() > 0) {// ������ʶ���½ģʽ�͹���ģʽ����Ҫ����ʶ���������ȴ�����
					if (recogniseService.getState() == State.WAITING) {
						RecogniseService.getInstance(null).Resume();
					}
				} else if (Login.this.loginView.getTabbedPane().getSelectedIndex() == 0) {// ���л����û���������ģʽ������Ҫ����ʶ�������䴦�ڵȴ�״̬
					if (recogniseService.getState() != State.WAITING) {
						// recogniseService.setM_mode(MODES.MODE_WAIT);
						recogniseService.waitMode();
					}
				}

			}
		}).start();

	}

	/**
	 * ����TabbedPane��ǩ�µĵ�¼��ť�飬����ͬģʽ�µĵ�¼
	 */
	public void login() {
		if (this.loginView.getTabbedPane().getSelectedIndex() == 0) {// �û����������½

			UserBean tempUser = new UserBean();
			tempUser.setId(this.loginView.getTextFieldAccount().getText());
			tempUser.setPwd(new String(this.loginView.getPasswordField().getPassword()));

			// ����û��Ƿ����
			boolean result = userModel.checkUserByPwd(tempUser);
			if (!result) {// �����ڣ�������ʾ�Ի���
				JOptionPane.showMessageDialog(loginView, "��������˺Ŵ���");
			} else {// ����
				loginView.dispose();
				openManage(tempUser.getId(),0);
			}
		} else if (this.loginView.getTabbedPane().getSelectedIndex() == 1) {// ����ʶ���½ģʽ
			RecogniseService.getInstance(recogniseResult).recognitionMode();
			try {
				// ȡ�����
				int r = recogniseResult.take(); // ��ȡ���Ƴ���recogniseResult���е�ͷ��
				if (r >= 0) {// IDΪ0������Ա

					// �ǹ���ģʽ��½������Ϊ1
					// new ManageAction(1);
					//r=0�ǹ���Ա��½ģʽ
					openManage(String.valueOf(r),0);//String.valueOf(r),��ɾ����ĳ���û����ݱ����ݵ�û�и������ݿ�ʱ������ֽ���titleû�����Ƶ����
					
					loginView.dispose();
					
					RecogniseService.getInstance(null).waitMode();
				} else if (r == -1) {// �����ڸ��ˣ�������ʾ�Ի���
					JOptionPane.showMessageDialog(loginView, "δע���û���");
				} else if (r == -2) {
					JOptionPane.showMessageDialog(loginView, "����Ա��ã���δ������������£�����ʶ���ܲ����ã��뾡�����������");

					openManage(String.valueOf(r),0);
					loginView.dispose();
					RecogniseService.getInstance(null).waitMode();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if (this.loginView.getTabbedPane().getSelectedIndex() == 2) {// ����&������½

			UserBean tempUser = new UserBean();
			tempUser.setId(this.loginView.getTextFieldAccount_1().getText());
			tempUser.setPwd(new String(this.loginView.getPasswordField_1().getPassword()));
			
			// �ȼ���û����������Ƿ���ȷ
			boolean result = userModel.checkUserByPwd(tempUser);
			
			if (result) {// �û�����������ȷ
				
				int r = -1;
				// ����ʶ��
				RecogniseService.getInstance(recogniseResult).recognitionMode();
				try {
					// ȡ�ý��
					r = recogniseResult.take();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				if (r == Integer.parseInt(tempUser.getId())) {// �û��������������ʶ����һ��
					openManage(String.valueOf(r),0);//String.valueOf(r),��ɾ����ĳ���û����ݱ����ݵ�û�и������ݿ�ʱ������ֽ���titleû�����Ƶ����

					loginView.dispose();
					RecogniseService.getInstance(null).waitMode();
				} else if (r == -1) {// ����ʶ��δͨ��

					if ("��".equals(tempUser.getHavePhoto())) {
						JOptionPane.showMessageDialog(loginView, "����ʶ��δͨ��");
					} else if ("��".equals(tempUser.getHavePhoto())) {
							JOptionPane.showMessageDialog(loginView, "δ¼����Ƭ��������������ģʽ");
					}
				} else if (r == -2) {
					JOptionPane.showMessageDialog(loginView, "��δ������������£�����ʶ���ܲ�����");
				} else {// ����ʶ��ͨ�������Ǻ��û���������Ľ����һ��
					JOptionPane.showMessageDialog(loginView, "����ʶ�����¼�˺Ų���ͬһ��");
				}

			} else {// �˺ż��������
				JOptionPane.showMessageDialog(loginView, "��������˺Ŵ���");
			}
		}

	}

	// ���ع���Ա�Ĺ������
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
	 * ��дKeyListener�������ķ���
	 */
	@Override
	public void keyPressed(KeyEvent e) { // �÷����ڱ�����û�б����ù�������������������û��ʹ��
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
