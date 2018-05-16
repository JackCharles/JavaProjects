package com.user;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class AddRno
 */
@WebServlet("/AddRno")
public class AddRno extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddRno()
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
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		String date = request.getParameter("date");
		String serial = request.getParameter("serial");
		String handler = request.getParameter("handler");
		String rno = request.getParameter("rno");
		String type = request.getParameter("type");// if type is null, that means this request from search page
		String page = request.getParameter("page");

		// for return search page
		String tdate = request.getParameter("tdate");
		String thandler = request.getParameter("thandler");
		String tparty = request.getParameter("tparty");
		String tpartyid = request.getParameter("tpartyid");

		User us = (User) request.getSession().getAttribute("user");
		if (us == null)
		{
			request.getRequestDispatcher("TimeOut.html").forward(request, response);
			return;
		}

		if (JdbcHelper.AddRno(date, Integer.valueOf(serial), handler, rno, us.getUserName()))
		{
			String detials = "案件日期：" + date + " 公证员：" + handler + " 流水号：" + serial + " 案卷号：" + rno;
			JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "审批案件", detials);

			RequestDispatcher disp;
			if (type == null)// from search page
				disp = request.getRequestDispatcher("SearchAndShow.jsp?Date=" + tdate + "&Handler="
						+ URLEncoder.encode(thandler, "utf-8") + "&Party=" + URLEncoder.encode(tparty, "utf-8")
						+ "&Partyid=" + tpartyid + "&page=" + page);
			else
				disp = request
						.getRequestDispatcher("ViewAll.jsp?type=" + URLEncoder.encode(type, "utf-8") + "&page=" + page);
			disp.forward(request, response);
		}
		else
			Tools.PrintErrorMsg(request, response, "添加案卷号失败：" + JdbcHelper.ErrorMsg);
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
