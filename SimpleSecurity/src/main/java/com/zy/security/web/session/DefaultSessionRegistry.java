
package com.zy.security.web.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.zy.security.web.session.interfaces.SessionCache;
import com.zy.security.web.session.interfaces.SessionRegistry;

/**
* @author zy
* @Date 2019-11-17 周日 下午 16:26:32
* @Description 默认的Session创建和操作逻辑
* @version 
*/
public class DefaultSessionRegistry implements SessionRegistry {
	// session缓存
	private SessionCache sessionCache;
	/** 
	 * <principal:Object,SessionIdSet>
	 */
	private final Map<Object, Set<String>> principals;
	
	/**
	 * <sessionId:Object,SessionInformation> 
	 */
	private final Map<String, SessionInformation> sessions;
	
	public DefaultSessionRegistry() {
		this.principals = new ConcurrentHashMap<>();
		this.sessions = new ConcurrentHashMap<>();
//		this.sessionCache = new NotSessionCache();
	}
	public DefaultSessionRegistry(ConcurrentMap<Object, Set<String>> principals,
			Map<String, SessionInformation> sessionIds) {
		super();
//		this.sessionCache = new NotSessionCache();
		this.principals = principals;
		this.sessions = sessionIds;
	}
	public DefaultSessionRegistry(SessionCache sessionCache, ConcurrentMap<Object, Set<String>> principals,
			Map<String, SessionInformation> sessionIds) {
		super();
		this.sessionCache = sessionCache;
		this.principals = principals;
		this.sessions = sessionIds;
	}

	
	public SessionCache getSessionCache() {
		return sessionCache;
	}
	public void setSessionCache(SessionCache sessionCache) {
		this.sessionCache = sessionCache;
	}
	
	
	@Override
	public List<SessionInformation> getAllSessions(Object principal, boolean isExpired) {
		if(principal.toString() == "" || principal == null) {
			throw new IllegalArgumentException("principal参数无效");
		}
		Set<String> set = principals.get(principal);
		
		List<SessionInformation> list = new ArrayList<>(set.size());
		
		for (String sessionId : set) {
			SessionInformation information = sessions.get(sessionId);
			
			if (information == null) {
				continue;
			}
			
			if(isExpired || !information.isExpire()) {
					list.add(information);
			}
		}
		
		return list;
	}

	@Override
	public SessionInformation getSessionInformation(String sessionId) {
		if(sessionId != null && !sessionId.isEmpty() ) {
			return sessions.get(sessionId);
		}
		return null;
	}

	@Override
	public void refreshLastRequest(String sessionId) {
		if(!sessionId.isEmpty()) {
			sessions.get(sessionId).refreshLastRequest();
			// 更新缓存中的Session信息
//			this.sessionCache.putSessionInCache(sessionInformation);
		}
	}

	@Override
	public void registerNewSession(String sessionId, Object principal) {
		SessionInformation information = sessions.get(sessionId);
		if(information != null) {
			return ;
		}
		
		SessionInformation sessionInformation = new SessionInformation(sessionId, principal, System.currentTimeMillis(), false);
		sessions.put(sessionId, sessionInformation);
		
		Set<String> set = principals.get(principal);

		if (set == null) {
			set = new CopyOnWriteArraySet<>();
			principals.putIfAbsent(principal,set);
		}
		set.add(sessionId);
		// 将session存储到缓存中
//		this.sessionCache.putSessionInCache(sessionInformation);
	}

	@Override
	public void removeSessionInformation(String sessionId) {
		if(sessionId.isEmpty() || !sessions.containsKey(sessionId)) {
			return ;
		}
		
		SessionInformation sessionInformation = sessions.get(sessionId);
		if(sessionInformation != null) {
			sessions.remove(sessionId);

			Object principal = sessionInformation.getPrincipal();
			Set<String> set = principals.get(principal);
			set.remove(sessionId);
			// 从缓存中删除
//			this.sessionCache.removeSessionFromCache(sessionId);
		}			
	}
}
