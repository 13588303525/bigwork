<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生签到管理系统</title>
<link href="../css/manage.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/jquery.js"></script>
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
<script src="../bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		$('#signQuery').click(function() {
			$.ajax({
				type : "GET",
				url : "Manage_getSignInfoView.action",
				data : {
					t_id : <s:property value="teacher.t_id" />,
					stamp : Math.random()
				},
				dataType : "html",
				success : function(data) {
					$('.right_body').html(data);
				}
			});
		});
	});
	$(function() {
		$('#recognizeSign').click(function() {
			$.ajax({
				type : "GET",
				url : "Manage_getRecognizeSignView.action",
				data : {
					t_id : <s:property value="teacher.t_id" />,
					stamp : Math.random()
				},
				dataType : "html",
				success : function(data) {
					$('.right_body').html(data);
				}
			});
		});
	});

	$(function() {
		$('#chuchai').click(function() {
			$.ajax({
				type : "GET",
				url : "Manage_getChuchaiForm.action",
				data : {
					stamp : Math.random()
				},
				dataType : "html",
				success : function(data) {
					$('#div_from').html(data);
				}
			});
		});
	});

	$(function() {
		$('#summary').click(function() {
			$.ajax({
				type : "GET",
				url : "Manage_getSummaryForm.action",
				data : {
					stamp : Math.random()
				},
				dataType : "html",
				success : function(data) {
					$('#div_from').html(data);
				}
			});
		});
	});
</script>
</head>
<body>
		<div class="left_body">
			<!-- <div>
				<div class="stuManage">学生信息管理</div>
				<div class="function">
					<a href="#" id="chuchai">采集人脸</a>
				</div>
				<div class="function">
					<a href="#" id="signQuery">签到信息查询</a>
				</div>
			</div>

			<div>
				<div class="stuManage">学生签到管理</div>
				<div class="function">
					<a href="#" id="recognizeSign">识别签到</a>
				</div>
				<div class="function">
					<a href="#" id="summary">设置评分规则</a>
				</div>
				<div class="function">
					<a href="#">输出签到信息</a>
				</div>
			</div> -->
			<a href="#" class="list-group-item active" style="text-align: center">报销管理
			</a> <a href="#" class="list-group-item" id="">权限管理</a>

			<!-- 申报单据 -->
			<div class="btn-group" style="width: 100%">
				<button type="button"
					class="btn btn-default dropdown-toggle list-group-item"
					data-toggle="dropdown" style="width: 100%">
					申报单据<span class="caret" style="float: right"></span>
				</button>
				<ul class="dropdown-menu" role="menu" style="width: 100%">
					<li><a href="#" data-toggle="modal" data-target="#myModal"
						id="chuchai">出差单</a></li>
					<li class="divider"></li>
					<li><a href="#" data-toggle="modal" data-target="#myModal"
						id="summary">汇总单</a></li>
					<li class="divider"></li>
					<li><a href="#" data-toggle="modal" data-target="#myModal">经费额度</a></li>
				</ul>

				<!-- 模态框（Modal） -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog" style="width: 60%">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">×</button>
								<div id="div_from"></div>
								<br />
								<br />
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">关闭</button>
								<button type="button" class="btn btn-primary">提交更改</button>
							</div>
						</div>
						<!-- /.modal-content -->
					</div>
					<!-- /.modal-dialog -->
				</div>
				<!-- /.modal -->
			</div>

			<!-- 审核单据 -->
			<div class="btn-group" style="width: 100%">
				<button type="button"
					class="btn btn-default dropdown-toggle list-group-item"
					data-toggle="dropdown" style="width: 100%">
					审核单据<span class="caret" style="float: right"></span>
				</button>
				<ul class="dropdown-menu" role="menu" style="width: 100%">
					<li><a href="#" id="chuchai">出差单</a></li>
					<li class="divider"></li>
					<li><a href="#" id="summary">汇总单</a></li>
					<li class="divider"></li>
					<li><a href="#">经费额度</a></li>
				</ul>
			</div>

		</div>

		<!-- ending left_body -->

		<div class="right_body"></div>
		<!-- ending right_body -->

</body>
</html>