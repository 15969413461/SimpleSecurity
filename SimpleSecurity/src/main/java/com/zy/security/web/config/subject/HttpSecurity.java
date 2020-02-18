
package com.zy.security.web.config.subject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.AnonymousAuthenticationProvider;
import com.zy.security.core.authentication.ProviderManager;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.interfaces.UserCache;
import com.zy.security.core.authentication.interfaces.UserDetailsChecker;
import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.core.authentication.rememberme.interfaces.RememberMeService;
import com.zy.security.web.config.AbstractHttpConfigurer;
import com.zy.security.web.config.AnonymousConfiguration;
import com.zy.security.web.config.AuthenticationManagerBuilder;
import com.zy.security.web.config.AuthorizationConfiguration;
import com.zy.security.web.config.CsrfConfiguration;
import com.zy.security.web.config.ForLoginConfiguration;
import com.zy.security.web.config.LogoutConfiguration;
import com.zy.security.web.config.RememberMeConfiguration;
import com.zy.security.web.config.SecurityContextPersistenceConfiguration;
import com.zy.security.web.config.SessionManagerConfiguration;
import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;
import com.zy.security.web.interfaces.RequestCache;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;

/**
* @author zy
* @Date 2019-11-27 周三 上午 01:04:51
* @Description 负责配置对象存储与数据流转，最终整合数据得到已排序的过滤器链
* @version 
*/
public final class HttpSecurity implements AbstractHttpSecurity {
	private Logger logger = LoggerFactory.getLogger(HttpSecurity.class);
	
	// 【filter执行顺序：filter对象】
	private List<Filter> filters = new LinkedList<>();
	
	// 存储使用到的AbstractHttpConfigurer实现类 [className:obj在List中的索引]
	private Map<String,Integer> index = new HashMap<>();
	private List<AbstractHttpConfigurer<HttpSecurity>> configs = new LinkedList<>();
	
	private FilterComparator filterComparator =  new FilterComparator();
	
	/**
	 * 永远存储的是最根本的AuthenticationManager.<br/>
	 * 由 {@link AuthenticationManagerBuilder}的 authenticationManagerBuilder()方法所填充. <br/>
	 * 即: 至少包括 {@link AnonymousAuthenticationProvider} 对象的 {@link ProviderManager}对象.
	 */
	private AuthenticationManager authenticationManager;
	/**
	 * 在 {@linkplain RememberMeConfiguration}的crateFilter()方法填充 
	 */
	private AuthenticationFailureHandler failureHandler;
	/**
	 * 在 {@linkplain RememberMeConfiguration}的crateFilter()方法填充 
	 */
	private AuthenticationSuccessHandler successHandler;
	/** 
	 * 在 {@linkplain CsrfConfiguration } 的crateFilter()方法填充 访问拒绝处理程序 
	 */
	private AccessDeniedHandler accessDeniedHandler;
	private String annoKey;
	private String RemememKey;
	private RequestCache requestCache;
	private UserCache userCache;
	private UserDetailsChecker userDetailsChecker;
	private UserDetailsService userDetailsService;
	
	/**
	 * 由 {@link CsrfConfiguration} 所填充
	 */
	private CsrfTokenRepository csrfTokenRepository;
	/**
	 * 由 {@link RememberMeConfiguration} 的createRememberMeService()方法所填充
	 */
	private RememberMeService remrmberMeService;

	/**
	 * 控制是否加载 {@link CsrfConfiguration}配置类,默认为true.即默认加载{@link CsrfConfiguration }配置类
	 */
//	private boolean enableCsrf = true;
	
	
	public HttpSecurity() {
		super();
	}
	
	/**
	 * 初始化不需要额外参数配置AbstractHttpConfigurer实现类
	 */
	protected void init() {
		this.getOrApply(new SecurityContextPersistenceConfiguration(this));
	}
	
	@Override
	public ForLoginConfiguration formLogin() {
		return getOrApply(new ForLoginConfiguration(this));
	}

	@Override
	public LogoutConfiguration logout() {
		return getOrApply(new LogoutConfiguration(this));
	}

	@Override
	public AnonymousConfiguration anonymous() {
		return getOrApply(new AnonymousConfiguration(this));
	}

	@Override
	public RememberMeConfiguration rememberMe() {
		return getOrApply(new RememberMeConfiguration(this));
	}

	@Override
	public CsrfConfiguration csrf() {
//		return enableCsrf ? getOrApply(new CsrfConfiguration(this)) : null; // 造成空指针异常
		return getOrApply(new CsrfConfiguration(this));
	}

	@Override
	public SessionManagerConfiguration SessionManager() {
		return getOrApply(new SessionManagerConfiguration(this));
	}
	
	@Override
	public AuthorizationConfiguration authorizeRequests() {
		return getOrApply(new AuthorizationConfiguration(this));
	}
	
	/**
	 * 允许在一个已知的过滤器(已注册的过滤器)之后添加过滤器
	 * @param filter - 要在类型Afterfilter之后注册的过滤器
	 * @param afterFilter - 已知过滤器，存在于过滤器集合中
	 */
	public HttpSecurity addFilterAfter(Filter filter,Class<? extends Filter> afterFilter) {
		filterComparator.registerAfter(filter.getClass(), afterFilter);
		filters.add(filter);
		return this; 
	}
	
