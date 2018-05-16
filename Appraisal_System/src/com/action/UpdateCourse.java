package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mysql.JdbcHelper;

/**
 * Servlet implementation class AddCourse
 */
@WebServlet("/UpdateCourse")
public class UpdateCourse extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateCourse()
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String action = request.getParameter("action");
		String cid = request.getParameter("courseId");
		String cname = request.getParameter("courseName");
		String oldcid = request.getParameter("oldcid");

		boolean s;
		if (action.equals("ADD"))// add course
			s = JdbcHelper.AddCourse(cid, cname);

		else// modify course
		if (oldcid.equals(cid))// not change cid
			s = JdbcHelper.ModifyCourse(oldcid, cname);
		else// cid has changed
		{
			s = JdbcHelper.DeleteCourse(oldcid);
			if (s)
				s = JdbcHelper.AddCourse(cid, cname);
		}
		if (s)// operation success
		{
			out.println("<meta charset=\"utf-8\"><title>修改/添加课程成功</title><p style=\"color:green\">☺");
			out.println("成功添加/修改课程，返回到课程修改页即可查看！</p>");
			out.println("<p style=\"color:red\">警告：如果你修改了课程号，请及时检查教师授课信息，可能出现无效授课号！</p>");
			out.println("<p><a href=\'viewcourses.jsp\' title=\"返回\">点我查看课程</a></p>");
			out.println("<p><a href=\'sysadmin.jsp\' title=\"返回\">点我返回主页</a></p>");
		}
		else// an error occured
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("添加/修改课程失败，" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'sysadmin.jsp\' title=\"返回\">点我返回</a></p>");
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
