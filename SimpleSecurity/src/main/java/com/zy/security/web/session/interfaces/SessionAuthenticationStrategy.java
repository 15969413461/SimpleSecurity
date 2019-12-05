
package com.zy.security.web.session.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
/**
* @author zy
* @Date 2019-11-17 周日 下午 15:52:48
* @Description 认证成功后session处理策略
* @version 
*/
public interface SessionAuthenticationStrategy {
	void onAuthentication(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response);
}
