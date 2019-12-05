
package com.zy.security.web.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.context.SecurityContextStrategy;
import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-20 周三 上午 00:34:48
* @Description 清空ThreadLocal的认证信息
* @version 
*/
public class SecurityContextLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SecurityContextStrategy.clearRepositoryContext(request);
	}

}
