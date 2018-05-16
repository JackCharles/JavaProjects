<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-信息修改页</title>
<style type="text/css">
table{font-family: 微软雅黑;
	border-style:solid;
	border-color:#00CED1;
	font-size: 18px;}
#n{width:80px;
	background-color: #9AFF9A;}
#v{width:180px;
	background-color:#9AFF9A}
#bt{width:90px;}
</style>

<%!String url="index.jsp";%>
<%UserBean us = (UserBean)session.getAttribute("user"); %>
		<!-- 获取返回页面 -->
		<%if(us!=null){
			if(us.role.equals("STUDENT"))
		  		url="student.jsp";
		  	else if(us.role.equals("TEACHER"))
		  		url="teacher.jsp";
		 	 else if(us.role.equals("SYSADMIN"))
		  		url="sysadmin.jsp";
		 	 else
		  		url="assadmin.jsp";
		  }%>
<script type="text/javascript">
function goback()
{
	location="<%=url%>";
}

function check(form)
{
	if(form.phone.value==""||form.address.value=="")
	{
		document.getElementById("error").innerHTML="输入不能为空！";
		return false;
	}
	if(!form.phone.value.match(/^[0-9]*$/))
	{
		document.getElementById("error").innerHTML="电话号码只能是数字！";
		return false;
	}
	
	return true;
}
</script>


</head>
<body>
	
	<div style="text-align: center; font-family: 微软雅黑; font-weight: lighter;">
	<br/><br/>
		
		<h1 style="color:blue">—修改个人信息—</h1>
		<% if(us==null){ %>
			<div>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></div>
		<% }else{%><!-- session有效 -->
	  
			<form action="ModifyInfo" method="post" onsubmit="return check(this)">
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
					<td id="n">姓名</td>
					<td id="v"><%=us.name %></td>
				</tr>
				<tr>
					<td id="n">性别</td>
					<td id="v"><%=us.sex %></td>
				</tr>
				<tr>
					<td id="n">职称</td>
					<% if(us.role.equals("STUDENT")){ %>
					<td id="v">学生</td>
					<%}else if(us.role.equals("TEACHER")){ %>
					<td id="v">教师</td>
					<%}else if(us.role.equals("SYSADMIN")){ %>
					<td id="v">系统管理员</td>
					<%}else{%>
					<td id="v">评教管理员</td>
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
						<td id="n">所教班级</td>
						<td id="v"><%for(int i=0;i<us.Tclass.size();i++){ %><%=us.Tclass.get(i)+","%><%}%></td>
					</tr>
				<%} %>
			<tr>
				<td id="n">电话</td>
				<td id="v"><input type="text" name="phone" value="<%=us.phone%>" size="24"/></td>
			</tr>
			<tr>
				<td id="n">住址</td>
				<td id="v"><input type="text" name="address" value="<%=us.address%>" size="24"/></td>
			</tr>
			</table><br/>
				<input id="bt" type="button" value = "返回" onclick="goback()"/>
				<input id="bt" type="reset" value = "重置"/>
				<input id="bt" type="submit" value="提交"/>
			</form>
			<p id = "error" style="color:red"></p>
		
		<%} %>
		</div>
</body>
</html>