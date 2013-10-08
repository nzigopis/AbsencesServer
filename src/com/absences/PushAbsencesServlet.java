package com.absences;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.absences.models.AbsencesLog;
import com.absences.models.SchoolClass;
import com.absences.models.Student;
import com.absences.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PushAbsencesServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	static 
	{
		ObjectifyInitialize.Dummy();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		String s = "";
		try
		{
			StringBuilder sb = new StringBuilder();
		    BufferedReader br = req.getReader();
		    String str;
		    while( (str = br.readLine()) != null ){
		        sb.append(str);
		    }  
		    
			Gson gson = new Gson();
			Type typeOfLog = new TypeToken<Collection<AbsencesLog>>(){}.getType();
			Object log = gson.fromJson(req.getReader(), typeOfLog);
			System.out.println(str);
		}
		catch (Exception ex)
		{
			s = ex.getMessage();
		}
		resp.setContentType("text/json; charset=utf-8");
//		resp.setCharacterEncoding("UTF-8");
//		resp.getWriter().println(s);
		resp.getWriter().close();
	}
}

