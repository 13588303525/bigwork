/**
 * Project             :FaceRecognise project
 * Comments            :�����û������࣬
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ���� | jxm 
 * 2 | 2013-4-30 |���ӡ��Ƿ�����Ƭ����һ����ж� |jxm  
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
	
	//����û�����
	private AddUserView addUserView;
	//����û�ģ��
	private AddUserModel addUserModel;
	//JTableģ��
	private DefaultTableModel model;
	
	private int FACENUM = 0; //�����ɼ�����������addButton���������
	//
	private ManageModel manageModel;
	private ManageView manageView;
	//�Ƿ���¼����Ƭ��ʶ
	private boolean isEntryPhotos = false;

	/**
	 * Description :���캯��
	 * 
	 * @param model
	 *            :Jtableģ��
	 * @param page
	 *            ����ǰ������ҳ
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
	 * Description :�����û�����İ�ť��Ӧ����
	 * 
	 * @param e
	 *            :��Ӧ�¼�
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		new AddUserResponseThread(e.getActionCommand()).start();
	}

	/**
	 * Description :�������û��������д��Ϣ����ʾ��Ϣ���
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
	 * Description :�ڲ��࣬����ť��Ӧ
	 */
	class AddUserResponseThread extends Thread {
		private String actionCommand;
		public AddUserResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {
			if (actionCommand.equals("buttonEntryPhotos")) {// ¼����Ƭ��ť��Ӧ

				// ��ʾ��ʾ��Ϣ
				addUserView.getLabelShowResult().setText("����¼����Ƭ��");

				// ����RecogniseService¼����Ƭ��result������Ž��
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				//addUserModel.entryPhotos(result);
				addUserModel.entryPhotosManual(result);

			} else if (actionCommand.equals("buttonAdd")) {//�����Ƭ��ť��Ӧ
				if(FACENUM < 6){
					System.out.println("���");
					BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
					RecogniseService.getInstance(result).manualCollectMode(); //null���������ɼ��տ�ʼ����δ�ɹ���
					
					if(RecogniseService.gotFace){//������ʱ�������һ��
						FACENUM++;
					}
					if(FACENUM==5){
						isEntryPhotos = true;
					}
				}else{
					try {
						// ������Ƭ��ʶ��Ϊtrue

						// ��ʾ��ʾ��Ϣ
						addUserView.getLabelHavePhoto().setText("��");
						addUserView.getLabelShowResult().setText("������");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				}
			}else if (actionCommand.equals("buttonSure")) {// ȷ����ť��Ӧ

				FACENUM=0;
				/*
				 * ��ȡ������û���Ϣ
				 */
				UserBean user = new UserBean();
				
				if("".equals(addUserView.getTextFieldAccount().getText())){
					JOptionPane.showMessageDialog(addUserView, "�û�������Ϊ��");	
				}else{
					user.setId(addUserView.getTextFieldAccount().getText()); //����˺�
					
					String pwd = new String(addUserView.getPasswordField()
							.getPassword());
					if("".equals(pwd)){
						JOptionPane.showMessageDialog(addUserView, "���벻��Ϊ��");	
					}else{
						user.setPwd(pwd);//�������
						
						if("".equals(addUserView.getTextFieldName().getText())){
							user.setName("δ��д");
						}else{
							user.setName(addUserView.getTextFieldName().getText());	
						}
						
						if (isEntryPhotos) {
							user.setHavePhoto("��");
							isEntryPhotos = false;
						} else {
							user.setHavePhoto("��");
						}
						
						user.setPicsPath("pics/");

						// ����û���Ϣ���������ݴ洢���洢�����Ϣ�����ļ��洢���洢��Ƭ��Ϣ��
						addUserModel.addUser(user);
						
						//������������
						if(user.getHavePhoto().equals("��")){

							BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
							manageModel.train(result);

							try {
								boolean r = result.take();
								if (r) {// ����true���ɹ����������⣬������ʾ�Ի���
									JOptionPane.showMessageDialog(addUserView, "�����������ϣ�");
								} else {// ����false������������ʧ�ܣ�������ʾ�Ի���
									JOptionPane.showMessageDialog(addUserView, "������Ҫ��������Ƭ��Ϣ���ܸ���������");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							//
						}
						
						// ������ʾ��Ϣ
						addUserView.getLabelShowResult().setText("�ɹ���");
						
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
			} else if (actionCommand.equals("buttonReset")) {// ���ð�ť��Ӧ
				// ����
				reset();
			} else if (actionCommand.equals("buttonQuit")) {// �˳���ť��Ӧ
				if (RecogniseService.getInstance() != null
						&& RecogniseService.getInstance().getState() != State.WAITING) {
					// ��������RecogniseService��������״̬���ǵȴ�״̬����ô�ͽ�������Ϊ�ȴ�״̬
					RecogniseService.getInstance(null).waitMode();
					
					// ����RecogniseService�߳�
					//RecogniseService.getInstance().setFlag(false);
					// �ͷ�����ͷcapture��Դ
					//RecogniseService.getInstance().releaseCapture();
				}

				// ����û�������ʧ
				addUserView.dispose();
			} 
			
			

		}
	}
}
