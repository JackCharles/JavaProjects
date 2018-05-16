package com.user;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class DeleteType
 */
@WebServlet("/DeleteType")
public class DeleteType extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteType()
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
		String fa = URLDecoder.decode(request.getParameter("fa"), "utf-8");
		String sub = URLDecoder.decode(request.getParameter("sub"), "utf-8");

		if (JdbcHelper.DeleteType(fa, sub))
		{
			User us = (User) request.getSession().getAttribute("user");
			if (us == null)
			{
				request.getRequestDispatcher("TimeOut.html").forward(request, response);
				return;
			}
			String detials = "删除的类型：" + fa + "-" + sub;
			JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "删除子类型", detials);
			request.getRequestDispatcher("TypeManager.jsp").forward(request, response);
		}
		else
			Tools.PrintErrorMsg(request, response, "删除类型失败：" + JdbcHelper.ErrorMsg);
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
