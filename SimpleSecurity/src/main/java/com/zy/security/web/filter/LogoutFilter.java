
package com.zy.security.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.Authentication;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.logout.LogoutHandler;
import com.zy.security.web.logout.LogoutSuccessHandler;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.DebugEnabled;
import com.zy.security.web.util.HttpMethod;
/**
* @author zy
* @Date 2019-11-19 周二 上午 00:51:18
* @Description 监控一个实际为退出功能的 URL（默认为login?logout=true），并且在匹配的时候完成用户的退出功能
* @version 
*/
public class LogoutFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 退出操作uri
	private RequestMatcher logoutUrl;
	// 退出处理类
	private final List<LogoutHandler> handlers;
	// 退出成功处理类
	private final LogoutSuccessHandler logoutSuccessHandler;
	
	
	public RequestMatcher getLogoutUrl() {
		return logoutUrl;
	}
	public void setLogoutUrl(String url,HttpMethod httpMethod) {
		this.logoutUrl = new AnyRequestMapping(url,httpMethod);
	}
	public List<LogoutHandler> getHandlers() {
		return handlers;
	}
	public LogoutSuccessHandler getLogoutSuccessHandler() {
		return logoutSuccessHandler;
	}
	public void setLogoutUrl(AnyRequestMapping logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	public LogoutFilter(RequestMatcher logoutUrl, List<LogoutHandler> handlers,
			LogoutSuccessHandler logoutSuccessHandler) {
		super();
		this.logoutUrl = logoutUrl;
		this.handlers = handlers;
		this.logoutSuccessHandler = logoutSuccessHandler;
	}
	public LogoutFilter(List<LogoutHandler> handlers, LogoutSuccessHandler logoutSuccessHandler) {
		this.handlers = handlers;
		this.logoutSuccessHandler = logoutSuccessHandler;
		setLogoutUrl("/logout", HttpMethod.GET);;
	}

	public void logoutInit() {}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	/**
	 * 过滤退出的uri，依次调用各个退出处理器进行退出处理，退出成功后重定向请求到指定的uri中
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		if(isLogout((HttpServletRequest)request)) { // 此请求是否为退出uri
			Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
			if(authentication != null) { // 用户已退出或没有登陆过
				for (LogoutHandler handler : this.handlers) {
					handler.logout(req, resp, authentication);
				}
				if(DebugEnabled.debug) {
					logger.info("退出成功：{}",authentication);
				}
			}
			this.logoutSuccessHandler.onLogoutSuccess(request, response, authentication);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	
	private boolean isLogout(HttpServletRequest req) {
		return this.logoutUrl.match(req);
	}
}
