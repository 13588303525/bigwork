/**
 * Project             :FaceRecognise project
 * Comments            :用户信息类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm
 */

package cn.sign.model.old;

import org.springframework.stereotype.Component;
@Component("user")
public class UserBean {
	private String id;
	private String pwd;
	private String name;
	private String havePhoto;
	private String picsPath;
	
	public UserBean(){
		
	}
	public UserBean(String id, String pwd, String name, String picsPath) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.picsPath = picsPath;
	}
	public UserBean(String id, String pwd, String name, String havePhoto, String picsPath) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.havePhoto = havePhoto;
		this.picsPath = picsPath;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicsPath() {
		return picsPath;
	}
	public void setPicsPath(String picsPath) {
		this.picsPath = picsPath;
	}
	public synchronized String getHavePhoto() {
		return havePhoto;
	}
	public synchronized void setHavePhoto(String havePhoto) {
		this.havePhoto = havePhoto;
	}
}
