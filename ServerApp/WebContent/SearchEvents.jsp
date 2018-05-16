<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.user.Tools"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="com.user.User"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>案件检索</title>
<script src="laydate/laydate.js"></script>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/search_css.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
function check(f)
{
	var reg = /^([\u4e00-\u9fa5]+|([a-zA-Z]+\s?)+)$/;
	if(f.Party.value!="" && !reg.test(f.Party.value))
	{
		alert("当事人姓名不合法！");
		return false;
	}
	var reg2 = /^(\d{14}|\d{17})(\d|[xX])$/;
	if(f.Partyid.value!="" && !reg2.test(f.Partyid.value))
	{
		alert("当事人身份证号不合法！");
		return false;
	}
	//只有号码部分不为空才有效
	if(f.trno.value!="")
	{
		if(!/(\d{4})/.test(f.tyear.value))
		{
			alert("案卷号年份不合法！");
			return false;
		}
		if(!/^\d+[-]{0,1}\d+$/.test(f.trno.value))
		{
			alert("案卷号号码部分不合法！");
			return false;
		}
		f.RNo.value="（"+f.tyear.value+"）"+"吉长国安证 "+f.ttype.value+" 字第"+f.trno.value+"号";
	}
	return true;
}
</script>

</head>
<body>
	<%  
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		if (request.getSession().getAttribute("user") == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response);
		else
		{
	%>
	
	<p id="title">案件检索（可多条件综合检索）</p>
	<form action="SearchAndShow.jsp?page=1" method="post" onsubmit="return check(this);">
		<input type="hidden" name="from" value="searchpage"/>
		<table class="bordered">
			<tr>
				<th>案件日期</th>
				<td><input id="inputbox" name="Date" class="laydate-icon"
					onclick="laydate()" readonly="readonly"/></td>
			</tr>
			<tr>
				<th>公证员姓名</th>
				<td>
				<select id = "selectbox" name="Handler">
					<option value="">空</option>
				<%ArrayList<User> li = JdbcHelper.QueryAllUsers();
					if(li==null)
						Tools.PrintErrorMsg(request, response, "查询用户失败："+JdbcHelper.ErrorMsg);
					else
						for(int i=0;i<li.size();++i){%>
						<option value="<%=li.get(i).getUserName()%>"><%=li.get(i).getUserName()%></option>
						<%} %>
				</select>
				</td>
			</tr>
			<tr>
				<th>当事人姓名</th>
				<td><input id="inputbox" type="text" name="Party" /></td>
			</tr>
			<tr>
				<th>当事人身份证</th>
				<td><input id="inputbox" type="text" name="Partyid" /></td>
			</tr>
			<tr>
				<th>案卷号</th>
				<td>（<input id="tyear" type="text" name="tyear" value="<%=new SimpleDateFormat("yyyy").format(new Date())%>"/>）吉长国安证  
				<select id ="ttype" name="ttype">
					<option value="民">民</option>
					<option value="经">经</option>
					<option value=" ">空</option>
				</select>
				  字第<input id="trno" type="text" name="trno"/>号
				  <input type="hidden" name="RNo" value=""/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><input id="button-sub" type="submit" value="搜索" />&nbsp;&nbsp;&nbsp;&nbsp;
					<input id="button-res" type="reset" value="重置" /></td>
			</tr>
		</table>
	</form>
	<p style="color: red; text-align: center;">说明：不按公证人检索将其留空即可，不按案卷号检索将号码部分留空即可！</p>
	<%
		}
	%>
</body>
</html>