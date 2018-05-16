<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="com.user.UserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除用户</title>
</head>
<body>
<%	UserBean us =(UserBean)session.getAttribute("user");
	String id = request.getParameter("id");
	String role = request.getParameter("role");
	if(id==null||role==null){%>
	<p style="font-family: 微软雅黑; color:red;">参数错误，删除失败！</p>
<%}else if(us==null||us.id.equals(id)){%>
	<p style="font-family: 微软雅黑; color:red;">你不是管理员或你无法删除你自己！</p>
<%}else{
	boolean b = JdbcHelper.DeleteUser(id, role);
	if(b){%>
	
<p style="font-family: 微软雅黑; color:blue;">删除成功，返回刷新查看！</p>
<%}else{%>

<p style="font-family: 微软雅黑; color:red;">删除失败：<%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
<%}}%>

<p><a href="modifyuser.jsp">点我返回</a></p>
</body>
</html>