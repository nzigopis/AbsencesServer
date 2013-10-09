package com.absences;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.absences.models.AbsencesLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PushAbsencesServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	static 
	{
		ObjectifyInitialize.Dummy();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		try
		{
			Gson gson = new Gson();
			Type typeOfLog = new TypeToken<Collection<AbsencesLog>>(){}.getType();
			Object log = gson.fromJson(req.getReader(), typeOfLog);
			System.out.println(log);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		resp.setContentType("text/json; charset=utf-8");
		resp.getWriter().close();
	}
}

