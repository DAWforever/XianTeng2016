package cn.edu.nju.iip.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaDownloadFile {
	
	private static final Logger logger = LoggerFactory.getLogger(JavaDownloadFile.class);

	public static void main(String[] args) {
		try {
			JavaDownloadFile downloader = new JavaDownloadFile();
			downloader.download("http://www.jxds.gov.cn/resource2/vfs/publish/001027003007001001/20150908135603806.doc", "D:\\");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method downloads file from URL to a given directory.
	 * @param fileURL	- 	file URL to download
	 * @param destinationDirectory	- directory to download file to
	 * @throws IOException
	 */
	public String download(String fileURL, String destinationDirectory) throws IOException {
		// File name that is being downloaded
		String downloadedFileName = fileURL.substring(fileURL.lastIndexOf("/")+1);
		
		// Open connection to the file
        URL url = new URL(fileURL);
        URLConnection myurlcon = url.openConnection();
        myurlcon.setConnectTimeout(5000);
        myurlcon.setReadTimeout(10000);
        InputStream is = myurlcon.getInputStream();
        
        // Stream to the destionation file
        String destionationFilePath = destinationDirectory + "/" + downloadedFileName;
        FileOutputStream fos = new FileOutputStream(destionationFilePath);
 
        // Read bytes from URL to the local file
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        
        logger.info("Downloading " + downloadedFileName);
        while ((bytesRead = is.read(buffer)) != -1) {
        	fos.write(buffer,0,bytesRead);
        }
        logger.info("done!");
 
        // Close destination stream
        fos.close();
        // Close URL stream
        is.close();
        return destionationFilePath;
	}
	
}