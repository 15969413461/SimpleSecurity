
package com.zy.security.web.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.AbstractUserDetailsAuthenticationProvider;
import com.zy.security.core.authentication.AnonymousAuthenticationProvider;
import com.zy.security.core.authentication.DaoAuthenticationProvider;
import com.zy.security.core.authentication.DefaultUserDetailsChecker;
import com.zy.security.core.authentication.DefaultUserDetailsService;
import com.zy.security.core.authentication.ProviderManager;
import com.zy.security.core.authentication.RememberMeAuthenticationProvider;
import com.zy.security.core.authentication.User;
import com.zy.security.core.authentication.compare.DefaultPasswordEncoder;
import com.zy.security.core.authentication.exception.AuthenticationProviderNotFoundException;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.interfaces.AuthenticationProvider;
import com.zy.security.core.authentication.interfaces.PasswordEncoder;
import com.zy.security.core.authentication.interfaces.UserCache;
import com.zy.security.core.authentication.interfaces.UserDetailsChecker;
import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.core.userdetails.NotUserCache;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.interfaces.RequestCache;
import com.zy.security.web.util.DefaultRequestCache;

/**
 * @author zy
 * @Date 2019-11-26 周二 上午 01:10:17
 * @Description RememberMeAuthenticationProvider - rememberMeService
 * @version
 */
public class AuthenticationManagerBuilder {
	private Logger logger = LoggerFactory.getLogger(AuthenticationManagerBuilder.class);
	
	// 身份认证管理器
	protected AuthenticationManager authenticationManager;
	// 对其他AuthenticationManager实现类的引用
	protected AuthenticationManager parent;
	// 验证器集合
	protected List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
	// 用于从外部加载用户验证所需数据
	protected UserDetailsService userDetailsService;
	// 是否清除已认证Token的凭证信息（如：密码、验证码等）,默认需清除密码信息
	protected Boolean eraseCredentials = true;
	protected PasswordEncoder passwordEncoder;
	// 存储被打断的请求
	protected RequestCache requestCache;
	// UserDetails缓存
	protected UserCache userCache;
	// UserDetails检检查
	protected UserDetailsChecker userDetailsChecker;
	
	/** ------------------------------------------各属性值设置方法----------------------------------------------------------------- */
	
	/**
	 * 设置验证器
	 * 
	 * @param authenticationProvider
	 * @return
	 */
	public AuthenticationManagerBuilder authenticationProvider(AuthenticationProvider authenticationProvider) {
		this.authenticationProviders.add(authenticationProvider);
		return this;
	}

	/**
	 * 允许提供父身份验证管理器，如果此身份验证管理器无法尝试对提供的身份验证进行身份验证，则将尝试提供父身份验证管理器,主要用于添加扩展的
	 * {@link AuthenticationManager} 实现类
	 * 
	 * @param authenticationManager
	 * @return
	 */
	public AuthenticationManagerBuilder parentAuthenticationManager(AuthenticationManager authenticationManager) {
		if (authenticationManager instanceof AnonymousAuthenticationProvider) {
			return this;
		}
		if (authenticationManager instanceof RememberMeAuthenticationProvider) {
			return this;
		}
		if (authenticationManager instanceof DaoAuthenticationProvider) {
			return this;
		}
		this.parent = authenticationManager;
		return this;
	}

	/**
	 * 是否清除已认证Token的凭证信息（如：密码、验证码等）,默认需清除密码信息，true则在身份验证成功后清除密码信息，默认值为true
	 * 
	 * @param eraseCredentials
	 * @return
	 */
	public AuthenticationManagerBuilder eraseCredentials(boolean eraseCredentials) {
		if (eraseCredentials) { // 若配置的为true则直接返回
			return this;
		}
		this.eraseCredentials = eraseCredentials;
		return this;
	}

