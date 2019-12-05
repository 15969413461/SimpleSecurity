
package com.zy.security.web.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.util.WebUtils;

/**
* @author zy
* @Date 2019-11-18 周一 下午 15:29:32
* @Description 与用户cookie相关联实现csrf防御（csrf凭证存储于cookie中），容易发生cookie盗用
* @version 
*/
@Deprecated
public class CookieCsrfTokenRepository extends AbstractCsrfTokenRepository {
	private String cookiePath;
	
	/**
	 * 构造器
	 */
	public CookieCsrfTokenRepository() {
		super();
	}
	/**
	 * 构造器
	 * @param cookiePath - 设置cookie的有效范围
	 * @param cookieName - 保存的cookie名称
	 */
	public CookieCsrfTokenRepository(String cookiePath,String cookieName) {
		super(cookieName);
		this.cookiePath = cookiePath;
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		String tokenValue = token == null ? "" : token.getToken();
		Cookie cookie = new Cookie(this.headerName, tokenValue);
		
		// 指示浏览器是否应仅使用安全协议（如HTTPS或SSL）发送cookie。
		cookie.setSecure(request.isSecure());

		if (this.cookiePath != null) {
				cookie.setPath(this.cookiePath);
		} else {
				cookie.setPath(this.getRequestContext(request));
		}
		if (token == null) {
			cookie.setMaxAge(0);
		}
		else {
			// 退出浏览器则删除cookie
			cookie.setMaxAge(-1);
		}

		response.addCookie(cookie);
	}

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, this.headerName);
		if (cookie == null) {
			return null;
		}
		String token = cookie.getValue();
		if (token != null) {
			return null;
		}
		return new DefaultCsrfToken(this.headerName, this.parameterName, token);
	}
	
	/**
	 * 获得当前请求的根路径
	 * @param request
	 * @return
	 */
	private String getRequestContext(HttpServletRequest request) {
		String contextPath = request.getRequestURI();
		return contextPath.length() > 0 ? contextPath : "/";
	}

	@Override
	public boolean refreshSessionInformationAttrVal(HttpServletRequest request) {
		// TODO 自动生成的方法存根
		return false;
	}
}
