package com.user;

public class OptLog
{
	public String datetime;
	public String userid;
	public String username;
	public String operation;
	public String details;

	public OptLog(String datetime, String userid, String username, String operation, String details)
	{
		super();
		this.datetime = datetime;
		this.userid = userid;
		this.username = username;
		this.operation = operation;
		this.details = details;
	}

}
