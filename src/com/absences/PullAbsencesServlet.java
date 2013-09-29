package com.absences;
import java.io.IOException;

import javax.servlet.http.*;

import com.absences.models.DailyAbsences;
import com.absences.models.SchoolClass;
import com.absences.models.Student;
import com.absences.models.User;
import com.google.gson.*;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.LoadType;

import static com.googlecode.objectify.ObjectifyService.factory;

@SuppressWarnings("serial")
public class PullAbsencesServlet extends HttpServlet {
	
	static {
	    ObjectifyService.register(User.class);
        ObjectifyService.register(SchoolClass.class);
        ObjectifyService.register(Student.class);
    }
	 
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Objectify ofy = factory().begin();
		LoadType<User> r = ofy.load().type(User.class);
		User u = null;
		if (r.count() == 0)
		{
			u = new User("admin", "admin");
			ofy.save().entity(u).now();
		}
		else
		{
			u = r.first().getValue();
		}
		Gson gson = new Gson();
		String res = gson.toJson(u);
		resp.setContentType("text/json");
		resp.getWriter().println(res);
	}
}
