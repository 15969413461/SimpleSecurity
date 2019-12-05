
package com.zy.security.web.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-19 周二 上午 00:53:11
* @Description 退出操作处理
* @version 
*/
public interface LogoutHandler {

	/**
	 * 退出操作处理
	 * @param request
	 * @param response
	 * @param authentication
	 */
	void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

}
