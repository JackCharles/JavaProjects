package com.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.JdbcHelper;

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
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String userid = request.getParameter("userid");
		String name = request.getParameter("username");
		String[] role = request.getParameterValues("userrole");
		String sex = request.getParameter("usersex");
		String position = request.getParameter("position");
		String id = request.getParameter("id");
		String phone = request.getParameter("userphone");
		String address = request.getParameter("useraddress");
		String passwd = request.getParameter("userpasswd");

		int roleno = 0;
		if (role != null)
			for (int i = 0; i < role.length; ++i)
				roleno += Integer.valueOf(role[i]);

		String realpasswd = (passwd == null || passwd.equals("")) ? Tools.getMd5(userid) : Tools.getMd5(passwd);
		if (JdbcHelper.AddUser(userid, name, roleno, sex, position, id, phone, address, realpasswd))
		{
			User us = (User) request.getSession().getAttribute("user");
			if (us == null)
			{
				request.getRequestDispatcher("TimeOut.html").forward(request, response);
				return;
			}
			String detials = "添加的用户：" + userid + " 姓名：" + name + " 权限：" + roleno + " 职位：" + position;
			JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "添加用户", detials);
			request.getRequestDispatcher("UserManager.jsp").forward(request, response);
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
