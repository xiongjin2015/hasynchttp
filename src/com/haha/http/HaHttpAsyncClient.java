package com.haha.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HaHttpAsyncClient implements HaHttpClient {

	// a executor service object to execute the http task
	private ExecutorService executorService;

	public HaHttpAsyncClient() {
		init();
	}

	@Override
	public void init() {
		if (this.executorService == null) {
			this.executorService = Executors.newCachedThreadPool();
		}
	}

	@Override
	public void get(String url, HaHttpHandler handler) throws Exception {
		HaHttpTask task = new HaHttpGetTask(url,
				HaHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String url, HaHttpHandler handler) throws Exception {
		HaHttpTask task = new HaHttpPostTask(url,
				HaHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String url, int maxRetryCount, HaHttpHandler handler)
			throws Exception {
		HaHttpTask task = new HaHttpGetTask(url, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String url, int maxRetryCount, HaHttpHandler handler)
			throws Exception {
		HaHttpTask task = new HaHttpPostTask(url, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String hostPath, HaHttpParams params, int maxRetryCount,
			HaHttpHandler handler) throws Exception {
		HaHttpTask task = new HaHttpPostTask(hostPath, params, maxRetryCount,
				handler);
		this.executorService.execute(task);

	}

	@Override
	public void post(String remotePath, HaHttpParams params, String name,
			String localFile, HaHttpHandler handler) throws Exception {
		HaHttpTask task = new HaFilePostTask(remotePath, params, name,
				localFile, HaHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String remotePath, String name, String localFile,
			HaHttpHandler handler) throws Exception {
		HaHttpTask task = new HaFilePostTask(remotePath, null, name, localFile,
				HaHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void destroy() {
		if (this.executorService != null) {
			this.executorService.shutdown();
			this.executorService = null;
		}
	}

}
