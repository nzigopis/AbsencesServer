package com.absences;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.bytecode.opencsv.CSVReader;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        BlobKey bkey = blobs.get("myFile").get(0);
        if (bkey == null) 
        {
            res.sendRedirect("/");
        } 
        else 
        {
        	BlobstoreInputStream is = new BlobstoreInputStream(bkey);
        	CSVReader csvReader = new CSVReader(new InputStreamReader(is),';');
        	List<String[]> rows = csvReader.readAll();
        	csvReader.close();
        	res.setContentType("text/html");
        	PrintWriter w = res.getWriter();
        	w.println("<html><body><table>");
        	for (String[] row : rows)
        	{
        		w.println("<tr>");
        		for (String td : row)
        		{
        			w.println("<td>");
            		w.println(td);
            		w.println("</td>");	
        		}
        		w.println("</tr>");
        	}
        	w.println("</table></body></html>");
        	w.close();
        }
    }
}