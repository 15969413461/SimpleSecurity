
package com.zy.security.web.session;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author zy
* @Date 2019-11-17 周日 下午 16:07:37
* @Description
* @version 
*/
@SuppressWarnings("serial")
public class SessionInformation implements Serializable {
	/** 
	 * session标识
	 */
	private String sessionId;
	/** 
	 * 与此session相关联的用户主体标识
	 */
	private Object principal;
	/** 
	 * 此会话最后一次发送请求的时间
	 */
	private Date lastVisit;
	/** 
	 * 此session是否失效
	 */
	private boolean expire;
	/**
	 * 存储其他个性化数据
	 */
	private Map<String,Object> attribute = new HashMap<>();
	
	
	public Map<String, ?> getAttributes() {
		return attribute;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Object getPrincipal() {
		return principal;
	}
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	public Date getLastVisit() {
		return lastVisit;
	}
	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}
	public boolean isExpire() {
		return expire;
	}
	public void setExpire(boolean expire) {
		this.expire = expire;
	}
	
	public SessionInformation(String sessionId, Object principal, long time, boolean expire) {
		this(sessionId, principal, new Date(time), expire);
	}
	
	
	public SessionInformation(String sessionId, Object principal, Date lastVisit, boolean expire) {
		if(sessionId.isEmpty()) {
			throw new IllegalArgumentException("sessionId不可为null");
		}
		if(principal == null) {
			throw new IllegalArgumentException("principal不可为null");
		}
		if(lastVisit == null) {
			throw new IllegalArgumentException("lastVisit不可为null");
		}
		this.sessionId = sessionId;
		this.principal = principal;
		this.lastVisit = lastVisit;
		this.expire = expire;
	}
	
	public Object getAttribute(String name) {
		return attribute.get(name);
	}
	
	public Object setAttribute(String name,Object value) {
		return attribute.put(name, value);
	}
	
	public Object removeAttribute(String key) {
		return this.attribute.remove(key);
	}
	
	/**
	 * 以当前数据更新Session的最后访问时间
	 */
	public SessionInformation refreshLastRequest() {
		this.lastVisit = new Date();
		return this;
	}
	/**
	 * 更新SessionInformation 的 Attribute之中指定的属性值
	 */
	public Object refreshAttribute(String key,Object refreshVal) {
		// key相同则value覆盖
		return this.setAttribute(key, refreshVal);
	}
	
}
