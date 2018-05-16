<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.mysql.JdbcHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-删除模板</title>
</head>
<body>
	<%String mid = request.getParameter("mid");
	  if(mid==null){%>
	  <p>删除模板错误</p>
	  <%}else{
		 boolean flag = JdbcHelper.DeleteModel(mid);
		 if(flag){%>
		 <p>删除成功</p>
		 <%}else{ %>
		 <p><%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] %></p>
		 
	  <%}} %>
	  <a href="modifymodel.jsp">点我返回</a>
</body>
</html>