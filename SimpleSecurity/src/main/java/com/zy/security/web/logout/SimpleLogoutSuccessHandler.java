
package com.zy.security.web.logout;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-20 周三 上午 00:17:54
* @Description 重定向到指定的uri
* @version 
*/
public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {
	private String defaultTargetUrl;
	
	public SimpleLogoutSuccessHandler(String defaultTargetUrl) {
		if(defaultTargetUrl == null || defaultTargetUrl.isEmpty()) {
//			throw new IllegalArgumentException("构造器参数不可为null或空串");
			this.defaultTargetUrl = "/login?logout=true";
		}
		this.defaultTargetUrl = defaultTargetUrl;
	}

	@Override
	public void onLogoutSuccess(ServletRequest request, ServletResponse response, Authentication authentication) {
		if(!response.isCommitted()) {
			try {
				((HttpServletResponse) response).sendRedirect(this.defaultTargetUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}
	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}
}
