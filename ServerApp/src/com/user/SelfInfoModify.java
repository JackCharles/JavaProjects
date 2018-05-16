package com.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class ChangePasswd
 */
@WebServlet("/SelfInfoModify")
public class SelfInfoModify extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SelfInfoModify()
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
		String type = request.getParameter("type");

		if (type == null)
			Tools.PrintErrorMsg(request, response, "Get参数丢失，请重试!");

		else if (type.equals("info"))
		{
			String phone = request.getParameter("userphone");
			String address = request.getParameter("useraddress");
			User us = (User) request.getSession().getAttribute("user");
			if (us == null)
			{
				request.getRequestDispatcher("TimeOut.html").forward(request, response);
				return;
			}
			if (JdbcHelper.ModifyUsers(us.getUserId(), us.getUserName(), us.getRole(), us.getSex(), us.getPosition(),
					us.getId(), phone, address, null))
			{
				String detials = "自我修改。电话改为：" + phone + " 地址改为：" + address;
				JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "修改用户", detials);
				us.setPhone(phone);
				us.setAddress(address);
				request.getSession().setAttribute("user", us);
				Tools.PrintSuccessMsg(request, response, "SelfModify.jsp", null);
			}
			else
				Tools.PrintErrorMsg(request, response, "信息修改失败：" + JdbcHelper.ErrorMsg);
		}
		else if (type.equals("chpwd"))
		{
			String oldpass = Tools.getMd5(request.getParameter("OldPasswd"));
			String newpass = Tools.getMd5(request.getParameter("NewPasswd1"));
			String id = ((User) request.getSession().getAttribute("user")).getUserId();

			if (JdbcHelper.ChangePassword(id, oldpass, newpass))
			{
				request.getSession().removeAttribute("user");
				Tools.PrintSuccessMsg(request, response, "index.jsp", "_top");
			}
			else
				Tools.PrintErrorMsg(request, response, "密码修改失败：" + JdbcHelper.ErrorMsg);
		}
		else
			Tools.PrintErrorMsg(request, response, "未知错误！");
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
