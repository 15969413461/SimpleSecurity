
package com.zy.security.core.context;

import java.io.Serializable;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-17 周日 上午 00:00:50
* @Description 上下文对象
* @version 
*/
public interface SecurityContext extends Serializable {
	/**
	 * 从ThreadLocal中获得Authentication
	 * @return
	 */
	Authentication getAuthentication();
	/**
	 * 将Authentication保存到ThreadLocal
	 * @param authentication
	 */
	void setAuthentication(Authentication authentication);
}
