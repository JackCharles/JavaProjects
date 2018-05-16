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
 * Servlet implementation class SysModifyInfo
 */
@WebServlet("/SysModifyInfo")
public class SysModifyInfo extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SysModifyInfo()
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
		// 系统管理员修改信息
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		UserBean user = new UserBean();
		String oldrole = request.getParameter("oldRole");
		user.role = request.getParameter("role");
		user.id = request.getParameter("id");
		user.name = request.getParameter("name");
		user.sex = request.getParameter("sex");
		user.phone = request.getParameter("phone");
		user.address = request.getParameter("address");
		String passwd = request.getParameter("passwd");
		if (user.name == null || user.phone == null || user.address == null)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("输入为空，请检查!</p>");
			out.println("<p><a href=\'modifyuser.jsp\'>返回修改</a></p>");
			return;
		}
		if (user.role.equals("STUDENT"))
			user.Sclass = String.valueOf(Integer.valueOf(request.getParameter("Sclass")));// 去掉多余前导0
		else if (user.role.equals("TEACHER"))// 检测课程号是否合法
		{
			String cl[] = request.getParameter("Tclass").split(",");
			for (int i = 0; i < cl.length; i++)
				user.Tclass.add(cl[i]);
			user.courseId = request.getParameter("course");
			if (JdbcHelper.QueryCourse(user.courseId).size() == 0 || user.Tclass.size() == 0)// 课程号或班级号有错
			{
				out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
				out.println("课程号或班级输入有误!</p>");
				out.println("<p><a href=\'modifyuser.jsp\'>返回修改</a></p>");
				return;
			}
		}

		if (user.role.equals(oldrole))// 没有改变角色
		{
			boolean s = JdbcHelper.ModifyInfo(user);
			if (!s)
			{
				out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
				out.println("修改失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
				out.println("<p><a href=\'modifyuser.jsp\'>返回重试</a></p>");
				return;
			}
			if (!passwd.equals("NOCHANGE"))
				JdbcHelper.ChangePassword(user.id, null, MD5.getMd5(passwd), "SYSADMIN");
			out.println("<meta charset=\"utf-8\"><title>修改信息成功</title><p style=\"color:green\">☺");
			out.println("您已经成功个人信息，返回刷新即可查看！</p>");
			out.println("<p><a href=\'modifyuser.jsp\'>点我返回</a></p>");
		}
		else// 角色改变了
		{
			boolean s = JdbcHelper.DeleteUser(user.id, oldrole);
			if (!s)
			{
				out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
				out.println("修改失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
				out.println("<p><a href=\'modifyuser.jsp\'>返回重试</a></p>");
				return;
			}
			s = JdbcHelper.AddUser(user);
			if (!s)
			{
				out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
				out.println("修改失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
				out.println("<p><a href=\'modifyuser.jsp\'>返回重试</a></p>");
				JdbcHelper.DeleteUser(user.id, user.role);
			}
			else
			{
				if (!passwd.equals("NOCHANGE"))
					JdbcHelper.ChangePassword(user.id, null, passwd, "SYSADMIN");
				out.println("<meta charset=\"utf-8\"><title>修改信息成功</title><p style=\"color:green\">☺");
				out.println("您已经成功个人信息，返回刷新即可查看！</p>");
				out.println("<p><a href=\'modifyuser.jsp\'>点我返回</a></p>");
			}
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
