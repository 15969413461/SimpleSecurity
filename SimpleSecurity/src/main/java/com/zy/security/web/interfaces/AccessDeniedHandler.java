
package com.zy.security.web.interfaces;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author zy
* @Date 2019-11-22 周五 下午 21:35:03
* @Description 访问拒绝处理程序
* @version 
*/
public interface AccessDeniedHandler {
	/**
	 * 访问拒绝处理逻辑
	 * @param request
	 * @param response
	 * @param accessDeniedException
	 * @throws IOException
	 * @throws ServletException
	 */
	void handle(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException;
	
	/**
	 * 设置错误页面uri
	 * @param errPage
	 */
	void setErrorPage(String errPage);
	/**
	 * 设置请求缓存器
	 * @param requestCache
	 */
	void setRequestCache(RequestCache requestCache);
}
