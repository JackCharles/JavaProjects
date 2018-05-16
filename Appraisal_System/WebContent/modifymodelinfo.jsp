<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.*" %>
<%@ page import="com.mysql.*" %>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-修改模板页</title>
<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-width: 1px;
	border-style: solid;}
td{background-color:#BBFFFF}
#qs{width:400px;}
#an{width:80px}
</style>

<script type="text/javascript">
function goback()
{
	location="modifymodel.jsp";
}
</script>

</head>
<body>
	<div>
	<h1>维护模板</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	String mid = request.getParameter("mid");
	if(us==null||!us.role.equals("ASSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{
		List<Question> qslist = JdbcHelper.QueryAssessQuestions(null, mid);
		List<Model> model = JdbcHelper.QueryModel(mid);
		if(qslist==null||model==null){%>
		
		<p>读入数据库失败！</p>
		
		<%}else{ %>
		<form action="ModifyModel" method="post">
			<input type="hidden" name="modelid" value="<%=mid%>"/>
			<input type="hidden" name="qstnum" value="<%=qslist.size()%>"/>
	
		<table align = "center">
			<tr>
				<td>模板ID：<%=model.get(0).mid%></td>
				<td colspan="5">模板名称：<input type="text" name="mname" value="<%=model.get(0).mname%>"/></td>
			</tr>
			<tr>
				<td>问题</td>
				<td>选项A</td>
				<td>选项B</td>
				<td>选项C</td>
				<td>选项D</td>
			</tr>
			<%for(int i=0;i<qslist.size();i++){ %>
			<tr>
				<td><input id="qs" type="text" name="qst<%=i%>" value="<%=qslist.get(i).qs%>"/></td>
				<td><input id="an" type="text" name="ansA<%=i%>" value="<%=qslist.get(i).ansA%>"/></td>
				<td><input id="an" type="text" name="ansB<%=i%>" value="<%=qslist.get(i).ansB%>"/></td>
				<td><input id="an" type="text" name="ansC<%=i%>" value="<%=qslist.get(i).ansC%>"/></td>
				<td><input id="an" type="text" name="ansD<%=i%>" value="<%=qslist.get(i).ansD%>"/></td>
			</tr>
			<%} %>
		</table><br/>
			<input id = 'bt' type="button" value="返回" onclick="goback()"/>
			<input id = 'bt' type="reset" value="重置"/>
			<input id = 'bt' type="submit" value="提交"/>
		</form>
	<%}}%>
	</div>
</body>
</html>