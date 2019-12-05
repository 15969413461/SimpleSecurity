
package com.zy.security.web.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.AbstractAuthenticationProcessingFilter;
import com.zy.security.web.filter.SessionManagementFilter;
import com.zy.security.web.interfaces.InvalidSessionStrategy;
import com.zy.security.web.session.ChangeSessionIdAuthenticationStrategy;
import com.zy.security.web.session.ConcurrentSessionControlAuthenticationStrategy;
import com.zy.security.web.session.CsrfAuthenticationStrategy;
import com.zy.security.web.session.DefaultSessionRegistry;
import com.zy.security.web.session.RegisterSessionAuthenticationStrategy;
import com.zy.security.web.session.SessionAuthenticationStrategyManager;
import com.zy.security.web.session.SessionCsrfTokenRepository;
import com.zy.security.web.session.SimpleRedirectInvalidSessionStrategy;
import com.zy.security.web.session.interfaces.SessionAuthenticationStrategy;
import com.zy.security.web.session.interfaces.SessionRegistry;

/**
 * @author zy
 * @Date 2019-11-25 周一 下午 14:43:18
 * @Description
 * @version
 */
public class SessionManagerConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(SessionManagerConfiguration.class);
	
	// session处理策略集合
	private List<SessionAuthenticationStrategy> sessionStrategyList;

//	private String expiredUri;
	private String invalidSessionUrl;
	
	// Session失效处理类
	private InvalidSessionStrategy invalidSessionStrategy;
	// SessionInformation操作类
	private SessionRegistry sessionRegistry;
	// session的最大并发数
	private int maxSessions;
	
	
	/**
	 * 默认构造器
	 * @param httpSecurity 
	 */
	public SessionManagerConfiguration(HttpSecurity httpSecurity) {
		super(httpSecurity);
		sessionStrategyList = new ArrayList<SessionAuthenticationStrategy>();
	}
	
	/** ------------------------------------------各属性值设置方法----------------------------------------------------------------- */
	
	/**
	 * session失效后跳转的uri,默认值为“/login?invalid ”
	 * @param invalidSessionUrl
	 * @return
	 */
	public SessionManagerConfiguration invalidSessionUrl(String invalidSessionUrl) {
		this.invalidSessionUrl = invalidSessionUrl;
		return this;
	}
	/**
	 * session过期后跳转的uri，默认值为“/login?expired”
	 * @param expiredUri
	 * @return
	 */
//	public SessionManagerConfiguration expiredUri(String expiredUri) {
//		this.expiredUri = expiredUri;
//		return this;
//	}
	/**
	 * 控制用户的最大会话数(session并发数量)。默认值是允许任意数量的用户
	 * @param num
	 * @return
	 */
	public SessionManagerConfiguration maximumSessions(int num) {
		this.maxSessions = num;
		return this;
	}
	/**
	 * 控制使用的会话注册表实现。默认使用 {@link DefaultSessionRegistry}
	 * @param sessionStrategy
	 * @return
	 */
	public SessionManagerConfiguration sessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
		return this;
	}
	/**
	 * session失效处理程序，默认使用 {@link SimpleRedirectInvalidSessionStrategy}
	 * @param sessionRegistry
	 * @return
	 */
	public SessionManagerConfiguration invalidSessionStrategy(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
		return this;
	}
	/**
	 * 追加认证成功后session处理策略（自定义策略）
	 * @param sessionAuthenticationStrategy
	 * @return
	 */
	public SessionManagerConfiguration SessionAuthenticationStrategy(SessionAuthenticationStrategy sessionAuthenticationStrategy) {
		if(sessionAuthenticationStrategy instanceof ConcurrentSessionControlAuthenticationStrategy) {
			return this;
		}
		if(sessionAuthenticationStrategy instanceof ChangeSessionIdAuthenticationStrategy) {
			return this;
		}
		if(sessionAuthenticationStrategy instanceof RegisterSessionAuthenticationStrategy) {
			return this;
		}
		if(sessionAuthenticationStrategy instanceof CsrfAuthenticationStrategy) {
			return this;
		}
		if(sessionAuthenticationStrategy instanceof SessionAuthenticationStrategyManager) {
			return this;
		}
		
		this.sessionStrategyList.add(sessionAuthenticationStrategy);
		return this;
	}
	
	/** ------------------------------------------设置默认值----------------------------------------------------------------- */
	
	private String getInvalidSessionUrl() {
		if(this.invalidSessionUrl == null) {
			this.invalidSessionUrl = "/login?invalid";
		}
		return this.invalidSessionUrl;
	}
	private SessionRegistry getSessionStrategy() {
		if(this.sessionRegistry == null) {
			this.sessionRegistry = new DefaultSessionRegistry();
		}
		return this.sessionRegistry;
	}
	private InvalidSessionStrategy getInvalidSessionStrategy() {
		if(this.invalidSessionStrategy == null) {
			this.invalidSessionStrategy = new SimpleRedirectInvalidSessionStrategy(getInvalidSessionUrl());
		}
		return this.invalidSessionStrategy;
	}
	
	
	private SessionManagementFilter createFilter() {
		ConcurrentSessionControlAuthenticationStrategy concurSessionStrategy  = null;
		if(this.maxSessions < 1) { // 用户未指定最大并发数，则不作限制
			// 用户登录状态并发控制
			concurSessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(getSessionStrategy());
		}else {
			concurSessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(getSessionStrategy(),this.maxSessions);
		} 
		// 登录成功时迁移一个新会话给当前用户
		ChangeSessionIdAuthenticationStrategy changeSessionIdAuthenticationStrategy = new ChangeSessionIdAuthenticationStrategy();
		// Servlet的Session转化为框架自身持有的SessionInformation对象
		RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(getSessionStrategy());
		
		List<SessionAuthenticationStrategy> sessionStrategyList = new ArrayList<>();
		
		// 添加默认的session策略
		sessionStrategyList.add(concurSessionStrategy);
		sessionStrategyList.add(changeSessionIdAuthenticationStrategy);
		sessionStrategyList.add(registerSessionAuthenticationStrategy);
		
//		Integer integer = http.getIndex().get(CsrfConfiguration.class.getName());
		SessionCsrfTokenRepository csrfTokenRepository = (SessionCsrfTokenRepository) http.getCsrfTokenRepository();
		
		if(csrfTokenRepository != null) { // 启用了csrf防御
			CsrfAuthenticationStrategy csrfAuthenticationStrategy = new CsrfAuthenticationStrategy(csrfTokenRepository);
			csrfTokenRepository.setSessionStrategy(getSessionStrategy());
			
			// 添加session策略
			sessionStrategyList.add(csrfAuthenticationStrategy);
		}
		
		// 追加用户自定义的session策略
		sessionStrategyList.addAll(this.sessionStrategyList);
		
		SessionAuthenticationStrategyManager manager = new SessionAuthenticationStrategyManager(sessionStrategyList);
		
		Integer integer = http.getIndex().get(ForLoginConfiguration.class.getName());
		AbstractAuthenticationProcessingFilter filter = (AbstractAuthenticationProcessingFilter) http.getFilters().get(integer);
		filter.setSessionStrategyManager(manager);

		SessionManagementFilter sessionManagementFilter = new SessionManagementFilter(manager, getInvalidSessionStrategy());
		return sessionManagementFilter;
	}

	@Override
	public void config() {
		List<Filter> filters = http.getFilters();
		filters.add(createFilter());
		logger.info("已装配 SessionManagerConfiguration,配置完毕.");
	}

	@Override
	public HttpSecurity and() {
		return super.http;
	}
}
