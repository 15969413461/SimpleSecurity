
package com.zy.security.core.authentication.rememberme;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.authentication.interfaces.UserDetailsChecker;
import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.core.authentication.rememberme.interfaces.RememberMeService;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.RememberMeAuthenticationToken;
import com.zy.security.core.userdetails.WebAuthenticationDetails;
import com.zy.security.core.userdetails.exception.AccountStatusException;

/**
* @author zy
* @Date 2019-11-17 周日 下午 13:29:43
* @Description
* @version 
*/
public abstract  class AbstractRememberMeService implements RememberMeService {
	/** 
	 * 存储用于“记住我”身份验证的令牌的cookie的名称
	 */
	private String rememberMeCookieName;
	/**
	 * 用于指示在登录时记住用户的HTTP(name)参数
	 * <p>如：< ... type="checkbox" name="rememberMe">'<p/> 
	 */
	private String rememberMeParameter;
	/** 
	 * cookie有效期（秒）,默认7天
	 */
	private int tokenValiditySeconds;
	/** 
	 * 在其中可见“记住我”cookie的域名
	 */
	private String rememberMeCookiePath;
	/** 
	 * 设置密钥以标识“记住我”身份验证创建的令牌
	 */
	private String key;
	
	private UserDetailsService userDetailsService;
	private UserDetailsChecker userDetailsChecker;
	// 对其他RememberMeService实现类的引用   - 未实现
	private RememberMeService parent;
	
	
	public AbstractRememberMeService() {
		super();
	}
	public AbstractRememberMeService(String rememberMeCookieName, String rememberMeParameter, int tokenValiditySeconds,
			String rememberMeCookiePath) {
		super();
		this.rememberMeCookieName = rememberMeCookieName;
		this.rememberMeParameter = rememberMeParameter;
		this.tokenValiditySeconds = tokenValiditySeconds;
		this.rememberMeCookiePath = rememberMeCookiePath;
	}

	@Override
	public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue = this.getCookieValue(request);
		
		if (cookieValue == null) {
			return null;
		}
		
		if (cookieValue.length() == 0) {
			Cookie cookie = setCookie(null, 0l);
			if(!response.isCommitted()) {
				response.addCookie(cookie);
			}
			return null;
		}
		
		String[] split = cookieValue.split(":");
		long expired = Long.parseLong(split[1]);
		if(expired - System.currentTimeMillis() < 0) { // cookie 过期
			return null;
		}
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(split[0]);
		try { // UserDetails检查
			this.userDetailsChecker.preCheck(userDetails);;
			this.userDetailsChecker.postCheck(userDetails);
		} catch (AccountStatusException e) {
			e.printStackTrace();
		}
		
		RememberMeAuthenticationToken rememberMeAuthenticationToken =
				new RememberMeAuthenticationToken(key, split[0], userDetails.getAuthorities());
		rememberMeAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
		return rememberMeAuthenticationToken;
	}
	
	/**
	 * 从rememberMe cookie中获得用户身份信息
	 * @param request
	 * @return
	 */
	protected String getCookieValue(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if ((cookies == null) || (cookies.length == 0)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (this.rememberMeCookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	@Override
	public final void loginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		String parame = request.getParameter(rememberMeParameter);
		
		if (parame != null) {
			// 忽略大小写进行比较
			if (parame.equalsIgnoreCase("true") || parame.equalsIgnoreCase("on")) {
				if(supports(authentication.getClass())) { // 判断当前RememberMeService能否适配此Token
					this.onLoginSuccess(request, response, authentication);
				}else {
					if(this.parent != null) {
						this.parent.onLoginSuccess(request, response, authentication);
					}
				}
			}
		}
	}

	protected Cookie setCookie(String str,long expiry) {
		Cookie cookie = new Cookie(rememberMeCookieName, str);
		cookie.setMaxAge((int) expiry);
		cookie.setPath(rememberMeCookiePath);
		return cookie;
	}
	
	
	public String getRememberMeCookieName() {
		return rememberMeCookieName;
	}
	public void setRememberMeCookieName(String rememberMeCookieName) {
		this.rememberMeCookieName = rememberMeCookieName;
	}
	public String getRememberMeParameter() {
		return rememberMeParameter;
	}
	public void setRememberMeParameter(String rememberMeParameter) {
		this.rememberMeParameter = rememberMeParameter;
	}
	public int getTokenValiditySeconds() {
		return tokenValiditySeconds;
	}
	public void setTokenValiditySeconds(int tokenValiditySeconds) {
		this.tokenValiditySeconds = tokenValiditySeconds;
	}
	public String getRememberMeCookiePath() {
		return rememberMeCookiePath;
	}
	public void setRememberMeCookieDomain(String rememberMeCookiePath) {
		this.rememberMeCookiePath = rememberMeCookiePath;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	public UserDetailsChecker getUserDetailsChecker() {
		return userDetailsChecker;
	}
	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}
	public RememberMeService getParent() {
		return parent;
	}
	public void setParent(RememberMeService parent) {
		this.parent = parent;
	}
	public void setRememberMeCookiePath(String rememberMeCookiePath) {
		this.rememberMeCookiePath = rememberMeCookiePath;
	}
}
