<%@page import="java.net.URLEncoder"%>
<%@page import="com.user.Tools"%>
<%@page import="com.user.User"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="com.user.EventType"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>类别管理</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/typemanage_css.css" rel="stylesheet" type="text/css" />

<script>
	function AreYouSure() {
		return confirm("你确定删除该子类型?");
	}

	function Check(f) {
		if (f.subtype.value == "") {
			alert("子类型不能为空!")
			return false;
		} 
		else if(f.subtype.value == "其它")
			{
			alert("待添加的子类型与默认项重复！")
			return false;
			}
			return confirm("请确认信息：\n1、父类型：" + f.fathertype.value + "\n2、子类型："
					+ f.subtype.value);
	}
</script>

</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		User us = (User) request.getSession().getAttribute("user");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			if ((us.getRole() & 1024) != 0)
			{
				ArrayList<EventType> tl = JdbcHelper.QueryAllType();
				if(tl==null)
					Tools.PrintErrorMsg(request,response, "查询子类型失败："+JdbcHelper.ErrorMsg);
				else{
		%>
	
	<div id="showtype">
		<div id="ms-area">
		<table class="bordered">
			<caption>民事案件类</caption>
			<thead>
				<tr>
					<th>一级类别名称</th>
					<th>二级类别名称</th>
					<%
						if ((us.getRole() & 2048) != 0)
						{
					%>
					<th>管理</th>
					<%
						}
					%>
				</tr>
			</thead>
			<%
				for (int i = 0; i < tl.size(); ++i)
				{
					if (tl.get(i).FatherType.equals("民事"))
					{
			%>
			<tr>
				<td>民事案件</td>
				<td><%=tl.get(i).ChildType%></td>
				<%
					if ((us.getRole() & 2048) != 0)
					{
						if(tl.get(i).ChildType.equals("其它"))
						{
				%>
				<td>默认项</td>
				<%}else{%>
				<td><a href="DeleteType?fa=<%=URLEncoder.encode("民事", "utf-8")%>
				&sub=<%=URLEncoder.encode(tl.get(i).ChildType,"utf-8")%>"
					onclick="return AreYouSure();">删除该项</a></td>
				<%
				}}
				%>
			</tr>
			<%
				}
						}
			%>
		</table>
		</div>
		<div id="jj-area">
		<table class="bordered">
			<caption>经济案件类</caption>
			<thead>
				<tr>
					<th>一级类别名称</th>
					<th>二级类别名称</th>
					<%
						if ((us.getRole() & 2048) != 0)
								{
					%>
					<th>管理</th>
					<%
						}
					%>
				</tr>
			</thead>
			<%
				for (int i = 0; i < tl.size(); ++i)
						{
							if (tl.get(i).FatherType.equals("经济"))
							{
			%>
			<tr>
				<td>经济案件</td>
				<td><%=tl.get(i).ChildType%></td>
				<%
					if ((us.getRole() & 2048) != 0)
									{
						if(tl.get(i).ChildType.equals("其它"))
						{
				%>
				<td>默认项</td>
				<%}else{ %>
				<td><a href="DeleteType?fa=<%=URLEncoder.encode("经济", "utf-8")%>
				&sub=<%=URLEncoder.encode(tl.get(i).ChildType,"utf-8")%>"
					onclick="return AreYouSure();">删除该项</a></td>
				<%
				}}
				%>
			</tr>
			<%
				}
						}
			%>
		</table>
		</div>
		<div id="sw-area">
		<table class="bordered">
			<caption>涉外案件类</caption>
			<thead>
				<tr>
					<th>一级类别名称</th>
					<th>二级类别名称</th>
					<%
						if ((us.getRole() & 2048) != 0)
								{
					%>
					<th>管理</th>
					<%
						}
					%>
				</tr>
			</thead>
			<%
				for (int i = 0; i < tl.size(); ++i)
						{
							if (tl.get(i).FatherType.equals("涉外"))
							{
			%>
			<tr>
				<td>涉外案件</td>
				<td><%=tl.get(i).ChildType%></td>
				<%
					if ((us.getRole() & 2048) != 0)
									{
						if(tl.get(i).ChildType.equals("其它"))
						{
				%>
				<td>默认项</td>
				<%}else{ %>
				<td><a href="DeleteType?fa=<%=URLEncoder.encode("涉外", "utf-8")%>
				&sub=<%=URLEncoder.encode(tl.get(i).ChildType,"utf-8")%>"
					onclick="return AreYouSure();">删除该项</a></td>
				<%
					}}
				%>
			</tr>
			<%
				}
						}
			%>
		</table>
		</div>
		<div id="qt-area">
		<table class="bordered">
			<caption>其它案件类</caption>
			<thead>
				<tr>
					<th>一级类别名称</th>
					<th>二级类别名称</th>
					<%
						if ((us.getRole() & 2048) != 0)
								{
					%>
					<th>管理</th>
					<%
						}
					%>
				</tr>
			</thead>
			<%
				for (int i = 0; i < tl.size(); ++i)
						{
							if (tl.get(i).FatherType.equals("其它"))
							{
			%>
			<tr>
				<td>其它案件</td>
				<td><%=tl.get(i).ChildType%></td>
				<%
					if ((us.getRole() & 2048) != 0)
									{
						if(tl.get(i).ChildType.equals("其它"))
						{
				%>
				<td>默认项</td>
				<%}else{ %>
				<td><a href="DeleteType?fa=<%=URLEncoder.encode("其它", "utf-8")%>
				&sub=<%=URLEncoder.encode(tl.get(i).ChildType,"utf-8")%>"
					onclick="return AreYouSure();">删除该项</a></td>
				<%
				}}
				%>
			</tr>
			<%
				}
			}
			%>
		</table>
		</div>
	</div>

	<%
				}
		if ((us.getRole() & 2048) != 0)
				{
	%>
	<div id="addtype">
		<form action="AddType" method="post" onsubmit="return Check(this);">
			<table class="bordered">
				<caption>添加子类型</caption>
				<tr>
					<th>一级类别：</th>
					<td><select name="fathertype">
							<option value="民事" selected="selected">民事案件</option>
							<option value="经济">经济案件</option>
							<option value="涉外">涉外案件</option>
							<option value="其它">其它类型</option>
					</select></td>
				</tr>
				<tr>
					<th>二级类别：</th>
					<td><input type="text" name="subtype" /></td>
				</tr>
				<tr>
					<td style="border-right: 0"></td>
					<td style="text-align: center; border-left: 0;"><input id="bt"
						type="submit" value="添加" /> <input id="bt" type="reset"
						value="重置" /></td>
				</tr>
			</table>
		</form>
	</div>
	<%
		}
			}
			else
				request.getRequestDispatcher("PermDen.html").forward(request, response);
		}
	%>
</body>
</html>