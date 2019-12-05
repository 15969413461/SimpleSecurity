
package com.zy.security.web.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.CsrfFilter;
import com.zy.security.web.filter.CsrfRequestMsgFilter;
import com.zy.security.web.handler.AccessDeniedHandlerImpl;
import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.session.SessionCsrfTokenRepository;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;
import com.zy.utils.Assert;

/**
* @author zy
* @Date 2019-11-24 周日 下午 15:23:50
* @Description errorPage - AccessDeniedHandler
* @version 
*/
public class CsrfConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(CsrfConfiguration.class);
	
	// Csrf凭证操作接口
	protected CsrfTokenRepository csrfTokenRepository;
	// 标识“记住我”身份验证创建的令牌,区分匿名Token和记住我Token
	protected String Rememberkey;
	// 被CsrfFilter过滤的uri
	private List<RequestMatcher> requireCsrfProtectionMatcher = new ArrayList<>();
	// 访问拒绝处理程序
	private AccessDeniedHandler accessDeniedHandler;
	// 最大比对次数
	private int maxCsrfTokenCount;

	private String cookieName;
	private String parameName;
	private String headerName;
	
	
	/**
	 * 默认构造器
	 */
	public CsrfConfiguration(HttpSecurity http) {
		super(http);
	}

	public HttpSecurity disable() {
		http.removeConfigurer(this);
		return super.http;
	}
	
	/**
	 * 设置CsrfTokenRepository实现类，默认使用 {@link SessionCsrfTokenRepository}
	 * <p>当前仅支持 {@link SessionCsrfTokenRepository}</p>
	 * @param csrfTokenRepository
	 * @return
	 */
	public CsrfConfiguration csrfTokenRepository (CsrfTokenRepository csrfTokenRepository) {
		Assert.notNull(csrfTokenRepository, "csrfTokenRepository不能为null");
//		if(! (csrfTokenRepository instanceof CookieCsrfTokenRepository) ) {
//			this.csrfTokenRepository = csrfTokenRepository;
//		}
		this.csrfTokenRepository = csrfTokenRepository;
		return this;
	}
	
	/**
	 * 设置启用csrf防御的uri
	 * @param mapping
	 * @return
	 */
	public CsrfConfiguration requireCsrfProtectionMatcher(RequestMatcher ... mapping) {
		Assert.notNull(mapping,"requireCsrfProtectionMatcher不能为null");
		List<RequestMatcher> list = Arrays.asList(mapping);
		this.requireCsrfProtectionMatcher.addAll(list);
		return this;
	}
	
	/**
	 * 设置访问拒绝处理程序，默认使用 {@link AccessDeniedHandlerImpl}
	 * @param accessDeniedHandler
	 * @return
	 */
	public CsrfConfiguration accessDeniedHandler (AccessDeniedHandler accessDeniedHandler) {
//		Assert.notNull(accessDeniedHandler,"AccessDeniedHandler不可为null.");
		this.accessDeniedHandler = accessDeniedHandler;
		return this;
	}
	
	/**
	 * 设置csrf凭证比对成功的最大值，若大于等于此值则更新csrf凭证信息。取值区间：[1,+∞],[-1]。<br/>
	 * 即：count=2，则比对成功2次后更新csrf凭证，count=-1，则永远不做变化
	 * @param count
	 * @return
	 */
	public CsrfConfiguration maxCsrfTokenCount (int count) {
		this.maxCsrfTokenCount = count;
		return this;
	}

	/**
	 * 
	 * @param cookieName
	 * @return
	 */
	public CsrfConfiguration csrfCookieName(String cookieName) {
		this.cookieName = cookieName;
		return this;
	}
	
	/**
	 * 保存客户端csrf凭证的请求参数名称,默认值为“_csrf”
	 * @param parameName
	 * @return
	 */
	public CsrfConfiguration csrfParameName(String parameName) {
		this.parameName = parameName;
		return this;
	}
	
	/**
	 * 保存客户端csrf凭证的请求头名称,默认值为“XSRF-TOKEN”
	 * @param parameName
	 * @return
	 */
	public CsrfConfiguration csrfHeaderName(String headerName) {
		this.headerName = headerName;
		return this;
	}
	
	/** ------------------------------------------设置默认值----------------------------------------------------------------- */
	
	private CsrfTokenRepository getCsrfTokenRepository() {
		if(this.csrfTokenRepository == null) {
			this.csrfTokenRepository = new SessionCsrfTokenRepository(getParameName(),getHeaderName());
		}
		return csrfTokenRepository;
	}
	
	private AccessDeniedHandler getAccessDeniedHandler() {
		if (accessDeniedHandler == null) {
			accessDeniedHandler = new AccessDeniedHandlerImpl();
		}
		return accessDeniedHandler;
	}
	
	private String getParameName() {
		if (this.parameName == null) {
			this.parameName = "_csrf";
		}
		return this.parameName;
	}
	
	private String getHeaderName() {
		if (this.headerName == null) {
			this.headerName = "XSRF-TOKEN";
		}
		return this.headerName;
	}
	
	@SuppressWarnings("unused")
	private String getCookieName() {
		if (this.cookieName == null) {
			this.cookieName = "XSRF-TOKEN";
		}
		return this.cookieName;
	}
	
	private CsrfFilter createCsrfFilter() {
		getAccessDeniedHandler();
		Map<String, Integer> index = super.http.getIndex();
		Integer integer = index.get(ForLoginConfiguration.class.getName());
		
		ForLoginConfiguration configurer = (ForLoginConfiguration) super.http.getConfigs().get(integer);
		
		String errorPage = configurer.getErrorPage();
		this.accessDeniedHandler.setErrorPage(errorPage);
		this.accessDeniedHandler.setRequestCache(http.getRequestCache());
		http.setAccessDeniedHandler(this.accessDeniedHandler);
		
		return new CsrfRequestMsgFilter(getCsrfTokenRepository(), accessDeniedHandler, maxCsrfTokenCount);
	}


	@Override
	public void config() {
		List<Filter> filters = super.http.getFilters();
		filters.add(createCsrfFilter());
		
		http.setCsrfTokenRepository(this.csrfTokenRepository);
		logger.info("已装配 CsrfConfiguration,配置完毕.");
	}


	@Override
	public HttpSecurity and() {
		return super.http;
	}
	
}
