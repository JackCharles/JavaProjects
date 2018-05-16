<%@page import="java.text.DecimalFormat"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="com.user.FileInfo"%>
<%@page import="com.jdbc.JdbcConfig"%>
<%@page import="java.io.File"%>
<%@ page import="com.user.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	DecimalFormat f = new DecimalFormat("#.00");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源统计</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#person th{
	width: 200px;
}
#person table{
	width: 500px;
}
</style>
</head>
<body>
	<%
		User us = (User) session.getAttribute("user");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			if ((us.getRole() & 32) != 0)
			{//全部资料
				File file = new File(JdbcConfig.BASEDIR);
				long totalSize = FileInfo.getFileSize(file);
				File[] UserDir = file.listFiles();
				if (UserDir != null)
				{
	%><!-- 有用户目录存在 -->
	<table class="bordered">
		<caption>资源统计</caption>
		<thead>
			<tr>
				<th>用户名</th>
				<th>上传文件数</th>
				<th>占用空间</th>
				<th>上传总量占比(%)</th>
			</tr>
		</thead>
		<%
				for (int i = 0; i < UserDir.length; ++i)
				{
					String tpath = UserDir[i].getPath();
					long tsize = FileInfo.getFileSize(UserDir[i]);
		%>
		<tr>
			<td><%=tpath.substring(tpath.lastIndexOf(File.separatorChar) + 1)%></td>
			<td><%=FileInfo.getFileCount(UserDir[i])%></td>
			<td><%=FileInfo.FormetFileSize(tsize)%></td>
			<td><%="" + f.format((totalSize == 0 ? 0 : (double) tsize / totalSize) * 100) + "%"%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
				else
				{
	%>
	<p style="color: red">没有用户存在，或目录设置错误，请联系管理人员！</p>
	<%
		}
	%>
	<%
		}
			else
			{//统计个人数据
				File pfile = new File(JdbcHelper.BASEDIR + us.getUserName());
				long total = FileInfo.getFileSize(new File(JdbcHelper.BASEDIR));
				long size = FileInfo.getFileSize(pfile);
				long count = FileInfo.getFileCount(pfile);
	%>
	<p style="color: red; font-size: 16px; text-align: center;">你没有查看全部统计信息的权限，该部分仅显示你自己的信息！</p>
	<div id="person">
		<table class="bordered" align="center">
		<caption>个人数据统计</caption>
		<tr>
			<th>上传文件数</th>
			<td><%=count%></td>
		</tr>
		<tr>
			<th>上传文件总大小</th>
			<td><%=FileInfo.FormetFileSize(size)%></td>
		</tr>
		<tr>
			<th>传文件总占比</th>
			<td><%="" + f.format((total == 0 ? 0 : (double) size / total) * 100) + "%"%></td>
		</tr>
		</table>
	</div>
	<%
		}
		}
	%>
</body>
</html>