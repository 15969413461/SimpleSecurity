
package com.zy.security.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.WebAttribute;
import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;
import com.zy.security.web.session.interfaces.SessionAuthenticationStrategy;
import com.zy.security.web.util.DebugEnabled;
import com.zy.utils.Assert;

/**
 * @author zy
 * @Date 2019-11-17 周日 下午 16:14:30
 * @Description 认证成功后的csrf相关操作
 * @version
 */
public class CsrfAuthenticationStrategy implements SessionAuthenticationStrategy {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// Csrf凭证操作接口
	private CsrfTokenRepository csrfTokenRepository;

	public CsrfAuthenticationStrategy(CsrfTokenRepository csrfTokenRepository) {
		super();
		Assert.notNull(csrfTokenRepository, "CsrfTokenRepository 不可为null");
		this.csrfTokenRepository = csrfTokenRepository;
	}

	/**
	 * 身份验证成功后，将csrf保存到Authentication对象的details对象中，并设置请求相关内容
	 */
	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) {
		// 失效的SessionInformation对象
		SessionInformation info = (SessionInformation) request.getAttribute(WebAttribute.EXIPRED_SESSION);
		if (info != null) {
			CsrfContextHolder.remove(info.getSessionId());
		}
		// 迁移前的会话id
		String oldSessionId = (String) request.getAttribute(WebAttribute.CHANGER_SESSION);
		if (oldSessionId != null) {
			CsrfContextHolder.remove(oldSessionId);
		}

		CsrfToken newToken = this.csrfTokenRepository.generateToken();
		
		if(DebugEnabled.debug) {
			logger.info("认证成功后关联的CsrfToken：{}", newToken.getToken());
		}
		
		this.csrfTokenRepository.saveToken(newToken, request, response);
	}

}
