<%@page import="com.user.User"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关于系统</title>

<style type="text/css">
#title{
	font-size: 24px;
	color:blue;
	text-align: center;
}

#content{
	font-size: 16px;
}

</style>

</head>
<body>
<%
	User us = (User)request.getSession().getAttribute("user");
	if(us==null)
		request.getRequestDispatcher("TimeOut.html").forward(request, response); 
	else
	{
%>
	<div id="title">系统使用说明</div>
	<div id="content">
<%
	String filepath = "E:/HelpInfo/help.txt";
	File file = new File(filepath);
	if(!file.exists())
	{
%>
	<p style="color:red;">相关文件不存在，请将写好的文档按此路径放置：<%=filepath %></p>
<%  }
	else
	{
        FileInputStream fis=new FileInputStream(file);
        InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line=br.readLine())!=null)
        {
%>
	<p><%=line %></p>
<%
        }
        br.close();
        isr.close();
        fis.close();
    }
	}
%>
	</div>
</body>
</html>