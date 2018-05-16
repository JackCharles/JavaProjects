<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.user.*" %>
<%@ page import="com.mysql.JdbcHelper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Comparator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评教管理系统-查看评教结果</title>

<style type="text/css">
div{font-family:微软雅黑; 
	font-weight: lighter;
	text-align:center;}
table{border-style: solid;
	border-width: 1px;}
td{background-color:#E0EEEE;
	text-align: left}
#q{text-align: center;
	width:450px}
#a{text-align: center;
	width:120px}
#s{text-align: center;
	width:80px}
</style>
<script type="text/javascript">
function goback()
{
	location="student.jsp";
}	
</script>
</head>

<body>
<div>
	<%UserBean us = (UserBean)session.getAttribute("user");%>
	<h1 style="color:blue;">—查看评教结果—</h1>
	<% if(us==null||!us.role.equals("ASSADMIN")){ %>
		<p>由于你长时间未操作或不是评教管理员，要继续请<a href="index.jsp" title="点击重新登录">重新登录</a></p>
	<% }else{
		List<AssRes> list = JdbcHelper.QueryAssResult("ALL");
		if(list==null){%>
		<p style="color:red">读取数据库错误：<%=JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode]%></p>
		<%}else if(list.size()==0){%>
		 <p>暂时没有评教活动!</p>
		<%}else{
			Comparator<AssRes> comparator = new Comparator<AssRes>(){
				   public int compare(AssRes s1, AssRes s2) {
					   if(s1.totalGd-s2.totalGd>0)
				   			return -1;
					   return 1;
				   }};
			Collections.sort(list,comparator);//总分排序
			for(int i=0;i<list.size();i++){%>
		<h3>关于<%=list.get(i).tname+"（"+list.get(i).tid+"）老师的评价结果"%></h3>
		<table align="center">
			<tr>
				<td>发起者：<%=list.get(i).starterName+"（"+list.get(i).starterId+"）"%><br/>
				使用模板：<%=list.get(i).modelName+"（"+list.get(i).modelId+"）"%></td>
				<td colspan="2">开始日期：<%=list.get(i).startTime%><br/>
				截止日期：<%=list.get(i).endTime%></td>
				<td colspan="2">评教状态：<%=list.get(i).stat%><br/>
				评教人数：<%=list.get(i).stuNum%></td>
				<td id="s">总成绩<%=list.get(i).totalGd%></td>
			</tr>
			<tr>
				<td id="q">问题</td>
				<td id="a">答案A[人数]</td>
				<td id="a">答案B[人数]</td>
				<td id="a">答案C[人数]</td>
				<td id="a">答案D[人数]</td>
				<td id="s">得分</td>
			</tr>
			<%for(int j=0;j<list.get(i).qst.length;j++){ %>
			<tr>
				<td><%=j+1+"、"+list.get(i).qst[j].qs%></td>
				<td><%="A、"+list.get(i).qst[j].ansA+" ["+list.get(i).totalRes[j].countA+"]"%></td>
				<td><%="B、"+list.get(i).qst[j].ansB+" ["+list.get(i).totalRes[j].countB+"]"%></td>
				<td><%="C、"+list.get(i).qst[j].ansC+" ["+list.get(i).totalRes[j].countC+"]"%></td>
				<td><%="D、"+list.get(i).qst[j].ansD+" ["+list.get(i).totalRes[j].countD+"]"%></td>
				<td id="s"><%=list.get(i).totalRes[j].qsGrade%></td>
			</tr>
			<%} %>
		</table>
	<%}}}%>
	<p><a href="assadmin.jsp">点我返回</a></p>
</div>
</body>
</html>