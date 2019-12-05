
package com.zy.security.web.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.web.interfaces.AuthenticationFailureHandler;

/**
* @author zy
* @Date 2019-11-20 周三 下午 15:51:07
* @Description
* @version 
*/
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
	/** 如果身份验证失败，则发送用户的URL.默认为“/login?error” */
	private String failureUrl;
	
	
	public DefaultAuthenticationFailureHandler(String failureUrl) {
		super();
		this.failureUrl = failureUrl;
	}
	public DefaultAuthenticationFailureHandler() {}


	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) {
		if(failureUrl != null && !resp.isCommitted()) {
			try {
				resp.sendRedirect(failureUrl);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	public String getFailureUrl() {
		return failureUrl;
	}
	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}
}
