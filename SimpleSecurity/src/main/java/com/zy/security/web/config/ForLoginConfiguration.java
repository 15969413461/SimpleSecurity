
package com.zy.security.web.config;

import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.rememberme.AbstractRememberMeService;
import com.zy.security.core.authentication.rememberme.interfaces.RememberMeService;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.DefaultLoginPageGeneratingFilter;
import com.zy.security.web.filter.UsernamePasswordAuthenticationFilter;
import com.zy.security.web.handler.DefaultAuthenticationFailureHandler;
import com.zy.security.web.handler.DefaultAuthenticationSuccessHandler;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.HttpMethod;

/**
 * @author zy
 * @Date 2019-11-24 周日 下午 16:38:24
 * @Description sessionInvalidUri 、 SessionManagerFilter优化
 * @version
 */
public class ForLoginConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(ForLoginConfiguration.class);
	
	// 登录页面
	private String loginPage;
	// 录页面下表单提交的路径,此url可不在mvc中声明处理
	private String loginFormUrl;
	// 身份验证失败后跳转的uri
	private String failureUrl;
	// 直接访问登录页面而不是访问过其他页面进行跳转来的url,验证成功后访问此方法设置的url
	private String defaultTargetUrl;
	// 访问被拒绝页面的URL
	private String errorPage;
	// session失效后跳转的url
	private String sessionInvalidUri;
	// 认证成功处理类
	private AuthenticationSuccessHandler successHandler;
	// 认证失败处理类
	private AuthenticationFailureHandler failureHandler;
	// 存储用户名的请求参数名
	private String usernameParameter;
	// 存储用户密码的请求参数名
	private String passwordParameter;
	
	public ForLoginConfiguration(HttpSecurity securityBuilder) {
		super(securityBuilder);
	}

	/** ------------------------------------------各属性值设置方法----------------------------------------------------------------- */

	/**
	 * 配置登录页面，默认值为get请求的“/login”
	 * @return
	 */
	public ForLoginConfiguration loginPage (String page) {
		this.loginPage = page;
		return this;
	}
	/**
	 * 录页面下表单提交的路径,此url可不在mvc中声明处理,默认值为post请求的“/login”
	 * @param page
	 * @return
	 */
	public ForLoginConfiguration loginProcessingUrl (String uri) {
		this.loginFormUrl = uri;
		return this;
	}
	/**
	 * 身份验证失败后跳转的uri，默认值为get请求的“/login/error”
	 * @param page
	 * @return
	 */
	public ForLoginConfiguration failureUrl (String uri) {
		this.failureUrl = uri;
		return this;
	}
	/**
	 * 直接访问登录页面而不是访问过其他页面进行跳转来的url,验证成功后访问此方法设置的url,默认为“/”
	 * @param page
	 * @return
	 */
	public ForLoginConfiguration defaultSuccessUrl (String uri) {
		this.defaultTargetUrl = uri;
		return this;
	}
	/**
	 * session失效后跳转的url,默认为"/login?invalid"
	 * @param uri
	 * @return
	 */
	public ForLoginConfiguration sessionInvalidUri (String uri) {
		this.sessionInvalidUri = uri;
		return this;
	}
	/**
	 * 指定要使用的AccessDeniedHandler的快捷方式是特定的错误页
	 * @param errorPage
	 * @return
	 */
	public ForLoginConfiguration accessDeniedPage (String errorPage) {
		this.errorPage = errorPage;
		return this;
	}
	/**
	 * 指定要使用的身份验证失败的处理程序,默认使用 {@link DefaultAuthenticationFailureHandler}}类
	 * @param failureHandler
	 * @return
	 */
	public ForLoginConfiguration failureHandler (AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
		return this;
	}
	/**
	 * 指定要使用的身份验证成功的处理程序，默认使用 {@link DefaultAuthenticationSuccessHandler}}类
	 * @param successHandler
	 * @return
	 */
	public ForLoginConfiguration successHandler (AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
		return this;
	}
	
	/** ------------------------------------------设置默认值----------------------------------------------------------------- */
	private String getLoginPage() {
		if(this.loginPage == null) {
			this.loginPage = "/login";
		}
		return this.loginPage;
	}
	private String getLoginFormUrl() {
		if(this.loginFormUrl == null) {
			this.loginFormUrl = "/login";
		}
		return this.loginFormUrl;
	}
	private String getDefaultSuccessUrl() {
		if(this.defaultTargetUrl == null) {
			this.defaultTargetUrl = "/";
		}
		return this.defaultTargetUrl;
	}
	protected String getErrorPage() {
		if(this.errorPage == null) {
			this.errorPage = "/login?accessDenied";
		}
		return errorPage;
	}
	private String getFailureUrl() {
		if(this.failureUrl == null) {
			this.failureUrl = "/login?error";
		}
		return failureUrl;
	}
	private String getSessionInvalidUri() {
		if(this.sessionInvalidUri == null) {
			this.sessionInvalidUri = "/login?invalid";
		}
		return sessionInvalidUri;
	}
	protected AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		if(this.successHandler == null) {
			this.successHandler = new DefaultAuthenticationSuccessHandler(http.getRequestCache(),getDefaultSuccessUrl());
		}
		return this.successHandler;
	}
	protected AuthenticationFailureHandler getAuthenticationFailureHandler() {
		if(this.failureHandler == null) {
			this.failureHandler = new DefaultAuthenticationFailureHandler(getFailureUrl());
		}
		return this.failureHandler;
	}
	private String getUsernameParameter() {
		if(this.usernameParameter == null) {
			this.usernameParameter = "username";
		}
		return this.usernameParameter;
	}
	private String getPasswordParameter() {
		if(this.passwordParameter == null) {
			this.passwordParameter = "passwordParameter";
		}
		return this.passwordParameter;
	}
	
	private UsernamePasswordAuthenticationFilter creatFilter() {
		UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter(getLoginFormUrl()
				, getUsernameParameter(), getPasswordParameter());
		authenticationFilter.setFailureHandler(getAuthenticationFailureHandler());
		authenticationFilter.setSuccessHandler(getAuthenticationSuccessHandler());
		
		AuthenticationManager authenticationManager = super.http.getAuthenticationManager();
//		AuthenticationManager auth = super.securityBuilder.getAuthenticationManager();
		
//		System.out.println("参数传递测试: "+authenticationManager.equals(auth)); // 结果：true
		
		authenticationFilter.setAuthenticationManager(authenticationManager);
		
		authenticationFilter.setRememberMeServices(super.http.getRemrmberMeService());
		return authenticationFilter;
	}
	
	private DefaultLoginPageGeneratingFilter createLoginPageFilter() {
		DefaultLoginPageGeneratingFilter filter = new DefaultLoginPageGeneratingFilter(getLoginPage());
		filter.setAuthenticationUrl(getLoginFormUrl());
		filter.setLoginPageUrl(new AnyRequestMapping(getLoginPage(),HttpMethod.GET));
		filter.setFailureUrl(getFailureUrl());;
		filter.setUsernameParameter(getUsernameParameter());
		filter.setPasswordParameter(getPasswordParameter());
		filter.setSessionInvalidUri(getSessionInvalidUri());
		filter.setAccessDenied(getErrorPage());
		
		// 通过LogoutConfiguration获得logoutSuccessUrl
		Integer integer = http.getIndex().get(LogoutConfiguration.class.getName());
		LogoutConfiguration logoutConfiguration = (LogoutConfiguration) http.getConfigs().get(integer);
		String logoutSuccessUrl = logoutConfiguration.getDefLogoutSuccessUrl();
		filter.setLogoutSuccessUrl(logoutSuccessUrl);
		
		RememberMeService remrmberMeService = super.http.getRemrmberMeService();
		if(remrmberMeService != null) {
			filter.setRememberMeParameter( ((AbstractRememberMeService) remrmberMeService).getRememberMeParameter() );
		}
		return filter;
	}
	
	@Override
	public void config() {
		List<Filter> filters = super.http.getFilters();
		filters.add(creatFilter());
		
		// 登录页面是默认值则添加DefaultLoginPageGeneratingFilter到过滤器链中
		if(getLoginPage().equals("/login")) { 
			filters.add(createLoginPageFilter());
		}
		logger.info("已装配 ForLoginConfiguration,配置完毕.");
	}


	@Override
	public HttpSecurity and() {
		return super.http;
	}
}
