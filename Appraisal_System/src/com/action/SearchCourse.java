package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mysql.JdbcHelper;
import com.user.UserBean;

/**
 * Servlet implementation class SearchCourse
 */
@WebServlet("/SearchCourse")
public class SearchCourse extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchCourse()
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
		// 查询课程
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String cid = request.getParameter("id");
		List<UserBean> cu = JdbcHelper.QueryCourse(cid);
		if (cu == null || cu.size() == 0)
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("查询失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'viewcourses.jsp\' title=\"返回上一页\">点我返回上一页</a></p>");
		}
		else
		{
			out.println("<head><meta charset=\"utf-8\"><title>搜索结果</title>");
			out.println("<script>function warning(){return confirm(\"你确定要删除该课程？\");}</script></head>");
			out.println("<body><div style=\"font-family: 微软雅黑;font-weight: lighter;color:blue;\">");
			out.println("<p style=\"font-size:24px\">—成功检索到课程—</p>");
			out.println("<p>课程号：" + cu.get(0).courseId + "</p>");
			out.println("<p>课程名：" + cu.get(0).courseName + "</p>");

			out.println("<p><a href=\"viewcourses.jsp\">返回</a>");
			out.println(
					"<a href=\"deletecourse.jsp?id=" + cu.get(0).courseId + "\" onclick=\"return warning()\">删除</a>");
			out.println("<a href=\"updatecourse.jsp?action=MODIFY&id=" + cu.get(0).courseId + "\">修改</a>");
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
