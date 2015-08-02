
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
        HaHttpTask task = new HaHttpGetTask(url, HaHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
        this.executorService.execute(task);
    }

    @Override
    public void get(String url, int maxRetryCount, HaHttpHandler handler) throws Exception {
        HaHttpTask task = new HaHttpGetTask(url, maxRetryCount, handler);
        this.executorService.execute(task);
    }

}
