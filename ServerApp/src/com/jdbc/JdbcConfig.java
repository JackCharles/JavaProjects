package com.jdbc;

public interface JdbcConfig
{
	static final String DRIVER = "com.mysql.jdbc.Driver";
	static final String URL = "jdbc:mysql://localhost/web_server";
	static final String USERNAME = "root";
	static final String PASSWORD = "root";
	static final String BASEDIR = "E:/uploaded/";// 上传文件路径
	static final String BACKUPDIR = "D:/database_backup/";
	static final String TEMPDIR = "E:/TEMP";
}
