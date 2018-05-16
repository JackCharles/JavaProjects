<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-管理用户页</title>

<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-style: solid;
	 border-width: 1px;
	 border-color: green;}
td{background-color: #FFEFD5;}	
#empty{background-color: #FFFFFF;}
</style>

<script type="text/javascript">
function check(form)
{
	if(!form.id.value.match(/^\d{4}$/))
	{
		document.getElementById("error").innerHTML = "账号为4位学号或工号";
		return false;
	}
	return true;
}

function warning()
{
	return confirm("你确定要删除该用户？\nYou'd better know what\nyou are doing now!");
}

</script>

</head>

<body>
<div>
	<h1>用户管理</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("SYSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{ %>
	<form action="SearchUser" method="post" onsubmit="return check(this)">
	输入学号/工号进行搜索：
		<input type="text" name="id"/>
		<input type="submit" value="搜索"/>
		<p id="error" style="color:red"></p>
	</form>
	
	<%	List<UserBean> stulist = JdbcHelper.QueryAllUser("STUDENT");
		List<UserBean> tealist = JdbcHelper.QueryAllUser("TEACHER");
		List<UserBean> asslist = JdbcHelper.QueryAllUser("ASSADMIN");
		List<UserBean> syslist = JdbcHelper.QueryAllUser("SYSADMIN");
		if(stulist==null||stulist==null||stulist==null||stulist==null)
		{%>
			<p>用户信息获取失败：</p>
			<p style="color:red;"><%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
		<%}else{%>
		<!-- 分四列 -->
		<table align="center">
			<tr>
				<td colspan="4">学生</td>
				<td colspan="4">教师</td>
				<td colspan="4">评教管理员</td>
				<td colspan="4">系统管理员</td>
			</tr>
			<tr>
				<td>姓名</td>
				<td>学号</td>
				<td colspan="2">管理</td>
				<td>姓名</td>
				<td>工号</td>
				<td colspan="2">管理</td>
				<td>姓名</td>
				<td>工号</td>
				<td colspan="2">管理</td>
				<td>姓名</td>
				<td>工号</td>
				<td colspan="2">管理</td>
			</tr>
			<% 
				int i=0,j=0,k=0,l=0,t=0;
				int stulen = stulist.size();
				int tealen = tealist.size();
				int asslen = asslist.size();
				int syslen = syslist.size();
				int m = Math.max(Math.max(stulen,tealen),Math.max(asslen, syslen));
				while(t<m){%>
				<tr>
					<%if(i<stulen){%>
						<td><%=stulist.get(i).name%></td>
						<td><%=stulist.get(i).id  %></td>
						<td><a href="deleteuser.jsp?id=<%=stulist.get(i).id%>&role=<%=stulist.get(i).role%>" onclick="return warning()">删除</a></td>
						<td><a href="sysmodifyinfo.jsp?id=<%=stulist.get(i).id%>
						&role=<%=stulist.get(i).role%>">修改</a></td>
						<%i++;}
					else{%><td id="empty" colspan="4"></td><%}
					if(j<tealen){%>
						<td><%=tealist.get(j).name%></td>
						<td><%=tealist.get(j).id  %></td>
						<td><a href="deleteuser.jsp?id=<%=tealist.get(j).id%>&role=<%=tealist.get(j).role%>" onclick="return warning()">删除</a></td>
						<td><a href="sysmodifyinfo.jsp?id=<%=tealist.get(j).id%>&role=<%=tealist.get(j).role%>">修改</a></td>
						<%j++;}
					else{%><td id="empty" colspan="4"></td><%}
					if(k<asslen){%>
						<td><%=asslist.get(k).name%></td>
						<td><%=asslist.get(k).id  %></td>
						<td><a href="deleteuser.jsp?id=<%=asslist.get(k).id%>&role=<%=asslist.get(k).role%>" onclick="return warning()">删除</a></td>
						<td><a href="sysmodifyinfo.jsp?id=<%=asslist.get(k).id%>&role=<%=asslist.get(k).role%>">修改</a></td>						
						<%k++;}
					else{%><td id="empty" colspan="4"></td><%}
					if(l<syslen){%>
						<td><%=syslist.get(l).name%></td>
						<td><%=syslist.get(l).id  %></td>
						<td><a href="deleteuser.jsp?id=<%=syslist.get(l).id%>&role=<%=syslist.get(l).role%>" onclick="return warning()">删除</a></td>
						<td><a href="sysmodifyinfo.jsp?id=<%=syslist.get(l).id%>&role=<%=syslist.get(l).role%>">修改</a></td>
						<%l++;}
						else{%><td id="empty" colspan="4"></td><%}%>
				</tr>
			<%t++;}}%>
	</table>
	<p><a href="sysadmin.jsp">返回主页</a></p>
	<%}%>
</div>

</body>
</html>