<%@page import="com.user.Tools"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.User"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/adduser_css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/adduser.js"></script>
</head>


<body>
	<%
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		User us = (User) session.getAttribute("user");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			if ((us.getRole() & 128) == 0)
				request.getRequestDispatcher("PermDen.html").forward(request, response);
			else
			{
				ArrayList<User> li = JdbcHelper.QueryAllUsers();
				if (li == null)
					Tools.PrintErrorMsg(request,response, "查询用户失败："+JdbcHelper.ErrorMsg);
				else
				{
					request.getSession().setAttribute("userslist", li);
	%>
	<table class="bordered">
		<caption>用户信息管理</caption>
		<thead>
			<tr>
				<th>ID</th>
				<th>姓名</th>
				<th>性别</th>
				<th>职位</th>
				<th>身份证号</th>
				<th>电话</th>
				<th>通讯地址</th>
				<th>详情/修改</th>
				<%
					if ((us.getRole() & 256) != 0)
								{
				%>
				<th>删除</th>
				<%
					}
				%>
			</tr>
		</thead>
		<%
			for (int i = 0; i < li.size(); ++i)
						{
		%>
		<tr>
			<td><%=li.get(i).getUserId()%></td>
			<td><%=li.get(i).getUserName()%></td>
			<td><%=li.get(i).getSex().equals("M") ? "男" : "女"%></td>
			<td><%=li.get(i).getPosition()%></td>
			<td><%=li.get(i).getId()%></td>
			<td><%=li.get(i).getPhone()%></td>
			<td><%=li.get(i).getAddress()%></td>
			<td><a href="ModifyUser.jsp?eid=<%=i%>">详情/修改</a></td>
			<%
				if ((us.getRole() & 256) != 0)
				{
					if (!li.get(i).getUserId().equals(us.getUserId()))
					{
			%>
			<td><a href="DeleteUser.jsp?id=<%=li.get(i).getUserId()%>" onclick="return AreYouSure();">删除</a> <%
 					}
 					else
 					{
 %>
			<td style="color: green">自己</td>
			<%
					}
				}
			%>
		</tr>
		<%
			} //for
		%>
	</table>
	<%
		}
			}
			if ((us.getRole() & 256) != 0)
			{
	%>

	<div id="btarea">
		<input id="addbutton" type="button" value="添加新用户" onclick="AddUser();" />
	</div>
	<div id="adduser">
		<form action="AddUser" method="post" onsubmit="return check(this);">
			<table class="bordered" align="center">
				<tr>
					<th>用户账号：</th>
					<td><input type="text" name="userid" /></td>
					<th>用户姓名：</th>
					<td><input type="text" name="username" /></td>
				</tr>
				<tr>
					<th>管理权限：</th>
					<td id="rolebox" colspan="3">
						<div id="r1">
							<input type="checkbox" name="userrole" value="2" />上传案件<br/> 
							<input type="checkbox" name="userrole" value="4" /><span>审批案件</span><br/>
							<input type="checkbox" name="userrole" value="8" /><span>删除案件</span>
						</div>
						<div id="r2">
							<input type="checkbox" name="userrole" value="16" />案件资料下载<br />
							<input type="checkbox" name="userrole" value="128" />查看所有用户<br />
							<input type="checkbox" name="userrole" value="512" /><span>修改用户资料</span>
						</div>
						<div id="r3">
							<input type="checkbox" name="userrole" value="32" />统计全部资源<br />
							<input type="checkbox" name="userrole" value="64" />个人信息修改<br />
							<input type="checkbox" name="userrole" value="1024" />查看子类型
						</div>
						<div id="r4">
							<input type="checkbox" name="userrole" value="1" />查看/检索所有案件<br />
							<input type="checkbox" name="userrole" value="256" /><span>添加/删除用户</span><br />
							<input type="checkbox" name="userrole" value="2048" /><span>添加/删除子类型</span>
						</div>
						<div id="r5">
							<input type="checkbox" name="userrole" value="4096" />查看日志<br />
							<input type="checkbox" name="userrole" value="8192" /><span>删除日志</span><br />
							<input type="checkbox" name="userrole" value="16384" /><span>数据库备份</span>
						</div>
					</td>
				</tr>
				<tr>
					<th>用户性别：</th>
					<td><select name="usersex">
							<option value='M' selected="selected">男</option>
							<option value='W'>女</option>
					</select></td>
					<th>用户职位：</th>
					<td><input type="text" name="position" /></td>
				</tr>
				<tr>
					<th>身份证号：</th>
					<td><input type="text" name="id" /></td>
					<th>联系电话：</th>
					<td><input type="text" name="userphone" /></td>
				</tr>
				<tr>
					<th>通讯地址：</th>
					<td><input type="text" name="useraddress" /></td>
					<th>初始密码：</th>
					<td><input type="text" name="userpasswd" /><br /> <span>此处留空则用户ID充当默认密码</span>
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: center;"><input id="button"
						type="submit" value="添加用户" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input id="button"
						type="reset" value="重置" /></td>
				</tr>
			</table>
		</form>
		<div id="warning">提示：表中描红的权限请谨慎分配，因为一些权限的存在可能会影响到另外的权限，<br/>
			   尤其是“添加/删除用户”和“修改用户资料”这两项，它们可以让该用户提升自己的<br/>
			   权限，或创建一个更高权限的用户。因此，权限表中的大部分权限普通用户都不应具有。
		</div>
	</div>
	<%
		}
		}
	%>
</body>
</html>