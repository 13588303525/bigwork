package com.test;

import com.opensymphony.xwork2.ActionSupport;

public class RoleAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RoleService roleService = new RoleService();

	private RoleModel role;// 角色model

	private String roleId;// 角色id

	private String roleName;// 角色名

	private String checkStr; // 获取权限列表
	private String permString;

	public RoleModel getRole() {
		return role;
	}

	public void setRole(RoleModel role) {
		this.role = role;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermString() {
		return permString;
	}

	public void setPermString(String permString) {
		this.permString = permString;
	}

	public String getCheckStr() {
		return checkStr;
	}

	public void setCheckStr(String checkStr) {
		this.checkStr = checkStr;
	}

	/**
	 * 初始化角色的权限树
	 * @return
	 */
	public String searchPermTree() {
		
		if (roleId != null && !roleId.equals("")) {
			role = new RoleModel();
			role = roleService.getRoleById(roleId);
		}
		String[] permArray=roleService.getPermArray(roleId);
		permString="[";
		if(permArray!=null){
			System.out.println("permArray_length="+permArray.length);
			for(String singlePerm:permArray){
				if(!permString.equals("[")){
					permString+=",";
				}
				permString+=singlePerm;
			}
		}
		permString+="]";
		//System.out.println(".." + this.permString);
		return SUCCESS;

	}
}
