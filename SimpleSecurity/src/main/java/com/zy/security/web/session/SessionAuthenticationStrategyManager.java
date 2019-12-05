
package com.zy.security.web.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
import com.zy.security.web.session.interfaces.SessionAuthenticationStrategy;

/**
* @author zy
* @Date 2019-11-17 周日 下午 15:54:32
* @Description Session策略调用类，针对于已认证的非匿名用户，囊括了session创建、失效处理、并发控制等
* @version 
*/
public class SessionAuthenticationStrategyManager implements SessionAuthenticationStrategy {
	private List<SessionAuthenticationStrategy> sessionStrategy;
	
	public SessionAuthenticationStrategyManager() {
		sessionStrategy = new ArrayList<>();
	}
	public SessionAuthenticationStrategyManager(List<SessionAuthenticationStrategy> sessionStrategy) {
		if(sessionStrategy == null || sessionStrategy.isEmpty()) {
			throw new IllegalArgumentException("构造参数sessionStrategy不可为null");
		}
		this.sessionStrategy = sessionStrategy;
	}
	
	
	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,HttpServletResponse response) {
		for (SessionAuthenticationStrategy sessionAuthenticationStrategy : sessionStrategy) {
			sessionAuthenticationStrategy.onAuthentication(authentication, request, response);
		}
	}

	public List<SessionAuthenticationStrategy> getSessionStrategy() {
		return sessionStrategy;
	}
	public void setSessionStrategy(List<SessionAuthenticationStrategy> sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}
}
