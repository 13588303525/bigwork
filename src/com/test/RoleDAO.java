package com.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class RoleDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.whu.idao.baseinfo.impl.IRoleDAO#executeQuery(java.lang.String,
	 * java.lang.Object[])
	 */
	private ResultSet rs;
	private PreparedStatement statement;
	private CallableStatement cstmt;
	private Connection connection;

	private static final String ARRAY_VARCHAR = "ARRAY_VARCHAR";

	public Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			connection = java.sql.DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.199:1521:orcl", "whufe",
					"whufe");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public void closeConnection(Connection conn) {

	}

	public ArrayList<RoleModel> executeQuery(String sql) {
		ArrayList<RoleModel> list = new ArrayList<RoleModel>();
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				RoleModel role = new RoleModel();
				role.setRoleid(rs.getString("roleid"));
				role.setRolename(rs.getString("rolename"));
				role.setPerms(rs.getString("perms"));
				role.setRemark(rs.getString("remark"));
				list.add(role);
			}
			rs.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<PermModel> executeQueryPerm(String sql) {

		ArrayList<PermModel> list = new ArrayList<PermModel>();
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				PermModel perm = new PermModel();
				perm.setPermid(rs.getString("permid"));
				perm.setPermname(rs.getString("permname"));
				perm.setUrl(rs.getString("url"));
				perm.setParentid(rs.getString("parentid"));
				perm.setDescription(rs.getString("description"));
				perm.setIsdisabled(rs.getString("isdisabled"));
				list.add(perm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public PermModel executeQueryPermModel(String sql) {

		PermModel perm = new PermModel();
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next()) {
				perm.setPermid(rs.getString("permid"));
				perm.setPermname(rs.getString("permname"));
				perm.setUrl(rs.getString("url"));
				perm.setParentid(rs.getString("parentid"));
				perm.setDescription(rs.getString("description"));
				perm.setIsdisabled(rs.getString("isdisabled"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return perm;
	}

	/**
	 * 获取权限树 JSON串
	 * 
	 * @param procedureName
	 *            存储过程名称
	 * @param permIds
	 *            传入存储过程的数组参数
	 * @return
	 */
	public String[] executeProcedureInitPermTree(String procName,
			String[] permIds) {
		String[] perms = null;

		connection = this.getConnection();
		// 将C3P0连接转成Oracle连接
		C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
		OracleConnection conn;
		try {
			conn = (OracleConnection) cp30NativeJdbcExtractor
					.getNativeConnection(connection);

			cstmt = conn.prepareCall(procName);
			ArrayDescriptor varchar2Desc = oracle.sql.ArrayDescriptor
					.createDescriptor(ARRAY_VARCHAR, conn);

			// 将字符串数组转换为oralce能识别的数组
			ARRAY vArray = new ARRAY(varchar2Desc, conn, permIds);
			cstmt = conn.prepareCall(procName);
			// 返回值在java中是第一个，所以先注册输出参数
			cstmt.registerOutParameter(1, OracleTypes.ARRAY, ARRAY_VARCHAR);
			cstmt.setArray(2, vArray);
			cstmt.execute();
			ARRAY array = (ARRAY) cstmt.getArray(1);
			perms = (String[]) array.getArray();
			cstmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return perms;
	}

}
