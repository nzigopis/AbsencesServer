package com.absences.models;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Student {
	@Id private long studentId;
	private String firstName,lastName,fatherName,motherName;
	@SuppressWarnings("unused")
	private List<DailyAbsences> absences = new ArrayList<DailyAbsences>();
	@Load Ref<SchoolClass> attendsClass; 
	
	public Student() {}
	
	public Student(long studentId, String firstName,String lastName,String fatherName,String motherName) 
	{
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fatherName = fatherName;
		this.motherName = motherName;
	}
	
	public SchoolClass getSchoolClass() { return attendsClass == null ? SchoolClass.NullClass : attendsClass.get(); }
    public void setSchoolClass(SchoolClass value) { attendsClass = Ref.create(value); }
    
	public long getStudentId() { return studentId; }
	public void setStudentId(long studentId) { this.studentId = studentId; }
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
}
