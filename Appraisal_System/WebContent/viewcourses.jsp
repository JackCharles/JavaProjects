<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-课程管理页</title>

<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-style: solid;
	 border-width: 1px;
	 border-color: green;}
td{background-color: #98F5FF;
	width:80px;}	
#n{width:150px}
</style>

<script type="text/javascript">
function check(form)
{
	if(!form.id.value.match(/^\d{4}$/))
	{
		document.getElementById("error").innerHTML = "课程号为4位数字！";
		return false;
	}
	return true;
}

function warning()
{
	return confirm("你确定要删除该课程？\nYou'd better know what\nyou are doing now!");
}

</script>

</head>

<body>
<div>
	<h1>课程管理</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("SYSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{ %>
	<form action="SearchCourse" method="post" onsubmit="return check(this)">
	输入课程号进行搜索：
		<input type="text" name="id"/>
		<input type="submit" value="搜索"/>
		<p id="error" style="color:red"></p>
	</form>
	
	<%	List<UserBean> cu = JdbcHelper.QueryCourse("ALL");
		if(cu==null)
		{%>
			<p>课程信息获取失败：</p>
			<p style="color:red;"><%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
		<%}else{%>
		<!-- 一列 -->
		<table align="center">
			<tr>
				<td id="n">课程名</td>
				<td>课程号</td>
				<td colspan="2">管理</td>
			</tr>
			<%	int m = cu.size();
				int i=0;
				while(i<m){%>
				<tr>
					<td id="n"><%=cu.get(i).courseName%></td>
					<td><%=cu.get(i).courseId  %></td>
					<%if(!cu.get(i).courseId.equals("9999")){%>
					<td><a href="deletecourse.jsp?id=<%=cu.get(i).courseId%>" onclick="return warning()">删除</a></td>
					<td><a href="updatecourse.jsp?action=MODIFY&id=<%=cu.get(i).courseId%>">修改</a></td>
					<%}i++;%>
				</tr>
				<%}}%>
	</table>
	<p><a href="sysadmin.jsp">返回主页</a></p>
	<%}%>
</div>

</body>
</html>