
package com.zy.security.core.authentication;

import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.authentication.interfaces.UserDetailsService;

/**
* @author zy
* @Date 2019-11-26 周二 下午 16:26:35
* @Description 默认的UserDetailsService实现，负责获得内置账户的UserDetails对象
* @version 
*/
public class DefaultUserDetailsService implements UserDetailsService {
	private UserDetails userDetails;
	
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
	
	public DefaultUserDetailsService() {
		super();
	}
	public DefaultUserDetailsService(UserDetails userDetails) {
		super();
		this.userDetails = userDetails;
	}


	@Override
	public UserDetails loadUserByUsername(String username) {
		if(this.userDetails.getUsername().equals(username)) {
			return this.userDetails;
		}
		return null;
	}

}
