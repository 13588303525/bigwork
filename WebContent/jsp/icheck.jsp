<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>icheck</title>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
<script src="../bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
 //为节点添加展开，关闭的操作
$(function(){
    $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
    $('.tree li.parent_li > span').on('click', function (e) {
        var children = $(this).parent('li.parent_li').find(' > ul > li');
        if (children.is(":visible")) {
            children.hide('fast');
            $(this).attr('title', 'Expand this branch').find(' > i').addClass('icon-plus-sign').removeClass('icon-minus-sign');
        } else {
            children.show('fast');
            $(this).attr('title', 'Collapse this branch').find(' > i').addClass('icon-minus-sign').removeClass('icon-plus-sign');
        }
        e.stopPropagation();
    });
});
</script>
<style>
.tree {
    min-height:20px;
    padding:19px;
    margin-bottom:20px;
    background-color:#fbfbfb;
    border:1px solid #999;
    -webkit-border-radius:4px;
    -moz-border-radius:4px;
    border-radius:4px;
    -webkit-box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05);
    -moz-box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05);
    box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05)
}
.tree li {
    list-style-type:none;
    margin:0;
    padding:10px 5px 0 5px;
    position:relative
}
.tree li::before, .tree li::after {
    content:'';
    left:-20px;
    position:absolute;
    right:auto
}
.tree li::before {
    border-left:1px solid #999;
    bottom:50px;
    height:100%;
    top:0;
    width:1px
}
.tree li::after {
    border-top:1px solid #999;
    height:20px;
    top:25px;
    width:25px
}
.tree li span {
    -moz-border-radius:5px;
    -webkit-border-radius:5px;
    border:1px solid #999;
    border-radius:5px;
    display:inline-block;
    padding:3px 8px;
    text-decoration:none
}
.tree li.parent_li>span {
    cursor:pointer
}
.tree>ul>li::before, .tree>ul>li::after {
    border:0
}
.tree li:last-child::before {
    height:30px
}
.tree li.parent_li>span:hover, .tree li.parent_li>span:hover+ul li span {
    background:#eee;
    border:1px solid #94a0b4;
    color:#000
}
</style>
</head>
<body>
<div class="tree well">
  <ul>
    <li> <span><i class="icon-folder-open"></i> 廊坊师范学院</span>
      <ul>
        <li> <span><i class="icon-minus-sign"></i> 教育学院</span>
          <ul>
            <li> <span><i class="icon-leaf"></i> 学前教育</span> </li>
          </ul>
        </li>
        <li> <span><i class="icon-minus-sign"></i> 物电学院</span>
          <ul>
            <li> <span><i class="icon-leaf"></i> 电气工程技术</span> </li>
            <li> <span><i class="icon-minus-sign"></i> 电子信息科学技术</span>
              <ul>
                <li> <span><i class="icon-minus-sign"></i> 电子一班</span>
                  <ul>
                    <li> <span><i class="icon-leaf"></i> 宋笑</span> </li>
                    <li> <span><i class="icon-leaf"></i> 二盟</span> </li>
                  </ul>
                </li>
                <li> <span><i class="icon-leaf"></i> 电子二班</span> </li>
              </ul>
            </li>
            <li> <span><i class="icon-leaf"></i> 物理学</span> </li>
          </ul>
        </li>
      </ul>
    </li>
  </ul>
</div>
</body>
</html>