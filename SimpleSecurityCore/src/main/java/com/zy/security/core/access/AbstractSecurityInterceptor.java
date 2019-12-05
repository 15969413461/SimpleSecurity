
package com.zy.security.core.access;

import java.util.Collection;

import com.zy.security.core.access.exception.AccessDeniedException;
import com.zy.security.core.access.exception.InsufficientAuthorityException;
import com.zy.security.core.access.interfaces.AccessDecisionManager;
import com.zy.security.core.access.interfaces.SecurityMetadataSource;
import com.zy.security.core.context.SecurityContextHandler;
import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.context.SecurityContextThreadLocalHandler;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.utils.Assert;

/**
 * @author zy
 * @Date 2019-11-16 周六 下午 22:02:46
 * @Description 权限控制抽象类
 * @version
 */
public abstract class AbstractSecurityInterceptor {
	// 获取权限集合操作类
	private SecurityMetadataSource securityMetadataSource;
	// 授权控制器
	private AccessDecisionManager accessDecisionManager;

	public void beforeInvocation(Object object) throws AccessDeniedException, InsufficientAuthorityException {
		Assert.notNull(object, "调用者必须存在");
		
		if (!supports(object.getClass())) {
			throw new IllegalArgumentException("不支持处理此类,by: " + object);
		}

		// 存储的是从外部加载的权限数据
		Collection<? extends RolePermission> attributes = this.securityMetadataSource.getAttributes(object);

		if (attributes == null || attributes.isEmpty()) {
			throw new AccessDeniedException("获取权限失败,by: " + object);
		}
		// 获得Authentication
		Authentication authenticate = authenticateIfRequired();

		Collection<? extends RolePermission> userRoles = this.accessDecisionManager.decide(authenticate, object, attributes);
		
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authenticate;
		token.setAuthorities(userRoles);
		
		finallyInvocation(token);
	}

	/**
	 * 从ThreadLocal中获得Authentication对象，根据配置的验证策略决定是否再进行身份验证
	 * 
	 * @return
	 */
	public Authentication authenticateIfRequired() {
		return SecurityContextStrategy.getContext().getAuthentication();
	}
	
	/**
	 * 保存token
	 * @param token
	 */
	protected void finallyInvocation(Authentication token) {
		SecurityContextHandler handler = new SecurityContextThreadLocalHandler();
		handler.getContext().setAuthentication(token);
	}
	
	public abstract boolean supports(Class<?> clazz);
}
