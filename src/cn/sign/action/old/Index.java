/**
 * Project             :FaceRecognise project
 * Comments            :入口类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.sign.action.old;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * 程序入口类
 * 
 * @author acer
 *
 */
public class Index {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		new Login();
	}
}
