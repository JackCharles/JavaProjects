package com.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.bean.FormBean;
import com.google.gson.Gson;
import com.jdbc.JdbcConfig;
import com.jdbc.JdbcHelper;
import com.user.Tools;

/**
 * Servlet implementation class ClientFileUpload
 */
@WebServlet("/ClientFileUpload")
public class ClientFileUpload extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientFileUpload()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	private String path;
	private FormBean formBean;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// 系统根目录
		path = JdbcConfig.BASEDIR;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置内存缓冲区，超过后写入临时文件
		factory.setSizeThreshold(1024 * 1024 * 4);
		// 设置临时文件存储位置
		File file = new File(JdbcConfig.TEMPDIR);
		if (!file.exists())
			file.mkdirs();
		factory.setRepository(file);
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个文件的最大上传值
		upload.setFileSizeMax(Long.MAX_VALUE);
		// 设置整个request的最大值
		upload.setSizeMax(Long.MAX_VALUE);
		upload.setHeaderEncoding("UTF-8");

		// 处理item，实践发现文件传输时的item：文件在前，表单在后，所以循环两次来消除顺序的影响
		// 先上传：{表单}，再上传：{文件，表单}
		try
		{
			List<?> items = upload.parseRequest(request);
			FileItem item = null;
			String fileName = null;

			for (int i = 0; i < items.size(); i++)
			{
				item = (FileItem) items.get(i);
				// 如果是表单
				if (item.isFormField())
				{
					System.out.println("文件上传：收到的表单内容如下：" + item.getFieldName() + ":" + item.getString("utf-8"));
					// 创建FormBean对象
					String json = item.getString("utf-8");
					Gson gson = new Gson();
					formBean = gson.fromJson(json, FormBean.class);
					// {表单}
					if (formBean.getIsform().equals("Y"))
					{
						List<String> pList = new ArrayList<>();
						pList.add(formBean.getDate());
						pList.add(formBean.getRecordMain());
						pList.add(formBean.getRecordAssist());
						pList.add(formBean.getType1());
						pList.add(formBean.getType2());
						// 拼凑格式：当事人@当事人身份id
						List<Map<String, String>> pm = formBean.getPersons();
						String str = "";
						for (int j = 0; j < pm.size(); j++)
						{
							if (pm.get(j).get("personID").isEmpty())
							{
								// 身份ID不应为空，所以不进入这里
								str = str + pm.get(j).get("personName") + "@ " + "\r\n";
							}
							else
							{
								str = str + pm.get(j).get("personName") + "@" + pm.get(j).get("personID") + "\r\n";
							}
						}
						pList.add(str);// person
						pList.add(formBean.getOther());
						pList.add("");// 卷宗号
						pList.add("");// 审批人
						String[] params = pList.toArray(new String[pList.size()]);

						if (JdbcHelper.AddEvent(params))
						{
							System.out.println("文件上传：记录添加成功");
							// 获取流水号
							String serial = String
									.valueOf(JdbcHelper.GetSerialNum(formBean.getRecordMain(), formBean.getDate()) - 1);
							System.out.println("文件上传：本次表单生成的serial:" + serial);
							out.append(serial);
							formBean.setSerial(serial);

							// 添加文件列表到数据库
							List<String> fList = formBean.getFiles();
							for (int j = 0; j < fList.size(); j++)
							{
								String f = fList.get(j);
								if (!JdbcHelper.addFileName(formBean.getDate(), serial, formBean.getRecordMain(), f))
								{
									System.out.println("文件上传：文件添加失败" + JdbcHelper.ErrorMsg);
								}
							}
							// 创建文件夹
							String p = JdbcHelper.getFilePath(formBean.getDate(),
									Integer.parseInt(formBean.getSerial()), formBean.getRecordMain());
							if (p != null)
							{
								if (!p.isEmpty())
									path = p;
							}
							File formfile = new File(path);
							if (!formfile.exists())
								formfile.mkdirs();
							System.out.println("文件上传：文件对应路径：" + path);

						}
						else
						{
							System.out.println("记录添加失败" + JdbcHelper.ErrorMsg);
							out.append("Err");
						}
					}
					else if (formBean.getIsform().equals("N"))
					{
						// 此处获得文件夹路径
						String p = JdbcHelper.getFilePath(formBean.getDate(), Integer.parseInt(formBean.getSerial()),
								formBean.getRecordMain());
						if (p != null)
						{
							if (!p.isEmpty())
								path = p;
						}
						File formfile = new File(path);
						// 如果文件列表不为空，那么文件夹就应该已经创建
						// 此处代码是冗余的，文件夹应已经创建
						if (!formfile.exists())
							formfile.mkdirs();
						System.out.println("文件上传：查到记录所对应的文件夹路径：" + path);
					}
					// 异常情况处理：文件上传失败后，文件路径发生改变，为保证不能修改记录的情况，只能删除该记录。注：流水号未不收回
					else if (formBean.getIsform().equals("E"))
					{
						if (JdbcHelper.DeleteEvent(formBean.getDate(), Integer.parseInt(formBean.getSerial()),
								formBean.getRecordMain()))
						{
							JdbcHelper.subSerial(formBean.getDate(), formBean.getRecordMain());
							for (int j = 0; j < formBean.getFiles().size(); j++)
							{
								if (JdbcHelper.deleteFileName(formBean.getDate(),
										Integer.parseInt(formBean.getSerial()), formBean.getRecordMain(),
										formBean.getFiles().get(j)))
								{

									System.out.println("已删除：" + formBean.getFiles().get(j));
								}
								else
								{
									System.out.println("删除失败：" + formBean.getFiles().get(j));
								}
							}
							out.append("DFin");
							return;
						}
						else
						{
							System.out.println("错误：记录删除失败");
						}
					}
					else
					{
						// 这里不应该被执行
						System.out.println("文件上传：错误的提交类型");
					}

				}

			}
			for (int i = 0; i < items.size(); i++)
			{
				item = (FileItem) items.get(i);
				// 保存文件
				if (!item.isFormField() && item.getName().length() > 0)
				{
					// 如果文件夹未生成，则失败。意味着表单处理失败。这里不应该进入
					File folder = new File(path);
					if (!folder.exists())
					{
						out.append("Err");
						return;
					}

					fileName = path + "/" + item.getName().replace('%', '0').replace('#', '0');
					item.write(new File(fileName));
					System.out.println("文件上传：" + fileName + " successfully saved!");

					String fileMobilePath = null;
					String itemName = item.getName();
					List<String> fileList = formBean.getFiles();
					for (int j = 0; j < fileList.size(); j++)
					{
						// 考虑了文件上传后，记录仍未没有删除的情况,这里也不应该进入
						if (fileList.get(j).equals(itemName))
						{
							System.out.println("文件上传后，没有被删除的情况,这里不应该进入");
						}
						String s[] = fileList.get(j).split("/");
						String name = s[s.length - 1];
						if (name.equals(itemName))
						{
							fileMobilePath = fileList.get(j);
							break;
						}
					}
					if (fileMobilePath == null)
					{
						// 不应该进入这里
						System.out.println("文件上传：未找到名字，文件列表出错");
						out.append("Err");
						return;
					}
					// 需要判断：文件全部写完后在进行后续逻辑，包括回应和删除记录
					// 实践表明，文件写入完毕后才会删除记录，write函数应该是在同一线程里执行的
					// 这里要判断是否所有文件已上传
					if (formBean != null)
					{
						// System.out.println(fileMobilePath);
						if (!JdbcHelper.deleteFileName(formBean.getDate(), Integer.parseInt(formBean.getSerial()),
								formBean.getRecordMain(), fileMobilePath))
						{
							System.out.println("文件上传：文件写入时数据库文件记录不存在" + JdbcHelper.ErrorMsg);
							// File fileDelete = new File(fileName);
							// fileDelete.delete();
						}
						List<String> fl = JdbcHelper.getFileList(formBean.getDate(),
								Integer.parseInt(formBean.getSerial()), formBean.getRecordMain());
						if (fl.size() > 0)
						{
							System.out.println("文件上传：待上传文件数量为" + fl.size());
							out.append("SFin");
						}
						else
						{
							JdbcHelper.AddLog(Tools.getDateTime(), formBean.getRecordMainID(), formBean.getRecordMain(),
									"上传案件", "案件日期：" + formBean.getDate() + " 公证员：" + formBean.getRecordMain() + " 流水号："
											+ formBean.getSerial());
							System.out.println("文件上传：文件已全部上传");
							out.append("AFin");
						}

					}
				}
			}

		} catch (FileUploadBase.IOFileUploadException e)
		{
			System.out.println("异常：FileUpload异常，文件写入失败");
		} catch (SocketTimeoutException e)
		{
			System.out.println("异常：连接超时，断开连接");
		} catch (Exception e)
		{
			e.printStackTrace();
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
