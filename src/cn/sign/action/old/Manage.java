/**
 * Project             :FaceRecognise project
 * Comments            :���������
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ���� | jxm 
 * 2 | 2013-8-11 | ���˫������Ƶʶ�����ļ���;���ɾ�������û���ť| jxm
 * 3 | 2013-8-13 | �޸ġ�ɾ�������û����й��ڽ�����µ�bug| jxm
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
	// ����Ա����
	private ManageView manageView;
	// ����Աģ��
	private ManageModel manageModel;
	// ��ѡ���������
	private int[] selectedRows;

	private String strSearch;

	// ��ǰ�û�
	private UserBean user;
	private BlockingQueue<Boolean> loadViewResult;

	/**
	 * Description :���캯��
	 * 
	 * @param mode
	 *            : ��½ģʽ
	 * @return ManageAction
	 */
	public Manage(String id, int mode, BlockingQueue<Boolean> loadViewResult) {

		this.loadViewResult = loadViewResult;

		manageModel = new ManageModel();
		
		// �����û�ID�õ��û�
		user = manageModel.getUserById(id);

		if (mode == 0) {// ����ģʽ��½
			manageView = new ManageView(this);
		} else {// �ǹ���ģʽ��½
			manageView = new ManageView(this);
			// ����ģʽ�����еİ�ť��������
			disableAllButton();
		}
		manageView.setTitle("��ͨ�û�:" + user.getName());
		// ��дmanageView���ڵĹرշ���
		manageView.addWindowListener(new WindowAdapter() {

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
		try {
			this.loadViewResult.put(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Description :�������İ�ť��Ӧ����
	 * 
	 * @param e
	 *            :��Ӧ�¼�
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new ManageResponseThread(e.getActionCommand()).start();

	}

	/**
	 * Description :Jtable�¼���Ӧ
	 * 
	 * @param e
	 *            :��Ӧ�¼�
	 * @return void
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// �õ�ѡ���������
		selectedRows = manageView.getTable().getSelectedRows();

		// ����ɾ�����޸İ�ť
		manageView.getButtonDelete().setEnabled(true);
		manageView.getButtonUpdate().setEnabled(true);

	}

	/**
	 * Description :�ڲ��࣬����ť��Ӧ
	 */
	class ManageResponseThread extends Thread {
		private String actionCommand;

		public ManageResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {

			if (actionCommand.equals("buttonSeacher")) {// ��ѯ��ť��Ӧ

				strSearch = manageView.getTextFieldSearch().getText();

				if ("".equals(strSearch)) {// ��ѯ�ֶ�Ϊ�գ���Ϊȫ����ѯ����һҳ��
					if (SwingUtilities.isEventDispatchThread()) {//�¼��ַ��߳�
						// ��ʾȫ����ѯ�ֶν���������ҳ
						manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
								manageModel.getUsersByAccount(strSearch));

					} else {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								// ��ʾȫ����ѯ�ֶν���������ҳ
								manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
										manageModel.getUsersByAccount(strSearch));

							}

						});
					}

				} else {// ��ѯ�ֶβ�Ϊ��

					if (SwingUtilities.isEventDispatchThread()) {
						// ��ʾȫ����ѯ�ֶν���������ҳ
						manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
								manageModel.getUsersByAccount(strSearch));
					} else {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// ��ʾȫ����ѯ�ֶν���������ҳ
								manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
										manageModel.getUsersByAccount(strSearch));
							}

						});
					}

				}
			}

			else if (actionCommand.equals("buttonTrain")) {// ѵ��ͼƬ��ť��Ӧ
				manageView.getButtonTrain().setEnabled(false);

				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);

				try {
					boolean r = result.take();
					if (r) {// ����true���ɹ����������⣬������ʾ�Ի���
						JOptionPane.showMessageDialog(manageView, "�����������ϣ�");
					} else {// ����false������������ʧ�ܣ�������ʾ�Ի���
						JOptionPane.showMessageDialog(manageView, "������Ҫ��������Ƭ��Ϣ���ܸ���������");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				manageView.getButtonTrain().setEnabled(true);
			} else if (actionCommand.equals("buttonAdd")) {// ����û���ť��Ӧ

				// ����û�
				new AddUserAction(manageView, (DefaultTableModel) manageView.getTable().getModel());
			} else if (actionCommand.equals("buttonUpdate")) {// �޸��û���Ϣ��ť��Ӧ
				if (selectedRows != null) {

					// �õ�ID�ţ�ֻȡһ��
					String id = manageView.getTable().getValueAt(selectedRows[0], 0).toString();
					// �޸��û�
					new UpdateUserAction(manageView, id, selectedRows[0], (DefaultTableModel) manageView.getTable().getModel(), 0);

					manageView.getButtonUpdate().setEnabled(false);
				}

			} else if (actionCommand.equals("buttonDelete")) {// ɾ����ť��Ӧ
				if (selectedRows != null) {// ɾ��
					for (int i = 0; i < selectedRows.length; i++) {
						String sn = manageView.getTable().getValueAt(selectedRows[i], 0).toString();
						if (sn != null && sn.trim()!="") {
							manageModel.deleteUserById(sn);
						}
					}
				}
				
				

				//������������
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);
				
				try {
					boolean r = result.take();
					if (r) {// ����true���ɹ����������⣬������ʾ�Ի���
						JOptionPane.showMessageDialog(manageView, "�����������ϣ�");
					} else {// ����false������������ʧ�ܣ�������ʾ�Ի���
						JOptionPane.showMessageDialog(manageView, "������Ҫ��������Ƭ��Ϣ���ܸ���������");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				//������ʾ�б�
				if (SwingUtilities.isEventDispatchThread()) {
					manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
							manageModel.getUsersByAccount(""));
				} else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// ��ʾȫ����ѯ�ֶν���������ҳ
							manageModel.refreshUserTable((DefaultTableModel) manageView.getTable().getModel(),
									manageModel.getUsersByAccount(""));

						}

					});
				}

				
			}
		}
	}

	/**
	 * Description :��ӦTabbedPane��ǩ�ı�
	 * 
	 * @param e
	 *            :��Ӧ�¼�
	 * @return void
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (manageView.getTabbedPane().getSelectedIndex() == 1) {// �л�����Ƶ����ʶ������

		}

	}

	/**
	 * Description :���ð�ť����ʹ��
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
