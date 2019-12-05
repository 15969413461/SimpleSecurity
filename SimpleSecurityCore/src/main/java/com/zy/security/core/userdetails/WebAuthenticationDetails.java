
package com.zy.security.core.userdetails;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
* @author zy
* @Date 2019-11-20 周三 下午 15:10:20
* @Description 封装Authentication的Details信息
* @version 
*/
@SuppressWarnings("serial")
public class WebAuthenticationDetails implements Serializable {
	private String remoteAddress;
	private String sessionId;
	
	/**
	 * 根据request对象获得IP和SessionID信息
	 * @param request
	 */
	public WebAuthenticationDetails(HttpServletRequest request) {
		this.remoteAddress = request.getRemoteAddr();

		HttpSession session = request.getSession(false);
		this.sessionId = (session != null) ? session.getId() : null;
	}
	
	
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
	@Override
	public String toString() {
		return "WebAuthenticationDetails [remoteAddress=" + remoteAddress + ", sessionId=" + sessionId + "]";
	}
}
