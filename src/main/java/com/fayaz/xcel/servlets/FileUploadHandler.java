package com.fayaz.xcel.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fayaz.xcel.logicallayer.BaseHandler;
import com.fayaz.xcel.logicallayer.HandlerFactory;

@WebServlet(name = "FileUploadHandler", value = "/FileUploadHandler")
public class FileUploadHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6832888182596259641L;
	private final String UPLOAD_DIRECTORY = "D:\\eclipse";
	
	static Logger logger = LoggerFactory.getLogger(FileUploadHandler.class);

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//logger.info("A POST req");
		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				String name = "";
				List<FileItem> multiparts = new ServletFileUpload(
						new DiskFileItemFactory()).parseRequest(request);
				
				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						
						logger.info("File Name:"+item.getName());
						name = new File(item.getName()).getName();
						item.write(new File(UPLOAD_DIRECTORY + File.separator
								+ name));
					}
				}

				// File uploaded successfully
				request.setAttribute("message", "File Uploaded Successfully");
				logger.info("File Upload Successfull");
				
				//Parsing extension and using the correct Handler to get list of sheet names
				String extension = name.substring(name.lastIndexOf('.')+1);
				logger.info("File Ext :"+extension);
				BaseHandler handler = HandlerFactory.getHandler(extension, UPLOAD_DIRECTORY + File.separator
						+ name);
				List<String> sheetNames = handler.getSheetNames();
				response.setContentType("text/html");
				response.getWriter().write(toJSON(sheetNames));
				
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				request.setAttribute("message", "File Upload Failed due to "
						+ ex);
			}

		} else {
			request.setAttribute("message",
					"Sorry this Servlet only handles file upload request");
		}

	}
	
	public static synchronized String toJSON(List<String> sheetNames)
	{
	    String json = "[\"";
		Iterator<String> sheets = sheetNames.iterator();
		while(sheets.hasNext()){			
			json += sheets.next() + "\",\"";
		}
	    return json.substring(0, json.length() - 2) + "]";
	}

}
