
package com.zy.security.web.config.subject;

import java.util.Map;

import com.zy.security.core.userdetails.NotUserCache;
import com.zy.security.web.config.AuthenticationManagerBuilder;

/**
* @author zy
* @Date 2019-11-27 周三 下午 16:35:42
* @Description 配置类，覆写此类方法实现用户的自定义配置
* @version 
*/
public class WebSecurityConfigurerAdapter {
	private Map<String, String[]> map; 
	/**
	 * http默认配置，相比于configure(HttpSecurity http),优先调用此方法
	 * @param http
	 */
	protected final void configureParent(HttpSecurity http) {
		http.formLogin()
		.and()
			.csrf()
			.maxCsrfTokenCount(5)
		.and()
			.logout()
		.and()
			.rememberMe()
		.and()
			.anonymous().RolePermissionType(false).principal("annoUser")
		.and()
			.SessionManager();
//		.and()
//			.authorizeRequests()
//			.antMatchers("/").hasAnyRole("user:edit,add")
//			.antMatchers("/test").hasAnyRole("user:edit,add","news:edit,add")
//			.antMatchers("/add").hasOrRole("user:edit,add","news:edit,add");
		// 追加注解携带的权限映射信息
		for (String uri : map.keySet()) {
			http.authorizeRequests().antMatchers(uri).hasOrRole(map.get(uri));
		}
		configure(http);
	}
	/**
	 * 身份认证默认配置，相比于configure(AuthenticationManagerBuilder auth),优先调用此方法
	 * @param auth
	 */
	protected final void configureParent(AuthenticationManagerBuilder auth) {
		auth.userCache(new NotUserCache());
		configure(auth);
	}
	/**
	 * 默认的WebSecurity配置
	 * @param web
	 */
	public void configureParent(WebSecurity web) {
		web.debug(true)
		   .ignoring()
		   		.antMatchers("/js/**","/img/**","/favicon.ico");
	}
	/**
	 * 自定义的WeSecurity配置
	 * @param web
	 */
	public void configure(WebSecurity web) {
		
	}
	/**
	 * 自定义的 http配置
	 * @param http
	 */
	public void configure(HttpSecurity http) {}
	
	/**
	 * 自定义的身份认证配置
	 * @param auth
	 */
	public void configure(AuthenticationManagerBuilder auth) {}
	
	public Map<String, String[]> getMap() {
		return map;
	}
	public void setMap(Map<String, String[]> map) {
		this.map = map;
	}
}
