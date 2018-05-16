<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作成功</title>
<style>

#pd{
	margin-left:203px;
	margin-top: 200px;
	width: 580px;
}

#pd img{float: left;}

#pd #text{
	font-family: serif;
	font-size: 50px;
	padding-top: 7px;
}
</style>

</head>
<body>
<%
	String redirect = request.getParameter("redirect");	
	String target = request.getParameter("target");
	if(redirect==null)
	{
		redirect = "MainPage.jsp";
		target = "_top";
	}
	else
		redirect = URLDecoder.decode(redirect, "utf-8");
%>
	<div id="pd">
		<img src="images/success.png"/>
		<div id="text">OPERATION SUCCESS!<br/>
			操作成功!<a href="<%=redirect %>" target="<%=target%>">点此返回</a>
		</div>
	</div>
</body>
</html>