
package com.zy.security.core.authentication.rememberme;

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
public class NulltRememberMeService extends AbstractRememberMeService {

	public NulltRememberMeService() {
		super();
	}

	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) {}

	@Override
	public boolean supports(Class<?> clz) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(clz);
	}

}
