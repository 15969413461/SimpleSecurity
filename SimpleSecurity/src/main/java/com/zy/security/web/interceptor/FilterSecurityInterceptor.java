
package com.zy.security.web.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.access.AbstractSecurityInterceptor;
import com.zy.security.web.config.subject.Authorization;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.FilterInvocation;

/**
* @author zy
* @Date 2019-11-16 周六 下午 16:19:44
* @Description 使用过滤器实现权限限制
* @version 
*/
public class FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 存储进行权限限制的uri
	@SuppressWarnings("unused")
	private Map<RequestMatcher,Authorization> authorizedUrlMap;
	
	public FilterSecurityInterceptor(Map<RequestMatcher, Authorization> authorizedUrlMap) {
		super();
		this.authorizedUrlMap = authorizedUrlMap;
	}

	public void invoke(FilterInvocation invocation) {
		try {
			super.beforeInvocation(invocation);
			invocation.getChain().doFilter(invocation.getRequest(), invocation.getResponse());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		HttpServletRequest req = (HttpServletRequest)request;
//		
//		for (RequestMatcher mapping : authorizedUrlMap.keySet()) {
//			if(mapping.match(req)) {
//				
//			}
//		}
		FilterInvocation invocation = new FilterInvocation(chain, request, response);
		invoke(invocation);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	@Override
	public void destroy() {}

}