	/**
	 * 添加从外部加载用户验证所需数据的应用程序
	 * 
	 * @param userDetailsService
	 * @return
	 */
	public AuthenticationManagerBuilder userDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
		return this;
	}

	/**
	 * 设置用于编码密码的服务接口实现类,默认使用 {@link DefaultPasswordEncoder}实现类
	 * 
	 * @param passwordEncoder
	 * @return
	 */
	public AuthenticationManagerBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		return this;
	}

	/**
	 * 设置{@linkplain RequestCache}实现
	 * 
	 * @param userCache
	 * @return
	 */
	public AuthenticationManagerBuilder requestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
		return this;
	}
	
	/**
	 * 设置{@linkplain UserCache}实现
	 * 
	 * @param userCache
	 * @return
	 */
	public AuthenticationManagerBuilder userCache(UserCache userCache) {
		this.userCache = userCache;
		return this;
	}

	/**
	 * 设置 {@link UserDetailsChecker}实现
	 * 
	 * @param userDetailsChecker
	 * @return
	 */
	public AuthenticationManagerBuilder userDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
		return this;
	}

	/** ------------------------------------------设置默认值----------------------------------------------------------------- */
	
	private UserDetailsService getUserDetailsService() {
		if (this.userDetailsService == null) {
			User user = new User();
			this.userDetailsService = new DefaultUserDetailsService(user);
		}
		return this.userDetailsService;
	}

	private PasswordEncoder getPasswordEncoder() {
		 if(this.passwordEncoder == null) {
			 this.passwordEncoder = new DefaultPasswordEncoder();
		 }
		return this.passwordEncoder;
	}

	private RequestCache getRequestCache() {
		if (this.requestCache == null) {
			this.requestCache = new DefaultRequestCache();
		}
		return this.requestCache;
	}
	
	private UserCache getUserCache() {
		if (this.userCache == null) {
			this.userCache = new NotUserCache();
		}
		return this.userCache;
	}

	private UserDetailsChecker getUserDetailsChecker() {
		if (this.userDetailsChecker == null) {
			this.userDetailsChecker = new DefaultUserDetailsChecker();
		}
		return this.userDetailsChecker;
	}

	/**
	 * 交给 AbstractAuthenticationProcessingFilter
	 * 的AuthenticationManager主要包含对匿名用户和rememberMe用户的验证器 ，通过parent引用其他的验证器
	 * 
	 * @return
	 */
	public AuthenticationManager authenticationManagerBuilder(HttpSecurity http) {
		List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
		
		String annoKey = getkey();
		logger.info("AnonymousKey: {}",annoKey);
		
		http.setAnnoKey(annoKey); // 保存到HttpSecurity中，便于创建RememberMeService时使用
		AnonymousAuthenticationProvider anonymousAuthenticationProvider = new AnonymousAuthenticationProvider(annoKey);
		authenticationProviders.add(anonymousAuthenticationProvider);
		
		Integer index = http.getIndex().get(RememberMeConfiguration.class.getName());
		if(index != null) { // 启用了rememberMe服务
			String string = getkey();
			logger.info("RememberMeKey: {}",string);

			http.setRemememKey(string); // 保存到HttpSecurity中，便于创建RememberMeService时使用
			RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider(string);
			authenticationProviders.add(rememberMeAuthenticationProvider);
		}
		
		try {
			this.authenticationManager = new ProviderManager(authenticationProviders,
					authenticationManagerParentBuilder());

			((ProviderManager) this.authenticationManager)
					.setEraseCredentialsAfterAuthentication(this.eraseCredentials);

		} catch (AuthenticationProviderNotFoundException e) {
			e.printStackTrace();
		}
		
		http.setUserCache(getUserCache());
		http.setUserDetailsChecker(getUserDetailsChecker());
		http.setUserDetailsService(getUserDetailsService());
		http.setRequestCache(getRequestCache());
		return this.authenticationManager;
	}

	/**
	 * 构造第二梯队的验证器，对非匿名Token和非RememberMeToken进行验证
	 * @return
	 */
	private AuthenticationManager authenticationManagerParentBuilder() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		// 在验证器容器的最后保存默认的验证器，若用户没有设置其他验证器则此验证器为第一个验证器
		this.authenticationProviders.add(this.authenticationProviders.size(), daoAuthenticationProvider);

		AbstractUserDetailsAuthenticationProvider auth = null;
		
		// 为 AbstractUserDetailsAuthenticationProvider 的实现类设置成员变量的实现类
		for (AuthenticationProvider authenticationProvider : authenticationProviders) {
			if (authenticationProvider instanceof AbstractUserDetailsAuthenticationProvider) {
				auth = (AbstractUserDetailsAuthenticationProvider) authenticationProvider;
				
				auth.setChecker(getUserDetailsChecker());
				auth.setPasswordEncoder(getPasswordEncoder());
				auth.setUserCache(getUserCache());
				auth.setUserDetailsService(getUserDetailsService());
			}
		}

		ProviderManager pm = null;
		try {
			pm = (ProviderManager) new ProviderManager(authenticationProviders, this.parent);
			pm.setEraseCredentialsAfterAuthentication(this.eraseCredentials);
		} catch (AuthenticationProviderNotFoundException e) {
			e.printStackTrace();
		}

		return pm;
	}
	
	public AuthenticationManagerBuilder and() {
		return this;
	}
	
	protected String getkey() {
		return UUID.randomUUID().toString();
	}
}
