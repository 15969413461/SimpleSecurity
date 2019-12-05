
package com.zy.security.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.WebAttribute;
import com.zy.security.web.session.interfaces.SessionAuthenticationStrategy;
import com.zy.security.web.session.interfaces.SessionRegistry;

/**
* @author zy
* @Date 2019-11-17 周日 下午 15:54:32
* @Description Session策略调用类
* @version 
*/
public class RegisterSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
	private SessionRegistry sessionStrategy;

	public SessionRegistry getSessionStrategy() {
		return sessionStrategy;
	}
	public RegisterSessionAuthenticationStrategy(SessionRegistry sessionStrategy) {
		if(sessionStrategy == null) {
			throw new IllegalArgumentException("构造器参数不可为null");
		}
		this.sessionStrategy = sessionStrategy;
	}

	/** 
	 * 根据请求中的Sessionid重新生成一个Session
	 */
	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,HttpServletResponse response) {
		// 失效的SessionInformation对象
		SessionInformation info = (SessionInformation) request.getAttribute(WebAttribute.EXIPRED_SESSION);
		if(info != null) {
			sessionStrategy.removeSessionInformation(info.getSessionId());
		}
		// 迁移前的会话id
		String oldSessionId = (String) request.getAttribute(WebAttribute.CHANGER_SESSION);
		if(oldSessionId != null) {
			sessionStrategy.removeSessionInformation(oldSessionId);
		}
		sessionStrategy.registerNewSession(request.getSession().getId(),authentication.getPrincipal());
	}
}
