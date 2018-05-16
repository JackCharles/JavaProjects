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
 * Servlet implementation class AddUser
 */
@WebServlet("/AddUser")
public class AddUser extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddUser()
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		int flag = 1;
		UserBean user = new UserBean();
		user.role = request.getParameter("role");
		user.id = request.getParameter("id");
		user.name = request.getParameter("name");
		user.sex = request.getParameter("sex");
		user.phone = request.getParameter("phone");
		user.address = request.getParameter("address");
		if (!user.id.matches("^\\d{4}$") || user.name == null)
			flag = -1;
		if (user.role.equals("STUDENT"))
		{
			user.Sclass = request.getParameter("Sclass");
			if (!user.Sclass.matches("^\\d{2}$"))
				flag = -1;
		}

		else if (user.role.equals("TEACHER"))
		{
			user.courseId = request.getParameter("course");
			String temp[] = request.getParameter("Tclass").split(",");
			for (int i = 0; i < temp.length; i++)
				user.Tclass.add(temp[i]);
			if (!user.courseId.matches("^\\d{4}$"))
				flag = -1;
		}
		if (flag == -1)
		{
			out.println("Input Error!");
			return;
		}
		boolean b = JdbcHelper.AddUser(user);
		if (b)
		{
			out.println("<meta charset=\"utf-8\"><title>添加用户成功</title><p style=\"color:green\">☺");
			out.println("成功添加用户，返回到用户修改页即可查看！</p>");
			out.println("<p><a href=\'adduser.jsp\' title=\"返回\">点我继续添加</a></p>");
			out.println("<p><a href=\'modifyuser.jsp\' title=\"返回\">点我查看用户</a></p>");
			out.println("<p><a href=\'sysadmin.jsp\' title=\"返回\">点我返回主页</a></p>");
		}
		else
		{
			JdbcHelper.DeleteUser(user.id, user.role);
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("添加用户失败，" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'adduser.jsp\' title=\"返回\">点我重试</a></p>");
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
