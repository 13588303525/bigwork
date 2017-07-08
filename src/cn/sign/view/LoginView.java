/**
 * Project             :FaceRecognise project
 * Comments            :登陆界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Toolkit;


import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;

import cn.sign.action.old.Login;


public class LoginView extends JFrame{
	private JTextField textFieldAccount;
	private JPasswordField passwordField;
	private JTabbedPane tabbedPane;
	private JPasswordField passwordField_1;
	private JTextField textFieldAccount_1;
	
	public LoginView(Login loginAction) {
		
		this.addKeyListener(loginAction);
		this.setFocusable(true);
		
		setTitle("\u767B\u9646");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
	
	
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setToolTipText("");
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		
		JPanel panelPwd = new JPanel();
		tabbedPane.addTab("\u5BC6\u7801\u767B\u9646", panelPwd);
		
		JLabel label = new JLabel("\u7528\u6237\u540D");
		
		textFieldAccount = new JTextField();
		textFieldAccount.setColumns(10);
		textFieldAccount.addKeyListener(loginAction);
		textFieldAccount.setFocusable(true);
		
		JLabel label_1 = new JLabel("\u5BC6\u3000\u7801");
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(loginAction);
		passwordField.setFocusable(true);
		
		GroupLayout groupLayout = new GroupLayout(panelPwd);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(74)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addComponent(label_1))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(passwordField)
						.addComponent(textFieldAccount, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
					.addContainerGap(121, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		panelPwd.setLayout(groupLayout);
		
		JPanel panelFace = new JPanel();
		tabbedPane.addTab("\u4EBA\u8138\u8BC6\u522B\u767B\u9646",panelFace);
		
		JPanel panelPwdAndFace = new JPanel();
		tabbedPane.addTab("\u5BC6\u7801&\u4EBA\u8138", null, panelPwdAndFace, null);
		
		JLabel label_2 = new JLabel("\u7528\u6237\u540D");
		
		JLabel label_3 = new JLabel("\u5BC6\u3000\u7801");
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setFocusable(true);
		
		textFieldAccount_1 = new JTextField();
		textFieldAccount_1.setFocusable(true);
		textFieldAccount_1.setColumns(10);
		GroupLayout gl_panelPwdAndFace = new GroupLayout(panelPwdAndFace);
		gl_panelPwdAndFace.setHorizontalGroup(
			gl_panelPwdAndFace.createParallelGroup(Alignment.LEADING)
				.addGap(0, 389, Short.MAX_VALUE)
				.addGroup(gl_panelPwdAndFace.createSequentialGroup()
					.addGap(74)
					.addGroup(gl_panelPwdAndFace.createParallelGroup(Alignment.LEADING)
						.addComponent(label_2)
						.addComponent(label_3))
					.addGap(27)
					.addGroup(gl_panelPwdAndFace.createParallelGroup(Alignment.LEADING, false)
						.addComponent(passwordField_1)
						.addComponent(textFieldAccount_1, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
					.addContainerGap(123, Short.MAX_VALUE))
		);
		gl_panelPwdAndFace.setVerticalGroup(
			gl_panelPwdAndFace.createParallelGroup(Alignment.LEADING)
				.addGap(0, 160, Short.MAX_VALUE)
				.addGroup(gl_panelPwdAndFace.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panelPwdAndFace.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(textFieldAccount_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelPwdAndFace.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_3))
					.addContainerGap(79, Short.MAX_VALUE))
		);
		panelPwdAndFace.setLayout(gl_panelPwdAndFace);
		tabbedPane.addChangeListener(loginAction);
		
		JPanel panelButton = new JPanel();
		getContentPane().add(panelButton, BorderLayout.SOUTH);
		
		JButton buttonLogin = new JButton("\u767B \u9646");
		buttonLogin.addActionListener(loginAction);
		buttonLogin.setActionCommand("buttonLogin");

		
		JButton buttonCancle = new JButton("\u53D6 \u6D88");
		buttonCancle.addActionListener(loginAction);
		buttonCancle.setActionCommand("buttonCancle");
		
		GroupLayout gl_panelButton = new GroupLayout(panelButton);
		gl_panelButton.setHorizontalGroup(
			gl_panelButton.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelButton.createSequentialGroup()
					.addGap(85)
					.addComponent(buttonLogin)
					.addGap(71)
					.addComponent(buttonCancle)
					.addContainerGap(110, Short.MAX_VALUE))
		);
		gl_panelButton.setVerticalGroup(
			gl_panelButton.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelButton.createSequentialGroup()
					.addGroup(gl_panelButton.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonCancle)
						.addComponent(buttonLogin))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelButton.setLayout(gl_panelButton);
		
		JLabel lblNewLabel = new JLabel(new ImageIcon("image/banner.jpg"));
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		this.setSize(400, 250);
		this.setResizable(false);
		this.setLocation(100,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 125);
		this.setVisible(true);	
	}
	public JTextField getTextFieldAccount() {
		return textFieldAccount;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	public JTextField getTextFieldAccount_1() {
		return textFieldAccount_1;
	}
	public JPasswordField getPasswordField_1() {
		return passwordField_1;
	}
	
}
