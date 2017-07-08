package com.test;

import java.util.ArrayList;
import java.util.List;
public class PermModel {

	private String permid;
	private String permname;
	private String url;
	private String parentid;
	private String description;
	private String isdisabled;
	private String beLong;

	public String getBeLong() {
		return beLong;
	}

	public void setBeLong(String beLong) {
		this.beLong = beLong;
	}
	
	public String getPermid() {
		return permid;
	}

	public void setPermid(String permid) {
		this.permid = permid;
	}

	public String getPermname() {
		return permname;
	}

	public void setPermname(String permname) {
		this.permname = permname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsdisabled() {
		return isdisabled;
	}

	public void setIsdisabled(String isdisabled) {
		this.isdisabled = isdisabled;
	}
	
	public boolean hasChildren(){
		List<PermModel> plist=new ArrayList<PermModel>();
		plist=new PermService().findSubPermsById(this.permid);
		if(plist.size()>0){
			return true;
		}
		return false;
	}
}
