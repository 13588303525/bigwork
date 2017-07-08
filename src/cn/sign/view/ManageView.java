/**
 * Project             :FaceRecognise project
 * Comments            :管理员界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.view;


import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;


import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import cn.sign.action.old.Manage;

import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ManageView extends JFrame {
	private JTextField textFieldSearch;
	private Object[][] dat = null;
	private Object[] title = new String[] { "ID", "账号", "姓名", "性别", "部门",
			"联系方式" };
	private JButton buttonUpdate;
	private JButton buttonDelete;
	private JButton buttonSeacher;
	private JButton buttonTrain;
	private JButton buttonAdd;
	private JTable table;
	private DefaultTableModel model;
	private JTabbedPane tabbedPane;
	private ButtonGroup buttonGroup;
	

	public ManageView(Manage manageAction) {

		setTitle("\u7BA1\u7406\u5458");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);

		JPanel panelManage = new JPanel();
		tabbedPane.addTab("管理", null, panelManage, null);

		textFieldSearch = new JTextField();
		textFieldSearch.setColumns(15);

		buttonSeacher = new JButton("\u67E5\u8BE2");
		buttonSeacher.addActionListener(manageAction);
		buttonSeacher.setActionCommand("buttonSeacher");

		JScrollPane scrollPane = new JScrollPane();

		buttonTrain = new JButton("\u66F4\u65B0\u4EBA\u8138\u5E93");
		buttonTrain.addActionListener(manageAction);
		buttonTrain.setActionCommand("buttonTrain");

		buttonAdd = new JButton("\u6DFB\u52A0");
		buttonAdd.addActionListener(manageAction);
		buttonAdd.setActionCommand("buttonAdd");

		buttonUpdate = new JButton("\u4FEE\u6539");
		buttonUpdate.addActionListener(manageAction);
		buttonUpdate.setActionCommand("buttonUpdate");
		buttonUpdate.setEnabled(false);

		buttonDelete = new JButton("\u5220\u9664");
		buttonDelete.addActionListener(manageAction);
		buttonDelete.setActionCommand("buttonDelete");
		buttonDelete.setEnabled(false);

		GroupLayout gl_panelManage = new GroupLayout(panelManage);
		gl_panelManage.setHorizontalGroup(
			gl_panelManage.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelManage.createSequentialGroup()
					.addGroup(gl_panelManage.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGap(79)
							.addComponent(textFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(buttonSeacher)
							.addGap(74)
							.addComponent(buttonTrain))
						.addGroup(gl_panelManage.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE))
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGap(193)
							.addComponent(buttonAdd)
							.addGap(18)
							.addComponent(buttonDelete)
							.addGap(18)
							.addComponent(buttonUpdate)))
					.addContainerGap())
		);
		gl_panelManage.setVerticalGroup(
			gl_panelManage.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelManage.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonSeacher)
						.addComponent(textFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonTrain))
					.addGap(31)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
					.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonAdd)
						.addComponent(buttonDelete)
						.addComponent(buttonUpdate))
					.addGap(23))
		);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(manageAction);

//		model = new DefaultTableModel(dat, new String[] { "ID", "\u8D26\u53F7",
//				"\u59D3\u540D", "\u6027\u522B", "\u90E8\u95E8",
//				"\u8054\u7CFB\u65B9\u5F0F" }) {
//			boolean[] columnEditables = new boolean[] { false, false, false,
//					false, false, false };
//
//			public boolean isCellEditable(int row, int column) {
//				return columnEditables[column];
//			}
//		};
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u5B66\u53F7", "\u59D3\u540D", "\u8BFE\u7A0B\u540D\u79F0", "\u5F55\u5165\u7167\u7247"
			}
		));
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		

		table.setRowHeight(20);

		scrollPane.setViewportView(table);

		panelManage.setLayout(gl_panelManage);

		tabbedPane.addChangeListener(manageAction);
		
		buttonGroup = new ButtonGroup();

		this.setSize(770, 424);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 385,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 240);
		this.setVisible(true);
	}


	public JTextField getTextFieldSearch() {
		return textFieldSearch;
	}

	public void setTextFieldSearch(JTextField textFieldSearch) {
		this.textFieldSearch = textFieldSearch;
	}

	public Object[][] getData() {
		return dat;
	}

	public void setData(Object[][] data) {
		this.dat = data;
	}

	public JButton getButtonUpdate() {
		return buttonUpdate;
	}

	public void setButtonUpdate(JButton buttonUpdate) {
		this.buttonUpdate = buttonUpdate;
	}

	public JButton getButtonDelete() {
		return buttonDelete;
	}

	public void setButtonDelete(JButton buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}


	public synchronized JButton getButtonSeacher() {
		return buttonSeacher;
	}

	public synchronized JButton getButtonTrain() {
		return buttonTrain;
	}

	public synchronized JButton getButtonAdd() {
		return buttonAdd;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
