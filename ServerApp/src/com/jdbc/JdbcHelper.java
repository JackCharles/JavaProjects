package com.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bean.RecordBean;
import com.user.Event;
import com.user.EventType;
import com.user.OptLog;
import com.user.Tools;
import com.user.User;

public class JdbcHelper implements JdbcConfig
{
	public static String ErrorMsg;

	// 获取数据库连接
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			if (conn == null)
				ErrorMsg = "连接数据库失败，请确认数据库开启，并保持网络畅通！";
			return conn;
		} catch (Exception e)
		{
			ErrorMsg = "数据库连接失败：" + e.getMessage();
			e.printStackTrace();
			return null;
		}
	}

	// 用户校验:成功返回user对象，失败返回null
	public static User VerifyUser(String id, String passwd)
	{
		User user = null;
		String sql = "select * from users where id=? and password=?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, passwd);

			res = ps.executeQuery();
			if (res.next())
			{
				user = new User(res.getString(1), res.getString(3), res.getInt(4), res.getString(5), res.getString(6),
						res.getString(7), res.getString(8), res.getString(9));
				return user;
			}
			else
			{
				ErrorMsg = "用户名或密码错误，请重试！";
				return null;
			}
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	public static boolean ChangePassword(String id, String oldpasswd, String newpasswd)
	{
		String sql = "update users set password=? where id=? and password=?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;
			ps = conn.prepareStatement(sql);
			ps.setString(1, newpasswd);
			ps.setString(2, id);
			ps.setString(3, oldpasswd);

			if (ps.executeUpdate() == 1)
				return true;
			else
			{
				ErrorMsg = "用户原密码错误，请重试。";
				return false;
			}
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	public static int GetSerialNum(String name, String date)// 返回最大可用序列号
	{
		String sql = "select maxsno from serialmanager where date=? and handler=?";
		String sql3 = "Insert into serialmanager values(?,?,1)";
		Connection conn = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return -1;
			ps = conn.prepareStatement(sql);
			ps.setDate(1, Date.valueOf(date));
			ps.setString(2, name);
			res = ps.executeQuery();
			if (res.next())
				return res.getInt(1);
			else
			{
				ps2 = conn.prepareStatement(sql3);
				ps2.setDate(2, Date.valueOf(date));
				ps2.setString(1, name);
				ps2.executeUpdate();
				ps2.close();
				return 1;
			}
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return -1;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return -1;
			}
		}
	}

	public static boolean AddEvent(String[] param)
	{
		String sql1 = "select * from events where date=? and serial=? and handler1=?";
		String sql2 = "insert into events values(?,?,?,?,?,?,?,?,?,?,?)";
		String sql3 = "insert into parties values(?,?,?,?,?)";
		// if (param[7] == null || param[7].equals(""))// 案卷号
		// param[7] = "####";

		int serial = GetSerialNum(param[1], param[0]);
		String Folder = JdbcConfig.BASEDIR + param[1] + "/" + param[0].replaceAll("-", "") + "_" + serial + "_"
				+ param[3] + "-" + param[4];
		String[] parties = param[5].split("\r\n");// 注意不可见字符，会导致数据库查询出错
		for (int i = 0; i < parties.length; ++i)
		{
			String name[] = parties[i].split("@");
			Folder += "_" + name[0];
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;
			ps = conn.prepareStatement(sql1);
			ps.setDate(1, Date.valueOf(param[0]));
			ps.setInt(2, serial);
			ps.setString(3, param[1]);
			res = ps.executeQuery();
			if (res.next())
			{
				ErrorMsg = "记录已存在，请勿重新添加！";
				return false;
			}
			else
			{
				// 写入Events表
				ps.close();
				ps = conn.prepareStatement(sql2);
				ps.setDate(1, Date.valueOf(param[0]));
				ps.setInt(2, serial);
				ps.setString(3, param[1]);
				ps.setString(4, param[2]);
				ps.setString(5, param[3]);
				ps.setString(6, param[4]);
				ps.setString(7, param[7]);
				ps.setString(8, param[6]);
				ps.setString(9, (param[7] == null || param[7].equals("")) ? "N" : "Y");
				ps.setString(10, Folder);
				ps.setString(11, param[8]);
				ps.executeUpdate();
				ps.close();

				// 更新当事人表
				ps = conn.prepareStatement(sql3);
				for (int i = 0; i < parties.length; ++i)
				{
					String name[] = parties[i].split("@");
					ps.setDate(1, Date.valueOf(param[0]));
					ps.setInt(2, serial);
					ps.setString(3, param[1]);// handler1
					ps.setString(4, name[0]);// name
					ps.setString(5, name[1]);// id
					ps.executeUpdate();
				}

				ps.close();
				ps = conn.prepareStatement("update serialmanager set maxsno=? where handler=? and date=?");
				ps.setInt(1, serial + 1);
				ps.setString(2, param[1]);
				ps.setDate(3, Date.valueOf(param[0]));
				ps.executeUpdate();
				ps.close();

				// 更新使用频率
				ps = conn.prepareStatement(
						"update typemanager set frequency=frequency+1 where fathertype=? and childtype=?");
				ps.setString(1, param[3]);
				ps.setString(2, param[4]);
				ps.executeUpdate();
				return true;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库操作错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 获取按条件检索结果,当justself为true时，必须设置handler
	public static ArrayList<Event> GetSearchRes(String date, String handler, String party, String partyid, String rno,
			String type, int page)
	{
		String sql = "select top 20 date, serial, handler1, handler2, type, subtype, rno, comment, confirmed, filepath, approval from "
				+ "(select ROW_NUMBER() over(order by date desc, serial asc) as rowno, * from "
				+ "(select distinct e.* from events e, parties p where 1=1";

		if (page < 1)
			page = 1;

		if (date != null && !date.equals(""))// 日期
			sql += " and e.date='" + date + "'";

		if (handler != null && !handler.equals(""))// 办案人
			sql += " and e.handler1='" + handler + "'";

		boolean isp = false;
		if (party != null && !party.equals(""))// 当事人姓名
		{
			sql += " and p.partyname = '" + party + "'";
			isp = true;
		}

		if (partyid != null && !partyid.equals(""))// 当事人身份证号
		{
			sql += " and p.partyid = '" + partyid + "'";
			isp = true;
		}

		if (isp)// 表连接
			sql += " and e.date = p.date and e.serial = p.serial and e.handler1 = p.handler";

		if (rno != null && !rno.equals(""))// 案卷号
			sql += " and rno='" + rno + "'";

		if (type.equals("民事") || type.equals("经济") || type.equals("涉外") || type.equals("其它"))// 检索类型
			sql += " and type='" + type + "'";
		else if (type.equals("N") || type.equals("Y"))
			sql += " and confirmed='" + type + "'";

		sql += ") as A) as B where rowno >" + ((page - 1) * 20);

		System.out.println(sql);

		ArrayList<Event> li = new ArrayList<Event>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				li.add(new Event(res.getDate(1).toString(), res.getInt(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9),
						res.getString(10), res.getString(11)));
			}
			return li;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 按类型获取所有案件，用于打印
	public static ArrayList<Event> GetAllEventsByType(String type)
	{
		String sql = "select * from events where 1=1";
		if (type.equals("民事") || type.equals("经济") || type.equals("涉外") || type.equals("其它"))// 检索类型
			sql += " and type='" + type + "'";
		else if (type.equals("N") || type.equals("Y"))
			sql += " and confirmed='" + type + "'";

		System.out.println(sql);

		ArrayList<Event> li = new ArrayList<Event>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				li.add(new Event(res.getDate(1).toString(), res.getInt(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9),
						res.getString(10), res.getString(11)));
			}
			return li;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 获取案件当事人
	public static ArrayList<User> GetParties(String date, int serial, String handler)
	{
		String sql = "select partyname,partyid from parties where date=? and serial=? and handler=?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		ArrayList<User> parties = new ArrayList<User>();
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			ps.setDate(1, Date.valueOf(date));
			ps.setInt(2, serial);
			ps.setString(3, handler);
			res = ps.executeQuery();
			while (res.next())
			{
				User us = new User();
				us.setId(res.getString(2));
				us.setUserName(res.getString(1));
				parties.add(us);
			}
			return parties;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 删除案件
	public static boolean DeleteEvent(String date, int serial, String handler)// 流水号不会因此而复用
	{
		String sql = "select filepath from events where date=? and serial=? and handler1=?";
		String sql1 = "delete from events where date=? and serial=? and handler1=?";
		String sql2 = "delete from parties where date=? and serial=? and handler=?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet reset = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;
			ps = conn.prepareStatement(sql);
			ps.setDate(1, Date.valueOf(date));
			ps.setInt(2, serial);
			ps.setString(3, handler);
			reset = ps.executeQuery();
			if (reset.next())
			{
				// 删除文件
				File file = new File(reset.getString(1));
				File temp[] = file.listFiles();
				if (temp != null)
					for (int i = 0; i < temp.length; ++i)
						temp[i].delete();
				file.delete();
			}
			else
				return false;
			ps.close();

			ps = conn.prepareStatement(sql1);
			ps.setDate(1, Date.valueOf(date));
			ps.setInt(2, serial);
			ps.setString(3, handler);
			int res = ps.executeUpdate();
			ps.close();
			ps = conn.prepareStatement(sql2);
			ps.setDate(1, Date.valueOf(date));
			ps.setInt(2, serial);
			ps.setString(3, handler);
			res *= ps.executeUpdate();
			if (res == 0)
			{
				ErrorMsg = "数据库删除操作出错！";
				return false;
			}
			return true;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库删除错误：" + e.getMessage();
			e.printStackTrace();
			return true;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 查询用户信息
	public static ArrayList<User> QueryAllUsers()
	{
		String sql = "select * from users where id!='0000'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		ArrayList<User> li = new ArrayList<User>();
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;

			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				li.add(new User(res.getString(1), res.getString(3), res.getInt(4), res.getString(5), res.getString(6),
						res.getString(7), res.getString(8), res.getString(9)));
			}

			return li;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库删除错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 修改用户信息
	public static boolean ModifyUsers(String uid, String name, int role, String sex, String position, String id,
			String phone, String address, String passwd)
	{
		String sql = "update users set name=?, role=?, sex=?, position=?, iid=?, phone=?, address=? where id=?";
		if (passwd != null)// 带密码的更新
			sql = "update users set name=?, role=?, sex=?, position=?, iid=?, phone=?, address=?, password=? where id=?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setInt(2, role);
			ps.setString(3, sex);
			ps.setString(4, position);
			ps.setString(5, id);
			ps.setString(6, phone);
			ps.setString(7, address);
			if (passwd != null)
			{
				ps.setString(8, passwd);
				ps.setString(9, uid);
			}
			else
				ps.setString(8, uid);
			if (ps.executeUpdate() != 0)
				return true;
			else
			{
				ErrorMsg = "未知的数据库错误！！！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 删除用户信息
	public static boolean DeleteUser(String uid)
	{
		String sql = "delete from users where id=?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, uid);
			if (ps.executeUpdate() != 0)
				return true;
			else
			{
				ErrorMsg = "未知的数据库错误！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库删除错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 添加用户
	public static boolean AddUser(String uid, String name, int role, String sex, String position, String id,
			String phone, String address, String passwd)
	{
		String sql = "insert into users values(?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement("select id from users where id=?");
			ps.setString(1, uid);
			ResultSet res = ps.executeQuery();
			if (res.next())
			{
				ErrorMsg = "待添加的用户已存在，请勿重复添加！";
				res.close();
				return false;
			}
			res.close();
			ps.close();

			ps = conn.prepareStatement(sql);
			ps.setString(1, uid);
			ps.setString(2, passwd);
			ps.setString(3, name);
			ps.setInt(4, role);
			ps.setString(5, id);
			ps.setString(6, sex);
			ps.setString(7, position);
			ps.setString(8, phone);
			ps.setString(9, address);

			if (ps.executeUpdate() != 0)
				return true;
			else
			{
				ErrorMsg = "未知的数据库错误！！！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 查询事件类型
	public static ArrayList<EventType> QueryAllType()
	{
		String sql = "select fathertype, childtype from typemanager order by frequency desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ArrayList<EventType> tl = new ArrayList<EventType>();
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;

			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
				tl.add(new EventType(res.getString(1), res.getString(2)));
			return tl;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 查询事件类型
	public static boolean DeleteType(String fa, String sub)
	{
		String sql = "delete from typemanager where fathertype=? and childtype=?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, fa);
			ps.setString(2, sub);
			return ps.executeUpdate() > 0 ? true : false;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 添加事件类型
	public static boolean AddType(String fa, String sub)
	{
		String sql = "insert into typemanager values(?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, fa);
			ps.setString(2, sub);
			return ps.executeUpdate() > 0 ? true : false;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 添加案卷号
	public static boolean AddRno(String date, int serial, String handler, String rno, String approval)
	{
		String sql = "update events set rno=?, confirmed='Y',approval=? where date=? and serial=? and handler1=?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, rno);
			ps.setString(2, approval);
			ps.setDate(3, Date.valueOf(date));
			ps.setInt(4, serial);
			ps.setString(5, handler);
			return ps.executeUpdate() > 0 ? true : false;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 查询所有日志
	public static ArrayList<OptLog> QueryAllLog(String type, int page)
	{
		String sql = "select top 20 CONVERT(varchar, datetime, 120 ),userid,username, opration,details"
				+ " from (select row_number() over(order by datetime desc) "
				+ "as rownumber,* from operationlog) A where rownumber >" + ((page - 1) * 20);
		if (!type.equals("全部事件"))
			sql += " and opration='" + type + "'";

		System.out.println(sql);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		ArrayList<OptLog> li = new ArrayList<OptLog>();
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;

			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				li.add(new OptLog(res.getString(1), res.getString(2), res.getString(3), res.getString(4),
						res.getString(5)));
			}
			return li;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 添加一条日志
	public static boolean AddLog(String datetime, String uid, String name, String opt, String detials)
	{
		String sql = "insert into operationlog values(?,?,?,?,?)";

		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, datetime);
			ps.setString(2, uid);
			ps.setString(3, name);
			ps.setString(4, opt);
			ps.setString(5, detials);
			if (ps.executeUpdate() > 0)
				return true;
			else
			{
				ErrorMsg = "插入数据失败！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 删除日志
	public static boolean DeleteLog(String startDate, String endDate)
	{
		String sql = "delete from operationlog where datetime between ? and ?";

		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, startDate);
			ps.setString(2, endDate);
			if (ps.executeUpdate() >= 0)
				return true;
			else
			{
				ErrorMsg = "未删除数据，可能是选择的日期范围内没有日志记录。";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 备份数据库
	public static boolean BackupDatabase()
	{
		String sql = "backup database server_database to disk=?";
		String time = Tools.getDateTime();
		time = time.replace('-', '_').replace(' ', '_').replace(':', '_');
		String path = JdbcConfig.BACKUPDIR + "sqlserver_backup_" + time + ".bak";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, path);
			ps.execute();
			return true;

		} catch (SQLException e)
		{
			ErrorMsg = "数据库备份错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	/////////////////// 以下为App服务
	public static ArrayList<String> getFileList(String date, int serial, String handler)
	{
		String sql = "select filename from filetoupload where date = '" + date + "' and serial = " + serial
				+ " and handler = '" + handler + "'";

		System.out.println(sql);
		ArrayList<String> li = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				li.add(res.getString(1));
			}
			return li;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	// 删除文件
	public static boolean deleteFileName(String date, int serial, String handler, String filename)
	{
		String sql = "delete from filetoupload where date=? and serial = ? and handler = ? and filename = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setInt(2, serial);
			ps.setString(3, handler);
			ps.setString(4, filename);

			if (ps.executeUpdate() != 0)
			{

				return true;
			}
			else
			{
				ErrorMsg = "未知的数据库错误！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库删除错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 添加文件名
	public static boolean addFileName(String date, String serial, String handler, String filename)
	{
		String sql = "insert into filetoupload values(?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;

			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, serial);
			ps.setString(3, handler);
			ps.setString(4, filename);

			if (ps.executeUpdate() != 0)
				return true;
			else
			{
				ErrorMsg = "未知的数据库错误！！！";
				return false;
			}

		} catch (SQLException e)
		{
			ErrorMsg = "数据库更新错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 查询文件路径
	public static String getFilePath(String date, int serial, String handler)
	{
		String sql = "select filepath from events where handler1 = '" + handler + "' and serial = " + serial
				+ " and date = '" + date + "' ";
		System.out.println(sql);
		String s = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				s = res.getString(1);
			}
			return s;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}

	}

	// 查询未审批记录数目
	public static int getUnconfirmCount(String handler)
	{
		String sql = "select distinct count(e.serial) from events e where handler1 = '" + handler
				+ "' and confirmed = 'N' ";
		System.out.println(sql);
		int count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return -1;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				count = res.getInt(1);
			}
			return count;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return -1;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return -1;
			}
		}
	}

	// 查询最近15天记录 或未审批记录
	public static ArrayList<Event> getRecentRes(String handler)
	{
		String sql = "select distinct e.* from events e, parties p where handler1 = '" + handler
				+ "' and (e.date <=getdate() and e.date >= dateadd(day,-90,getdate())  or e.confirmed = 'N' )";

		System.out.println(sql);
		ArrayList<Event> li = new ArrayList<Event>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			while (res.next())
			{
				li.add(new Event(res.getDate(1).toString(), res.getInt(2), res.getString(3), res.getString(4),
						res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9),
						res.getString(10), res.getString(11)));
			}
			return li;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	public static boolean changeRno(String date, String serial, String handler1, String rno, String approval)
	{
		String sql = "update events set rno=? , confirmed = 'Y' , approval = ? where date = ? and serial = ? and handler1 = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;
			ps = conn.prepareStatement(sql);
			ps.setString(1, rno);
			ps.setString(2, approval);
			ps.setString(3, date);
			ps.setString(4, serial);
			ps.setString(5, handler1);

			if (ps.executeUpdate() == 1)
				return true;
			else
			{
				ErrorMsg = "审批记录时，数据库更新失败";
				return false;
			}
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}

	// 查找未提交完整的文件对应的记录
	public static RecordBean getUnfinFileRecord(String handler)
	{
		String sql = "select distinct e.*,f.filename from events e, filetoupload f where e.handler1 = '" + handler
				+ "' and e.handler1 = f.handler and e.date = f.date and e.serial = f.serial ";
		System.out.println(sql);
		RecordBean li = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();
			List<String> pathList = new ArrayList<>();
			if (res.next())
			{
				li = new RecordBean();
				li.setDate(res.getDate(1).toString());
				li.setSerial(String.valueOf(res.getInt(2)));
				li.setRecordMain(res.getString(3));
				li.setRecordAssist(res.getString(4));
				li.setType1(res.getString(5));
				li.setType2(res.getString(6));
				li.setNumber(res.getString(7));
				li.setOther(res.getString(8));
				li.setStat(res.getString(9));
				li.setApproval(res.getString(11));
				pathList.add(res.getString(12));
			}
			while (res.next())
			{
				pathList.add(res.getString(12));
			}
			if (li != null)
			{
				li.setFiles(pathList);
				List<Map<String, String>> persons = JdbcHelper.getPersons(li.getRecordMain(), li.getSerial(),
						li.getDate());
				li.setPersons(persons);
				System.out.println("查找到未完成的记录为：" + li.toString());
			}
			else
			{
				System.out.println("没有未完成的记录");
			}
			return li;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	public static List<Map<String, String>> getPersons(String handler, String serial, String date)
	{
		String sql = "select partyname,partyid from parties where date = '" + date + "' and serial = " + serial
				+ " and handler = '" + handler + "'";
		System.out.println(sql);
		List<Map<String, String>> li = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return null;
			ps = conn.prepareStatement(sql);
			res = ps.executeQuery();

			while (res.next())
			{
				Map<String, String> map = new HashMap<>();
				map.put("personName", res.getString(1));
				map.put("personID", res.getString(2));
				li.add(map);
			}
			return li;
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return null;
			}
		}
	}

	public static boolean subSerial(String date, String handler)
	{
		String sql = "update serialmanager set maxsno = (maxsno-1) where handler=? and date=?";
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = getConnection();
			if (conn == null)
				return false;
			ps = conn.prepareStatement(sql);
			ps.setString(1, handler);
			ps.setString(2, date);
			if (ps.executeUpdate() == 1)
				return true;
			else
			{
				ErrorMsg = "流水号减一失败";
				return false;
			}
		} catch (SQLException e)
		{
			ErrorMsg = "数据库查询错误：" + e.getMessage();
			e.printStackTrace();
			return false;
		} finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e)
			{
				ErrorMsg = "关闭数据库连接错误：" + e.getMessage();
				e.printStackTrace();
				return false;
			}
		}
	}
}
