/**
 * Project             :FaceRecognise project
 * Comments            :添加用户界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.view;


import java.awt.Toolkit;

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

import cn.sign.action.old.AddUserAction;

import javax.swing.JRadioButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AddUserView extends JFrame{
	private JTextField textFieldAccount;
	private JPasswordField passwordField;
	private JTextField textFieldName;
	private JLabel labelShowResult;
	private JLabel labelHavePhoto;
	public AddUserView(AddUserAction addUserAction) {
		setTitle("\u6DFB\u52A0\u7528\u6237");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		JLabel label = new JLabel("\u5B66\u53F7(*)");
		
		textFieldAccount = new JTextField();
		textFieldAccount.setColumns(20);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801(*)");
		
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		
		JLabel label_2 = new JLabel("\u59D3\u540D(*)");
		
		textFieldName = new JTextField();
		textFieldName.setColumns(20);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JButton button = new JButton("\u5F55\u5165\u7167\u7247");
		button.addActionListener(addUserAction);
		button.setActionCommand("buttonEntryPhotos");
		
		labelShowResult = new JLabel("");
		
		JButton button_1 = new JButton("\u786E\u5B9A");
		button_1.addActionListener(addUserAction);
		button_1.setActionCommand("buttonSure");
		
		JButton button_2 = new JButton("\u91CD\u7F6E");
		button_2.addActionListener(addUserAction);
		button_2.setActionCommand("buttonReset");
		
		
		JButton button_3 = new JButton("\u9000\u51FA");
		button_3.addActionListener(addUserAction);
		button_3.setActionCommand("buttonQuit");
		
		JLabel label_6 = new JLabel("\u5F55\u5165\u7167\u7247");
		
		labelHavePhoto = new JLabel();
		labelHavePhoto.setText("否");
		
		JButton button_4 = new JButton("\u6DFB\u52A0\u7167\u7247");
		button_4.addActionListener(addUserAction);
		button_4.setActionCommand("buttonAdd");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(16)
							.addComponent(button_1)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(labelHavePhoto))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(26)
									.addComponent(button_2)
									.addGap(29)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(button_4)
										.addComponent(button_3))))
							.addContainerGap(42, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(label)
										.addComponent(label_1)
										.addComponent(label_2))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(158)
									.addComponent(labelShowResult))
								.addComponent(label_6))
							.addContainerGap(136, Short.MAX_VALUE))
						.addComponent(button)))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_1)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_2)
								.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(24)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_6)
								.addComponent(labelHavePhoto))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(button)
							.addGap(33))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(button_4)
							.addGap(18)))
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_1)
						.addComponent(button_2)
						.addComponent(button_3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelShowResult)
					.addGap(28))
		);
		panel.setLayout(gl_panel);
		
		//this.setSize(301, 327);
		this.setSize(334, 290);
		this.setLocation(100,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 197);
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
	public synchronized JLabel getLabelHavePhoto() {
		return labelHavePhoto;
	}
}
