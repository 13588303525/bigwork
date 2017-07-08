<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录界面</title>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
<script src="../bootstrap/js/bootstrap.min.js"></script>
</head>

<body>
	<div id="login" class="container" style="width:15%;position: absolute;left:40%;top:20%">
		<form action="Login.action" method="post">
			<label>用户名：</label>
			<input type="text" class="form-control" placeholder="请输入手机号/邮箱" name="teacher.t_id" /> <br/>
			<label>密 码：</label>
			<input type="text" class="form-control" placeholder="请输入密码" name="teacher.pwd" /><br/>
			<button type="submit" class="btn btn-success">登录</button>
			<button type="button" class="btn btn-info" style="float:right">注册</button>
		</form>
	</div>
</body>
</html>