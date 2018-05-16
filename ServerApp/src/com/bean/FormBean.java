package com.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormBean
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

	String serial = "";
	String isform = "";

	public String getRecordMainID()
	{
		return recordMainID;
	}

	public void setRecordMainID(String recordMainID)
	{
		this.recordMainID = recordMainID;
	}

	public FormBean()
	{
	}

	public String getSerial()
	{
		return serial;
	}

	public String getIsform()
	{
		return isform;
	}

	public void setIsform(String isform)
	{
		this.isform = isform;
	}

	public void setSerial(String serial)
	{
		this.serial = serial;
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

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getType1()
	{
		return type1;
	}

	public void setType1(String type1)
	{
		this.type1 = type1;
	}

	public String getType2()
	{
		return type2;
	}

	public void setType2(String type2)
	{
		this.type2 = type2;
	}

	public List<Map<String, String>> getPersons()
	{
		return persons;
	}

	public void setPersons(List<Map<String, String>> persons)
	{
		this.persons = persons;
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

	@Override
	public String toString()
	{
		return "FormBean{" + "recordMain='" + recordMain + '\'' + ", recordAssist='" + recordAssist + '\'' + ", date='"
				+ date + '\'' + ", type1='" + type1 + '\'' + ", type2='" + type2 + '\'' + ", persons="
				+ persons.toString() + ", files=" + files.toString() + ", other='" + other + '\'' + '}';
	}

}
