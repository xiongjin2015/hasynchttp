package com.haha.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import com.haha.http.HaHttpParams.Param;

public class HaFilePostTask extends HaHttpTask {
	private File localFile;
	private String name;
	private HaHttpParams params;

	public HaFilePostTask(String remotePath, HaHttpParams params, String name,
			String localFile, int maxRetryCount, HaHttpHandler handler)
			throws Exception {
		super(remotePath, maxRetryCount, handler);
		this.params = params;
		this.name = name;
		this.localFile = new File(localFile);
	}

	protected void request() throws Exception {
		InputStream fin = null;
		OutputStream os = null;
		InputStream in = null;
		ByteArrayOutputStream baos = null;

		try {
			// record the request start time point
			long startTime = System.currentTimeMillis();

			// prepare the url connection
			URL url = new URL(request.getUrlString());

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(HaHttpCfg.DEFAULT_CONNECT_TIMEOUT);
			connection.setReadTimeout(HaHttpCfg.DEFAULT_READ_TIMEOUT);
			connection.setUseCaches(false);

			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("User-Agent", "haha app/2.0");
			connection.setRequestProperty("Connection", "Keep-Alive");
			Random rand = new Random(System.currentTimeMillis());
			String boundary = "---------------------------7d"
					+ Long.toHexString(rand.nextLong());
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			StringBuilder start = new StringBuilder();

			if (params != null) {
				List<Param> list = params.getParams();
				if (list.size() > 0) {
					for (Param param : list)
						start.append(buildPart(boundary, param.getKey(),
								param.getValue()));
				}
			}

			start.append(buildPart(boundary));

			String end = "\r\n--" + boundary + "--\r\n";

			byte[] startb = start.toString().getBytes();
			byte[] endb = end.getBytes();

			long contentLength = this.localFile.length() + startb.length
					+ endb.length;
			connection.addRequestProperty("Content-Length",
					String.valueOf(contentLength));

			// output stream for connection
			os = connection.getOutputStream();
			os.write(startb);

			// data buffer for read and write
			byte[] buf = new byte[HaHttpCfg.READ_BUFFER_SIZE];
			fin = new FileInputStream(this.localFile);
			int sz = fin.read(buf);
			while (sz != -1) {
				os.write(buf, 0, sz);
				sz = fin.read(buf);
			}

			os.write(endb);
			os.flush();

			// get the response code and message
			int code = connection.getResponseCode();
			String msg = connection.getResponseMessage();
			String encoding = connection.getContentEncoding();
			Map<String, List<String>> headers = connection.getHeaderFields();

			// read the response content
			if (encoding != null && encoding.toLowerCase().matches("gzip")) {
				in = new GZIPInputStream(new BufferedInputStream(
						connection.getInputStream()));
			} else {
				in = new BufferedInputStream(connection.getInputStream());
			}

			// for read response content from remote host
			baos = new ByteArrayOutputStream();
			// read the response content
			sz = in.read(buf);
			while (sz != -1) {
				baos.write(buf, 0, sz);
				sz = in.read(buf);
			}

			// record the request end time point
			long endTime = System.currentTimeMillis();

			// make the response object
			this.response = new HaHttpResponse(code, msg, headers,
					baos.toByteArray(), endTime - startTime);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}

				if (os != null) {
					os.close();
				}

				if (in != null) {
					in.close();
				}

				if (baos != null) {
					baos.close();
				}
				// close the connection
				connection.disconnect();
			} catch (Exception e) {
			}
		}
	}

	private String buildPart(String boundary) {
		StringBuilder start = new StringBuilder();
		start.append("--" + boundary + "\r\n");
		start.append("Content-Disposition: form-data; name=\"" + name
				+ "\"; filename=\"" + this.localFile.getName() + "\"\r\n");
		start.append("Content-Type: application/octet-stream\r\n");
		start.append("Content-Transfer-Encoding: binary\r\n\r\n");
		return start.toString();
	}

	private String buildPart(String boundary, String key, String value) {
		StringBuilder start = new StringBuilder();
		start.append("--" + boundary + "\r\n");
		start.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
		start.append("\r\n");
		start.append(value + "\r\n");
		return start.toString();
	}
}
