
package com.zy.security.web.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;
import com.zy.security.web.interfaces.RequestCache;

/**
* @author zy
* @Date 2019-11-20 周三 下午 15:50:41
* @Description 认证成功处理类
* @version 
*/
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private RequestCache requestCache;
	// 配置的默认登录成功跳转的页面
	private String defaultSuccessUrl;
	
	
//	public DefaultAuthenticationSuccessHandler(String defaultSuccessUrl) {
//		this.defaultSuccessUrl = defaultSuccessUrl;
//		this.requestCache = new DefaultRequestCache();
//	}
//	public DefaultAuthenticationSuccessHandler(RequestCache requestCache) {
//		if(requestCache == null) {
//			throw new IllegalArgumentException("RequestCache不能为null");
//		}
//		this.requestCache = requestCache;
//		this.defaultSuccessUrl = "/";
//	}
	/**
	 * 默认构造器
	 * @param requestCache
	 * @param defaultSuccessUrl
	 */
	public DefaultAuthenticationSuccessHandler(RequestCache requestCache, String defaultSuccessUrl) {
		super();
		this.requestCache = requestCache;
		this.defaultSuccessUrl = defaultSuccessUrl;
	}
	
	public RequestCache getRequestCache() {
		return requestCache;
	}
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	public String getDefaultSuccessUrl() {
		return defaultSuccessUrl;
	}
	public void setDefaultSuccessUrl(String defaultSuccessUrl) {
		this.defaultSuccessUrl = defaultSuccessUrl;
	}
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
		String oldUri = this.requestCache.loadRequestForCache(request);
		if(!response.isCommitted()) {
			try {
				if(oldUri != null) {
						response.setStatus(200);
						response.sendRedirect(oldUri);
				}else {
						response.sendRedirect(this.defaultSuccessUrl);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
