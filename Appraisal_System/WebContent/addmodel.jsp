<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.*" %>
<%@ page import="com.mysql.*" %>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-添加模板页</title>
<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-width: 1px;
	border-style: solid;}
td{background-color:#BBFFFF}
#qs{width:350px;}
#an{width:100px}
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
	<h1>添加模板</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("ASSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{%>
		<%String mid = 'm'+String.valueOf(System.currentTimeMillis());%>
		<form action="AddModel" method="post">
			<input type="hidden" name="modelid" value="<%=mid%>"/>

		<table align = "center">
			<tr>
				<td>模板ID：<%=mid%></td>
				<td colspan="2">模板名称：<input type="text" name="mname" value="新模板"/></td>
				<td colspan="2">问题数量：<input type="text" name="qstnum" value="10"/><td>
			</tr>
			<tr>
				<td>问题</td>
				<td>选项A</td>
				<td>选项B</td>
				<td>选项C</td>
				<td>选项D</td>
			</tr>
			<%for(int i=0;i<20;i++){ %>
			<tr>
				<td><input id="qs" type="text" name="qst<%=i%>" value="问题<%=i+1%>"/></td>
				<td><input id="an" type="text" name="ansA<%=i%>" value="选项A"/></td>
				<td><input id="an" type="text" name="ansB<%=i%>" value="选项B"/></td>
				<td><input id="an" type="text" name="ansC<%=i%>" value="选项C"/></td>
				<td><input id="an" type="text" name="ansD<%=i%>" value="选项D"/></td>
			</tr>
			<%} %>
		</table><br/>
			<input id = 'bt' type="button" value="返回" onclick="goback()"/>
			<input id = 'bt' type="reset" value="重置"/>
			<input id = 'bt' type="submit" value="提交"/>
		</form>
	<%}%>
	</div>
</body>
</html>