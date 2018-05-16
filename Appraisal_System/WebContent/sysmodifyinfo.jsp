<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-用户信息修改</title>

<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
select{width: 154px;
	height: 20px;}
table{border-width: 1px;
	border-style: solid;}
td{width:160px;
	background-color: #EEE5DE}
#bt{width:108px}
</style>

<script type="text/javascript">
function goback()
{
	location="modifyuser.jsp";
}
</script>

</head>

<body>
<div>
	<h1><br/><br/>用户信息修改</h1>
	<%UserBean us = (UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("SYSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{
		String Id = request.getParameter("id");
		String Role = request.getParameter("role");
		UserBean modi = JdbcHelper.queryUserInfo(Id,Role);
		%>
		
	<form action="SysModifyInfo" method="post">
		<input type="hidden" name="oldRole" value="<%=Role%>"/>
		<input type="hidden" name="id" value="<%=Id%>"/>
		<!-- 以上为隐藏域方便后台操作 -->
		
	<table align="center">
		<tr>
			<td>学号/工号</td>
			<td><input type="text" disabled="disabled" value="<%=Id%>"/></td>
		</tr>
		<!--用户id不可更改-->
		<tr>
			<td>用户类型</td>
			<td><select name = "role">
					<option value="<%=modi.role%>">
						<%if(modi.role.equals("STUDENT")){%><%="学生"%>
						<%}else if(modi.role.equals("TEACHER")){%><%="教师"%>
						<%}else if(modi.role.equals("SYSADMIN")){%><%="系统管理员"%>
						<%}else{%><%="评教管理员"%><%}%>
					</option>
					<option value="STUDENT">学生</option>
					<option value="TEACHER">教师</option>
					<option value="ASSADMIN">评教管理员</option>
					<option value="SYSADMIN">系统管理员</option>
			</select></td>
		</tr>
		<tr>
			<td>姓名</td>
			<td><input type="text" name="name" value="<%=modi.name%>"/></td>
		</tr>
		<tr>
			<td>性别</td>
			<td><select name = "sex">
					<option value="<%=modi.sex%>"><%=modi.sex%></option>
					<option value="男">男</option>
					<option value="女">女</option>
			</select></td>
		</tr>
		<tr>
			<td>电话</td>
			<td><input type="text" name="phone" value="<%=modi.phone%>"/></td>
		</tr>
		<tr>
			<td>住址</td>
			<td><input type="text" name="address" value="<%=modi.address%>"/></td>
		</tr>
		<tr>
			<td>登录密码</td>
			<td><input type="text" name="passwd" value="NOCHANGE"/></td>
		</tr>
		<!-- 以上为公共信息,下面部分将根据角色判断 -->
		
		<tr><td colspan = '2' style="color:red">"所在班级"仅对学生有效</td></tr>
		<tr>
			<td>所在班级</td>
			<td><input type="text" name="Sclass" value="<%=modi.Sclass%>"/></td>
		</tr>
		<tr><td colspan = '2' style="color:red">"所授课程"和"授课班级"仅对教师有效</td></tr>
		<tr>
			<td>所授课程</td>
			<td><input type="text" name="course" value="<%=modi.courseId%>"/></td>
		</tr>
		<tr>
			<td>授课班级</td>
			<td><input type="text" name="Tclass" value="<%if(modi.Tclass.size()>0)
			{%><%=modi.Tclass.get(0)%><%}for(int i=1;i<modi.Tclass.size();i++)
			{ %><%=","+modi.Tclass.get(i)%><%}%>"/></td>
		</tr>
	</table><br/>
		<input id = 'bt' type="button" value="返回" onclick="goback()"/>
		<input id = 'bt' type="reset" value="重置"/>
		<input id = 'bt' type="submit" value="提交"/>
	</form>
	<p style="color:red">学号/工号和课程号均为四位数字，班级为100以内的数字
	<br/>教师如果有多个班级，则将这些班级以英文逗号隔开
	<br/>密码如无必要请不要修改，保持原样"NOCHANGE"即可
	<br/>对所选角色有效的信息均不能为空，提交前请仔细检查</p>
	<%}%>
</div>

</body>
</html>