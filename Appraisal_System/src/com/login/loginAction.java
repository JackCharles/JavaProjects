package com.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.action.MD5;
import com.mysql.JdbcHelper;
import com.user.UserBean;

/**
 * Servlet implementation class loginAction
 */
@WebServlet("/loginAction")
public class loginAction extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public loginAction()
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		UserBean user = new UserBean();
		String role = null;

		// 验证用户登录
		String UserId = request.getParameter("userid");
		Pattern pattern = Pattern.compile("^\\d{4}$");
		if (!pattern.matcher(UserId).matches())
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("请不要变着法子进行SQL注入！！！</p>");// 输出错误信息
			out.println("<a href=\"index.jsp\">返回重新登录</a>");
			return;
		}
		String Password = request.getParameter("password");
		String enpass = MD5.getMd5(Password);
		if (enpass != null)
			role = JdbcHelper.VerifyUser(UserId, enpass);
		else
			role = null;

		if (role != null)
		{
			user = JdbcHelper.queryUserInfo(UserId, role);// 获取整个用户信息存在user中
			request.getSession().setAttribute("user", user);// 将user对象放到session中

			if (role.equals("SYSADMIN"))// system administrator
				response.sendRedirect("sysadmin.jsp");
			else if (role.equals("ASSADMIN"))// assess administrator
				response.sendRedirect("assadmin.jsp");
			else if (role.equals("TEACHER"))
				response.sendRedirect("teacher.jsp");
			else if (role.equals("STUDENT"))
				response.sendRedirect("student.jsp");
		}
		else
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println(JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");// 输出错误信息
			if (JdbcHelper.ErrorCode == 2)
				out.println("<p>☛所有人初始密码为你的学号或工号！</p>");// 提示密码
			out.println("<a href=\"index.jsp\">重试</a>");
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
