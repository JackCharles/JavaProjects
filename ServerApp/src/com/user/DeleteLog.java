package com.user;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class DeleteLog
 */
@WebServlet("/DeleteLog")
public class DeleteLog extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteLog()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String startDate = request.getParameter("StartDate");
		String endDate = request.getParameter("EndDate");
		User us = (User) request.getSession().getAttribute("user");
		if (us == null)
		{
			request.getRequestDispatcher("TimeOut.html").forward(request, response);
			return;
		}

		if (JdbcHelper.DeleteLog(startDate, endDate))
		{
			String detials = "删除日志：" + startDate + " 至 " + endDate + " 的日志";
			JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "删除日志", detials);

			RequestDispatcher disp = request
					.getRequestDispatcher("ViewAllLog.jsp?type=" + URLDecoder.decode("全部事件", "utf-8") + "&page=1");
			disp.forward(request, response);
		}
		else
			Tools.PrintErrorMsg(request, response, JdbcHelper.ErrorMsg);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
