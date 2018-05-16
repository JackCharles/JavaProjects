<%@ page import="com.user.Tools"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page import="com.user.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除用户</title>
</head>
<body>
	<%
		User us = (User) session.getAttribute("user");
		String uid = request.getParameter("id");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			boolean res = JdbcHelper.DeleteUser(uid);
			if (res)
			{
				String detials = "删除的用户ID：" + uid;
				JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "删除用户", detials);
				request.getRequestDispatcher("UserManager.jsp").forward(request, response); 
			}
			else
			{
				Tools.PrintErrorMsg(request, response, "删除用户失败："+JdbcHelper.ErrorMsg);
				System.out.println(JdbcHelper.ErrorMsg);
			}
		}
	%>
</body>
</html>