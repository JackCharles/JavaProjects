<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<link rel="shortcut icon" href="favicon.ico"/>
<link rel="Bookmark" href="favicon.ico"/>
<style>
body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, form,
	fieldset, input, p, blockquote, th, td {
	margin: 0;
	padding: 0;
	border: none;
}

body {
	font-size: 12px;
	font-family: Cambria, "Hoefler Text", "Liberation Serif", Times,
		"Times New Roman", serifbackground : #fff;
	color: #2b2b2b;
	width: 100%;
	FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
		startColorStr=#113562, endColorStr=#226cc5); /*IE 6 7 8*/
	background: -ms-linear-gradient(top, #113562, #226cc5); /* IE 10 */
	background: -moz-linear-gradient(top, #113562, #226cc5); /*火狐*/
	background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#113562),
		to(388df3)); /*谷歌*/
	background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#113562),
		to(#226cc5)); /* Safari 4-5, Chrome 1-9*/
	background: -webkit-linear-gradient(top, #113562, #226cc5);
	/*Safari5.1 Chrome 10+*/
	background: -o-linear-gradient(top, #113562, #226cc5); /*Opera 11.10+*/
}

address, caption, cite, code, dfn, em, strong, th, var {
	font-style: normal;
	font-weight: normal;
}

select, input, img {
	vertical-align: middle;
}

table {
	border-collapse: collapse;
	border-spacing: 0;
}

table, td, tr, th {
	font-size: 12px;
}

a {
	text-decoration: none;
	cursor: pointer;
}

fieldset, img {
	border: 0;
}

.main {
	position: absolute;
	left: 50%;
	top: 50%;
	background: url(images/login.png) no-repeat;
	width: 772px;
	height: 468px;
	margin: -234px 0 0 -386px;
}

.input-box {
	position: absolute;
	top: 110px;
	left: 410px;
	color: #0952a1;
}

.input-box input {
	border: 1px solid #7491b5;
	width: 160px;
	height: 30px;
	background-color: #f5fafe;
	padding-left: 4px;
	line-height: 28px;
}

.input-box p {
	line-height: 40px;
}

.input-box .record {
	margin-left: 48px;
}

.input-box .record input {
	width: 15px;
	height: 15px;
}

.input-box .link {
	margin-top: 14px;
	margin-left: 70px;
}

a {
	height: 28px;
	width: 72px;
	display: inline-block;
	color: #fff;
	line-height: 28px;
	text-align: center;
	margin-top: 10px;
}

#log {
	background: url(images/log.png) no-repeat;
	margin-right: 12px;
	margin-left: 48px;
	width: 72px;
	height: 28px;
	border-style: none;
}

#reset {
	background: url(images/reset.png) no-repeat;
	color: #6d6d6d;
	width: 72px;
	height: 28px;
	border-style: none;
}

.main-box .copy {
	text-align: center;
	margin-top: 8px;
	color: #666666;
}

.text {
	text-align: center;
	color: #fff;
	margin-top: 100px;
	line-height: 24px;
	font-size: 14px;
}

#error{
	margin-top: 300px;
	text-align: center;
	font-size: 30px;
	color: white;
}
#error a{width:500px;}
</style>

<script type="text/javascript">
	function setCookie(c_name, value, expiredays){
		var exdate=new Date();
		exdate.setDate(exdate.getDate() + expiredays);
		document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
	}


	function getCookie(c_name)
	{
		if (document.cookie.length>0)
		{
			c_start=document.cookie.indexOf(c_name + "=")
		if (c_start!=-1)
		{ 
			c_start=c_start + c_name.length+1;
			c_end=document.cookie.indexOf(";",c_start);
			if (c_end==-1)
				c_end=document.cookie.length;
			return unescape(document.cookie.substring(c_start,c_end))
		} 
		}
	return "";
	}　　

	window.onload = function(){
		var usn = getCookie("username");
		var psd = getCookie("password");
		if(usn!=""&&psd!="")
		{
			document.getElementById("USN").value=usn;
			document.getElementById("PSD").value=psd;
		}
	}

	function Check(form) {
		if (form.UserName.value.length <= 0 || form.Password.value.length <= 0) {
			alert("用户名或密码不能为空！");
			return false;
		}
		if (form.UserName.value.length >=15) {
			alert("用户名太长！");
			return false;
		}
		if(form.record.checked)
		{
			setCookie('username', form.UserName.value, 15);
			setCookie('password', form.Password.value, 15);
		}
		return true;
	}
</script>

<body style="background-color: #3987cf;">

	<%
		String res = request.getParameter("reason");
		if (res != null)
			res = URLDecoder.decode(res, "utf-8");
		else
			res = "";
	%>
	<div class="main" id="notie">
		<div class="login-box">
			<div class="input-box">
				<form action="UserLogin" method="post" onsubmit="return Check(this)">
					<p>
						用户名：<input id="USN" type="text" name="UserName" value=""
							class="user" />
					</p>
					<p>
						<span style="padding-right: 12px;">密</span>码：<input id="PSD"
							type="password" class="password" name="Password" value="" />
					</p>
					<p>
						<span class="record">记住密码：<input type="checkbox"
							name="record" /></span>
					</p>
					<p>
						<input type="submit" id="log" value="登&nbsp;录" /> <input
							type="reset" id="reset" value="重&nbsp;置" />
					</p>
				</form>
			</div>
			<p
				style="color: red; font-size: 16px; padding-top: 290px; padding-left: 420px;"><%=res%></p>
		</div>

		<div class="text">2017 长春市软航信息技术有限公司，设计开发。</div>
	</div>

	<div id="error">
		该系统对IE支持较差，请更换浏览器，<br/>
		推荐Chrome、Firfox、360浏览器等新产品！<br/>
		<a href = "<%="http://"+request.getServerName()+request.getContextPath()+"/ClientApp/chrome.exe"%>">点击此处下载Chrom浏览器</a>
	</div>

	<script type="text/javascript">
	
	if (!!window.ActiveXObject || "ActiveXObject" in window)
	{
		document.getElementById("notie").style.display="none";
	}
	else
		document.getElementById("error").style.display="none";
	</script>
</body>
</html>
