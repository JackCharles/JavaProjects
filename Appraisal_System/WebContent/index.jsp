<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-登录页</title>

<script type = "text/javascript">
function check(form)
{
	if(!form.userid.value.match(/^\d{4}$/))
	{
		document.getElementById("errid").innerHTML = "账号为4位学号或工号";
		return false;
	}
	return true;
}
</script>

</head>
<body>
	<div style = "font-size:50px; font-family: 华文行楷; text-align: center; color: #1E90FF;">
	<br/><br/>
	欢迎使用考评管理系统
	<br/><br/>
	</div>
	
	<form action = "loginAction" method = "post" onsubmit = "return check(this)">
		<div style = "text-align: center; font-family: 微软雅黑 ; font-size: 20px; color:#1E90FF;">	
			<p>账号：<input type = "text" name = "userid" size = "20"/></p>
			<p>密码：<input type = "password" name = "password" size = "21"/></p>
			<p><input type = "reset" value = "重置" style = "width:100px;"/>
			   <input type = "submit" value = "登录" style = "width:105px;"/></p>
			<span id = "errid" style = "color:red"></span>
		</div>	
	</form>
	
</body>
</html>