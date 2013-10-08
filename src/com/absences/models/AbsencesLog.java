package com.absences.models;

public class AbsencesLog
{
	private long id;
	private String stmtType;
	private long studentId;
	private String absencesDate;
	private int h1;
	private int h2;
	private int h3;
	private int h4;
	private int h5;
	private int h6;
	private int h7;
	
	long getId()
	{
		return id;
	}
	void setId(long id)
	{
		this.id = id;
	}
	String getStmtType()
	{
		return stmtType;
	}
	void setStmtType(String stmtType)
	{
		this.stmtType = stmtType;
	}
	long getStudentId()
	{
		return studentId;
	}
	void setStudentId(long studentId)
	{
		this.studentId = studentId;
	}
	String getAbsencesDate()
	{
		return absencesDate;
	}
	void setAbsencesDate(String absencesDate)
	{
		this.absencesDate = absencesDate;
	}
	int getH1()
	{
		return h1;
	}
	void setH1(int h1)
	{
		this.h1 = h1;
	}
	int getH2()
	{
		return h2;
	}
	void setH2(int h2)
	{
		this.h2 = h2;
	}
	int getH3()
	{
		return h3;
	}
	void setH3(int h3)
	{
		this.h3 = h3;
	}
	int getH4()
	{
		return h4;
	}
	void setH4(int h4)
	{
		this.h4 = h4;
	}
	int getH5()
	{
		return h5;
	}
	void setH5(int h5)
	{
		this.h5 = h5;
	}
	int getH6()
	{
		return h6;
	}
	void setH6(int h6)
	{
		this.h6 = h6;
	}
	int getH7()
	{
		return h7;
	}
	void setH7(int h7)
	{
		this.h7 = h7;
	}
}
