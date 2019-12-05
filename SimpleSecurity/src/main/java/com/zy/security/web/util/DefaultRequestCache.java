
package com.zy.security.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zy.security.core.userdetails.WebAttribute;
import com.zy.security.web.interfaces.RequestCache;
import com.zy.utils.Assert;

/**
* @author zy
* @Date 2019-11-17 周日 下午 15:32:01
* @Description 缓存被打断的请求
* @version 
*/
public class DefaultRequestCache implements RequestCache {
	private StringBuilder builder;
	public DefaultRequestCache() {}

	@Override
	public void saveRequest(HttpServletRequest request) {
		Assert.notNull(request, "HttpServletRequest不可为null");
		HttpSession session = request.getSession(false);
		if(session == null) { // 匿名用户访问受限
			session = request.getSession(true);
		}
		
		String queryString = request.getQueryString();
		if(queryString != null) { // 带请求参数
			if(builder == null) {
				builder = new StringBuilder();
			}
			builder.append(request.getRequestURI()).append("?").append(queryString);
			session.setAttribute(WebAttribute.REQUEST_CACHE, builder.toString());
		}else {
			session.setAttribute(WebAttribute.REQUEST_CACHE, request.getRequestURI());
		}
	}

	@Override
	public String loadRequestForCache(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return null ;
		}
		Object attribute = session.getAttribute(WebAttribute.REQUEST_CACHE);
		if(attribute == null) {
			return null;
		}
		return attribute.toString();
	}

	@Override
	public void removeRequestForCache(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ;
		}
		session.removeAttribute(WebAttribute.REQUEST_CACHE);
	}

}
