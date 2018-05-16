package com.user;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class SaveEvent
 */
@WebServlet("/SaveEvent")
public class SaveEvent extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SaveEvent()
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
		String paramters[] = new String[11];
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart)
		{
			// Create a factory for disk-based file items
			org.apache.commons.fileupload.FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Parse the request
			List<?> items;
			try
			{
				items = upload.parseRequest(request);
				// Process the uploaded items
				Iterator<?> iter = items.iterator();

				int i = 0;
				while (iter.hasNext())
				{
					FileItem item = (FileItem) iter.next();
					if (item.isFormField())
					{
						paramters[i] = item.getString("utf-8");
						++i;
					}

				}

				User us = (User) request.getSession().getAttribute("user");
				if (us == null)
				{
					request.getRequestDispatcher("TimeOut.html").forward(request, response);
					return;
				}
				paramters[7] = paramters[10];// 7重置为案卷号
				paramters[8] = (paramters[7] == null || paramters[7].equals("")) ? "" : us.getUserName();// 审批人
				for (int i1 = 0; i1 < paramters.length; ++i1)
					System.out.println(i1 + ": " + paramters[i1]);
				boolean res = JdbcHelper.AddEvent(paramters);
				if (res)
				{
					String detials = "案件日期：" + paramters[0] + " 公证员：" + paramters[1] + " 流水号："
							+ (JdbcHelper.GetSerialNum(paramters[1], paramters[0]) - 1);
					JdbcHelper.AddLog(Tools.getDateTime(), us.getUserId(), us.getUserName(), "上传案件", detials);
					Tools.PrintSuccessMsg(request, response, "UploadEvent.jsp", null);
				}
				else
				{
					Tools.PrintErrorMsg(request, response, "添加案件失败：" + JdbcHelper.ErrorMsg);
				}

			} catch (FileUploadException e)
			{
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		else
			Tools.PrintErrorMsg(request, response, "发生了未知错误，请联系管理员！");
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
