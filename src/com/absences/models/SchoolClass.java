package com.absences.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SchoolClass 
{
	@Id private String classId;
	private String classDescription;
	
	public static final SchoolClass NullClass = new SchoolClass("", "");
	
	public SchoolClass() {}
	
	public SchoolClass(String classId, String classDescription) 
	{
		this.classId = classId;
		this.classDescription = classDescription;
	}
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassDescription() {
		return classDescription;
	}
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}
	
	
}
