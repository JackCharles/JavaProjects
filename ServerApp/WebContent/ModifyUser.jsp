<%@ page import="java.util.ArrayList"%>
<%@ page import="com.user.User"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息修改</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="css/modifytable_css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function CheckInfo(f) 
	{
		if (f.userid.value == '' || f.username.value == ''|| f.position.value == '')
		{
			alert("基本信息不完整，请检查！");
			return false;
		}
		return confirm("请再次确认用户信息是否正确？");
	}
</script>
</head>


<body>
	<%
		User us = (User) session.getAttribute("user");
		String id = request.getParameter("eid");
		@SuppressWarnings("unchecked")
		ArrayList<User> li = (ArrayList<User>) request.getSession().getAttribute("userslist");
		if (us == null || li == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			User t = li.get(Integer.valueOf(id));
	%>
	<div id="aaaa"></div>
	<form action="ModifyUserInfo" method="post" id="modifyarea"
		onsubmit="return CheckInfo(this);">
		<input type="hidden" name="userid" value="<%=t.getUserId()%>" />

		<table class="bordered" id="modifytable" align="center">
			<caption>用户信息修改</caption>
			<tr>
				<th>用户ID</th>
				<td><%=t.getUserId()%></td>
				<th>用户名</th>
				<td><input id="inputbox" type="text" name="username"
					value="<%=t.getUserName()%>" /></td>
			</tr>
			<tr>
				<th>管理权限</th>
				<td id="rolebox" colspan="3">
					<div id="r1">
						<input type="checkbox" name="userrole" value="2"
							<%=(t.getRole() & 2) != 0 ? "checked=checked" : ""%> />上传案件<br />
						<input type="checkbox" name="userrole" value="4"
							<%=(t.getRole() & 4) != 0 ? "checked=checked" : ""%> /><span>审批案件</span><br />
						<input type="checkbox" name="userrole" value="8"
							<%=(t.getRole() & 8) != 0 ? "checked=checked" : ""%> /><span>删除案件</span>
					</div>
					<div id="r2">
						<input type="checkbox" name="userrole" value="16"
							<%=(t.getRole() & 16) != 0 ? "checked=checked" : ""%> />案件资料下载<br />
						<input type="checkbox" name="userrole" value="128"
							<%=(t.getRole() & 128) != 0 ? "checked=checked" : ""%> />查看所有用户<br />
						<input type="checkbox" name="userrole" value="512"
							<%=(t.getRole() & 512) != 0 ? "checked=checked" : ""%> /><span>修改用户资料</span>
					</div>
					<div id="r3">
						<input type="checkbox" name="userrole" value="32"
							<%=(t.getRole() & 32) != 0 ? "checked=checked" : ""%> />统计全部资源<br />
						<input type="checkbox" name="userrole" value="64"
							<%=(t.getRole() & 64) != 0 ? "checked=checked" : ""%> />个人信息修改<br />
						<input type="checkbox" name="userrole" value="1024"
							<%=(t.getRole() & 1024) != 0 ? "checked=checked" : ""%> />查看子类型
					</div>
					<div id="r4">
						<input type="checkbox" name="userrole" value="1"
							<%=(t.getRole() & 1) != 0 ? "checked=checked" : ""%> />查看/检索所有案件<br />
						<input type="checkbox" name="userrole" value="256"
							<%=(t.getRole() & 256) != 0 ? "checked=checked" : ""%> /><span>增加/删除用户</span><br />
						<input type="checkbox" name="userrole" value="2048"
							<%=(t.getRole() & 2048) != 0 ? "checked=checked" : ""%> /><span>添加/删除子类型</span>
					</div>
					<div id="r5">
						<input type="checkbox" name="userrole" value="4096"
							<%=(t.getRole() & 4096) != 0 ? "checked=checked" : ""%> />查看日志<br />
						<input type="checkbox" name="userrole" value="8192"
							<%=(t.getRole() & 8192) != 0 ? "checked=checked" : ""%> /><span>删除日志</span><br />
						<input type="checkbox" name="userrole" value="16384"
							<%=(t.getRole() & 16384) != 0 ? "checked=checked" : ""%> /><span>数据库备份</span>
					</div>
				</td>
			</tr>
			<tr>
				<th>性别</th>
				<td><select name="usersex" id="inputbox">
						<%
							if (t.getSex().equals("M"))
							{
						%>
						<option value='M' selected="selected">男</option>
						<option value='W'>女</option>
						<%
							}
							else
							{
						%>
						<option value='M'>男</option>
						<option value='W' selected="selected">女</option>
						<%
							}
						%>
				</select></td>
				<th>职位</th>
				<td><input id="inputbox" type="text" name="position"
					value="<%=t.getPosition()%>" /></td>
			</tr>
			<tr>
				<th>身份证号</th>
				<td><input id="inputbox" type="text" name="id"
					value="<%=t.getId()%>" /></td>
				<th>电话</th>
				<td><input id="inputbox" type="text" name="userphone"
					value="<%=t.getPhone()%>" /></td>
			</tr>
			<tr>
				<th>通讯地址</th>
				<td><input id="inputbox" type="text" name="useraddress"
					value="<%=t.getAddress()%>" /></td>
				<th style="color: red;">重置密码</th>
				<td><input id="inputbox" type="text" name="userpasswd"
					value="####" /></td>
			</tr>
			<tr>
				<%
					if ((us.getRole() & 512) != 0)
					{
				%>
				<td colspan="4"><input id="submitbutton" type="submit"
					value="提交修改">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="UserManager.jsp">返回用户列表</a></td>
				<%
					}
					else
					{
				%>
					<td colspan="4" style="color: red; font-size: 16px; text-align: center;">你没有修改其他用户资料的权限！</td>
				<%
					}
				%>
			</tr>
		</table>
	</form>
	<div id="attention">
		<p>1、重置密码项，仅供管理员统一修改密码，或找回密码使用。</p>
		<p>2、如无需修改密码，请不要更改此项，保留"####"字样即可！</p>
	</div>
	<div id="warning">提示：表中描红的权限请谨慎分配，因为一些权限的存在可能会影响到另外的权限，<br/>
		尤其是“添加/删除用户”和“修改用户资料”这两项，它们可以让该用户提升自己的<br/>
		权限，或创建一个更高权限的用户。因此，权限表中的大部分权限普通用户都不应具有。
	</div>
	<%
		}
	%>
</body>
</html>