package com.haha.http;

public interface HaHttpClient {

	public void init();

	/* interfaces with retry count default 1 */
	public void get(String url, HaHttpHandler handler) throws Exception;

	public void post(String url, HaHttpHandler handler) throws Exception;

	/* interfaces with specified retry count */
	public void get(String url, int maxRetryCount, HaHttpHandler handler)
			throws Exception;

	public void post(String url, int maxRetryCount, HaHttpHandler handler)
			throws Exception;

	public void post(String hostPath, HaHttpParams params, int maxRetryCount,
			HaHttpHandler handler) throws Exception;

	/* upload file:only upload file without other params */
	public void post(String remotePath, String name, String localFile,
			HaHttpHandler handler) throws Exception;

	/* upload file:not only upload file with other params */
	public void post(String remotePath, HaHttpParams params, String name,
			String localFile, HaHttpHandler handler) throws Exception;

	public void destroy();

}
