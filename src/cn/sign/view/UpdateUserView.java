/**
 * Project             :FaceRecognise project
 * Comments            :修改用户信息界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.view;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;

import cn.sign.action.old.UpdateUserAction;
import cn.sign.model.old.UserBean;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateUserView extends JFrame{
	private JTextField textFieldAccount;
	private JPasswordField passwordField;
	private JTextField textFieldName;
	private JLabel labelShowResult;
	private JLabel lblelEntryPhoto;
	private JButton buttonEntryPhotos;
	public UpdateUserView(UpdateUserAction updateUserAction,UserBean user) {
		setTitle("\u4FEE\u6539\u4FE1\u606F");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel label = new JLabel("\u5B66\u53F7(*)");
		
		textFieldAccount = new JTextField();
		textFieldAccount.setColumns(20);
		textFieldAccount.setText(user.getId());
		
		JLabel label_1 = new JLabel("\u5BC6\u7801(*)");
		
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		passwordField.setText(user.getPwd());
		
		JLabel label_2 = new JLabel("\u59D3\u540D(*)");
		
		textFieldName = new JTextField();
		textFieldName.setColumns(20);
		textFieldName.setText(user.getName());
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		buttonEntryPhotos = new JButton("\u5F55\u5165\u7167\u7247");
		buttonEntryPhotos.addActionListener(updateUserAction);
		buttonEntryPhotos.setActionCommand("buttonEntryPhotos");
		
		labelShowResult = new JLabel("");
		
		JButton button_1 = new JButton("\u786E\u5B9A");
		button_1.addActionListener(updateUserAction);
		button_1.setActionCommand("buttonSure");
		
		JButton button_2 = new JButton("\u91CD\u7F6E");
		button_2.addActionListener(updateUserAction);
		button_2.setActionCommand("buttonReset");
		
		
		JButton button_3 = new JButton("\u9000\u51FA");
		button_3.addActionListener(updateUserAction);
		button_3.setActionCommand("buttonQuit");
		
		JLabel label_6 = new JLabel("\u5F55\u5165\u7167\u7247");
		
		lblelEntryPhoto = new JLabel();
		lblelEntryPhoto.setText(user.getHavePhoto());
		
		JButton button_4 = new JButton("\u6DFB\u52A0\u4EBA\u8138");
		button_4.addActionListener(updateUserAction);//绑定动作监听器
		button_4.setActionCommand("buttonAdd");//设置动作命令的名称
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(159)
									.addComponent(labelShowResult))
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(label_2)
										.addComponent(label_1)
										.addComponent(label))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(16)
											.addComponent(button_1))
										.addComponent(label_6))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblelEntryPhoto)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(25)
											.addComponent(button_2)
											.addGap(28)
											.addComponent(button_3)))
									.addGap(26)))
							.addContainerGap(42, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(buttonEntryPhotos)
							.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
							.addComponent(button_4)
							.addGap(57))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_6)
						.addComponent(lblelEntryPhoto))
					.addGap(25)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonEntryPhotos)
						.addComponent(button_4))
					.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_1)
						.addComponent(button_2)
						.addComponent(button_3))
					.addGap(37)
					.addComponent(labelShowResult)
					.addContainerGap(99, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		this.setSize(338, 334);
		this.setLocation(100,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 214);
		this.setVisible(true);
	}
	public JTextField getTextFieldAccount() {
		return textFieldAccount;
	}
	public void setTextFieldAccount(JTextField textFieldAccount) {
		this.textFieldAccount = textFieldAccount;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}
	public JTextField getTextFieldName() {
		return textFieldName;
	}
	public void setTextFieldName(JTextField textFieldName) {
		this.textFieldName = textFieldName;
	}
	public JLabel getLabelShowResult() {
		return labelShowResult;
	}
	public void setLabelShowResult(JLabel labelShowResult) {
		this.labelShowResult = labelShowResult;
	}
	public synchronized JLabel getLblelEntryPhoto() {
		return lblelEntryPhoto;
	}
	public synchronized JButton getButtonEntryPhotos() {
		return buttonEntryPhotos;
	}
}
