<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<tr align="center" bgcolor="#999999">
	<td width="20%">学号</td>
	<td width="15%">姓名</td>
	<td colspan="<s:property value="(signInfoList.get(0).length-3)"/>">签到得分</td>
	<td width="15%">总分</td>
</tr>

 <s:iterator id="signList" value="signInfoList" > 
	<tr align="center">
	 	<s:iterator value="#signList" status="rowstatus">
			<td>
    			<s:property value="#signList[#rowstatus.index]"/>
			</td>
		</s:iterator>
	</tr>
</s:iterator>