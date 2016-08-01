package com.haha.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * class for wrapperring request parameters
 * 
 * @author xj
 * 
 */
public class HaHttpParams {

	private final static String DEFAULT_ENCODE = "utf-8";
	private List<Param> params = new ArrayList<Param>();

	public static HaHttpParams newParams() {
		return new HaHttpParams();
	}

	public HaHttpParams put(String key, String value) {
		if (key != null) {
			if (value == null)
				params.add(new Param(key, ""));
			else
				params.add(new Param(key, value));
		}
		return this;
	}

	List<Param> getParams() {
		return params;
	}

	public HaHttpParams put(Map<String, String> paramMap) {
		if (paramMap == null)
			return this;
		Iterator<String> iterator = paramMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = paramMap.get(key);
			put(key, value);
		}
		return this;
	}

	public String get(String key) {
		try {
			for (Param param : params) {
				if (param.key.equals(key))
					return param.value;
			}
		} catch (Exception e) {
		}

		return null;
	}

	public void replace(String key, String value) {
		try {
			for (Param param : params) {
				if (param.key.equals(key))
					param.value = value;
			}
		} catch (Exception e) {
		}
	}

	public boolean contain(String key) {
		try {
			for (Param param : params) {
				if (param.key.equals(key))
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public HaHttpParams mergeToHead(HaHttpParams params) {
		if (params != null) {
			this.params.addAll(0, params.params);
		}
		return this;
	}

	public HaHttpParams mergeToEnd(HaHttpParams params) {
		if (params != null)
			this.params.addAll(params.params);
		return this;
	}

	public String encode() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		Iterator<Param> iter = params.iterator();
		while (iter.hasNext()) {
			Param param = iter.next();
			sb.append(URLEncoder.encode(param.key, DEFAULT_ENCODE));
			sb.append("=");
			sb.append(URLEncoder.encode(param.value, DEFAULT_ENCODE));
			if (iter.hasNext())
				sb.append("&");
		}
		return sb.toString();
	}

	final static class Param {

		private String key;
		private String value;

		public Param(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

}
