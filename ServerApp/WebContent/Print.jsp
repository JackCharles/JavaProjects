<%@page import="com.user.User"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="com.user.Tools"%>
<%@page import="com.user.Event"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");
String title = request.getParameter("title");
String type = request.getParameter("type");
if(title==null||type==null)
	Tools.PrintErrorMsg(request, response, "Get参数丢失，请不要单独前往此页面！");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>打印案件</title>
<link href="css/print_css.css" rel="stylesheet"type="text/css" media="print"/>
<script language="javascript" src="js/jquery-1.4.4.min.js"></script>
<script language="javascript" src="js/jquery.jqprint-0.3.js"></script>

<script language="javascript">
function print(){
	$("#ddd").jqprint();
}
</script>

<style type="text/css">
table{
	border-collapse: collapse;
	font-family: "宋体";
	font-size: 16px;
}
table caption{
	font-size: 16px;
	font-weight: bold;
}
td, th{
	border: 1px solid black;
}

#bta{
	padding-top:20px;
	text-align: center;
}

#bta input{
	width:200px;
	height: 50px;
}
</style>


</head>
<body>
	<%
	ArrayList<Event> li;
	type = URLDecoder.decode(type,"utf-8");
	if(type.equals("search"))//from search page
		li = (ArrayList<Event>)request.getSession().getAttribute("events");
	else
		li = JdbcHelper.GetAllEventsByType(type);
	if(li==null)
		if(type.equals("search"))
			Tools.PrintErrorMsg(request, response, "session丢失，可能是登录超时！");
		else
			Tools.PrintErrorMsg(request, response, "案卷查询失败："+JdbcHelper.ErrorMsg);
	else
	{
	%>
	<div id="ddd">
		<table align="center">
		<caption><%=URLDecoder.decode(title,"utf-8") %></caption>
		<tr>
			<th>日期</th>
			<th>流水</th>
			<th>公证员</th>
			<th>案件类型(子类型)</th>
			<th>当事人(身份证号)</th>
			<th>备注</th>
			<th>案卷号</th>
			<th>审批</th>
			<th>审批人</th>
		</tr>
		<%for(int i=0;i<li.size();++i){ 
		ArrayList<User> pt = JdbcHelper.GetParties(li.get(i).date, li.get(i).serial, li.get(i).handler1);
		%>
		<tr>
			<td><%=li.get(i).date %></td>
			<td><%=li.get(i).serial %></td>
			<td><%=li.get(i).handler1%></td>
			<td><%=li.get(i).type+"("+li.get(i).subtype+")" %></td>
			<td>
				<%for(int j=0;j<pt.size();++j){ %>
					<%=pt.get(j).getUserName()+"("+pt.get(j).getId()+")"%>
					<%if(j<pt.size()-1){ %>
					<br/>
				<%}} %>
			</td>
			<td><%=li.get(i).comment %></td>
			<td><%=li.get(i).rno %></td>
			<td><%=li.get(i).confirm.equals("N")?"未审":"已审" %></td>
			<td><%=li.get(i).approval %></td>
		</tr>
		<%} %>
		</table>
	</div>
	<div id="bta"><input type="button" onclick="print()" value="打印文档(建议A4纸横向打印)"/></div>
	<%}%>
</body>
</html>