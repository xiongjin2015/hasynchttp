package com.haha.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class HaHttpPostTask extends HaHttpTask {

	public HaHttpPostTask(String url, int maxRetryCount, HaHttpHandler handler) throws Exception{
		super(url, maxRetryCount, handler);
	}
	
	public HaHttpPostTask(String hostPath, HaHttpParams params, int maxRetryCount, HaHttpHandler handler) throws Exception{
		super(hostPath, params, maxRetryCount, handler);
	}

	protected void request() throws Exception{
		//the response content reading input stream
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try{
			//record the request start time point
			long startTime = System.currentTimeMillis();
			
			//prepare the url connection
			URL url = new URL(request.getProtocol(), request.getHost(), request.getPort(), request.getPath());
			
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(HaHttpCfg.DEFAULT_CONNECT_TIMEOUT);
			connection.setReadTimeout(HaHttpCfg.DEFAULT_READ_TIMEOUT);
			
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("User-Agent", "haha app/2.0");
			connection.setRequestProperty("Connection", "Close");
			String query = request.getQuery(); 
			if(query != null){
				connection.setDoOutput(true);
				connection.getOutputStream().write(query.getBytes());
			}
			
			//get the response code and message
			int code = connection.getResponseCode();
			String msg = connection.getResponseMessage();
			String encoding = connection.getContentEncoding();
			Map<String, List<String>> headers = connection.getHeaderFields();

			//read the response content
			if(encoding!=null && encoding.toLowerCase().matches("gzip")){
				in = new GZIPInputStream(new BufferedInputStream(connection.getInputStream()));
			}else{
				in = new BufferedInputStream(connection.getInputStream());
			}
			
			//for read response content from remote host
			byte[] buf = new byte[HaHttpCfg.READ_BUFFER_SIZE];
			baos = new ByteArrayOutputStream();
			
			//read the response content
			int sz = in.read(buf);
			while(sz != -1){
				baos.write(buf, 0, sz);
				sz = in.read(buf);
			}
			
			//record the request end time point
			long endTime = System.currentTimeMillis();
			
			//make the response object
			this.response = new HaHttpResponse(code, msg, headers, baos.toByteArray(), endTime-startTime);
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			try{
				if(in != null){
					in.close();
				}
				
				if(baos != null){
					baos.close();
				}
				
				//close the connection
				connection.disconnect();
			}catch(Exception e){}
		}
	}

}
