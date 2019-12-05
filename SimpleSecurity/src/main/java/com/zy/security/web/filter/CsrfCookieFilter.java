
package com.zy.security.web.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.session.CookieCsrfTokenRepository;
import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;
import com.zy.security.web.util.WebUtils;

/**
* @author zy
* @Date 2019-11-23 周六 下午 13:35:16
* @Description 从cookie中获得csrf凭证信息
* @version  
*/
@Deprecated
public class CsrfCookieFilter extends CsrfFilter {
	
	public CsrfCookieFilter(CsrfTokenRepository tokenRepository) {
		super(tokenRepository);
	}
	public CsrfCookieFilter(CsrfTokenRepository tokenRepository,AccessDeniedHandler handler) {
		super(tokenRepository,handler);
	}
	public CsrfCookieFilter(CsrfTokenRepository tokenRepository,int maxCsrfTokenCount) {
		super(tokenRepository,maxCsrfTokenCount);
	}
	/**
	 * 默认构造器
	 * @param tokenRepository
	 */
	public CsrfCookieFilter(CsrfTokenRepository tokenRepository,AccessDeniedHandler handler,int maxCsrfTokenCount) {
		super(tokenRepository,handler,maxCsrfTokenCount);
	}
	
	
	@Override
	protected String attemptCstfToken (HttpServletRequest req, HttpServletResponse resp,CsrfToken csrfToken) {
		Cookie cookie = WebUtils.getCookie(req, csrfToken.getCookieName());
		return cookie.getValue();
	}

	@Override
	protected boolean supports(Class<?> csrfTokenRepository) {
		return CookieCsrfTokenRepository.class.isAssignableFrom(csrfTokenRepository);
	}

}
