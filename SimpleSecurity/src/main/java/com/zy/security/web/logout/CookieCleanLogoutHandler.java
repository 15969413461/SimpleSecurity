
package com.zy.security.web.logout;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-20 周三 上午 00:27:04
* @Description
* @version 
*/
public class CookieCleanLogoutHandler implements LogoutHandler {
	
	// JSESSION、rememberMe...
	private List<String> cookieNames;
	
	public CookieCleanLogoutHandler(List<String> cookieNames) {
		super();
		this.cookieNames = cookieNames;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if(cookieNames == null) {
			return ;
		}
		for (String cookieName : cookieNames) {
			Cookie cookie = new Cookie(cookieName, null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

}
