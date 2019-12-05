
package com.zy.security.web.filter;

import java.io.IOException;

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

import com.zy.security.web.interfaces.InvalidSessionStrategy;
import com.zy.security.web.session.SessionAuthenticationStrategyManager;
import com.zy.security.web.util.DebugEnabled;
import com.zy.utils.Assert;

/**
 * @author zy
 * @Date 2019-11-21 周四 下午 13:54:35
 * @Description 总览Session策略调用操作，负责session的创建、更新、失效处理、并发控制等操作
 * @version
 */
public class SessionManagementFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// Session策略调用类
	@SuppressWarnings("unused")
	private SessionAuthenticationStrategyManager sessionAuthenticationStrategyManager;
	// Session失效处理类
	private InvalidSessionStrategy invalidSessionStrategy;
	// 已处理标识
	static final String FILTER_APPLIED = "session_management_filter";

	public SessionManagementFilter(SessionAuthenticationStrategyManager sessionAuthenticationStrategyManager,
			InvalidSessionStrategy invalidSessionStrategy) {
		super();
		Assert.notNull(sessionAuthenticationStrategyManager, "SessionAuthenticationStrategyManager 不可为null");
		this.sessionAuthenticationStrategyManager = sessionAuthenticationStrategyManager;
		this.invalidSessionStrategy = invalidSessionStrategy;
	}

	/**
	 * 到达此过滤器时，若用户不是匿名用户则启用session相关处理策略，是匿名用户则监控session是否过期，过期则执行session失效处理策略
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		// 防止请求转发之后进行使用此过滤器
		if (req.getAttribute(FILTER_APPLIED) != null) {
			chain.doFilter(request, response);
			if(DebugEnabled.debug) {
				logger.info("SessionManagementFilter return{当前请求已应用过此Filter}.");
			}
			return;
		}

		req.setAttribute(FILTER_APPLIED, Boolean.TRUE);
		
		// 当前请求有关联session且关联的session已失效
		if (req.getRequestedSessionId() != null && !req.isRequestedSessionIdValid()) {
			this.invalidSessionStrategy.onInvalidSessionDetected(req, resp);

			logger.info("SessionManagementFilter return{关联的session已失效}.");
			return;
		}
		// 请求在SecurityContextInitFilter一定会关联一个SecurityContext，但此对象可能不包含Authentication对象信息
		/*SecurityContext context = SecurityContextStrategy.getContext();
		Authentication authentication = context.getAuthentication();*/
		
		// authentication是不是匿名用户Token
		/*if (authentication != null && !authentication.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {

			// 已认证用户的session失效会在此方法中处理
			this.sessionAuthenticationStrategyManager.onAuthentication(authentication, req, resp);

			// 保存到会话中
			SecurityContextStrategy.saveContext(context, req);
		} else {
			// 当前请求有关联session且关联的session已失效
			if (req.getRequestedSessionId() != null && !req.isRequestedSessionIdValid()) {
				this.invalidSessionStrategy.onInvalidSessionDetected(req, resp);

				logger.info("SessionManagementFilter return{关联的session已失效}.");
				return;
			}
		}*/
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
