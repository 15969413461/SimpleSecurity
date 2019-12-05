
package com.zy.security.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.zy.utils.Assert;

/**
* @author zy
* @Date 2019-11-23 周六 下午 14:26:48
* @Description 
* @version 
*/
public class WebUtils {
	
	/**
	 * 从cookie中获得指定cookie值
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request, "HttpServletRequest 不能为null");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
}
