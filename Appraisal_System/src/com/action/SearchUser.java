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
 * Servlet implementation class SearchUser
 */
@WebServlet("/SearchUser")
public class SearchUser extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchUser()
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
		// 查询用户
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		String role = JdbcHelper.QueryUserById(id);
		if (role == null)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println(JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'modifyuser.jsp\' title=\"返回上一页\">点我返回上一页</a></p>");
			return;
		}
		UserBean user = JdbcHelper.queryUserInfo(id, role);
		if (user == null)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println(JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'modifyuser.jsp\' title=\"返回上一页\">点我返回上一页</a></p>");
			return;
		}
		else
		{
			out.println("<head><meta charset=\"utf-8\"><title>搜索结果</title>");
			out.println("<script>function warning(){return confirm(\"你确定要删除该用户？\");}</script></head>");
			out.println("<body><div style=\"font-family: 微软雅黑;font-weight: lighter;color:blue;\">");
			out.println("<p style=\"font-size:24px\">—成功检索到用户—</p>");
			out.println("<p>姓名：" + user.name + "</p>");
			out.println("<p>姓别：" + user.sex + "</p>");
			out.println("<p>电话：" + user.phone + "</p>");
			out.println("<p>住址：" + user.address + "</p>");

			out.println("<p><a href=\"modifyuser.jsp\">返回</a>");
			out.println("<a href=\"deleteuser.jsp?id=" + user.id + "&role=" + user.role
					+ "\" onclick=\"return warning()\">删除</a>");
			out.println("<a href=\"sysmodifyinfo.jsp?id=" + user.id + "&role=" + user.role + "\">修改</a>");
			out.println("</div></body>");
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
