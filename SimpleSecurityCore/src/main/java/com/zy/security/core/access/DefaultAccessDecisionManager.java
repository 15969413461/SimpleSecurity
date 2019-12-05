
package com.zy.security.core.access;

import java.util.Collection;

import com.zy.security.core.access.exception.AccessDeniedException;
import com.zy.security.core.access.exception.InsufficientAuthorityException;
import com.zy.security.core.access.interfaces.AccessDecisionManager;
import com.zy.security.core.access.interfaces.SecurityMetadataSource;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-16 周六 下午 15:09:31
* @Description 进行访问资源所需权限与访问用户自身权限的比较
* @version 
*/
public class DefaultAccessDecisionManager implements AccessDecisionManager {

	private SecurityMetadataSource securityMetadataSource;
	
	@Override
	public Collection<? extends RolePermission> decide(Authentication authentication, Object object
			, Collection<? extends RolePermission> resourceRoles)	throws AccessDeniedException, InsufficientAuthorityException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;
		Collection<? extends RolePermission> userRoles = securityMetadataSource.getAttributes(token.getPrincipal());
		return userRoles;
	}

}
