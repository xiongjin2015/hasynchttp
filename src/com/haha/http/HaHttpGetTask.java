
package com.haha.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HaHttpGetTask extends HaHttpTask {

    public HaHttpGetTask(String url, int maxRetryCount, HaHttpHandler handler)
            throws Exception {
        super(url, maxRetryCount, handler);
    }

    @Override
    protected void request() throws Exception {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            // record the requet start time point
            long startTime = System.currentTimeMillis();

            // prepare the url connection
            URL url = new URL(request.getUrlString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(HaHttpCfg.DEFAULT_CONNECT_TIMEOUT);
            connection.setReadTimeout(HaHttpCfg.DEFAULT_READ_TIMEOUT);

            connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setRequestProperty("User-Agent", "haha http/2.0");
            connection.setRequestProperty("Connection", "Close");

            int code = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            String encoding = connection.getContentEncoding();
            Map<String, List<String>> headers = connection.getHeaderFields();

            if (encoding != null && encoding.toLowerCase().contains("gzip")) {
                in = new GZIPInputStream(new BufferedInputStream(
                        connection.getInputStream()));
            } else {
                in = new BufferedInputStream(connection.getInputStream());
            }

            // for read response content from remote host
            byte[] buf = new byte[HaHttpCfg.READ_BUFFER_SIZE];
            out = new ByteArrayOutputStream();

            // read the response content
            int sz = in.read(buf);
            while (sz != -1) {
                out.write(buf, 0, sz);
                sz = in.read(buf);
            }

            // record the request end time point
            long endTime = System.currentTimeMillis();

            // make the response object
            this.response = new HaHttpResponse(code, msg, headers,
                    out.toByteArray(), endTime - startTime);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                // close the connection
                connection.disconnect();
            } catch (Exception e) {
            }
        }
    }
}
