package com.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordBean
{
	String recordMainID = "";
	String recordMain = "";
	String recordAssist = "";
	String date = "";
	String type1 = "";
	String type2 = "";
	List<Map<String, String>> persons = new ArrayList<>();
	List<String> files = new ArrayList<>();
	String other = "";
	String approval = "";

	String number = "";
	String serial = "";
	String stat = "";

	public String getRecordMainID()
	{
		return recordMainID;
	}

	public void setRecordMainID(String recordMainID)
	{
		this.recordMainID = recordMainID;
	}

	public String getApproval()
	{
		return approval;
	}

	public void setApproval(String approval)
	{
		this.approval = approval;
	}

	@Override
	public String toString()
	{
		return "RecordBean{" + "recordMain='" + recordMain + '\'' + ", recordAssist='" + recordAssist + '\''
				+ ", date='" + date + '\'' + ", type1='" + type1 + '\'' + ", type2='" + type2 + '\'' + ", persons="
				+ persons + ", files=" + files + ", other='" + other + '\'' + ", number='" + number + '\''
				+ ", serial='" + serial + '\'' + ", stat='" + stat + '\'' + '}';
	}

	public String getRecordMain()
	{
		return recordMain;
	}

	public void setRecordMain(String recordMain)
	{
		this.recordMain = recordMain;
	}

	public String getRecordAssist()
	{
		return recordAssist;
	}

	public void setRecordAssist(String recordAssist)
	{
		this.recordAssist = recordAssist;
	}

	public String getType1()
	{
		return type1;
	}

	public void setType1(String type1)
	{
		this.type1 = type1;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public List<Map<String, String>> getPersons()
	{
		return persons;
	}

	public void setPersons(List<Map<String, String>> persons)
	{
		this.persons = persons;
	}

	public String getType2()
	{
		return type2;
	}

	public void setType2(String type2)
	{
		this.type2 = type2;
	}

	public List<String> getFiles()
	{
		return files;
	}

	public void setFiles(List<String> files)
	{
		this.files = files;
	}

	public String getOther()
	{
		return other;
	}

	public void setOther(String other)
	{
		this.other = other;
	}

	public String getSerial()
	{
		return serial;
	}

	public void setSerial(String serial)
	{
		this.serial = serial;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getStat()
	{
		return stat;
	}

	public void setStat(String stat)
	{
		this.stat = stat;
	}

}
