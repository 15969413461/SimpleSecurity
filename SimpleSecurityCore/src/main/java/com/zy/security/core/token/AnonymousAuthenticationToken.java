
package com.zy.security.core.token;

import java.util.Collection;

import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-15 周五 上午 01:01:30
* @Description 标识匿名用户
* <p>
* 在SpringSecurity中，会话第一次请求在未进行身份验证和Remember Cookie验证或以上验证均失败时，
* 会被AnonymousAuthenticationFilter所拦截，然后为此请求关联一个已认证的AnonymousAuthenticationToken对象
* ，接着将请求转交给下一个过滤器。
* <p/>
* @version 
*/
@SuppressWarnings("serial")
public class AnonymousAuthenticationToken extends AbstractAuthentication {
	// 用户主体标识
	private final Object principal;
	/*
	 * UUID.randomUUID().toString()
	 * 在SpringSecurity中，有AnonymousConfigurer类构建，实例化AnonymousAuthenticationFilter时保存为其成员变量，
	 * 在需要为请求关联AnonymousAuthenticationToken对象时，将此值传递给匿名Token
	 */
	private Integer keyHash;

	/**
	 * 创建已认证的 AnonymousAuthenticationToken 对象
	 * @param key
	 * @param principal
	 * @param authorities
	 */
	public AnonymousAuthenticationToken(String key, Object principal,Collection<? extends RolePermission> authorities) {
		super(authorities);
		
		if( key== null || key == "" || principal == null || principal == "") {
			throw new IllegalArgumentException("构造器参数不可为null或空串");
		}
		
		this.principal = principal;
		this.keyHash = key.hashCode();
		super.setAuthenticated(true);
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyHash == null) ? 0 : keyHash.hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnonymousAuthenticationToken other = (AnonymousAuthenticationToken) obj;
		if (keyHash == null) {
			if (other.keyHash != null)
				return false;
		} else if (!keyHash.equals(other.keyHash))
			return false;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		return true;
	}

	public Integer getKey() {
		return this.keyHash;
	}

	@Override
	public String toString() {
		return "AnonymousAuthenticationToken [principal=" + principal + ", keyHash=" + keyHash + ", authorities="
				+ authorities + ", authenticated=" + authenticated + ", details=" + details + "]";
	}
	
	
}
