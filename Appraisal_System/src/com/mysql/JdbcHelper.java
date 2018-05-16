package com.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.action.MD5;
import com.user.AssRes;
import com.user.Model;
import com.user.QsAnswer;
import com.user.Question;
import com.user.UserBean;

public class JdbcHelper implements JdbcConfig
{
	public static String ErrorMsg[] = {"", "", "", ""};
	public static int ErrorCode = 1;

	// 获取数据库连接
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			return conn;
		} catch (Exception e)
		{
			ErrorCode = 0;
			ErrorMsg[0] = "数据库连接失败：" + e.getMessage();
			return null;
		}
	}

	// 用户校验
	public static String VerifyUser(String id, String passwd)
	{
		String SqlCmd = "select * from users where user_id =\'" + id + "\' and password=\'" + passwd + "\'";
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Statement stat = null;
		ResultSet res = null;

		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery(SqlCmd);
			if (res.next())// 返回用户角色
				return res.getString("role");
			else
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "用户名或密码错误，请检查！";
				return null;// 验证未通过
			}
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 根据ID查询用户，返回角色
	public static String QueryUserById(String id)
	{
		String SqlCmd = "select * from users where user_id =\'" + id + "\'";
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Statement stat = null;
		ResultSet res = null;

		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery(SqlCmd);
			if (res.next())// 返回用户角色
				return res.getString("role");
			else
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "查询的用户不存在，请检查ID是否正确！";
				return null;
			}
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 用户详细信息查询
	public static UserBean queryUserInfo(String id, String role)
	{
		UserBean us = new UserBean();
		us.id = id;
		us.role = role;
		String SqlCmd;
		if (role.equals("SYSADMIN"))
			SqlCmd = "select * from system_admin where sys_id=\'" + id + "\'";
		else if (role.equals("ASSADMIN"))
			SqlCmd = "select * from assess_admin where ass_id=\'" + id + "\'";
		else if (role.equals("TEACHER"))
			SqlCmd = "select * from teachers where Tno=\'" + id + "\'";
		else
			SqlCmd = "select * from students where Sno=\'" + id + "\'";

		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return null;
		}

		Statement stat = null;
		ResultSet res = null;
		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery(SqlCmd);
			if (res.next())
			{
				us.name = res.getString("name");
				us.sex = res.getString("sex");
				us.phone = res.getString("phone");
				us.address = res.getString("address");
				if (role.equals("STUDENT"))
					us.Sclass = res.getString("class");
				if (role.equals("TEACHER"))
				{
					us.courseId = res.getString("Cno");
					res.close();
					res = stat.executeQuery("select name from courses where Cno=\'" + us.courseId + "\'");
					if (res.next())
					{
						us.courseName = res.getString("name");
						res.close();
					}
					res = stat.executeQuery("select class from teach_class where Tno=\'" + id + "\'");
					while (res.next())
						us.Tclass.add(res.getString("class"));
				}
				return us;
			}
			else
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "ID错误或ID与role不匹配！";
				return null;
			}
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 更改密码
	public static boolean ChangePassword(String id, String oldpass, String newpass, String role)
	{

		String SqlCmd1 = "select * from users where user_id=\'" + id + "\' and password=\'" + oldpass + "\'";
		String SqlCmd2 = "update users set password=\'" + newpass + "\' where user_id=\'" + id + "\'";

		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return false;
		}
		int s;
		Statement stat = null;
		ResultSet res = null;
		try
		{
			stat = conn.createStatement();
			if (role.equals("SYSADMIN"))
				s = stat.executeUpdate(SqlCmd2);// 更新新密码
			else
			{
				res = stat.executeQuery(SqlCmd1);// 验证旧密码
				if (res.next())
					s = stat.executeUpdate(SqlCmd2);// 更新新密码
				else
				{
					ErrorCode = 2;
					ErrorMsg[ErrorCode] = "密码未通过验证，请检查！";
					return false;
				}
			}
			if (s == 0)
			{
				ErrorCode = 1;
				ErrorMsg[ErrorCode] = "执行更新操作失败！";
				return false;
			}
			else
				return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 修改用户信息
	public static boolean ModifyInfo(UserBean user)
	{
		String SqlCmd;
		if (user.role.equals("STUDENT"))
			SqlCmd = "update students set name=\'" + user.name + "\',sex=\'" + user.sex + "\',class=\'" + user.Sclass
					+ "\',phone=\'" + user.phone + "\',address=\'" + user.address + "\' where Sno=\'" + user.id + "\'";

		else if (user.role.equals("TEACHER"))
			SqlCmd = "update teachers set name=\'" + user.name + "\',sex=\'" + user.sex + "\',Cno=\'" + user.courseId
					+ "\',phone=\'" + user.phone + "\',address=\'" + user.address + "\' where Tno=\'" + user.id + "\'";

		else if (user.role.equals("SYSADMIN"))
			SqlCmd = "update system_admin set name=\'" + user.name + "\',sex=\'" + user.sex + "\',phone=\'" + user.phone
					+ "\',address=\'" + user.address + "\' where sys_id=\'" + user.id + "\'";
		else
			SqlCmd = "update assess_admin set name=\'" + user.name + "\',sex=\'" + user.sex + "\',phone=\'" + user.phone
					+ "\',address=\'" + user.address + "\' where ass_id=\'" + user.id + "\'";

		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return false;
		}

		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			int s = stat.executeUpdate(SqlCmd);//
			if (user.role.equals("TEACHER"))
			{
				stat.execute("delete from teach_class where Tno=\'" + user.id + "\'");
				for (int i = 0; i < user.Tclass.size(); i++)
					stat.execute("insert into teach_class values(\'" + user.id + "\',\'" + user.Tclass.get(i) + "\')");
			}
			if (s == 0)
			{
				ErrorCode = 1;
				ErrorMsg[ErrorCode] = "执行更新操作失败！";
				return false;
			}
			else
				return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询评教信息
	public static List<UserBean> QueryAssess(String stuid)
	{
		List<UserBean> list = new ArrayList<UserBean>();

		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return null;
		}
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String mydate = format.format(date);
		String sclass = null;
		Statement stat1 = null, stat2 = null, stat3 = null;
		ResultSet res1 = null, res2 = null, res3 = null;
		try
		{
			stat3 = conn.createStatement();
			res3 = stat3.executeQuery("select class from students where Sno=\'" + stuid + "\'");
			if (res3.next())
				sclass = res3.getString("class");// 获取班级

			stat1 = conn.createStatement();
			res1 = stat1.executeQuery(
					"select Tno from started_assess where \'" + mydate + "\' between StartTime and EndTime");
			while (res1.next())
			{
				String Tno = res1.getString("Tno");// 获取有效时间内的教师号
				String TTno = "T" + Tno;
				stat2 = conn.createStatement();
				res2 = stat2.executeQuery("select " + TTno + " from students_assess where Sno=\'" + stuid + "\'");
				if (res2.next())
				{
					if (res2.getString(TTno).equals("N"))// 还没有评价过，Tno有效//res2可以关闭//班级筛选
					{
						res2 = stat2.executeQuery("select teachers.name Tname,courses.name Cname, teachers.Cno from "
								+ "teachers,courses,teach_class,students where teachers.Tno=\'" + Tno
								+ "\' and teachers.Cno=courses.Cno and teachers.Tno=teach_class.Tno and teach_class.class=\'"
								+ sclass + "\'");
						if (res2.next())
						{
							UserBean us = new UserBean();
							us.id = Tno;
							us.name = res2.getString("Tname");
							us.courseId = res2.getString("Cno");
							us.courseName = res2.getString("Cname");
							list.add(us);
							res2.close();
						} // if
					} // if
				} // if
				stat2.close();
			} // while
			return list;// 返回list

		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			e.printStackTrace();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res1 != null)
					res1.close();
				if (stat1 != null)
					stat1.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询所有用户只返回id name
	public static List<UserBean> QueryAllUser(String role)
	{
		List<UserBean> list = new ArrayList<UserBean>();

		String SqlCmd;
		if (role.equals("STUDENT"))
			SqlCmd = "select Sno id,name from students";
		else if (role.equals("TEACHER"))
			SqlCmd = "select Tno id,name from teachers";
		else if (role.equals("SYSADMIN"))
			SqlCmd = "select sys_id id,name from system_admin";
		else
			SqlCmd = "select ass_id id,name from assess_admin";

		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return null;
		}

		Statement stat = null;
		ResultSet res = null;
		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery(SqlCmd);
			while (res.next())
			{
				UserBean us = new UserBean();
				us.id = res.getString("id");
				us.name = res.getString("name");
				us.role = role;
				list.add(us);
			}
			return list;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			e.printStackTrace();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 删除用户
	public static boolean DeleteUser(String id, String role)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return false;
		}

		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			stat.execute("delete from users where user_id=\'" + id + "\'");
			if (role.equals("STUDENT"))
			{
				stat.execute("delete from students where Sno=\'" + id + "\'");
				stat.execute("delete from students_assess where Sno=\'" + id + "\'");
				ResultSet res = stat.executeQuery("select * from total_assesses where Sno=\'" + id + "\'");
				if (res.next())
				{
					stat.execute("delete from total_assesses where Sno=\'" + id + "\'");
					res.close();
				}
			}
			else if (role.equals("TEACHER"))
			{
				stat.execute("delete from teachers where Tno=\'" + id + "\'");
				stat.execute("delete from teach_class where Tno=\'" + id + "\'");
				stat.execute("alter table students_assess drop column T" + id);
				ResultSet res = stat.executeQuery("select * from total_assesses where Tno=\'" + id + "\'");
				if (res.next())
				{
					stat.execute("delete from total_assesses where Tno=\'" + id + "\'");
					res.close();
				}
				res = stat.executeQuery("select * from started_assess where Tno=\'" + id + "\'");
				if (res.next())
				{
					stat.execute("delete from started_assess where Tno=\'" + id + "\'");
					res.close();
				}
			}
			else if (role.equals("SYSADMIN"))
				stat.execute("delete from system_admin where sys_id=\'" + id + "\'");
			else
				stat.execute("delete from assess_admin where ass_id=\'" + id + "\'");
			return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			e.printStackTrace();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 添加用户
	public static boolean AddUser(UserBean user)// class只有老师有效
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return false;
		}

		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			ResultSet res = stat.executeQuery("select user_id from users where user_id=\'" + user.id + "\'");
			if (res.next())
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "用户ID：" + user.id + "已存在，添加失败！";
				res.close();
				return false;
			}
			else
			{
				stat.execute("insert into users values(\'" + user.id + "\',\'" + MD5.getMd5(user.id) + "\',\'"
						+ user.role + "\')");
				if (user.role.equals("STUDENT"))
				{
					stat.execute("insert into students values(\'" + user.id + "\',\'" + user.name + "\',\'" + user.sex
							+ "\',\'" + user.Sclass + "\',\'" + user.phone + "\',\'" + user.address + "\')");
					stat.execute("insert into students_assess (Sno) values(\'" + user.id + "\')");
				}
				else if (user.role.equals("TEACHER"))
				{
					res = stat.executeQuery("select * from courses where Cno=\'" + user.courseId + "\'");
					if (res.next())
					{
						stat.execute("insert into teachers values(\'" + user.id + "\',\'" + user.name + "\',\'"
								+ user.courseId + "\',\'" + user.sex + "\',\'" + user.phone + "\',\'" + user.address
								+ "\')");
						stat.execute("alter table students_assess add T" + user.id + " char(1) not null default \'N\'");
						for (int i = 0; i < user.Tclass.size(); i++)
							stat.execute("insert into teach_class values(\'" + user.id + "\',\'" + user.Tclass.get(i)
									+ "\')");
					}
					else
					{
						ErrorCode = 2;
						ErrorMsg[ErrorCode] = "课程号无效，请返回重试！";
						return false;
					}
				}
				else if (user.role.equals("SYSADMIN"))
				{
					stat.execute("insert into system_admin values(\'" + user.id + "\',\'" + user.name + "\',\'"
							+ user.sex + "\',\'" + user.phone + "\',\'" + user.address + "\')");
				}
				else
				{
					stat.execute("insert into assess_admin values(\'" + user.id + "\',\'" + user.name + "\',\'"
							+ user.sex + "\',\'" + user.phone + "\',\'" + user.address + "\')");
				}
				return true;
			}
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询课程//cid="ALL"查询全部
	public static List<UserBean> QueryCourse(String cid)
	{
		String SqlCmd;
		List<UserBean> list = new ArrayList<UserBean>();
		if (!cid.equals("ALL"))
			SqlCmd = "select * from courses where Cno =\'" + cid + "\'";
		else
			SqlCmd = "select * from courses";
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Statement stat = null;
		ResultSet res = null;

		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery(SqlCmd);
			while (res.next())
			{
				UserBean us = new UserBean();
				us.courseId = res.getString("Cno");
				us.courseName = res.getString("name");
				list.add(us);
			}
			return list;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 删除课程
	public static boolean DeleteCourse(String cid)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return false;
		}
		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			stat.execute("delete from courses where Cno=\'" + cid + "\'");
			return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 添加课程
	public static boolean AddCourse(String cid, String cname)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return false;
		}
		Statement stat = null;
		ResultSet res = null;
		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery("select Cno from courses where Cno=\'" + cid + "\'");
			if (res.next())
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "待添加的课程ID已存在";
				return false;
			}
			else
			{
				stat.execute("insert into courses values(\'" + cid + "\',\'" + cname + "\')");
				return true;
			}
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 添加课程
	public static boolean ModifyCourse(String cid, String cname)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return false;
		}
		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			stat.executeUpdate("update courses set name=\'" + cname + "\' where Cno=\'" + cid + "\'");
			return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询评教问题,tid=null时用mid查，mid=null时用tid查
	public static List<Question> QueryAssessQuestions(String tid, String mid)
	{
		List<Question> qslist = new ArrayList<Question>();
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		String modelid;
		Statement stat = null;
		ResultSet res = null;
		try
		{
			stat = conn.createStatement();
			if (mid == null)
			{
				res = stat.executeQuery("select model from started_assess where Tno=\'" + tid + "\'");
				res.next();
				modelid = res.getString("model");
			}
			else
				modelid = mid;
			res = stat.executeQuery("select * from " + modelid);
			while (res.next())
			{
				Question qst = new Question();
				qst.qs = res.getString("question");
				qst.ansA = res.getString("A");
				qst.ansB = res.getString("B");
				qst.ansC = res.getString("C");
				qst.ansD = res.getString("D");
				qslist.add(qst);
			}
			return qslist;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 提交评教
	public static boolean SubmitAssess(String sid, String tid, String[] answer)
	{
		String anslen = String.valueOf(answer.length);
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return false;
		}
		Statement stat = null;
		ResultSet res = null;
		try
		{
			stat = conn.createStatement();
			if (stat.executeQuery("select * from total_assesses where Sno=\'" + sid + "\' and Tno=\'" + tid + "\'")
					.next())
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "你已经评教完成，请返回";
				return false;// 查询错误
			}
			res = stat.executeQuery("select model from started_assess where Tno=\'" + tid + "\'");// 获取模板id
			if (res.next())
			{
				String modelId = res.getString("model");
				stat.executeUpdate("Insert into total_assesses (Tno,Sno,model,qs_num)" + " values(\'" + tid + "\',\'"
						+ sid + "\',\'" + modelId + "\',\'" + anslen + "\')");
				for (int i = 1; i <= answer.length; i++)
					stat.executeUpdate("update total_assesses set Q" + i + "=\'" + answer[i - 1] + "\' where Tno=\'"
							+ tid + "\' and Sno=\'" + sid + "\'");
				stat.executeUpdate("update students_assess set T" + tid + "=\'Y\' where Sno=\'" + sid + "\'");
				return true;
			}
			return false;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询已经评价的老师
	public static List<UserBean> QueryAssessedTeacher(String sid)
	{
		List<UserBean> list = new ArrayList<UserBean>();
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Statement stat = null, stat2 = null;
		ResultSet res = null, res2 = null;
		try
		{
			stat = conn.createStatement();
			stat2 = conn.createStatement();
			res = stat.executeQuery("select Tno from total_assesses where Sno=\'" + sid + "\'");
			while (res.next())
			{
				UserBean us = new UserBean();
				us.id = res.getString("Tno");
				res2 = stat2.executeQuery("select teachers.name Tname,teachers.Cno cid,courses.name Cname "
						+ "from teachers,courses where teachers.Cno!=\'\' and Tno=\'" + us.id
						+ "\' and teachers.Cno=courses.Cno");
				res2.next();
				us.name = res2.getString("Tname");
				us.courseId = res2.getString("cid");
				us.courseName = res2.getString("Cname");
				list.add(us);
				res2.close();
			}

			return list;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询评教历史信息
	public static List<Question> QueryAssessInfo(String sid, String tid)
	{
		List<Question> list = new ArrayList<Question>();
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Statement stat = null, stat2 = null;
		ResultSet res = null, res2 = null;
		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery("select * from total_assesses where Sno=\'" + sid + "\' and Tno=\'" + tid + "\'");
			res.next();
			String mid = res.getString("model");// 获取model id
			stat2 = conn.createStatement();
			res2 = stat2.executeQuery("select * from " + mid);// 查询问题题目

			int i = 1;
			while (res2.next())
			{
				Question qs = new Question();
				qs.qs = res2.getString("question");
				qs.answer = res.getString("Q" + i);
				i++;
				qs.answer += res2.getString(qs.answer);
				list.add(qs);
			}
			return list;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询评教模板//mid="ALL"查询全部返回模板名称和id
	public static List<Model> QueryModel(String mid)
	{
		List<Model> list = new ArrayList<Model>();
		String SqlCmd;
		if (!mid.equals("ALL"))
			SqlCmd = "select * from model_index where model_id =\'" + mid + "\'";
		else
			SqlCmd = "select * from model_index";

		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Statement stat = null;
		ResultSet res = null;

		try
		{
			stat = conn.createStatement();
			res = stat.executeQuery(SqlCmd);
			while (res.next())
			{
				Model m = new Model();
				m.mid = res.getString("model_id");
				m.mname = res.getString("name");
				list.add(m);
			}
			return list;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 发起评教
	public static boolean UpdateStartAssess(String tno, String modelid, String starterid, String stime, String etime)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0;
			return false;
		}

		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			ResultSet res = stat.executeQuery("select * from teachers where Tno=\'" + tno + "\'");
			if (res.next())
			{
				if (res.getString("Cno") != null)
				{
					stat.execute("delete from started_assess where Tno=\'" + tno + "\'");
					stat.executeUpdate("insert into started_assess values(\'" + tno + "\',\'" + modelid + "\',\'"
							+ starterid + "\',\'" + stime + "\',\'" + etime + "\')");
					stat.executeUpdate("update students_assess set T" + tno + "=\'N\'");
					stat.executeUpdate("delete from total_assesses where Tno=\'" + tno + "\'");
					return true;
				}
				else
				{
					ErrorCode = 2;
					ErrorMsg[ErrorCode] = tno + "教师没有有效课程！";
					return false;
				}
			}
			else
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = tno + "教师不存在！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 删除模板
	public static boolean DeleteModel(String mid)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return false;
		}
		Statement stat = null;
		try
		{
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String mydate = df.format(date);
			stat = conn.createStatement();
			ResultSet res = stat.executeQuery("select * from started_assess where model=\'" + mid + "\' and \'" + mydate
					+ "\' between StartTime and EndTime");
			if (res.next())
			{
				ErrorCode = 2;
				ErrorMsg[ErrorCode] = "当前模板正在使用中，不能删除！";
				return false;
			}

			res = stat.executeQuery("select Tno from started_assess where model=\'" + mid + "\'");
			if (res.next())
			{
				String tno = res.getString("Tno");
				stat.executeUpdate("update students_assess set T" + tno + "=\'N\'");// 评教无效
			}

			stat.execute("delete from model_index where model_id=\'" + mid + "\'");
			stat.execute("drop table " + mid);
			stat.execute("update started_assess set model=\'\' where model=\'" + mid + "\'");
			stat.execute("delete from total_assesses where model=\'" + mid + "\'");

			return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 添加和修改模板option="ADD"添加或"MODIFY"修改
	public static boolean AddOrModifyModel(List<Question> qst, String mid, String name, String option)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return false;
		}

		Statement stat = null;
		try
		{
			stat = conn.createStatement();
			if (option.equals("ADD"))// 添加
			{
				stat.execute("CREATE TABLE `" + mid + "` ("
						+ " `Qno` int(2) unsigned NOT NULL AUTO_INCREMENT COMMENT '题号索引',"
						+ "`question` varchar(255) COLLATE utf8_general_mysql500_ci NOT NULL DEFAULT '' COMMENT '具体问题',"
						+ " `A` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '优' COMMENT '选项A',"
						+ " `B` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '良' COMMENT '选项B',"
						+ "`C` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '中',"
						+ "`D` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '差'," + "PRIMARY KEY (`Qno`)"
						+ ") ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci");

				for (int i = 0; i < qst.size(); i++)
					stat.executeUpdate("insert into " + mid + " values(\'" + (i + 1) + "\',\'" + qst.get(i).qs + "\',\'"
							+ qst.get(i).ansA + "\',\'" + qst.get(i).ansB + "\',\'" + qst.get(i).ansC + "\',\'"
							+ qst.get(i).ansD + "\')");
				stat.executeUpdate("insert into model_index values(\'" + mid + "\',\'" + name + "\')");
			}
			else// 修改
			{
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String mydate = df.format(date);
				stat = conn.createStatement();
				ResultSet res = stat.executeQuery("select * from started_assess where model=\'" + mid + "\' and \'"
						+ mydate + "\' between StartTime and EndTime");
				if (res.next())
				{
					ErrorCode = 2;
					ErrorMsg[ErrorCode] = "当前模板正在使用中，不能进行修改！";
					return false;
				}
				res = stat.executeQuery("select Tno from started_assess where model=\'" + mid + "\'");
				if (res.next())
				{
					String tno = res.getString("Tno");
					stat.executeUpdate("update students_assess set T" + tno + "=\'N\'");// 评教无效
				}
				for (int i = 0; i < qst.size(); i++)
					stat.executeUpdate("update " + mid + " set question=\'" + qst.get(i).qs + "\'," + "A=\'"
							+ qst.get(i).ansA + "\'," + "B=\'" + qst.get(i).ansB + "\'," + "C=\'" + qst.get(i).ansC
							+ "\'," + "D=\'" + qst.get(i).ansD + "\' where Qno=" + (i + 1));
				stat.executeUpdate("update model_index set name=\'" + name + "\' where model_id=\'" + mid + "\'");
				stat.execute("delete from total_assesses where model=\'" + mid + "\'");
				stat.execute("delete from started_assess where model=\'" + mid + "\'");
			}
			return true;
		} catch (SQLException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = "SQL执行错误：" + e.getMessage();
			return false;// 查询错误
		} finally
		{
			try
			{
				if (stat != null)
					stat.close();
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}

	// 查询评教结果tid="ALL"查询所有老师
	public static List<AssRes> QueryAssResult(String tid)
	{
		List<AssRes> list = new ArrayList<AssRes>();
		Connection conn = getConnection();
		if (conn == null)
		{
			ErrorCode = 0; // 连接数据库失败
			return null;
		}
		Date date = new Date();// 获取当前时间
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String mydate = format.format(date);
		Statement stat = null, stat2 = null;
		ResultSet res = null, res2 = null;
		try
		{
			stat = conn.createStatement();
			if (tid.equals("ALL"))// 查询所有老师
				res = stat.executeQuery("select started_assess.* from started_assess,teachers "
						+ "where started_assess.Tno=teachers.Tno and teachers.Cno!=\'\'");
			else
				res = stat
						.executeQuery("select started_assess.* from started_assess,teachers where started_assess.Tno=\'"
								+ tid + "\' and started_assess.Tno=teachers.Tno and teachers.Cno!=\'\'");

			stat2 = conn.createStatement();
			while (res.next())// 选出所有老师如果查询制定老师while循环一次
			{
				AssRes ar = new AssRes();
				ar.tid = res.getString("Tno");
				ar.starterId = res.getString("StarterID");
				ar.modelId = res.getString("model");
				ar.startTime = res.getString("StartTime");
				ar.endTime = res.getString("EndTime");
				Date cuurdt = format.parse(mydate);
				Date start = format.parse(ar.startTime);
				Date end = format.parse(ar.endTime);
				if (cuurdt.before(start))
					ar.stat = "尚未开始";
				else if (cuurdt.after(start) && cuurdt.before(end))
					ar.stat = "进行中";
				else
					ar.stat = "已结束";
				res2 = stat2.executeQuery("select count(*) qsnum from " + ar.modelId);
				res2.next();
				int qsnum = Integer.parseInt(res2.getString("qsnum"));
				ar.qst = new Question[qsnum];
				res2 = stat2.executeQuery("select * from " + ar.modelId);
				int k = 0;
				while (res2.next())
				{
					ar.qst[k] = new Question();// 初始化
					ar.qst[k].qs = res2.getString("question");
					ar.qst[k].ansA = res2.getString("A");
					ar.qst[k].ansB = res2.getString("B");
					ar.qst[k].ansC = res2.getString("C");
					ar.qst[k].ansD = res2.getString("D");
					k++;
				}
				ar.totalRes = new QsAnswer[qsnum];
				for (int i = 0; i < qsnum; i++)
					ar.totalRes[i] = new QsAnswer();// 初始化
				res2 = stat2.executeQuery("select * from total_assesses where Tno=\'" + ar.tid + "\'");// 选出所有关于该老师的评价
				while (res2.next())// 统计每一个学生回答
				{
					for (int i = 1; i <= qsnum; i++)
						if (res2.getString("Q" + i).equals("A"))
							ar.totalRes[i - 1].countA++;
						else if (res2.getString("Q" + i).equals("B"))
							ar.totalRes[i - 1].countB++;
						else if (res2.getString("Q" + i).equals("C"))
							ar.totalRes[i - 1].countC++;
						else
							ar.totalRes[i - 1].countD++;
					ar.stuNum++;
				}
				// 统计每个问题及总体得分
				float temp = 0;
				for (int i = 0; i < qsnum; i++)
				{
					ar.totalRes[i].qsGrade = (float) ((25.0 * ar.totalRes[i].countD + 50.0 * ar.totalRes[i].countC
							+ 75.0 * ar.totalRes[i].countB + 100.0 * ar.totalRes[i].countA) / ar.stuNum);
					temp += ar.totalRes[i].qsGrade;
				}
				ar.totalGd = temp / qsnum;// 总体评价
				res2 = stat2.executeQuery("select name from model_index where model_id=\'" + ar.modelId + "\'");
				res2.next();
				ar.modelName = res2.getString("name");// 获取模板名称
				res2 = stat2.executeQuery("select name from teachers where Tno=\'" + ar.tid + "\'");
				res2.next();
				ar.tname = res2.getString("name");// 获取老师名字
				res2 = stat2.executeQuery("select name from assess_admin where ass_id=\'" + ar.starterId + "\'");
				res2.next();
				ar.starterName = res2.getString("name");// 获取管理员名字

				list.add(ar);// the most important step!
			}
			return list;
		} catch (SQLException | ParseException e)
		{
			ErrorCode = 1;
			ErrorMsg[ErrorCode] = e.getMessage();
			return null;// 查询错误
		} finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				ErrorCode = 3;
				ErrorMsg[ErrorCode] = "关闭连接错误：" + e.getMessage();// 关闭失败CLOSE_ERROR
			}
		}
	}
}
