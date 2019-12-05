
package com.zy.security.web.session;

import com.zy.security.web.session.interfaces.SessionCache;

/**
* @author zy
* @Date 2019-11-17 周日 下午 16:34:16
* @Description
* @version 
*/
public class NotSessionCache implements SessionCache {
	
	@Override
	public SessionInformation getSessionFromCache(String sessionid) {
		return null;
	}

	@Override
	public boolean putSessionInCache(SessionInformation session) {
		return true;
	}

	@Override
	public boolean removeSessionFromCache(String sessionid) {
		return true;
	}

}
