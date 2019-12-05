
package com.zy.security.web.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.WebAuthenticationDetails;
import com.zy.security.web.util.AnyRequestMapping;

/**
* @author zy
* @Date 2019-11-20 周三 下午 14:45:40
* @Description
* @version 
*/
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private String usernameParameter;
	private String passwordParameter;
	private boolean postOnly = true;
	
	
	/**
	 * 使用默认的参数名
	 * @param loginUrl
	 */
	public UsernamePasswordAuthenticationFilter(String loginUrl) {
		super(loginUrl);
	}
	public UsernamePasswordAuthenticationFilter(String loginUrl, String usernameParameter, String passwordParameter) {
		super(loginUrl);
		this.usernameParameter = usernameParameter;
		this.passwordParameter = passwordParameter;
	}
	public UsernamePasswordAuthenticationFilter(AnyRequestMapping loginUrl) {
		super(loginUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationException("不支持此类型Http认证请求: " + request.getMethod());
		}
		String username = request.getParameter(this.usernameParameter);
		String password = request.getParameter(this.passwordParameter);
		
		username = username==null?"":username;
		password = password==null?"":password;
		
		if(username.isEmpty() || password.isEmpty()) {
			throw new IllegalArgumentException("请求参数携带不充分.by: username="+username+" ,password="+password);
		}
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		
		setDetails(request, token);
		
		return super.authenticationManager.authenticate(token);
	}

	/**
	 * 置有关身份验证请求的其他详细信息，比如：IP、证书序列号
	 * @param request
	 * @param authRequest
	 */
	private void setDetails(HttpServletRequest request,
			UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(new WebAuthenticationDetails(request));
	}
	
	
	public String getUsernameParameter() {
		return usernameParameter;
	}
	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}
	public String getPasswordParameter() {
		return passwordParameter;
	}
	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}
	public boolean isPostOnly() {
		return postOnly;
	}
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
}
