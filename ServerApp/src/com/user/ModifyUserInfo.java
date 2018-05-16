package com.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class ModifyUserInfo
 */
@WebServlet("/ModifyUserInfo")
public class ModifyUserInfo extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModifyUserInfo()
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

		if (JdbcHelper.ModifyUsers(userid, name, roleno, sex, position, id, phone, address,
				passwd.equals("####") ? null : Tools.getMd5(passwd)))
		{
			User us = (User) request.getSession().getAttribute("user");
			if (us == null)
			{
				request.getRequestDispatcher("TimeOut.html").forward(request, response);
				return;
			}
			String detials = "被修改的用户ID：" + userid;
			JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "修改用户", detials);
			if (userid.equals(us.getUserId()))// 修改了自己的信息
			{
				if (!passwd.equals("####"))// 修改了自己的密码，重新登录
				{
					request.getSession().removeAttribute("user");
					Tools.PrintSuccessMsg(request, response, "index.jsp", "_top");
				}
				else// 重设session
				{
					us.setUserName(name);
					us.setRole(roleno);
					us.setSex(sex);
					us.setPhone(phone);
					us.setPosition(position);
					us.setId(id);
					us.setAddress(address);
					request.getSession().setAttribute("user", us);
					Tools.PrintSuccessMsg(request, response, "UserManager.jsp", null);
				}
			}
			else
				Tools.PrintSuccessMsg(request, response, "UserManager.jsp", null);
		}
		else
			Tools.PrintErrorMsg(request, response, "修改失败：" + JdbcHelper.ErrorMsg);
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
