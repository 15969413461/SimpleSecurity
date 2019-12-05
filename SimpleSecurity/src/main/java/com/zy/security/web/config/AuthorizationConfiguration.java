
package com.zy.security.web.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.access.JdbcSecurityMetadataSource;
import com.zy.security.core.access.interfaces.SecurityMetadataSource;
import com.zy.security.web.config.subject.Authorization;
import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.AccessManagementFilter;
import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.HttpMethod;

/**
* @author zy
* @Date 2019-11-24 周日 下午 16:39:33
* @Description 用户授权配置方法类
* @version 
*/
public class AuthorizationConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private SecurityMetadataSource securityMetadataSource;
	
	// 存储进行权限限制的uri
	private Map<RequestMatcher,Authorization> authorizedUrlMap = new HashMap<>();
	
	
	public AuthorizationConfiguration(HttpSecurity http) {
		super(http);
	}

	/**
	 * 使用 {@link AntRequestMapping}定义绝对uri，且将全部uri默认定义为get请求
	 * @param uris
	 * @return
	 */
	public Authorization antMatchers(String ... uris) {
		Authorization authorization = new Authorization(this);
		for (String uri : uris) {
			authorizedUrlMap.put(new AnyRequestMapping(uri, HttpMethod.GET), authorization);
		}
		return authorization;
	}
	
	/**
	 * 使用 {@link AntRequestMapping}定义绝对uri，使用传入的参数定义请求类型
	 * @param httpMethod - uri的请求类型
	 * @param uris uri字符数组
	 * @return Authorization - 代表则此类uri的权限认证规则和操作
	 */
	public Authorization antMatchers(HttpMethod httpMethod,String ... uris) {
		Authorization authorization = new Authorization(this);
		for (String uri : uris) {
			authorizedUrlMap.put(new AnyRequestMapping(uri, httpMethod), authorization);
		}
		return authorization;
	}
	
	/**
	 * 设置 {@link SecurityMetadataSource}的实现类，默认使用 {@link JdbcSecurityMetadataSource}类
	 * @param securityMetadataSource
	 * @return
	 */
	public AuthorizationConfiguration SecurityMetadataSource(SecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
		return this;
	}
	
	
	
	private SecurityMetadataSource getSecurityMetadataSource() {
		if(this.securityMetadataSource == null) {
			this.securityMetadataSource = new JdbcSecurityMetadataSource();
		}
		return securityMetadataSource;
	}
	
	
	private AccessManagementFilter createFilter() {
		AccessDeniedHandler accessDeniedHandler = http.getAccessDeniedHandler();
		
		return new AccessManagementFilter(accessDeniedHandler,authorizedUrlMap,getSecurityMetadataSource());
	}
	
	@Override
	public void config() {
		http.getFilters().add(createFilter());
		logger.info("已装配 AuthorizationConfiguration,配置完毕.");
	}

	@Override
	public HttpSecurity and() {
		return super.http;
	}
}
