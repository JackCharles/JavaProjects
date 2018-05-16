package com.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.RecentBean;
import com.bean.RecordBean;
import com.google.gson.Gson;
import com.jdbc.JdbcHelper;
import com.user.Event;
import com.user.User;

/**
 * Servlet implementation class ClientGetRecent
 */
@WebServlet("/ClientGetRecent")
public class ClientGetRecent extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientGetRecent()
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

		if (json != null)
		{
			Gson gson = new Gson();
			RecentBean recentBeanFrom = gson.fromJson(json, RecentBean.class);
			String handler1 = recentBeanFrom.getRecordMain();
			ArrayList<Event> li = JdbcHelper.getRecentRes(handler1);
			List<RecordBean> recordBeans = new ArrayList<RecordBean>();
			for (int j = 0; j < li.size(); j++)
			{
				Event e = li.get(j);
				RecordBean recordBean = new RecordBean();
				recordBean.setDate(e.date);
				recordBean.setSerial(String.valueOf(e.serial));
				recordBean.setRecordMain(e.handler1);
				recordBean.setRecordAssist(e.handler2);
				recordBean.setNumber(e.rno);
				recordBean.setStat(e.confirm);
				recordBean.setType1(e.type);
				recordBean.setType2(e.subtype);

				List<User> lp = JdbcHelper.GetParties(e.date, e.serial, e.handler1);
				List<Map<String, String>> persons = new ArrayList<>();
				for (int k = 0; k < lp.size(); k++)
				{
					Map<String, String> m = new HashMap<String, String>();
					m.put("personName", lp.get(k).getUserName());
					m.put("personID", lp.get(k).getId());
					persons.add(m);
				}
				recordBean.setPersons(persons);
				recordBean.setApproval(e.approval);
				recordBean.setOther(e.comment);
				String filepath = e.filepath;
				// System.out.println("文件路径为："+filepath);
				if (getFileNameFromPath(filepath) != null)
				{
					recordBean.setFiles(getFileNameFromPath(filepath));
				}
				recordBeans.add(recordBean);
			}

			RecentBean recentBeanTo = new RecentBean();
			recentBeanTo.setRecordMain(handler1);
			recentBeanTo.setRecordBeanlist(recordBeans);
			Gson gsonTo = new Gson();
			out.append(gsonTo.toJson(recentBeanTo));

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

	private List<String> getFileNameFromPath(String path)
	{
		List<String> FileNames = new ArrayList<>();
		File file = new File(path);
		if (!file.exists())
		{
			return null;
		}
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++)
		{
			if (fileList[i].isFile())
			{
				String fileName = fileList[i].getName();
				FileNames.add(fileName);
			}
		}
		return FileNames;
	}

}
