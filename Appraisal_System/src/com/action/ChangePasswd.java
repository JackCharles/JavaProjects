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
 * Servlet implementation class ChangePasswd
 */
@WebServlet("/ChangePasswd")
public class ChangePasswd extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangePasswd()
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
		// 修改密码（通用）
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("session意外丢失，请重新登录!</p>");
			out.println("<p><a href=\'index.jsp\' title=\"重新登录\">点重新登录</a></p>");
			return;
		}
		String oldPass = MD5.getMd5(request.getParameter("oldpasswd"));
		String newPass = MD5.getMd5(request.getParameter("newpasswd1"));
		// String path = request.getRequestURI();
		boolean flag = JdbcHelper.ChangePassword(user.id, oldPass, newPass, "notsys");
		if (flag)// 修改成功
		{
			request.getSession().removeAttribute("user");// 重新登录
			out.println("<meta charset=\"utf-8\"><title>修改密码成功</title><p style=\"color:green\">☺");
			out.println("您已经成功修改密码，请重新登录！</p>");
			out.println("<p><a href=\'index.jsp\' title=\"重新登录\">点我重新登录</a></p>");
		}
		else
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println(JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'chpasswd.jsp\' title=\"返回上一页\">点我返回上一页</a></p>");
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
