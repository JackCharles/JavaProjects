package com.user;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tools
{
	public static String getMd5(String str)
	{
		if (str == null)
			return null;
		MessageDigest md;
		try
		{
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(32);
		} catch (NoSuchAlgorithmException e)
		{
			return null;
		}
	}

	public static void PrintErrorMsg(HttpServletRequest request, HttpServletResponse response, String msg)
	{
		try
		{
			msg = URLEncoder.encode(msg, "utf-8");
			request.getRequestDispatcher("OptFailed.jsp?errormsg=" + msg).forward(request, response);
		} catch (IOException | ServletException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static void PrintSuccessMsg(HttpServletRequest request, HttpServletResponse response, String redirect,
			String target)
	{
		if (target == null)
			target = "_self";
		try
		{
			redirect = URLEncoder.encode(redirect, "utf-8");
			request.getRequestDispatcher("OptSuccess.jsp?redirect=" + redirect + "&target=" + target).forward(request,
					response);
		} catch (IOException | ServletException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static String getDateTime()
	{
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return ft.format(new Date());
	}
}
