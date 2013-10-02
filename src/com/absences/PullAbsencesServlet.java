package com.absences;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.*;

import com.absences.models.SchoolClass;
import com.absences.models.Student;
import com.absences.models.User;
import com.google.gson.*;
import com.googlecode.objectify.cmd.LoadType;

import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class PullAbsencesServlet extends HttpServlet 
{
	static 
	{
		ObjectifyInitialize.Dummy();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		String s = "";
		try
		{
			List<User> users = ofy().load().type(User.class).list();
		
			List<SchoolClass> classes = ofy().load().type(SchoolClass.class).list();
			List<Student> students = ofy().load().type(Student.class).list();
			
			Gson gson = new Gson();
			s = String.format("{\"users\": %s, \"classes\": %s, \"students\": %s}", 
				gson.toJson(users), gson.toJson(classes), gson.toJson(students));
		}
		catch (Exception ex)
		{
			s = ex.getMessage();
		}
		resp.setContentType("text/json");
		resp.getWriter().println(s);
		resp.getWriter().close();
	}
}
