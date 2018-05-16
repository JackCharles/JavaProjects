package com.user;

public class AssRes
{
	public String tid;
	public String tname;
	public QsAnswer totalRes[];// 每个老师关于每个问题的每个选项的统计，多少个问题动态申请
	public float totalGd;// 总成绩
	public String stat;// 评教状态
	public String starterId;// 发起者
	public String starterName;
	public String startTime;
	public String endTime;
	public String modelId;
	public String modelName;
	public Question qst[];// 返回具体问题
	public int stuNum = 0;
}
