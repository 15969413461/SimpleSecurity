
package com.zy.security.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.RequestCache;

/**
* @author zy
* @Date 2019-11-22 周五 下午 21:41:36
* @Description
* @version 
*/
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	private String errorPage;
	private RequestCache requestCache;
	
	
	public AccessDeniedHandlerImpl(String errorPage) {
		super();
		this.errorPage = errorPage;
	}
	public AccessDeniedHandlerImpl() {
		super();
	}
	
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!response.isCommitted()) {
			// 缓存当前请求
			requestCache.saveRequest(request);
			
			if (errorPage != null) {
				response.setStatus(403);
				// 请求重定向到到错误页面
				response.sendRedirect(errorPage);
			}
			else {
				response.sendError(403,"拒绝访问");
			}
		}
	}
	@Override
	public void setErrorPage(String errPage) {
		if(errPage == null || errPage.isEmpty()) {
			return ;
		}
		this.errorPage = errPage;
	}
	@Override
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
	public RequestCache getRequestCache() {
		return requestCache;
	}
	public String getErrorPage() {
		return errorPage;
	}
}
