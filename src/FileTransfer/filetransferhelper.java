package FileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

 public class filetransferhelper extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public filetransferhelper() {
		super();
	}  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd=request.getRequestDispatcher("copied.jsp");
		PrintWriter out=response.getWriter();
		String inputPath=request.getParameter("ip");
		String outputPath=request.getParameter("op");
		if(inputPath!=null && outputPath!=null)
		{
			StandardCopyOption[] opts =
			{StandardCopyOption.REPLACE_EXISTING,
			StandardCopyOption.COPY_ATTRIBUTES};
			//File f = new File(inputPath);
				
			//if(f.exists() && !f.isDirectory()) { /* do something */ }
			/*FileChannel inputChannel = null;
		    FileChannel outputChannel = null;*/
		   	File sourceFile=new File(inputPath);
			File destFile=new File(outputPath);
			if(sourceFile.isDirectory() && destFile.isDirectory()){
				try {
				    FileUtils.copyDirectory(sourceFile, destFile);
				} catch (IOException e) {
				    e.printStackTrace();
				}	
			}
			else
			{
			    try
			    {
			    	Files.copy(sourceFile.toPath(), destFile.toPath(),opts);
			    	} finally 
			    		{
			    		/*if(inputChannel!=null && outputChannel!=null){
				        inputChannel.close();
				        outputChannel.close();
			    		}*/
					  }
			    	rd.forward(request,response);
				}
			}
		else
			out.write("Please enter proper input and output paths");
	}   	  	    
}