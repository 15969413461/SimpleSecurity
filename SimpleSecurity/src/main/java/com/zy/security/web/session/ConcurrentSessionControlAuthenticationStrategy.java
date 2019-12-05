
package com.zy.security.web.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.WebAttribute;
import com.zy.security.web.session.interfaces.SessionAuthenticationStrategy;
import com.zy.security.web.session.interfaces.SessionRegistry;

/**
* @author zy
* @Date 2019-11-21 周四 下午 14:32:51
* @Description 处理并发会话控制的策略
* @version 
*/
public class ConcurrentSessionControlAuthenticationStrategy implements SessionAuthenticationStrategy {
	private final SessionRegistry sessionStrategy;
	// session的最大并发数
	private int maxSessions;
	private boolean isControl = false;
	
	public int getMaxSessions() {
		return maxSessions;
	}
	public void setMaxSessions(int maxSessions) {
		this.maxSessions = maxSessions;
	}
	public SessionRegistry getSessionStrategy() {
		return sessionStrategy;
	}
	
	public ConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionStrategy, int maxSessions) {
		this(sessionStrategy);
		this.isControl = true;
		if(maxSessions < 1) { // 设置最小值
			this.maxSessions = 1;
			return ; 
		}
		this.maxSessions = maxSessions;
	}
	public ConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionStrategy) {
		if(sessionStrategy == null) {
			throw new IllegalArgumentException("SessionStrategy 不可为null");
		}
		this.sessionStrategy = sessionStrategy;
	}

	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,HttpServletResponse response) {
		if(!isControl) { // 若未设置最大并发数则不作限制
			return;
		}
		
		// 获得全部为过期的会话
		List<SessionInformation> sessions = sessionStrategy.getAllSessions(authentication.getPrincipal(), false);
		int sessionCount = sessions.size();
		
		if(sessionCount < this.maxSessions || this.maxSessions == 0) { // 未超过上限或没有session注册
			return;
		}
		if(sessionCount == this.maxSessions) {
			HttpSession session = request.getSession(false);
			if(session != null) {
				for (SessionInformation sessionInformation : sessions) {
					// 如果当前session未过期则结束此方法
					if( sessionInformation.getSessionId().equals(session.getId()) ) {
						return ;
					}
				}
			}
		}
		// 超出允许会话数	
		allowableSessionsExceeded(sessions,request);
	}
	/**
	 * 寻找最后访问时间最早的session，并将其设为过期
	 * @param sessions
	 * @param num
	 * @param sessionStrategy
	 */
	private void allowableSessionsExceeded(List<SessionInformation> sessions,HttpServletRequest req) {
		// 对for循环中上一个session的引用
		SessionInformation lastInfo = null;
		for (SessionInformation session : sessions) {
			
			/* public boolean before(Date when) {
			        return getMillisOf(this) < getMillisOf(when);
			 }*/
			// 类似于冒泡排序，找寻比自己小的对象
			if (lastInfo == null || session.getLastVisit().before(lastInfo.getLastVisit())) {
				lastInfo = session;
			}
		}

		lastInfo.setExpire(true);
		req.setAttribute(WebAttribute.EXIPRED_SESSION, lastInfo);
		// 在SessionInformation失效时移除相关的csrf信息
		CsrfContextHolder.remove(lastInfo.getSessionId());
	}
}
