<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.sun.glass.ui.CommonDialogs.Type"%>
<%@page import="java.net.URLDecoder"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.user.*"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="java.util.ArrayList"%>

<%
	User us = (User) session.getAttribute("user");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看全部案件</title>
<link href="css/table_css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/dialog.js"></script>
<link rel="stylesheet" href="css/ui-dialog.css" type="text/css" />

<script type="text/javascript">
		function SetRno(date, serial, handler,type, page)
		{
			dialog({
				title:'填写案卷号',
				content:'（<input id="tyear" type="text" value="'+new Date().getFullYear()+'" style="width:40px;"/>）吉长国安证 '+  
				'<select id ="ttype" style="height:28px; font-size:16px;">'+
				'<option value="民">民</option>'+
				'<option value="经">经</option>'+
				'<option value=" ">空</option>'+
				'</select>'+
			  	' 字第<input id="trno" type="text" style="width:100px;"/>号',
				statusbar:'无',
				button:[
					{
						value:'添加案卷号',
						autofocus:true,
						callback:function(){
							var tyear = document.getElementById("tyear").value;
							var ttype = document.getElementById("ttype").value;
							var trno = document.getElementById("trno").value;
							if(!/(\d{4})/.test(tyear))
							{
								this.statusbar("年份不合法！");
								return false;
							}
							if(!/^\d+[-]{0,1}\d+$/.test(trno))
							{
								this.statusbar("号码部分不合法！");
								return false;
							}
							else
							{
								var temp = document.createElement("form");
								temp.action = "AddRno";
								temp.method = "post";
								temp.style.display = "none";
								var params = {"date":date,
									"serial":serial,
									"handler":handler,
									"rno":"（"+tyear+"）"+"吉长国安证 "+ttype+" 字第"+trno+"号",
									"type":type,
									"page":page
								};
								for (var x in params) {
									var opt = document.createElement("input");
								    opt.name = x;
								    opt.value = params[x];
								    temp.appendChild(opt);
								}
								document.body.appendChild(temp);
								temp.submit();
								temp.parentNode.removeChild(temp);
								return true;
							}
						}
					},
					{
						value:'取消添加'
					}
				]
			}).showModal();	
		}
	</script>

</head>


<body>
	<%
		if (us == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			String type = URLDecoder.decode(request.getParameter("type"),"utf-8");
			int pageno = Integer.valueOf(request.getParameter("page"));
			System.out.println(type+"---"+pageno);
			String TypeZh="";
			if(type.equals("N"))
				TypeZh = "未审批";
			else if(type.equals("Y"))
				TypeZh = "已审批";
			else
				TypeZh = type;
			ArrayList<Event> li;
			if ((us.getRole() & 1) != 0)
				li = JdbcHelper.GetSearchRes(null, null, null, null, null, type, pageno);
			else//只能查看自己的案件
			{
	%>
				<p style="color: red; font-size: 16px; text-align: center;">你没有查看/检索所有资料的权限，该部分仅显示你自己的案件！</p>
	<%
				li = JdbcHelper.GetSearchRes(null, us.getUserName(), null, null, null, type, pageno);
			}
			if (li == null)
				Tools.PrintErrorMsg(request,response, "查询案件失败："+JdbcHelper.ErrorMsg);
			else
			{
				request.getSession().setAttribute("events", li);
				int count = li.size();
	%>

	<div align="right"><a href="Print.jsp?title=<%=URLEncoder.encode(TypeZh+"案件（全部案件）", "utf-8")%>
	&type=<%=URLEncoder.encode(type, "utf-8")%>" target="_blank">打印该类型所有案件</a></div>
	<table class="bordered">
		<caption>
			全部案件<%="（" + TypeZh + "案件）"%>
		</caption>
		<thead>
			<tr>
				<th>日期</th>
				<th>流水号</th>
				<th>公证员</th>
				<th>副手</th>
				<th>案件类型</th>
				<th>案卷号</th>
				<th>当事人</th>
				<th>审批</th>
				<th>审批人</th>
				<th>详情</th>
				<%
				if ((us.getRole() & 8) != 0)
				{
				%>
				<th>删除</th>
				<%
				}
				if ((us.getRole() & 4) != 0)
				{
				%>
				<th>审批</th>
				<%
				}
				%>
			</tr>
		</thead>
			<%
			for (int i = 0; i < count; ++i)
			{
				String ty =li.get(i).type + "-" + li.get(i).subtype;
				String rno = li.get(i).rno;
				String comment = li.get(i).comment;
				List<User> parties = JdbcHelper.GetParties(li.get(i).date, li.get(i).serial, li.get(i).handler1);
				String partyStr="";
				for(int ia=0; ia<parties.size();++ia)
					partyStr += parties.get(ia).getUserName()+", ";
			%>
		<tr>
			<td><%=li.get(i).date%></td>
			<td><%=li.get(i).serial%></td>
			<td><%=li.get(i).handler1%></td>
			<td><%=li.get(i).handler2%></td>
			<td><%=ty.length()>8?ty.substring(0,8)+"...":ty%></td>
			<td><%=rno.equals("")?"":rno.substring(1,5)+" - "+rno.substring(12,13)+" - "+rno.substring(16,rno.length()-1)%></td>
			<td><%=partyStr.length()>8?partyStr.substring(0,8):partyStr %></td>
			<!-- <td></td> -->
			<td>
				<%
				if (li.get(i).confirm.equals("N"))
				{
				%> <span style="color: red">未审批</span> <%
 				}
 				else
 				{
 				%> <span>已审批</span> <%
 				} //for
 				%>
			</td>
			<td><%=li.get(i).approval%></td>
			<td><a href="ViewDetails.jsp?eid=<%=i%>">详情</a></td>
			<%
				if ((us.getRole() & 8) != 0)
				{
			%>
			<td style="color: red;"><a
				href="DeleteEvent.jsp?eid=<%=i%>&type=<%=URLEncoder.encode(type,"utf-8")%>&page=<%=pageno%>"
				onclick="return confirm('你确定要删除吗？此操作无法撤销！');">删除</a></td>
			<%
				}
				if ((us.getRole() & 4) != 0)
				{
			%>
			<td>
				<%
					if (li.get(i).confirm.equals("N"))
					{
				%> <a href="#"
				onclick="SetRno('<%=li.get(i).date%>',
					<%=li.get(i).serial%>,
					'<%=li.get(i).handler1%>',
					'<%=type%>',
					'<%=pageno%>');">审批</a>
				<%
					}
					else
					{
				%> 无 <%
					}
				%>
			</td>
			<%
			}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<p style="text-align: center; font-size: 16px;">
		本页共<%=count%>条数据！（说明：案卷号进行了简化展示，对应关系为"年份 - 类型 - 案卷号码"）
	</p>
	<p style="text-align: center;">
		<%
			if (pageno > 1)
			{
		%>		<a href="ViewAll.jsp?type=<%=URLEncoder.encode(type,"utf-8")%>&page=1">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="ViewAll.jsp?type=<%=URLEncoder.encode(type,"utf-8")%>&page=<%=pageno - 1%>">上一页</a>
		<%
			}
		%>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			if (count == 20)
			{
		%>		
				<a href="ViewAll.jsp?type=<%=URLEncoder.encode(type,"utf-8")%>&page=<%=pageno + 1%>">下一页</a>
		<%
			}
		%>
	</p>
	<%
		}
		}
	%>
</body>
</html>