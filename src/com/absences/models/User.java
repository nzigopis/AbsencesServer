package com.absences.models;

import com.googlecode.objectify.annotation.*;

@Entity
public class User {
	@Id private String userName;	
	private String userPassword;
	
	@SuppressWarnings("unused")
	private User() {
		super();	
	}

	public User(String name, String password) {
		super();
		this.userName = name;
		this.userPassword = password;
	}	
	
	public String getUserName() {
		return this.userName;
	}

}
