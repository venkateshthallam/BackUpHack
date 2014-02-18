package FileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
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
			
			/*FileChannel inputChannel = null;
		    FileChannel outputChannel = null;*/
		   	File sourceFile=new File(inputPath);
			File destFile=new File(outputPath);
			if(sourceFile.isDirectory())
			{
				//copyFolder(sourceFile,destFile);
				copyDirectory(sourceFile,destFile);
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
	public static void copyFolder(File src, File dest)
	throws IOException{

	if(src.isDirectory()){

		//if directory not exists, create it
		if(!dest.exists()){
		   dest.mkdir();
		   System.out.println("Directory copied from " 
                          + src + "  to " + dest);
		}
		
		//list all the directory contents
		String files[] = src.list();

		for (String file : files) {
		   //construct the src and dest file structure
		   File srcFile = new File(src, file);
		   File destFile = new File(dest, file);
		   //recursive copy
		   copyFolder(srcFile,destFile);
		}

	}else{
		//if file, then copy it
		//Use bytes stream to support all file types
		InputStream in = new FileInputStream(src);
	        OutputStream out = new FileOutputStream(dest); 

	        byte[] buffer = new byte[1024];

	        int length;
	        //copy the file content in bytes 
	        while ((length = in.read(buffer)) > 0){
	    	   out.write(buffer, 0, length);
	        }

	        in.close();
	        out.close();
	        System.out.println("File copied from " + src + " to " + dest);
	}
}
	public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
		FileLock lock=null;
		FileChannel channel=null;
		try{
	    if (sourceLocation.isDirectory()) {
	        if (!targetLocation.exists()) {
	            targetLocation.mkdir();
	        }

	      /*  String[] children = sourceLocation.list();
	        for (int i=0; i<children.length; i++) {
	            copyDirectory(new File(sourceLocation, children[i]),
	                    new File(targetLocation, children[i]));
	        }*/
	        try {
	            FileUtils.copyDirectory(sourceLocation, targetLocation);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	    } 
		}
		finally{
				/*lock.close();
				channel.close();*/
				
		}
	}
}