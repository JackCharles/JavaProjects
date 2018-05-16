package com.user;

public class Event
{
	public String date;
	public int serial;
	public String handler1;

	public String handler2;
	public String type;
	public String subtype;
	public String rno;
	public String comment;
	public String confirm;
	public String filepath;
	public String approval;

	public Event(String date, int serial, String handler1, String handler2, String type, String subtype, String rno,
			String comment, String confirm, String path, String approval)
	{
		this.date = date;
		this.serial = serial;
		this.handler1 = handler1;
		this.handler2 = handler2;
		this.type = type;
		this.subtype = subtype;
		this.rno = rno;
		this.comment = comment;
		this.confirm = confirm;
		this.filepath = path;
		this.approval = approval;
	}

}
