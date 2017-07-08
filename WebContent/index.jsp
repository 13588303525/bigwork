<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
	String roleName=request.getParameter("rolename");
	String remark=request.getParameter("remark");
 %>

<html>
	<head>

		<title>权限列表</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<link href="../css/common.css" rel="stylesheet" type="text/css">
		<link href="../css/form.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="../js/JQuery zTree v3.3/css/demo.css"
			type="text/css" />
		<link rel="stylesheet"
			href="../js/JQuery zTree v3.3/css/zTreeStyle/zTreeStyle.css"
			type="text/css" />

		<script type="text/javascript"
			src="../js/JQuery zTree v3.3/js/jquery-1.4.4.min.js">
</script>
		<script type="text/javascript"
			src="../js/JQuery zTree v3.3/js/jquery.ztree.core-3.3.js">
</script>
		<script type="text/javascript"
			src="../js/JQuery zTree v3.3/js/jquery.ztree.excheck-3.3.js">
</script>
		<script type="text/javascript" src="../js/ztreeImplement.js">
</script>
<script type="text/javascript">
$(document).ready(function() {
	createZTree();
});
</script>
	</head>

	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="table-layout: fixed">
			<tr>
				<td height="30" background="../images/tab_05.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="table-layout: fixed">
						<tr>
							<td width="12" height="30">
								<img src="../images/tab_03.gif" width="12" height="30" />
							</td>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													<td width="1%">
														<img src="../images/tb.gif" width="16" height="16" />
													</td>
													<td width="99%" class="topTitle">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
							<td width="16">
								<img src="../images/tab_07.gif" width="16" height="30" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- 添加角色 -->
			<tr>
				<td>
<form name="roleForm" action="" method="post">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="table-layout: fixed">
						<caption>
							当前角色为:
							<%if(roleName!=null){%><%=roleName %><%} %>
						</caption>
						<tr>
							<td width="8" background="../images/tab_12.gif">
								&nbsp;
							</td>
							<td valign="top" align="center">
								
									<fieldset style="width: 500px;">
										<legend>
											权限设置
										</legend>
										<input type="hidden" id="roleName" name="role.rolename" value=<%=roleName %> />
										<input type="hidden" name="role.remark" value=<%=remark %> />
										
										<div style="width: 500px">
										<p>
										<input type="button" value="&nbsp;展开&nbsp;" onclick="expandAllNodes()" class="btn"/>
										<input type="button" value="&nbsp;收起&nbsp;" onclick="collapseAllNodes()" class="btn"/>
										</p>
											<ul id="zTreePerm" class="ztree"></ul>
										</div>
										
										<br/>
										<input type="button" value="&nbsp;全&nbsp;&nbsp;选&nbsp;" onclick="CheckAllNodes()" class="btn"/>
										<input type="button" value="取消全选" onclick="CancelAllNodes()" class="btn"/>
									</fieldset>

								
								<br />
								<br>
							</td>
							<td width="8" background="../images/tab_15.gif">
								&nbsp;
							</td>
						</tr>
					</table>
					</form>
				</td>
			</tr>
			<tr>
				<td height="35" background="../images/tab_19.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="12">
								<img src="../images/tab_18.gif" width="12" height="35" />
							</td>
							<td>
								&nbsp;
							</td>
							<td width="16">
								<img src="../images/tab_20.gif" width="16" height="35" />
							</td>
						</tr>
					</table>

				</td>
			</tr>


		</table>
		<script type="text/javascript">

</script>

	</body>
</html>
