
package com.zy.security.web.logout;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-19 周二 上午 00:53:20
* @Description 退出成功之后进行的处理
* @version 
*/
public interface LogoutSuccessHandler {

	/**
	 * 将请求重定向到指定的uri上
	 * @param request
	 * @param response
	 * @param authentication
	 */
	void onLogoutSuccess(ServletRequest request, ServletResponse response, Authentication authentication);

}
