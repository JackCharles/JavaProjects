<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.*" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-维护模板页</title>
<script type="text/javascript">
function warning()
{
	return confirm("你确定要删除或修改模板，评教期间无法删除和修改\n删除模板后与之相关的评教信息都将删除!");
}
</script>

<style type="text/css">
div{text-align:center;
	font-family: 微软雅黑;
	font-weight: lighter;
	color:blue;}
table{border-style: solid;
	border-width: 1px;}
select{width: 154px;
	height: 20px;}
td{background-color: #FFDEAD;
	width:150px;}
#bt{width:108px}
</style>

</head>
<body>
<div>
	<h1>维护模板</h1>
	<%UserBean us=(UserBean)session.getAttribute("user");
	if(us==null||!us.role.equals("ASSADMIN")){%>
	<p>你不是系统管理员或你长时间未操作，要继续请<a href="index.jsp">重新登录</a></p>
	<%}else{ %>
		<p style="color:red;font-size:24px">注意：评教期间无法删除和修改，删除或修改模板后<br/>与之相关的评教信息都将删除!!!</p>
		<%List<Model> mlist = JdbcHelper.QueryModel("ALL");
		  if(mlist==null){%>
		  <p style="color:red"><%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%><a href="assadmin.jsp">点我返回</a></p>
		 <%}else{ %>
		 	<table align = "center">
			<%for(int i=0;i<mlist.size();i++){ %>
				<tr>
					<td><%=mlist.get(i).mname%></td>
					<td><%=mlist.get(i).mid%></td>
					<td>
						<a href="deletemodel.jsp?mid=<%=mlist.get(i).mid%>" onclick="warning()">删除</a>
						<a href="modifymodelinfo.jsp?mid=<%=mlist.get(i).mid%>" onclick="warning()">修改</a>
					</td>
				</tr>
			<%} %>
			</table><br/>
			<a href="assadmin.jsp">点我返回</a>
	<%}}%>
</div>

</body>
</html>