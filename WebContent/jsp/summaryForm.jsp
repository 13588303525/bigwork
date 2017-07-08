<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/summaryForm.css" rel="stylesheet" type="text/css" />

<center><h2>汇总单</h2></center>
<form>
<table style="border-bottom:0px">
  <tr>
    <td width="15%" height="27">单位（公章）</td>
    <td width="25%"><input type="text" /></td>
    <td width="18%">经费项目代码</td>
    <td width="25%"><input type="text" /></td>
    <td width="10%">单据张数</td>
    <td width="7%"><input type="text" /></td>
  </tr>
 </table>
 <table style="border-top:0px, border-bottom:0px" >
  <tr>
    <td width="10px">项目</td>
    <td width="16%">金额</td>
    <td width="16%">项目</td>
    <td width="16%">金额</td>
    <td width="16%">项目</td>
    <td width="20%">金额</td>
  </tr>
  <tr>
    <td>办公用品</td>
    <td><input type="text" /></td>
    <td>市内交通费</td>
    <td><input type="text" /></td>
    <td>固定资产购建费</td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td>通讯邮寄费</td>
    <td><input type="text" /></td>
    <td>燃油、燃料费</td>
    <td><input type="text" /></td>
    <td>体育用品购置费</td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td>技术服务咨询费</td>
    <td><input type="text" /></td>
    <td>过路、过桥费</td>
    <td><input type="text" /></td>
    <td>日用品、材料费</td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td>业务招待费</td>
    <td><input type="text" /></td>
    <td>机动车辆维修费</td>
    <td><input type="text" /></td>
    <td>材料测试加工费</td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td>宣传广告费</td>
    <td><input type="text" /></td>
    <td>机动车辆保险费</td>
    <td><input type="text" /></td>
    <td>材料测试加工费</td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td>图书资料版面费</td>
    <td><input type="text" /></td>
    <td>会议、会务费</td>
    <td><input type="text" /></td>
    <td>劳务费</td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td>复印、印刷费</td>
    <td><input type="text" /></td>
    <td>培训费</td>
    <td><input type="text" /></td>
    <td>其他</td>
    <td><input type="text" /></td>
  </tr>
  </table>
  <table style="border-top:0px border-bottom:0px">
   <tr >
    <td width="30%">复印、印刷费</td>
    <td width="40%" colspan="4"><input type="text" /></td>
    <td width="30%">￥：
     <input style="width:70%" type="text" /></td>
  </tr>

  <tr>
    <td rowspan="2" id="verse_card">转卡信息</td>
    <td colspan="2">工号</td>
    <td>姓名</td>
    <td>建行银行卡号</td>
    <td>转卡金额</td>
  </tr>
  <tr>
    <td height="20" colspan="2"><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
  </tr>
</table>
 <table style="border-top:0px">
  <tr>
    <td>经费主管：</td>
    <td><input type="text" /></td>
    <td>经办人：</td>
    <td><input type="text" /></td>
    <td>联系电话：</td>
    <td><input type="text" /></td>
  </tr>
</table>
</form>