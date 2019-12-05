
package com.zy.security.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.rememberme.AbstractRememberMeService;
import com.zy.security.core.authentication.rememberme.interfaces.RememberMeService;
import com.zy.security.core.context.SecurityContext;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.Authentication;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;
import com.zy.security.web.util.DebugEnabled;
import com.zy.security.web.util.WebUtils;

/**
* @author zy
* @Date 2019-11-20 周三 下午 23:16:08
* @Description 负责解析remember cookie，并进行身份验证
* @version 
*/
public class RememberMeAuthenticationFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 认证成功处理类
	private AuthenticationSuccessHandler successHandler;
	// 身份认证管理器
	private AuthenticationManager authenticationManager;
	// RememberMe服务处理类
	private RememberMeService rememberMeService;
	
	
	public RememberMeAuthenticationFilter(AuthenticationManager authenticationManager,
			RememberMeService rememberMeService) {
		if(authenticationManager == null || rememberMeService == null) {
			throw new IllegalArgumentException("构造参数不可为null");
		}
		this.authenticationManager = authenticationManager;
		this.rememberMeService = rememberMeService;
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String cookieName = ((AbstractRememberMeService)rememberMeService).getRememberMeCookieName();
		Cookie cookie = WebUtils.getCookie(req,cookieName );
		SecurityContext securityContext = SecurityContextStrategy.getContext();
		
		if (securityContext.getAuthentication() == null && cookie != null) { // 用户未认证且请求中存于RememberMe cookie
			Authentication rememberMeAuth = rememberMeService.autoLogin(req,resp);
			if (rememberMeAuth != null) { // RememberMe cookie有效
//					rememberMeAuth = authenticationManager.authenticate(rememberMeAuth);

					// 保存Authentication到Threadlocal中
					securityContext.setAuthentication(rememberMeAuth);
					// 将SecurityContext关联到session中
					SecurityContextStrategy.setContext(securityContext);
					if (successHandler != null) {
						successHandler.onAuthenticationSuccess(req, resp,rememberMeAuth);
						
						if(DebugEnabled.debug) {
							logger.info("RememberMeAuthenticationFilter：{RememberMe认证成功}.");
						}
						return;
					}
			}
		}
		chain.doFilter(request, response);
	}

	
	@Override
	public void destroy() {}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}


	public AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}
	public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	public RememberMeService getRememberMeService() {
		return rememberMeService;
	}
	public void setRememberMeService(RememberMeService rememberMeService) {
		this.rememberMeService = rememberMeService;
	}
}
