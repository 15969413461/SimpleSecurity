
package com.zy.security.web.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-20 周三 下午 14:34:35
* @Description 认证成功处理类
* @version 
*/
public interface AuthenticationSuccessHandler {

	/**
	 * 查看请求缓存，若有则重定向到此请求的uri下,无则跳转的默认配置的uri下
	 */
	void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth);

}
