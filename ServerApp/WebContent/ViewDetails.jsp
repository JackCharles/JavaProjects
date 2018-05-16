<%@page import="java.net.URL"%>
<%@page import="com.user.Tools"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.jdbc.JdbcConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.User"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.user.Event"%>
<%@ page import="java.io.File"%>
<%
	User us = (User) session.getAttribute("user");
	String eid = request.getParameter("eid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>案件详情</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/viewdetails_css.css" rel="stylesheet" type="text/css" />

</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else 
		{
			@SuppressWarnings("unchecked")
			ArrayList<Event> list = (ArrayList<Event>) request.getSession().getAttribute("events");
			if (list == null)
				request.getRequestDispatcher("TimeOut.html").forward(request, response);
			else 
			{
				Event e = list.get(Integer.valueOf(eid));
				ArrayList<User> parties = JdbcHelper.GetParties(e.date, e.serial, e.handler1);
				if (parties == null) 
					Tools.PrintErrorMsg(request,response, "查询当事人失败："+JdbcHelper.ErrorMsg);
				else 
				{
	%>
	<!-- 这里展示文件详细内容及下载 -->

	<table class="bordered" align="center">
		<caption>案件详情</caption>
		<tr>
			<th>日期</th>
			<td><%=e.date%></td>
			<th>流水号</th>
			<td><%=e.serial%></td>
		</tr>
		<tr>
			<th>公证员（主）</th>
			<td><%=e.handler1%></td>
			<th>公证员（副）</th>
			<td><%=e.handler2%></td>
		</tr>
		<tr>
			<th>案件类型</th>
			<td><%=e.type + "-" + e.subtype%></td>
			<th>案卷号</th>
			<td><%=e.rno%></td>
		</tr>
		<tr>
			<th>审批情况</th>
			<td>
				<%
					if (e.confirm.equals("N")) {
				%>
				 <span style="color: red">未审批</span>
				<%
 					} else {
 				%>
 				<span>已审批</span>
 				<%
 					}
 				%>
			</td>
			<th>审批人</th>
			<td><%=e.approval%></td>
		</tr>
		<tr>
			<th>案件备注</th>
			<td colspan="3"><%=e.comment%></td>
		</tr>
		<%
			for (int i = 0; i < parties.size(); ++i) {
		%>
		<tr>
			<th>当事人<%=i + 1%>姓名</th>
			<td><%=parties.get(i).getUserName()%></td>
			<th>身份证号</th>
			<td><%=parties.get(i).getId()%></td>
		</tr>
		<%
			}
		%>
	</table>
	<br />
	<%
		if ((us.getRole() & 16) == 0) {
	%>
	<p style="color: red; font-size: 16px; text-align: center;">你没有下载资料的权限，要下载文件请联系管理员！</p>
	<%
		}
	%>
	<table class="bordered" align="center">
		<caption>案件相关资料</caption>
		<%
			File basedir = new File(e.filepath);
			File[] files = basedir.listFiles();
			if (files == null)
				Tools.PrintErrorMsg(request,response, "获取文件失败："+e.filepath);
			else
				for (int i = 0; i < files.length; ++i) 
				{
					String linkpath = files[i].getPath().replace("E:\\", "");
		%>
		<tr>
			<td><%=files[i].getName()%></td>
			<td><a href="<%=linkpath%>" target="_blank">预览</a></td>
			<%
				if ((us.getRole() & 16) != 0) {
			%>
			<td><a
				href="download.jsp?filename=<%=URLEncoder.encode(files[i].getPath(), "utf-8")%>
			&date=<%=e.date%>&serial=<%=e.serial%>&handler=<%=URLEncoder.encode(e.handler1,"utf-8")%>">点击下载</a></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<div id="attention">
		<p>1、预览功能目前只有部分文件支持，如你的浏览器不支持预览，则转入下载页面！</p>
		<p>2、目前下载模块对迅雷支持不是很好，请使用浏览器自带的下载功能下载文件！</p>
		<p>3、文件命名不标准可能会发生无法下载的情况，请右键单击[预览]选择另存为即可!</p>
	</div>
	<%
		}
	}
	%>
</body>
<%
	if ((us.getRole() & 16) == 0) {
%>
<script type="text/javascript">
	document.body.onselectstart = document.body.oncontextmenu = function() {
		return false;
	}
</script>
<%
	}}
%>

</html>