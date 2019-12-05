
package com.zy.security.web.interfaces;

import javax.servlet.http.HttpServletRequest;

/**
* @author zy
* @Date 2019-11-17 周日 下午 15:29:12
* @Description 存储被打断的请求
* @version 
*/
public interface RequestCache {
	
	/**
	 * 将当前请求存储于缓存中
	 * @param request
	 * @param isSave
	 */
	void saveRequest(HttpServletRequest request);
	
	/**
	 * 从缓存中获得之前被打断的请求
	 * @return
	 */
	String loadRequestForCache(HttpServletRequest request);
	
	/**
	 * 从缓存中删除之前存储的请求
	 * @param request
	 */
	void removeRequestForCache(HttpServletRequest request);
}
