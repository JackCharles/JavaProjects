<%@page import="com.user.Tools"%>
<%@page import="com.jdbc.JdbcHelper"%>
<%@page import="com.user.User"%>
<%@page import="java.net.URLDecoder"%>
<%@page language="java" contentType="application/x-msdownload" pageEncoding="UTF-8"%> 
<%@page import="java.net.URLEncoder"%>
<%@page import = "java.io.FileInputStream" %>

<%
	User us = (User) request.getSession().getAttribute("user");
	if (us == null)
		request.getRequestDispatcher("TimeOut.html").forward(request, response); 
	else
	{
		response.reset();//可以加也可以不加  
		response.setContentType("application/x-download");
		String filedownload = URLDecoder.decode(request.getParameter("filename"), "utf-8");
		String filedisplay = filedownload.substring(filedownload.lastIndexOf('\\') + 1);
		//写日志
		String details = "日期：" + request.getParameter("date") + " 公证员：" + URLDecoder.decode(request.getParameter("handler"),"utf-8")
				+ " 流水号：" + request.getParameter("serial") + " 文件名：" + filedisplay;
		JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "下载案件", details);

		filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);

		java.io.OutputStream outp = null;
		java.io.FileInputStream in = null;
		try
		{
			outp = response.getOutputStream();
			in = new FileInputStream(filedownload);

			byte[] b = new byte[1024];
			int i = 0;

			while ((i = in.read(b)) > 0)
				outp.write(b, 0, i);
			outp.flush();
			out.clear();
			out = pageContext.pushBody();
		} catch (Exception e)
		{
			System.out.println("download Error!");
		} finally
		{
			if (in != null)
			{
				in.close();
				in = null;
			}
		}
	}
%> 