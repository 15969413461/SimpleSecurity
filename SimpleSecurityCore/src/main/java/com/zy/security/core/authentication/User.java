
package com.zy.security.core.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.zy.security.core.authentication.interfaces.SecurityProperties;
import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-26 周二 下午 15:30:22
* @Description 内置账户
* @version 
*/
//@ConfigurationProperties(prefix = "")
@SuppressWarnings("serial")
public final class User implements UserDetails,SecurityProperties {
	
	private String username = "user";
	private String password = "123456"/*setPassword()*/;
	
	@SuppressWarnings("unused")
	private String setPassword() {
		String string = UUID.randomUUID().toString();
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Password："+string);
		System.out.println("------------------------------------------------------------------------");
		return string;
	}
	
	private List<RolePermission> roles = new ArrayList<>();
	
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public Collection<? extends RolePermission> getAuthorities() {
		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "DefaultUserDetails [username=" + username + ", password=[protected] , roles=" + roles + "]";
	}
	
	
}
