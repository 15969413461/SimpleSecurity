
package com.zy.security.web.config.subject;

import java.util.ArrayList;
import java.util.List;

import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.HttpMethod;

/**
 * @author zy
 * @Date 2019-12-01 周日 下午 15:01:32
 * @Description 排除uri配置
 * @version
 */
public class IgnoringConfiguration {
	private WebSecurity webSecurity;
	// 排除的uri，此集合中的uri或此类uri将不会进入过滤器链
	public static final List<RequestMatcher> ignoredRequests = new ArrayList<>();
	
	public IgnoringConfiguration(WebSecurity webSecurity) {
		this.webSecurity = webSecurity;
	}
	
	/**
	 * 使用 {@link AntRequestMapping} 定义排除的uri,默认为get请求<br/>
	 * 要忽略post请求的uri请使用  antMatchers(AntRequest Mapping)方法.
	 * @param uris
	 * @return
	 */
	public List<RequestMatcher> antMatchers(String ... uris){
		for (String uri : uris) {
			if(uri == null || uri.isEmpty()) {
				continue ;
			}
			IgnoringConfiguration.ignoredRequests.add(new AntRequestMapping(uri, HttpMethod.GET));
		}
		return IgnoringConfiguration.ignoredRequests;
	}
	/**
	 * 使用 {@link AntRequestMapping} 定义排除的uri，get、post均可,受限于{@link HttpMethod}类支持的请求类型
	 * @param mapping
	 * @return
	 */
	public List<RequestMatcher> antMatchers(AntRequestMapping mapping){
		IgnoringConfiguration.ignoredRequests.add(mapping);
		return IgnoringConfiguration.ignoredRequests;
	}
	
	public WebSecurity and() {
		return this.webSecurity;
	}

	public WebSecurity getWebSecurity() {
		return webSecurity;
	}
	public void setWebSecurity(WebSecurity webSecurity) {
		this.webSecurity = webSecurity;
	}
	public List<RequestMatcher> getIgnoredRequests() {
		return ignoredRequests;
	}
}
