
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

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.rememberme.interfaces.RememberMeService;
import com.zy.security.core.context.SecurityContext;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.Authentication;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;
import com.zy.security.web.session.SessionAuthenticationStrategyManager;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.DebugEnabled;
import com.zy.security.web.util.HttpMethod;

/**
* @author zy
* @Date 2019-11-20 周三 上午 00:47:31
* @Description 监控一个使用用户名和密码基于 form 认证的URL（默认为/login），并在 URL匹配的情况下尝试认证该用户
* @version 
*/                   
public abstract class AbstractAuthenticationProcessingFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 过滤的uri
	private AnyRequestMapping loginUrl;
	// 身份认证管理器
	protected AuthenticationManager authenticationManager;
	// RememberMe服务处理类
	private RememberMeService rememberMeServices;
	// 认证成功后session处理策略过滤器
	private SessionAuthenticationStrategyManager sessionStrategyManager;
	// 认证成功处理类
	private AuthenticationSuccessHandler successHandler;
	// 认证失败处理类
	private AuthenticationFailureHandler failureHandler;
	
	
	public AbstractAuthenticationProcessingFilter(String loginUrl) {
		this(new AnyRequestMapping(loginUrl, HttpMethod.POST));
	}
	public AbstractAuthenticationProcessingFilter(AnyRequestMapping loginUrl) {
		super();
		this.loginUrl = loginUrl;
	}
	public AbstractAuthenticationProcessingFilter(AnyRequestMapping loginUrl,
			AuthenticationManager authenticationManager, RememberMeService rememberMeServices,
			AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
		super();
		this.loginUrl = loginUrl;
		this.authenticationManager = authenticationManager;
		this.rememberMeServices = rememberMeServices;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}
	
	
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	public RememberMeService getRememberMeServices() {
		return rememberMeServices;
	}
	public void setRememberMeServices(RememberMeService rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}
	public SessionAuthenticationStrategyManager getSessionStrategyManager() {
		return sessionStrategyManager;
	}
	public void setSessionStrategyManager(SessionAuthenticationStrategyManager sessionStrategyManager) {
		this.sessionStrategyManager = sessionStrategyManager;
	}
	public AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}
	public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}
	public AnyRequestMapping getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(AnyRequestMapping loginUrl) {
		this.loginUrl = loginUrl;
	}
	public AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}
	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (!isLogin(req, resp)) { // 不是token数据表单提交url则放行
			chain.doFilter(request, response);
			return ;
		}
		
		Authentication auth = null;
		try {
			// 开始进行身份验证
			auth = attemptAuthentication(req, resp);
			
			if(auth == null) {
				
				if(DebugEnabled.debug) {	
					logger.info("AbstractAuthenticationProcessingFilter return{Token为null}.");
				}
				return ;
			}
		} catch (AuthenticationException e) {
			authenticationFail(req,resp,e);
			
			if(DebugEnabled.debug) {
				logger.info("AbstractAuthenticationProcessingFilter return{身份验证失败}.");
			}
			return ;
		}
		sessionStrategyManager.onAuthentication(auth, req, resp);
		successfulAuthentication(req, resp, chain, auth);
	}
	/**
	 * 身份验证成功的后续处理逻辑
	 * @param request
	 * @param response
	 * @param chain
	 * @param auth
	 */
	private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) {
		SecurityContext context = SecurityContextStrategy.getContext();
		context.setAuthentication(auth);
		// 将身份认证结果查出到ThreadLocal中和存储库中
		SecurityContextStrategy.saveContext(context,request);
		
		this.rememberMeServices.loginSuccess(request, response, auth);
		successHandler.onAuthenticationSuccess(request, response, auth);
	}

	/**
	 * 出现认证异常时进行收尾工作
	 * @param req
	 * @param resp
	 * @param e
	 */
	private void authenticationFail(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) {
		SecurityContextStrategy.clearContext();
		failureHandler.onAuthenticationFailure(req, resp, e);
	}
	
	/**
	 * 身份验证逻辑方法,通过子类对Token个性化处理，从请求参数中获得各个Token所需数据，然后传递到AuthenticationManager中进行验证
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) 
			throws AuthenticationException, IOException,ServletException;
	
	/**
	 * 比对是否是表单提交的url
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean isLogin(HttpServletRequest request,HttpServletResponse response) {
		return loginUrl.match(request);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	@Override
	public void destroy() {}

}
