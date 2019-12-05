
package com.zy.security.web.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;

/**
* @author zy
* @Date 2019-11-20 周三 上午 00:34:16
* @Description
* @version 
*/
public class CsrfLogoutHandler implements LogoutHandler {
	private CsrfTokenRepository csrfTokenRepository;
	
	public CsrfLogoutHandler(CsrfTokenRepository csrfTokenRepository) {
		super();
		this.csrfTokenRepository = csrfTokenRepository;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		csrfTokenRepository.saveToken(null, request, response);
//		CsrfContextHolder.remove(request);
	}

}
