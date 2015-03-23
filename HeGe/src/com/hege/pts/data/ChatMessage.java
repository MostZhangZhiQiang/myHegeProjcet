package com.hege.pts.data;

import java.util.Date;

public class ChatMessage
{
	private String msg;
	private Type type;
	private Date date;

	public ChatMessage(String msg, Type type, Date date) {
		this.msg = msg;
		this.type = type;
		this.date = date;
	}
	public enum Type
	{
		INCOMING, OUTCOMING
	}
	
	public ChatMessage()
	{
	}
	
	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}

}