	/**
	 * 在指定已知过滤器(已注册的过滤器)的所在位置添加过滤器,而原位置的过滤器往后移一位
	 * @param filter - 注册的过滤器
	 * @param atFilter - 已注册的过滤器
	 */
	public HttpSecurity addFilterAt(Filter filter,Class<? extends Filter> atFilter) {
		filterComparator.registerAt(filter.getClass(), atFilter);
		filters.add(filter);
		return this; 
	}
	
	/**
	 * 允许在某个已知过滤器(已注册的过滤器)之前添加过滤器
	 * @param filter - 注册的过滤器
	 * @param beforeFilter - 已注册的过滤器
	 */
	public HttpSecurity addFilterBefore(Filter filter,Class<? extends Filter> beforeFilter) {
		filterComparator.registerBefore(filter.getClass(), beforeFilter);
		filters.add(filter);
		return this; 
	}
	
	/**
	 * 在过滤器链末尾追加一个过滤器
	 * @return
	 */
	public HttpSecurity addFilter(Filter filter) {
		filterComparator.registerEnd(filter.getClass());
		filters.add(filter);
		return this; 
	}
	
	
//	public HttpSecurity disableCsrfConfig() {
//		this.enableCsrf = false;
//		return this;
//	}
	
	/** -----------------------------------------getter()、setter()------------------------------------------------------- */
	public List<Filter> getFilters() {
		return filters;
	}
	public void setAuthenticationManager(AuthenticationManager auth) {
		this.authenticationManager = auth;
	}
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	public String getAnnoKey() {
		return annoKey;
	}
	public void setAnnoKey(String annoKey) {
		this.annoKey = annoKey;
	}
	public String getRemememKey() {
		return RemememKey;
	}
	public void setRemememKey(String remememKey) {
		RemememKey = remememKey;
	}
	public Map<String, Integer> getIndex() {
		return index;
	}
	public List<AbstractHttpConfigurer<HttpSecurity>> getConfigs() {
		return configs;
	}
	public UserCache getUserCache() {
		return userCache;
	}
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}
	public UserDetailsChecker getUserDetailsChecker() {
		return userDetailsChecker;
	}
	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	public RememberMeService getRemrmberMeService() {
		return remrmberMeService;
	}
	public void setRemrmberMeService(RememberMeService remrmberMeService) {
		this.remrmberMeService = remrmberMeService;
	}
	public CsrfTokenRepository getCsrfTokenRepository() {
		return csrfTokenRepository;
	}
	public void setCsrfTokenRepository(CsrfTokenRepository csrfTokenRepository) {
		this.csrfTokenRepository = csrfTokenRepository;
	}
	public AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}
	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}
	public AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}
	public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
	}
	public AccessDeniedHandler getAccessDeniedHandler() {
		return accessDeniedHandler;
	}
	public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
		this.accessDeniedHandler = accessDeniedHandler;
	}
	public RequestCache getRequestCache() {
		return requestCache;
	}
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	

	@Override
	public List<Filter> config() {
		// 对Config对象进行排序
		Collections.sort(this.configs, new ConfigurationComparator());
		
		
		for (int i = 0; i < configs.size(); i++) {
			AbstractHttpConfigurer<HttpSecurity> configurer = configs.get(i);
			// 通过排序后的Config对象更新它们在List中的索引,方便之后的config对象获取
			this.index.put(configurer.getClass().getName(), i);
		}
		
		for (AbstractHttpConfigurer<HttpSecurity> configurer : configs) {
			// 分别调用各个配置存储类的config方法创建Filter
			configurer.config();
		}
		Collections.sort(this.filters, filterComparator);
		
		logger.info("FIlter链初始化完毕.");
		return this.filters;
	}
	
	/**
	 * 控制配置类的唯一性，同时记录启用的配置类，方便用户实现自己所需的方法
	 * @param config
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private <C extends AbstractHttpConfigurer<HttpSecurity>>C getOrApply(C config){
		String name = config.getClass().getName();
		Integer index  =  this.index.get(name);
		if(index == null) { // 第一次为添加
			this.index.put(name, this.configs.size());
			this.configs.add(config);
			return config;
		}
		// 之后为获得第一次添加的对象
		AbstractHttpConfigurer<HttpSecurity> configurer = this.configs.get(index);
		return (C) configurer;
	}
	
	public <C extends AbstractHttpConfigurer<HttpSecurity>> C removeConfigurer(C config) {
		String name = config.getClass().getName();
		int integer = this.getIndex().get(config.getClass().getName());
		if(integer == 0 ) {
			logger.error("忽略配置失败，by：{}",name);
			return null;
		}
		AbstractHttpConfigurer<HttpSecurity> remove = this.configs.remove(integer);
		logger.info("忽略配置,by：{}",remove);
		
		logger.info("忽略配置成功,by：{}",name);
		return config;
		
	}
}
