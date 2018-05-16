<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>评教管理系统-系统管理页</title>
</head>

<body>
	<%UserBean us = (UserBean)session.getAttribute("user");%>
	<h1 style="color:#00BFFF;font-family:微软雅黑; font-weight: lighter; 
	font-style: italic;">—欢迎使用评教管理系统-系统管理页—</h1>
	<% if(us==null||!us.role.equals("SYSADMIN")){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{ %>
		<h2 style="font-family: 微软雅黑; font-weight: lighter;">
		<%String sex = us.sex.equals("男")?"先生":"女士";%>
		Welcome：<%=us.name+" （"+us.id+"）"+ sex%>
		</h2>
		
		<h1>---------------------------------------------------------------</h1>
		
		<h2 style="font-family: 微软雅黑; font-weight: lighter;">现在您可以：</h2>
		<div style="font-family: 微软雅黑; font-weight: lighter; font-size: 20px;">
			<p>◆ <a href="modifyuser.jsp">修改/删除 用户信息</a></p>
			<p>◆ <a href="adduser.jsp">添加 用户信息</a></p>
			<p>◆ <a href="viewcourses.jsp">修改/删除 课程信息</a></p>
			<p>◆ <a href="updatecourse.jsp?action=ADD">添加 课程信息</a></p>
			<p>◆ <a href="viewinfo.jsp">修改你的个人信息</a></p>
			<p>◆ <a href="chpasswd.jsp">修改你的登录密码</a></p>	
			<p>◆ <a href="index.jsp">退出登录</a></p>		
		</div>
	<% } %>
</body>
</html>