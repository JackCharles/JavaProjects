<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.user.Tools"%>
<%@page import="com.jdbc.JdbcConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.user.User"%>
<%@ page import="com.jdbc.JdbcHelper"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.user.Event"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除案件</title>
</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		User us = (User) session.getAttribute("user");
		
		String type = request.getParameter("type");//zh
		String eid = request.getParameter("eid");//en
		String pageno = request.getParameter("page");//en

		String tdate = request.getParameter("Date");//en
		String thandler = request.getParameter("Handler");//zh
		String tparty = request.getParameter("Party");//zh
		String tpartyid = request.getParameter("Partyid");//en
		String trno = request.getParameter("RNo");//en
		
		@SuppressWarnings("unchecked")
		ArrayList<Event> li = (ArrayList<Event>) request.getSession().getAttribute("events");
		
		if (us == null || li == null)
			request.getRequestDispatcher("TimeOut.html").forward(request, response); 
		else
		{
			int i = Integer.valueOf(eid);
			if (JdbcHelper.DeleteEvent(li.get(i).date, li.get(i).serial, li.get(i).handler1))
			{
				String detials = "案件日期：" + li.get(i).date + " 公证员：" + li.get(i).handler1 + " 流水号："
						+ li.get(i).serial;
				JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "删除案件", detials);
				
				RequestDispatcher disp;
				if (type != null)//from viewall
					disp = request.getRequestDispatcher("ViewAll.jsp?type=" + type + "&page=" + pageno);
				else
					disp = request.getRequestDispatcher("SearchAndShow.jsp?Date=" + tdate + "&Handler=" + thandler + "&Party=" + tparty + "&Partyid=" + tpartyid + "&RNo=" + trno + "&page=" + pageno);
				disp.forward(request, response);
			}
			else
			{
				if (type != null)
					Tools.PrintErrorMsg(request ,response, "删除案件失败："+JdbcHelper.ErrorMsg);
				else
					Tools.PrintErrorMsg(request, response, "删除案件失败："+JdbcHelper.ErrorMsg);
			}
		}
	%>
</body>
</html>