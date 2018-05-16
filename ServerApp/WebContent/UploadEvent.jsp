<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.*"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	String uploadUrl = "http://" + request.getServerName()+request.getContextPath()+"/FileUpload";
	System.out.println("uploadURL:"+uploadUrl);
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	String today = df.format(new Date());// new Date()为获取当前系统时间
	User us = (User) session.getAttribute("user");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>案件上传</title>
<link href="css/fileupload_css.css" rel="stylesheet" type="text/css" />
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<link href="swfupload/css/default.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="swfupload/swfupload.js"></script>
<script type="text/javascript" src="swfupload/js/swfupload.queue.js"></script>
<script type="text/javascript" src="swfupload/js/fileprogress.js"></script>
<script type="text/javascript" src="swfupload/js/handlers.js"></script>
<script type="text/javascript" src="laydate/laydate.js"></script>
<script type="text/javascript" src="js/fileupload.js"></script>

<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/dialog.js"></script>
<link rel="stylesheet" href="css/ui-dialog.css" type="text/css" />

<!-- 这部分JS代码必须写在JSP中，因为用到Java变量 -->
<script type="text/javascript">
	//swfuplaod config
	var swfu;
	window.onload = function() {
		var settings = {
			flash_url : "swfupload/swfupload.swf",
			upload_url: "<%=uploadUrl%>",
			use_query_string : true,
			file_size_limit : 0,
			file_types : "*.*",
			file_types_description : "All Files",
			file_upload_limit : 0, //配置上传个数
			file_queue_limit : 0,
			custom_settings : {
				progressTarget : "fsUploadProgress",
				cancelButtonId : "btnCancel"
			},
			debug : false,
			// Button settings
			button_image_url : "swfupload/images/button.png",
			button_width : "100",
			button_height : "29",
			button_placeholder_id : "spanButtonPlaceHolder",
			button_text : '<span class="theFont">选择文件</span>',
			button_text_style : ".theFont { font-size: 16; }",
			button_text_left_padding : 12,
			button_text_top_padding : 3,
			button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor : SWFUpload.CURSOR.HAND,
			//handler			
			file_queued_handler : fileQueued,
			file_queue_error_handler : fileQueueError,
			file_dialog_complete_handler : fileDialogComplete,
			upload_start_handler : uploadStart,
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,
			queue_complete_handler : queueComplete
		};
		swfu = new SWFUpload(settings);
	};
</script>

</head>
<body>
	<%
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			//if ((us.getRole() & 2) != 0)
			if(true)
			{
		%>
	
	<div id="title">案件上传</div>
	<form id="form1" method="post" enctype="multipart/form-data"
		action="SaveEvent" onsubmit="return CheckInfo(this);">
		<div id="leftpart">
			<!-- 左侧部分 -->
			<table class="bordered">
				<tr>
					<th id="attt">注意事项</th>
					<td id="attt">
						<div id="attention">
							1.请确认浏览器安装并启用了flash插件。 <a
								href="https://get2.adobe.com/cn/flashplayer/" target="_blank">安装flash</a><br />
							2.上传流程：填写表单->选择文件->上传->完成。<br /> 
							3.<span style="color: red">文件名请勿包含下列特殊字符(&%^#@!`~\*/?)。</span><br/>
							3.<span style="color: red">上传期间请勿关闭或刷新本页面。</span><br/>
							5.<span style="color: red">如遇传输失败，请刷新页面并按步骤重试。</span>
						</div>
					</td>
				</tr>
				<tr>
					<th>案件日期</th>
					<td><input id="inputbox" name="Date" class="laydate-icon"
						onclick="laydate()" value="<%=today%>" readonly="readonly"/></td>
				</tr>
				<tr>
					<th>公证员(主)</th>
					<td><select id="selectbox" name="Handler1">
							<%
								ArrayList<User> li = JdbcHelper.QueryAllUsers();
								if(li==null)
									Tools.PrintErrorMsg(request,response, "查询用户失败："+JdbcHelper.ErrorMsg);
								for (int i = 0; i < li.size(); ++i)
								{
							%>
							<option value="<%=li.get(i).getUserName()%>"><%=li.get(i).getUserName() + "（" + li.get(i).getUserId() + "）"%></option>
							<%
								}
							%>
					</select></td>
				</tr>
				<tr>
					<th>公证员(副)</th>
					<td><select id="selectbox" name="Handler2">
							<option value="无">无</option>
							<%
								for (int i = 0; i < li.size(); ++i)
								{
							%>
							<option value="<%=li.get(i).getUserName()%>"><%=li.get(i).getUserName() + "（" + li.get(i).getUserId() + "）"%></option>
							<%
								}
							%>
					</select></td>
				</tr>
				<tr>
					<th>案件类型</th>
					<td><select id="typearea" name="Type"
						onchange="ShowSubType(this);">
							<option value="#" selected="selected">选择案件类型</option>
							<option value="民事">民事案件</option>
							<option value="经济">经济案件</option>
							<option value="涉外">涉外案件</option>
							<option value="其它">其它</option>
						</select> <select id="subtypearea" name="SubType">
							<option id="idle" value="#">选择案件类型</option>
							<%
								ArrayList<EventType> tl = JdbcHelper.QueryAllType();
								if(tl==null)
									Tools.PrintErrorMsg(request,response, "查询案件类型失败："+JdbcHelper.ErrorMsg);
								String ft;
								for (int i = 0; i < tl.size(); ++i)
								{
									if (tl.get(i).FatherType.equals("民事"))
										ft = "ms";
									else if (tl.get(i).FatherType.equals("经济"))
										ft = "jj";
									else if (tl.get(i).FatherType.equals("涉外"))
										ft = "sw";
									else
										ft = "other";
							%>
							<option class=<%=ft%> value=<%=tl.get(i).ChildType%>><%=tl.get(i).ChildType%></option>
							<%
								}
							%>
					</select>
					</td>
				</tr>
				<tr>
					<th>当事人管理</th>
					<td><input id="button" type="button" value="添加当事人"
						onclick="AddParty();" /> <input id="button" type="button"
						value="清空当事人" onclick="ClearParty();" /></td>
				</tr>
				<tr>
					<th>当事人列表</th>
					<td><textarea id="PartyBox" rows="5" name="party"
							readonly="readonly"></textarea></td>
				</tr>
				<tr>
					<th>案件备注</th>
					<td><textarea id="commemtbox" rows="5" name="Comment"></textarea></td>
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
			</table>
			<p style="color: red; font-size: 16px;">注意：不带案卷号上传则将号码栏留空即可！</p>
		</div>


		<div id="rightpart">
			<!-- 右侧部分 -->
			<table class="bordered">
				<tr>
					<td>
						<div class="fieldset flash" id="fsUploadProgress">
							<span class="legend">传输队列</span>
						</div>
						<p id="divStatus">0 个文件已上传</p>
					</td>
				</tr>
				<tr>
					<td id="bttt">
						<div>
							<span id="spanButtonPlaceHolder"></span> <input id="startupload"
								type="button" value="开始上传" onclick="CheckAndSetInfo();" /> <input
								id="btnCancel" type="button" value="取消所有上传"
								onclick="swfu.cancelQueue();" disabled="disabled" />
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<%
		}
			else
			{
				request.getRequestDispatcher("PermDen.html").forward(request, response);
			}
	}
	%>
</body>
</html>