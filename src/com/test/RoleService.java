package com.test;

import java.util.ArrayList;

public class RoleService{

	private RoleDAO roleDAO = new RoleDAO();

	/*
	 * 查询角色
	 */
	public RoleModel getRoleById(String roleid) {
		String sql = "select * from ts_role where roleid=" + roleid;
		RoleModel roleModel = new RoleModel();
		ArrayList<RoleModel> list = roleDAO.executeQuery(sql);
		if (list.size() > 0) {
			roleModel = list.get(0);
		}
		return roleModel;
	}

	/**
	 * 根据角色的权限 初始化权限树 取得权限JSON数组
	 */
	public String[] getPermArray(String roleId) {
		String perms = this.getRoleById(roleId).getPerms();
		String[] permIds = null;
		if (perms != null)
			permIds = perms.split(",");
		if(permIds!=null)
			System.out.println("permIds_length=" + permIds.length);
		String procedureName = "{ call INIT_PERM_TREE(?,?) }";
		return roleDAO.executeProcedureInitPermTree(procedureName, permIds);
	}

}
