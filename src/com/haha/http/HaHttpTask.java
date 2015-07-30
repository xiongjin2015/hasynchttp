package com.haha.http;

import java.net.HttpURLConnection;

public abstract class HaHttpTask implements Runnable {

	protected int maxRetryCount = 1;
	
	protected HaHttpRequest request;
	protected HaHttpResponse response;
	protected HaHttpHandler handler;
	
	protected HttpURLConnection connection;

	public HaHttpTask(String url, int maxRetryCount, HaHttpHandler handler) throws Exception{
		this.request = new HaHttpRequest(url);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	protected abstract void request() throws Exception;
	
	private void query() throws Exception{
		boolean success = false;
		while(maxRetryCount-- >0 && !success){
			try{
				request();
				success = true;
			}catch(Exception e){
				if(maxRetryCount<=0){
					throw e;
				}else{
					if(handler !=null){
						String msg = e.getMessage();
						if(msg == null){
							msg = "null, unknown error";
						}
						this.handler.onRetry(this.request, msg);
					}
				}
			}
		}
	}	
	
	private void inform() throws Exception{
		if(handler == null){
			return;
		}
		
		if(response == null){
			throw new Exception("null response");
		}
	
		if(response.getCode() == HttpURLConnection.HTTP_OK){
			handler.onSuccess(this.request, this.response);
		}else{
			handler.onFailed(this.request, this.response);
		}
	}
	
	@Override
	public void run() {
		
		try{
			query();
			inform();
		}catch(Exception e){
			if(handler != null)
				handler.onError(request, e.getMessage());
		}
		
	}

}
