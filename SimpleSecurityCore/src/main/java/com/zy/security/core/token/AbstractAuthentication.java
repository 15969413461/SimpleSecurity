
package com.zy.security.core.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-13 周三 下午 10:20:53
* @Description 封装Token凭证的共有属性和方法
* @version 
*/
@SuppressWarnings("serial")
public abstract class AbstractAuthentication implements Authentication {
	// 用户权限集合
	protected Collection<? extends RolePermission> authorities;
	// 是否已经过验证
	protected boolean authenticated;
	// 存储有关身份验证请求的其他详细信息，比如：IP、证书序列号
	protected Object details;
	
	/**
	 * 检查权限集合有效性，然后将权限集合转换为只读集合
	 * @param authorities2
	 */
	public AbstractAuthentication(Collection<? extends RolePermission> authorities) {
		if(authorities == null) {
			this.authorities = Collections.emptyList();
			return ;
		}
		
		for (RolePermission rolePermission : authorities) {
			if(rolePermission == null) {
				throw new IllegalArgumentException("用户权限集合中的RolePermission对象不能为null");
			}
		}
	}
	/**
	 * 将权限集合转变为只读集合,在验证成功返回到上层调用方法时被调用。<br/>
	 * 必须保证权限集合不会被修改
	 * @param authorities
	 */
	public void  onlyReadToAuthorities(Collection<? extends RolePermission> authorities) {
		List<RolePermission> list = new ArrayList<>();
		list.addAll(authorities);
		
		// 将权限集合转变为只读集合
		this.authorities = Collections.unmodifiableList(list);
	}
	public Collection<? extends RolePermission> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<? extends RolePermission> userRoles) {
		this.authorities = userRoles;
	}
	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}
	protected void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}
	@Override
	public Object getDetails() {
		return this.details;
	}
	@Override
	public Object setDetails(Object details) {
		return this.details = details;
	}
}
