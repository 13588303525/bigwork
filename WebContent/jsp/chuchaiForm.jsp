<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/chuchaiForm.css" rel="stylesheet" type="text/css" />

<center><h2>出差单</h2></center>
<from>
<table>
  <tr>
    <td width="16%">单位（公章）</td>
    <td width="16%"><input type="text" /></td>
    <td width="16%">姓名</td>
    <td width="16%"><input type="text" /></td>
    <td width="16%">职称</td>
    <td width="20%"><input type="text" /></td>
  </tr>
  <tr>
    <td>项目经费</td>
    <td><input type="text" /></td>
    <td>出差事由</td>
    <td colspan="2"><input type="text" /></td>
    <td>附单据(
      <input style="width:20%" type="text" />
    )张</td>
  </tr>
</table>
<table>
  <tr>
    <td width="20%" colspan="2">出差地点</td>
    <td width="10%" rowspan="2"><p>起　止</p>
    <p>日　期</p></td>
    <td width="5%" rowspan="2">天数</td>
    <td width="25%" colspan="3">城市交通费</td>
    <td width="10%" rowspan="2">住宿费</td>
    <td width="20%" colspan="2">补贴报销</td>
    <td width="10%" rowspan="2">其他</td>
  </tr>
  <tr>
    <td>省内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="width:15%; vertical-align:middle" type="radio" name="RadioGroup1" value="单选" id="RadioGroup1_0" /></td>
    <td>省外&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="width:15%; vertical-align:middle" type="radio" name="RadioGroup1" value="单选" id="RadioGroup1_1" /></td>
    <td>飞机</td>
    <td>火车</td>
    <td>其他</td>
    <td>伙食补助费</td>
    <td>公杂费</td>
  </tr>
  <tr>
    <td colspan="2"><input type="text" /></td>
    <td>——</td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td colspan="2"><input type="text" /></td>
    <td>——</td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td colspan="2"><input type="text" /></td>
    <td>——</td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td colspan="3">核准报销金额</td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
    <td><input type="text" /></td>
  </tr>
  <tr>
    <td colspan="3">合计人名币（大写）</td>
    <td colspan="8"><input style="width:70%" type="text" />￥：
    <input style="width:24%" type="text" /></td>
  </tr>
</table>
<table>
  <tr>
    <td width="20%" rowspan="2">转卡信息</td>
    <td width="15%" colspan="2">工号</td>
    <td width="15%">姓名</td>
    <td width="30%" colspan="3">建行卡号</td>
    <td width="20%" colspan="2">转卡金额</td>
  </tr>
  <tr>
    <td colspan="2"><input type="text" /></td>
    <td ><input type="text" /></td>
    <td colspan="3"><input type="text" /></td>
    <td colspan="2"><input type="text" /></td>
  </tr>
  <tr>
    <td colspan="10">报销审批人：
      <input style="width:15%" type="text" />&nbsp;　　　　&nbsp;&nbsp;&nbsp;&nbsp;
      日期：
      <input style="width:5%" type="text" />
      年
      <input style="width:5%" type="text" />
      月
      <input style="width:5%" type="text" />
      日&nbsp;　　　　　　　　　　&nbsp;&nbsp;&nbsp;&nbsp;
      
      经办人：
      <input style="width:15%" type="text" />
    </td>
  </tr>
</table>
</from>
</body>
</html>
