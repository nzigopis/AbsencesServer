package com.absences;

import com.absences.models.SchoolClass;
import com.absences.models.Student;
import com.absences.models.User;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyInitialize
{
	static {
	    ObjectifyService.register(User.class);
        ObjectifyService.register(SchoolClass.class);
        ObjectifyService.register(Student.class);
    }
	
	static void Dummy() {}
}
