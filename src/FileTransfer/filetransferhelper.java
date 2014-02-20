package FileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

public class filetransferhelper extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	List<String> fileList;
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public filetransferhelper() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("copied.jsp");
		PrintWriter out = response.getWriter();
		String inputPath = request.getParameter("ip");
		String outputPath = request.getParameter("op");
		String compressFlag=request.getParameter("Compress");
		out.print(compressFlag);
		fileList = new ArrayList<String>();
		if (inputPath != null && outputPath != null) {
			File sourceFile = new File(inputPath);
			File destFile = new File(outputPath);
			if(compressFlag.equalsIgnoreCase("Compress")){
			      generateFileList(sourceFile);
			      zipIt(outputPath,inputPath);
			}
			if (sourceFile.isDirectory()) {				
				copyDir(sourceFile,destFile);
			} else {
				copyFile(sourceFile, destFile);
				rd.forward(request, response);

			}
		}
	}

	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		StandardCopyOption[] opts = { StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES };
		Files.copy(sourceFile.toPath(), destFile.toPath(), opts);
	}
	
	public static void copyDir(File sourceLocation, File targetLocatio){
		try {
		    FileUtils.copyDirectory(sourceLocation, targetLocatio);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	 public void zipIt(String zipFile,String SOURCE_FOLDER)
	  {
	    byte[] buffer = new byte[1024];
	    String source="";
	   try{
	       try
	       {
	           source=SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\")+1,SOURCE_FOLDER.length());
	       }
	       catch(Exception e)
	       {
	               source=SOURCE_FOLDER;
	       }
	      FileOutputStream fos = new FileOutputStream(zipFile);
	      ZipOutputStream zos = new ZipOutputStream(fos);

	      System.out.println("Output to Zip : " + zipFile);

	      for(String file : this.fileList){

	          System.out.println("File Added : " + file);
	          ZipEntry ze= new ZipEntry(source+File.separator+file);
	          zos.putNextEntry(ze);

	          FileInputStream in =
	                     new FileInputStream(SOURCE_FOLDER + File.separator + file);

	          int len;
	          while ((len = in.read(buffer)) > 0) {
	              zos.write(buffer, 0, len);
	          }

	          in.close();
	      }

	      zos.closeEntry();
	      //remember close it
	      zos.close();

	      System.out.println("Folder successfully compressed");
	  }catch(IOException ex){
	     ex.printStackTrace();
	  }
	 }
	  public void generateFileList(File node,String src){

	      //add file only
	  if(node.isFile())
	      {
	      fileList.add(generateZipEntry(node.toString()),src);

	  }

	  if(node.isDirectory())
	        {
	      String[] subNote = node.list();
	      for(String filename : subNote){
	          generateFileList(new File(node, filename));
	         }
	  }

	  }
	  private String generateZipEntry(String file,String SOURCE_FOLDER)
	 {
	      return file.substring(SOURCE_FOLDER.length()+1, file.length());
	  }

	
}