<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.user.UserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- 公共页面 -->
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>评教管理系统-密码修改页</title>
	
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
		function check(form)
		{
			if(form.newpasswd1.value==null||form.oldpasswd.value==null||form.newpasswd2.value==null)
			{
				document.getElementById("passErr").innerHTML="输入不能为空！";
				return false;
			}
			if(form.newpasswd1.value.length<6)
			{
				document.getElementById("passErr").innerHTML="新密码至少6位！";
				return false;
			}
			if(form.newpasswd1.value!=form.newpasswd2.value)
			{
				document.getElementById("passErr").innerHTML="新密码两次输入不一致！";
				return false;
			}
			if(form.newpasswd1.value==form.oldpasswd.value)
			{
				document.getElementById("passErr").innerHTML="新密码与旧密码不能相同！";
				return false;
			}
			return true;
		}
		
		function goback()
		{
			location="<%=url%>";
		}
	
	</script>
	
</head>

<body>
	<% if(us==null){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{ %>
	<br/><br/><br/><br/>
	<h1 style="text-align: center; color:blue ;font-family:微软雅黑; 
	font-weight: lighter;">修改密码</h1>
	
	<form action="ChangePasswd" method="post" onsubmit = "return check(this)">
		<table style="border-style: solid; border-color: #2E8B57; border-width: 2px;" align = "center">
			<tr>
				<td bgcolor="#ADD8E6" align = "center">原始密码：</td>
				<td><input type="password" name="oldpasswd"></td>
			</tr>
			<tr>
				<td bgcolor="#ADD8E6" align = "center">新密码：</td>
				<td><input type="password" name="newpasswd1"></td>
			</tr>
			<tr>
				<td bgcolor="#ADD8E6" align = "center">确认密码：</td>
				<td><input type="password" name="newpasswd2"></td>
			</tr>
		</table>
		<p align = "center">
			<input style="width:78px" type="button" name="back" value="返回" onclick="goback()"/>
			<input style="width:78px" type="reset" name="reset" value="重置"/>
			<input style="width:78px" type="submit" name="submit" value="提交"/>
		</p>
		<div id="passErr" style="color:red; text-align: center"></div>
	</form>
	<% } %>
	
</body>
</html>