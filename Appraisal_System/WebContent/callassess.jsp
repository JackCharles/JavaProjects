<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.util.List" %>
<%@ page import = "com.user.Model" %>
<%@ page import = "com.user.UserBean" %>
<%@ page import = "com.mysql.JdbcHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type = "text/javascript">
function check(form)
{
	if(!form.userid.value.match(/^\d{4}$/))
	{
		document.getElementById("errid").innerHTML = "账号格式不正确！";
		return false;
	}
	return true;
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发起评教页面</title>

<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-style: solid;
	border-width: 1px;
	border-color: black}
td{background-color:#BEBEBE;
	color:black;}
</style>
<script type="text/javascript">
function goback()
{
	location="assadmin.jsp";
}
</script>

</head>

<body>
<div>
	<br/><br/>
	<h1>发起评教</h1>
	<%UserBean us = (UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("ASSADMIN")){%>
	<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<%}else{ %>	
		<p style="color:red;font-size:24px">注意：请仔细确认教师号，如果以前已有<br/>评教活动，本次将覆盖前面的评教活动！！！</p>
		<form method="post" action="CallAssess">
			<input type="hidden" name="starterid" value="<%=us.id %>"/>
		<table align=center>
			<tr>
				<td>采用模板：</td>
				<td>
					<select name = "mid">
					<%List<Model> aList = JdbcHelper.QueryModel("ALL");
					  for(int i=0;i<aList.size();i++){ %>
						<option value = "<%=aList.get(i).mid%>"><%=aList.get(i).mname %></option>
					<%} %>
					</select>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>评教对象：</td>
				<td><input type="text" name="teacher_be_assess"/></td>
				
				<td>四位教师号如:3001</td>
			<tr>
				<td>开始时间：</td>
				<td><input type="text" name="start_time" value="2016-12-10"/></td>
				<td>格式为“2016-12-10”</td>
			</tr>
			<tr>
				<td>结束时间：</td>
				<td><input type="text" name="end_time" value="2017-01-10"/></td>
				<td>格式为“2016-12-10”</td>
			</tr>
		</table>
		<br/>
			<input id = 'bt' type="button" value="返回" onclick="goback()"/>
			<input id = 'bt' type="reset" value="重置"/>
			<input id = 'bt' type="submit" value="提交"/>
		</form>
	<%} %>
	</div>
</body>
</html>