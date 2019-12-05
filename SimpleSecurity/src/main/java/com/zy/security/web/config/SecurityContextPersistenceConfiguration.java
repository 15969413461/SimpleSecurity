
package com.zy.security.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.config.subject.HttpSecurity;
import com.zy.security.web.filter.SecurityContextPersistenceFilter;

/**
* @author zy
* @Date 2019-11-29 周五 下午 14:37:41
* @Description SecurityContextPersistenceFilter配置类
* @version 
*/
public class SecurityContextPersistenceConfiguration extends AbstractHttpConfigurer<HttpSecurity> {
	private Logger logger = LoggerFactory.getLogger(SecurityContextPersistenceConfiguration.class);
	
	
	public SecurityContextPersistenceConfiguration(HttpSecurity securityBuilder) {
		super(securityBuilder);
	}

	private SecurityContextPersistenceFilter createFilter() {
		return new SecurityContextPersistenceFilter();
	}
	
	@Override
	public void config() {
		http.getFilters().add(createFilter());
		logger.info("已装配 SecurityContextPersistenceConfiguration,配置完毕.");
	}

	@Override
	public HttpSecurity and() {
		return super.http;
	}

}
