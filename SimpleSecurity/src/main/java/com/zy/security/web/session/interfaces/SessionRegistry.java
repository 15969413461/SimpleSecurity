
package com.zy.security.web.session.interfaces;

import java.util.List;

import com.zy.security.web.session.SessionInformation;

/**
* @author zy
* @Date 2019-11-17 周日 下午 16:06:54
* @Description session相关操作
* @version 
*/
public interface SessionRegistry {
	/**
	 * 获得全部关于此主体的Session
	 * @param principal - 用户的主体标识
	 * @param isExpired - 是否包含过期会话，true则包含
	 * @return
	 */
	List<SessionInformation> getAllSessions(Object principal,boolean isExpired);
	
	/**
	 * 根据Sessionid获得Session对象
	 * @param sessionId
	 * @return
	 */
	SessionInformation getSessionInformation(String sessionId);
	
	/**
	 * 更新指定Session的最后访问时间
	 * @param sessionId
	 */
	void refreshLastRequest(String sessionId);
	
	/**
	 * 创建一个新的Session
	 * @param sessionId
	 * @param principal
	 */
	void registerNewSession(String sessionId, Object principal);
	
	/**
	 * 根据Sessionid删除Session
	 * @param sessionId
	 */
	void removeSessionInformation(String sessionId);
}
