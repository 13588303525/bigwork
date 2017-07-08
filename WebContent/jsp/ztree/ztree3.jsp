<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
<script src="../bootstrap/js/bootstrap.min.js"></script>

<script src="../../ztree/js/jquery.ztree.all-3.5.js"></script>
<link href="../../ztree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" />

<script>
var setting = {  
        isSimpleData : true,              //数据是否采用简单 Array 格式，默认false  
        treeNodeKey : "id",               //在isSimpleData格式下，当前节点id属性  
        treeNodeParentKey : "pId",        //在isSimpleData格式下，当前节点的父节点id属性  
        showLine : true,                  //是否显示节点间的连线  
        checkable : true                  //每个节点上是否显示 CheckBox  
    };  
  
var treeNodes = [   
    {"id":1, "pId":0, "name":"test1"},   
    {"id":11, "pId":1, "name":"test11"},   
    {"id":12, "pId":1, "name":"test12"},   
    {"id":111, "pId":11, "name":"test111"},   
]; 
</script>
  
</head>
<body>

</body>
</html>