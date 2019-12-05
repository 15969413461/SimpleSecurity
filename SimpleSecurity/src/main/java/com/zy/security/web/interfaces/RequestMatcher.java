
package com.zy.security.web.interfaces;

import javax.servlet.http.HttpServletRequest;

/**
* @author zy
* @Date 2019-11-19 周二 下午 13:53:00
* @Description uri比较
* @version 
*/
public interface RequestMatcher {
	/**
	 * uri比较，this的uri需囊括参数的uri <br/>
	 * 即：a：/a/** ,  b：/a/b/** <br/>
	 * a.match(b) = true --》  a的uri囊括b的uri
	 * @param mapping
	 * @return
	 */
	public boolean match(HttpServletRequest request);
}
