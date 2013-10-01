package com.absences;
import java.io.IOException;

import javax.servlet.http.*;

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
		LoadType<User> r = ofy().load().type(User.class);
		User u = null;
		if (r.count() == 0)
		{
			u = new User("admin", "admin");
			ofy().save().entity(u).now();
		}
		else
		{
			u = r.first().now();
		}
		Gson gson = new Gson();
		String res = gson.toJson(u);
		resp.setContentType("text/json");
		resp.getWriter().println(res);
	}
}
