package com.absences;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.bytecode.opencsv.CSVReader;

import com.absences.models.Student;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.common.base.Joiner;

import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet 
{
	static 
	{
		ObjectifyInitialize.Dummy();
    }

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
    {
    	Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        BlobKey bkey = blobs.get("myFile").get(0);
        if (bkey == null) 
        {
            res.sendRedirect("/");
        } 
        else 
        {
        	BlobstoreInputStream is = new BlobstoreInputStream(bkey);
        	CSVReader csvReader = new CSVReader(new InputStreamReader(is, "CP1252"),';');
        	List<String[]> rows = csvReader.readAll();
        	csvReader.close();
        	
        	List<String> updateRes = updateDb(rows);
        	
        	res.setCharacterEncoding("UTF-8");
        	res.setContentType("text/html");
        	PrintWriter w = res.getWriter();
        	w.printf("<html><body><h1>Προβλήματα Ενημέρωσης.</h1><ol>");
        	for (String error : updateRes)
        	{
        		w.printf("<li>%s</li>", error);
        	}
        	w.println("</table></ol></html>");
        	w.close();
        }
    }

    private List<String> updateDb(List<String[]> rows)
    {
    	List<String> errors = new ArrayList<String>();
    	Map<Long, String[]> validRows = new HashMap<Long, String[]>();
    	
    	int line = 0;
    	for (String[] row : rows)
    	{
    		try
    		{
	    		validRows.put(Long.parseLong(row[0]), row);
    		}
    		catch (Exception ex)
    		{
    			errors.add("Γραμμή " + (line + 1) + ". Ο AM [" + row[0] + "] δεν είναι αριθμός.");
    		}
    	}
    	Set<Long> existingIds = new HashSet<Long>(); 
    	Map<Long, Student> existingStudents = ofy().load().type(Student.class).ids(validRows.keySet());

    	List<Student> studentsToSave = new ArrayList<Student>();
    	
    	for (Student student : existingStudents.values())
    	{
    		try
    		{
	    		String[] row = validRows.get(student.getStudentId());
	    		student.setFirstName(row[1]);
	    		student.setLastName(row[2]);
	    		student.setFatherName(row[3]);
	    		student.setMotherName(row[4]);
	    		
	    		// Add to save list
	    		studentsToSave.add(student);
	    		
	    		existingIds.add(student.getStudentId());
    		}
    		catch (Exception ex)
    		{
    			errors.add(ex.getMessage() + " " + Joiner.on(',').join(rows));
    		}
    	}
    	
    	for (long studentId : validRows.keySet())
    	{
    		if (existingIds.contains(studentId))
    			continue;
    		
    		// Add New Student
    		String[] row = validRows.get(studentId);
    		studentsToSave.add(new Student(studentId, row[1], row[2], row[3], row[4]));
    	}
    	
    	if (studentsToSave.size() > 0)
    		try
    		{
    			ofy().save().entities(studentsToSave);
    		}
    		catch (Exception ex)
    		{
    			errors.add(ex.getMessage());
    		}
    	
    	return errors;
    }
}