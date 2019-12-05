
package com.zy.security.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.WebAttribute;
import com.zy.security.core.userdetails.WebAuthenticationDetails;
import com.zy.security.web.session.interfaces.SessionAuthenticationStrategy;
import com.zy.security.web.util.DebugEnabled;

/**
* @author zy
* @Date 2019-11-21 周四 下午 14:34:21
* @Description 迁移或创建一个新的会话，将session存储到request中
* @version 
*/
public class ChangeSessionIdAuthenticationStrategy implements SessionAuthenticationStrategy {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public ChangeSessionIdAuthenticationStrategy() {}

	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,HttpServletResponse response) {
		boolean hadSession = request.getSession(false) == null;
		if(hadSession) {
			HttpSession session = request.getSession(true);
			changeSessionId(authentication, session.getId());
			
			if(DebugEnabled.debug) {
				logger.info("创建了一个新会话：{} ,by：{}",session.getId(),authentication.getPrincipal());
			}
			return ;
		}
		
		// 将迁移前的会话id保存到请求中，供之后的session处理策略使用
		request.setAttribute(WebAttribute.CHANGER_SESSION, request.getRequestedSessionId());
		// changeSessionId()方法只改变sessionid，不会改变request所关联的session，即调用此方法前后session的哈希码相同
		String newSsessionId = request.changeSessionId();
		
		changeSessionId(authentication, newSsessionId);
		
		if(DebugEnabled.debug) {
			logger.info("迁移到了新会话：{} ,by：{}",newSsessionId,authentication.getPrincipal());
		}
	}
	
	private void changeSessionId(Authentication authentication,String sessionId) {
		if(authentication instanceof UsernamePasswordAuthenticationToken) {
			WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
			details.setSessionId(sessionId);
		}
	}
}
