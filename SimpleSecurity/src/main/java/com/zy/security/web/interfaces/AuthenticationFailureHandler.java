
package com.zy.security.web.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.authentication.exception.AuthenticationException;

/**
* @author zy
* @Date 2019-11-20 周三 下午 14:34:44
* @Description 认证失败处理类
* @version 
*/
public interface AuthenticationFailureHandler {

	/**
	 * 转交给异常处理类进行处理
	 * @param req
	 * @param resp
	 * @param e
	 */
	void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e);

}
