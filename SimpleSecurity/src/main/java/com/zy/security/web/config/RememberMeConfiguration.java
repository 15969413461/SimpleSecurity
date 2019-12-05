
package com.zy.security.web.config;

import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.ProviderManager;
import com.zy.security.core.authentication.RememberMeAuthenticationProvider;
import com.zy.security.core.authentication.interfaces.AuthenticationProvider;
import com.zy.security.core.authentication.rememberme.AbstractRememberMeService;
import com.zy.security.core.authentication.rememberme.DefaultRememberMeService;
import com.zy.security.core.authentication.rememberme.interfaces.RememberMeService;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.RememberMeAuthenticationFilter;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;
import com.zy.security.web.interfaces.AuthenticationSuccessHandler;

/**
 * @author zy
 * @Date 2019-11-25 周一 下午 13:17:38
 * @Description 自定义token的RememberMeService扩展
 * @version
 */
public class RememberMeConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(RememberMeConfiguration.class);
	
//	private RememberMeService rememberMeService;
	// 用于指示在登录时记住用户的HTTP(name)参数
	private String rememberMeParameter;
	// cookie 名称
	private String rememberMeCookieName;
	// remember me cookie的作用域
	private String rememberMeCookiePath;
	// private PersistentTokenRepository tokenRepository;
	private int tokenValiditySeconds;
	
	/** ------------------------------------------各属性值设置方法----------------------------------------------------------------- */
	
	public RememberMeConfiguration(HttpSecurity httpSecurity) {
		super(httpSecurity);
	}

	/**
	 * 设置remember me cookie在当前站点的作用域，默认为“/”
	 * 
	 * @param path
	 * @return
	 */
	public RememberMeConfiguration rememberMeCookiePath(String path) {
		this.rememberMeCookiePath = path;
		return this;
	}

	/**
	 * 储用于“记住我”身份验证的令牌的cookie的名称。默认为'remember-me'
	 * @param name
	 * @return
	 */
	public RememberMeConfiguration rememberMeCookieName(String name) {
		this.rememberMeCookieName = name;
		return this;
	}

	/**
	 * 用于指示在登录时记住用户的HTTP(name)参数，默认值为“rememberMe”
	 * <p>
	 * 如：< ... type="checkbox" name="rememberMe">'
	 * <p/>
	 * @param parame
	 * @return
	 */
	public RememberMeConfiguration rememberMeParameter(String parame) {
		this.rememberMeParameter = parame;
		return this;
	}

	/**
	 * 设置cookie过期时间，默认值为7天（604800s）
	 * @param seconds
	 * @return
	 */
	public RememberMeConfiguration tokenValiditySeconds(int seconds) {
		this.tokenValiditySeconds = seconds;
		return this;
	}
	
	/**
	 * 正对于用户自定义Token的RememberMeService服务配置
	 * @param seconds
	 * @return
	 */
	public RememberMeConfiguration RememberMeServiceParent(AbstractRememberMeService  rememberMeService) {
//		this.remems = rememberMeService;
		return this;
	}
	
	/** ------------------------------------------设置默认值----------------------------------------------------------------- */
	
	private String getRememberMeParameter() {
		if (this.rememberMeParameter == null) {
			this.rememberMeParameter = "rememberMe";
		}
		return rememberMeCookieName;
	}

	private String getRememberMeCookieName() {
		if (this.rememberMeCookieName == null) {
			this.rememberMeCookieName = "remember-me";
		}
		return this.rememberMeCookieName;
	}

	private String getRememberMeCookiePath() {
		if (this.rememberMeCookiePath == null) {
			this.rememberMeCookiePath = "/";
		}
		return this.rememberMeCookiePath;
	}

	private int getTokenValiditySeconds() {
		if (this.tokenValiditySeconds == 0) {
			this.tokenValiditySeconds = 604800;
		}
		return this.tokenValiditySeconds;
	}
	
	
	private RememberMeService createRememberMeService() {
		// 创建对象
		AbstractRememberMeService rememberMeService = new DefaultRememberMeService(getRememberMeCookieName(), getRememberMeParameter()
				, getTokenValiditySeconds(), getRememberMeCookiePath());
		// 填充属性
		rememberMeService.setUserDetailsChecker(http.getUserDetailsChecker());
		rememberMeService.setUserDetailsService(http.getUserDetailsService());
		rememberMeService.setKey(http.getRemememKey());
		return rememberMeService;
	}
	
	private RememberMeAuthenticationFilter crateFilter() {
		ProviderManager authenticationManager = (ProviderManager)http.getAuthenticationManager();
		List<AuthenticationProvider> authProviderLst = authenticationManager.getAuthProviderLst();
		
		// 创建针对于RememberMeAuthenticationToken的验证器
		RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider(http.getRemememKey());
		
		// 将验证器添加到验证器集合中
		authProviderLst.add(rememberMeAuthenticationProvider);
		
		RememberMeService rememberMeService = createRememberMeService();
		
		http.setRemrmberMeService(rememberMeService); // 保存到HttpServurity中，尤其转交给AbstractAuthenticationProcessingFilter
		
		RememberMeAuthenticationFilter rememberMeAuthenticationFilter = new RememberMeAuthenticationFilter(authenticationManager, rememberMeService);
		
		Integer integer = http.getIndex().get(ForLoginConfiguration.class.getName());
		ForLoginConfiguration forLoginConfiguration = (ForLoginConfiguration) http.getConfigs().get(integer);
		AuthenticationFailureHandler failureHandler = forLoginConfiguration.getAuthenticationFailureHandler();
		AuthenticationSuccessHandler successHandler = forLoginConfiguration.getAuthenticationSuccessHandler();
		http.setSuccessHandler(successHandler);
		http.setFailureHandler(failureHandler);
		
		rememberMeAuthenticationFilter.setSuccessHandler(successHandler);
		
		return rememberMeAuthenticationFilter;
	}

	@Override
	public void config() {
		List<Filter> filters = http.getFilters(); 
		filters.add(crateFilter());
		logger.info("已装配 RememberMeConfiguration,配置完毕.");
	}

	@Override
	public HttpSecurity and() {
		return super.http;
	}
}
