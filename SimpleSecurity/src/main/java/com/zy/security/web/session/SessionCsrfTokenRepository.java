
package com.zy.security.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.session.interfaces.SessionRegistry;

/**
* @author zy
* @Date 2019-11-18 周一 下午 15:29:08
* @Description 与用户会话相关联实现csrf防御（csrf凭证存储于SessionInformation对象中）
* @version 
*/
public class SessionCsrfTokenRepository extends AbstractCsrfTokenRepository {
	
	private SessionRegistry sessionStrategy;
	
	/**
	 * 默认构造器
	 */
	public SessionCsrfTokenRepository() {
		super();
	}
	public SessionCsrfTokenRepository(String parameterName, String headname) {
		super(parameterName, headname);
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		SessionInformation sessionInformation = getSessionInformationForRequest(request);
		if(sessionInformation != null) {
			if(token == null) { // 删除csrf相关信息
				sessionInformation.removeAttribute(this.getAttributeName());
				CsrfContextHolder.remove(sessionInformation.getSessionId());
			}else { // 存储CsrfToken
				sessionInformation.setAttribute(this.getAttributeName(), token);
				CsrfContextHolder.setCsrf(sessionInformation.getSessionId(),token.getToken());
			}
		}
	}
	
	private SessionInformation getSessionInformationForRequest(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			return sessionStrategy.getSessionInformation(session.getId());
		}
		return null;
	}
	
	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		SessionInformation sessionInformation = getSessionInformationForRequest(request);
		if(sessionInformation != null) {
			return (CsrfToken) sessionInformation.getAttribute(getAttributeName());
		}
		return null;
	}
	
	@Override
	public boolean refreshSessionInformationAttrVal(HttpServletRequest request){
		SessionInformation sessionInformation = getSessionInformationForRequest(request);
		if(sessionInformation == null) {
			return false;
		}
		
		Object attribute = sessionInformation.refreshAttribute(this.attributeName, super.createNewToken());
		if(attribute != null) {
			sessionInformation.getSessionId();
			return true;
		}
		return false;
	}

	public SessionRegistry getSessionStrategy() {
		return sessionStrategy;
	}
	public void setSessionStrategy(SessionRegistry sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}
}
