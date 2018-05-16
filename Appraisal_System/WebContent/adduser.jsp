<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加用户</title>

<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-style: solid;
	border-width: 1px;}
select{width: 154px;
	height: 20px;}
td{background-color: #FFDEAD;
	width:160px;}
#bt{width:108px}
</style>

<script type="text/javascript">
function getIdName(role)
{
	if(role=="STUDENT")
	{
		document.getElementById("idname").innerHTML="学号";
		document.getElementById("class").innerHTML="<td>班级</td><td><input type=\"text\" name=\"class\"/></td>";
	}
	else
	{
		document.getElementById("idname").innerHTML="工号";
		document.getElementById("class").innerHTML="";
	}
	if(role=="TEACHER")
	{
		document.getElementById("course").innerHTML="<td>所授课程</td><td><input type=\"text\" name=\"course\"/></td>";
		document.getElementById("Tclass").innerHTML="<td>所教班级</td><td><input type=\"text\" name=\"Tclass\"/></td>";
	}
	else
	{
		document.getElementById("course").innerHTML="";
		document.getElementById("Tclass").innerHTML="";
	}
}

function goback()
{
	location="sysadmin.jsp";
}	
</script>

</head>
<body>

<div>
	<br/><br/>
	<h1>添加用户</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("SYSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{ %>
	<form action="AddUser" method="post">
	<table align="center">
		<tr>
			<td>类型</td>
			<td><select name = "role" onchange="getIdName(this.value)">
					<option value="STUDENT">学生</option>
					<option value="TEACHER">教师</option>
					<option value="ASSADMIN">评教管理员</option>
					<option value="SYSADMIN">系统管理员</option>
			</select></td>
		</tr>
		<tr>
			<td id="idname">学号</td>
			<td><input type="text" name="id"/></td>
		</tr>
		<tr>
			<td>姓名</td>
			<td><input type="text" name="name"/></td>
		</tr>
		<tr>
			<td>姓别</td>
			<td><select name="sex">
				<option value="男">男</option>
				<option value="女">女</option>
			</select></td>
		</tr>
		<tr id="class">
			<td>班级</td>
			<td><input type="text" name="Sclass"/></td>
		</tr>
		<tr id="course"></tr>
		<tr id="Tclass"></tr>
		<tr>
			<td>电话</td>
			<td><input type="text" name="phone"/></td>
		</tr>
		<tr>
			<td>住址</td>
			<td><input type="text" name="address"/></td>
		</tr>
	</table><br/>
			<input id = 'bt' type="button" value="返回" onclick="goback()"/>
			<input id = 'bt' type="reset" value="重置"/>
			<input id = 'bt' type="submit" value="提交"/>
	</form>
	<p style="color:red">学号/工号和课程号均为四位数字，班级为100以内的数字<br/>教师如果有多个班级，则将这些班级以英文逗号隔开</p>
	<%}%>
	
</div>
</body>
</html>