<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人信息维护</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/selfmodify_css.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		User us = (User) request.getSession().getAttribute("user");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
	%>
	<div id="selfinfo-modify">
		<form action="SelfInfoModify?type=info" method="post">
			<table class="bordered" align="center">
				<caption>个人信息维护</caption>
				<tr>
					<th>登录ID</th>
					<td><%=us.getUserId()%></td>
					<th>用户姓名</th>
					<td><%=us.getUserName()%></td>
				</tr>
				<tr>
					<th>管理权限</th>
					<td><%=us.getRole()%></td>
					<th>性别</th>
					<td><%=us.getSex().equals("M") ? "男" : "女"%></td>
				</tr>
				<tr>
					<th>职位</th>
					<td><%=us.getPosition()%></td>
					<th>身份证号</th>
					<td><%=us.getId()%></td>
				</tr>
				<tr>
					<th>电话</th>
					<td><input id="inputbox1" type="text" name="userphone"
						value="<%=us.getPhone()%>" /></td>
					<th>通讯地址</th>
					<td><input id="inputbox2" type="text" name="useraddress"
						value="<%=us.getAddress()%>" /></td>
				</tr>
				<%
					if ((us.getRole() & 64) != 0)
						{
				%>
				<tr>
					<td colspan="4"><input id="button" type="submit" value="修改信息" />
						<input id="button" type="reset" value="重置" /></td>
				</tr>
				<%
					}
					else
					{
				%>
				<tr>
					<td style="color: red; font-size: 16px; text-align: center;" colspan="4">你没有修改个人信息的权限！</td>
				</tr>
				<%} %>
			</table>
		</form>
	</div>


	<div id="change-passwd">
		<form action="SelfInfoModify?type=chpwd" method="post"
			onsubmit="return CheckPasswd(this)">
			<table class="bordered" align="center">
				<caption>修改登录密码</caption>
				<tr>
					<th>原始密码：</th>
					<td><input id="inputbox" type="password" name="OldPasswd" /></td>
				</tr>
				<tr>
					<th>新的密码：</th>
					<td><input id="inputbox" type="password" name="NewPasswd1" /></td>
				</tr>
				<tr>
					<th>确认密码：</th>
					<td><input id="inputbox" type="password" name="NewPasswd2" /></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;"><input id="button"
						type="submit" value="修改密码" /> <input id="button" type="reset"
						value="重置" /></td>
				</tr>
			</table>
		</form>
	</div>
	<%
		}
	%>
</body>
</html>