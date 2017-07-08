<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/recognizeSign.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
$(function() {
	$('#courseSelect').change(
		function(){
			var checkValue=$("#courseSelect").val();
			$.ajax({
				type : "GET",
				url : "Manage_showRecognizeSign.action",
				data : {c_id:$("#courseSelect").val(),stamp:Math.random()},
				dataType : "html",
				success : function(data) {
					$('#recognizeSignList').html(data);
				}
			});
		});
});
</script>


<div id="recTitle">
	<div id="recImg">
		<img src="../image/recognizeSign.png" height="20">
	</div>
	<div id="recName">签到打分规则</div>
</div>
<!-- ending resTitle -->

<div id="recognizeSign">
	<form method="post" id="recForm">
		<label id="courseName">选择课程:</label>
		<select id="courseSelect">
			<option value=""></option>
			<s:iterator id="course" value="courseList">
				<option value="<s:property value="#course.c_id" />"><s:property
						value="#course.c_name" /></option>
			</s:iterator>
		</select> 
		
		<input type="submit" value="签到完成">
	</form>		
</div>

<table id="recognizeSignList">

</table>