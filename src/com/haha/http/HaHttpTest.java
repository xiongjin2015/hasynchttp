package com.haha.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HaHttpTest {

	public static void testURL() {
		try {
			String strurl = "http://www.baidu.com/abc?a=1&b=2";
			URL url = new URL(strurl);
			System.out.println(url.getHost());
			System.out.println(url.getPath());
			System.out.println(url.getQuery());
			System.out.println(url.getPort());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static class TestHandler extends HaHttpHandler {

		@Override
		public void onSuccess(HaHttpRequest req, HaHttpResponse resp) {
			System.out.println("success, code: " + resp.getCode());
			System.out.println("content:" + resp.getContent());
			System.out.println("time used:" + resp.getTimeUsed());
			Map<String,List<String>> headers = resp.getHeaders();
			if(headers == null)
				System.out.println("headers is null");
			Iterator<String> iterator = headers.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				System.out.println("key:"+key);
				for(String value:headers.get(key)){
					System.out.println(value+":");
				}
				System.out.println();
				
			}
		}

		@Override
		public void onFailed(HaHttpRequest req, HaHttpResponse resp) {
			System.out.println("response code: " + resp.getCode()
					+ ", response msg:" + resp.getMsg());
		}

		@Override
		public void onError(HaHttpRequest req, String errMsg) {
			System.out.println("error: " + errMsg);
		}

		@Override
		public void onRetry(HaHttpRequest req, String reason) {
			// TODO Auto-generated method stub
		}

	}

	private static void testHttp() {
		try {
			HaHttpClient client = HaHttp.newHttpClient();
			// client.get("http://127.0.0.1:8080", new TestHandler());
			client.get(
					"https://trk.big5.io/clk.cfm?dp=237&dc=2167&s1=&s2=&s3=b2fc80b7b66aa0c6|1|us|io.big5.jobs_now_hiring|3|104|26|0|78693|91391|en|0|58_21_73_76_601|3|14|1|0|1|554c4085|1|0|1||200001|3|1_8_20_16_14_11|1|1|coolpad|coolpad_8297|us_nj_absecon|310_53|1|0|0|",
					new TestHandler());
//			client.get(
//					"https://app.adjust.io/zdgo7c?&gps_adid=6b6131a5-c306-4c9e-9377-45f06857fb6d&android_id=e3977c27949012dc&install_callback=http://ssdk.adkmob.com/postback/adjust/?tid=b2fc80b7b66aa0c6|3|us|com.nuclear.qmwow.platform.r2game|3|104|26|0|78452|91101|en|0|58_21_73_76_601|3|14|1|0|0|554c4085|1|0|1||200001|3|1_8_20_16_14_11|1|1|coolpad|coolpad_8297|us_nj_absecon|310_53|1|0|0|",
//					new TestHandler());
			//client.get("http://www.sohu.com/", new TestHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testHttpUrl() {
		try {
			URL url = new URL("http://127.0.0.1:8080");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			int a = conn.getResponseCode();
			System.out.println(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		testHttp();
	}

}
