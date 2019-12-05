
package com.zy.security.web.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.access.exception.AccessDeniedException;
import com.zy.security.core.access.exception.InsufficientAuthorityException;
import com.zy.security.core.access.interfaces.SecurityMetadataSource;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.AnonymousAuthenticationToken;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.RememberMeAuthenticationToken;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.web.config.subject.Authorization;
import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.Authorize;
import com.zy.security.web.util.DebugEnabled;

/**
 * @author zy
 * @Date 2019-12-02 周一 上午 00:08:49
 * @Description 访问管理的Filter实现
 * @version
 */
public class AccessManagementFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 访问拒绝处理程序
	private AccessDeniedHandler accessDeniedHandler;
	// 存储进行权限限制的uri  [AnyRequestMapping:Authorization]
	private Map<RequestMatcher, Authorization> authorizedUrlMap;
	// 获取权限集合操作类
	private SecurityMetadataSource securityMetadataSource;
	
	
	public AccessManagementFilter(AccessDeniedHandler accessDeniedHandler,
			Map<RequestMatcher, Authorization> authorizedUrlMap, SecurityMetadataSource securityMetadataSource) {
		super();
		this.accessDeniedHandler = accessDeniedHandler;
		this.authorizedUrlMap = authorizedUrlMap;
		this.securityMetadataSource = securityMetadataSource;
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		for (RequestMatcher mapping : authorizedUrlMap.keySet()) {
			if (mapping.match(req)) {
				try {
					Screening(req,resp,authorizedUrlMap.get(mapping));
					
					if (DebugEnabled.debug) {
						logger.info("验证通过！请求：'{}' \t，类型'{}'",req.getRequestURI(),req.getMethod());
					}
					break ;
				} catch (AccessDeniedException e) {
					if (DebugEnabled.debug) {
						logger.info("拒绝访问！请求：'{}' \t，类型'{}'",req.getRequestURI(),req.getMethod());
					}
					
					accessDeniedHandler.handle(req, resp);
					return;
				} catch (InsufficientAuthorityException e) {
					if (DebugEnabled.debug) {
						logger.info("权限不足！请求：'{}' \t，类型'{}'进行权限验证",req.getRequestURI(),req.getMethod());
					}
					
					accessDeniedHandler.handle(req, resp);
					return;
//					e.printStackTrace();
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void Screening(HttpServletRequest req,HttpServletResponse resp,Authorization auth)
			throws AccessDeniedException, InsufficientAuthorityException {
		switch (auth.getAuthorizeAttr()) {
			case Authorize.role:				role(req, resp, auth);break;
			case Authorize.authenticated:		authenticated(req,resp);break;
			case Authorize.anonymous:			anonymous(req,resp);break;
			case Authorize.permitAll:			break;
			case Authorize.rememberMe:			rememberMe(req, resp);break;
			case Authorize.fullyAuthenticated:	fullyAuthenticated(req, resp);break;
			case Authorize.denyAll:				throw new AccessDeniedException("此url任何人都不允许访问!");
	
			default:
				logger.error("额外的权限访问控制类别.by：{}",auth);
		}
	}
	
	public void anonymous(HttpServletRequest req,HttpServletResponse resp) 
			throws AccessDeniedException, InsufficientAuthorityException{
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		if( !(authentication instanceof AnonymousAuthenticationToken) ) {
			throw new AccessDeniedException("此url只可匿名用户访问!");
		}
		
	}
	
	public void authenticated(HttpServletRequest req,HttpServletResponse resp) 
			throws AccessDeniedException, InsufficientAuthorityException{
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		if( authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
			return ;
		}
		throw new AccessDeniedException("此url只可经过身份验证的用户访问!");
	}
	
	public void rememberMe(HttpServletRequest req,HttpServletResponse resp) 
			throws AccessDeniedException, InsufficientAuthorityException{
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		if( !(authentication instanceof RememberMeAuthenticationToken) ) {
			throw new AccessDeniedException("此url只可已记住的用户访问!");
		}
	}
	
	public void fullyAuthenticated(HttpServletRequest req,HttpServletResponse resp) 
			throws AccessDeniedException, InsufficientAuthorityException{
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		if(authentication.isAuthenticated() && !(authentication instanceof RememberMeAuthenticationToken)
				&& authentication instanceof AnonymousAuthenticationToken ) {
			throw new AccessDeniedException("此url只可已通过身份验证且未被“记住”的用户访问!");
		}
	}
	
	public void role(HttpServletRequest req,HttpServletResponse resp,Authorization auth) 
			throws AccessDeniedException, InsufficientAuthorityException{
		// 从ThreadLocal中获得用户的认证信息
		Authentication authentication = SecurityContextStrategy.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken) {
			if(DebugEnabled.debug) {
				logger.info("匿名用户拒绝访问！请求：'{}' \t，类型'{}'",req.getRequestURI(),req.getMethod());
			}
			throw new AccessDeniedException("匿名用户拒绝访问!");
		}
		
		// 通过用户主体标识获得权限集合
		Collection<RolePermission> attributes = this.securityMetadataSource.getAttributes(authentication.getPrincipal());
		// 配置类与注解存储的权限集合
		List<RolePermission> uriRoles = auth.writeRolePermission();
		
		boolean contains;
		switch (auth.getTag()) {
			case 0:	// 与
				/*
				 * 用户的权限集合是否包含指定集合的所有元素
				 * containsAll底层采用for循环调用equals方法进行两对象比较，且equals两侧的对象发生了置换
				 */
				contains = uriRoles.containsAll(attributes);
				if(!contains) { // 二者不是全集
					throw new InsufficientAuthorityException();
				}
				break;
			case 1:	// 或	
				if(attributes.size() < uriRoles.size()) {
					for (RolePermission userRole : attributes) {
						for (RolePermission uriRole : uriRoles) {
							if(userRole.compare(uriRole)) {
								return ;
							}
							throw new InsufficientAuthorityException();
						}
					}
				}else {
					for (RolePermission uriRole : uriRoles) {
						for (RolePermission userRole : attributes) {
							if(userRole.compare(uriRole)) {
								return ;
							}
							throw new InsufficientAuthorityException();
						}
					}
				}
				break;
			case 2:	// 非	
				if(attributes.size() < uriRoles.size()) {
					for (RolePermission userRole : attributes) {
						for (RolePermission uriRole : uriRoles) {
							if(userRole.compare(uriRole)) {
								throw new AccessDeniedException();
							}
						}
					}
				}else {
					for (RolePermission uriRole : uriRoles) {
						for (RolePermission userRole : attributes) {
							if(userRole.compare(uriRole)) {
								throw new AccessDeniedException();
							}
						}
					}
				}
				break;
			
		default:
			logger.error("额外的权限验证规则.by：{}",auth);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	@Override
	public void destroy() {}

	
	public AccessDeniedHandler getAccessDeniedHandler() {
		return accessDeniedHandler;
	}
	public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
		this.accessDeniedHandler = accessDeniedHandler;
	}
	public Map<RequestMatcher, Authorization> getAuthorizedUrlMap() {
		return authorizedUrlMap;
	}
	public void setAuthorizedUrlMap(Map<RequestMatcher, Authorization> authorizedUrlMap) {
		this.authorizedUrlMap = authorizedUrlMap;
	}
	public SecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}
	public void setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}
}
