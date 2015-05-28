package com.haha.http;

public interface HaHttpClient {
	
	public void init();
	
	/*interfaces with retry count default 1*/
	public void get(String url, HaHttpHandler handler) throws Exception;

}
