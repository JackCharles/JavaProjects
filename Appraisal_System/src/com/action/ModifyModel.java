package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mysql.JdbcHelper;
import com.user.Question;

/**
 * Servlet implementation class ModifyModel
 */
@WebServlet("/ModifyModel")
public class ModifyModel extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModifyModel()
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

		String qstnum = request.getParameter("qstnum");
		String mid = request.getParameter("modelid");
		String name = request.getParameter("mname");
		int num = Integer.parseInt(qstnum);
		List<Question> list = new ArrayList<Question>();
		for (int i = 0; i < num; i++)
		{
			Question qst = new Question();
			qst.qs = request.getParameter("qst" + i);
			qst.ansA = request.getParameter("ansA" + i);
			qst.ansB = request.getParameter("ansB" + i);
			qst.ansC = request.getParameter("ansC" + i);
			qst.ansD = request.getParameter("ansD" + i);
			list.add(qst);
		}

		boolean s = JdbcHelper.AddOrModifyModel(list, mid, name, "MODIFY");
		if (s)
		{
			out.println("<meta charset=\"utf-8\"><title>修改模板成功</title><p style=\"color:green\">☺");
			out.println("修改模板成功，返回即可可查看！</p>");
			out.println("<p><a href=\'modifymodel.jsp\' title=\"返回\">点我继续添加</a></p>");
		}
		else
		{
			out.println("<meta charset=\"utf-8\"><title>错误</title><p style=\"color:red\">☹");
			out.println("修改模板失败：" + JdbcHelper.ErrorMsg[JdbcHelper.ErrorCode] + "</p>");
			out.println("<p><a href=\'modifymodel.jsp\' title=\"返回\">点我重试</a></p>");
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
