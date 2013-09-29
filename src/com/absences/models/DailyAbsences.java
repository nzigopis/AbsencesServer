package com.absences.models;

import java.sql.Date;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;

@Embed
public class DailyAbsences {
	private Date date;
	private int[] absences = new int[7];
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getAbsenceForHour(int hour) {
		return absences[hour];
	}

	public void setAbsenceForHour(int hour, int value) {
		this.absences[hour] = value;
	}
	
}
