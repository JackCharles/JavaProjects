package com.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.RecordBean;
import com.bean.ResultBean;
import com.google.gson.Gson;
import com.jdbc.JdbcHelper;
import com.user.EventType;
import com.user.Tools;
import com.user.User;

@WebServlet("/ClientLogin")
public class ClientLogin extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public ClientLogin()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String username = request.getParameter("username").toString();
		String password = request.getParameter("password").toString();
		System.out.println("username:" + username + "password:" + password);

		String userid = username;
		String passwd = Tools.getMd5(password);

		if (passwd != null && userid != null)
		{
			User user = JdbcHelper.VerifyUser(userid, passwd);
			if (user == null)
			{
				System.out.println(JdbcHelper.ErrorMsg);
				ResultBean resultBean = new ResultBean();
				resultBean.setPassResult("Deny");
				Gson gson = new Gson();
				String resultJson = gson.toJson(resultBean);
				response.getWriter().append(resultJson);

				System.out.println(resultJson);
				System.out.println("登录拒绝");
			}
			else
			{
				int unconCount = JdbcHelper.getUnconfirmCount(user.getUserName());
				System.out.println(userid + "未审批的表单数量为：" + unconCount);

				ResultBean resultBean = new ResultBean();
				resultBean.setPassResult("Acc");

				// 查找未完成的记录，设置result中的resultBeanlist和finFileList，无则为空
				// record查找的是带有路径的未完成的记录，该记录每个记录者最多只有一条，可能有多个文件
				RecordBean recordBean = JdbcHelper.getUnfinFileRecord(user.getUserName());
				if (recordBean != null)
				{
					List<RecordBean> recordBeans = new ArrayList<>();
					recordBeans.add(recordBean);
					resultBean.setRecordBeanlist(recordBeans);// 设置未完成的文件名
					// 查找已完成的文件名
					String filepath = JdbcHelper.getFilePath(recordBean.getDate(),
							Integer.parseInt(recordBean.getSerial()), recordBean.getRecordMain());
					List<String> fileNames = new ArrayList<>();
					if (filepath != null)
					{
						if (!filepath.isEmpty())
							fileNames = getFileNameFromPath(filepath);
					}
					resultBean.setFinFileList(fileNames);// 设置完成的文件名
				}

				// 设置未审批的记录数目
				resultBean.setUnconCount(String.valueOf(unconCount));
				resultBean.setUsername(user.getUserName());

				List<EventType> typelist = JdbcHelper.QueryAllType();
				if (typelist == null)
				{
					resultBean.setPassResult("Err");
					System.out.println("2级类别异常----1");
				}
				else
					for (int i = 0; i < typelist.size(); i++)
					{
						EventType eventType = typelist.get(i);
						if (eventType.FatherType.equals("民事"))
						{
							resultBean.getType1().add(eventType.ChildType);
						}
						else if (eventType.FatherType.equals("涉外"))
						{
							resultBean.getType3().add(eventType.ChildType);
						}
						else if (eventType.FatherType.equals("经济"))
						{
							resultBean.getType2().add(eventType.ChildType);
						}
						else if (eventType.FatherType.equals("其它"))
						{
							resultBean.getType4().add(eventType.ChildType);
						}
						else
						{
							resultBean.setPassResult("Err");
							System.out.println("2级类别异常----2");
						}
					}

				ArrayList<User> userList = JdbcHelper.QueryAllUsers();
				if (userList != null)
				{
					for (int i = 0; i < userList.size(); i++)
					{
						resultBean.getUsers().add(userList.get(i).getUserName());
					}
				}
				else
				{
					resultBean.setPassResult("Err");
					System.out.println("公证人信息异常");
				}

				Gson gson = new Gson();
				String resultJson = gson.toJson(resultBean);

				response.getWriter().append(resultJson);
				System.out.println("登录成功，返回Json：" + resultJson);

			}
		}
		else
		{

			System.out.println("网络传输错误");
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
