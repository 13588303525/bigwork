<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="~/Scripts/jquery-1.10.2.js"></script>
    <script src="~/Content/bootstrap/js/bootstrap.js"></script>
    <link href="~/Content/bootstrap/css/bootstrap.css" rel="stylesheet" />

    <script src="~/Scripts/jquery.ztree.all-3.5.js"></script>
    <link href="~/Content/zTree.theme.metro.css" rel="stylesheet" />
    <!--  http://www.cnblogs.com/landeanfen/p/5124913.html -->
    <script>
    var setting = {
    	    view: {
    	        addHoverDom: addHoverDom,
    	        removeHoverDom: removeHoverDom,
    	        selectedMulti: false
    	    },
    	    check: {
    	        enable: true
    	    },
    	    data: {
    	        simpleData: {
    	            enable: true
    	        }
    	    },
    	    edit: {
    	        enable: false
    	    }
    	};

    	var zNodes = [
    	    { id: 1, pId: 0, name: "父节点1", open: true },
    	    { id: 11, pId: 1, name: "父节点11" },
    	    { id: 111, pId: 11, name: "叶子节点111" },
    	    { id: 112, pId: 11, name: "叶子节点112" },
    	    { id: 113, pId: 11, name: "叶子节点113" },
    	    { id: 114, pId: 11, name: "叶子节点114" },
    	    { id: 12, pId: 1, name: "父节点12" },
    	    { id: 121, pId: 12, name: "叶子节点121" },
    	    { id: 122, pId: 12, name: "叶子节点122" },
    	    { id: 123, pId: 12, name: "叶子节点123" },
    	    { id: 124, pId: 12, name: "叶子节点124" },
    	    { id: 13, pId: 1, name: "父节点13", isParent: true },
    	    { id: 2, pId: 0, name: "父节点2" },
    	    { id: 21, pId: 2, name: "父节点21", open: true },
    	    { id: 211, pId: 21, name: "叶子节点211" },
    	    { id: 212, pId: 21, name: "叶子节点212" },
    	    { id: 213, pId: 21, name: "叶子节点213" },
    	    { id: 214, pId: 21, name: "叶子节点214" },
    	    { id: 22, pId: 2, name: "父节点22" },
    	    { id: 221, pId: 22, name: "叶子节点221" },
    	    { id: 222, pId: 22, name: "叶子节点222" },
    	    { id: 223, pId: 22, name: "叶子节点223" },
    	    { id: 224, pId: 22, name: "叶子节点224" },
    	    { id: 23, pId: 2, name: "父节点23" },
    	    { id: 231, pId: 23, name: "叶子节点231" },
    	    { id: 232, pId: 23, name: "叶子节点232" },
    	    { id: 233, pId: 23, name: "叶子节点233" },
    	    { id: 234, pId: 23, name: "叶子节点234" },
    	    { id: 3, pId: 0, name: "父节点3", isParent: true }
    	];

    	$(document).ready(function () {
    	    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    	});

    	function addHoverDom(treeId, treeNode) {
    	    var sObj = $("#" + treeNode.tId + "_span");
    	    if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
    	    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
    	        + "' title='add node' onfocus='this.blur();'></span>";
    	    sObj.after(addStr);
    	    var btn = $("#addBtn_" + treeNode.tId);
    	    if (btn) btn.bind("click", function () {
    	        var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    	        zTree.addNodes(treeNode, { id: (100 + newCount), pId: treeNode.id, name: "new node" + (newCount++) });
    	        return false;
    	    });
    	};
    	function removeHoverDom(treeId, treeNode) {
    	    $("#addBtn_" + treeNode.tId).unbind().remove();
    	};
    </script>
</head>
<body>
<div id="menuContent" class="menuContent" style="width:95%;border:1px solid rgb(170,170,170);z-index:10;">
         <ul id="treeDemo" class="ztree" style="margin-top:0; width:100%; height:auto;"></ul>
    </div>
    
    <input type="text" class="form-control" id="txt_ztree" placeholder="点击文本框出现ztree" onclick="showMenu()" />
            <div id="menuContent2" class="menuContent" style="display:none;position: absolute;width:95%;border:1px solid rgb(170,170,170);z-index:10;">
                <ul id="treeDemo2" class="ztree" style="margin-top:0; width:160px; height:auto;"></ul>
            </div>
</body>
</html>