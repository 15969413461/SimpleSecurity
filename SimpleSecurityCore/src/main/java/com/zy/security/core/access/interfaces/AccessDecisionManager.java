
package com.zy.security.core.access.interfaces;

import java.util.Collection;

import com.zy.security.core.access.exception.AccessDeniedException;
import com.zy.security.core.access.exception.InsufficientAuthorityException;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-16 周六 下午 14:35:06
* @Description 进行访问资源所需权限与访问用户自身权限的比较
* @version 
*/
public interface AccessDecisionManager {
	/**
	 * 权限集合比较，比较成功则静默结束方法，失败则抛出对应异常
	 * @param authentication - 已认证的Token对象
	 * @param object - 访问资源的主体，一般是Request对象
	 * @param resourceRoles - 被访问资源所需的权限
	 * @throws AccessDeniedException
	 * @throws InsufficientAuthorityException
	 * @return 授权成功后返回访问用户的权限集合
	 */
	Collection<? extends RolePermission> decide(Authentication authentication, Object object,Collection<? extends RolePermission> resourceRoles) 
			throws AccessDeniedException, InsufficientAuthorityException;
}
