package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mysql.JdbcHelper;

/**
 * Servlet implementation class CallAssess
 */
@WebServlet("/CallAssess")
public class CallAssess extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CallAssess()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String tno = request.getParameter("teacher_be_assess");
		String modelid = request.getParameter("mid");
		String starterid = request.getParameter("starterid");
		String stime = request.getParameter("start_time");
		String etime = request.getParameter("end_time");
		boolean s = JdbcHelper.UpdateStartAssess(tno, modelid, starterid, stime, etime);
		if (s)
		{
			out.println("<meta charset=\"utf-8\"><title>成功</title><p style=\"color:green\">☺");
			out.println("发起评教成功！</p>");
			out.println("<p><a href=\'callassess.jsp\' title=\"返回\">点我返回</a></p>");
		}
		else
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("发起评教失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'callassess.jsp\' title=\"返回\">点我重试</a></p>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
