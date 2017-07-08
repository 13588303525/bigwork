package com.test;

import java.util.ArrayList;

public class PermService {
	private RoleDAO dao = new RoleDAO();
	public ArrayList<PermModel> findAllPerms() {
		String sql="select * from tbl_perm where isdisabled='0' and belong='0' order by permid";
		ArrayList<PermModel> list=dao.executeQueryPerm(sql);
		return list;
	}

	
	public PermModel findPermById(String param) {
		String sql="select * from tbl_perm where permid="+param;
		PermModel pm=dao.executeQueryPermModel(sql);
		return pm;
	}
	
	public ArrayList<PermModel> findSubPermsById(String param) {
		String sql="select * from tbl_perm where parentId="+param;
		return dao.executeQueryPerm(sql);
	}

	
}
