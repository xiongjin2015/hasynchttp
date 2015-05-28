package com.haha.http;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * data structure for http request
 * 
 * @author xj
 *
 */
public class HaHttpRequest {

	/**
	 * request url object;
	 */
	private URL url;

	/**
	 * local file directory for get file from remote server;
	 */
	private String localDir;

	/**
	 * local file name for download from/upload to remote server;
	 */
	private String fileName;

	public HaHttpRequest(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	public HaHttpRequest(String hostPath, HaHttpParams params) throws Exception {
		if (params == null)
			this.url = new URL(hostPath);
		else
			this.url = new URL(hostPath + "?" + params.encode());
	}

	public HaHttpRequest(String url, String localDir) throws Exception {
		if (localDir == null || "".equals(localDir))
			throw new Exception("local dir must not be null!");

		this.url = new URL(url);
		this.localDir = localDir;
	}

	public HaHttpRequest(String url, String localDir, String fileName)
			throws Exception {
		if (localDir == null || "".equals(localDir))
			throw new Exception("local dir must not be null!");

		if (fileName == null || "".equals(fileName))
			throw new Exception("fileName must not be null");

		this.url = new URL(url);
		this.localDir = localDir;
		this.fileName = fileName;
	}
	
	public String getUrlString(){
		return url.toString();
	}
	
	public String getLocalDir() {
		if(localDir == null)
			return "";
		
		return localDir;
	}

	public String getFileName() {
		return fileName;
	}
	
	public String getLocalFile(){
		if(localDir != null && fileName != null){
			return localDir + File.separator + fileName;
		}
		
		return null;
	}

}
