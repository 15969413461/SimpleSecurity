
package com.zy.security.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.context.SecurityContext;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.AnonymousAuthenticationToken;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.core.userdetails.SimpleRolePermission;
import com.zy.security.core.userdetails.WebAuthenticationDetails;

/**
* @author zy
* @Date 2019-11-21 周四 上午 00:20:18
* @Description 如果用户到这一步还没有经过认证，将会为这个请求关联一个认证的 token，标识此用户是匿名用户
* @version 
*/
public class AnonymousAuthenticationFilter implements Filter {
//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 标识此用户的key，若key与实例化赋予的值不同则证明此Token被篡改
	private final String key;
	private Object principal;
	private List<? extends RolePermission> authorities;

	
	public String getKey() {
		return key;
	}
	public Object getPrincipal() {
		return principal;
	}
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	public List<? extends RolePermission> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<? extends RolePermission> authorities) {
		this.authorities = authorities;
	}
	
	
	/**
	 * principal：anonymousUser
	 * authorities：[ROLE_ANONYMOUS]
	 * @param key
	 */
	public AnonymousAuthenticationFilter(String key) {
		List<RolePermission> list = new ArrayList<>();
		list.add(new SimpleRolePermission("ROLE_ANONYMOUS"));
		this.key = key;
		this.principal = "anonymousUser";
		this.authorities = list;
	}
	public AnonymousAuthenticationFilter(String key, Object principal, List<? extends RolePermission> authorities) {
		super();
		this.key = key;
		this.principal = principal;
		this.authorities = authorities;
	}


	/**
	 * 如果用户到这一步还没有经过认证，将会为这个请求关联一个认证的 token，标识此用户是匿名用户
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		SecurityContext securityContext = SecurityContextStrategy.getContext();
		if (securityContext.getAuthentication() == null) {
			Authentication authentication = createAuthentication(req);
			securityContext.setAuthentication(authentication);
		}
		chain.doFilter(req, resp);
	}
	
	
	/**
	 * 创建一个匿名Token
	 * @param req
	 * @return
	 */
	private Authentication createAuthentication(HttpServletRequest req) {
		AnonymousAuthenticationToken token = new AnonymousAuthenticationToken(key, principal, authorities);
		token.setDetails(new WebAuthenticationDetails(req));
		return token;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	@Override
	public void destroy() {}
}
