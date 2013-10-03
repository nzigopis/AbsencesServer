package com.absences;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

import com.absences.models.SchoolClass;
import com.absences.models.Student;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.common.base.Joiner;

import static com.googlecode.objectify.ObjectifyService.ofy;

import org.mozilla.universalchardet.UniversalDetector;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet 
{
	private final Charset CP1523 = Charset.forName("Cp1253");
	
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
        	List<String[]> rows = readUploadedBlob(bkey);
        	
        	List<String> updateRes = updateDb(rows);
        	
        	printUploadResult(res, updateRes);
        }
    }

	private void printUploadResult(HttpServletResponse res,
			List<String> updateRes) throws IOException
	{
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

	private List<String[]> readUploadedBlob(BlobKey bkey) throws IOException, UnsupportedEncodingException
	{
		Charset encoding = CP1523;
		
		BlobstoreInputStream is = new BlobstoreInputStream(bkey);
		String encName = detectEncoding(is);
		is.close();
		if (encName != null)
			encoding = Charset.forName(encName);
		
		is = new BlobstoreInputStream(bkey);
		
		CSVReader csvReader = new CSVReader(new InputStreamReader(is, encoding),';'); 
		List<String[]> rows = csvReader.readAll();
		csvReader.close();

		return rows;
	}
	
	private String detectEncoding(InputStream is) throws java.io.IOException 
	{
	    byte[] buf = new byte[4096];
	    UniversalDetector detector = new UniversalDetector(null);
	    int nread;
	    while ((nread = is.read(buf)) > 0 && !detector.isDone()) 
	    {
	      detector.handleData(buf, 0, nread);
	    }
	    detector.dataEnd();
	    String encoding = detector.getDetectedCharset();
	    detector.reset();
	    
	    return encoding;
	  }

    private List<String> updateDb(List<String[]> rows)
    {
    	List<String> errors = new ArrayList<String>();
    	
    	Map<Long, String[]> validRows = getValidRows(rows, errors);
    	
    	List<Object> entitiesToSave = new ArrayList<Object>();
    	Set<Long> existingIds = updateExistingStudents(validRows, entitiesToSave, errors);
    	
    	createNewStudents(validRows, entitiesToSave, existingIds);
    	
    	if (entitiesToSave.size() > 0)
    		try
    		{
    			ofy().save().entities(entitiesToSave);
    		}
    		catch (Exception ex)
    		{
    			errors.add(ex.getMessage());
    		}
    	
    	return errors;
    }

	private void createNewStudents(Map<Long, String[]> validRows,
			List<Object> entitiesToSave, Set<Long> existingIds)
	{
		for (long studentId : validRows.keySet())
    	{
    		if (existingIds.contains(studentId))
    			continue;
    		
    		// Add New Student
    		String[] row = validRows.get(studentId);
    		Student s = new Student(studentId, row[1], row[2], row[3], row[4]);
    		entitiesToSave.add(s);
    		
    		SchoolClass c = ofy().load().type(SchoolClass.class).id(row[7]).now();
    		if (c == null)
    		{
    			c = new SchoolClass();
    			c.setClassId(row[7]);
    			c.setClassDescription(row[7]);
    			entitiesToSave.add(c);
    		}
    		s.setSchoolClass(c);
    	}
	}

	private Set<Long> updateExistingStudents(Map<Long, String[]> validRows,
			List<Object> entitiesToSave, List<String> errors)
	{
		Set<Long> existingIds = new HashSet<Long>(); 
		Map<Long, Student> existingStudents = ofy().load().type(Student.class).ids(validRows.keySet());

		for (Student student : existingStudents.values())
    	{
			String[] row = null;
    		try
    		{
	    		row = validRows.get(student.getStudentId());
	    		student.setFirstName(row[1]);
	    		student.setLastName(row[2]);
	    		student.setFatherName(row[3]);
	    		student.setMotherName(row[4]);
	    		
	    		// Add to save list
	    		entitiesToSave.add(student);
	    		existingIds.add(student.getStudentId());

	    		if (!student.getSchoolClass().getClassId().equalsIgnoreCase(row[7]))
	    		{
		    		SchoolClass c = ofy().load().type(SchoolClass.class).id(row[7]).now();
		    		if (c == null)
		    		{
		    			c = new SchoolClass();
		    			c.setClassId(row[7]);
		    			c.setClassDescription(row[7]);
		    			entitiesToSave.add(c);
		    		}
		    		student.setSchoolClass(c);
	    		}
    		}
    		catch (Exception ex)
    		{
    			String rowData = "";
    			if (row != null)
    				rowData = Joiner.on(',').join(row);
    			errors.add(ex.getMessage() + " " + rowData);
    		}
    	}
		return existingIds;
	}

	private Map<Long, String[]> getValidRows(List<String[]> rows, List<String> errors)
	{
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
		return validRows;
	}
}