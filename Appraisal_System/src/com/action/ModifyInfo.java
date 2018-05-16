package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mysql.JdbcHelper;
import com.user.UserBean;

/**
 * Servlet implementation class ModifyInfo
 */
@WebServlet("/ModifyInfo")
public class ModifyInfo extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModifyInfo()
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
		// 修改用户信息
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String newphone = request.getParameter("phone");
		String newaddress = request.getParameter("address");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("session意外丢失，请重新登录!</p>");
			out.println("<p><a href=\'index.jsp\' title=\"重新登录\">点我重新登录</a></p>");
			return;
		}

		user.phone = newphone;
		user.address = newaddress;
		boolean flag = JdbcHelper.ModifyInfo(user);
		if (flag)// 成功
		{
			request.getSession().setAttribute("user", user);
			out.println("<meta charset=\"utf-8\"><title>修改信息成功</title><p style=\"color:green\">☺");
			out.println("您已经成功个人信息，返回刷新即可查看！</p>");
			out.println("<p><a href=\'viewinfo.jsp\' title=\"返回\">点我返回</a></p>");
		}
		else
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println(JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'modifyinfo.jsp\' title=\"返回上一页\">点我返回上一页</a></p>");
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
