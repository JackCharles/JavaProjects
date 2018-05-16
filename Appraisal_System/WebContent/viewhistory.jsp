<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-查看历史评教</title>

<style type="text/css">
div{font-family:微软雅黑; 
	font-weight: lighter;
	text-align:center;}
</style>
<script type="text/javascript">
function goback()
{
	location="student.jsp";
}	
</script>
</head>

<body>
<div>
	<%UserBean us = (UserBean)session.getAttribute("user");%>
	<h1 style="color:blue;">—查看历史评教—</h1>
	<% if(us==null){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{
		List<UserBean> tlist = JdbcHelper.QueryAssessedTeacher(us.id);
		if(tlist==null){%>
		<p style="color:red">读取数据库出错:<%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
		<input style="width:200px" type="button" value="返回" onclick="goback()"/>
	<% }else if(tlist.size()==0){%>
		<p>你当前暂未进行过评教！</p>
		<input style="width:200px" type="button" value="返回" onclick="goback()"/>
	<%}else{
		request.getSession().setAttribute("tlist", tlist);
		for(int i=0;i<tlist.size();i++){%>
			<h3>点击查看关于<a href="viewassessinfo.jsp?tid=<%=i%>">
			《<%=tlist.get(i).courseName%>》<%=tlist.get(i).name%></a> 老师的评教信息</h3>
		<%} %>
		<input style="width:200px" type="button" value="返回" onclick="goback()"/>
	<%}}%>
</div>
</body>
</html>