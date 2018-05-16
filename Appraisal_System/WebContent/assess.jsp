<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.UserBean" %>
<%@ page import="com.user.Question" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理里系统-学生评教页</title>

<style type="text/css">
div{font-family:微软雅黑; 
	font-weight: lighter;
	text-align:center;}
table{border-style: solid;
	border-width: 1;
	border-color:#008B00;}
#qs{background-color: #AFEEEE;
	width: 500px;}
#an{background-color: #87CEFA;
	width: 120px;}
#bt{height: 30px;
	width: 200px;}
</style>

<script type="text/javascript">
function goback()
{
	location="student.jsp";
}

function check(form)
{ 
	return true;
}
</script>

</head>

<body>
<div>
	<%UserBean us = (UserBean)session.getAttribute("user");
	  @SuppressWarnings("unchecked")//屏蔽异常（Object转泛型会发出不安全警告）
	  List<UserBean> list = (List<UserBean>)session.getAttribute("TnCn");
	  String index = request.getParameter("index");%>
	<h1 style="color:blue;">—学生评教页—</h1>
	<% if(us==null){ %>
		<p>由于你长时间未操作，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{
		if(index==null||list==null||list.size()==0){%>
			<p style="color:red">意外的参数传递错误，请返回刷新重试！<a href="student.jsp">点我返回</a></p>
		<%}else{%>
			<!-- 此处正式开始展示问题 -->
			<%int i=Integer.parseInt(index);//获得list索引值 %>
			<h3>
			<span style="color:blue"><%=us.name%></span>同学，你现在进行的是对
			<span style="color:blue">《<%=list.get(i).courseName%>》<%=list.get(i).name%></span>
			老师的教学评价
			</h3>

			<!-- 查询评教问题 -->
			<%List<Question> qslist = JdbcHelper.QueryAssessQuestions(list.get(i).id,null);
			  if(qslist == null || qslist.size()==0){%>
			  <p style="color:red"><%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%><a href="student.jsp">点我返回</a></p>
			  <%}else{%>
			  
			<!-- 评价正文 -->
			<form action="SubmitAssess" method="post" onsubmit="return check(this)">
				<input type="hidden" name="sid" value="<%=us.id%>"/>
				<input type="hidden" name="tid" value="<%=list.get(i).id%>"/>
				<input type="hidden" name="qsnum" value="<%=qslist.size()%>"/>
				<!-- 上面为隐藏域，传递学号,教师号和问题总数-->
				<table align="center">
					<%for(int j=0;j<qslist.size();j++){ %>
					<tr style="text-align:left;">
						<td id="qs"><%=j+1+"、"+qslist.get(j).qs%></td><!-- 题目，下面是答案 -->
						<td id="an"><input type="radio" name="answer<%=j+1%>" value="A"/> <%="A."+qslist.get(j).ansA%></td>
						<td id="an"><input type="radio" name="answer<%=j+1%>" value="B"/> <%="B."+qslist.get(j).ansB%></td>
						<td id="an"><input type="radio" name="answer<%=j+1%>" value="C"/> <%="C."+qslist.get(j).ansC%></td>
						<td id="an"><input type="radio" name="answer<%=j+1%>" value="D"/> <%="D."+qslist.get(j).ansD%></td>
					</tr>
					<%} %>
				</table><br/>
				<input id = 'bt' type="button" value="返回" onclick="goback()"/>
				<input id = 'bt' type="reset" value="重置"/>
				<input id = 'bt' type="submit" value="提交"/>
			</form>
				
		<%}}}%>
</div>
</body>
</html>