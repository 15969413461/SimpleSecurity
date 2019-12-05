
package com.zy.security.core.token;

import java.util.Collection;

import com.zy.security.core.userdetails.RolePermission;

/**
 * @author zy
 * @Date 2019-11-15 周五 上午 00:39:42
 * @Description 根据RememberMe Cookie的值来组织令牌内容
 * @version
 */
@SuppressWarnings("serial")
public class RememberMeAuthenticationToken extends AbstractAuthentication {
	// 用户主体标识
	private Object principal;
	// rememberme cookie的值
	private final int keyHash;

	
	/**
	 * 创建未认证的RememberMeAuthenticationToken对象
	 * @param authorities
	 * @param keyHash
	 */
	public RememberMeAuthenticationToken(Collection<? extends RolePermission> authorities, int keyHash) {
		super(authorities);
		this.keyHash = keyHash;
	}

	/**
	 * 创建已认证的RememberMeAuthenticationToken对象
	 * @param key - rememberMe cookie的值
	 * @param principal - 用户主体标识
	 * @param authorities - 用户权限集合
	 */
	public RememberMeAuthenticationToken(String key, Object principal,
			Collection<? extends RolePermission> authorities) {
		super(authorities);
		
		if( key== null || key == "" || principal == null || principal == "") {
			throw new IllegalArgumentException("构造器参数不可为null或空串");
		}

		this.keyHash = key.hashCode();
		this.principal = principal;
		setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	public int getKey() {
		return keyHash;
	}

	@Override
	public String toString() {
		return "RememberMeAuthenticationToken [principal=" + principal + ", keyHash=" + keyHash + ", authorities="
				+ authorities + ", authenticated=" + authenticated + ", details=" + details + "]";
	}
}
