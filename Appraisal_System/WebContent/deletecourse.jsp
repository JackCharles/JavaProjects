<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="com.user.UserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除课程</title>
</head>
<body>
		<%	UserBean us =(UserBean)session.getAttribute("user");
			String id = request.getParameter("id");
			if(id==null||id.equals("9999")){%>
			<p style="font-family: 微软雅黑; color:red;">参数错误，删除失败！</p>
		<%}else if(us==null||!us.role.equals("SYSADMIN")){%>
			<p style="font-family: 微软雅黑; color:red;">你不是管理员或登录超时！</p>
		<%}else{
			boolean b = JdbcHelper.DeleteCourse(id);
			if(b){%>
			
		<p style="color:blue;">删除成功，返回刷新查看！</p>
		<p style="color:red;">警告：删除课程后可能会有教师对应无效课程，请及时更改!</p>
		<%}else{%>
		
		<p style="color:red;">删除失败：<%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
		<%}}%>

		<p><a href="viewcourses.jsp">点我返回</a></p>
</body>
</html>