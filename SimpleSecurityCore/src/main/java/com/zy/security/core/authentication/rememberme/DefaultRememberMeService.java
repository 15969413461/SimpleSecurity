
package com.zy.security.core.authentication.rememberme;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;

/**
* @author zy
* @Date 2019-11-17 周日 下午 13:54:18
* @Description 
* @version 
*/
public class DefaultRememberMeService extends AbstractRememberMeService {

	public DefaultRememberMeService(String rememberMeCookieName, String rememberMeParameter, int tokenValiditySeconds,
			String rememberMeCookiePath) {
		super(rememberMeCookieName, rememberMeParameter, tokenValiditySeconds, rememberMeCookiePath);
	}

	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;
		String principal = token.getPrincipal().toString();
//		String password = token.getCredentials().toString();
		
		// 计算过期时间
		long time = System.currentTimeMillis();
		time += 1000L * super.getTokenValiditySeconds();
		
		StringBuilder builder = new StringBuilder();
		//格式：  zs:1574869570562
		builder.append(principal).append(":").append(time);
		
		// 设置cookie相关属性
		Cookie cookie = super.setCookie(builder.toString(),time);
		
		response.addCookie(cookie);
	}

	@Override
	public boolean supports(Class<?> clz) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(clz);
	}

}
