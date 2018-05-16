<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<%@ page import="com.user.Question" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-查看历史评教</title>

<style type="text/css">
div{font-family:微软雅黑; 
	font-weight: lighter;
	text-align:center;}
#qs{text-align: left;
	background-color: #FFF8DC;
	width:400px;
	height: 30px}
#an{background-color: #FFDAB9;
	text-align: left;
	width:120px;
	height:30px}
table{border-style: solid;
	border-width: 1px;
	border-color: blue;}
</style>
<script type="text/javascript">
function goback()
{
	location="viewhistory.jsp";
}	
</script>
</head>


<body>
<div>
	<%UserBean us = (UserBean)session.getAttribute("user");
	  if(us==null){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{
		String index = request.getParameter("tid");
		@SuppressWarnings("unchecked")
		List<UserBean> tlist = (List<UserBean>)request.getSession().getAttribute("tlist");
		if(index==null||tlist==null){%>
		<p>参数错误:<a href="student.jsp">点我返回</a></p>
		<%}else{
		String tid = tlist.get(Integer.parseInt(index)).id;
		List<Question> qslist = JdbcHelper.QueryAssessInfo(us.id, tid);
		if(qslist==null||qslist.size()==0){%>
		<p style="color:red">读取数据库出错:<%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
	<% }else{%>
			<h2 style="color:blue;">—你正在查看对
			<span style="color:red"></span><%=tlist.get(Integer.parseInt(index)).name%> 老师的历史评教—</h2>
			<table align="center">
			<%for(int i=0;i<qslist.size();i++){%>
				<tr>
					<td id="qs"><%=i+1+"、"+ qslist.get(i).qs%></td>
					<td id="an"><%=qslist.get(i).answer%></td>
				</tr>
			<%} %>		
			</table><br/>
			<input style="width:200px" type="button" value="返回" onclick="goback()"/>
	<%}}}%>
</div>
</body>
</html>