package com.user;

public class User
{
	private String UserId;
	private String UserName;
	private int Role;
	private String Id;// 身份证
	private String Sex;
	private String Position;
	private String Phone;

	public User(String userId, String userName, int role, String id, String sex, String position, String phone,
			String address)
	{
		super();
		UserId = userId;
		UserName = userName;
		Role = role;
		Id = id;
		Sex = sex;
		Position = position;
		Phone = phone;
		Address = address;
	}

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getSex()
	{
		return Sex;
	}

	public void setSex(String sex)
	{
		Sex = sex;
	}

	public String getPosition()
	{
		return Position;
	}

	public void setPosition(String position)
	{
		Position = position;
	}

	public String getPhone()
	{
		return Phone;
	}

	public void setPhone(String phone)
	{
		Phone = phone;
	}

	public String getAddress()
	{
		return Address;
	}

	public void setAddress(String address)
	{
		Address = address;
	}

	private String Address;

	public User()
	{
		// TODO 自动生成的构造函数存根
	}

	public String getUserId()
	{
		return UserId;
	}

	public void setUserId(String userId)
	{
		UserId = userId;
	}

	public String getUserName()
	{
		return UserName;
	}

	public void setUserName(String userName)
	{
		UserName = userName;
	}

	public int getRole()
	{
		return Role;
	}

	public void setRole(int role)
	{
		Role = role;
	}

}
