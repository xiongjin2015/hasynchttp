package com.haha.http;

/**
 * callback handler for http request after request has executed
 * 
 * @author xj
 */
public abstract class HaHttpHandler {

	/**
	 * notify the invoker that the request has success, which means the http
	 * response code is 200 OK
	 * 
	 * @param req
	 *            : the http request
	 * @param resp
	 *            : the relate response
	 */
	public abstract void onSuccess(HaHttpRequest req, HaHttpResponse resp);

	/**
	 * notify the invoker that the request has failed, which means the there is
	 * response, but the response code is not 200 OK
	 * 
	 * @param req
	 *            : the http request
	 * @param resp
	 *            : the relate response
	 */
	public abstract void onFailed(HaHttpRequest req, HaHttpResponse resp);

	/**
	 * notify the invoker that the request has an error, which means the request
	 * has not executed
	 * 
	 * @param req
	 *            : the http request
	 * @param errMsg
	 *            : the relate error message
	 */
	public abstract void onError(HaHttpRequest req, String errMsg);

	/**
	 * notify the invoker that the request will retry later request has not
	 * executed
	 * 
	 * @param req
	 *            : the http request
	 * @param reason
	 *            : the retry reason
	 */
	public abstract void onRetry(HaHttpRequest req, String reason);

}
