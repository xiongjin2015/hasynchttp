package com.haha.http;

public interface HaHttpCfg {

	final int DEFAULT_MAX_RETRY_COUNT = 1;

	final int DEFAULT_HTTP_PORT = 80;

	final int READ_BUFFER_SIZE = 16 * 1024;

	final int DEFAULT_CONNECT_TIMEOUT = 10000; // 10s

	final int DEFAULT_READ_TIMEOUT = 15000; // 15s

}
