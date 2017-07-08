/**
 * Project             :FaceRecognise project
 * Comments            :�����û������࣬
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ���� | jxm 
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

	// �޸��û���Ϣ������
	private UpdateUserView updateUserView;
	// �޸��û���Ϣģ��
	private UpdateUserModel updateUserModel;
	// Jtableģ��
	private DefaultTableModel model;

	//
	private ManageView manageView;
	private int FACENUM = 0; //�����ɼ�����������addButton���������
	//
	private ManageModel manageModel;
	// �к�
	private int row;
	// �޸��û�
	private UserBean user;

	/**
	 * Description :���캯��
	 * 
	 * @param id
	 *            :�޸��û�ID
	 * @param row
	 *            �����û������к�
	 * @param model
	 *            ��Jtable ģ��
	 * @param mode
	 *            : �޸�ģʽ
	 * @return UpdateUserAction
	 */
	public UpdateUserAction(ManageView manageView, String id, int row, DefaultTableModel model, int mode) {
		updateUserModel = new UpdateUserModel();
		manageModel = new ManageModel();
		this.manageView = manageView;
		this.model = model;
		// ����ID�Ż���û�
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
	 * Description :�ڲ��࣬����ť��Ӧ
	 */
	class UpdateResponseThread extends Thread {
		private String actionCommand;

		public UpdateResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {

			if (actionCommand.equals("buttonEntryPhotos")) {// ¼����Ƭ��ť��Ӧ
				
				// ��ʾ��ʾ��Ϣ
				updateUserView.getLabelShowResult().setText("����¼����Ƭ��");

				// ����RecogniseService¼����Ƭ��result������Ž��
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				//addUserModel.entryPhotos(result);
				updateUserModel.entryPhotosManual(result);
				
			}else if (actionCommand.equals("buttonAdd")) {// ���������ť��Ӧ
				if(FACENUM < 6){
					System.out.println("���");
					BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
					RecogniseService.getInstance(result).manualCollectMode(); //null���������ɼ��տ�ʼ����δ�ɹ���
					
					if(RecogniseService.gotFace){//������ʱ�������һ��
						FACENUM++;
					}
				}else{
					try {
						// ������Ƭ��ʶ��Ϊtrue
						// ��ʾ��ʾ��Ϣ
						updateUserView.getLblelEntryPhoto().setText("��");
						updateUserView.getLabelShowResult().setText("������");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				}
			} else if (actionCommand.equals("buttonSure")) {// ȷ����ť��Ӧ
				user.setId(updateUserView.getTextFieldAccount().getText());
				user.setPwd(new String(updateUserView.getPasswordField()
						.getPassword()));
				user.setName(updateUserView.getTextFieldName().getText());
				user.setHavePhoto(updateUserView.getLblelEntryPhoto().getText());
				user.setPicsPath("pics/");

				updateUserModel.updateUser(user);

				updateUserView.getLabelShowResult().setText("�ɹ�!");
				
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

				
				
				
				//������������
				
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);

				try {
					boolean r = result.take();
					if (r) {// ����true���ɹ����������⣬������ʾ�Ի���
						JOptionPane.showMessageDialog(updateUserView, "�����������ϣ�");
					} else {// ����false������������ʧ�ܣ�������ʾ�Ի���
						JOptionPane.showMessageDialog(updateUserView, "������Ҫ��������Ƭ��Ϣ���ܸ���������");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
			} else if (actionCommand.equals("buttonReset")) {// ���ð�ť��Ӧ
				reset();
			} else if (actionCommand.equals("buttonQuit")) {// �˳���ť��Ӧ
				// ��������RecogniseService��������״̬���ǵȴ�״̬����ô�ͽ�������Ϊ�ȴ�״̬
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
