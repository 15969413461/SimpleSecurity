
package com.zy.security.core.context;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-17 周日 上午 00:07:57
* @Description
* @version 
*/
@SuppressWarnings("serial")
public class SecurityContextImpl implements SecurityContext{
	private Authentication authentication;
	
	public SecurityContextImpl(Authentication authentication) {
		super();
		this.authentication = authentication;
	}
	public SecurityContextImpl() {}
	
	@Override
	public Authentication getAuthentication() {
		return this.authentication;
	}

	@Override
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	@Override
	public String toString() {
		return "SecurityContextImpl [authentication=" + authentication + "]";
	}
}
