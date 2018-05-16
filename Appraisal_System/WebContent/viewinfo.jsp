<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- 公共页面查看信息 -->

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>评教管理系统-信息查看页</title>

<style type="text/css">
table{font-family: 微软雅黑;
	border-style:solid;
	border-color: #00CDCD;
	font-size: 18px;}
td{ background-color:#FAFAD2;
	width:130px;}
#bt{width:135px}
</style>

<%UserBean us = (UserBean)session.getAttribute("user"); %>
<%!String url="index.jsp";%>
<!-- 页面跳转信息 -->
<%if(us!=null){
	if(us.role.equals("STUDENT"))
		url="student.jsp";
	else if(us.role.equals("TEACHER"))
		url="teacher.jsp";
	else if(us.role.equals("SYSADMIN"))
		url="sysadmin.jsp";
	else
		url="assadmin.jsp";}%>
		
<script type="text/javascript">
function goback()
{
	location="<%=url%>";
}

function modify()
{
	location="modifyinfo.jsp";
}
</script>

</head>
<body>

	<div style="text-align: center; font-family: 微软雅黑; font-weight: lighter;">
	<br/><br/>
		<h1 style="color:blue">—您的个人信息—</h1>
		<% if(us==null){ %>
			<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
		<% }else{ %><!-- session有效 -->
			  
			<table align="center">
			<%if(us.role.equals("STUDENT")){%>
				<tr>
					<td id="n">学号</td>
					<td id="v"><%=us.id %></td>
				</tr>
				<tr>
					<td id="n">班级</td>
					<td id="v"><%=us.Sclass%></td>
				</tr>
				<%}else{%>
				<tr>
					<td id="n">工号</td>
					<td id="v"><%=us.id %></td>
				</tr>
				<%}%>
				
				<tr>
					<td>姓名</td>
					<td><%=us.name %></td>
				</tr>
				<tr>
					<td>性别</td>
					<td><%=us.sex %></td>
				</tr>
				<tr>
					<td>职称</td>
					<% if(us.role.equals("STUDENT")){ %>
					<td>学生</td>
					<%}else if(us.role.equals("TEACHER")){ %>
					<td>教师</td>
					<%}else if(us.role.equals("SYSADMIN")){ %>
					<td>系统管理员</td>
					<%}else{%>
					<td>评教管理员</td>
					<%} %>	
				</tr>
				<%if(us.role.equals("TEACHER")) {%>
				<tr>
					<td>所授课程</td>
					<%if(us.courseId==null){%>
					<td>课程无效</td>
					<%}else{ %>
					<td><%=us.courseName %></td>
					<%}%>
				</tr>
				<tr>
					<td>所教班级</td>
					<td><%if(us.Tclass.size()>0){%><%=us.Tclass.get(0)%><%}for(int i=1;i<us.Tclass.size();i++){ %><%=","+us.Tclass.get(i)%><%}%></td>
				</tr>
				<%} %>
				<tr>
					<td>电话</td>
					<td><%=us.phone %></td>
				</tr>
				<tr>
					<td>住址</td>
					<td><%=us.address %></td>
				</tr>
			</table><br/>
		<input id = "bt" type = "button" name = "back" value="返回" onclick = "goback()"/>
		<input id = "bt" type = "button" name = "mod" value="修改" onclick = "modify()"/>
		<%} %>
	</div>
</body>
</html>