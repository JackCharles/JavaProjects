<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
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
td{background-color: #FFDEAD;
	width:160px;}
#bt{width:108px}
</style>

<script type="text/javascript">

function goback()
{
	location="sysadmin.jsp";
}
function check(form)
{
	if(form.courseId.value==""||form.courseName.value=="")
	{
		document.getElementById("error").innerHTML = "输入不能为空!";
		return false;
	}
	
	if(!form.courseId.value.match(/^\d{4}$/))
	{
		document.getElementById("error").innerHTML = "课程号仅为4位数字!";
		return false;
	}
	return true;
}
</script>

</head>
<body>

<div>
	<br/><br/>
	<h1>添加/修改课程</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("SYSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{ 
		String action = request.getParameter("action");
		String cid = request.getParameter("id");
		String cname = null;
		if(action == null){%>
			<p>参数错误，请重试!<a href="sysadmin.jsp">点我返回</a></p>
		<%}else{
			if(action.equals("MODIFY")){
				List<UserBean> cu =JdbcHelper.QueryCourse(cid);
				cname = cu.get(0).courseName;
				}%>
	
		<form action="UpdateCourse" method="post" onsubmit="return check(this)">
			<input type="hidden" name="action" value="<%=action%>"/>
			<input type="hidden" name="oldcid" value="<%=cid%>"/>
		<table align="center">
		
			<tr>
				<td>课程号</td>		
				<td><input type="text" name="courseId" value="<%if(action.equals("MODIFY")){%><%=cid%><%}%>"/></td>
			</tr>

			<tr>
				<td>课程名</td>
				<td><input type="text" name="courseName" value="<%if(action.equals("MODIFY")){%><%=cname%><%}%>"/></td>
			</tr>
		</table>
		<br/>
			<input id = 'bt' type="button" value="返回" onclick="goback()"/>
			<input id = 'bt' type="reset" value="重置"/>
			<input id = 'bt' type="submit" value="提交"/>
		</form>
		<p style="color:blue">提示：课程号为四位数字！</p>
		<p id="error" style="color:red"></p>
	<%}}%>
	
</div>
</body>
</html>