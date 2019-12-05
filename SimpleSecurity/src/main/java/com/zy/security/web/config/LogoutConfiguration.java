
package com.zy.security.web.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.LogoutFilter;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.logout.CookieCleanLogoutHandler;
import com.zy.security.web.logout.CsrfLogoutHandler;
import com.zy.security.web.logout.LogoutHandler;
import com.zy.security.web.logout.LogoutSuccessHandler;
import com.zy.security.web.logout.SecurityContextLogoutHandler;
import com.zy.security.web.logout.SimpleLogoutSuccessHandler;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.HttpMethod;
import com.zy.utils.Assert;

/**
* @author zy
* @Date 2019-11-24 周日 下午 14:24:22
* @Description
* @version 
*/
public class LogoutConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(LogoutConfiguration.class);
	
	private String defLogoutUrl = "/logout";
	private String defLogoutSuccessUrl = "/login?logout";

	private RequestMatcher logoutUrl;

	private List<LogoutHandler> logoutHandlers = new ArrayList<>();
	private LogoutSuccessHandler logoutSuccessHandler;
	
	// 清空ThreadLocal的认证信息
	private SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
	
	/**
	 * 默认构造器
	 * @param httpSecurity 
	 */
	public LogoutConfiguration(HttpSecurity httpSecurity) {
		super(httpSecurity);
	}
	
	
	/** ------------------------------------------各属性值设置方法----------------------------------------------------------------- */
	
	/**
	 * 设置退出uri,默认使用post请求
	 * @param logoutUrl
	 * @return
	 */
	public LogoutConfiguration logoutUrl(String logoutUrl) {
		this.logoutUrl = new AnyRequestMapping(logoutUrl, HttpMethod.POST);
		return this;
	}
	/**
	 * 设置退出uri,post、get亦可
	 * @param logoutUrl
	 * @return
	 */
	public LogoutConfiguration logoutUrl(AnyRequestMapping mapping) {
		this.logoutUrl = mapping;
		return this;
	}
	/**
	 * 设置用户退出操作处理类
	 * @param logoutHandler
	 * @return
	 */
	public LogoutConfiguration addLogoutHandler(LogoutHandler logoutHandler) {
		Assert.notNull(logoutHandler, "logoutHandler不能为null");
		this.logoutHandlers.add(logoutHandler);
		return this;
	}
	
	/**
	 * 设置退出成功后跳转的uri,默认使用get请求
	 * @param logoutSuccessUrl
	 * @return
	 */
	public LogoutConfiguration logoutSuccessUrl(String logoutSuccessUrl) {
		this.defLogoutSuccessUrl = logoutSuccessUrl;
		return this;
	}
	/**
	 * 设置退出成功处理类
	 * @param logoutSuccessHandler
	 * @return
	 */
	public LogoutConfiguration logoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
		this.logoutSuccessHandler = logoutSuccessHandler;
		return this;
	}
	
	/**
	 * 设置退出时要删除的cookie
	 * @param cookieNamesToClear
	 * @return
	 */
	public LogoutConfiguration deleteCookies(String... cookieNamesToClear) {
		List<String> asList = Arrays.asList(cookieNamesToClear);
		CookieCleanLogoutHandler cookieCleanLogoutHandler = new CookieCleanLogoutHandler(asList);
		return addLogoutHandler(cookieCleanLogoutHandler);
	}
	
	/**
	 * 获得被过滤的退出uri，默认使用post请求的"/logout"
	 * @return
	 */
	protected RequestMatcher getLogoutUrl() {
		if(logoutUrl == null) {
			this.logoutUrl(defLogoutUrl);
		}
		return logoutUrl;
	}
	protected String getDefLogoutUrl() {
		return defLogoutUrl;
	}
	protected String getDefLogoutSuccessUrl() {
		return defLogoutSuccessUrl;
	}
	/** ------------------------------------------设置默认值----------------------------------------------------------------- */
	
	/**
	 * 获得退出成功处理类，默认使用 {@link SimpleLogoutSuccessHandler}
	 * @return
	 */
	private LogoutSuccessHandler getLogoutSuccessHandler() {
		LogoutSuccessHandler handler = this.logoutSuccessHandler;
		if (handler == null) {
			handler = new SimpleLogoutSuccessHandler(this.defLogoutSuccessUrl);
		}
		return handler;
	}

	private LogoutFilter createLogoutFilter() {
		// SecurityContextLogoutHandler设置为最后一个Logouthandler
		Integer integer = http.getIndex().get(CsrfConfiguration.class.getName());
		if(integer != null) {
			CsrfLogoutHandler csrfLogoutHandler = new CsrfLogoutHandler(http.getCsrfTokenRepository());
			logoutHandlers.add(csrfLogoutHandler);
		}
		logoutHandlers.add(contextLogoutHandler);
		
		LogoutFilter logoutFilter = new LogoutFilter(getLogoutUrl(), logoutHandlers, getLogoutSuccessHandler());
		return logoutFilter;
	}

	@Override
	public void config() {
		List<Filter> filters = super.http.getFilters();
		filters.add(createLogoutFilter());
		logger.info("已装配 LogoutConfiguration,配置完毕.");
	}


	@Override
	public HttpSecurity and() {
		return super.http;
	}
}
