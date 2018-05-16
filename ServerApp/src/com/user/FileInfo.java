package com.user;

import java.io.File;
import java.text.DecimalFormat;

public class FileInfo
{
	public static long getFileSize(File f)
	{
		if (!f.exists())
			return 0L;

		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++)
		{
			if (flist[i].isDirectory())
			{
				size = size + getFileSize(flist[i]);
			}
			else
			{
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String FormetFileSize(long fileS)
	{// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024)
		{
			fileSizeString = df.format((double) fileS) + "B";
		}
		else if (fileS < 1048576)
		{
			fileSizeString = df.format((double) fileS / 1024) + "K";
		}
		else if (fileS < 1073741824)
		{
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		}
		else
		{
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static long getFileCount(File f)
	{
		if (!f.exists())
			return 0L;
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++)
		{
			if (flist[i].isDirectory())
			{
				size = size + getFileCount(flist[i]);
				size--;
			}
		}
		return size;

	}
}
