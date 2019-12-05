
package com.zy.security.core.token;

import java.util.Collection;

import com.zy.security.core.authentication.interfaces.EraseCredentials;
import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-15 周五 上午 00:23:25
* @Description 根据用户名和密码来组织令牌内容
* @version 
*/
@SuppressWarnings("serial")
public class UsernamePasswordAuthenticationToken extends AbstractAuthentication implements EraseCredentials {
	// 用户名
	private final Object principal;
	// 密码
	private Object credentials;
	
	/**
	 * 创建未认证的UsernamePasswordAuthenticationToken对象
	 * @param principal
	 * @param credentials
	 */
	public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(null);
		
		if( credentials== null || credentials == "" || principal == null || principal == "") {
			throw new IllegalArgumentException("构造器参数不可为null或空串");
		}
		
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}

	/**
	 * 创建已认证的UsernamePasswordAuthenticationToken对象
	 * @param authorities
	 * @param principal
	 * @param credentials
	 */
	public UsernamePasswordAuthenticationToken(Object principal,Object credentials,Collection<? extends RolePermission> authorities) {
		super(authorities);
		
		if( credentials== null || credentials == "" || principal == null || principal == "") {
			throw new IllegalArgumentException("构造器参数不可为null或空串");
		}
		
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public void eraseCredentials() {
		this.credentials = null;
	}

	@Override
	public String toString() {
		return "UsernamePasswordAuthenticationToken [principal=" + principal + ", credentials=" + credentials
				+ ", authorities=" + authorities + ", authenticated=" + authenticated + ", details=" + details + "]";
	}
}
