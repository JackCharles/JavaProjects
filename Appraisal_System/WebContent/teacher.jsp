<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.user.UserBean"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>评教管理系统-教师管理页</title>
</head>

<body>
	<%UserBean us = (UserBean)session.getAttribute("user");%>
	<h1 style="color:#00BFFF;font-family:微软雅黑; font-weight: lighter; 
	font-style: italic;">—欢迎使用评教管理系统-教师页—</h1>
	<% if(us==null||!us.role.equals("TEACHER")){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{ %>
		<h2 style="font-family: 微软雅黑; font-weight: lighter;">
		
		<%String sex = us.sex.equals("男")?"先生":"女士";%>

		Welcome：
		<%if(us.courseId!=null && us.courseId.equals("9999")){ %>
		辅导员<%}%>
		<%=us.name+" （"+us.id+"）"+ sex%>
		</h2>
		<%if(us.courseId==null){%>
		<h3 style="color:red">您所授课程无效，请及时联系管理员！！！</h3>
		<%} %>
		<h1>---------------------------------------------------------------</h1>
		
		<h2 style="font-family: 微软雅黑; font-weight: lighter;">现在您可以：</h2>
		<div style="font-family: 微软雅黑; font-weight: lighter; font-size: 20px;">
			<p>◆ <a href="viewpersonres.jsp">察看学期考评成绩</a></p>
			<p>◆ <a href="viewinfo.jsp">察看维护个人信息</a></p>
			<p>◆ <a href="chpasswd.jsp">修改您的登录密码</a></p>	
			<p>◆ <a href="index.jsp">退出登录</a></p>		
		</div>
	<% } %>
</body>
</html>