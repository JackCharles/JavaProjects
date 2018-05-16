package com.user;

import java.util.ArrayList;
import java.util.List;

public class UserBean
{
	public String name;
	public String id;
	public String sex;
	public String role;
	public String Sclass;// 此属性只有学生有
	public String phone;
	public String address;
	public String courseId;// 此属性只有老师有效
	public String courseName;//
	public List<String> Tclass = new ArrayList<String>();// 此属性只有老师有

}