
package com.zy.security.web.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zy.utils.Assert;

/**
* @author zy
* @Date 2019-11-24 周日 下午 13:29:42
* @Description 通过此类组织sessionid与csrf凭证的映射关系，将csrf凭证暴露给外部程序.<br/>
* 限制: 外部程序不可与本类的包名相同，否则外部程序可修改保存的映射关系.
* @version 
*/
public final class CsrfContextHolder {
	private static Map<String,String> csrfMap = new ConcurrentHashMap<>() ;
	
	public static String getCsrfBySessionId(HttpServletRequest request) {
		Assert.notNull(request, "HttpServletRequest 不可为null");
		String sessionId = request.getRequestedSessionId();
		if(sessionId != null) {
			return csrfMap.get(sessionId);
		}
		return "";
	}
	
	protected static void setCsrf(String sessionId,String csrf) {
		if(sessionId == null || csrf == null || sessionId.isEmpty() || csrf.isEmpty()) {
			throw new IllegalArgumentException("参数不可为null或空串");
		}
		csrfMap.put(sessionId, csrf);
	}

	public static void remove(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			csrfMap.remove(session.getId());
		}
	}
	public static void remove(String sessionId) {
		csrfMap.remove(sessionId);
	}
	
	@Override
	public String toString() {
		return "CsrfContextHolder [toString()=" + super.toString() + "]";
	}
}
