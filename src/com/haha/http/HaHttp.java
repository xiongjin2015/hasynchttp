package com.haha.http;

public class HaHttp {
	
	private static HaHttpClient defaultClient = null;
	
	public static synchronized HaHttpClient defaultHttpClient(){
		if(defaultClient == null)
			defaultClient = new HaHttpAsyncClient();
		return defaultClient;
	}
	
	public static HaHttpClient newHttpClient(){
		return new HaHttpAsyncClient();
	}

}
