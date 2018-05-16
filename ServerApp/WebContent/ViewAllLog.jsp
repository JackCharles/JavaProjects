<%@page import="java.net.URLDecoder"%>
<%@page import="com.user.Tools"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="com.user.OptLog"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.user.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作日志</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/viewlog_css.css" rel="stylesheet" type="text/css" />
<script src="laydate/laydate.js"></script>
<script>
function checkdate(f)
{
	if(f.StartDate.value==""||f.EndDate.value=="")
	{
		alert("日期不能为空！");
		return false;
	}
	var start = f.StartDate.value.split('-');
	var end = f.EndDate.value.split('-');
	var today = new Date();
	var sd = new Date(start[0], start[1]-1, start[2]);
	var ed = new Date(end[0], end[1]-1, end[2]);
	if(sd>today||sd>=ed)
	{
		alert("日期输入不合法！");
		return false;
	}
	return confirm("确定删除"+f.StartDate.value+"至"+f.EndDate.value+"的所有日志？");
}
</script>
</head>
<body>
	<%
		User us = (User) request.getSession().getAttribute("user");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			if ((us.getRole() & 4096) != 0)
			{
				request.setCharacterEncoding("utf-8");
				response.setCharacterEncoding("utf-8");
				int pageno = Integer.valueOf(request.getParameter("page"));
				String type = URLDecoder.decode(request.getParameter("type"),"utf-8");
				ArrayList<OptLog> li = JdbcHelper.QueryAllLog(type, pageno);
				if(li==null)
					Tools.PrintErrorMsg(request,response, "查询日志失败："+JdbcHelper.ErrorMsg);
				int count = li.size();
		%>
	
	<table class="bordered">
		<caption>
			系统操作日志<%="（" + type + "）"%></caption>
		<thead>
			<tr>
				<th>操作时间</th>
				<th>操作员ID</th>
				<th>操作员姓名</th>
				<th>事件类别</th>
				<th id="details">事件详情</th>
			</tr>
		</thead>
		<%
			for (int i = 0; i < count; i++)
					{
		%>
		<tr>
			<td><%=li.get(i).datetime%></td>
			<td><%=li.get(i).userid%></td>
			<td><%=li.get(i).username%></td>
			<td><%=li.get(i).operation%></td>
			<td><%=li.get(i).details%></td>
		</tr>
		<%
			}
		%>
	</table>
	<p style="text-align: center">
		本页共<%=count%>条数据！
	</p>
	<p style="text-align: center;">
		<%
			if (pageno > 1)
			{
		%><a href="ViewAllLog.jsp?type=<%=URLDecoder.decode(type,"utf-8")%>&page=1">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="ViewAllLog.jsp?type=<%=URLDecoder.decode(type,"utf-8")%>&page=<%=pageno - 1%>">上一页</a>
		<%
			}
		%>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			if (count == 20)
			{
		%><a href="ViewAllLog.jsp?type=<%=URLDecoder.decode(type,"utf-8")%>&page=<%=pageno + 1%>">下一页</a>
		<%
			}
		%>
	</p>

	<div id="search">
		<form action="ViewAllLog.jsp" method="post">
			<table class="bordered">
				<caption>按类型查找日志</caption>
				<tr>
					<td><input type="hidden" name="page" value="1" /> <select
						name="type">
							<option value="全部事件">全部事件</option>
							<option value="上传案件">上传案件</option>
							<option value="下载案件">下载案件资料</option>
							<option value="删除案件">删除案件资料</option>
							<option value="审批案件">审批案件（添加案卷号）</option>
							<option value="添加用户">添加用户</option>
							<option value="删除用户">删除用户</option>
							<option value="修改用户">修改用户</option>
							<option value="添加子类型">添加案件子类型</option>
							<option value="删除子类型">删除案件子类型</option>
							<option value="数据库备份">数据库备份</option>
							<option value="删除日志">删除日志</option>
					</select> <input type="submit" value="查询日志" /></td>
				</tr>
			</table>
		</form>
	</div>

	<%
		if ((us.getRole() & 8192) != 0)
				{
	%>
	<div id="delete-log">
		<form action="DeleteLog" method="post" onsubmit="return checkdate(this);">
			<table class="bordered">
				<caption>删除日志</caption>
				<tr>
					<td>删除： <input id="datebox" name="StartDate"
						class="laydate-icon" onclick="laydate()" readonly="readonly"/> 至 <input id="datebox"
						name="EndDate" class="laydate-icon" onclick="laydate()" readonly="readonly"/> 的日志 <input
						id="subbt" type="submit" value="删除日志" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<%
		}
			}
			else
				request.getRequestDispatcher("PermDen.html").forward(request, response);
		}
	%>
</body>
</html>