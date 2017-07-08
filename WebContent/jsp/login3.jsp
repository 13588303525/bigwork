<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
<script src="../js/jquery.min.js"></script>
<script src="../bootstrap/js/bootstrap.min.js"></script>
</script>
<style>
body {
	margin-left: auto;
	margin-right: auto;
	margin-TOP: 100PX;
	width: 20em;
}</style>
</head>
<body>
	<div class="row">
		<div class="col-xs-12">
			<table class="table table-striped">
				<tr>
					<td><label for="stuNo">学号：</label> <input type="text"
						id="stuNo" />
						<div class="btn-group" style="width: 157px; height: 26px;">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown" style="width: 157px; height: 26px;">
								班级</button>
							<ul class="dropdown-menu" role="menu">
								<li><a href="#">一班</a></li>
								<li><a href="#">二班</a></li>
								<li><a href="#">三班</a></li>
								<li><a href="#">四班</a></li>
								<li><a href="#">五班</a></li>
							</ul>
						</div>
						<button type="button" class="btn btn-primary">查询</button>
						<button type="button" class="btn">重置</button></td>
				</tr>
			</table>
		</div>

		<div class="input-group">
			<span class="input-group-addon" id="basic-addon1">@</span> <input
				id="userName" type="text" class="form-control" placeholder="用户名"
				aria-describedby="basic-addon1" />
		</div>
		<br />
		<!--下面是密码输入框-->
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon1">@</span> <input
				id="passWord" type="text" class="form-control" placeholder="密码"
				aria-describedby="basic-addon1" />
		</div>
		<br />
		<!--下面是登陆按钮,包括颜色控制-->
		<button type="button" style="width: 280px;" class="btn btn-default">登录</button>
</body>
</html>
