
package com.zy.security.web.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.session.SessionCsrfTokenRepository;
import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;

/**
* @author zy
* @Date 2019-11-23 周六 下午 13:38:53
* @Description 从请求报文中获得csrf凭证信息
* @version 
*/
public class CsrfRequestMsgFilter extends CsrfFilter {
	
	public CsrfRequestMsgFilter(CsrfTokenRepository tokenRepository, AccessDeniedHandler accessDeniedHandler,
			List<RequestMatcher> list, int maxCsrfTokenCount) {
		super(tokenRepository, accessDeniedHandler, list, maxCsrfTokenCount);
	}
	public CsrfRequestMsgFilter(CsrfTokenRepository tokenRepository) {
		super(tokenRepository);
	}
	public CsrfRequestMsgFilter(CsrfTokenRepository tokenRepository,AccessDeniedHandler handler) {
		super(tokenRepository,handler);
	}
	public CsrfRequestMsgFilter(CsrfTokenRepository tokenRepository,int maxCsrfTokenCount) {
		super(tokenRepository,maxCsrfTokenCount);
	}
	/**
	 * 默认构造器
	 * @param tokenRepository
	 */
	public CsrfRequestMsgFilter(CsrfTokenRepository tokenRepository,AccessDeniedHandler handler,int maxCsrfTokenCount) {
		super(tokenRepository,handler,maxCsrfTokenCount);
	}
	
	@Override
	protected String attemptCstfToken (HttpServletRequest req, HttpServletResponse resp,CsrfToken csrfToken) {
		String actualToken = req.getHeader(csrfToken.getHeaderName());
		if (actualToken == null) {
			actualToken = req.getParameter(csrfToken.getParameterName());
		}
		return actualToken;
	}

	@Override
	protected boolean supports(Class<?> csrfTokenRepository) {
		return SessionCsrfTokenRepository.class.isAssignableFrom(csrfTokenRepository);
	}

}
