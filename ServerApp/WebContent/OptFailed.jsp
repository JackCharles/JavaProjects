<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作失败</title>
<style>

#pd{
	margin-left:203px;
	margin-top: 150px;
	width: 580px;
}

#pd img{float: left;}

#pd #text{
	font-family: serif;
	font-size: 50px;
	padding-top: 7px;
}

#reason{
	margin-top: 30px;
	color: red;
	margin-left: 203px;
	width: 580px;
	border: 1px solid #000;
	height: 200px;
}
</style>

</head>
<body>
<%
	String errormsg = request.getParameter("errormsg");
	if(errormsg==null)
		errormsg = "发生了未知错误！";
	else
		errormsg = URLDecoder.decode(errormsg, "utf-8");
%>
	<div id="pd">
		<img src="images/error.png"/>
		<div id="text">OPERATION FAILURE!<br/>
			操作执行失败！
		</div>
	</div>
	<div id="reason">
		<p>详细内容：</p>
		<p><%=errormsg %></p>
	</div>
</body>
</html>