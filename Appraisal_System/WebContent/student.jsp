<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>评教管理系统-学生服务页</title>
</head>
<body>
	<%UserBean us = (UserBean)session.getAttribute("user");%>
	<h1 style="color:#00BFFF;font-family:微软雅黑; font-weight: lighter; 
	font-style: italic;">—欢迎使用评教管理系统-学生页—</h1>
	<% if(us==null||!us.role.equals("STUDENT")){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{ %>
		<h2 style="font-family: 微软雅黑; font-weight: lighter;">
		Welcome：<%=us.name+"（"+us.id+"）"+"同学" %>
		</h2>
		
		<h1>---------------------------------------------------------------</h1>
		
		<h2 style="font-family: 微软雅黑; font-weight: lighter;">现在您可以：</h2>
		<div style="font-family: 微软雅黑; font-weight: lighter; font-size: 20px;">
			<p>◆ <a href="viewhistory.jsp">察看历史考评记录</a></p>
			<p>◆ <a href="viewinfo.jsp">察看维护个人信息</a></p>
			<p>◆ <a href="chpasswd.jsp">修改您的登录密码</a></p>
			<p>◆ <a href="index.jsp">退出登录</a></p>	
				<%List<UserBean> list = JdbcHelper.QueryAssess(us.id);
				if (list == null){%>
				<p>查询数据库出错!</p>
				<p style="color:red"><%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] %></p>
				<%}
				else{
					if(list.size()==0){%>
						<p>当前没有评教活动！</p>
					<%}
					else{
						request.getSession().setAttribute("TnCn", list);//将该list放入seession
						int n = list.size();
						for(int i=0;i<n;i++)
						{%>
							<p>点击评价<a href="assess.jsp?index=<%=i%>">《<%=list.get(i).courseName%>》 <%=list.get(i).name%></a></p>
						<%}%>
					<%}%>
				<%} %>	
		</div>
	<% } %>
</body>
</html>