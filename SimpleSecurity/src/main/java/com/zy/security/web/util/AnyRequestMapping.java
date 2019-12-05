
package com.zy.security.web.util;

import javax.servlet.http.HttpServletRequest;

import com.zy.security.web.interfaces.RequestMatcher;

/**
* @author zy
* @Date 2019-11-29 周五 上午 02:12:10
* @Description uri比较
* @version 
*/
public class AnyRequestMapping implements RequestMatcher {
	private String url;
	private HttpMethod httpMethod;
	
	public AnyRequestMapping(String logoutUrl, HttpMethod method) {
		this.url = logoutUrl;
		this.httpMethod = method;
	}

	public AnyRequestMapping(String logoutUrl, String method) {
		this(logoutUrl, HttpMethod.get(method));
		
	}

	@Override
	public boolean match(HttpServletRequest request) {
		String method = request.getMethod();
		if(!this.httpMethod.equals(HttpMethod.get(method))) { // 请求类型不同
			return false;
		}
		String path = request.getRequestURI();
		return this.url.equals(path);
	}

	@Override
	public String toString() {
		return "AnyRequestMapping [url=" + url + ", httpMethod=" + httpMethod + "]";
	}
}
