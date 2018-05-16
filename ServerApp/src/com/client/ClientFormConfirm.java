package com.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.RecordBean;
import com.google.gson.Gson;
import com.jdbc.JdbcHelper;
import com.user.Tools;

/**
 * Servlet implementation class ClientFormConfirm
 */
@WebServlet("/ClientFormConfirm")
public class ClientFormConfirm extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientFormConfirm()
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
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		String json = request.getParameter("json").toString();
		System.out.println("收到审批请求的记录为" + json);
		if (json != null)
		{
			Gson gson = new Gson();
			RecordBean recordBean = gson.fromJson(json, RecordBean.class);
			if (JdbcHelper.changeRno(recordBean.getDate(), recordBean.getSerial(), recordBean.getRecordMain(),
					recordBean.getNumber(), recordBean.getApproval()))
			{

				JdbcHelper.AddLog(Tools.getDateTime(), recordBean.getRecordMainID(), recordBean.getRecordMain(), "审批案件",
						"案件日期：" + recordBean.getDate() + " 公证员：" + recordBean.getRecordMain() + " 流水号："
								+ recordBean.getSerial() + " 案卷号：" + recordBean.getNumber());
				out.append("Acc");
			}
			else
			{
				System.out.println("审批失败，数据库响应异常");
				out.append("Err");
			}
		}
		else
		{
			System.out.println("收到请求，但请求值为空");
			out.append("Err");
		}
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
