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
 * Servlet implementation class SubmitAssess
 */
@WebServlet("/SubmitAssess")
public class SubmitAssess extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubmitAssess()
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
		// 提交评价
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		boolean isError = false;// 检测参数传递是否有错
		String sid = request.getParameter("sid");
		String tid = request.getParameter("tid");
		String qsnum = request.getParameter("qsnum");
		if (sid == null || tid == null || qsnum == null)
			isError = true;
		int n = Integer.parseInt(qsnum);
		String answer[] = new String[n];// 答案数组
		for (int i = 1; i <= n; i++)
		{
			answer[i - 1] = request.getParameter("answer" + i);
			if (answer[i - 1] == null)
			{
				isError = true;
				break;
			}
		}
		if (isError)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("提交失败，你有未做选择的题目或系统错误！</p>");
			out.println("<p><a href=\'student.jsp\' title=\"返回\">点我重试</a></p>");
			return;
		}
		else
		{
			// 写入数据库
			boolean x = JdbcHelper.SubmitAssess(sid, tid, answer);
			if (x)
			{
				out.println("<meta charset=\"utf-8\"><title>评教成功</title><p style=\"color:green\">☺");
				out.println("提交成功，请返回！</p>");
				out.println("<p><a href=\'student.jsp\' title=\"返回\">点我返回</a></p>");
			}
			else
			{
				out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
				out.println("提交失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
				out.println("<p><a href=\'student.jsp\' title=\"返回\">点我返回</a></p>");
			}
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
