package com.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jdbc.JdbcConfig;
import com.jdbc.JdbcHelper;

/**
 * Servlet implementation class FileUpload
 */
@WebServlet("/FileUpload")
public class FileUpload extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUpload()
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
		PrintWriter out = response.getWriter();
		String date = request.getParameter("date");
		String handler1 = URLDecoder.decode(request.getParameter("handler1"), "UTF-8");
		String type = URLDecoder.decode(request.getParameter("type"), "UTF-8");
		String subtype = URLDecoder.decode(request.getParameter("subtype"), "UTF-8");
		String party[] = URLDecoder.decode(request.getParameter("party"), "UTF-8").split("\n");
		int serial = JdbcHelper.GetSerialNum(handler1, date);
		if (serial == -1)
		{
			Tools.PrintErrorMsg(request, response, "获取流水号失败：" + JdbcHelper.ErrorMsg);
			return;
		}
		String Folder = JdbcConfig.BASEDIR + handler1 + "/" + date.replaceAll("-", "") + "_" + serial + "_" + type + "-"
				+ subtype;
		for (int i = 0; i < party.length; ++i)
			Folder += "_" + party[i].split("@")[0];
		File file = new File(Folder);
		if (!file.exists())
			file.mkdirs();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(1024 * 1024 * 4);
		// session需要重新post
		// 设置临时文件存储位置
		file = new File(JdbcConfig.TEMPDIR);
		if (!file.exists())
			file.mkdirs();
		factory.setRepository(file);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(Long.MAX_VALUE);
		// 设置整个request的最大值
		upload.setSizeMax(Long.MAX_VALUE);
		upload.setHeaderEncoding("UTF-8");

		try
		{
			List<?> items = upload.parseRequest(request);
			FileItem item = null;
			String fileName = null;
			for (int i = 0; i < items.size(); i++)
			{
				item = (FileItem) items.get(i);
				// 保存文件
				if (!item.isFormField())
				{
					fileName = Folder + "/" + item.getName().replace('%', '0').replace('#', '0');
					item.write(new File(fileName));
				}
				// 千万不可删除，swf以服务端有返回值证明文件传输完成！
				out.println(fileName + "successfully uploaded!");
			}
		} catch (Exception e)
		{
			out.println(e.getMessage());
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
