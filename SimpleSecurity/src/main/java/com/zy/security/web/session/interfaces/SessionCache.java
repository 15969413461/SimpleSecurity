
package com.zy.security.web.session.interfaces;

import com.zy.security.web.session.SessionInformation;

/**
* @author zy
* @Date 2019-11-17 周日 下午 16:27:57
* @Description session缓存策略
* @version 
*/
public interface SessionCache {
	/**
	 * 从缓存中获得session信息
	 * @param username
	 * @return
	 */
	SessionInformation getSessionFromCache(String sessionid);
	/**
	 * 将session对象保存到缓存中
	 * @param user
	 * @return
	 */
	boolean putSessionInCache(SessionInformation session);
	/**
	 * 从缓存中删除保存的session信息
	 * @param username
	 */
	boolean removeSessionFromCache(String sessionid);
}
