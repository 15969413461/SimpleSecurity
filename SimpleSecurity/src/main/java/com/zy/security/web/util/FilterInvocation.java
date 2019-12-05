
package com.zy.security.web.util;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author zy
* @Date 2019-11-16 周六 下午 22:53:29
* @Description Filter属性封装类
* @version 
*/
public class FilterInvocation {
	private FilterChain chain;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public FilterInvocation(FilterChain chain, HttpServletRequest request, HttpServletResponse response) {
		if (request == null || response == null || chain == null) {
			throw new IllegalArgumentException("构造器参数不可为nulll");
		}
		this.chain = chain;
		this.request = request;
		this.response = response;
	}
	public FilterInvocation(FilterChain chain, ServletRequest request, ServletResponse response) {
		if (request == null || response == null || chain == null) {
			throw new IllegalArgumentException("构造器参数不可为nulll");
		}
		this.chain = chain;
		this.request = (HttpServletRequest) request;
		this.response = (HttpServletResponse) response;
	}
	
	public FilterChain getChain() {
		return chain;
	}
	public void setChain(FilterChain chain) {
		this.chain = chain;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
