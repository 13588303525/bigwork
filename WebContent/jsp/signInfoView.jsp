<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/signInfo.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
$(function() {
	$('#courseSelect').change(
		function(){
			var checkValue=$("#courseSelect").val();
			$.ajax({
				type : "GET",
				url : "Manage_showsSignInfo.action",
				data : {c_id:$("#courseSelect").val(),stamp:Math.random()},
				dataType : "html",
				success : function(data) {
					$('#signInfoList').html(data);
				}
			});
		});
});
</script>

<div id="resTitle">
	<div id="resImg">
		<img src="../image/research.png" height="20">
	</div>
	<div id="resName">签到信息查询</div>
</div>
<!-- ending resTitle -->

<div id="signResearch">
	<form method="post" id="resForm">
		<label id="courseName">课程名称:</label> 
		<select id="courseSelect">
			<option value="">请选择</option>
			<s:iterator id="course" value="courseList">
				<option value="<s:property value="#course.c_id" />"><s:property
						value="#course.c_name" /></option>
			</s:iterator>
		</select> 
		
		<label id="sclass">学生班级:</label> 
		<select id="classSelect">
			<option value="">全部</option>
			<!-- <option value="className_XXXX">classNameXXXX</option> -->
		</select> 
		
		<label id="sno">学号:</label> 
		<input type="text" width="120">
		<input type="submit" value="查询">
	</form>
</div>
<!-- ending signForm -->

<table id="signInfoList">

</table>