<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.*"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="shortcut icon" href="favicon.ico"/>
<link rel="Bookmark" href="favicon.ico"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>主页</title>
<link href="css/mainpage_css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/mainpage.js"></script>
<script type="text/javascript" src="js/qrcode.min.js"></script>

</head>

<body>
	<%
		User us = (User) session.getAttribute("user");
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月dd日  E");
	%>

	<div id="box">
		<!-- 1 -->
		<div id="welcome">
			<div id="welcome-user">
				您好，<%=us.getUserName()+"（"+us.getUserId()+"）" %><br/>
				<%=ft.format(new Date()) %>&nbsp;
			</div>
		</div>

		<div id="header">
			<!-- 2 -->
			<ul>
				<li><a href="#" onclick="SearchEvent();">案件检索</a></li>
				<li><a href="#" onclick="UploadEvent();">案件上传</a></li>
				<li><a href="#">全部案件</a>
					<ul>
						<li><a href="#" onclick="ViewAllEvent();">全部案件</a></li>
						<li><a href="#" onclick="ViewMS();">民事案件</a></li>
						<li><a href="#" onclick="ViewJJ();">经济案件</a></li>
						<li><a href="#" onclick="ViewSW();">涉外案件</a></li>
						<li><a href="#" onclick="ViewOther();">其他案件</a></li>
						<li><a href="#" onclick="ViewConfirm();">已审批案件</a></li>
						<li><a href="#" onclick="ViewNotConf();" class="last">未审批案件</a></li>
					</ul></li>
				<li><a href="#" onclick="Statistic();">资源统计</a></li>
				<li><a href="#">系统管理</a>
					<ul>
						<li><a href="#" onclick="UserManager();">用户管理</a></li>
						<li><a href="#" onclick="TypeManager();">类别管理</a></li>
						<li><a href="#" onclick="ShowOptLogs();">日志管理</a></li>
						<li><a href="#" onclick="BackupDatabase();">数据库备份</a></li>
						<li><a href="#" onclick="AboutSystem();" class="last">关于系统</a></li>
					</ul>
				</li>
				<li><a href="#" onclick="SelfInfoModify();">个人信息</a></li>
				<li><a href="Logout.jsp" class="last">退出系统</a></li>
			</ul>
		</div>

		<div id="body">
			<!-- 3 -->
			<div id="welcom-page">
				<div id="text">扫码下载客户端APP</div>
				<div id="qrcode"></div>
				<script>
				var qrcode = new QRCode('qrcode', {
					  text: '<%="http://"+request.getServerName()+
							  request.getContextPath()+"/ClientApp/client_app.apk"%>',
					  width: 150,
					  height: 150,
					  colorDark: '#000000',
					  colorLight: '#ffffff',
					  correctLevel: QRCode.CorrectLevel.H
					});
				</script>
			</div>
			
			<div id="search-event"></div>

			<div id="upload-event"></div>

			<div id="viewall-event"></div>

			<div id="statistic"></div>

			<div id="selfinfo-modify"></div>

			<div id="system-admin"></div>

			<%
				}
			%>

		</div>
	</div>
	<div id="cop">2017 长春市软航信息技术有限公司，设计开发。</div>
</body>

</html>