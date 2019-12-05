
package com.zy.security.web.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author zy
* @Date 2019-11-21 周四 下午 14:07:23
* @Description session失效处理策略
* @version 
*/
public interface InvalidSessionStrategy {

	/**
	 * session失效处理逻辑
	 * @param req
	 * @param resp
	 */
	void onInvalidSessionDetected(HttpServletRequest req, HttpServletResponse resp);

}
